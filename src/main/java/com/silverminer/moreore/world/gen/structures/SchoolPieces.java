package com.silverminer.moreore.world.gen.structures;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.silverminer.moreore.MoreOre;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class SchoolPieces {

	public static void register() {
		JigsawManager.REGISTRY.register(
				new JigsawPattern(new ResourceLocation(MoreOre.MODID, "school"), new ResourceLocation("empty"),
						ImmutableList.of(Pair.of(new ModSingleJigsawPiece(MoreOre.MODID + ":school"), 1)),
						JigsawPattern.PlacementBehaviour.RIGID));
	}

	public static void generate(ChunkGenerator generator, TemplateManager templateManager, BlockPos position,
			List<StructurePiece> pieces, SharedSeedRandom random) {
		if (position.getY() < 40) {
			return;
		}
		JigsawManager.func_236823_a_(new ResourceLocation(MoreOre.MODID, "school"), 7, SchoolPiece::new, generator,
				templateManager, position, pieces, random, false, false);
	}

	public void readAdditional(CompoundNBT tagCompound) {
		tagCompound.putBoolean("Villager", true);
	}

	public static class SchoolPiece extends AbstractVillagePiece {
		public SchoolPiece(TemplateManager templateManager, JigsawPiece jigsawPiece, BlockPos position,
				int groundLevelDelta, Rotation rotation, MutableBoundingBox bounds) {
			super(ModStructurePieces.SCHOOL, templateManager, jigsawPiece, position, groundLevelDelta, rotation,
					bounds);
		}

		public SchoolPiece(TemplateManager templateManager, CompoundNBT compoundNBT) {
			super(templateManager, compoundNBT, ModStructurePieces.SCHOOL);
		}
	}
}