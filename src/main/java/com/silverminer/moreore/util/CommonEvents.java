package com.silverminer.moreore.util;

import com.mojang.brigadier.CommandDispatcher;
import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.client.render.SquirrelRenderer;
import com.silverminer.moreore.client.render.VillageGuardianRenderer;
import com.silverminer.moreore.commands.ModCommands;
import com.silverminer.moreore.common.portal.PortalWorldSaveData;
import com.silverminer.moreore.init.BiomeInit;
import com.silverminer.moreore.init.ModEntityTypesInit;
import com.silverminer.moreore.init.blocks.BiologicBlocks;
import com.silverminer.moreore.objects.blocks.ModLogBlock;
import com.silverminer.moreore.util.items.ComposterItems;
import com.silverminer.moreore.world.gen.Generation;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.command.CommandSource;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

@SuppressWarnings("deprecation")
public class CommonEvents {

	@EventBusSubscriber(modid = MoreOre.MODID, bus = EventBusSubscriber.Bus.MOD)
	public static class ModEventBus {
		@SubscribeEvent
		public void onCommandsRegister(RegisterCommandsEvent event) {
			CommandDispatcher<CommandSource> commandDispatcher = event.getDispatcher();
			new ModCommands(commandDispatcher);
		}

		@SubscribeEvent
		public void setup(final FMLCommonSetupEvent event) {
			DeferredWorkQueue.runLater(Generation::setupWorldGen);

			for (ComposterItems item : MoreOre.composterItems) {
				MoreOre.registerCompostable(item.getChance(), item.getItem().get().asItem());
			}
		}

		@SubscribeEvent
		public static void onRegisterBiomes(final RegistryEvent.Register<Biome> event) {
			BiomeInit.registerBiomes();
		}

		@SubscribeEvent
		public static void setupClient(FMLClientSetupEvent event) {
			for (RegistryObject<Block> block : MoreOre.cutoutBlocks) {
				RenderTypeLookup.setRenderLayer(block.get(), RenderType.getCutout());
			}
			RenderTypeLookup.setRenderLayer(BiologicBlocks.SILVER_TRAPDOOR.get(), RenderType.getCutout());
			RenderTypeLookup.setRenderLayer(BiologicBlocks.POTTET_ICE_SAPLING.get(), RenderType.getCutout());
			RenderTypeLookup.setRenderLayer(BiologicBlocks.POTTET_GOLD_SAPLING.get(), RenderType.getCutout());
			RenderTypeLookup.setRenderLayer(BiologicBlocks.POTTET_SILVER_SAPLING.get(), RenderType.getCutout());
			RenderingRegistry.registerEntityRenderingHandler(ModEntityTypesInit.VILLAGE_GUARDIAN.get(),
					VillageGuardianRenderer::new);
			RenderingRegistry.registerEntityRenderingHandler(ModEntityTypesInit.SQUIRREL.get(), SquirrelRenderer::new);
		}

		@SubscribeEvent
		public static void loadCompleteEvent(FMLLoadCompleteEvent event) {
			DeferredWorkQueue.runLater(Generation::generateOre);
			MoreOre.LOGGER.info("Erz Generiert");
			ModLogBlock.addLogStrippeAble((ModLogBlock) BiologicBlocks.SILVER_LOG.get(),
					(ModLogBlock) BiologicBlocks.STRIPPED_SILVER_LOG.get());
		}
	}

	@EventBusSubscriber(modid = MoreOre.MODID, bus = EventBusSubscriber.Bus.FORGE)
	public static class ForgeEventBus {
		@SubscribeEvent
		public static void onWorldLoad(WorldEvent.Load event) {
			World world = event.getWorld().getWorld();

			// WorldSavedData can no longer be stored per map but only per dimension. So
			// store the registry in the overworld.
			if (!world.isRemote && world.func_230315_m_() == DimensionType.func_236019_a_()
					&& world instanceof ServerWorld) {
				MoreOre.portalSaveData = PortalWorldSaveData.get((ServerWorld) world);
			}
		}
	}
}