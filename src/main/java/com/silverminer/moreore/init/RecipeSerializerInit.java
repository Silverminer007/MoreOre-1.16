package com.silverminer.moreore.init;

import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.common.recipe.RuneRecipe;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeSerializerInit {
	public static final DeferredRegister<IRecipeSerializer<?>> SERIALIZER = DeferredRegister
			.create(ForgeRegistries.RECIPE_SERIALIZERS, MoreOre.MODID);

	public static final RegistryObject<IRecipeSerializer<?>> RUNES = SERIALIZER.register("runes",
			() -> new RuneRecipe.Serializer());
}