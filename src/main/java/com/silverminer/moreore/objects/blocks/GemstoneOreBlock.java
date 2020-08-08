package com.silverminer.moreore.objects.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class GemstoneOreBlock extends Block {
	private final int exp;

	public GemstoneOreBlock(Properties properties, int exp) {
		super(properties);
		this.exp = exp;
	}

	public int getExperience(Random rand, int minimum) {
		return MathHelper.nextInt(rand, minimum, exp);
	}

	@Override
	public int getExpDrop(BlockState state, net.minecraft.world.IWorldReader reader, BlockPos pos, int fortune,
			int silktouch) {
		return silktouch == 0 ? this.getExperience(RANDOM, fortune) : 0;//Drops Exp. whether the Tool don't have silktouch
	}
}
