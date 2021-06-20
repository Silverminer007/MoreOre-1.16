package com.silverminer.moreore.util.structures;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.moreore.common.world.gen.structures.AbstractStructure;
import com.silverminer.moreore.init.structures.StructureFeatureInit;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;

public class StructureUtils {
	public static final Logger LOGGER = LogManager.getLogger(StructureUtils.class);

	public static void setupWorldGen() {
		LOGGER.info("Generating Structures");
		for (AbstractStructure<NoFeatureConfig> structure : StructureFeatureInit.STRUCTURES_LIST) {
			try {
				if (structure.isEndStructure()) {
					register(DimensionSettings.END, structure, new StructureSeparationSettings(
							structure.getDistance(), structure.getSeparation(), structure.getSeedModifier()));
				}
				if (structure.isNetherStructure()) {
					register(DimensionSettings.NETHER, structure, new StructureSeparationSettings(
							structure.getDistance(), structure.getSeparation(), structure.getSeedModifier()));
				}
				register(DimensionSettings.OVERWORLD, structure, new StructureSeparationSettings(
						structure.getDistance(), structure.getSeparation(), structure.getSeedModifier()));
			} catch (Throwable t) {
				LOGGER.error("Structures of Shrines can't be registered correctly because exception was thrown\n {}",
						t);
			}
		}
	}

	public static void register(RegistryKey<DimensionSettings> dimension, Structure<?> structure,
			StructureSeparationSettings separationSettings) {
		DimensionSettings DS = WorldGenRegistries.NOISE_GENERATOR_SETTINGS.get(dimension);
		if (DS == null) {
			LOGGER.error("Something went wrong in structure registerting to dimensions. Please report this issue");
			return;
		}
		DimensionStructuresSettings structuresSettings = DS.structureSettings();
		structuresSettings.structureConfig().put(structure, separationSettings);
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