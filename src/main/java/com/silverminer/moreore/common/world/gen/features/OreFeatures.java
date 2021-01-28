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
				.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD,
						OreBlocks.SILVER_ORE.get().getDefaultState(), 9))
				.withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(0, 5, 49))).square().func_242731_b(7));

		RUBIN = register(OreBlocks.RUBIN_ORE.getId(), Feature.ORE
				.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD,
						OreBlocks.RUBIN_ORE.get().getDefaultState(), 12))
				.withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(0, 5, 56))).square().func_242731_b(6));

		SAPHIR = register(OreBlocks.SAPHIR_ORE.getId(), Feature.ORE
				.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD,
						OreBlocks.SAPHIR_ORE.get().getDefaultState(), 12))
				.withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(0, 5, 56))).square().func_242731_b(6));

		ALEXANDRIT = register(OreBlocks.ALEXANDRIT_ORE.getId(), Feature.ORE
				.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD,
						OreBlocks.ALEXANDRIT_ORE.get().getDefaultState(), 3))
				.withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(5, 5, 16))).square().func_242731_b(8));

		RAINBOW = register(OreBlocks.RAINBOW_ORE.getId(), Feature.ORE
				.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD,
						OreBlocks.RAINBOW_ORE.get().getDefaultState(), 2))
				.withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(5, 5, 16))).square().func_242731_b(9));
	}

	private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(ResourceLocation key,
			ConfiguredFeature<FC, ?> feature) {
		return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, key, feature);
	}
}