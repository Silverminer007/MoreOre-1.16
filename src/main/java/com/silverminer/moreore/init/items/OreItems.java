package com.silverminer.moreore.init.items;

import com.silverminer.moreore.init.blocks.*;
import com.silverminer.moreore.util.items.ModItemGroups;
import com.silverminer.moreore.MoreOre;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class OreItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MoreOre.MODID);

	public static final RegistryObject<Item> ALEXANDRIT_ORE = ITEMS.register("alexandrit_ore", 
			() -> new BlockItem(OreBlocks.ALEXANDRIT_ORE.get(), new Item.Properties().tab(ModItemGroups.MINERALIC)));

	public static final RegistryObject<Item> ALEXANDRIT_BLOCK = ITEMS.register("alexandrit_block", 
			() -> new BlockItem(OreBlocks.ALEXANDRIT_BLOCK.get(), new Item.Properties().tab(ModItemGroups.MINERALIC)));

	public static final RegistryObject<Item> ALEXANDRIT = ITEMS.register("alexandrit", 
			() -> new Item(new Item.Properties().tab(ModItemGroups.MINERALIC)));

	public static final RegistryObject<Item> SILVER_ORE = ITEMS.register("silver_ore", 
			() -> new BlockItem(OreBlocks.SILVER_ORE.get(), new Item.Properties().tab(ModItemGroups.MINERALIC)));

	public static final RegistryObject<Item> SILVER_INGOT = ITEMS.register("silver_ingot", 
			() -> new Item(new Item.Properties().tab(ModItemGroups.MINERALIC)));

	public static final RegistryObject<Item> SILVER_BLOCK = ITEMS.register("silver_block", 
			() -> new BlockItem(OreBlocks.SILVER_BLOCK.get(), new Item.Properties().tab(ModItemGroups.MINERALIC)));

	public static final RegistryObject<Item> CAT_GOLD_BLOCK = ITEMS.register("cat_gold_block", 
			() -> new BlockItem(OreBlocks.CAT_GOLD_BLOCK.get(), new Item.Properties().tab(ModItemGroups.MINERALIC)));

	public static final RegistryObject<Item> RUBIN = ITEMS.register("rubin", 
			() -> new Item(new Item.Properties().tab(ModItemGroups.MINERALIC)));

	public static final RegistryObject<Item> SAPHIR = ITEMS.register("saphir", 
			() -> new Item(new Item.Properties().tab(ModItemGroups.MINERALIC)));

	public static final RegistryObject<Item> RUBIN_ORE = ITEMS.register("rubin_ore", 
			() -> new BlockItem(OreBlocks.RUBIN_ORE.get(), new Item.Properties().tab(ModItemGroups.MINERALIC)));

	public static final RegistryObject<Item> RUBIN_BLOCK = ITEMS.register("rubin_block", 
			() -> new BlockItem(OreBlocks.RUBIN_BLOCK.get(), new Item.Properties().tab(ModItemGroups.MINERALIC)));

	public static final RegistryObject<Item> SAPHIR_ORE = ITEMS.register("saphir_ore", 
			() -> new BlockItem(OreBlocks.SAPHIR_ORE.get(), new Item.Properties().tab(ModItemGroups.MINERALIC)));

	public static final RegistryObject<Item> SAPHIR_BLOCK = ITEMS.register("saphir_block", 
			() -> new BlockItem(OreBlocks.SAPHIR_BLOCK.get(), new Item.Properties().tab(ModItemGroups.MINERALIC)));
}
