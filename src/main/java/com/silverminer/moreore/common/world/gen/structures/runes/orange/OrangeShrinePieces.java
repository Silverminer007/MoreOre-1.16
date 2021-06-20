package com.silverminer.moreore.common.world.gen.structures.runes.orange;

import java.util.List;
import java.util.Random;

import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.common.loot_tables.MoreoreLootTables;
import com.silverminer.moreore.common.world.gen.structures.AbstractStructurePiece;
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

public class OrangeShrinePieces {

	private static final ResourceLocation[] location = new ResourceLocation[] {
			new ResourceLocation(MoreOre.MODID, "runes/orange/" + OrangeShrineStructure.SHORT_NAME + "_1"),
			new ResourceLocation(MoreOre.MODID, "runes/orange/" + OrangeShrineStructure.SHORT_NAME + "_2"),
			new ResourceLocation(MoreOre.MODID, "runes/orange/" + OrangeShrineStructure.SHORT_NAME + "_3") };

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random) {
		if (random.nextBoolean()) {
			pieces.add(new OrangeShrinePieces.Piece(templateManager, location[0], pos, rotation, 0));
		} else if (random.nextBoolean()) {
			pieces.add(new OrangeShrinePieces.Piece(templateManager, location[1], pos, rotation, 0));
		} else {
			pieces.add(new OrangeShrinePieces.Piece(templateManager, location[2], pos, rotation, 0));
		}
	}

	public static class Piece extends AbstractStructurePiece {
		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
				int componentTypeIn) {
			super(MoreoreStructurePieceType.ORANGE_SHRINE, templateManager, location, pos, rotation, componentTypeIn);
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(MoreoreStructurePieceType.ORANGE_SHRINE, templateManager, cNBT);
		}

		@Override
		public StructureProcessor getProcessor() {
			return BlockIgnoreStructureProcessor.STRUCTURE_BLOCK;
		}

		@Override
		protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand,
				MutableBoundingBox sbb) {
			boolean loot = Config.STRUCTURES.ORANGE_SHRINE.LOOT_CHANCE.get() > rand.nextDouble();
			if (function.equals("chest")) {
				worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
				LockableLootTileEntity.setLootTable(worldIn, rand, pos.below(1),
						loot ? MoreoreLootTables.BLUE_SHIP : MoreoreLootTables.EMPTY);
			}
		}
	}
}