package com.silverminer.moreore.common.objects.blocks;

import com.silverminer.moreore.common.properties.Material;
import com.silverminer.moreore.common.properties.ModBlockStateProperties;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class BeamerBlock extends Block {
	public static final EnumProperty<Material> MATERIAL = ModBlockStateProperties.MATERIAL;

	public BeamerBlock(Properties properties) {
		super(properties);
	}

	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit) {
		worldIn.setBlockState(pos, state.with(MATERIAL, Material.getNext(state)), 3);
		return ActionResultType.SUCCESS;
	}

	public void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(MATERIAL);
	}

	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(MATERIAL, Material.BEAMER);
	}
}
