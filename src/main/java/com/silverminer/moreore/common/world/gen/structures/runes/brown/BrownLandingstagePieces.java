package com.silverminer.moreore.common.world.gen.structures.runes.brown;

import java.util.List;
import java.util.Random;

import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.common.loot_tables.MoreoreLootTables;
import com.silverminer.moreore.common.world.gen.structures.ColorStructurePiece;
import com.silverminer.moreore.common.world.gen.structures.MoreoreStructurePieceType;
import com.silverminer.moreore.util.structures.config.Config;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
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

public class BrownLandingstagePieces {

	private static final ResourceLocation[] location = new ResourceLocation[] {
			new ResourceLocation(MoreOre.MODID, "runes/brown/" + "landing_stage_com" + "_1"),
			new ResourceLocation(MoreOre.MODID, "runes/brown/" + "landing_stage_com" + "_2") };

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random) {
		if (random.nextBoolean()) {
			pieces.add(new BrownLandingstagePieces.Piece(templateManager, location[0], pos, rotation, 0));
		} else {
			pieces.add(new BrownLandingstagePieces.Piece(templateManager, location[1], pos, rotation, 0));
		}
	}

	public static class Piece extends ColorStructurePiece {
		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
				int componentTypeIn) {
			super(MoreoreStructurePieceType.BROWN_LANDINGSTAGE, templateManager, location, pos, rotation,
					componentTypeIn, true);
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(MoreoreStructurePieceType.BROWN_LANDINGSTAGE, templateManager, cNBT);
		}

		@Override
		public StructureProcessor getProcessor() {
			return BlockIgnoreStructureProcessor.STRUCTURE_BLOCK;
		}

		@Override
		protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand,
				MutableBoundingBox sbb) {
			boolean loot = Config.STRUCTURES.BROWN_LANDINGSTAGE.LOOT_CHANCE.get() > rand.nextDouble();
			if (function.equals("chest_1")) {
				LockableLootTileEntity.setLootTable(worldIn, rand, pos.below(1),
						loot ? MoreoreLootTables.LANDINGSTAGE_1 : MoreoreLootTables.EMPTY);
			}
			if (function.equals("chest_2")) {
				LockableLootTileEntity.setLootTable(worldIn, rand, pos.below(1),
						loot ? MoreoreLootTables.LANDINGSTAGE_2 : MoreoreLootTables.EMPTY);
			}
		}

		@SuppressWarnings("deprecation")
		public int getGenerationHeight(ISeedReader world, BlockPos blockpos1) {
			return world.getSeaLevel() - 2;
		}

		@Override
		protected boolean useRandomVarianting() {
			return true;
		}
	}
}