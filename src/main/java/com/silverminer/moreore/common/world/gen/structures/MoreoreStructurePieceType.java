package com.silverminer.moreore.common.world.gen.structures;

import java.util.Locale;

import com.silverminer.moreore.MoreOre;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;

public class MoreoreStructurePieceType {
	public static final IStructurePieceType TEMPEL = register(TempelPieces.Piece::new, MoreOre.MODID,
			TempelStructure.SHORT_NAME);

	public static final IStructurePieceType DESERT_TEMPEL = register(DesertTempelPieces.Piece::new, MoreOre.MODID,
			DesertTempelStructure.SHORT_NAME);

	public static final IStructurePieceType NUT_BUSH_PLANTATION = register(NutBushPlantationPieces.Piece::new, MoreOre.MODID,
			NutBushPlantationStructure.SHORT_NAME);

	private static IStructurePieceType register(IStructurePieceType type, String modid, String key) {
		return Registry.register(Registry.STRUCTURE_PIECE,
				new ResourceLocation(modid.toLowerCase(Locale.ROOT), key.toLowerCase(Locale.ROOT)), type);
	}
}