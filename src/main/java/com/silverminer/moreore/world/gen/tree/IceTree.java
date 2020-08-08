package com.silverminer.moreore.world.gen.tree;

import java.util.Random;

import com.silverminer.moreore.init.blocks.BiologicBlocks;

import net.minecraft.block.Blocks;
import net.minecraft.block.trees.BigTree;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TwoLayerFeature;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.JungleFoliagePlacer;
import net.minecraft.world.gen.trunkplacer.ForkyTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.MegaJungleTrunkPlacer;

public class IceTree extends BigTree{
	
	public static final BaseTreeFeatureConfig ICE_TREE_CONFIG =(new BaseTreeFeatureConfig.Builder(
			new SimpleBlockStateProvider(Blocks.ICE.getDefaultState()),
			new SimpleBlockStateProvider(BiologicBlocks.ICE_LEAVES.get().getDefaultState()),
			new BlobFoliagePlacer(2, 0, 0, 0, 3), new ForkyTrunkPlacer(5, 2, 2), new TwoLayerFeature(1, 0, 2)))
					.func_236700_a_().build();

	public static final BaseTreeFeatureConfig HUGE_ICE_TREE_CONFIG = (new BaseTreeFeatureConfig.Builder(
			new SimpleBlockStateProvider(Blocks.ICE.getDefaultState()),
			new SimpleBlockStateProvider(BiologicBlocks.ICE_LEAVES.get().getDefaultState()),
			new JungleFoliagePlacer(2, 0, 0, 0, 2), new MegaJungleTrunkPlacer(10, 2, 19), new TwoLayerFeature(1, 1, 2)))
					.build();

	@Override
	protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean b) {
		return Feature.field_236291_c_.withConfiguration(ICE_TREE_CONFIG);
	}
	@Override
	public ConfiguredFeature<BaseTreeFeatureConfig, ?> getHugeTreeFeature(Random p_225547_1_) {
		return Feature.field_236291_c_.withConfiguration(HUGE_ICE_TREE_CONFIG);
	}
}