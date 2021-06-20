package com.silverminer.moreore.common.world.gen.tree.foliage_placer;

import java.util.Random;
import java.util.Set;

import com.mojang.datafixers.Products.P3;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.RecordCodecBuilder.Instance;
import com.mojang.serialization.codecs.RecordCodecBuilder.Mu;
import com.silverminer.moreore.init.FoliagePlacerTypeInit;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FoliagePlacerType;

public class CircleFoliagePlacer extends FoliagePlacer {

	public static final Codec<CircleFoliagePlacer> CODEC = RecordCodecBuilder.create((p_236742_0_) -> {
		return circleParts(p_236742_0_).apply(p_236742_0_, CircleFoliagePlacer::new);
	});
	protected final int radius;

	public CircleFoliagePlacer(FeatureSpread p_i241999_1_, FeatureSpread p_i241999_2_, int radius) {
		super(p_i241999_1_, p_i241999_2_);
		this.radius = radius;
	}

	protected static <P extends CircleFoliagePlacer> P3<Mu<P>, FeatureSpread, FeatureSpread, Integer> circleParts(
			Instance<P> instance) {
		return foliagePlacerParts(instance).and(Codec.intRange(0, 16).fieldOf("radius").forGetter((foliagePlacer) -> {
			return foliagePlacer.radius;
		}));
	}

	@Override
	protected void createFoliage(IWorldGenerationReader world, Random rand, BaseTreeFeatureConfig treeConfig,
			int p_230372_4_, Foliage foliage, int hightMin, int p_230372_7_, Set<BlockPos> positions, int hightMax,
			MutableBoundingBox mbb) {
		BlockPos startPos = foliage.foliagePos();
		int startx = startPos.getX();
		int starty = startPos.getY() - 1;
		int startz = startPos.getZ();

		for (int x = startx - radius; x <= startx + radius; x++) {
			for (int y = starty - radius; y <= starty + radius; y++) {
				for (int z = startz - radius; z <= startz + radius; z++) {
					if ((x - startx) * (x - startx) + (y - starty) * (y - starty) + (z - startz) * (z - startz) < radius
							* radius) {
						BlockPos position = new BlockPos(x, y, z);
						if (TreeFeature.isAirOrLeaves(world, position)) {
							world.setBlock(position, treeConfig.leavesProvider.getState(rand, position), 19);
							mbb.expand(new MutableBoundingBox(position, position));
							positions.add(position.immutable());
						}
					}
				}
			}
		}
	}

	@Override
	public int foliageHeight(Random rand, int p_230374_2_, BaseTreeFeatureConfig treeConfig) {
		return treeConfig.trunkPlacer.getTreeHeight(rand) - 1;
	}

	@Override
	protected FoliagePlacerType<?> type() {
		return FoliagePlacerTypeInit.CIRCLE.get();
	}

	protected boolean shouldSkipLocation(Random p_230373_1_, int p_230373_2_, int p_230373_3_, int p_230373_4_,
			int p_230373_5_, boolean p_230373_6_) {
		return p_230373_2_ == p_230373_5_ && p_230373_4_ == p_230373_5_
				&& (p_230373_1_.nextInt(2) == 0 || p_230373_3_ == 0);
	}
}