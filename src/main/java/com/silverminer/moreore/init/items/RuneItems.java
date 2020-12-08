package com.silverminer.moreore.init.items;

import com.silverminer.moreore.init.blocks.OreBlocks;
import com.silverminer.moreore.init.blocks.RuneBlocks;
import com.silverminer.moreore.util.items.ModItemGroups;
import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.common.objects.items.RuneItem;
import com.silverminer.moreore.common.objects.items.runes.RuneType;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RuneItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MoreOre.MODID);

	public static final RegistryObject<Item> RUNETABLE = ITEMS.register("rune_table",
			() -> new BlockItem(RuneBlocks.RUNETABLE.get(), new Item.Properties().group(ModItemGroups.RUNES)));

	public static final RegistryObject<Item> RUNE_BASE = ITEMS.register("rune_base",
			() -> new Item(new Item.Properties().group(ModItemGroups.RUNES)));

	public static final RegistryObject<Item> RUNE_BASE_BLOCK = ITEMS.register("rune_base_block",
			() -> new BlockItem(RuneBlocks.RUNE_BASE_BLOCK.get(), new Item.Properties().group(ModItemGroups.RUNES)));

	public static final RegistryObject<Item> RAINBOW_ORE = ITEMS.register("rainbow_ore",
			() -> new BlockItem(OreBlocks.RAINBOW_ORE.get(), new Item.Properties().group(ModItemGroups.RUNES)));

	public static final RegistryObject<Item> RAINBOW_BLOCK = ITEMS.register("rainbow_block",
			() -> new BlockItem(OreBlocks.RAINBOW_BLOCK.get(), new Item.Properties().group(ModItemGroups.RUNES)));

	public static final RegistryObject<Item> RAINBOW_RUNE = ITEMS.register("rainbow_rune",
			() -> new RuneItem(RuneType.RAINBOW, new Item.Properties().group(ModItemGroups.RUNES)));

	public static final RegistryObject<Item> RAINBOW = ITEMS.register("rainbow",
			() -> new Item(new Item.Properties().group(ModItemGroups.RUNES)));

	public static final RegistryObject<Item> RUNE_GREEN = ITEMS.register("rune_green",
			() -> new RuneItem(RuneType.GREEN, new Item.Properties().group(ModItemGroups.RUNES)));

	public static final RegistryObject<Item> RUNE_GREEN_SHARD = ITEMS.register("rune_green_shard",
			() -> new Item(new Item.Properties().group(ModItemGroups.RUNES)));

	public static final RegistryObject<Item> RUNE_ORANGE = ITEMS.register("rune_orange",
			() -> new RuneItem(RuneType.ORANGE, new Item.Properties().group(ModItemGroups.RUNES)));

	public static final RegistryObject<Item> RUNE_ORANGE_SHARD = ITEMS.register("rune_orange_shard",
			() -> new Item(new Item.Properties().group(ModItemGroups.RUNES)));

	public static final RegistryObject<Item> RUNE_BLUE = ITEMS.register("rune_blue",
			() -> new RuneItem(RuneType.BLUE, new Item.Properties().group(ModItemGroups.RUNES)));

	public static final RegistryObject<Item> RUNE_BLUE_SHARD = ITEMS.register("rune_blue_shard",
			() -> new Item(new Item.Properties().group(ModItemGroups.RUNES)));

	public static final RegistryObject<Item> RUNE_RED = ITEMS.register("rune_red",
			() -> new RuneItem(RuneType.RED, new Item.Properties().group(ModItemGroups.RUNES)));

	public static final RegistryObject<Item> RUNE_RED_SHARD = ITEMS.register("rune_red_shard",
			() -> new Item(new Item.Properties().group(ModItemGroups.RUNES)));

	public static final RegistryObject<Item> RUNE_YELLOW = ITEMS.register("rune_yellow",
			() -> new RuneItem(RuneType.YELLOW, new Item.Properties().group(ModItemGroups.RUNES)));

	public static final RegistryObject<Item> RUNE_YELLOW_SHARD = ITEMS.register("rune_yellow_shard",
			() -> new Item(new Item.Properties().group(ModItemGroups.RUNES)));

	public static final RegistryObject<Item> RUNE_GRAY = ITEMS.register("rune_gray",
			() -> new RuneItem(RuneType.GRAY, new Item.Properties().group(ModItemGroups.RUNES)));

	public static final RegistryObject<Item> RUNE_GRAY_SHARD = ITEMS.register("rune_gray_shard",
			() -> new Item(new Item.Properties().group(ModItemGroups.RUNES)));

	public static final RegistryObject<Item> RUNE_BROWN = ITEMS.register("rune_brown",
			() -> new RuneItem(RuneType.BROWN, new Item.Properties().group(ModItemGroups.RUNES)));

	public static final RegistryObject<Item> RUNE_BROWN_SHARD = ITEMS.register("rune_brown_shard",
			() -> new Item(new Item.Properties().group(ModItemGroups.RUNES)));

	public static final RegistryObject<Item> RUNE_PURPLE = ITEMS.register("rune_purple",
			() -> new RuneItem(RuneType.PURPLE, new Item.Properties().group(ModItemGroups.RUNES)));

	public static final RegistryObject<Item> RUNE_PURPLE_SHARD = ITEMS.register("rune_purple_shard",
			() -> new Item(new Item.Properties().group(ModItemGroups.RUNES)));
}