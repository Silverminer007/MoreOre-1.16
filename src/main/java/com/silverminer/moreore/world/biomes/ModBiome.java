package com.silverminer.moreore.world.biomes;

import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.Biome;

public class ModBiome extends Biome {

	protected ModBiome(Builder biomeBuilder) {
		super(biomeBuilder);
	}

	public void addSpawn(EntityClassification type, Biome.SpawnListEntry spawnListEntry) {
		super.addSpawn(type, spawnListEntry);
	}
}