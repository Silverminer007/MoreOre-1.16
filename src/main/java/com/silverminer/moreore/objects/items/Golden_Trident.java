package com.silverminer.moreore.objects.items;

import com.silverminer.moreore.common.portal.registration.PortalRegistry;

import java.util.ArrayList;
import java.util.List;

import com.silverminer.moreore.init.items.ToolItems;
import com.silverminer.moreore.objects.blocks.SilverPortalFrameBlock;

import net.minecraft.block.DispenserBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.BlockState;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.Direction.AxisDirection;
import net.minecraft.world.IWorldReader;

public class Golden_Trident extends TridentItem {

	public Golden_Trident(Properties builder) {
		super(builder);
		DispenserBlock.registerDispenseBehavior(this, dispenserBehavior);
	}

	@Override
	public boolean doesSneakBypassUse(ItemStack stack, IWorldReader world, BlockPos pos, PlayerEntity player) {
		return true;
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		if (context.getWorld().getBlockState(context.getPos()).getBlock() instanceof SilverPortalFrameBlock) {
			context.getPlayer().swingArm(context.getHand());
		}

		return super.onItemUse(context);
	}

	/**
	 * Custom dispenser behavior that allows dispensers to activate portals with a
	 * contained portal activator.
	 */
	private final static IDispenseItemBehavior dispenserBehavior = new IDispenseItemBehavior() {
		private final DefaultDispenseItemBehavior defaultBehavior = new DefaultDispenseItemBehavior();

		@Override
		public ItemStack dispense(IBlockSource source, ItemStack stack) {
			if (ItemStack.areItemsEqual(stack, new ItemStack(ToolItems.GOLDEN_TRIDENT.get()))) {
				World world = source.getWorld();
				BlockState dispenser = world.getBlockState(source.getBlockPos());

				// Start searching for portal frame blocks in the direction the dispenser is
				// facing.
				Direction dispenserFacing = dispenser.get(DispenserBlock.FACING);
				BlockPos searchStartPos = source.getBlockPos().offset(dispenserFacing);

				if (world.isAirBlock(searchStartPos)) {
					// Search along the other two axis besides the one the dispenser is facing in.
					// E.g. dispenser faces south: Search one block south of the dispenser, up,
					// down,
					// east and west.
					List<Direction> searchDirections = new ArrayList<>();
					Axis dispenserAxis = dispenserFacing.getAxis();

					for (Axis axis : Axis.values()) {
						if (axis != dispenserAxis) {
							searchDirections.add(Direction.getFacingFromAxis(AxisDirection.POSITIVE, axis));
							searchDirections.add(Direction.getFacingFromAxis(AxisDirection.NEGATIVE, axis));
						}
					}

					BlockPos currentPos;

					for (Direction facing : searchDirections) {
						currentPos = searchStartPos.offset(facing);

						if (world.getBlockState(currentPos).getBlock() instanceof SilverPortalFrameBlock) {
							if (PortalRegistry.activatePortal(world, currentPos, facing.getOpposite())) {
								return stack;
							}
						}
					}
				}
			}

			return defaultBehavior.dispense(source, stack);
		}
	};
}
