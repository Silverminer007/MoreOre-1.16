package com.silverminer.moreore.init.items;

import com.silverminer.moreore.init.blocks.*;
import com.silverminer.moreore.util.items.ModItemGroups;

import com.silverminer.moreore.MoreOre;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TreeItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MoreOre.MODID);

	public static final RegistryObject<Item> BLUE_STICK = InitItems.registerCompostItems("blue_stick",
			() -> new Item(new Item.Properties().group(ModItemGroups.BIOLOGIC).maxStackSize(64)), 0.3F);

	public static final RegistryObject<Item> SILVER_PLANKS = ITEMS.register("silver_planks",
			() -> new BlockItem(BiologicBlocks.SILVER_PLANKS.get(),
					new Item.Properties().group(ModItemGroups.BIOLOGIC)));

	public static final RegistryObject<Item> SILVER_LOG = InitItems.registerCompostItems("silver_logs",
			() -> new BlockItem(BiologicBlocks.SILVER_LOG.get(), new Item.Properties().group(ModItemGroups.BIOLOGIC)), 0.3F);

	public static final RegistryObject<Item> STRIPPED_SILVER_LOG = InitItems.registerCompostItems("stripped_silver_logs",
			() -> new BlockItem(BiologicBlocks.STRIPPED_SILVER_LOG.get(), new Item.Properties().group(ModItemGroups.BIOLOGIC)), 0.3F);

	public static final RegistryObject<Item> SILVER_LEAVES = InitItems.registerCompostItems("silver_leaves",
			() -> new BlockItem(BiologicBlocks.SILVER_LEAVES.get(),
					new Item.Properties().group(ModItemGroups.BIOLOGIC)), 0.3F);

	public static final RegistryObject<Item> SILVER_SAPLING = InitItems.registerCompostItems("silver_saplings",
			() -> new BlockItem(BiologicBlocks.SILVER_SAPLING.get(),
					new Item.Properties().group(ModItemGroups.BIOLOGIC)), 0.3F);

	public static final RegistryObject<Item> SILVER_WOOD = InitItems.registerCompostItems("silver_wood",
			() -> new BlockItem(BiologicBlocks.SILVER_WOOD.get(), new Item.Properties().group(ModItemGroups.BIOLOGIC)), 0.3F);

	public static final RegistryObject<Item> SILVER_WALL = ITEMS.register("silver_wall",
			() -> new BlockItem(BiologicBlocks.SILVER_WALL.get(), new Item.Properties().group(ModItemGroups.BIOLOGIC)));

	public static final RegistryObject<Item> SILVER_TRAPDOOR = ITEMS.register("silver_trapdoor",
			() -> new BlockItem(BiologicBlocks.SILVER_TRAPDOOR.get(),
					new Item.Properties().group(ModItemGroups.BIOLOGIC)));

	public static final RegistryObject<Item> GOLD_LEAVES = InitItems.registerCompostItems("gold_leaves",
			() -> new BlockItem(BiologicBlocks.GOLD_LEAVES.get(), new Item.Properties().group(ModItemGroups.BIOLOGIC)), 0.3F);

	public static final RegistryObject<Item> GOLD_SAPLING = InitItems.registerCompostItems("gold_saplings",
			() -> new BlockItem(BiologicBlocks.GOLD_SAPLING.get(),
					new Item.Properties().group(ModItemGroups.BIOLOGIC)), 0.3F);

	public static final RegistryObject<Item> ICE_LEAVES = InitItems.registerCompostItems("ice_leaves",
			() -> new BlockItem(BiologicBlocks.ICE_LEAVES.get(), new Item.Properties().group(ModItemGroups.BIOLOGIC)), 0.3F);

	public static final RegistryObject<Item> ICE_SAPLING = InitItems.registerCompostItems("ice_saplings",
			() -> new BlockItem(BiologicBlocks.ICE_SAPLING.get(), new Item.Properties().group(ModItemGroups.BIOLOGIC)), 0.3F);

	public static final RegistryObject<Item> SILVER_WOOD_BUTTON = ITEMS.register("silver_wood_button",
			() -> new BlockItem(BiologicBlocks.SILVER_WOOD_BUTTON.get(),
					new Item.Properties().group(ModItemGroups.BIOLOGIC)));
}
