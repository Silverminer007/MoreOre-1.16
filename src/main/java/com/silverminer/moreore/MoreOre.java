package com.silverminer.moreore;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.moreore.init.EnchantmentInit;
import com.silverminer.moreore.init.StructureFeatureInit;
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
import com.silverminer.moreore.common.portal.PortalWorldSaveData;
import com.silverminer.moreore.util.CommonEvents;
import com.silverminer.moreore.util.items.ComposterItems;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * @author Silverminer007
 * @version Last Edit: Moreore-1.16.3-1.1.0-Beta5
 * @since 25.04.2020
 * @comment This Mod adds Blocks, Items, Biomes, Dimensions, Structures, fun and
 *          much more!!! Have fun!!!
 */
@Mod(MoreOre.MODID)
public class MoreOre {
	public static final String MODID = "moreore";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	public static PortalWorldSaveData portalSaveData;
	public static ArrayList<ComposterItems> composterItems = new ArrayList<ComposterItems>();
	public static ArrayList<RegistryObject<Block>> cutoutBlocks = new ArrayList<RegistryObject<Block>>();
	public static final ResourceLocation SILVER_DIM_TYPE = new ResourceLocation("dimensions", "silver_dimension");

	public MoreOre() {
		LOGGER.debug("Mod: MoreOre wurde geladen");
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.register(CommonEvents.class);
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
		StructureFeatureInit.FEATURES.register(modEventBus);
		ModEntityTypesInit.ENTITIES.register(modEventBus);
		EnchantmentInit.ENCHANTMENTS.register(modEventBus);
		TreeDecotratorInit.TYPES.register(modEventBus);
	}
}