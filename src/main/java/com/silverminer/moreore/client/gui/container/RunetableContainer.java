package com.silverminer.moreore.client.gui.container;

import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.moreore.common.objects.items.RuneItem;
import com.silverminer.moreore.common.recipe.ModRecipeType;
import com.silverminer.moreore.common.recipe.RuneRecipe;
import com.silverminer.moreore.common.tags.ModTags;
import com.silverminer.moreore.init.ContainerTypesInit;
import com.silverminer.moreore.init.blocks.RuneBlocks;
import com.silverminer.moreore.init.items.RuneItems;
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
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.world.World;

public class RunetableContainer extends AbstractRepairContainer {
	protected static final Logger LOGGER = LogManager.getLogger(RunetableContainer.class);
	private final World world;
	private final List<RuneRecipe> recipes;
	private RuneRecipe recipe;
	public final IInventory runeInv;;
	public final UUID activeUUID;
	private final int invSize;

	public int materialCost;
	private final IntReferenceHolder maximumCost = IntReferenceHolder.single();

	public RunetableContainer(int p_i231590_1_, PlayerInventory inventory) {
		this(p_i231590_1_, inventory, IWorldPosCallable.DUMMY);
	}

	public RunetableContainer(int p_i231591_1_, PlayerInventory inventory, IWorldPosCallable iworldposcallable) {
		super(ContainerTypesInit.RUNE_TABLE.get(), p_i231591_1_, inventory, iworldposcallable);
		this.trackInt(this.maximumCost);
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
		return (this.recipe != null && this.recipe.matches(this.field_234643_d_, this.world))
				|| ((player.abilities.isCreativeMode || player.experienceLevel >= this.maximumCost.get()));
	}

	protected ItemStack func_230301_a_(PlayerEntity player, ItemStack stack) {
		if (!player.abilities.isCreativeMode) {
			player.addExperienceLevel(-this.maximumCost.get());
		}
		stack.onCrafting(player.world, player, stack.getCount());
		this.field_234642_c_.onCrafting(player);
		this.shrinkSlot(0);
		this.shrinkSlot(1, this.materialCost);
		this.field_234644_e_.consume((world, pos) -> {
			world.playEvent(1044, pos, 0);
		});
		return stack;
	}

	private void shrinkSlot(int slot) {
		this.shrinkSlot(slot, 1);
	}

	private void shrinkSlot(int slot, int count) {
		ItemStack itemstack = this.field_234643_d_.getStackInSlot(slot);
		if (itemstack.getItem() == RuneItems.RUNE_GRAY.get()) {
			itemstack.setDamage(itemstack.getDamage() + 1);
			if (itemstack.getDamage() == itemstack.getMaxDamage()) {
				this.field_234643_d_.setInventorySlotContents(slot, ItemStack.EMPTY);
			} else {
				this.field_234643_d_.setInventorySlotContents(slot, itemstack);
			}
			return;
		}
		if (!itemstack.isEmpty() && itemstack.getCount() > count) {
			itemstack.shrink(count);
			this.field_234643_d_.setInventorySlotContents(slot, itemstack);
		} else {
			this.field_234643_d_.setInventorySlotContents(slot, ItemStack.EMPTY);
		}
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
			// Repair logic start
			ItemStack stack0 = this.field_234643_d_.getStackInSlot(0).copy();
			ItemStack stack1 = this.field_234643_d_.getStackInSlot(1).copy();
			if (stack0 != null && stack0.getItem() instanceof RuneItem && stack1 != null) {
				boolean isShard = stack1.getItem().isIn(ModTags.RUNE_SHARDS);
				if (stack1.getItem() == RuneItems.RUNE_GRAY.get() && stack0.getItem() != RuneItems.RUNE_GRAY.get()) {
					if (stack0.getDamage() <= 0) {
						this.field_234642_c_.setInventorySlotContents(0, ItemStack.EMPTY);
						this.maximumCost.set(0);
						return;
					} else {
						this.materialCost = 0;
						this.maximumCost.set(1);
						stack0.setDamage(0);
					}
				} else {
					int l2 = Math.min(stack0.getDamage(),
							isShard ? stack0.getMaxDamage() / 8 : stack0.getMaxDamage() / 4);
					if (l2 <= 0) {
						this.field_234642_c_.setInventorySlotContents(0, ItemStack.EMPTY);
						this.maximumCost.set(0);
						return;
					}
					int i3;
					for (i3 = 0; l2 > 0 && i3 < stack1.getCount(); ++i3) {
						int j3 = stack0.getDamage() - l2;
						stack0.setDamage(j3);
						l2 = Math.min(stack0.getDamage(),
								isShard ? stack0.getMaxDamage() / 8 : stack0.getMaxDamage() / 4);
					}

					this.materialCost = i3;
					this.maximumCost.set(i3);
				}
				itemstack = stack0.copy();
			} else {
				this.materialCost = 1;
				this.maximumCost.set(0);
			}
			// Repair logic end
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
