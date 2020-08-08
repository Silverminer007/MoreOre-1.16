package com.silverminer.moreore;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.moreore.init.BiomeInit;
import com.silverminer.moreore.init.EnchantmentInit;
import com.silverminer.moreore.init.FeatureInit;
import com.silverminer.moreore.init.InitDimensions;
import com.silverminer.moreore.init.ModEntityTypesInit;
import com.silverminer.moreore.init.TreeDecotratorInit;
import com.silverminer.moreore.init.blocks.BiologicBlocks;
import com.silverminer.moreore.init.blocks.InitBlocks;
import com.silverminer.moreore.init.blocks.JonaBlocks;
import com.silverminer.moreore.init.blocks.OreBlocks;
import com.silverminer.moreore.init.items.ArmorItems;
import com.silverminer.moreore.init.items.BlockItems;
import com.silverminer.moreore.init.items.FootItems;
import com.silverminer.moreore.init.items.InitItems;
import com.silverminer.moreore.init.items.JonaItems;
import com.silverminer.moreore.init.items.OreItems;
import com.silverminer.moreore.init.items.ToolItems;
import com.silverminer.moreore.init.items.TreeItems;
import com.silverminer.moreore.objects.blocks.ModLogBlock;
import com.silverminer.moreore.client.render.SquirrelRenderer;
import com.silverminer.moreore.client.render.VillageGuardianRenderer;
import com.silverminer.moreore.common.portal.PortalWorldSaveData;
import com.silverminer.moreore.util.EventHub;
import com.silverminer.moreore.util.items.ComposterItems;
import com.silverminer.moreore.world.gen.Generation;
import com.silverminer.moreore.world.gen.structures.ModStructurePieces;

import net.minecraft.block.Block;
import net.minecraft.block.ComposterBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Dimension;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * @author Silverminer
 * @version Last Edit: Moreore-1.15.2-1.1.1-Pre-1.3
 * @since 25.04.2020
 * @comment This Mod adds Blocks, Items, Biomes, Dimensions, Structures, fun and
 *          much more!!! Have fun!!!
 */
@Mod(MoreOre.MODID)
@EventBusSubscriber(modid = MoreOre.MODID, bus = EventBusSubscriber.Bus.MOD)
public class MoreOre {
	public static final String MODID = "moreore";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	public static PortalWorldSaveData portalSaveData;
	public static ArrayList<ComposterItems> composterItems = new ArrayList<ComposterItems>();
	public static ArrayList<RegistryObject<Block>> cutoutBlocks = new ArrayList<RegistryObject<Block>>();
	public static final ResourceLocation SILVER_DIM_TYPE = new ResourceLocation(MODID, "silver_dimension");

	public MoreOre() {
		LOGGER.debug("Mod: MoreOre wurde geladen");
		MinecraftForge.EVENT_BUS.register(EventHub.class);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.register(EventHub.class);
		ArmorItems.ITEMS.register(modEventBus);
		BlockItems.ITEMS.register(modEventBus);
		FootItems.ITEMS.register(modEventBus);
		OreItems.ITEMS.register(modEventBus);
		ToolItems.ITEMS.register(modEventBus);
		TreeItems.ITEMS.register(modEventBus);
		JonaItems.ITEMS.register(modEventBus);
		InitItems.ITEMS.register(modEventBus);
		InitBlocks.BLOCKS.register(modEventBus);
		JonaBlocks.BLOCKS.register(modEventBus);
		OreBlocks.BLOCKS.register(modEventBus);
		BiologicBlocks.BLOCKS.register(modEventBus);
		BiomeInit.BIOMES.register(modEventBus);
		FeatureInit.FEATURES.register(modEventBus);
		InitDimensions.WORLDS.register(modEventBus);
		ModEntityTypesInit.ENTITIES.register(modEventBus);
		EnchantmentInit.ENCHANTMENTS.register(modEventBus);
		TreeDecotratorInit.TYPES.register(modEventBus);
	}

	public void setup(final FMLCommonSetupEvent event) {
		ModStructurePieces.registerPieces();
		Generation.setupWorldGen();
		LOGGER.info("Registered Pieces");

		for (ComposterItems item : MoreOre.composterItems) {
			registerCompostable(item.getChance(), item.getItem().get().asItem());
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
		RenderingRegistry.registerEntityRenderingHandler(ModEntityTypesInit.SQUIRREL.get(),
				SquirrelRenderer::new);
	}

	@SubscribeEvent
	public static void loadCompleteEvent(FMLLoadCompleteEvent event) {
		Generation.generateOre();
		MoreOre.LOGGER.info("Erz Generiert");
		ModLogBlock.addLogStrippeAble((ModLogBlock) BiologicBlocks.SILVER_LOG.get(),
				(ModLogBlock) BiologicBlocks.STRIPPED_SILVER_LOG.get());
	}

	public static void registerCompostable(float chance, IItemProvider itemIn) {
		ComposterBlock.CHANCES.put(itemIn.asItem(), chance);
	}
}