package com.silverminer.moreore.world.gen;

import com.silverminer.moreore.init.BiomeInit;
import com.silverminer.moreore.init.FeatureInit;
import com.silverminer.moreore.init.ModEntityTypesInit;
import com.silverminer.moreore.init.blocks.InitBlocks;
import com.silverminer.moreore.init.blocks.OreBlocks;
import com.silverminer.moreore.world.biomes.GoldenMountains;
import com.silverminer.moreore.world.biomes.SilverTale;
import com.silverminer.moreore.world.biomes.ModBiome;
import com.silverminer.moreore.world.gen.structures.StructureGenConfig;

import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.DesertBiome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
//import net.minecraft.world.gen.feature.structure.MineshaftConfig;//Belongs Mineshaft generation
//import net.minecraft.world.gen.feature.structure.MineshaftStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;

public class Generation {
	public static void setupWorldGen() {
		for (Biome biome : ForgeRegistries.BIOMES) {
			if (biome == Biomes.RIVER) {
				continue;
			}
			if (biome instanceof SilverTale || biome instanceof GoldenMountains) {
				if (StructureGenConfig.GENERATE_TEMPELS.get()) {
					addSurfaceStructure(biome, FeatureInit.TEMPEL.get());
				}
				if (StructureGenConfig.GENERATE_SCHOOLS.get()) {
					addSurfaceStructure(biome, FeatureInit.SCHOOL.get());
				}
				((ModBiome) biome).addSpawn(EntityClassification.CREATURE,
						new Biome.SpawnListEntry(ModEntityTypesInit.VILLAGE_GUARDIAN.get(), 1, 1, 3));
//				addStructure(biome, Feature.MINESHAFT, new MineshaftConfig(0.004D, MineshaftStructure.Type.NORMAL),
//						GenerationStage.Decoration.UNDERGROUND_STRUCTURES);//Nur unter den Inseln
			}
			if (biome instanceof DesertBiome) {
				if (StructureGenConfig.GENERATE_DESERT_TEMPEL.get()) {
					addSurfaceStructure(biome, FeatureInit.DESERT_TEMPEL.get());
				}
			}
		}
	}

	@SuppressWarnings("unused")
	private static <P extends IFeatureConfig> void addStructure(Biome biome, Structure<P> structure, P config,
			GenerationStage.Decoration deco) {
		biome.func_235063_a_(structure.func_236391_a_(config));
//		biome.addFeature(deco, structure.func_236391_a_(config)
//				.withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
	}

	private static void addSurfaceStructure(Biome biome, Structure<NoFeatureConfig> structure) {
		biome.func_235063_a_(structure.func_236391_a_(IFeatureConfig.NO_FEATURE_CONFIG));
//		biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES,
//				structure.func_236391_a_(IFeatureConfig.NO_FEATURE_CONFIG)
//						.withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
	}

	public static void generateOre() {
		for (Biome biome : ForgeRegistries.BIOMES) {
			if (biome == BiomeInit.SILVERTALE.get()) {
				biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
						Feature.ORE
								.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE,
										OreBlocks.SILVER_ORE.get().getDefaultState(), 6))
								.withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(5, 0, 0, 60))));

				biome.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION,
						Feature.ORE
								.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE,
										InitBlocks.GLOWING_STONE.get().getDefaultState(), 15))
								.withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(20, 0, 0, 60))));
			}
			if (biome == BiomeInit.DEATH_ICE_TALE.get()) {
				biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
						Feature.ORE
								.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE,
										OreBlocks.RUBIN_ORE.get().getDefaultState(), 6))
								.withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(5, 0, 0, 60))));
				biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
						Feature.ORE
								.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE,
										OreBlocks.SAPHIR_ORE.get().getDefaultState(), 6))
								.withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(5, 0, 0, 60))));
			}
			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
					Feature.ORE
							.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE,
									OreBlocks.ALEXANDRIT_ORE.get().getDefaultState(), 6))
							.withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(5, 0, 0, 60))));

			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
					Feature.ORE
							.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE,
									OreBlocks.RAINBOW_ORE.get().getDefaultState(), 6))
							.withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(4, 0, 0, 45))));
		}
	}
}
