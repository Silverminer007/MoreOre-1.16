package com.silverminer.moreore.common.world.gen.structures.runes.blue;

import java.util.List;
import java.util.Random;

import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.common.loot_tables.MoreoreLootTables;
import com.silverminer.moreore.common.world.gen.structures.ColorStructurePiece;
import com.silverminer.moreore.common.world.gen.structures.MoreoreStructurePieceType;
import com.silverminer.moreore.util.structures.config.Config;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class HousePieces {

	private static final ResourceLocation[] location = new ResourceLocation[] {
			new ResourceLocation(MoreOre.MODID, "runes/blue/" + "blue_house_1") };

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random) {
		pieces.add(new HousePieces.Piece(templateManager, location[0], pos, rotation, 0));
	}

	public static class Piece extends ColorStructurePiece {
		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
				int componentTypeIn) {
			super(MoreoreStructurePieceType.BLUE_HOUSE, templateManager, location, pos, rotation, componentTypeIn,
					true);
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(MoreoreStructurePieceType.BLUE_HOUSE, templateManager, cNBT);
		}

		@Override
		public StructureProcessor getProcessor() {
			return BlockIgnoreStructureProcessor.AIR_AND_STRUCTURE_BLOCK;
		}

		@Override
		protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand,
				MutableBoundingBox sbb) {
			if (Config.STRUCTURES.BLUE_HOUSE.LOOT_CHANCE.get() > rand.nextDouble()) {
				if (function.equals("chest")) {
					worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
					TileEntity tileentity = worldIn.getTileEntity(pos.down());
					if (tileentity instanceof ChestTileEntity) {
						((ChestTileEntity) tileentity).setLootTable(MoreoreLootTables.BLUE_HOUSE, rand.nextLong());
					}
				}
				if (function.equals("chest_2")) {
					worldIn.setBlockState(pos, Blocks.GLASS.getDefaultState(), 3);
					TileEntity tileentity = worldIn.getTileEntity(pos.down());
					if (tileentity instanceof ChestTileEntity) {
						((ChestTileEntity) tileentity).setLootTable(MoreoreLootTables.BLUE_HOUSE_2, rand.nextLong());
					}
					tileentity = worldIn.getTileEntity(pos.down(2));
					if (tileentity instanceof ChestTileEntity) {
						((ChestTileEntity) tileentity).setLootTable(MoreoreLootTables.BLUE_HOUSE_2, rand.nextLong());
					}
				}
			}
		}

		@Override
		protected boolean useRandomVarianting() {
			return false;
		}
	}
}