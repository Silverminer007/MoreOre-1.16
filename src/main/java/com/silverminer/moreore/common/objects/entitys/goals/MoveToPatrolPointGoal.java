package com.silverminer.moreore.common.objects.entitys.goals;

import net.minecraft.block.Block;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;

public class MoveToPatrolPointGoal extends MoveToBlockGoal {
	private final Block block;
	private final MobEntity entity;
	private BlockPos invalidPos = BlockPos.ZERO;
	private final int searchLength;
	private int delay = 100;

	public MoveToPatrolPointGoal(Block blockIn, CreatureEntity creature, double speed, int yMax) {
		super(creature, speed, 50, yMax);
		this.searchLength = 50;
		this.block = blockIn;
		this.entity = creature;
		this.invalidPos = new BlockPos(entity.position());
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by
	 * another one
	 */
	public void start() {
		super.start();
		this.entity.fallDistance = 1.0F;
	}

	public void tick() {
		super.tick();
		if (this.isReachedTarget() && this.isDelayZero()) {
			this.invalidPos = this.blockPos;
		}
		this.searchForDestination();
	}

	private boolean isDelayZero() {
		if (this.delay == 0) {
			this.delay = 100;
			return true;
		} else {
			this.delay = this.delay - 1;
		}
		return false;
	}

	/**
	 * Return true to set given position as destination
	 */
	@SuppressWarnings("deprecation")
	protected boolean isValidTarget(IWorldReader worldIn, BlockPos pos) {
		IChunk ichunk = worldIn.getChunk(pos.getX() >> 4, pos.getZ() >> 4, ChunkStatus.FULL, false);
		if (ichunk == null) {
			return false;
		} else {
			boolean isPatrolPoint = ichunk.getBlockState(pos).getBlock() == this.block;
			boolean isAirUp = ichunk.getBlockState(pos.above()).isAir(ichunk, pos)
					&& ichunk.getBlockState(pos.above(2)).isAir(ichunk, pos);
			return isPatrolPoint && isAirUp;
		}
	}

	protected boolean searchForDestination() {
		int i = this.searchLength;
		BlockPos blockpos = new BlockPos(this.entity.position());
		World world = this.entity.level;
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

		for (int x = 0; x <= i; x = x > 0 ? -x : 1 - x) {
			for (int y = 0; y <= i; y = y > 0 ? -y : 1 - y) {
				for (int z = 0; z <= i; z = z > 0 ? -z : 1 - z) {
					blockpos$mutable.set(blockpos).move(x, y - 1, z);
					if (this.isValidTarget(world, blockpos$mutable)) {
						if (this.entity.isWithinRestriction(blockpos$mutable)) {
							if (this.invalidPos.getX() == blockpos$mutable.getX()
									&& this.invalidPos.getY() == blockpos$mutable.getY()
									&& this.invalidPos.getZ() == blockpos$mutable.getZ()) {
								continue;
							} else {
								this.blockPos = blockpos$mutable;
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
}