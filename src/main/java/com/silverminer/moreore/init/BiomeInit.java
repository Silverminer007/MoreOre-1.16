package com.silverminer.moreore.init;

import com.silverminer.moreore.world.biomes.DeathIceTale;
import com.silverminer.moreore.world.biomes.GoldenMountains;
import com.silverminer.moreore.world.biomes.SilverTale;
import com.silverminer.moreore.init.blocks.OreBlocks;
import com.silverminer.moreore.init.blocks.InitBlocks;
import com.silverminer.moreore.MoreOre;

import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeAmbience;
import net.minecraft.world.biome.MoodSoundAmbience;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.Biome.RainType;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BiomeInit {
	public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, MoreOre.MODID);
	public static final RegistryObject<Biome> SILVERTALE = BIOMES.register("silvertale",
			() -> new SilverTale(new Biome.Builder().depth(0.12f).scale(1.0f).temperature(0.9f)// Water (Fog) Color
																								// 0xcdcdcd
					.surfaceBuilder(SurfaceBuilder.DEFAULT,
							new SurfaceBuilderConfig(Blocks.GRASS_BLOCK.getDefaultState(),
									Blocks.DIRT.getDefaultState(), Blocks.STONE.getDefaultState()))
					.category(Category.PLAINS).downfall(0.5f).precipitation(RainType.SNOW)
					.func_235097_a_((new BiomeAmbience.Builder()).func_235246_b_(4159204).func_235248_c_(329011)
							.func_235239_a_(12638463).func_235243_a_(MoodSoundAmbience.field_235027_b_)
							.func_235238_a_())
					.parent("Plains")));

	public static final RegistryObject<Biome> GOLDEN_MOUNTAINS = BIOMES.register("golden_mountains",
			() -> new GoldenMountains(new Biome.Builder().depth(0.12f).scale(1.3f).temperature(2.6f)// Water (Fog) Color
																									// 0xffeeff
					.surfaceBuilder(SurfaceBuilder.DEFAULT,
							new SurfaceBuilderConfig(Blocks.GRASS_BLOCK.getDefaultState(),
									OreBlocks.CAT_GOLD_BLOCK.get().getDefaultState(),
									InitBlocks.GLOW_BLUE_BLOCK.get().getDefaultState()))
					.category(Category.PLAINS).downfall(0.2f).precipitation(RainType.RAIN)
					.func_235097_a_((new BiomeAmbience.Builder()).func_235246_b_(4159204).func_235248_c_(329011)
							.func_235239_a_(12638463).func_235243_a_(MoodSoundAmbience.field_235027_b_)
							.func_235238_a_())
					.parent("Plains")));

	public static final RegistryObject<Biome> DEATH_ICE_TALE = BIOMES.register("death_ice_tale",
			() -> new DeathIceTale(new Biome.Builder().depth(0.7f).scale(0.9f).temperature(-1.2f)// Water Color 0xo0ffe
					.surfaceBuilder(SurfaceBuilder.DEFAULT,
							new SurfaceBuilderConfig(Blocks.GRASS_BLOCK.getDefaultState(),
									Blocks.PACKED_ICE.getDefaultState(), Blocks.BLUE_ICE.getDefaultState()))
					.category(Category.ICY).downfall(0.8f).precipitation(RainType.SNOW)// Water Fog Color 0xc0cc0c
					.func_235097_a_((new BiomeAmbience.Builder()).func_235246_b_(4159204).func_235248_c_(329011)
							.func_235239_a_(12638463).func_235243_a_(MoodSoundAmbience.field_235027_b_)
							.func_235238_a_())
					.parent("Plains")));

	public static void registerBiomes() {
		registerBiome(SILVERTALE.get(), Type.PLAINS);
		registerBiome(GOLDEN_MOUNTAINS.get(), Type.PLAINS);
		registerBiome(DEATH_ICE_TALE.get(), Type.PLAINS);
	}

	private static void registerBiome(Biome biome, Type... types) {
		BiomeDictionary.addTypes(biome, types);
		BiomeManager.addSpawnBiome(biome);
	}
}
