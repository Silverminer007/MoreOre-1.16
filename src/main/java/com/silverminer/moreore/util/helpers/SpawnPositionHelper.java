package com.silverminer.moreore.util.helpers;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
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

	@Nullable
	protected static BlockPos validatePos(World world, int x, int z) {
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(x, 0, z);
		Biome biome = world.getBiome(blockpos$mutable);
		BlockState blockstate = biome.getGenerationSettings().getSurfaceBuilderConfig().getTop();
		if (!blockstate.getBlock().isIn(BlockTags.VALID_SPAWN)) {
			return null;
		} else {
			int i = world.getHeight(Heightmap.Type.MOTION_BLOCKING, x, z);
			if (i < 0) {
				return null;
			} else {
				int j = world.getHeight(Heightmap.Type.WORLD_SURFACE, x, z);
				if (j <= i && j > world.getHeight(Heightmap.Type.OCEAN_FLOOR, x, z)) {
					return null;
				} else {
					for (int k = i + 1; k >= 0; --k) {
						blockpos$mutable.setPos(x, k, z);
						BlockState blockstate1 = world.getBlockState(blockpos$mutable);
						if (!blockstate1.getFluidState().isEmpty()) {
							break;
						}

						if (blockstate1.equals(blockstate)) {
							return blockpos$mutable.up().toImmutable();
						}
					}

					return null;
				}
			}
		}
	}
}
