package com.silverminer.moreore.world.gen.structures;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.MarginedStructureStart;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;

public class DesertTempelStructure extends AbstractStructure {
	public static final String SHORT_NAME = "desert_tempel";

	public DesertTempelStructure(Codec<NoFeatureConfig> configFactoryIn) {
		super(configFactoryIn, SHORT_NAME);
	}

	@Override
	protected int getFeatureDistance(ChunkGenerator generator) {
		return StructureGenConfig.DESERT_TEMPEL_DISTANCE.get();
	}

	@Override
	protected int getFeatureSeparation(ChunkGenerator generator) {
		return StructureGenConfig.DESERT_TEMPEL_SEPARATION.get();
	}

	@Override
	protected double getSpawnChance() {
		return StructureGenConfig.DESERT_TEMPEL_SPAWN_CHANCE.get();
	}

	protected int getSeedModifier() {
		return 9832423;
	}

	@Override
	@Nonnull
	public IStartFactory<NoFeatureConfig> getStartFactory() {
		return Start::new;
	}

	public static class Start<C extends IFeatureConfig> extends MarginedStructureStart<C> {

		public Start(Structure<C> structure, int chunkX, int chunkZ, MutableBoundingBox bounds, int reference,
				long seed) {
			super(structure, chunkX, chunkZ, bounds, reference, seed);
		}

		@Override
		public void func_230364_a_(ChunkGenerator generator, TemplateManager templateManager, int chunkX,
				int chunkZ, Biome biome, C iFeatureConfig) {
			Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];
			int xOffset = 32;
			int zOffset = 32;
			if (rotation == Rotation.CLOCKWISE_90) {
				xOffset *= -1;
			} else if (rotation == Rotation.CLOCKWISE_180) {
				xOffset *= -1;
				zOffset *= -1;
			} else if (rotation == Rotation.COUNTERCLOCKWISE_90) {
				zOffset *= -1;
			}

			int xCenter = (chunkX << 4) + 7;
			int zCenter = (chunkZ << 4) + 7;

			int i1 = generator.func_222531_c(xCenter, zCenter, Heightmap.Type.WORLD_SURFACE_WG);
			int j1 = generator.func_222531_c(xCenter, zCenter + zOffset, Heightmap.Type.WORLD_SURFACE_WG);
			int k1 = generator.func_222531_c(xCenter + xOffset, zCenter, Heightmap.Type.WORLD_SURFACE_WG);
			int l1 = generator.func_222531_c(xCenter + xOffset, zCenter + zOffset, Heightmap.Type.WORLD_SURFACE_WG);
			int minHeight = Math.min(Math.min(i1, j1), Math.min(k1, l1));

			BlockPos blockpos = new BlockPos(chunkX * 16, minHeight - 2, chunkZ * 16);
			DesertTempelPieces.generate(generator, templateManager, blockpos, this.components, this.rand);
			this.recalculateStructureSize();
		}
	}
}