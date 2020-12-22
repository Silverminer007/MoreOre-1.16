package com.silverminer.moreore.common.world.gen.structures.runes.green;

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

public class GreenDungeonStructure extends AbstractStructure<NoFeatureConfig> {
	public static final String SHORT_NAME = "green_dungeon";

	public GreenDungeonStructure(Codec<NoFeatureConfig> configFactoryIn) {
		super(configFactoryIn, 3, SHORT_NAME);
	}

	@Override
	public double getSpawnChance() {
		return Config.STRUCTURES.GREEN_DUNGEON.SPAWN_CHANCE.get();
	}

	public int getSeedModifier() {
		return Config.STRUCTURES.GREEN_DUNGEON.SEED.get();
	}

	@Override
	public Decoration getDecorationStage() {
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}

	@Override
	public int getDistance() {
		return Config.STRUCTURES.GREEN_DUNGEON.DISTANCE.get();
	}

	@Override
	public int getSeparation() {
		return Config.STRUCTURES.GREEN_DUNGEON.SEPARATION.get();
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
		public void func_230364_a_(DynamicRegistries registries, ChunkGenerator chunkGenerator,
				TemplateManager templateManager, int chunkX, int chunkZ, Biome biome, NoFeatureConfig config) {
			int i = chunkX * 16;
			int j = chunkZ * 16;
			BlockPos blockpos = new BlockPos(i, 0, j);
			Rotation rotation = Rotation.randomRotation(this.rand);
			GreenDungeonPieces.generate(templateManager, blockpos, rotation, this.components, this.rand);
			this.recalculateStructureSize();
		}
	}
}