package com.silverminer.moreore.init.structures;

import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.common.world.gen.structures.desert_tempel.DesertTempelStructure;
import com.silverminer.moreore.common.world.gen.structures.nut_bush_plantation.NutBushPlantationStructure;
import com.silverminer.moreore.common.world.gen.structures.runes.green.GreenDungeonStructure;
import com.silverminer.moreore.common.world.gen.structures.tempel.TempelStructure;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;

public class ModStructureFeatures {

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> TEMPEL = register(
			TempelStructure.SHORT_NAME,
			StructureFeatureInit.TEMPEL.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> DESERT_TEMPEL = register(
			DesertTempelStructure.SHORT_NAME,
			StructureFeatureInit.DESERT_TEMPEL.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> NUT_BUSH_PLANTATION = register(
			NutBushPlantationStructure.SHORT_NAME,
			StructureFeatureInit.NUT_BUSH_PLANTATION.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> GREEN_DUNGEON = register(
			GreenDungeonStructure.SHORT_NAME,
			StructureFeatureInit.GREEN_DUNGEON.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));

	private static <FC extends IFeatureConfig, F extends Structure<FC>> StructureFeature<FC, F> register(String name,
			StructureFeature<FC, F> structureFeature) {
		return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE,
				new ResourceLocation(MoreOre.MODID, name), structureFeature);
	}
}