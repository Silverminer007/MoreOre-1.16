package com.silverminer.moreore.common.tags;

import com.silverminer.moreore.MoreOre;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tags.ItemTags;

public class ModTags {
	public static final ITag.INamedTag<Block> GIANT_ZOMBIE_SUMMON_BLOCKS = BlockTags
			.bind(MoreOre.MODID + ":giant_zombie_summon_blocks");

	public static final INamedTag<Item> RUNE_SHARDS = ItemTags
			.bind(MoreOre.MODID + ":rune_shards");
}