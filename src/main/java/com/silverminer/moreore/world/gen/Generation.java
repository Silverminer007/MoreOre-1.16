package com.silverminer.moreore.world.gen;

import com.silverminer.moreore.init.FeatureInit;
import com.silverminer.moreore.init.blocks.InitBlocks;
import com.silverminer.moreore.init.blocks.OreBlocks;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.DepthAverageConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;

public class Generation {
	public static void setupWorldGen() {

		for (Biome biome : ForgeRegistries.BIOMES) {
			if (biome.getCategory() == Category.RIVER) {
				continue;
			}
			if (biome.getCategory() != Category.NETHER && biome.getCategory() != Category.THEEND
					&& biome.getCategory() != Category.OCEAN) {
				addStructure(biome, FeatureInit.TEMPEL.get());
				addStructure(biome, FeatureInit.SCHOOL.get());
				if (biome.getCategory() == Category.DESERT) {
					addStructure(biome, FeatureInit.DESERT_TEMPEL.get());
				}
			}
		}

		/*
		 * ((ModBiome) biome).addSpawn(EntityClassification.CREATURE, new
		 * Biome.SpawnListEntry(ModEntityTypesInit.VILLAGE_GUARDIAN.get(), 1, 1, 3));
		 */
	}

	private static void addStructure(Biome biome, Structure<NoFeatureConfig> structure) {
		biome.func_242440_e().func_242487_a().add(() -> structure.func_236391_a_(IFeatureConfig.NO_FEATURE_CONFIG));
	}

	public static void generateOre() {
		Registry.register(WorldGenRegistries.field_243653_e, OreBlocks.SILVER_ORE.getId(),
				Feature.field_236289_V_
						.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.field_241882_a,
								OreBlocks.SILVER_ORE.get().getDefaultState(), 6))
						.withPlacement(Placement.field_242910_o.configure(new DepthAverageConfig(5, 5))));

		Registry.register(WorldGenRegistries.field_243653_e, InitBlocks.GLOWING_STONE.getId(),
				Feature.field_236289_V_
						.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.field_241882_a,
								InitBlocks.GLOWING_STONE.get().getDefaultState(), 15))
						.withPlacement(Placement.field_242910_o.configure(new DepthAverageConfig(7, 7))));

		Registry.register(WorldGenRegistries.field_243653_e, OreBlocks.RUBIN_ORE.getId(),
				Feature.field_236289_V_
						.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.field_241882_a,
								OreBlocks.RUBIN_ORE.get().getDefaultState(), 6))
						.withPlacement(Placement.field_242910_o.configure(new DepthAverageConfig(5, 5))));

		Registry.register(WorldGenRegistries.field_243653_e, OreBlocks.SAPHIR_ORE.getId(),
				Feature.field_236289_V_
						.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.field_241882_a,
								OreBlocks.SAPHIR_ORE.get().getDefaultState(), 6))
						.withPlacement(Placement.field_242910_o.configure(new DepthAverageConfig(5, 5))));

		Registry.register(WorldGenRegistries.field_243653_e, OreBlocks.ALEXANDRIT_ORE.getId(),
				Feature.field_236289_V_
						.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.field_241882_a,
								OreBlocks.ALEXANDRIT_ORE.get().getDefaultState(), 6))
						.withPlacement(Placement.field_242910_o.configure(new DepthAverageConfig(5, 5))));

		Registry.register(WorldGenRegistries.field_243653_e, OreBlocks.RAINBOW_ORE.getId(),
				Feature.field_236289_V_
						.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.field_241882_a,
								OreBlocks.RAINBOW_ORE.get().getDefaultState(), 6))
						.withPlacement(Placement.field_242910_o.configure(new DepthAverageConfig(4, 4))));
	}
}
