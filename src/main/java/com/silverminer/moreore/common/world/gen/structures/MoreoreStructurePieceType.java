package com.silverminer.moreore.common.world.gen.structures;

import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.common.world.gen.structures.nut_bush_plantation.NutBushPlantationPieces;
import com.silverminer.moreore.common.world.gen.structures.runes.brown.BrownLandingstagePieces;
import com.silverminer.moreore.common.world.gen.structures.runes.green.GreenDungeonPieces;
import com.silverminer.moreore.common.world.gen.structures.runes.orange.OrangeShrinePieces;
import com.silverminer.moreore.common.world.gen.structures.runes.purple.PurplehousePieces;
import com.silverminer.moreore.common.world.gen.structures.runes.yellow.DesertTempelPieces;
import com.silverminer.moreore.common.world.gen.structures.tempel.TempelPieces;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;

public class MoreoreStructurePieceType {
	public static final IStructurePieceType TEMPEL = TempelPieces.Piece::new;

	public static final IStructurePieceType DESERT_TEMPEL = DesertTempelPieces.Piece::new;

	public static final IStructurePieceType NUT_BUSH_PLANTATION = NutBushPlantationPieces.Piece::new;

	public static final IStructurePieceType GREEN_DUNGEON = GreenDungeonPieces.Piece::new;
	public static final IStructurePieceType BROWN_LANDINGSTAGE = BrownLandingstagePieces.Piece::new;
	public static final IStructurePieceType ORANGE_SHRINE = OrangeShrinePieces.Piece::new;
	public static final IStructurePieceType PURPLE_HOUSE = PurplehousePieces.Piece::new;

	public static void regsiter() {
		Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(MoreOre.MODID, "tempel"), TEMPEL);
		Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(MoreOre.MODID, "desert_tempel"),
				DESERT_TEMPEL);
		Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(MoreOre.MODID, "nut_bush_plantation"),
				NUT_BUSH_PLANTATION);
		Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(MoreOre.MODID, "green_dungeon"),
				GREEN_DUNGEON);
		Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(MoreOre.MODID, "brown_landingstage"),
				BROWN_LANDINGSTAGE);
		Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(MoreOre.MODID, "purple_house"), PURPLE_HOUSE);
		Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(MoreOre.MODID, "orange_shrine"),
				ORANGE_SHRINE);
	}
}