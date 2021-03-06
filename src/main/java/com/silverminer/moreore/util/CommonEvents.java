package com.silverminer.moreore.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import com.mojang.brigadier.CommandDispatcher;
import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.client.render.SquirrelRenderer;
import com.silverminer.moreore.client.render.VillageGuardianRenderer;
import com.silverminer.moreore.common.commands.TlpCommand;
import com.silverminer.moreore.common.objects.entitys.SquirrelEntity;
import com.silverminer.moreore.common.objects.entitys.VillageGuardian;
import com.silverminer.moreore.common.portal.PortalWorldSaveData;
import com.silverminer.moreore.common.world.biomeprovider.SilverBiomeProvider;
import com.silverminer.moreore.common.world.gen.features.OreFeatures;
import com.silverminer.moreore.common.world.gen.features.TreeFeatures;
import com.silverminer.moreore.common.world.gen.tree.GoldTree;
import com.silverminer.moreore.common.world.gen.tree.IceTree;
import com.silverminer.moreore.common.world.gen.tree.NutBush;
import com.silverminer.moreore.init.ModEntityTypesInit;
import com.silverminer.moreore.init.ModStructureFeatures;
import com.silverminer.moreore.init.blocks.BiologicBlocks;
import com.silverminer.moreore.util.items.ComposterItems;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ComposterBlock;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.Biome.RainType;
import net.minecraft.world.biome.MobSpawnInfo.Spawners;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class CommonEvents {

	protected static final Logger LOGGER = LogManager.getLogger(CommonEvents.class);

	@EventBusSubscriber(modid = MoreOre.MODID, bus = EventBusSubscriber.Bus.MOD)
	public static class ModEventBus {
		protected static final Marker MARKER = MarkerManager.getMarker("ModEventBus");

		@SubscribeEvent
		public static void setupClient(FMLClientSetupEvent event) {
			LOGGER.debug(MARKER, "Firing Client Setup Event");
			for (RegistryObject<Block> block : MoreOre.cutoutBlocks) {
				RenderTypeLookup.setRenderLayer(block.get(), RenderType.getCutout());
			}
			RenderTypeLookup.setRenderLayer(BiologicBlocks.SILVER_TRAPDOOR.get(), RenderType.getCutout());
			RenderTypeLookup.setRenderLayer(BiologicBlocks.POTTET_ICE_SAPLING.get(), RenderType.getCutout());
			RenderTypeLookup.setRenderLayer(BiologicBlocks.POTTET_GOLD_SAPLING.get(), RenderType.getCutout());
			RenderTypeLookup.setRenderLayer(BiologicBlocks.POTTET_SILVER_SAPLING.get(), RenderType.getCutout());
			RenderTypeLookup.setRenderLayer(BiologicBlocks.POTTET_NUT_BUSH_SAPLING.get(), RenderType.getCutout());
			RenderTypeLookup.setRenderLayer(BiologicBlocks.NUT_BUSH_LOG.get(), RenderType.getCutout());
			RenderingRegistry.registerEntityRenderingHandler(ModEntityTypesInit.VILLAGE_GUARDIAN.get(),
					VillageGuardianRenderer::new);
			RenderingRegistry.registerEntityRenderingHandler(ModEntityTypesInit.SQUIRREL.get(), SquirrelRenderer::new);
		}

		@SubscribeEvent
		public static void setup(final FMLCommonSetupEvent event) {
			LOGGER.debug(MARKER, "Firening Setup Event");

			// Make every Item in this list Compostable in an composter block
			for (ComposterItems item : MoreOre.composterItems) {
				ComposterBlock.CHANCES.put(item.getItem().get().asItem(), item.getChance());
			}

			// Add the Saplings to the allowed Blocks of the Flower Pot
			((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(BiologicBlocks.GOLD_SAPLING.get().getRegistryName(),
					() -> BiologicBlocks.POTTET_GOLD_SAPLING.get());
			((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(BiologicBlocks.SILVER_SAPLING.get().getRegistryName(),
					() -> BiologicBlocks.POTTET_SILVER_SAPLING.get());
			((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(BiologicBlocks.ICE_SAPLING.get().getRegistryName(),
					() -> BiologicBlocks.POTTET_ICE_SAPLING.get());
			((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(BiologicBlocks.NUT_BUSH_SAPLING.get().getRegistryName(),
					() -> BiologicBlocks.POTTET_NUT_BUSH_SAPLING.get());

			// Add the SilverBiomeProvider to be use able in .json files for better Biome
			// forming
			Registry.register(Registry.BIOME_PROVIDER_CODEC, new ResourceLocation(MoreOre.MODID, "silver"),
					SilverBiomeProvider.CODEC);

			event.enqueueWork(() -> {
				OreFeatures.registerOres();
				StructureUtils.setupWorldGen();
			});
		}

		@SubscribeEvent
		public static void entityAttribute(final EntityAttributeCreationEvent event) {
			event.put(ModEntityTypesInit.VILLAGE_GUARDIAN.get(), VillageGuardian.setCustomAttributes());
			event.put(ModEntityTypesInit.SQUIRREL.get(), SquirrelEntity.setCustomAttributes());

		}
	}

	@EventBusSubscriber(modid = MoreOre.MODID, bus = EventBusSubscriber.Bus.FORGE)
	public static class ForgeEventBus {
		protected static final Marker MARKER = MarkerManager.getMarker("ForgeEventBus");

		@SubscribeEvent
		public static void onWorldLoad(WorldEvent.Load event) {
			LOGGER.debug(MARKER, "Firing World Load Event");
			IWorld iworld = event.getWorld();

			if (!(iworld instanceof World))
				return;
			World world = (World) iworld;
			// WorldSavedData can no longer be stored per map but only per dimension. So
			// store the registry in the overworld.
			if (!world.isRemote() && world.getDimensionKey() == World.OVERWORLD && world instanceof ServerWorld) {
				MoreOre.portalSaveData = PortalWorldSaveData.get((ServerWorld) world);
			}
		}

		@SubscribeEvent
		public static void onCommandsRegister(RegisterCommandsEvent event) {
			LOGGER.debug(MARKER, "Registering Commands");
			CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
			TlpCommand.register(dispatcher);
		}

		@SubscribeEvent(priority = EventPriority.HIGH)
		public static void onBiomeLoad(BiomeLoadingEvent event) {
			// Features

			if (event.getName().equals(new ResourceLocation(MoreOre.MODID, "silver_tale"))) {
				// Generate Gold trees and Silver trees
				event.getGeneration().withFeature(9, () -> TreeFeatures.SILVER_GOLD_TREE_RANDOM);
				// Generate Silver ore
				event.getGeneration().withFeature(7, () -> OreFeatures.SILVER);

			} else if (event.getName().equals(new ResourceLocation(MoreOre.MODID, "golden_mountains"))) {
				// Generate Gold Tree
				event.getGeneration().withFeature(9, () -> GoldTree.GOLD_TREE_RANDOM);

			} else if (event.getName().equals(new ResourceLocation(MoreOre.MODID, "death_ice_tale"))) {
				// Generate Ice Tree
				event.getGeneration().withFeature(9, () -> IceTree.ICE_TREE_RANDOM);
				// Generate RUBIN and SAPHIR
				event.getGeneration().withFeature(7, () -> OreFeatures.SAPHIR);
				event.getGeneration().withFeature(7, () -> OreFeatures.RUBIN);

			} else if (event.getName().equals(new ResourceLocation("minecraft", "forest"))) {
				// Generate NutBushs in Forests
				event.getGeneration().withFeature(9, () -> NutBush.NUT_BUSH_RANDOM);
			}
			// Generate these Ores in every biome
			event.getGeneration().withFeature(7, () -> OreFeatures.ALEXANDRIT);
			event.getGeneration().withFeature(7, () -> OreFeatures.RAINBOW);

			// Structures

			if (event.getCategory() != Category.NETHER && event.getCategory() != Category.THEEND
					&& event.getCategory() != Category.OCEAN && event.getCategory() != Category.RIVER) {

				event.getGeneration().withStructure(ModStructureFeatures.TEMPEL);
				event.getGeneration().withStructure(ModStructureFeatures.NUT_BUSH_PLANTATION);

				// Generate the Desert Tempel Structure in every Biome, where it doesn't rain
				if (event.getClimate().precipitation == RainType.NONE) {
					event.getGeneration().withStructure(ModStructureFeatures.DESERT_TEMPEL);
				}
			}

			// Enties

			if (event.getName().equals(new ResourceLocation("minecraft", "flower_forest"))
					|| event.getName().equals(new ResourceLocation("minecraft", "forest"))
					|| event.getName().equals(new ResourceLocation(MoreOre.MODID, "silver_tale"))) {
				// Spawn Squirrels in Silvertales and Forests
				event.getSpawns().getSpawner(EntityClassification.CREATURE)
						.add(new Spawners(ModEntityTypesInit.SQUIRREL.get(), 10, 4, 4));
			}
		}
	}
}