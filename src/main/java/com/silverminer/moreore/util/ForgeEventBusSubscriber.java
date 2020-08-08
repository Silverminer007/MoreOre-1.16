package com.silverminer.moreore.util;

import com.silverminer.moreore.init.InitDimensions;
import com.silverminer.moreore.MoreOre;

import net.minecraft.world.DimensionType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = MoreOre.MODID, bus = Bus.FORGE)
public class ForgeEventBusSubscriber {

	@SubscribeEvent
	public static void registerDimensions(final RegisterDimensionsEvent event) {
		if (DimensionType.byName(MoreOre.SILVER_DIM_TYPE) == null) {
			DimensionManager.registerDimension(MoreOre.SILVER_DIM_TYPE, InitDimensions.SILVER_DIM.get(), null, true);
		}
	}
}