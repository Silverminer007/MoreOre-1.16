package com.silverminer.moreore.init;

import com.silverminer.moreore.MoreOre;
import net.minecraft.entity.item.PaintingType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PaintingTypeInit {
	public static final DeferredRegister<PaintingType> PAINTINGS = DeferredRegister
			.create(ForgeRegistries.PAINTING_TYPES, MoreOre.MODID);

	public static final RegistryObject<PaintingType> J = PAINTINGS.register("j",//This must be the name of the Painting
			() -> new PaintingType(16, 16));//This must be the texture size of the Picture
}