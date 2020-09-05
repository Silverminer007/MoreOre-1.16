package com.silverminer.moreore.world.gen.tree.treedecorator;

import java.util.List;
import java.util.Random;
import java.util.Set;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.silverminer.moreore.init.TreeDecotratorInit;
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
	public static final Codec<BananaTreeDecorator> CODEC = Codec.FLOAT.fieldOf("probability")
			.xmap(BananaTreeDecorator::new, (decorator) -> {
				return decorator.probability;
			}).codec();
	private final float probability;

	public BananaTreeDecorator(float probabilityIn) {
		super();
		this.probability = probabilityIn;
	}

	public <T> BananaTreeDecorator(Dynamic<T> p_i225867_1_) {
		this(p_i225867_1_.get("probability").asFloat(0.0F));
	}

	public void func_225576_a_(IWorld world, Random random, List<BlockPos> positions1, List<BlockPos> positions2,
			Set<BlockPos> positions3, MutableBoundingBox boundingbox) {
		if (!(random.nextFloat() >= this.probability)) {
			int i = positions1.get(0).getY();
			positions1.stream().filter((pos) -> {
				return pos.getY() - i <= 2;
			}).forEach((pos) -> {
				for (Direction direction : Direction.Plane.HORIZONTAL) {
					if (random.nextFloat() <= 0.25F) {
						Direction direction1 = direction.getOpposite();
						BlockPos blockpos = pos.add(direction1.getXOffset(), 0, direction1.getZOffset());
						if (Feature.func_236297_b_(world, blockpos)) {
							BlockState blockstate = BiologicBlocks.BANANA.get().getDefaultState();
							this.func_227423_a_(world, blockpos, blockstate, positions3, boundingbox);
						}
					}
				}

			});
		}
	}

	@Override
	protected TreeDecoratorType<?> func_230380_a_() {
		return TreeDecotratorInit.BANANA.get();
	}
}