package com.silverminer.moreore.common.recipe;

import com.silverminer.moreore.MoreOre;

import net.minecraft.item.crafting.IRecipeType;

public interface IModRecipeType extends IRecipeType<RuneRecipe> {
	IRecipeType<RuneRecipe> RUNES = IRecipeType.register(MoreOre.MODID + ":runes");
}