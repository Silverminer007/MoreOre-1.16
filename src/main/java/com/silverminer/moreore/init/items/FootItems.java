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
					() -> new BlockItem(BiologicBlocks.BANANA.get(), new Item.Properties().group(ModItemGroups.BIOLOGIC)
							.food(new Food.Builder().hunger(3).saturation(1.2f).fastToEat()
									.effect(() -> new EffectInstance(Effects.NIGHT_VISION, 2000), 0.5f).build())),
					0.3F);

	public static final RegistryObject<Item> LETUCE_SEED = InitItems.registerCompostItems("letuce_seed",
			() -> new BlockItem(BiologicBlocks.LETUCE.get(), new Item.Properties().group(ModItemGroups.BIOLOGIC)),
			0.3F);

	public static final RegistryObject<Item> LETUCE = InitItems.registerCompostItems("letuce",
			() -> new Item(new Item.Properties().group(ModItemGroups.BIOLOGIC)
					.food(new Food.Builder().hunger(4).saturation(0.8f).build())),
			0.3F);

	public static final RegistryObject<Item> NUTS = InitItems.registerCompostItems("nut",
			() -> new BlockNamedItem(BiologicBlocks.NUT_BUSH.get(), new Item.Properties().group(ModItemGroups.BIOLOGIC)
					.food(new Food.Builder().hunger(2).saturation(1.0f).fastToEat().build())),
			0.3F);
}
