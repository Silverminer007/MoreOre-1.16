package com.silverminer.moreore.world.biomes;

import com.silverminer.moreore.world.gen.tree.IceTree;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.Placement;

public class DeathIceTale extends ModBiome {
	public DeathIceTale(Builder biomeBuilder) {
		super(biomeBuilder);
		DefaultBiomeFeatures.addFreezeTopLayer(this);
		addCarver(GenerationStage.Carving.AIR,
				Biome.createCarver(WorldCarver.CANYON, new ProbabilityConfig(0.098754F)));
		DefaultBiomeFeatures.addOres(this);

		this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, 
				Feature.field_236291_c_.withConfiguration(IceTree.HUGE_ICE_TREE_CONFIG)
				.withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(2, 0.1F, 1))));

		this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION,
				Feature.field_236291_c_.withConfiguration(IceTree.ICE_TREE_CONFIG).withPlacement(
						Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(7, 0.1f, 1))));
		this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.SKELETON, 3, 4, 4));
		this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.ZOMBIE, 3, 4, 4));
		this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.SPIDER, 3, 4, 4));
		this.addSpawn(EntityClassification.MISC, new Biome.SpawnListEntry(EntityType.SNOW_GOLEM, 3, 4, 4));
		this.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(EntityType.SHEEP, 12, 4, 4));
		this.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(EntityType.PIG, 10, 4, 4));
		this.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(EntityType.CHICKEN, 10, 4, 4));
		this.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(EntityType.COW, 8, 4, 4));
		this.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(EntityType.HORSE, 5, 2, 6));
		this.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(EntityType.DONKEY, 1, 1, 3));
	}
}