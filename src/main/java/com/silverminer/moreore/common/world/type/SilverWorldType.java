package com.silverminer.moreore.common.world.type;

import java.util.ArrayList;
import java.util.OptionalLong;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.common.world.biomeprovider.SilverBiomeProvider;
import com.silverminer.moreore.util.helpers.silver_dim.ModDimensionType;

import net.minecraft.tags.BlockTags;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.Dimension;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeMagnifier;
import net.minecraft.world.biome.FuzzedBiomeMagnifier;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.NoiseChunkGenerator;
import net.minecraft.world.gen.settings.DimensionGeneratorSettings;
import net.minecraftforge.common.world.ForgeWorldType;
import net.minecraftforge.registries.ForgeRegistries;

public class SilverWorldType extends ForgeWorldType {
	protected static final Logger LOGGER = LogManager.getLogger(SilverWorldType.class);

	public static final ResourceLocation SILVER_DIM_KEY = new ResourceLocation(MoreOre.MODID, "silver_dimension");

	public static final RegistryKey<DimensionType> SILVER_LOCATION = RegistryKey
			.create(Registry.DIMENSION_TYPE_REGISTRY, SILVER_DIM_KEY);
	public static final DimensionType DIMENSION_TYPE = new ModDimensionType(OptionalLong.empty(), true, false, false,
			true, 1, false, false, true, true, false, 256, DefaultBiomeMagnifier.INSTANCE,
			BlockTags.INFINIBURN_OVERWORLD.getName(), DimensionType.OVERWORLD_EFFECTS, 0);

	public static final RegistryKey<Dimension> SILVER_DIM = RegistryKey.create(Registry.LEVEL_STEM_REGISTRY,
			SILVER_DIM_KEY);

