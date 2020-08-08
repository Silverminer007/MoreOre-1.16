package com.silverminer.moreore.world.gen.structures;

import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.silverminer.moreore.MoreOre;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class DesertTempelPieces {

	public static void register() {
		JigsawManager.REGISTRY.register(new JigsawPattern(
				new ResourceLocation(MoreOre.MODID, DesertTempelStructure.SHORT_NAME), new ResourceLocation("empty"),
				ImmutableList.of(
						Pair.of(new ModSingleJigsawPiece(MoreOre.MODID + ":" + DesertTempelStructure.SHORT_NAME), 1)),
				JigsawPattern.PlacementBehaviour.RIGID));
	}

	public static void generate(ChunkGenerator generator, TemplateManager templateManager, BlockPos position,
			List<StructurePiece> pieces, Random random) {
		if (position.getY() < 40) {
			return;
		}
		JigsawManager.func_236823_a_(new ResourceLocation(MoreOre.MODID, DesertTempelStructure.SHORT_NAME), 7,
				DesertTempelPiece::new, generator, templateManager, position, pieces, random, false, false);
	}

	public void readAdditional(CompoundNBT tagCompound) {
		tagCompound.putBoolean("Villager", true);
	}

	public static class DesertTempelPiece extends AbstractVillagePiece {
		public DesertTempelPiece(TemplateManager templateManager, JigsawPiece jigsawPiece, BlockPos position,
				int groundLevelDelta, Rotation rotation, MutableBoundingBox bounds) {
			super(ModStructurePieces.DESERT_TEMPEL, templateManager, jigsawPiece, position, groundLevelDelta, rotation,
					bounds);
		}

		public DesertTempelPiece(TemplateManager templateManager, CompoundNBT compoundNBT) {
			super(templateManager, compoundNBT, ModStructurePieces.DESERT_TEMPEL);
		}
	}
}