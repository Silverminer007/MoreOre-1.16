package com.silverminer.moreore.objects.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class BananaBlock extends Block {
	public static final Direction[] GENERATE_DIRECTIONS = new Direction[] { Direction.WEST, Direction.EAST,
			Direction.SOUTH , Direction.NORTH};
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(4.0D, 2.0D, 4.0D, 12.0D, 14.0D, 12.0D);

	public BananaBlock(Properties properties) {
		super(properties);
	}

	public static Direction func_235331_a_(Random rand) {
		return Util.func_240989_a_(GENERATE_DIRECTIONS, rand);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		return false;
	}
}