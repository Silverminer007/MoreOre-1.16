package com.silverminer.moreore.util.helpers;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpawnPositionHelper {
	public static BlockPos calculate(BlockPos pos, World world) {
		boolean placefound = false;
		while(!placefound) {
			int freeBlocks = 0;
			for(int i = 256; i != 0; i--) {
				if(isAir(new BlockPos(pos.getX(), i, pos.getZ()), world)) freeBlocks++;
				else freeBlocks = 0;
				if(freeBlocks == 2) {
					pos = new BlockPos(pos.getX(), i, pos.getZ());
					if(!isSolid(pos.down(), world)) freeBlocks--;
					else {
						placefound = true;
						break;
					}
				}
			}
			if(!placefound){
				pos = new BlockPos(pos.getX() + 1, 256, pos.getZ());
				continue;
			}
		}
		return pos;
	}
	private static boolean isSolid(BlockPos pos, World world) {
		return world.getBlockState(pos).isSolid();
	}
	private static boolean isAir(BlockPos pos, World world) {
		return world.getBlockState(pos) == Blocks.AIR.getDefaultState();
	}
}
