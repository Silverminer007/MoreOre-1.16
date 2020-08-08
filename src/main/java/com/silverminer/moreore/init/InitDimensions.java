package com.silverminer.moreore.init;

import com.silverminer.moreore.world.dimensions.ModSilverDimension;
import com.silverminer.moreore.MoreOre;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitDimensions{
	public static final DeferredRegister<ModDimension> WORLDS = DeferredRegister.create(ForgeRegistries.MOD_DIMENSIONS, MoreOre.MODID);
	public static final RegistryObject<ModDimension> SILVER_DIM = WORLDS.register("silver_world", 
			() -> new ModSilverDimension());
}
