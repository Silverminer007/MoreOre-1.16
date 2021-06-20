package com.silverminer.moreore.common.objects.blocks;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.WetSpongeBlock;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BurnedOutLavaSpongeBlock extends WetSpongeBlock {

	public BurnedOutLavaSpongeBlock(Properties properties) {
		super(properties);
	}

	/**
	 * Called periodically clientside on blocks near the player to show effects
	 * (like furnace fire particles). Note that this method is unrelated to
	 * {@link randomTick} and {@link #needsRandomTick}, and will always be called
	 * regardless of whether the block can receive random update ticks
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		Direction direction = Direction.getRandom(rand);
		if (direction != Direction.UP) {
			BlockPos blockpos = pos.relative(direction);
			BlockState blockstate = worldIn.getBlockState(blockpos);
			if (!stateIn.canOcclude() || !blockstate.isFaceSturdy(worldIn, blockpos, direction.getOpposite())) {
				double d0 = (double) pos.getX();
				double d1 = (double) pos.getY();
				double d2 = (double) pos.getZ();
				if (direction == Direction.DOWN) {
					d1 = d1 - 0.05D;
					d0 += rand.nextDouble();
					d2 += rand.nextDouble();
				} else {
					d1 = d1 + rand.nextDouble() * 0.8D;
					if (direction.getAxis() == Direction.Axis.X) {
						d2 += rand.nextDouble();
						if (direction == Direction.EAST) {
							++d0;
						} else {
							d0 += 0.05D;
						}
					} else {
						d0 += rand.nextDouble();
						if (direction == Direction.SOUTH) {
							++d2;
						} else {
							d2 += 0.05D;
						}
					}
				}

				worldIn.addParticle(ParticleTypes.DRIPPING_LAVA, d0, d1, d2, 0.0D, 0.0D, 0.0D);
			}
		}
	}
}
