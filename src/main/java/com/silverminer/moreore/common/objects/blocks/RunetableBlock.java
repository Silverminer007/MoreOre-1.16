package com.silverminer.moreore.common.objects.blocks;

import com.silverminer.moreore.client.gui.container.RunetableContainer;

import net.minecraft.block.BlockState;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class RunetableBlock extends CraftingTableBlock {
	private static final ITextComponent CONTAINER_NAME = new TranslationTextComponent("container.upgrade");

	public RunetableBlock(Properties properties) {
		super(properties);
	}

	public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos) {
		return new SimpleNamedContainerProvider((id, inventory, player) -> {
			return new RunetableContainer(id, inventory, IWorldPosCallable.create(worldIn, pos));
		}, CONTAINER_NAME);
	}

	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit) {
		if (worldIn.isClientSide()) {
			return ActionResultType.SUCCESS;
		} else {
			player.openMenu(state.getMenuProvider(worldIn, pos));
			return ActionResultType.CONSUME;
		}
	}
}