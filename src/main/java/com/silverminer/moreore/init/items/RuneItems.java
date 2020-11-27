package com.silverminer.moreore.init.items;

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

	public static final RegistryObject<Item> RUNE_GREEN = ITEMS.register("rune_green",
			() -> new RuneItem(new Item.Properties().group(ModItemGroups.RUNES).maxStackSize(1)));

	public static final RegistryObject<Item> RUNE_GREEN_SHARD = ITEMS.register("rune_green_shard",
			() -> new RuneShardItem(new Item.Properties().group(ModItemGroups.RUNES), () -> RUNE_GREEN));
}