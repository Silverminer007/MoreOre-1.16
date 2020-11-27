package com.silverminer.moreore.client.gui.container;

import java.util.List;
import java.util.UUID;

import com.silverminer.moreore.common.objects.items.RuneItem;
import com.silverminer.moreore.common.recipe.ModRecipeType;
import com.silverminer.moreore.common.recipe.RuneRecipe;
import com.silverminer.moreore.init.ContainerTypesInit;
import com.silverminer.moreore.init.blocks.RuneBlocks;
import com.silverminer.moreore.util.RuneInventoryRegistry;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.AbstractRepairContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.world.World;

public class RunetableContainer extends AbstractRepairContainer {
	private final World world;
	private final List<RuneRecipe> recipes;
	private RuneRecipe recipe;
	public final IInventory runeInv;;
	public final UUID activeUUID;
	private final int invSize;

	public RunetableContainer(int p_i231590_1_, PlayerInventory inventory) {
		this(p_i231590_1_, inventory, IWorldPosCallable.DUMMY);
	}

	public RunetableContainer(int p_i231591_1_, PlayerInventory inventory, IWorldPosCallable iworldposcallable) {
		super(ContainerTypesInit.RUNE_TABLE.get(), p_i231591_1_, inventory, iworldposcallable);
		this.world = inventory.player.world;
		this.recipes = this.world.getRecipeManager().getRecipesForType(ModRecipeType.RUNES);
		this.activeUUID = inventory.player.getUniqueID();
		this.runeInv = RuneInventoryRegistry.getInventory(this.activeUUID);
		this.invSize = RuneInventoryRegistry.getInventorySize(this.activeUUID);
		for (int i = 0; i < this.invSize; i++) {
			// 176 ist die x breite wo der erste slot beginnt. Mal 22 wegen 18 Slot größe +
			// 4 Abstand
			this.addSlot(new Slot(this.runeInv, i, 177, 8 + i * 22) {
				/**
				 * Check if the stack is allowed to be placed in this slot, used for armor slots
				 * as well as furnace fuel.
				 */
				public boolean isItemValid(ItemStack stack) {
					return stack.getItem() instanceof RuneItem;
				}
			});
		}
	}

	public void onContainerClosed(PlayerEntity playerIn) {
		super.onContainerClosed(playerIn);
		if (this.runeInv instanceof Inventory)
			RuneInventoryRegistry.setInventory(playerIn.getUniqueID(), (Inventory) this.runeInv);
	}

	protected boolean func_230302_a_(BlockState state) {
		return state.isIn(RuneBlocks.RUNETABLE.get());
	}

	protected boolean func_230303_b_(PlayerEntity player, boolean p_230303_2_) {
		return this.recipe != null && this.recipe.matches(this.field_234643_d_, this.world);
	}

	protected ItemStack func_230301_a_(PlayerEntity player, ItemStack stack) {
		stack.onCrafting(player.world, player, stack.getCount());
		this.field_234642_c_.onCrafting(player);
		this.func_234654_d_(0);
		this.func_234654_d_(1);
		this.field_234644_e_.consume((world, pos) -> {
			world.playEvent(1044, pos, 0);
		});
		return stack;
	}

	private void func_234654_d_(int slot) {
		ItemStack itemstack = this.field_234643_d_.getStackInSlot(slot);
		itemstack.shrink(1);
		this.field_234643_d_.setInventorySlotContents(slot, itemstack);
	}

	/**
	 * called when the Anvil Input Slot changes, calculates the new result and puts
	 * it in the output slot
	 */
	public void updateRepairOutput() {
		List<RuneRecipe> list = this.world.getRecipeManager().getRecipes(ModRecipeType.RUNES, this.field_234643_d_,
				this.world);
		if (list.isEmpty()) {
			this.field_234642_c_.setInventorySlotContents(0, ItemStack.EMPTY);
		} else {
			this.recipe = list.get(0);
			ItemStack itemstack = this.recipe.getCraftingResult(this.field_234643_d_);
			this.field_234642_c_.setRecipeUsed(this.recipe);
			this.field_234642_c_.setInventorySlotContents(0, itemstack);
		}

	}

	protected boolean func_241210_a_(ItemStack stack) {
		return this.recipes.stream().anyMatch((recipe) -> {
			return recipe.isValidAdditionItem(stack);
		});
	}

	/**
	 * Called to determine if the current slot is valid for the stack merging
	 * (double-click) code. The stack passed in is null for the initial slot that
	 * was double-clicked.
	 */
	public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
		return slotIn.inventory != this.field_234642_c_ && super.canMergeSlot(stack, slotIn);
	}

	/**
	 * Handle when the stack in slot {@code index} is shift-clicked. Normally this
	 * moves the stack between the player inventory and the other inventory(s).
	 */
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index == 2) {
				if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (index != 0 && index != 1) {
				if (index >= 3 && index < 39) {
					int i = this.func_241210_a_(itemstack) ? 1 : 0;
					if (!this.mergeItemStack(itemstack1, i, 2, false)) {
						if (this.invSize > 0)
							if (!this.mergeItemStack(itemstack1, 39, 38 + this.invSize, false))
								return ItemStack.EMPTY;
					}
				}
			} else if (index >= 39) {
				if (!this.mergeItemStack(this.inventorySlots.get(index).getStack(), 3, 39, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, itemstack1);
		}

		return itemstack;
	}
}
