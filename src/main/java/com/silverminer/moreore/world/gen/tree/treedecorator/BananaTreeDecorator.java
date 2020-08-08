package com.silverminer.moreore.world.gen.tree.treedecorator;

import java.util.List;
import java.util.Random;
import java.util.Set;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.silverminer.moreore.init.blocks.BiologicBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

public class BananaTreeDecorator extends TreeDecorator {
	public static final Codec<BananaTreeDecorator> field_236866_a_ = Codec.FLOAT.fieldOf("probability")
			.xmap(BananaTreeDecorator::new, (p_236868_0_) -> {
				return p_236868_0_.probability;
			}).codec();
	private final float probability;

	public BananaTreeDecorator(float probabilityIn) {
//		super(ModTreeDecoratorType.BANANA);
		this.probability = probabilityIn;
	}

	public <T> BananaTreeDecorator(Dynamic<T> p_i225867_1_) {
		this(p_i225867_1_.get("probability").asFloat(0.0F));
	}

	public void func_225576_a_(IWorld p_225576_1_, Random p_225576_2_, List<BlockPos> p_225576_3_,
			List<BlockPos> p_225576_4_, Set<BlockPos> p_225576_5_, MutableBoundingBox p_225576_6_) {
		if (!(p_225576_2_.nextFloat() >= this.probability)) {
			int i = p_225576_3_.get(0).getY();
			p_225576_3_.stream().filter((p_236867_1_) -> {
				return p_236867_1_.getY() - i <= 2;
			}).forEach((p_236869_5_) -> {
				for (Direction direction : Direction.Plane.HORIZONTAL) {
					if (p_225576_2_.nextFloat() <= 0.25F) {
						Direction direction1 = direction.getOpposite();
						BlockPos blockpos = p_236869_5_.add(direction1.getXOffset(), 0, direction1.getZOffset());
						if (Feature.func_236297_b_(p_225576_1_, blockpos)) {
							BlockState blockstate = BiologicBlocks.BANANA.get().getDefaultState();
							this.func_227423_a_(p_225576_1_, blockpos, blockstate, p_225576_5_, p_225576_6_);
						}
					}
				}

			});
		}
	}

	@Override
	protected TreeDecoratorType<?> func_230380_a_() {
		return ModTreeDecoratorType.BANANA;
	}
}