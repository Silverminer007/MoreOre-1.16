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
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class VikingShipPieces {

	private static final ResourceLocation part1 = new ResourceLocation(MoreOre.MODID,
			"runes/blue/viking_ship/" + "viking_ship_p1");
	private static final ResourceLocation part2 = new ResourceLocation(MoreOre.MODID,
			"runes/blue/viking_ship/" + "viking_ship_p2");

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random) {
		pieces.add(new VikingShipPieces.Piece(templateManager, part1, pos, rotation, 0));
		pieces.add(new VikingShipPieces.Piece(templateManager, part2, pos.add(new BlockPos(27, 0, 0).rotate(rotation)),
				rotation, 0));
	}

	public static class Piece extends ColorStructurePiece {
		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
				int componentTypeIn) {
			super(MoreoreStructurePieceType.BLUE_SHIP, templateManager, location, pos, rotation, componentTypeIn, true);
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(MoreoreStructurePieceType.BLUE_SHIP, templateManager, cNBT);
		}

		@Override
		public StructureProcessor getProcessor() {
			return BlockIgnoreStructureProcessor.AIR_AND_STRUCTURE_BLOCK;
		}

		@Override
		protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand,
				MutableBoundingBox sbb) {
			if (Config.STRUCTURES.BLUE_RUNE.LOOT_CHANCE.get() > rand.nextDouble()) {
				if (function.equals("chest")) {
					worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
					TileEntity tileentity = worldIn.getTileEntity(pos.down());
					if (tileentity instanceof ChestTileEntity) {
						((ChestTileEntity) tileentity).setLootTable(MoreoreLootTables.BLUE_SHIP, rand.nextLong());
					}
				}
				if (function.equals("barrel_1")) {
					for (int i = 1; i <= 1; i++) {
						worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
						TileEntity tileentity = worldIn.getTileEntity(pos.down(i));
						if (tileentity instanceof LockableLootTileEntity) {
							((LockableLootTileEntity) tileentity).setLootTable(MoreoreLootTables.BLUE_SHIP_BARREL,
									rand.nextLong());
						}
					}
				}
				if (function.equals("barrel_2")) {
					for (int i = 1; i <= 2; i++) {
						worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
						TileEntity tileentity = worldIn.getTileEntity(pos.down(i));
						if (tileentity instanceof LockableLootTileEntity) {
							((LockableLootTileEntity) tileentity).setLootTable(MoreoreLootTables.BLUE_SHIP_BARREL,
									rand.nextLong());
						}
					}
				}
				if (function.equals("barrel_3")) {
					for (int i = 1; i <= 3; i++) {
						worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
						TileEntity tileentity = worldIn.getTileEntity(pos.down(i));
						if (tileentity instanceof LockableLootTileEntity) {
							((LockableLootTileEntity) tileentity).setLootTable(MoreoreLootTables.BLUE_SHIP_BARREL,
									rand.nextLong());
						}
					}
				}
			}
		}

		@SuppressWarnings("deprecation")
		public int getGenerationHeight(ISeedReader world, BlockPos blockpos1) {
			return world.getSeaLevel() - 1;
		}

		@Override
		protected boolean useRandomVarianting() {
			return false;
		}
	}
}