package com.silverminer.moreore.common.tags;

import com.silverminer.moreore.MoreOre;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;

public class ModBlockTags {
	public static final ITag.INamedTag<Block> GIANT_ZOMBIE_SUMMON_BLOCKS = BlockTags
			.makeWrapperTag(MoreOre.MODID + ":giant_zombie_summon_blocks");
}