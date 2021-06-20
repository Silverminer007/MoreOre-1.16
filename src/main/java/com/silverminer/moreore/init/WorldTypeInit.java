package com.silverminer.moreore.init;

import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.common.world.type.SilverWorldType;

import net.minecraftforge.common.world.ForgeWorldType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class WorldTypeInit {
	public static final DeferredRegister<ForgeWorldType> TYPES = DeferredRegister.create(ForgeRegistries.WORLD_TYPES,
			MoreOre.MODID);

	public static final RegistryObject<SilverWorldType> SILVER = TYPES.register("silver", () -> new SilverWorldType());
}