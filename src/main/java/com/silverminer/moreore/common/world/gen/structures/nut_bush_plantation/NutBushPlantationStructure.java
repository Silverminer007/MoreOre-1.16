package com.silverminer.moreore.common.world.gen.structures.nut_bush_plantation;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;
import com.silverminer.moreore.common.world.gen.structures.AbstractStructure;
import com.silverminer.moreore.common.world.gen.structures.AbstractStructureStart;
import com.silverminer.moreore.util.structures.config.Config;

public class NutBushPlantationStructure extends AbstractStructure<NoFeatureConfig> {
	public static final String SHORT_NAME = "nut_bush_plantation";

	public NutBushPlantationStructure(Codec<NoFeatureConfig> configFactoryIn) {
		super(configFactoryIn, 3, SHORT_NAME);
	}

	@Override
	@Nonnull
	public IStartFactory<NoFeatureConfig> getStartFactory() {
		return Start::new;
	}

	@Override
	public Decoration step() {
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}

	@Override
	public int getDistance() {
		return Config.STRUCTURES.NUT_BUSH_PLANTATION.DISTANCE.get();
	}

	@Override
	public int getSeparation() {
		return Config.STRUCTURES.NUT_BUSH_PLANTATION.SEPARATION.get();
	}

	@Override
	public int getSeedModifier() {
		return Config.STRUCTURES.NUT_BUSH_PLANTATION.SEED.get();
	}

	@Override
	public double getSpawnChance() {
		return Config.STRUCTURES.NUT_BUSH_PLANTATION.SPAWN_CHANCE.get();
	}

	public static class Start extends AbstractStructureStart<NoFeatureConfig> {

		public Start(Structure<NoFeatureConfig> structure, int chunkX, int chunkZ, MutableBoundingBox boundingbox,
				int refernce, long seed) {
			super(structure, chunkX, chunkZ, boundingbox, refernce, seed);
		}

		@Override
		public void generatePieces(DynamicRegistries registries, ChunkGenerator chunkGenerator,
				TemplateManager templateManager, int chunkX, int chunkZ, Biome biome, NoFeatureConfig config) {
			int i = chunkX * 16;
			int j = chunkZ * 16;
			BlockPos blockpos = new BlockPos(i, 0, j);
			Rotation rotation = Rotation.getRandom(this.random);
			NutBushPlantationPieces.generate(templateManager, blockpos, rotation, this.pieces, this.random);
			this.calculateBoundingBox();
		}
	}
}