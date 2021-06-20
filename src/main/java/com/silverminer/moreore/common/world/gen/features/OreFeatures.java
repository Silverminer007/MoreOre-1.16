package com.silverminer.moreore.common.world.gen.features;

import com.silverminer.moreore.init.blocks.OreBlocks;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;

public class OreFeatures {

	public static ConfiguredFeature<?, ?> ALEXANDRIT;

	public static ConfiguredFeature<?, ?> SILVER;

	public static ConfiguredFeature<?, ?> RUBIN;

	public static ConfiguredFeature<?, ?> SAPHIR;

	public static ConfiguredFeature<?, ?> RAINBOW;

	public static void registerOres() {
		SILVER = register(OreBlocks.SILVER_ORE.getId(), Feature.ORE
				.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE,
						OreBlocks.SILVER_ORE.get().defaultBlockState(), 9))
				.decorated(Placement.RANGE.configured(new TopSolidRangeConfig(0, 5, 49))).squared().count(7));

		RUBIN = register(OreBlocks.RUBIN_ORE.getId(), Feature.ORE
				.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE,
						OreBlocks.RUBIN_ORE.get().defaultBlockState(), 12))
				.decorated(Placement.RANGE.configured(new TopSolidRangeConfig(0, 5, 56))).squared().count(6));

		SAPHIR = register(OreBlocks.SAPHIR_ORE.getId(), Feature.ORE
				.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE,
						OreBlocks.SAPHIR_ORE.get().defaultBlockState(), 12))
				.decorated(Placement.RANGE.configured(new TopSolidRangeConfig(0, 5, 56))).squared().count(6));

		ALEXANDRIT = register(OreBlocks.ALEXANDRIT_ORE.getId(), Feature.ORE
				.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE,
						OreBlocks.ALEXANDRIT_ORE.get().defaultBlockState(), 3))
				.decorated(Placement.RANGE.configured(new TopSolidRangeConfig(5, 5, 16))).squared().count(8));

		RAINBOW = register(OreBlocks.RAINBOW_ORE.getId(), Feature.ORE
				.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE,
						OreBlocks.RAINBOW_ORE.get().defaultBlockState(), 2))
				.decorated(Placement.RANGE.configured(new TopSolidRangeConfig(5, 5, 16))).squared().count(9));
	}

	private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(ResourceLocation key,
			ConfiguredFeature<FC, ?> feature) {
		return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, key, feature);
	}
}