package com.silverminer.moreore.objects.blocks;

import java.util.HashMap;
import java.util.Map;

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

	public static Map<Block, Block> LOG_AND_STRIPPED_LOG = new HashMap<Block, Block>();

	/**
	 * Use this for normal Logs. You have to add it to the Minecraft "logs.json" tag.
	 * To make it Stripe able use the {@link ModLogBlock#addLogStrippeAble} Method, while the {@link FMLLoadCompleteEvent} event Phase
	 * See the Method for more info
	 * 
	 * @param verticalColorIn
	 * @param properties
	 */
	public ModLogBlock(Properties properties) {
		super(properties);
	}

	/**
	 * This Method makes sure that the Block is Stripped when it is clicked with an axe in the hand
	 * 
	 * The Error is that is Possible to stripe the log with the hand, when the axe is in the off hand
	 */
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit) {
		if (!(player.getHeldItemMainhand().getItem() instanceof AxeItem
				|| player.getHeldItemOffhand().getItem() instanceof AxeItem)) {
			return ActionResultType.PASS;
		}
		if (LOG_AND_STRIPPED_LOG.containsKey(state.getBlock())) {
			worldIn.setBlockState(pos, LOG_AND_STRIPPED_LOG.get(state.getBlock()).getDefaultState().with(AXIS, state.get(AXIS)));
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

	/**
	 * Use this Method while {@link FMLLoadCompleteEvent} event Phase to make a logBlock stripe able
	 * 
	 * @param logBlock This is the log
	 * @param strippedLogBlock This is the stripped log
	 */
	public static void addLogStrippeAble(ModLogBlock logBlock, ModLogBlock strippedLogBlock) {
		LOG_AND_STRIPPED_LOG.put(logBlock, strippedLogBlock);
	}
}