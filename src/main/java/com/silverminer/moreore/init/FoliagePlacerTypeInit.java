package com.silverminer.moreore.init;

import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.common.world.gen.tree.foliage_placer.CircleFoliagePlacer;

import net.minecraft.world.gen.foliageplacer.FoliagePlacerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FoliagePlacerTypeInit {
	public static final DeferredRegister<FoliagePlacerType<?>> TYPES = DeferredRegister
			.create(ForgeRegistries.FOLIAGE_PLACER_TYPES, MoreOre.MODID);

	public static final RegistryObject<FoliagePlacerType<CircleFoliagePlacer>> CIRCLE = TYPES.register("circle",
			() -> new FoliagePlacerType<CircleFoliagePlacer>(CircleFoliagePlacer.CODEC));
}