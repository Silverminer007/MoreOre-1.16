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

public class JonaItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MoreOre.MODID);

	public static final RegistryObject<Item> ERDBEERE = InitItems.registerCompostItems("erdbeere",
			() -> new BlockNamedItem(JonaBlocks.ERDBEER_BUSCH.get(),
					new Item.Properties().group(ModItemGroups.JONA)
							.food(new Food.Builder().hunger(2).saturation(1.0f).fastToEat()
									.effect(() -> new EffectInstance(Effects.WATER_BREATHING, 2000), 1.0f).build())),
			0.3F);

	public static final RegistryObject<Item> ERDBEER_BLOCK = InitItems.registerCompostItems("erdbeer_block",
			() -> new BlockItem(JonaBlocks.ERDBEER_BLOCK.get(), new Item.Properties().group(ModItemGroups.JONA)), 0.3F);

	public static final RegistryObject<Item> OHNEZAHN = ITEMS.register("ohnezahn",
			() -> new BlockItem(JonaBlocks.OHNEZAHN.get(), new Item.Properties().group(ModItemGroups.JONA)));
}
