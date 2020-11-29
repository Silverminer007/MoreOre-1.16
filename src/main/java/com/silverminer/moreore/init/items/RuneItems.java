package com.silverminer.moreore.init.items;

import com.silverminer.moreore.init.blocks.OreBlocks;
import com.silverminer.moreore.init.blocks.RuneBlocks;
import com.silverminer.moreore.util.items.ModItemGroups;
import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.common.objects.items.RuneItem;
import com.silverminer.moreore.common.objects.items.RuneShardItem;

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
			() -> new RuneItem(new Item.Properties().group(ModItemGroups.RUNES).maxStackSize(1)));

	public static final RegistryObject<Item> RUNE_BASE_BLOCK = ITEMS.register("rune_base_block",
			() -> new BlockItem(RuneBlocks.RUNE_BASE_BLOCK.get(), new Item.Properties().group(ModItemGroups.RUNES)));


	public static final RegistryObject<Item> RAINBOW_ORE = ITEMS.register("rainbow_ore", 
			() -> new BlockItem(OreBlocks.RAINBOW_ORE.get(), new Item.Properties().group(ModItemGroups.RUNES)));

	public static final RegistryObject<Item> RAINBOW_BLOCK = ITEMS.register("rainbow_block", 
			() -> new BlockItem(OreBlocks.RAINBOW_BLOCK.get(), new Item.Properties().group(ModItemGroups.RUNES)));

	public static final RegistryObject<Item> RAINBOW = ITEMS.register("rainbow", 
			() -> new Item(new Item.Properties().group(ModItemGroups.RUNES)));

	public static final RegistryObject<Item> RAINBOW_RUNE = ITEMS.register("rainbow_rune", 
			() -> new Item(new Item.Properties().group(ModItemGroups.RUNES)));

	public static final RegistryObject<Item> RUNE_GREEN = ITEMS.register("rune_green",
			() -> new RuneItem(new Item.Properties().group(ModItemGroups.RUNES).maxStackSize(1)));

	public static final RegistryObject<Item> RUNE_GREEN_SHARD = ITEMS.register("rune_green_shard",
			() -> new RuneShardItem(new Item.Properties().group(ModItemGroups.RUNES), () -> RUNE_GREEN));
}