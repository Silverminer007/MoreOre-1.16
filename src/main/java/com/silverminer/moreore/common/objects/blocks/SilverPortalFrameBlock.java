package com.silverminer.moreore.common.objects.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import java.util.List;

import com.silverminer.moreore.common.portal.Portal;
import com.silverminer.moreore.common.portal.PortalRegistry;
import com.silverminer.moreore.init.items.ToolItems;

/**
 * Represents the frame of the portal mutliblock.
 */
public class SilverPortalFrameBlock extends Block {
	public SilverPortalFrameBlock(Block.Properties properties)
	{
		super(properties);
	}

	@SuppressWarnings("deprecation")
	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		if (!world.isRemote)
		{
			ItemStack heldStack = player.getHeldItem(hand);
			Item usedItem = heldStack.getItem();

			if (usedItem == ToolItems.GOLDEN_TRIDENT.get()){
				if (player.isSneaking()) {
					world.destroyBlock(pos, true);
					this.onReplaced(world.getBlockState(pos), world, pos, Blocks.AIR.getDefaultState(), false);
				}
				else if (!PortalRegistry.isPortalAt(pos, player.world.getDimensionKey())){
					PortalRegistry.activatePortal(world, pos, hit.getFace());
				}
			}
		}

		return super.onBlockActivated(state, world, pos, player, hand, hit);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onReplaced(BlockState oldState, World world, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (!world.isRemote)
		{
			// Deactivate damaged portals.

			List<Portal> affectedPortals = PortalRegistry.getPortalsAt(pos, world.getDimensionKey());
			if (affectedPortals == null || affectedPortals.size() < 1) return;
			Portal firstPortal = affectedPortals.get(0);

			if (firstPortal.isDamaged(world))
			{
				PortalRegistry.deactivatePortal(world, pos);
			}
		}

		super.onReplaced(oldState, world, pos, newState, isMoving);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean isMoving)
	{
		if (!world.isRemote	&&
			!neighborBlock.getDefaultState().isAir() &&	// I'd like to supply a proper BlockState here but the new block has already been placed, so there's no way.
			!(neighborBlock instanceof SilverPortalBlock)	&&
			!(neighborBlock instanceof SilverPortalFrameBlock)){
			// Deactivate all portals that share this frame block if an address block was removed or changed.

			List<Portal> affectedPortals = PortalRegistry.getPortalsAt(pos, world.getDimensionKey());

			if (affectedPortals == null || affectedPortals.size() < 1) return;

			Portal firstPortal = affectedPortals.get(0);

			if (firstPortal.isDamaged(world))
			{
				PortalRegistry.deactivatePortal(world, pos);
			}
		}

		super.neighborChanged(state, world, pos, neighborBlock, neighborPos, isMoving);
	}
}