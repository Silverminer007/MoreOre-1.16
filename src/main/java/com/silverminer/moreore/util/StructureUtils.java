package com.silverminer.moreore.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableMap;
import com.silverminer.moreore.init.StructureFeatureInit;

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

			DimensionSettings.field_242740_q.func_236108_a_().field_236193_d_.put(structure,
					new StructureSeparationSettings(structure.getDistance(), structure.getSeparation(),
							structure.getSeedModifier()));
		});
	}
}