package com.silverminer.moreore.init.blocks;

import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.common.objects.blocks.RunetableBlock;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RuneBlocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MoreOre.MODID);

	public static final RegistryObject<Block> RUNETABLE = BLOCKS.register("rune_table",
			() -> new RunetableBlock(Block.Properties.from(Blocks.SMITHING_TABLE)));
}