package com.silverminer.moreore.world.gen.tree;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class TreeUtils {
	public static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String name,
			ConfiguredFeature<FC, ?> feature) {
		return Registry.register(WorldGenRegistries.field_243653_e, name, feature);
	}
}