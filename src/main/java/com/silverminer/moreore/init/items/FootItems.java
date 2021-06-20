package com.silverminer.moreore.init.items;

import com.silverminer.moreore.init.blocks.*;
import com.silverminer.moreore.util.items.ModItemGroups;
import com.silverminer.moreore.MoreOre;

import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FootItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MoreOre.MODID);

	public static final RegistryObject<Item> BANANA = InitItems
			.registerCompostItems("banana",
					() -> new Item(new Item.Properties().tab(ModItemGroups.BIOLOGIC)
							.food(new Food.Builder().nutrition(3).saturationMod(1.2f).fast()
									.effect(() -> new EffectInstance(Effects.NIGHT_VISION, 2000), 0.5f).build())),
					0.3F);

	public static final RegistryObject<Item> BANANA_SEED = InitItems.registerCompostItems("banana_seed",
			() -> new BlockNamedItem(BiologicBlocks.BANANA.get(), new Item.Properties().tab(ModItemGroups.BIOLOGIC)),
			0.3F);

	public static final RegistryObject<Item> LETUCE_SEED = InitItems.registerCompostItems("letuce_seed",
			() -> new BlockItem(BiologicBlocks.LETUCE.get(), new Item.Properties().tab(ModItemGroups.BIOLOGIC)),
			0.3F);

	public static final RegistryObject<Item> LETUCE = InitItems.registerCompostItems("letuce",
			() -> new Item(new Item.Properties().tab(ModItemGroups.BIOLOGIC)
					.food(new Food.Builder().nutrition(4).saturationMod(0.8f).build())),
			0.3F);
}
