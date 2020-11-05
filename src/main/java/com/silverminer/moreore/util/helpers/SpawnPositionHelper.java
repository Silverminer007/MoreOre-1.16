package com.silverminer.moreore.util.helpers;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.Heightmap;

public class SpawnPositionHelper {
	public static BlockPos calculate(BlockPos pos, World world) {
		ChunkPos chunkPos = new ChunkPos((int) Math.ceil((double) pos.getX() / 16),
				(int) Math.ceil((double) pos.getZ() / 16));
		for (int i = chunkPos.getXStart(); i <= chunkPos.getXEnd(); ++i) {
			for (int j = chunkPos.getZStart(); j <= chunkPos.getZEnd(); ++j) {
				BlockPos blockpos = validatePos(world, i, j);
				if (blockpos != null) {
					return blockpos;
				}
			}
		}

		return pos;
	}

	@Nullable
	protected static BlockPos validatePos(World world, int chunkX, int chunkZ) {
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(chunkX, 0, chunkZ);
		Biome biome = world.getBiome(blockpos$mutable);
		BlockState blockstate = biome.getGenerationSettings().getSurfaceBuilderConfig().getTop();
		if (!blockstate.getBlock().isIn(BlockTags.VALID_SPAWN)) {
			return null;
		} else {
			Chunk chunk = world.getChunk(chunkX >> 4, chunkZ >> 4);
			int i = chunk.getTopBlockY(Heightmap.Type.MOTION_BLOCKING, chunkX & 15, chunkZ & 15);
			if (i < 0) {
				return null;
			} else {
				int j = chunk.getTopBlockY(Heightmap.Type.WORLD_SURFACE, chunkX & 15, chunkZ & 15);
				if (j <= i && j > chunk.getTopBlockY(Heightmap.Type.OCEAN_FLOOR, chunkX & 15, chunkZ & 15)) {
					return null;
				} else {
					for (int k = i + 1; k >= 0; --k) {
						blockpos$mutable.setPos(chunkX, k, chunkZ);
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
