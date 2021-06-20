package com.silverminer.moreore.common.world.gen.structures.runes.blue;

import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
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
import com.silverminer.moreore.util.events.handler.CommonEvents;
import com.silverminer.moreore.util.structures.config.Config;

public class BlueRuneStructure extends AbstractStructure<NoFeatureConfig> {
	public static final String SHORT_NAME = "blue_rune";

	public BlueRuneStructure(Codec<NoFeatureConfig> configFactoryIn) {
		super(configFactoryIn, 3, SHORT_NAME);
	}

	@Override
	public double getSpawnChance() {
		return Config.STRUCTURES.BLUE_RUNE.SPAWN_CHANCE.get();
	}

	public int getSeedModifier() {
		return Config.STRUCTURES.BLUE_RUNE.SEED.get();
	}

	@Override
	public Decoration step() {
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}

	@Override
	public int getDistance() {
		return Config.STRUCTURES.BLUE_RUNE.DISTANCE.get();
	}

	@Override
	public int getSeparation() {
		return Config.STRUCTURES.BLUE_RUNE.SEPARATION.get();
	}

	@Override
	protected boolean isFeatureChunk(ChunkGenerator generator, BiomeProvider provider, long seed, SharedSeedRandom rand,
			int chunkX, int chunkZ, Biome biome, ChunkPos pos, NoFeatureConfig config) {
		if (CommonEvents.ForgeEventBus.USE_HOUSE) {
			return super.isFeatureChunk(generator, provider, seed, rand, chunkX, chunkZ, biome, pos, config);
		}

		// Check the entire size of the structure to see if it's all a viable biome:
		for (Biome biome1 : provider.getBiomesWithin(chunkX * 16 + 9, generator.getGenDepth(), chunkZ * 16 + 9,
				getSize() * 16)) {
			if (!biome1.getGenerationSettings().isValidStart(this)) {
				return false;
			}
		}

		int i = chunkX >> 4;
		int j = chunkZ >> 4;
		rand.setSeed((long) (i ^ j << 4) ^ seed);
		rand.nextInt();
		return rand.nextDouble() < getSpawnChance();
	}

	@Override
	@Nonnull
	public IStartFactory<NoFeatureConfig> getStartFactory() {
		return Start::new;
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
			LOGGER.info("Use House usage: {}", CommonEvents.ForgeEventBus.USE_HOUSE);
			if (CommonEvents.ForgeEventBus.USE_HOUSE)
				HousePieces.generate(templateManager, blockpos, rotation, this.pieces, this.random);
			else
				VikingShipPieces.generate(templateManager, blockpos, rotation, this.pieces, this.random);
			this.calculateBoundingBox();
		}

		public boolean needsGround() {
			return false;
		}
	}
}