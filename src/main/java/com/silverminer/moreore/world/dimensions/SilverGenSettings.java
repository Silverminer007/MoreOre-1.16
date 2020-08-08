package com.silverminer.moreore.world.dimensions;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.Dimension;
import net.minecraft.world.gen.settings.DimensionGeneratorSettings;

public class SilverGenSettings extends DimensionGeneratorSettings{
	public SilverGenSettings(long p_i231914_1_, boolean p_i231914_3_, boolean p_i231914_4_,
			SimpleRegistry<Dimension> p_i231914_5_) {
		super(p_i231914_1_, p_i231914_3_, p_i231914_4_, p_i231914_5_);
	}
	public int getBiomeSize() {
		return 4;
	}
	public int getRiverSize() {
		return 4;
	}
	public int getBiomeId() {
		return -1;
	}
	public int getBedrockFloorHeight() {
		return 0;
	}
	public BlockPos getSpawnPos() {//Nur bei End Generator
		return new BlockPos(0, 75, 0);
	}
	public BlockState getDefaultFluid() {
		return Blocks.WATER.getDefaultState();
	}
}
