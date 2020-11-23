package com.silverminer.moreore.common.recipe;

import com.google.gson.JsonObject;
import com.silverminer.moreore.init.RecipeSerializerInit;

import net.minecraft.block.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RuneRecipe implements IRecipe<IInventory> {
	private final Ingredient base;
	private final Ingredient addition;
	private final ItemStack result;
	private final ResourceLocation recipeId;

	public RuneRecipe(ResourceLocation recipeId, Ingredient base, Ingredient addition, ItemStack result) {
		this.recipeId = recipeId;
		this.base = base;
		this.addition = addition;
		this.result = result;
	}

	/**
	 * Used to check if a recipe matches current crafting inventory
	 */
	public boolean matches(IInventory inv, World worldIn) {
		return this.base.test(inv.getStackInSlot(0)) && this.addition.test(inv.getStackInSlot(1));
	}

	/**
	 * Returns an Item that is the result of this recipe
	 */
	public ItemStack getCraftingResult(IInventory inv) {
		ItemStack itemstack = this.result.copy();
		CompoundNBT compoundnbt = inv.getStackInSlot(0).getTag();
		if (compoundnbt != null) {
			itemstack.setTag(compoundnbt.copy());
		}

		return itemstack;
	}

	/**
	 * Used to determine if this recipe can fit in a grid of the given width/height
	 */
	public boolean canFit(int width, int height) {
		return width * height >= 2;
	}

	/**
	 * Get the result of this recipe, usually for display purposes (e.g. recipe
	 * book). If your recipe has more than one possible result (e.g. it's dynamic
	 * and depends on its inputs), then return an empty stack.
	 */
	public ItemStack getRecipeOutput() {
		return this.result;
	}

	public boolean isValidAdditionItem(ItemStack addition) {
		return this.addition.test(addition);
	}

	public ItemStack getIcon() {
		return new ItemStack(Blocks.SMITHING_TABLE);
	}

	public ResourceLocation getId() {
		return this.recipeId;
	}

	public IRecipeSerializer<?> getSerializer() {
		return RecipeSerializerInit.RUNES.get();
	}

	public IRecipeType<?> getType() {
		return IModRecipeType.RUNES;
	}

	public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>>
			implements IRecipeSerializer<RuneRecipe> {
		public RuneRecipe read(ResourceLocation recipeId, JsonObject json) {
			Ingredient ingredient = Ingredient.deserialize(JSONUtils.getJsonObject(json, "base"));
			Ingredient ingredient1 = Ingredient.deserialize(JSONUtils.getJsonObject(json, "addition"));
			ItemStack itemstack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
			return new RuneRecipe(recipeId, ingredient, ingredient1, itemstack);
		}

		public RuneRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			Ingredient ingredient = Ingredient.read(buffer);
			Ingredient ingredient1 = Ingredient.read(buffer);
			ItemStack itemstack = buffer.readItemStack();
			return new RuneRecipe(recipeId, ingredient, ingredient1, itemstack);
		}

		public void write(PacketBuffer buffer, RuneRecipe recipe) {
			recipe.base.write(buffer);
			recipe.addition.write(buffer);
			buffer.writeItemStack(recipe.result);
		}
	}
}