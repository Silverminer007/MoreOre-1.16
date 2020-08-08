package com.silverminer.moreore.world.gen.structures;

import com.silverminer.moreore.MoreOre;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;

public class ModStructurePieces {
	public static final IStructurePieceType TEMPEL = TempelPieces.TempelPiece::new;
	public static final IStructurePieceType SCHOOL = SchoolPieces.SchoolPiece::new;
	public static final IStructurePieceType DESERT_TEMPEL = DesertTempelPieces.DesertTempelPiece::new;
	public static void registerPieces() {
		TempelPieces.register();
		SchoolPieces.register();
		DesertTempelPieces.register();
		register(TempelStructure.SHORT_NAME, TEMPEL);
		register(SchoolStructure.SHORT_NAME, SCHOOL);
		register(DesertTempelStructure.SHORT_NAME, DESERT_TEMPEL);
	}

	private static void register(String key, IStructurePieceType type) {
		Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(MoreOre.MODID, key), type);
	}
}
