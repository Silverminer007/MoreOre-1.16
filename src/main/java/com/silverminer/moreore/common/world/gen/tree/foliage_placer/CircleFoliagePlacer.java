package com.silverminer.moreore.common.world.gen.tree.foliage_placer;

import java.util.Random;
import java.util.Set;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FoliagePlacerType;

public class CircleFoliagePlacer extends FoliagePlacer {
	protected final int radius;

	public CircleFoliagePlacer(FeatureSpread p_i241999_1_, FeatureSpread p_i241999_2_, int radius) {
		super(p_i241999_1_, p_i241999_2_);
		this.radius = radius;
	}

	@Override
	protected FoliagePlacerType<?> func_230371_a_() {
		return FoliagePlacerType.BLOB;
	}

	@Override
	protected void func_230372_a_(IWorldGenerationReader world, Random rand, BaseTreeFeatureConfig treeConfig,
			int p_230372_4_, Foliage foliage, int hightMin, int p_230372_7_, Set<BlockPos> positions, int hightMax,
			MutableBoundingBox mbb) {
		BlockPos startPos = foliage.func_236763_a_();
		int startx = startPos.getX();
		int starty = startPos.getY() - 1;
		int startz = startPos.getZ();

		for (int x = startx - radius; x <= startx + radius; x++) {
			for (int y = starty - radius; y <= starty + radius; y++) {
				for (int z = startz - radius; z <= startz + radius; z++) {
					if ((x - startx) * (x - startx) + (y - starty) * (y - starty) + (z - startz) * (z - startz) < radius
							* radius) {
						BlockPos position = new BlockPos(x, y, z);
						if (TreeFeature.isAirOrLeavesAt(world, position)) {
							world.setBlockState(position, treeConfig.leavesProvider.getBlockState(rand, position), 19);
							mbb.expandTo(new MutableBoundingBox(position, position));
							positions.add(position.toImmutable());
						}
					}
				}
			}
		}
	}

	@Override
	public int func_230374_a_(Random rand, int p_230374_2_, BaseTreeFeatureConfig treeConfig) {
		return treeConfig.trunkPlacer.func_236917_a_(rand) - 1;
	}

	@Override
	protected boolean func_230373_a_(Random rand, int p_230373_2_, int p_230373_3_, int p_230373_4_, int p_230373_5_,
			boolean p_230373_6_) {
		return p_230373_2_ == p_230373_5_ && p_230373_4_ == p_230373_5_ && (rand.nextInt(2) == 0 || p_230373_3_ == 0);
	}

}