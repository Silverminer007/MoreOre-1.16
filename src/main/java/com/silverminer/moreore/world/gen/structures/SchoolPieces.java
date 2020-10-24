package com.silverminer.moreore.world.gen.structures;

import java.util.List;
import java.util.Random;

import com.silverminer.moreore.MoreOre;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class SchoolPieces {

	private static final ResourceLocation location = new ResourceLocation(MoreOre.MODID, SchoolStructure.SHORT_NAME);

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random) {
		pieces.add(new SchoolPieces.Piece(templateManager, location, pos, rotation, 0));
	}

	public static class Piece extends MoreoreStructurePiece {
		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
				int componentTypeIn) {
			super(MoreoreStructurePieceType.SCHOOL, templateManager, location, pos, rotation, componentTypeIn);
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(MoreoreStructurePieceType.SCHOOL, templateManager, cNBT);
		}

		@Override
		public StructureProcessor getProcessor() {
			return BlockIgnoreStructureProcessor.STRUCTURE_BLOCK;
		}
	}
}