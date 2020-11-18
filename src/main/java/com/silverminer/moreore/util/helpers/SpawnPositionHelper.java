package com.silverminer.moreore.util.helpers;

import javax.annotation.Nullable;

import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;

public class SpawnPositionHelper {
	public static BlockPos calculate(BlockPos pos, World world) {
		for (int i = 0; i <= 30; i = i > 0 ? -i : 1 - i) {
			for (int j = 0; j <= 30; j = j > 0 ? -j : 1 - j) {
				BlockPos blockpos = validatePos(world, pos.getX() + i, pos.getZ() + j);
				if (blockpos != null) {
					return blockpos;
				}
			}
		}

		return world.getHeight(Heightmap.Type.WORLD_SURFACE, pos);
	}

	protected static boolean isPositionValid(World world, BlockPos pos) {
		return (world.getBlockState(pos).getBlock() == Blocks.AIR)
				&& (world.getBlockState(pos.up()).getBlock() == Blocks.AIR)
				&& (world.getBlockState(pos.down()).getBlock().isIn(BlockTags.VALID_SPAWN));
	}

	@Nullable
	protected static BlockPos validatePos(World world, int x, int z) {
		int modyfier = 1;
		int y = 0;
		if (!world.getDimensionType().getHasCeiling()) {
			y = 256;
			modyfier = -1;
		}
		while (!isPositionValid(world, new BlockPos(x, y, z))) {
			if (y <= 0 || y > 256) {
				return null;
			}
			y += modyfier;
		}
		return new BlockPos(x, y, z);
	}
}
