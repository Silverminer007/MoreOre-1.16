package com.silverminer.moreore.common.world.gen.tree.treedecorator;

import java.util.List;
import java.util.Random;
import java.util.Set;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.silverminer.moreore.common.objects.blocks.BananaBlock;
import com.silverminer.moreore.init.TreeDecotratorInit;
import com.silverminer.moreore.init.blocks.BiologicBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

public class BananaTreeDecorator extends TreeDecorator {
	public static final BananaTreeDecorator DECORATOR = new BananaTreeDecorator(0.2F);
	public static final Codec<BananaTreeDecorator> CODEC = Codec.FLOAT.fieldOf("probability")
			.xmap(BananaTreeDecorator::new, (decorator) -> {
				return decorator.probability;
			}).codec();
	private final float probability;

	public BananaTreeDecorator(float probabilityIn) {
		super();
		this.probability = probabilityIn;
	}

	public <T> BananaTreeDecorator(Dynamic<T> dynamic) {
		this(dynamic.get("probability").asFloat(0.3F));
	}

	@Override
	protected TreeDecoratorType<?> func_230380_a_() {
		return TreeDecotratorInit.BANANA.get();
	}

	@Override
	public void func_225576_a_(ISeedReader seedReader, Random random, List<BlockPos> positions1,
			List<BlockPos> positions2, Set<BlockPos> positions3, MutableBoundingBox boundingbox) {
		if (!(random.nextFloat() >= this.probability)) {
			int i = positions1.get(0).getY();
			positions1.stream().filter((pos) -> {
				return pos.getY() - i <= 2;
			}).forEach((pos) -> {
				for (Direction direction : Direction.Plane.HORIZONTAL) {
					if (random.nextFloat() <= 0.25F) {
						Direction direction1 = direction.getOpposite();
						BlockPos blockpos = pos.add(direction1.getXOffset(), 0, direction1.getZOffset());
						if (Feature.isAirAt(seedReader, blockpos)) {
							BlockState blockstate = BiologicBlocks.BANANA.get().getDefaultState()
									.with(BananaBlock.AGE, random.nextInt(3) - 1)
									.with(BananaBlock.HORIZONTAL_FACING, Direction.Plane.HORIZONTAL.random(random));
							this.func_227423_a_(seedReader, blockpos, blockstate, positions3, boundingbox);
						}
					}
				}

			});
		}
	}
}