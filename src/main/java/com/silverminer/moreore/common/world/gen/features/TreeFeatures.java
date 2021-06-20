package com.silverminer.moreore.common.world.gen.features;

import com.google.common.collect.ImmutableList;
import com.silverminer.moreore.common.world.gen.tree.GoldTree;
import com.silverminer.moreore.common.world.gen.tree.SilverTree;
import com.silverminer.moreore.common.world.gen.tree.TreeUtils;

import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.MultipleRandomFeatureConfig;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.Placement;

public class TreeFeatures {
	public static final ConfiguredFeature<?, ?> SILVER_GOLD_TREE_RANDOM = TreeUtils.register("silver_gold_tree_random",
			Feature.RANDOM_SELECTOR
					.configured(new MultipleRandomFeatureConfig(
							ImmutableList.of(GoldTree.GOLD_TREE_RANDOM.weighted(0.1F)),
							SilverTree.SILVER_TREE_RANDOM))
					.decorated(Features.Placements.HEIGHTMAP)
					.decorated(Placement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(10, 0.1F, 1))));
}