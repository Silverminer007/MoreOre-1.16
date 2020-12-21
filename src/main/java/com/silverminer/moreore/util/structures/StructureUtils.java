package com.silverminer.moreore.util.structures;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableMap;
import com.silverminer.moreore.init.structures.StructureFeatureInit;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;

public class StructureUtils {
	public static final Logger LOGGER = LogManager.getLogger(StructureUtils.class);

	public static void setupWorldGen() {
		LOGGER.info("Generating Structures");
		StructureFeatureInit.STRUCTURES_LIST.forEach(structure -> {
			DimensionStructuresSettings.field_236191_b_ = // Default structures
					ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
							.putAll(DimensionStructuresSettings.field_236191_b_)
							.put(structure, new StructureSeparationSettings(structure.getDistance(),
									structure.getSeparation(), structure.getSeedModifier()))
							.build();

			DimensionSettings.field_242740_q.getStructures().field_236193_d_.put(structure,
					new StructureSeparationSettings(structure.getDistance(), structure.getSeparation(),
							structure.getSeedModifier()));
		});
	}

	public static boolean checkBiome(List<? extends Object> allowedBiomeCategories,
			List<? extends String> blacklistedBiomes, ResourceLocation name, Biome.Category category) {
		boolean flag = allowedBiomeCategories.contains(category.toString())
				|| allowedBiomeCategories.contains(category);

		if (!blacklistedBiomes.isEmpty() && flag) {
			flag = !blacklistedBiomes.contains(name.toString());
		}

		return flag;
	}
}