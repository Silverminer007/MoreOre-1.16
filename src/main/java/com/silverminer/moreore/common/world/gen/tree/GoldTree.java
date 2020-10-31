package com.silverminer.moreore.common.world.gen.tree;

import java.util.Random;

import com.silverminer.moreore.init.blocks.BiologicBlocks;
import com.silverminer.moreore.init.blocks.OreBlocks;

import net.minecraft.block.trees.BigTree;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.feature.TwoLayerFeature;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.JungleFoliagePlacer;
import net.minecraft.world.gen.trunkplacer.ForkyTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.MegaJungleTrunkPlacer;

public class GoldTree extends BigTree {
	public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> GOLD_TREE_CONFIG = TreeUtils
			.register("gold_tree",
					Feature.TREE.withConfiguration((new BaseTreeFeatureConfig.Builder(
							new SimpleBlockStateProvider(OreBlocks.CAT_GOLD_BLOCK.get().getDefaultState()),
							new SimpleBlockStateProvider(BiologicBlocks.GOLD_LEAVES.get().getDefaultState()),
							new BlobFoliagePlacer(FeatureSpread.func_242253_a(2, 1), FeatureSpread.func_242253_a(0, 2),
									3),
							new ForkyTrunkPlacer(5, 2, 2), new TwoLayerFeature(1, 0, 2)).setIgnoreVines().build())));

	public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> HUGE_GOLD_TREE_CONFIG = TreeUtils.register(
			"huge_gold_tree",
			Feature.TREE.withConfiguration((new BaseTreeFeatureConfig.Builder(
					new SimpleBlockStateProvider(OreBlocks.CAT_GOLD_BLOCK.get().getDefaultState()),
					new SimpleBlockStateProvider(BiologicBlocks.GOLD_LEAVES.get().getDefaultState()),
					new JungleFoliagePlacer(FeatureSpread.func_242253_a(2, 1), FeatureSpread.func_242253_a(0, 2), 3),
					new MegaJungleTrunkPlacer(10, 2, 19), new TwoLayerFeature(1, 1, 2))).build()));

	@Override
	public ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean p_225546_2_) {
		return GOLD_TREE_CONFIG;
	}

	@Override
	protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getHugeTreeFeature(Random p_225547_1_) {
		return HUGE_GOLD_TREE_CONFIG;
	}

}
