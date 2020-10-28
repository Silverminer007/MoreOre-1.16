package com.silverminer.moreore.common.objects.blocks;

import java.util.Random;

import com.silverminer.moreore.init.items.TreeItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class NutLeavesBlock extends LeavesBlock implements IGrowable {
	public static final IntegerProperty AGE = BlockStateProperties.AGE_0_1;
	private int timer = 0;

	public NutLeavesBlock(Properties properties) {
		super(properties);
		this.setDefaultState(this.getDefaultState().with(AGE, 0));
	}

	/**
	 * Whether this IGrowable can grow
	 */
	public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return state.get(AGE) < 1;
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
		return state.get(AGE) < 1 || super.ticksRandomly(state);
	}

	/**
	 * Performs a random tick on a block.
	 */
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		if (super.ticksRandomly(state)) {
			super.randomTick(state, worldIn, pos, random);
		} else {
			if (timer % 50 == 0) {
				int i = state.get(AGE);
				if (i < 1 && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state,
						true) && worldIn.rand.nextInt(1) < 0.02) {
					worldIn.setBlockState(pos, state.with(AGE, Integer.valueOf(i + 1)), 2);
					timer = 0;
					net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
				}
			} else {
				timer++;
			}
		}
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder.add(AGE));
	}

	@SuppressWarnings("deprecation")
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit) {
		if (state.get(AGE) == 1) {
			int j = 1 + worldIn.rand.nextInt(4);
			spawnAsEntity(worldIn, pos, new ItemStack(TreeItems.NUTS.get(), j));
			worldIn.playSound((PlayerEntity) null, pos, SoundEvents.ITEM_SWEET_BERRIES_PICK_FROM_BUSH,
					SoundCategory.BLOCKS, 1.0F, 0.8F + worldIn.rand.nextFloat() * 0.4F);
			worldIn.setBlockState(pos, state.with(AGE, 0), 2);
			return ActionResultType.func_233537_a_(worldIn.isRemote);
		} else if (player.getHeldItem(handIn).getItem() == Items.BONE_MEAL) {
			return ActionResultType.PASS;
		} else {
			return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
		}
	}
}