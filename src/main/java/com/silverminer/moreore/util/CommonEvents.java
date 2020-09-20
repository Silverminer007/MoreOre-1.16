package com.silverminer.moreore.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.brigadier.CommandDispatcher;
import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.client.render.SquirrelRenderer;
import com.silverminer.moreore.client.render.VillageGuardianRenderer;
import com.silverminer.moreore.commands.ModCommands;
import com.silverminer.moreore.common.portal.PortalWorldSaveData;
import com.silverminer.moreore.init.FeatureInit;
import com.silverminer.moreore.init.ModEntityTypesInit;
import com.silverminer.moreore.init.blocks.BiologicBlocks;
import com.silverminer.moreore.objects.blocks.ModLogBlock;
import com.silverminer.moreore.objects.entitys.SquirrelEntity;
import com.silverminer.moreore.objects.entitys.VillageGuardian;
import com.silverminer.moreore.util.items.ComposterItems;
import com.silverminer.moreore.world.gen.Generation;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
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

	public static final Logger LOGGER = LogManager.getLogger(CommonEvents.class);

	@EventBusSubscriber(modid = MoreOre.MODID, bus = EventBusSubscriber.Bus.MOD)
	public static class ModEventBus {
		@SubscribeEvent
		public void onCommandsRegister(RegisterCommandsEvent event) {
			LOGGER.debug("Registering Commands");
			CommandDispatcher<CommandSource> commandDispatcher = event.getDispatcher();
			new ModCommands(commandDispatcher);
		}

		@SubscribeEvent
		public static void setupClient(FMLClientSetupEvent event) {
			LOGGER.debug("Firing Client Setup Event");
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

		@SubscribeEvent
		public static void setup(final FMLCommonSetupEvent event) {
			LOGGER.debug("Firening Setup Event");
			DeferredWorkQueue.runLater(Generation::setupWorldGen);
			DeferredWorkQueue.runLater(() -> {
				GlobalEntityTypeAttributes.put(ModEntityTypesInit.VILLAGE_GUARDIAN.get(),
						VillageGuardian.setCustomAttributes());
			});
			DeferredWorkQueue.runLater(() -> {
				GlobalEntityTypeAttributes.put(ModEntityTypesInit.SQUIRREL.get(),
						SquirrelEntity.setCustomAttributes().func_233813_a_());
			});

			for (ComposterItems item : MoreOre.composterItems) {
				MoreOre.registerCompostable(item.getChance(), item.getItem().get().asItem());
			}
		}
	}

	@EventBusSubscriber(modid = MoreOre.MODID, bus = EventBusSubscriber.Bus.FORGE)
	public static class ForgeEventBus {
		@SubscribeEvent
		public static void onWorldLoad(WorldEvent.Load event) {
			IWorld iworld = event.getWorld();

			if (!(iworld instanceof World))
				return;
			World world = (World) iworld;
			// WorldSavedData can no longer be stored per map but only per dimension. So
			// store the registry in the overworld.
			if (!world.isRemote() && world.func_234923_W_() == World.field_234918_g_ && world instanceof ServerWorld) {
				MoreOre.portalSaveData = PortalWorldSaveData.get((ServerWorld) world);
			}
		}

		@SubscribeEvent
		public static void onBiomeLoad(BiomeLoadingEvent event) {
			LOGGER.debug("The BiomeLoadEvent is Fired for Biome: {}", event.getName());
			if (event.getCategory() == Category.RIVER) {
				return;
			}
			if (event.getCategory() != Category.NETHER && event.getCategory() != Category.THEEND
					&& event.getCategory() != Category.OCEAN) {

				event.getGeneration()
						.func_242516_a(FeatureInit.TEMPEL.get().func_236391_a_(IFeatureConfig.NO_FEATURE_CONFIG));

				event.getGeneration()
						.func_242516_a(FeatureInit.SCHOOL.get().func_236391_a_(IFeatureConfig.NO_FEATURE_CONFIG));

				if (event.getCategory() == Category.DESERT) {
					event.getGeneration().func_242516_a(
							FeatureInit.DESERT_TEMPEL.get().func_236391_a_(IFeatureConfig.NO_FEATURE_CONFIG));
				}
			}
		}
	}
}