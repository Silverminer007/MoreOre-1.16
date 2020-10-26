package com.silverminer.moreore.common.objects.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import com.silverminer.moreore.init.blocks.BiologicBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class BananaBlock extends HorizontalBlock implements IGrowable {
	protected static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] {
			Block.makeCuboidShape(6.0D, 4.0D, 6.0D, 10.0D, 16.0D, 10.0D),
			Block.makeCuboidShape(5.0D, 2.0D, 5.0D, 11.0D, 16.0D, 11.0D),
			Block.makeCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D) };
	public static final IntegerProperty AGE = BlockStateProperties.AGE_0_2;

	public BananaBlock(Properties properties) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(HORIZONTAL_FACING, Direction.NORTH).with(AGE,
				Integer.valueOf(0)));
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE_BY_AGE[state.get(AGE)];
	}

	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		return false;
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(HORIZONTAL_FACING, AGE);
	}

	/**
	 * Whether this IGrowable can grow
	 */
	public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return state.get(AGE) < 2;
	}

	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
		return true;
	}

	public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state) {
		worldIn.setBlockState(pos, state.with(AGE, Integer.valueOf(state.get(AGE) + 1)), 2);
	}

	/**
	 * Returns whether or not this block is of a type that needs random ticking.
	 * Called for ref-counting purposes by ExtendedBlockStorage in order to broadly
	 * cull a chunk from the random chunk update list for efficiency's sake.
	 */
	public boolean ticksRandomly(BlockState state) {
		return state.get(AGE) < 2;
	}

	/**
	 * Performs a random tick on a block.
	 */
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		int i = state.get(AGE);
		if (i < 2 && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state,
				worldIn.rand.nextInt(5) == 0)) {
			worldIn.setBlockState(pos, state.with(AGE, Integer.valueOf(i + 1)), 2);
			net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
		}
	}

	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		Block block = worldIn.getBlockState(pos.offset(state.get(HORIZONTAL_FACING))).getBlock();
		return block == BiologicBlocks.SILVER_LOG.get() || block == BiologicBlocks.STRIPPED_SILVER_LOG.get();
	}

	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockState blockstate = this.getDefaultState();
		IWorldReader iworldreader = context.getWorld();
		BlockPos blockpos = context.getPos();

		for (Direction direction : context.getNearestLookingDirections()) {
			if (direction.getAxis().isHorizontal()) {
				blockstate = blockstate.with(HORIZONTAL_FACING, direction);
				if (blockstate.isValidPosition(iworldreader, blockpos)) {
					return blockstate;
				}
			}
		}

		return null;
	}

	/**
	 * Update the provided state given the provided neighbor facing and neighbor
	 * state, returning a new state. For example, fences make their connections to
	 * the passed in state if possible, and wet concrete powder immediately returns
	 * its solidified counterpart. Note that this method should ideally consider
	 * only the specific face passed in.
	 */
	@SuppressWarnings("deprecation")
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn,
			BlockPos currentPos, BlockPos facingPos) {
		return facing == stateIn.get(HORIZONTAL_FACING) && !stateIn.isValidPosition(worldIn, currentPos)
				? Blocks.AIR.getDefaultState()
				: super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}
}