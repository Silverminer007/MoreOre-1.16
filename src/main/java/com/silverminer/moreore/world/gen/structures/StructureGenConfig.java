package com.silverminer.moreore.world.gen.structures;

import net.minecraftforge.common.ForgeConfigSpec;

public class StructureGenConfig {

	public static ForgeConfigSpec.BooleanValue GENERATE_TEMPELS;
	public static ForgeConfigSpec.DoubleValue TEMPEL_SPAWN_CHANCE;
	public static ForgeConfigSpec.IntValue TEMPEL_DISTANCE;
	public static ForgeConfigSpec.IntValue TEMPEL_SEPARATION;

	public static ForgeConfigSpec.BooleanValue GENERATE_SCHOOLS;
	public static ForgeConfigSpec.DoubleValue SCHOOL_SPAWN_CHANCE;
	public static ForgeConfigSpec.IntValue SCHOOL_DISTANCE;
	public static ForgeConfigSpec.IntValue SCHOOL_SEPARATION;

	public static ForgeConfigSpec.BooleanValue GENERATE_DESERT_TEMPEL;
	public static ForgeConfigSpec.DoubleValue DESERT_TEMPEL_SPAWN_CHANCE;
	public static ForgeConfigSpec.IntValue DESERT_TEMPEL_DISTANCE;
	public static ForgeConfigSpec.IntValue DESERT_TEMPEL_SEPARATION;

	public static void init(ForgeConfigSpec.Builder SERVER_BUILDER, ForgeConfigSpec.Builder CLIENT_BUILDER) {
		SERVER_BUILDER.comment("Structure Generation Config");

		GENERATE_TEMPELS = SERVER_BUILDER.comment("Generate Tempels").define("structures.tempel.generate", true);
		TEMPEL_SPAWN_CHANCE = SERVER_BUILDER.comment("Tempel Spawn Chance")
				.defineInRange("structures.tempel.spawn_chance", 0.7, 0.0, 1.0);
		TEMPEL_DISTANCE = SERVER_BUILDER.comment("Tempel Distance (in chunks)")
				.defineInRange("structures.tempel.distance", 35, 1, 500);
		TEMPEL_SEPARATION = SERVER_BUILDER.comment("Tempel Minimum Separation (in chunks)")
				.defineInRange("structures.tempel.separation", 8, 1, 500);
		GENERATE_SCHOOLS = SERVER_BUILDER.comment("Generate Schools").define("structures.school.generate", true);
		SCHOOL_SPAWN_CHANCE = SERVER_BUILDER.comment("School Spawn Chance")
				.defineInRange("structures.school.spawn_chance", 0.7, 0.0, 1.0);
		SCHOOL_DISTANCE = SERVER_BUILDER.comment("School Distance (in chunks)")
				.defineInRange("structures.school.distance", 45, 1, 500);
		SCHOOL_SEPARATION = SERVER_BUILDER.comment("School Minimum Separation (in chunks)")
				.defineInRange("structures.school.separation", 10, 1, 500);
		GENERATE_DESERT_TEMPEL = SERVER_BUILDER.comment("Generate Desert Tempel").define("structures.desert_tempel.generate", true);
		DESERT_TEMPEL_SPAWN_CHANCE = SERVER_BUILDER.comment("Desert Tempel Spawn Chance")
				.defineInRange("structures.desert_tempel.spawn_chance", 0.7, 0.0, 1.0);
		DESERT_TEMPEL_DISTANCE = SERVER_BUILDER.comment("Desert Tempel Distance (in chunks)")
				.defineInRange("structures.desert_tempel.distance", 45, 1, 500);
		DESERT_TEMPEL_SEPARATION = SERVER_BUILDER.comment("Desert Tempel Minimum Separation (in chunks)")
				.defineInRange("structures.desert_tempel.separation", 10, 1, 500);
	}
}