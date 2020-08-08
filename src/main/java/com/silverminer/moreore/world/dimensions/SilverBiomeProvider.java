package com.silverminer.moreore.world.dimensions;

import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.silverminer.moreore.init.BiomeInit;
import com.silverminer.moreore.world.gen.VoronoiGenerator;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProvider;

public class SilverBiomeProvider extends BiomeProvider {
	private VoronoiGenerator biomeNoise;
	double biomeSize = 32.0d;
	public static final Codec<SilverBiomeProvider> field_235297_e_ = RecordCodecBuilder.create((p_235302_0_) -> {
		return p_235302_0_.group(Codec.LONG.fieldOf("seed").stable().forGetter((p_235304_0_) -> {
			return p_235304_0_.field_235298_h_;
		}), Codec.BOOL.optionalFieldOf("legacy_biome_init_layer", Boolean.valueOf(false), Lifecycle.stable())
				.forGetter((p_235303_0_) -> {
					return p_235303_0_.field_235299_i_;
				}), Codec.BOOL.fieldOf("large_biomes").withDefault(false).stable().forGetter((p_235301_0_) -> {
					return p_235301_0_.field_235300_j_;
				})).apply(p_235302_0_, p_235302_0_.stable(SilverBiomeProvider::new));
	});
	private final long field_235298_h_;
	private Boolean field_235299_i_;
	private Boolean field_235300_j_;

	protected SilverBiomeProvider(long p_i231643_1_, boolean p_i231643_3_, boolean p_i231643_4_) {
		super(biomeList);
		this.field_235298_h_ = 0;
		this.field_235299_i_ = p_i231643_3_;
		this.field_235300_j_ = p_i231643_4_;
		this.biomeNoise = new VoronoiGenerator();
	}

	private static final List<Biome> biomeList = ImmutableList.of(BiomeInit.SILVERTALE.get(),
			BiomeInit.GOLDEN_MOUNTAINS.get(), BiomeInit.DEATH_ICE_TALE.get(), Biomes.RIVER, Biomes.FROZEN_RIVER,
			Biomes.PLAINS, Biomes.FOREST, Biomes.TAIGA, Biomes.JUNGLE, Biomes.DESERT, Biomes.BADLANDS, Biomes.SWAMP);

	@Override
	public Biome getNoiseBiome(int x, int y, int z) {
		return getBiome(new LinkedList<Biome>(biomeList),
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BiomeProvider func_230320_a_(long p_230320_1_) {
		// TODO Auto-generated method stub
		return null;
	}
}
