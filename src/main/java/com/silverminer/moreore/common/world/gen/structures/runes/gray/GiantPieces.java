package com.silverminer.moreore.common.world.gen.structures.runes.gray;

import java.util.List;
import java.util.Random;

import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.common.loot_tables.MoreoreLootTables;
import com.silverminer.moreore.common.world.gen.structures.ColorStructurePiece;
import com.silverminer.moreore.common.world.gen.structures.MoreoreStructurePieceType;
import com.silverminer.moreore.util.structures.config.Config;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class GiantPieces {

	private static final ResourceLocation[] location = new ResourceLocation[] {
			new ResourceLocation(MoreOre.MODID, "runes/gray/" + GiantStructure.SHORT_NAME),
			new ResourceLocation(MoreOre.MODID, "runes/gray/" + GiantStructure.SHORT_NAME + "2"),
			new ResourceLocation(MoreOre.MODID, "runes/gray/" + GiantStructure.SHORT_NAME + "3"),
			new ResourceLocation(MoreOre.MODID, "runes/gray/" + GiantStructure.SHORT_NAME + "4"),
			new ResourceLocation(MoreOre.MODID, "runes/gray/" + GiantStructure.SHORT_NAME + "5"),
			new ResourceLocation(MoreOre.MODID, "runes/gray/" + GiantStructure.SHORT_NAME + "5"),
			new ResourceLocation(MoreOre.MODID, "runes/gray/" + GiantStructure.SHORT_NAME + "6"),
			new ResourceLocation(MoreOre.MODID, "runes/gray/" + GiantStructure.SHORT_NAME + "7"),
			new ResourceLocation(MoreOre.MODID, "runes/gray/" + GiantStructure.SHORT_NAME + "8"),
			new ResourceLocation(MoreOre.MODID, "runes/gray/" + GiantStructure.SHORT_NAME + "9") };

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random) {
		pieces.add(new GiantPieces.Piece(templateManager, location[random.nextInt(location.length - 1)], pos, rotation,
				0));
	}

	public static class Piece extends ColorStructurePiece {
		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
				int componentTypeIn) {
			super(MoreoreStructurePieceType.GIANT, templateManager, location, pos, rotation, componentTypeIn, true);
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(MoreoreStructurePieceType.GIANT, templateManager, cNBT);
		}

		@Override
		public StructureProcessor getProcessor() {
			return BlockIgnoreStructureProcessor.STRUCTURE_AND_AIR;
		}

		@Override
		protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand,
				MutableBoundingBox sbb) {
			boolean loot = Config.STRUCTURES.GIANT.LOOT_CHANCE.get() > rand.nextDouble();
			if (function.equals("chest")) {
				worldIn.setBlock(pos, Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 3);
				LockableLootTileEntity.setLootTable(worldIn, rand, pos.below(1),
						loot ? MoreoreLootTables.BLUE_SHIP : MoreoreLootTables.EMPTY);
			}
			if (function.equals("chest2")) {
				worldIn.setBlock(pos, Blocks.CHISELED_STONE_BRICKS.defaultBlockState(), 3);
				LockableLootTileEntity.setLootTable(worldIn, rand, pos.below(1),
						loot ? MoreoreLootTables.BLUE_SHIP : MoreoreLootTables.EMPTY);
			}
			if (function.equals("chest3")) {
				worldIn.setBlock(pos, Blocks.CHISELED_POLISHED_BLACKSTONE.defaultBlockState(), 3);
				LockableLootTileEntity.setLootTable(worldIn, rand, pos.below(1),
						loot ? MoreoreLootTables.BLUE_SHIP : MoreoreLootTables.EMPTY);
			}
		}

		@Override
		protected boolean useRandomVarianting() {
			return true;
		}
	}
}