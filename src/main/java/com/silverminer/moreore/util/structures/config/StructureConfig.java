package com.silverminer.moreore.util.structures.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.registries.ForgeRegistries;

public class StructureConfig {
	public final LootableStructureGenConfig DESERT_TEMPEL;
	public final StructureGenConfig NUT_BUSH_PLANTATION;
	public final LootableStructureGenConfig TEMPEL;
	public final LootableStructureGenConfig GREEN_DUNGEON;
	public final LootableStructureGenConfig BROWN_LANDINGSTAGE;
	public final LootableStructureGenConfig ORANGE_SHRINE;
	public final ForgeConfigSpec.ConfigValue<List<? extends String>> BLACKLISTED_BIOMES;

	public StructureConfig(final ForgeConfigSpec.Builder SERVER_BUILDER) {
		NUT_BUSH_PLANTATION = new StructureGenConfig(SERVER_BUILDER, "Nut Bush Plantation", "nut_bush_plantation", 0.6D,
				40, 8, 609322918);
		TEMPEL = new LootableStructureGenConfig(SERVER_BUILDER, "Tempel", "tempel", 0.6D, 38, 8, 1664499850, 1.0D);
		DESERT_TEMPEL = new LootableStructureGenConfig(SERVER_BUILDER, "Desert Tempel", "desert_tempel", 0.6D, 30, 10,
				309865502, 0.6D, Category.DESERT, Category.MESA);
		GREEN_DUNGEON = new LootableStructureGenConfig(SERVER_BUILDER, "Green Dungeon", "green_dungeon", 1.0D, 30, 10,
				651038472, 1.0D, Category.FOREST);
		BROWN_LANDINGSTAGE = new LootableStructureGenConfig(SERVER_BUILDER, "Brown Landingstage", "brown_landingstage",
				1.0D, 30, 10, 98298754, 1.0D, Category.SWAMP);
		ORANGE_SHRINE = new LootableStructureGenConfig(SERVER_BUILDER, "Orange Shrine", "orange_shrine", 1.0D, 30, 10,
				-987516987, 1.0D, Category.SAVANNA);
		BLACKLISTED_BIOMES = SERVER_BUILDER
				.comment("Structure Generation Config", "Take care what you change, this changes may cant be undone",
						"", "Biomes in which Structures cant generate in")
				.defineList(
						"structures.blacklisted_biomes", getAllBiomesForCategory(Biome.Category.RIVER,
								Biome.Category.OCEAN, Biome.Category.THEEND, Biome.Category.NETHER),
						StructureConfig::validateBiome);
	}

	public static class StructureGenConfig {
		public final ForgeConfigSpec.BooleanValue GENERATE;
		public final ForgeConfigSpec.DoubleValue SPAWN_CHANCE;
		public final ForgeConfigSpec.IntValue DISTANCE;
		public final ForgeConfigSpec.IntValue SEPARATION;
		public final ForgeConfigSpec.IntValue SEED;
		public final ForgeConfigSpec.ConfigValue<List<? extends Biome.Category>> BIOME_CATEGORIES;
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> BIOME_BLACKLIST;

		public StructureGenConfig(final ForgeConfigSpec.Builder SERVER_BUILDER, String name, String dataName,
				int dSeed) {
			this(SERVER_BUILDER, name, dataName, 0.6D, 50, 10, dSeed);
		}

		public StructureGenConfig(final ForgeConfigSpec.Builder SERVER_BUILDER, String name, String dataName,
				double dSpawnChance, int dDistance, int dSeparation, int dSeed) {
			this(SERVER_BUILDER, name, dataName, dSpawnChance, dDistance, dSeparation, dSeed, Category.FOREST,
					Category.PLAINS, Category.TAIGA);
		}

