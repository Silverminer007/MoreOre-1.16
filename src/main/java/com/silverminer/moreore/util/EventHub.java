package com.silverminer.moreore.util;

import com.mojang.brigadier.CommandDispatcher;
import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.commands.ModCommands;
import com.silverminer.moreore.common.portal.PortalWorldSaveData;

import net.minecraft.command.CommandSource;
import net.minecraft.world.World;
import net.minecraft.world.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

public class EventHub {
	@SubscribeEvent
	public static void onWorldLoad(WorldEvent.Load event) {
		World world = event.getWorld().getWorld();

		// WorldSavedData can no longer be stored per map but only per dimension. So
		// store the registry in the overworld.
		if (!world.isRemote && world.func_230315_m_() == DimensionType.func_236019_a_() && world instanceof ServerWorld) {
			MoreOre.portalSaveData = PortalWorldSaveData.get((ServerWorld) world);
		}
	}
	@SubscribeEvent
	public static void onServerStartingEvent(FMLServerStartingEvent event) {
		CommandDispatcher<CommandSource> commandDispatcher = event.getCommandDispatcher();
		new ModCommands(commandDispatcher);
	}
}
