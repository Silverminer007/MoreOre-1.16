package com.silverminer.moreore.client.container;

import java.util.List;

import com.silverminer.moreore.common.recipe.IModRecipeType;
import com.silverminer.moreore.common.recipe.RuneRecipe;
import com.silverminer.moreore.init.ContainerTypesInit;
import com.silverminer.moreore.init.blocks.RuneBlocks;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.AbstractRepairContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.world.World;

public class RunetableContainer extends AbstractRepairContainer {
	private final World world;
	private final List<RuneRecipe> recipes;
	private RuneRecipe recipe;

	public RunetableContainer(int p_i231590_1_, PlayerInventory inventory) {
		this(p_i231590_1_, inventory, IWorldPosCallable.DUMMY);
	}

	public RunetableContainer(int p_i231591_1_, PlayerInventory inventory, IWorldPosCallable iworldposcallable) {
		super(ContainerTypesInit.RUNE_TABLE.get(), p_i231591_1_, inventory, iworldposcallable);
		this.world = inventory.player.world;
		this.recipes = this.world.getRecipeManager().getRecipesForType(IModRecipeType.RUNES);
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
		List<RuneRecipe> list = this.world.getRecipeManager().getRecipes(IModRecipeType.RUNES, this.field_234643_d_,
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
}