	public static final RegistryKey<DimensionSettings> SILVER_DIM_SETTINGS = RegistryKey
			.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, SILVER_DIM_KEY);

	public static final ArrayList<Supplier<Biome>> BIOMES = Lists.newArrayList();

	public SilverWorldType() {
		super(new IBasicChunkGeneratorFactory() {

			@Override
			public ChunkGenerator createChunkGenerator(Registry<Biome> biomeRegistry,
					Registry<DimensionSettings> dimensionSettingsRegistry, long seed) {
				return new NoiseChunkGenerator(new SilverBiomeProvider(BIOMES, 37), seed, () -> {
					 return dimensionSettingsRegistry.getOrThrow(SILVER_DIM_SETTINGS);
				});
			}

			public DimensionGeneratorSettings createSettings(DynamicRegistries dynamicRegistries, long seed,
					boolean generateStructures, boolean bonusChest, String generatorSettings) {
				Registry<DimensionType> dimensionType = dynamicRegistries
						.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY);
				Registry<Biome> biomes = dynamicRegistries.registryOrThrow(Registry.BIOME_REGISTRY);
				Registry<DimensionSettings> dimensionSettings = dynamicRegistries
						.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY);
				SimpleRegistry<Dimension> simpleregistry = DimensionType.defaultDimensions(dimensionType, biomes,
						dimensionSettings, seed);
				simpleregistry.register(SILVER_DIM,
						new Dimension(() -> DIMENSION_TYPE, this.createChunkGenerator(biomes, dimensionSettings, seed)),
						Lifecycle.experimental());
				ChunkGenerator overworld = DimensionGeneratorSettings.makeDefaultOverworld(biomes, dimensionSettings,
						seed);
				simpleregistry = DimensionGeneratorSettings.withOverworld(dimensionType, simpleregistry, overworld);
				DimensionGeneratorSettings dgs = new DimensionGeneratorSettings(seed, generateStructures, bonusChest,
						simpleregistry);
				return dgs;
			}
		});
	}

	public static final ArrayList<String> BIOME_KEYS = Lists.newArrayList("minecraft:river", "minecraft:frozen_river",
			"minecraft:plains", "minecraft:forest", "minecraft:taiga", "minecraft:jungle", "minecraft:desert",
			"minecraft:badlands", "minecraft:swamp", "moreore:silver_tale", "moreore:golden_mountains",
			"moreore:death_ice_tale", "minecraft:dark_forest_hills", "minecraft:dark_forest");

	public static void initBiomes() {
		String s = settings;
		JsonObject jsonobject = !s.isEmpty() ? JSONUtils.parse(s) : new JsonObject();
		Dynamic<JsonElement> dynamic = new Dynamic<>(JsonOps.INSTANCE, jsonobject);
		WorldGenRegistries.register(WorldGenRegistries.NOISE_GENERATOR_SETTINGS, SILVER_DIM_KEY,
				DimensionSettings.DIRECT_CODEC.parse(dynamic).resultOrPartial(LOGGER::error).orElseGet(() -> {
					return null;
				}));
		for (Biome b : ForgeRegistries.BIOMES.getValues()) {
			if (BIOME_KEYS.contains(b.getRegistryName().toString())) {
				BIOMES.add(() -> b);
			}
		}
	}

	private static final String settings = "{\r\n" + "	\"bedrock_roof_position\": -10,\r\n" + "	\"bedrock_floor_position\": 0,\r\n"
			+ "	\"sea_level\": 7,\r\n" + "	\"disable_mob_generation\": false,\r\n" + "	\"structures\": {\r\n"
			+ "		\"structures\": {\r\n" + "			\"moreore:nut_bush_plantation\": {\r\n"
			+ "				\"spacing\": 80,\r\n" + "				\"separation\": 20,\r\n"
			+ "				\"salt\": 987656789\r\n" + "			},\r\n" + "			\"moreore:tempel\": {\r\n"
			+ "				\"spacing\": 80,\r\n" + "				\"separation\": 20,\r\n"
			+ "				\"salt\": 1664499850\r\n" + "			},\r\n"
			+ "			\"moreore:desert_tempel\": {\r\n" + "				\"spacing\": 80,\r\n"
			+ "				\"separation\": 20,\r\n" + "				\"salt\": 309865502\r\n"
			+ "			},\r\n" + "			\"minecraft:buried_treasure\": {\r\n"
			+ "				\"spacing\": 1,\r\n" + "				\"separation\": 0,\r\n"
			+ "				\"salt\": 0\r\n" + "			},\r\n" + "			\"minecraft:ruined_portal\": {\r\n"
			+ "				\"spacing\": 40,\r\n" + "				\"separation\": 15,\r\n"
			+ "				\"salt\": 34222645\r\n" + "			},\r\n" + "			\"minecraft:mansion\": {\r\n"
			+ "				\"spacing\": 80,\r\n" + "				\"separation\": 20,\r\n"
			+ "				\"salt\": 10387319\r\n" + "			},\r\n"
			+ "			\"minecraft:nether_fossil\": {\r\n" + "				\"spacing\": 2,\r\n"
			+ "				\"separation\": 1,\r\n" + "				\"salt\": 14357921\r\n" + "			},\r\n"
			+ "			\"minecraft:endcity\": {\r\n" + "				\"spacing\": 20,\r\n"
			+ "				\"separation\": 11,\r\n" + "				\"salt\": 10387313\r\n" + "			},\r\n"
			+ "			\"minecraft:igloo\": {\r\n" + "				\"spacing\": 32,\r\n"
			+ "				\"separation\": 8,\r\n" + "				\"salt\": 14357618\r\n" + "			},\r\n"
			+ "			\"minecraft:fortress\": {\r\n" + "				\"spacing\": 27,\r\n"
			+ "				\"separation\": 4,\r\n" + "				\"salt\": 30084232\r\n" + "			},\r\n"
			+ "			\"minecraft:bastion_remnant\": {\r\n" + "				\"spacing\": 27,\r\n"
			+ "				\"separation\": 4,\r\n" + "				\"salt\": 30084232\r\n" + "			},\r\n"
			+ "			\"minecraft:swamp_hut\": {\r\n" + "				\"spacing\": 32,\r\n"
			+ "				\"separation\": 8,\r\n" + "				\"salt\": 14357620\r\n" + "			},\r\n"
			+ "			\"minecraft:monument\": {\r\n" + "				\"spacing\": 32,\r\n"
			+ "				\"separation\": 5,\r\n" + "				\"salt\": 10387313\r\n" + "			},\r\n"
			+ "			\"minecraft:pillager_outpost\": {\r\n" + "				\"spacing\": 32,\r\n"
			+ "				\"separation\": 8,\r\n" + "				\"salt\": 165745296\r\n" + "			},\r\n"
			+ "			\"minecraft:ocean_ruin\": {\r\n" + "				\"spacing\": 20,\r\n"
			+ "				\"separation\": 8,\r\n" + "				\"salt\": 14357621\r\n" + "			},\r\n"
			+ "			\"minecraft:mineshaft\": {\r\n" + "				\"spacing\": 1,\r\n"
			+ "				\"separation\": 0,\r\n" + "				\"salt\": 0\r\n" + "			},\r\n"
			+ "			\"minecraft:village\": {\r\n" + "				\"spacing\": 32,\r\n"
			+ "				\"separation\": 8,\r\n" + "				\"salt\": 10387312\r\n" + "			},\r\n"
			+ "			\"minecraft:shipwreck\": {\r\n" + "				\"spacing\": 24,\r\n"
			+ "				\"separation\": 4,\r\n" + "				\"salt\": 165745295\r\n" + "			},\r\n"
			+ "			\"minecraft:desert_pyramid\": {\r\n" + "				\"spacing\": 32,\r\n"
			+ "				\"separation\": 8,\r\n" + "				\"salt\": 14357617\r\n" + "			},\r\n"
			+ "			\"minecraft:stronghold\": {\r\n" + "				\"spacing\": 1,\r\n"
			+ "				\"separation\": 0,\r\n" + "				\"salt\": 0\r\n" + "			},\r\n"
			+ "			\"minecraft:jungle_pyramid\": {\r\n" + "				\"spacing\": 32,\r\n"
			+ "				\"separation\": 8,\r\n" + "				\"salt\": 14357619\r\n" + "			}\r\n"
			+ "		}\r\n" + "	},\r\n" + "	\"noise\": {\r\n" + "		\"density_factor\": 0.0,\r\n"
			+ "		\"density_offset\": 0.0,\r\n" + "		\"simplex_surface_noise\": true,\r\n"
			+ "		\"bottom_slide\": {\r\n" + "			\"target\": -30,\r\n" + "			\"size\": 7,\r\n"
			+ "			\"offset\": 1\r\n" + "		},\r\n" + "		\"size_horizontal\": 2,\r\n"
			+ "		\"size_vertical\": 1,\r\n" + "		\"height\": 128,\r\n" + "		\"sampling\": {\r\n"
			+ "			\"xz_scale\": 2.0,\r\n" + "			\"y_scale\": 1.0,\r\n"
			+ "			\"xz_factor\": 80.0,\r\n" + "			\"y_factor\": 160.0\r\n" + "		},\r\n"
			+ "		\"top_slide\": {\r\n" + "			\"target\": -3000,\r\n" + "			\"size\": 64,\r\n"
			+ "			\"offset\": -46\r\n" + "		}\r\n" + "	},\r\n" + "	\"default_block\": {\r\n"
			+ "		\"Name\": \"minecraft:stone\"\r\n" + "	},\r\n" + "	\"default_fluid\": {\r\n"
			+ "		\"Properties\": {\r\n" + "			\"level\": \"0\"\r\n" + "		},\r\n"
			+ "		\"Name\": \"minecraft:water\"\r\n" + "	}\r\n" + "}";
}