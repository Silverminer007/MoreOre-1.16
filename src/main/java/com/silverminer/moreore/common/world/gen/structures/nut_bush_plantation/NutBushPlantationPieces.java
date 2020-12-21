package com.silverminer.moreore.common.world.gen.structures.nut_bush_plantation;

import java.util.List;
import java.util.Random;

import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.common.world.gen.structures.AbstractStructurePiece;
import com.silverminer.moreore.common.world.gen.structures.MoreoreStructurePieceType;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class NutBushPlantationPieces {

	private static final ResourceLocation[] locations = new ResourceLocation[] {
			new ResourceLocation(MoreOre.MODID, NutBushPlantationStructure.SHORT_NAME),
			new ResourceLocation(MoreOre.MODID, "small_" + NutBushPlantationStructure.SHORT_NAME) };

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random) {
		pieces.add(new NutBushPlantationPieces.Piece(templateManager, locations[random.nextInt(locations.length)],
				pos, rotation, 0));
	}

	public static class Piece extends AbstractStructurePiece {
		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
				int componentTypeIn) {
			super(MoreoreStructurePieceType.NUT_BUSH_PLANTATION, templateManager, location, pos, rotation, componentTypeIn);
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(MoreoreStructurePieceType.NUT_BUSH_PLANTATION, templateManager, cNBT);
		}

		@Override
		public StructureProcessor getProcessor() {
			return BlockIgnoreStructureProcessor.STRUCTURE_BLOCK;
		}
	}
}