		public StructureGenConfig(final ForgeConfigSpec.Builder SERVER_BUILDER, String name, String dataName,
				double dSpawnChance, int dDistance, int dSeparation, int dSeed, Category... category) {
			dataName = dataName.toLowerCase(Locale.ROOT);
			GENERATE = SERVER_BUILDER.comment("Generate " + name + "s?").define("structures." + dataName + ".generate",
					true);
			SPAWN_CHANCE = SERVER_BUILDER.comment(name + " Spawn Chance [default: " + dSpawnChance + "]")
					.defineInRange("structures." + dataName + ".spawn_chance", dSpawnChance, 0.0, 1.0);
			DISTANCE = SERVER_BUILDER.comment(name + " Distance (in chunks) [default: " + dDistance + "]")
					.defineInRange("structures." + dataName + ".distance", dDistance, 1, 500);
			SEPARATION = SERVER_BUILDER.comment(name + " Minimum Separation (in chunks) [default: " + dSeparation + "]")
					.defineInRange("structures." + dataName + ".separation", dSeparation, 1, 500);
			SEED = SERVER_BUILDER
					.comment(name + " Seed (Only Change if you know what you are doing)[default: " + dSeed + "]")
					.defineInRange("structures." + dataName + ".seed", dSeed, Integer.MIN_VALUE, Integer.MAX_VALUE);
			BIOME_CATEGORIES = SERVER_BUILDER.comment("Biome Types the " + name + " can generate in").defineList(
					"structures." + dataName + ".biome_categories", Arrays.asList(category),
					StructureConfig::validateBiomeCategory);
			BIOME_BLACKLIST = SERVER_BUILDER.comment("Biomes the " + name + " can NOT generate in").defineList(
					"structures." + dataName + ".biome_blacklist", Collections.emptyList(),
					StructureConfig::validateBiome);
		}
	}

	public static class LootableStructureGenConfig extends StructureGenConfig {
		public final ForgeConfigSpec.DoubleValue LOOT_CHANCE;

		public LootableStructureGenConfig(Builder SERVER_BUILDER, String name, String dataName, double dSpawnChance,
				int dDistance, int dSeparation, int dSeed, double dLootChance, Category... category) {
			super(SERVER_BUILDER, name, dataName, dSpawnChance, dDistance, dSeparation, dSeed, category);
			LOOT_CHANCE = SERVER_BUILDER.comment(name + " Generate Loot Chance [default: " + dLootChance + "]")
					.defineInRange("structures." + dataName.toLowerCase(Locale.ROOT) + ".loot_chance", dLootChance, 0.0,
							1.0);
		}

		public LootableStructureGenConfig(Builder SERVER_BUILDER, String name, String dataName, double dSpawnChance,
				int dDistance, int dSeparation, int dSeed, double dLootChance) {
			this(SERVER_BUILDER, name, dataName, dSpawnChance, dDistance, dSeparation, dSeed, dLootChance,
					Category.FOREST, Category.TAIGA, Category.PLAINS);
		}

		public LootableStructureGenConfig(final ForgeConfigSpec.Builder SERVER_BUILDER, String name, String dataName,
				int dSeed, double dLootChance) {
			this(SERVER_BUILDER, name, dataName, 0.6D, 50, 10, dSeed, dLootChance);
		}
	}

	private static boolean validateBiome(Object o) {
		return o == null || ForgeRegistries.BIOMES.containsKey(new ResourceLocation((String) o));
	}

	private static boolean validateBiomeCategory(Object o) {
		for (Biome.Category category : Biome.Category.values()) {
			if (category == Biome.Category.valueOf((String) o)) {
				return true;
			}
		}
		return false;
	}

	public static List<String> getAllBiomesForCategory(Biome.Category... categories) {
		List<String> biomes = new ArrayList<>();

		for (Biome biome : ForgeRegistries.BIOMES.getValues()) {
			for (Biome.Category category : categories) {
				if (biome.getCategory() == category) {
					biomes.add(Objects.requireNonNull(biome.getRegistryName()).toString());
				}
			}
		}

		return biomes;
	}
}