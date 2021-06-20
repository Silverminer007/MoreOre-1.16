package com.silverminer.moreore.common.objects.blocks;

import java.util.Random;

import com.silverminer.moreore.common.objects.entitys.SquirrelEntity;
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

public class NutBushLeavesBlock extends LeavesBlock implements IGrowable {
	public static final IntegerProperty AGE = BlockStateProperties.AGE_1;
	private int timer = 0;

	public NutBushLeavesBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(AGE, 0));
	}

	/**
	 * Whether this IGrowable can grow
	 */
	public boolean isValidBonemealTarget(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return state.getValue(AGE) < 1;
	}

	public boolean isBonemealSuccess(World worldIn, Random rand, BlockPos pos, BlockState state) {
		return true;
	}

	public void performBonemeal(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state) {
		worldIn.setBlock(pos, state.setValue(AGE, Integer.valueOf(state.getValue(AGE) + 1)), 2);
	}

	/**
	 * Returns whether or not this block is of a type that needs random ticking.
	 * Called for ref-counting purposes by ExtendedBlockStorage in order to broadly
	 * cull a chunk from the random chunk update list for efficiency's sake.
	 */
	public boolean isRandomlyTicking(BlockState state) {
		return state.getValue(AGE) < 1 || super.isRandomlyTicking(state);
	}

	/**
	 * Performs a random tick on a block.
	 */
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		if (super.isRandomlyTicking(state)) {
			super.randomTick(state, worldIn, pos, random);
		}
		if (timer % 50 == 0) {
			int i = state.getValue(AGE);
			if (i < 1 && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, true)
					&& worldIn.random.nextInt(1) < 0.02) {
				worldIn.setBlock(pos, state.setValue(AGE, Integer.valueOf(i + 1)), 2);
				timer = 0;
				net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
			}
		} else {
			timer++;
		}
	}

	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder.add(AGE));
	}

	@SuppressWarnings("deprecation")
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit) {
		if (state.getValue(AGE) == 1) {
			int j = 1 + worldIn.random.nextInt(4);
			Block.popResource(worldIn, pos, new ItemStack(TreeItems.NUTS.get(), j));
			worldIn.playSound((PlayerEntity) null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES,
					SoundCategory.BLOCKS, 1.0F, 0.8F + worldIn.random.nextFloat() * 0.4F);
			worldIn.setBlock(pos, state.setValue(AGE, 0), 2);
			return ActionResultType.sidedSuccess(worldIn.isClientSide);
		} else if (player.getItemInHand(handIn).getItem() == Items.BONE_MEAL) {
			return ActionResultType.PASS;
		} else {
			return super.use(state, worldIn, pos, player, handIn, hit);
		}
	}

	@Override
	public boolean isLadder(BlockState state, net.minecraft.world.IWorldReader world, BlockPos pos,
			net.minecraft.entity.LivingEntity entity) {
		return entity instanceof SquirrelEntity && pos.closerThan(entity.position(), 0.5);
	}
}