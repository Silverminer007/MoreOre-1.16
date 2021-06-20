package com.silverminer.moreore.common.world.gen.tree;

import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.silverminer.moreore.init.blocks.BiologicBlocks;

import net.minecraft.block.Blocks;
import net.minecraft.block.trees.BigTree;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.MultipleRandomFeatureConfig;
import net.minecraft.world.gen.feature.TwoLayerFeature;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.JungleFoliagePlacer;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.trunkplacer.ForkyTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.MegaJungleTrunkPlacer;

public class IceTree extends BigTree {

	public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> ICE_TREE_CONFIG = TreeUtils.register("ice_tree",
			Feature.TREE.configured(
					(new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.ICE.defaultBlockState()),
							new SimpleBlockStateProvider(BiologicBlocks.ICE_LEAVES.get().defaultBlockState()),
							new BlobFoliagePlacer(FeatureSpread.of(2, 1), FeatureSpread.of(0, 2),
									3),
							new ForkyTrunkPlacer(5, 2, 2), new TwoLayerFeature(1, 0, 2))).ignoreVines().build()));

	public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> HUGE_ICE_TREE_CONFIG = TreeUtils
			.register("huge_ice_tree",
					Feature.TREE.configured((new BaseTreeFeatureConfig.Builder(
							new SimpleBlockStateProvider(Blocks.ICE.defaultBlockState()),
							new SimpleBlockStateProvider(BiologicBlocks.ICE_LEAVES.get().defaultBlockState()),
							new JungleFoliagePlacer(FeatureSpread.of(2, 1),
									FeatureSpread.of(0, 2), 3),
							new MegaJungleTrunkPlacer(10, 2, 19), new TwoLayerFeature(1, 1, 2))).build()));

	public static final ConfiguredFeature<?, ?> ICE_TREE_RANDOM = TreeUtils.register("ice_tree_random",
			Feature.RANDOM_SELECTOR
					.configured(new MultipleRandomFeatureConfig(
							ImmutableList.of(HUGE_ICE_TREE_CONFIG.weighted(0.2F)), ICE_TREE_CONFIG))
					.decorated(Features.Placements.HEIGHTMAP)
					.decorated(Placement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(10, 0.1F, 1))));

	@Override
	protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getConfiguredFeature(Random randomIn, boolean b) {
		return ICE_TREE_CONFIG;
	}

	@Override
	public ConfiguredFeature<BaseTreeFeatureConfig, ?> getConfiguredMegaFeature(Random p_225547_1_) {
		return HUGE_ICE_TREE_CONFIG;
	}
}