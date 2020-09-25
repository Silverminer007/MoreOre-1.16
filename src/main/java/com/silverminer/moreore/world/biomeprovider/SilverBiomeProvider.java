package com.silverminer.moreore.world.biomeprovider;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.silverminer.moreore.world.biomeprovider.gen.VoronoiGenerator;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SilverBiomeProvider extends BiomeProvider {

	public static final Codec<SilverBiomeProvider> CODEC = RecordCodecBuilder.create((biomeProvider) -> {
		return biomeProvider.group(Biome.field_242420_e.fieldOf("biomes").forGetter((provider) -> {
			return provider.biomes;
		}), Codec.intRange(0, 62).fieldOf("biome_size").orElse(30).forGetter((provider) -> {
			return provider.biomeSize;
		})).apply(biomeProvider, SilverBiomeProvider::new);
	});

	private final List<Supplier<Biome>> biomes;

	private final VoronoiGenerator biomeNoise;

	private final int biomeSize;

	protected SilverBiomeProvider(List<Supplier<Biome>> biomes, int size) {
		super(biomes.stream());
		this.biomes = biomes;
		this.biomeNoise = new VoronoiGenerator();
		this.biomeSize = size;
	}

	@Override
	public Biome getNoiseBiome(int x, int y, int z) {
		return getBiome(
				new LinkedList<Biome>(biomes.stream().map(Supplier::get).collect(ImmutableList.toImmutableList())),
				biomeNoise.getValue((double) x / biomeSize, (double) y / biomeSize, (double) z / biomeSize));
	}

	public Biome getBiome(List<Biome> biomeList, double noiseVal) {
		for (int i = biomeList.size(); i >= 0; i--) {
			if (noiseVal > (2.0f / biomeList.size()) * i - 1)
				return biomeList.get(i);
		}
		return biomeList.get(biomeList.size() - 1);
	}

	@Override
	protected Codec<? extends BiomeProvider> func_230319_a_() {
		return CODEC;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public BiomeProvider func_230320_a_(long seed) {
		return this;
	}
}