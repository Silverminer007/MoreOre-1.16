package com.silverminer.moreore.common.objects.blocks;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class ModLogBlock extends RotatedPillarBlock {

	public static Map<Block, Supplier<Block>> LOG_AND_STRIPPED_LOG = new HashMap<Block, Supplier<Block>>();

	/**
	 * Use this for normal Logs. You have to add it to the Minecraft "logs.json"
	 * tag. To make it Stripe able use the other Constrcutor. This is used to
	 * register the sptripped varaints
	 * 
	 * @param verticalColorIn
	 * @param properties
	 */
	public ModLogBlock(Properties properties) {
		super(properties);
	}

	/**
	 * For Info look in the other Constructor
	 * @param properties
	 * @param strippedLog
	 */
	public ModLogBlock(Properties properties, Supplier<Block> strippedLog) {
		this(properties);
		LOG_AND_STRIPPED_LOG.put(this, strippedLog);
	}

	/**
	 * This Method makes sure that the Block is Stripped when it is clicked with an
	 * axe in the hand
	 */
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit) {
		// Checks if an Axe is in the Hand where the player clicked with
		if (!((handIn == Hand.MAIN_HAND && player.getMainHandItem().getItem() instanceof AxeItem)
				|| (handIn == Hand.OFF_HAND && player.getOffhandItem().getItem() instanceof AxeItem))) {
			return ActionResultType.PASS;
		}
		if (LOG_AND_STRIPPED_LOG.containsKey(state.getBlock())) {
			Block block = LOG_AND_STRIPPED_LOG.get(state.getBlock()).get();
			// Checks if the Block can be rotated
			if (block.defaultBlockState().getValues().containsKey(AXIS)) {
				worldIn.setBlock(pos, block.defaultBlockState().setValue(AXIS, state.getValue(AXIS)), 3);
			} else {
				worldIn.setBlock(pos, block.defaultBlockState(), 3);
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}
}