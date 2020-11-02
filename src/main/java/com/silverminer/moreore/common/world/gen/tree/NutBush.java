package com.silverminer.moreore.common.world.gen.tree;

import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.silverminer.moreore.common.world.gen.tree.foliage_placer.CircleFoliagePlacer;
import com.silverminer.moreore.init.blocks.BiologicBlocks;

import net.minecraft.block.trees.BigTree;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.MultipleRandomFeatureConfig;
import net.minecraft.world.gen.feature.TwoLayerFeature;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;

public class NutBush extends BigTree {

	public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> SMALL_NUT_BUSH_CONFIG = TreeUtils.register(
			"small_nut_bush",
			Feature.TREE.withConfiguration((new BaseTreeFeatureConfig.Builder(
					new SimpleBlockStateProvider(BiologicBlocks.NUT_BUSH_LOG.get().getDefaultState()),
					new SimpleBlockStateProvider(BiologicBlocks.NUT_BUSH_LEAVES.get().getDefaultState()),
					new CircleFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0), 2),
					new StraightTrunkPlacer(2, 0, 0), new TwoLayerFeature(1, 0, 1))).setIgnoreVines().build()));

	public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> NUT_BUSH_CONFIG = TreeUtils.register("nut_bush",
			Feature.TREE.withConfiguration((new BaseTreeFeatureConfig.Builder(
					new SimpleBlockStateProvider(BiologicBlocks.NUT_BUSH_LOG.get().getDefaultState()),
					new SimpleBlockStateProvider(BiologicBlocks.NUT_BUSH_LEAVES.get().getDefaultState()),
					new CircleFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0), 3),
					new StraightTrunkPlacer(4, 0, 0), new TwoLayerFeature(1, 0, 1))).setIgnoreVines().build()));

	public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> HUGE_NUT_BUSH_CONFIG = TreeUtils.register(
			"huge_nut_bush",
			Feature.TREE.withConfiguration((new BaseTreeFeatureConfig.Builder(
					new SimpleBlockStateProvider(BiologicBlocks.NUT_BUSH_LOG.get().getDefaultState()),
					new SimpleBlockStateProvider(BiologicBlocks.NUT_BUSH_LEAVES.get().getDefaultState()),
					new CircleFoliagePlacer(FeatureSpread.func_242252_a(3), FeatureSpread.func_242252_a(0), 4),
					new StraightTrunkPlacer(6, 0, 0), new TwoLayerFeature(1, 0, 1))).setIgnoreVines().build()));

	public static final ConfiguredFeature<?, ?> NUT_BUSH_RANDOM = TreeUtils.register("nut_bush_random",
			Feature.RANDOM_SELECTOR
					.withConfiguration(
							new MultipleRandomFeatureConfig(ImmutableList.of(HUGE_NUT_BUSH_CONFIG.withChance(0.2F),
									SMALL_NUT_BUSH_CONFIG.withChance(0.2F)), NUT_BUSH_CONFIG))
					.withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)
					.withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(10, 0.1F, 1))));

	@Override
	public ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean p_225546_2_) {
		return randomIn.nextInt() < 0.7 ? NUT_BUSH_CONFIG : SMALL_NUT_BUSH_CONFIG;
	}

	@Override
	protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getHugeTreeFeature(Random p_225547_1_) {
		return HUGE_NUT_BUSH_CONFIG;
	}

}
