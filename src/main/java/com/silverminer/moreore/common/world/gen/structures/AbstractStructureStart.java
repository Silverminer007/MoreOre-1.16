package com.silverminer.moreore.common.world.gen.structures;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructureStart;

public abstract class AbstractStructureStart<C extends IFeatureConfig> extends StructureStart<C> {
	protected static final Logger LOGGER = LogManager.getLogger(AbstractStructureStart.class);

	public AbstractStructureStart(Structure<C> structure, int chunkX, int chunkZ, MutableBoundingBox boundingbox,
			int p_i225874_5_, long seed) {
		super(structure, chunkX, chunkZ, boundingbox, p_i225874_5_, seed);
	}

	@Override
	public void placeInChunk(final ISeedReader seedReader, final StructureManager manager,
			final ChunkGenerator generator, final Random random, final MutableBoundingBox boundingBox,
			final ChunkPos position) {
		super.placeInChunk(seedReader, manager, generator, random, boundingBox, position);

		if (!this.needsGround())
			return;
		for (int x = this.boundingBox.x0 - 1; this.boundingBox.x1 + 1 >= x; ++x) {
			for (int z = this.boundingBox.z0 - 1; this.boundingBox.z1 + 1 >= z; ++z) {
				for (int y = this.boundingBox.y0 - 1; y > 1; --y) {
					BlockPos pos = new BlockPos(x, y, z);
					if (shouldBlockBeReplaced(seedReader, pos)) {
						replaceBlock(seedReader, pos);
					} else {
						break;
					}
				}
			}
		}
	}

	public boolean needsGround() {
		return true;
	}

	private boolean shouldBlockBeReplaced(ISeedReader seedReader, BlockPos pos) {
		return seedReader.isEmptyBlock(pos) || seedReader.getBlockState(pos).getMaterial().isLiquid()
				|| seedReader.getBlockState(pos).getMaterial().isReplaceable();
	}

	protected BlockState getTopLayerBlock(ISeedReader seedReader, BlockPos pos) {
		return seedReader.getBiome(pos).getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial();
	}

	protected BlockState getLowerLayerBlock(ISeedReader seedReader, BlockPos pos) {
		return seedReader.getBiome(pos).getGenerationSettings().getSurfaceBuilderConfig().getUnderMaterial();
	}

	private void replaceBlock(ISeedReader seedReader, BlockPos pos) {
		BlockState state = getTopLayerBlock(seedReader, pos);

		Block downBlock = seedReader.getBlockState(pos.below()).getBlock();
		// Wenn unter dem Block keine Erde und kein Grass ist und der Block darunter
		// Stein ist oder die Drei dar�ber keine Luft dann nutze das untere Material
		if ((downBlock != Blocks.DIRT && downBlock != Blocks.GRASS_BLOCK)
				&& (downBlock == Blocks.STONE || !seedReader.isEmptyBlock(pos.above(1)) && !seedReader.isEmptyBlock(pos.below(2))
						&& !seedReader.isEmptyBlock(pos.below(3)))) {
			state = getLowerLayerBlock(seedReader, pos);
		}

		// Wenn �ber dem jetzigem Block Grass ist dann nutze hier statt Grass, Erde
		Block upBlock = seedReader.getBlockState(pos.below()).getBlock();
		if ((upBlock == Blocks.GRASS_BLOCK || upBlock == Blocks.GRASS_PATH)
				&& (state.getBlock() == Blocks.GRASS_BLOCK || state.getBlock() == Blocks.GRASS_PATH)) {
			state = Blocks.DIRT.defaultBlockState();
		}

		seedReader.setBlock(pos, state, 2);
	}
}