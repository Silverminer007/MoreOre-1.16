package com.silverminer.moreore.util.helpers;

import javax.annotation.Nullable;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;

public class SpawnPositionHelper {
	public static BlockPos calculate(BlockPos pos, World world) {
		try {
			for (int i = 0; i <= 30; i = i > 0 ? -i : 1 - i) {
				for (int j = 0; j <= 30; j = j > 0 ? -j : 1 - j) {
					BlockPos blockpos = validatePos(world, pos.getX() + i, pos.getZ() + j);
					if (blockpos != null) {
						return blockpos;
					}
				}
			}

			return world.getHeightmapPos(Heightmap.Type.WORLD_SURFACE, pos);
		} catch (Throwable e) {
			return null;
		}
	}

	protected static boolean isPositionValid(World world, BlockPos pos) {
		return (world.getBlockState(pos).getBlock() == Blocks.AIR)
				&& (world.getBlockState(pos.above()).getBlock() == Blocks.AIR)
				&& (world.getBlockState(pos.below()).canOcclude());
	}

	@Nullable
	protected static BlockPos validatePos(World world, int x, int z) {
		boolean firstMatch = true;
		for (int y = 256; y > 0; y--) {
			BlockPos tempPos = new BlockPos(x, y, z);
			if (isPositionValid(world, tempPos)) {
				if ((!world.dimensionType().hasCeiling())) {
					return tempPos;
				} else if (!firstMatch) {
					return tempPos;
				} else {
					firstMatch = false;
				}
			}
		}
		return null;
	}
}
