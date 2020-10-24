package com.silverminer.moreore.init;

import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.world.gen.structures.DesertTempelStructure;
import com.silverminer.moreore.world.gen.structures.SchoolStructure;
import com.silverminer.moreore.world.gen.structures.TempelStructure;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;

public class ModStructureFeatures {
	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> SCHOOL = register(
			SchoolStructure.SHORT_NAME,
			StructureFeatureInit.SCHOOL.get().func_236391_a_(IFeatureConfig.NO_FEATURE_CONFIG));

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> TEMPEL = register(
			TempelStructure.SHORT_NAME,
			StructureFeatureInit.TEMPEL.get().func_236391_a_(IFeatureConfig.NO_FEATURE_CONFIG));

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> DESERT_TEMPEL = register(
			DesertTempelStructure.SHORT_NAME,
			StructureFeatureInit.DESERT_TEMPEL.get().func_236391_a_(IFeatureConfig.NO_FEATURE_CONFIG));

	private static <FC extends IFeatureConfig, F extends Structure<FC>> StructureFeature<FC, F> register(String name,
			StructureFeature<FC, F> structureFeature) {
		return WorldGenRegistries.func_243664_a(WorldGenRegistries.field_243654_f,
				new ResourceLocation(MoreOre.MODID, name), structureFeature);
	}
}