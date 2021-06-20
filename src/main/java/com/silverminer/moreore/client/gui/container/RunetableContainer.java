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
import com.silverminer.moreore.util.runes.RuneInventoryRegistry;

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
	public final UUID activeUUID;
	public final IInventory runeInv;
	private final int invSize;

	public int materialCost;
	private final IntReferenceHolder maximumCost = IntReferenceHolder.standalone();

	public RunetableContainer(int p_i231590_1_, PlayerInventory inventory) {
		this(p_i231590_1_, inventory, IWorldPosCallable.NULL);
	}

	public RunetableContainer(int p_i231591_1_, PlayerInventory inventory, IWorldPosCallable iworldposcallable) {
		super(ContainerTypesInit.RUNE_TABLE.get(), p_i231591_1_, inventory, iworldposcallable);
		this.addDataSlot(this.maximumCost);
		this.world = inventory.player.level;
		this.recipes = this.world.getRecipeManager().getAllRecipesFor(ModRecipeType.RUNES);
		this.activeUUID = inventory.player.getUUID();
		Inventory inv = RuneInventoryRegistry.getInventory(this.activeUUID);
		this.runeInv = new Inventory(inv.getItem(0).copy(), inv.getItem(1).copy(),
				inv.getItem(2).copy());
		this.invSize = RuneInventoryRegistry.getInventorySize(this.activeUUID);
		for (int i = 0; i < this.invSize; i++) {
			// 176 ist die x breite wo der erste slot beginnt. Mal 22 wegen 18 Slot gr��e +
			// 4 Abstand
			this.addSlot(new Slot(this.runeInv, i, 177, 8 + i * 22) {
				/**
				 * Check if the stack is allowed to be placed in this slot, used for armor slots
				 * as well as furnace fuel.
				 */
				public boolean mayPlace(ItemStack stack) {
					return stack.getItem() instanceof RuneItem;
				}
			});
		}
	}

	public void removed(PlayerEntity playerIn) {
		super.removed(playerIn);
		if (this.runeInv instanceof Inventory)
			RuneInventoryRegistry.setInventory(playerIn, (Inventory) this.runeInv);
	}

	private void shrinkSlot(int slot) {
		this.shrinkSlot(slot, 1);
	}

	private void shrinkSlot(int slot, int count) {
		ItemStack itemstack = this.inputSlots.getItem(slot);
		if (itemstack.getItem() == RuneItems.RUNE_GRAY.get()) {
			itemstack.setDamageValue(itemstack.getDamageValue() + 1);
			if (itemstack.getDamageValue() == itemstack.getMaxDamage()) {
				this.inputSlots.setItem(slot, ItemStack.EMPTY);
			} else {
				this.inputSlots.setItem(slot, itemstack);
			}
			return;
		}
		if (!itemstack.isEmpty() && itemstack.getCount() > count) {
			itemstack.shrink(count);
			this.inputSlots.setItem(slot, itemstack);
		} else {
			this.inputSlots.setItem(slot, ItemStack.EMPTY);
		}
	}

	/**
	 * called when the Anvil Input Slot changes, calculates the new result and puts
	 * it in the output slot
	 */
	public void createResult() {
		List<RuneRecipe> list = this.world.getRecipeManager().getRecipesFor(ModRecipeType.RUNES, this.inputSlots,
				this.world);
		if (list.isEmpty()) {
			this.resultSlots.setItem(0, ItemStack.EMPTY);
		} else {
			this.recipe = list.get(0);
			ItemStack itemstack = this.recipe.assemble(this.inputSlots);
			// Repair logic start
			ItemStack stack0 = this.inputSlots.getItem(0).copy();
			ItemStack stack1 = this.inputSlots.getItem(1).copy();
			if (stack0 != null && stack0.getItem() instanceof RuneItem && stack1 != null) {
				boolean isShard = stack1.getItem().is(ModTags.RUNE_SHARDS);
				if (stack1.getItem() == RuneItems.RUNE_GRAY.get() && stack0.getItem() != RuneItems.RUNE_GRAY.get()) {
					if (stack0.getDamageValue() <= 0) {
						this.resultSlots.setItem(0, ItemStack.EMPTY);
						this.maximumCost.set(0);
						return;
					} else {
						this.materialCost = 0;
						this.maximumCost.set(1);
						stack0.setDamageValue(0);
					}
				} else {
					int l2 = Math.min(stack0.getDamageValue(),
							isShard ? stack0.getMaxDamage() / 8 : stack0.getMaxDamage() / 4);
					if (l2 <= 0) {
						this.resultSlots.setItem(0, ItemStack.EMPTY);
						this.maximumCost.set(0);
						return;
					}
					int i3;
					for (i3 = 0; l2 > 0 && i3 < stack1.getCount(); ++i3) {
						int j3 = stack0.getDamageValue() - l2;
						stack0.setDamageValue(j3);
						l2 = Math.min(stack0.getDamageValue(),
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
			this.resultSlots.setRecipeUsed(this.recipe);
			this.resultSlots.setItem(0, itemstack);
		}

	}

	protected boolean shouldQuickMoveToAdditionalSlot(ItemStack stack) {
		return this.recipes.stream().anyMatch((recipe) -> {
			return recipe.isValidAdditionItem(stack);
		});
	}

	/**
	 * Called to determine if the current slot is valid for the stack merging
	 * (double-click) code. The stack passed in is null for the initial slot that
	 * was double-clicked.
	 */
	public boolean canTakeItemForPickAll(ItemStack stack, Slot slotIn) {
		return slotIn.container != this.resultSlots && super.canTakeItemForPickAll(stack, slotIn);
	}

	/**
	 * Handle when the stack in slot {@code index} is shift-clicked. Normally this
	 * moves the stack between the player inventory and the other inventory(s).
	 */
	public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index == 2) {
				if (!this.moveItemStackTo(itemstack1, 3, 39, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(itemstack1, itemstack);
			} else if (index != 0 && index != 1) {
				if (index >= 3 && index < 39) {
					int i = this.shouldQuickMoveToAdditionalSlot(itemstack) ? 1 : 0;
					if (!this.moveItemStackTo(itemstack1, i, 2, false)) {
						if (this.invSize > 0)
							if (!this.moveItemStackTo(itemstack1, 39, 38 + this.invSize, false))
								return ItemStack.EMPTY;
					}
				}
			} else if (index >= 39) {
				if (!this.moveItemStackTo(this.slots.get(index).getItem(), 3, 39, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 3, 39, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, itemstack1);
		}

		return itemstack;
	}

	@Override
	protected boolean mayPickup(PlayerEntity p_230303_1_, boolean p_230303_2_) {
		return (this.recipe != null && this.recipe.matches(this.inputSlots, this.world))
				|| ((player.abilities.instabuild || player.experienceLevel >= this.maximumCost.get()));
	}

	@Override
	protected ItemStack onTake(PlayerEntity player, ItemStack stack) {
		if (!player.abilities.instabuild) {
			player.giveExperienceLevels(-this.maximumCost.get());
		}
		stack.onCraftedBy(player.level, player, stack.getCount());
		this.resultSlots.awardUsedRecipes(player);
		this.shrinkSlot(0);
		this.shrinkSlot(1, this.materialCost);
		this.access.execute((world, pos) -> {
			world.globalLevelEvent(1044, pos, 0);
		});
		return stack;
	}

	@Override
	protected boolean isValidBlock(BlockState state) {
		return state.is(RuneBlocks.RUNETABLE.get());
	}
}
