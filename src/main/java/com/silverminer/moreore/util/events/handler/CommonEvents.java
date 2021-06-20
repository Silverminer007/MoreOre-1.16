package com.silverminer.moreore.util.events.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.client.gui.screen.RunetableScreen;
import com.silverminer.moreore.client.render.GiantZombieKingRenderer;
import com.silverminer.moreore.client.render.SquirrelRenderer;
import com.silverminer.moreore.client.render.VillageGuardianRenderer;
import com.silverminer.moreore.common.commands.TlpCommand;
import com.silverminer.moreore.common.objects.entitys.GiantZombieKingEntity;
import com.silverminer.moreore.common.objects.entitys.SquirrelEntity;
import com.silverminer.moreore.common.objects.entitys.VillageGuardian;
import com.silverminer.moreore.common.portal.PortalWorldSaveData;
import com.silverminer.moreore.common.recipe.ModRecipeType;
import com.silverminer.moreore.common.recipe.RuneRecipe;
import com.silverminer.moreore.common.world.gen.features.OreFeatures;
import com.silverminer.moreore.common.world.gen.features.TreeFeatures;
import com.silverminer.moreore.common.world.gen.structures.MoreoreStructurePieceType;
import com.silverminer.moreore.common.world.gen.tree.GoldTree;
import com.silverminer.moreore.common.world.gen.tree.IceTree;
import com.silverminer.moreore.common.world.gen.tree.NutBush;
import com.silverminer.moreore.common.world.type.SilverWorldType;
import com.silverminer.moreore.init.ContainerTypesInit;
import com.silverminer.moreore.init.ModEntityTypesInit;
import com.silverminer.moreore.init.blocks.BiologicBlocks;
import com.silverminer.moreore.init.items.RuneItems;
import com.silverminer.moreore.init.structures.ModStructureFeatures;
import com.silverminer.moreore.util.events.RuneInventoryChangeEvent;
import com.silverminer.moreore.util.helpers.silver_dim.ObjHolder;
import com.silverminer.moreore.util.items.ComposterItems;
import com.silverminer.moreore.util.network.MoreorePacketHandler;
import com.silverminer.moreore.util.runes.RuneInventoryRegistry;
import com.silverminer.moreore.util.runes.RuneSaveData;
import com.silverminer.moreore.util.structures.StructureUtils;
import com.silverminer.moreore.util.structures.config.Config;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ComposterBlock;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.CreateWorldScreen;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.resources.DataPackRegistries;
import net.minecraft.resources.FolderPackFinder;
import net.minecraft.resources.IPackNameDecorator;
import net.minecraft.resources.ResourcePackList;
import net.minecraft.resources.ServerPackFinder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.codec.DatapackCodec;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldSettingsImport;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.MobSpawnInfo.Spawners;
import net.minecraft.world.gen.settings.DimensionGeneratorSettings;
import net.minecraft.world.GameRules;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.BlockEvent;
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
				RenderTypeLookup.setRenderLayer(block.get(), RenderType.cutout());
			}
			RenderTypeLookup.setRenderLayer(BiologicBlocks.SILVER_TRAPDOOR.get(), RenderType.cutout());
			RenderTypeLookup.setRenderLayer(BiologicBlocks.POTTET_ICE_SAPLING.get(), RenderType.cutout());
			RenderTypeLookup.setRenderLayer(BiologicBlocks.POTTET_GOLD_SAPLING.get(), RenderType.cutout());
			RenderTypeLookup.setRenderLayer(BiologicBlocks.POTTET_SILVER_SAPLING.get(), RenderType.cutout());
			RenderTypeLookup.setRenderLayer(BiologicBlocks.POTTET_NUT_BUSH_SAPLING.get(), RenderType.cutout());
			RenderTypeLookup.setRenderLayer(BiologicBlocks.NUT_BUSH_LOG.get(), RenderType.cutout());
			RenderingRegistry.registerEntityRenderingHandler(ModEntityTypesInit.VILLAGE_GUARDIAN.get(),
					VillageGuardianRenderer::new);
			RenderingRegistry.registerEntityRenderingHandler(ModEntityTypesInit.SQUIRREL.get(), SquirrelRenderer::new);
			RenderingRegistry.registerEntityRenderingHandler(ModEntityTypesInit.GIANT_ZOMBIE_KING.get(),
					GiantZombieKingRenderer::new);
			ScreenManager.register(ContainerTypesInit.RUNE_TABLE.get(), RunetableScreen::new);
		}

		@SubscribeEvent
		public static void setup(final FMLCommonSetupEvent event) {
			LOGGER.debug(MARKER, "Firening Setup Event");
			MoreorePacketHandler.register();

			// Make every Item in this list Compostable in an composter block
			for (ComposterItems item : MoreOre.composterItems) {
				ComposterBlock.COMPOSTABLES.put(item.getItem().get().asItem(), item.getChance());
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

			ModRecipeType.RUNES = Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(MoreOre.MODID, "runes"),
					new IRecipeType<RuneRecipe>() {
						public String toString() {
							return new ResourceLocation(MoreOre.MODID, "runes").toString();
						}
					});

			event.enqueueWork(() -> {
				StructureUtils.setupWorldGen();
				MoreoreStructurePieceType.regsiter();
				OreFeatures.registerOres();
				SilverWorldType.initBiomes();
			});
		}

		@SubscribeEvent
		public static void entityAttribute(final EntityAttributeCreationEvent event) {
			event.put(ModEntityTypesInit.VILLAGE_GUARDIAN.get(), VillageGuardian.setCustomAttributes());
			event.put(ModEntityTypesInit.SQUIRREL.get(), SquirrelEntity.setCustomAttributes());
			event.put(ModEntityTypesInit.GIANT_ZOMBIE_KING.get(), GiantZombieKingEntity.setCustomAttributes());

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
			if (!world.isClientSide() && world.dimension() == World.OVERWORLD && world instanceof ServerWorld) {
				MoreOre.portalSaveData = PortalWorldSaveData.get((ServerWorld) world);
				MoreOre.runeSaveData = RuneSaveData.get((ServerWorld) world);
			}
		}

		@SubscribeEvent
		public static void onCommandsRegister(RegisterCommandsEvent event) {
			LOGGER.debug(MARKER, "Registering Commands");
			CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
			TlpCommand.register(dispatcher);
		}

		@SuppressWarnings("resource")
		@SubscribeEvent
		public static void onGuiOpen(GuiOpenEvent event) {
			/*if (event.getGui() instanceof CreateWorldScreen) {
				if (Config.STRUCTURES.USE_SILVER_DIMENSION.get()) {
					CreateWorldScreen cWS = (CreateWorldScreen) event.getGui();
					File modsDir = new File(Minecraft.getInstance().gameDirectory, "mods");
					if (modsDir.exists()) {
						modsDir = new File(modsDir, "silver_dimension_1.2.0.json");
						try {
							if (modsDir.createNewFile()) {
								FileWriter out = new FileWriter(modsDir);
								out.write(ObjHolder.SILVER_DIM_JSON);
								out.close();
							}
							BufferedReader br = new BufferedReader(new FileReader(modsDir));
							StringBuilder sb = new StringBuilder();
							try {
								String line = br.readLine();

								while (line != null) {
									sb.append(line);
									sb.append(System.lineSeparator());
									line = br.readLine();
								}
							} finally {
								br.close();
							}
							String text = sb.toString();
							long seed = (new Random()).nextLong();
							text = text.replaceAll("\"seed\": 0", "\"seed\": " + seed);
							modsDir.delete();
							if (modsDir.createNewFile()) {
								FileWriter out = new FileWriter(modsDir);
								out.write(text);
								out.close();
							}
							DynamicRegistries.Impl dynamicregistries$impl = DynamicRegistries.builtin();
							ResourcePackList resourcepacklist = new ResourcePackList(new ServerPackFinder(),
									new FolderPackFinder(cWS.getTempDataPackDir().toFile(), IPackNameDecorator.WORLD));

							DataPackRegistries datapackregistries;
							try {
								Class<?> c = cWS.getClass();
								Field f = c.getDeclaredField("field_238933_b_");
								f.setAccessible(true);
								MinecraftServer.configurePackRepository(resourcepacklist, (DatapackCodec) f.get(cWS),
										false);
								CompletableFuture<DataPackRegistries> completablefuture = DataPackRegistries
										.loadResources(resourcepacklist.openAllSelected(),
												Commands.EnvironmentType.INTEGRATED, 2, Util.bootstrapExecutor(),
												Minecraft.getInstance());
								Minecraft.getInstance().managedBlock(completablefuture::isDone);
								datapackregistries = completablefuture.get();
							} catch (Throwable interruptedexception) {
								LOGGER.error("Error loading data packs when importing world settings",
										(Throwable) interruptedexception);
								ITextComponent itextcomponent = new TranslationTextComponent(
										"selectWorld.import_worldgen_settings.failure");
								ITextComponent itextcomponent1 = new StringTextComponent(
										interruptedexception.getMessage());
								Minecraft.getInstance().getToasts()
										.addToast(SystemToast.multiline(Minecraft.getInstance(),
												SystemToast.Type.WORLD_GEN_SETTINGS_TRANSFER, itextcomponent,
												itextcomponent1));
								resourcepacklist.close();
								return;
							}
							WorldSettingsImport<JsonElement> worldsettingsimport = WorldSettingsImport.create(
									JsonOps.INSTANCE, datapackregistries.getResourceManager(), dynamicregistries$impl);
							JsonParser jsonparser = new JsonParser();

							DataResult<DimensionGeneratorSettings> dataresult;
							try (BufferedReader bufferedreader = Files
									.newBufferedReader(Paths.get(modsDir.getAbsolutePath()))) {
								JsonElement jsonelement = jsonparser.parse(bufferedreader);
								dataresult = DimensionGeneratorSettings.CODEC.parse(worldsettingsimport, jsonelement);
							} catch (JsonIOException | JsonSyntaxException | IOException ioexception) {
								dataresult = DataResult.error("Failed to parse file: " + ioexception.getMessage());
							}
							br = new BufferedReader(new FileReader(modsDir));
							sb = new StringBuilder();
							try {
								String line = br.readLine();

								while (line != null) {
									sb.append(line);
									sb.append(System.lineSeparator());
									line = br.readLine();
								}
							} finally {
								br.close();
							}
							text = sb.toString();
							text = text.replaceAll("\"seed\": " + seed, "\"seed\": 0");
							modsDir.delete();
							if (modsDir.createNewFile()) {
								FileWriter out = new FileWriter(modsDir);
								out.write(text);
								out.close();
							}

							if (dataresult.error().isPresent()) {
								ITextComponent itextcomponent2 = new TranslationTextComponent(
										"selectWorld.import_worldgen_settings.failure");
								String s1 = dataresult.error().get().message();
								LOGGER.error("Error parsing world settings: {}", (Object) s1);
								ITextComponent itextcomponent3 = new StringTextComponent(s1);
								Minecraft.getInstance().getToasts()
										.addToast(SystemToast.multiline(Minecraft.getInstance(),
												SystemToast.Type.WORLD_GEN_SETTINGS_TRANSFER, itextcomponent2,
												itextcomponent3));
							}

							datapackregistries.close();
							dataresult.resultOrPartial(LOGGER::error).ifPresent((p_239046_5_) -> {
								try {
									Class<?> c = cWS.worldGenSettingsComponent.getClass();
									Method m = c.getDeclaredMethod("func_239052_a_", DynamicRegistries.Impl.class,
											DimensionGeneratorSettings.class);
									m.setAccessible(true);
									cWS.worldGenSettingsComponent.init(cWS, Minecraft.getInstance(),
											Minecraft.getInstance().font);
									LOGGER.info("Added Silver Dimension to the world");
									m.invoke(cWS.worldGenSettingsComponent, dynamicregistries$impl, p_239046_5_);
									event.setGui(cWS);

								} catch (Throwable e) {
									e.printStackTrace();
									LOGGER.error(e.getCause());
								}
							});
						} catch (IOException e) {
							e.printStackTrace();
							return;
						}
					}
				} else {
					LOGGER.info("Silver Dimension wasn't registered because config disabled this");
				}
			}*/
		}

		@SubscribeEvent(priority = EventPriority.HIGH)
		public static void onBiomeLoad(BiomeLoadingEvent event) {
			// Features

			if (event.getName().equals(new ResourceLocation(MoreOre.MODID, "silver_tale"))) {
				// Generate Gold trees and Silver trees
				event.getGeneration().addFeature(9, () -> TreeFeatures.SILVER_GOLD_TREE_RANDOM);
				// Generate Silver ore
				event.getGeneration().addFeature(7, () -> OreFeatures.SILVER);

			} else if (event.getName().equals(new ResourceLocation(MoreOre.MODID, "golden_mountains"))) {
				// Generate Gold Tree
				event.getGeneration().addFeature(9, () -> GoldTree.GOLD_TREE_RANDOM);

			} else if (event.getName().equals(new ResourceLocation(MoreOre.MODID, "death_ice_tale"))) {
				// Generate Ice Tree
				event.getGeneration().addFeature(9, () -> IceTree.ICE_TREE_RANDOM);
				// Generate RUBIN and SAPHIR
				event.getGeneration().addFeature(7, () -> OreFeatures.SAPHIR);
				event.getGeneration().addFeature(7, () -> OreFeatures.RUBIN);

			} else if (event.getName().equals(new ResourceLocation("minecraft", "forest"))) {
				// Generate NutBushs in Forests
				event.getGeneration().addFeature(9, () -> NutBush.NUT_BUSH_RANDOM);
			}
			// Generate these Ores in every biome
			event.getGeneration().addFeature(7, () -> OreFeatures.ALEXANDRIT);
			event.getGeneration().addFeature(7, () -> OreFeatures.RAINBOW);

			// Structures
			if (!Config.STRUCTURES.BLACKLISTED_BIOMES.get().contains(event.getName().toString())) {
				if (Config.STRUCTURES.TEMPEL.GENERATE.get()
						&& StructureUtils.checkBiome(Config.STRUCTURES.TEMPEL.BIOME_CATEGORIES.get(),
								Config.STRUCTURES.TEMPEL.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().addStructureStart(ModStructureFeatures.TEMPEL);
				}
				if (Config.STRUCTURES.NUT_BUSH_PLANTATION.GENERATE.get()
						&& StructureUtils.checkBiome(Config.STRUCTURES.NUT_BUSH_PLANTATION.BIOME_CATEGORIES.get(),
								Config.STRUCTURES.NUT_BUSH_PLANTATION.BIOME_BLACKLIST.get(), event.getName(),
								event.getCategory())) {
					event.getGeneration().addStructureStart(ModStructureFeatures.NUT_BUSH_PLANTATION);
				}
				// Rune Structures
				if (Config.STRUCTURES.DESERT_TEMPEL.GENERATE.get() && StructureUtils.checkBiome(
						Config.STRUCTURES.DESERT_TEMPEL.BIOME_CATEGORIES.get(),
						Config.STRUCTURES.DESERT_TEMPEL.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().addStructureStart(ModStructureFeatures.DESERT_TEMPEL);
				}
				if (Config.STRUCTURES.GREEN_DUNGEON.GENERATE.get() && StructureUtils.checkBiome(
						Config.STRUCTURES.GREEN_DUNGEON.BIOME_CATEGORIES.get(),
						Config.STRUCTURES.GREEN_DUNGEON.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().addStructureStart(ModStructureFeatures.GREEN_DUNGEON);
				}
				if (Config.STRUCTURES.ORANGE_SHRINE.GENERATE.get() && StructureUtils.checkBiome(
						Config.STRUCTURES.ORANGE_SHRINE.BIOME_CATEGORIES.get(),
						Config.STRUCTURES.ORANGE_SHRINE.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().addStructureStart(ModStructureFeatures.ORANGE_SHRINE);
				}
				if (Config.STRUCTURES.BROWN_LANDINGSTAGE.GENERATE.get()
						&& StructureUtils.checkBiome(Config.STRUCTURES.BROWN_LANDINGSTAGE.BIOME_CATEGORIES.get(),
								Config.STRUCTURES.BROWN_LANDINGSTAGE.BIOME_BLACKLIST.get(), event.getName(),
								event.getCategory())) {
					event.getGeneration().addStructureStart(ModStructureFeatures.BROWN_LANDINGSTAGE);
				}
				if (Config.STRUCTURES.PURPLE_HOUSE.GENERATE.get() && StructureUtils.checkBiome(
						Config.STRUCTURES.PURPLE_HOUSE.BIOME_CATEGORIES.get(),
						Config.STRUCTURES.PURPLE_HOUSE.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().addStructureStart(ModStructureFeatures.PURPLE_HOUSE);
				}
				if (Config.STRUCTURES.GIANT.GENERATE.get()
						&& StructureUtils.checkBiome(Config.STRUCTURES.GIANT.BIOME_CATEGORIES.get(),
								Config.STRUCTURES.GIANT.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().addStructureStart(ModStructureFeatures.GIANT);
				}
				if (Config.STRUCTURES.BLACK_SMITH.GENERATE.get() && StructureUtils.checkBiome(
						Config.STRUCTURES.BLACK_SMITH.BIOME_CATEGORIES.get(),
						Config.STRUCTURES.BLACK_SMITH.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().addStructureStart(ModStructureFeatures.BLACK_SMITH);
				}
			}
			boolean flag = Config.STRUCTURES.BLUE_RUNE.USE_HOUSE.get();
			if (Config.STRUCTURES.BLUE_RUNE.GENERATE.get() && StructureUtils.checkBiome(
					flag ? Config.STRUCTURES.BLUE_RUNE.BIOME_CATEGORIES.get()
							: Config.STRUCTURES.BLUE_RUNE.BIOME_CATEGORIES_VIKING_SHIP.get(),
					flag ? Config.STRUCTURES.BLUE_RUNE.BIOME_BLACKLIST.get()
							: Config.STRUCTURES.BLUE_RUNE.BIOME_BLACKLIST_VIKING_SHIP.get(),
					event.getName(), event.getCategory())) {
				event.getGeneration().addStructureStart(ModStructureFeatures.BLUE_RUNE);
			}
			if (Thread.currentThread().getName() == "Render thread" || Thread.currentThread().getName() == "Server thread") {
				USE_HOUSE = flag;
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

		/** TODO: This isn't the best way. I have to search for a better one */
		public static boolean USE_HOUSE = false;

		/**********************************************************************************************************
		 * Apply Rune effects START
		 *******************************************************************************************************/
		@SubscribeEvent
		public static void getBreakSpeed(PlayerEvent.BreakSpeed event) {
			PlayerEntity player = event.getPlayer();
			Inventory inv = RuneInventoryRegistry.getInventory(player.getUUID());
			if (inv.hasAnyOf(Sets.newHashSet(RuneItems.RUNE_YELLOW.get()))) {
				float addDamage = 1.0F + 0.2F * damageItems(inv, player, RuneItems.RUNE_RED.get(), 1, 0.02F);
				RuneInventoryRegistry.setInventory(player, inv);
				event.setNewSpeed(event.getOriginalSpeed() * addDamage);
			}
		}

		@SubscribeEvent
		public static void applyRuneEffects(LivingAttackEvent event) {
			if (event.getEntityLiving() instanceof PlayerEntity && !event.getEntity().level.isClientSide()) {
				PlayerEntity player = (PlayerEntity) event.getEntityLiving();
				Inventory inv = RuneInventoryRegistry.getInventory(player.getUUID());
				if (inv.hasAnyOf(Sets.newHashSet(RuneItems.RUNE_RED.get())) && event.getSource().isFire()) {
					damageItems(inv, player, RuneItems.RUNE_RED.get(), 1, 0.075F);
					event.setCanceled(true);
				} else if (inv.hasAnyOf(Sets.newHashSet(RuneItems.RUNE_BLUE.get()))
						&& event.getSource() == DamageSource.DROWN) {
					damageItems(inv, player, RuneItems.RUNE_BLUE.get(), 1, 0.075F);
					event.setCanceled(true);
				}
			}
		}

		@SubscribeEvent
		public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
			if (event.getEntityLiving() instanceof PlayerEntity && event.getItem().isEdible()) {
				PlayerEntity player = (PlayerEntity) event.getEntityLiving();
				Inventory inv = RuneInventoryRegistry.getInventory(player.getUUID());
				if (inv.hasAnyOf(Sets.newHashSet(RuneItems.RUNE_BROWN.get()))) {
					int addFoot = damageItems(inv, player, RuneItems.RUNE_BROWN.get(), 3, 0.25F);
					player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() + addFoot);
				}
			}
		}

		@SubscribeEvent
		public static void onBlockBreak(BlockEvent.BreakEvent event) {
			if (event.getWorld() instanceof World
					&& ((World) event.getWorld()).getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
				PlayerEntity player = event.getPlayer();
				Inventory inv = RuneInventoryRegistry.getInventory(player.getUUID());
				if (inv.hasAnyOf(Sets.newHashSet(RuneItems.RUNE_GREEN.get()))
						&& event.getState().getBlock() instanceof CropsBlock) {
					int addDrobs = damageItems(inv, player, RuneItems.RUNE_GREEN.get(), 3, 0.25F)
							* (event.getWorld().getRandom().nextInt(2) + 1);
					if (event.getWorld() instanceof ServerWorld) {
						ServerWorld serverWorld = (ServerWorld) event.getWorld();
						event.getState()
								.getDrops(new LootContext.Builder(serverWorld).withRandom(serverWorld.getRandom())
										.withParameter(LootParameters.TOOL, player.getMainHandItem())
										.withParameter(LootParameters.ORIGIN, new Vector3d(event.getPos().getX(),
												event.getPos().getY(), event.getPos().getZ())))
								.forEach(stack -> {
									stack.setCount(stack.getCount() * addDrobs);
									serverWorld.addFreshEntity(new ItemEntity(serverWorld, event.getPos().getX(),
											event.getPos().getY(), event.getPos().getZ(), stack));
								});
					}
					event.setExpToDrop(event.getExpToDrop() * addDrobs);
				}
				if (inv.hasAnyOf(Sets.newHashSet(RuneItems.RUNE_PURPLE.get()))) {
					int addDrobs = damageItems(inv, player, RuneItems.RUNE_PURPLE.get(), 3, 0.3F)
							* (event.getWorld().getRandom().nextInt(2) + 1) / 2;
					if (event.getWorld() instanceof ServerWorld) {
						ServerWorld serverWorld = (ServerWorld) event.getWorld();
						event.getState()
								.getDrops(new LootContext.Builder(serverWorld).withRandom(serverWorld.getRandom())
										.withParameter(LootParameters.TOOL, player.getMainHandItem())
										.withParameter(LootParameters.ORIGIN, new Vector3d(event.getPos().getX(),
												event.getPos().getY(), event.getPos().getZ())))
								.forEach(stack -> {
									stack.setCount(stack.getCount() * addDrobs);
									if (!(stack.getItem() instanceof BlockItem))
										serverWorld.addFreshEntity(new ItemEntity(serverWorld, event.getPos().getX(),
												event.getPos().getY(), event.getPos().getZ(), stack));
								});
					}
					event.setExpToDrop(event.getExpToDrop() * addDrobs / 2);
				}
			}
		}

		private static int damageItems(Inventory inv, PlayerEntity player, Item item, int maxDamageItems,
				float breakChance) {
			int j = 0;
			int invSize = RuneInventoryRegistry.getInventorySize(player.getUUID());
			for (int i = 0; i < invSize; i++) {
				ItemStack stack = inv.getItem(i).copy();
				if (stack.getItem() == item) {
					j++;
					if (player.getRandom().nextFloat() < breakChance && !player.abilities.instabuild) {
						stack.setDamageValue(stack.getDamageValue() + 1);
						if (stack.getDamageValue() == stack.getMaxDamage()) {
							if (!player.level.isClientSide()) {
								player.sendMessage(
										new TranslationTextComponent("runes.moreore.broken", stack.getDisplayName()),
										player.getUUID());
							}
							stack = ItemStack.EMPTY;
						}
						inv.setItem(i, stack);
						maxDamageItems--;
						if (maxDamageItems <= 0) {
							break;
						}
					}
				}
			}
			RuneInventoryRegistry.setInventory(player, inv);
			return j;
		}

		@SubscribeEvent
		public static void runeInvChange(RuneInventoryChangeEvent event) {
			int orangeRunesOld = 0;
			int orangeRunesNew = 0;
			int invSize = RuneInventoryRegistry.getInventorySize(event.getPlayer().getUUID());
			if (event.getNewInv().hasAnyOf(Sets.newHashSet(RuneItems.RUNE_ORANGE.get()))
					|| event.getOldInv().hasAnyOf(Sets.newHashSet(RuneItems.RUNE_ORANGE.get()))) {
				for (int i = 0; i < invSize; i++) {
					ItemStack stack = event.getNewInv().getItem(i).copy();
					if (stack.getItem() == RuneItems.RUNE_ORANGE.get()) {
						orangeRunesNew++;
					}
				}
				for (int i = 0; i < invSize; i++) {
					ItemStack stack = event.getOldInv().getItem(i).copy();
					if (stack.getItem() == RuneItems.RUNE_ORANGE.get()) {
						orangeRunesOld++;
					}
				}
				event.getPlayer().getAttribute(Attributes.MOVEMENT_SPEED)
						.addPermanentModifier(new AttributeModifier("Orange Rune Addition Speed",
								(double) 0.1F * (orangeRunesNew - orangeRunesOld),
								AttributeModifier.Operation.ADDITION));
			}
		}

		private static int timer = 0;

		@SubscribeEvent
		public static void orangeRuneDamage(PlayerTickEvent event) {
			if (event.phase == Phase.START) {
				if (timer++ % 100 == 0) {
					PlayerEntity player = event.player;
					Inventory inv = RuneInventoryRegistry.getInventory(player.getUUID());
					if (inv.hasAnyOf(Sets.newHashSet(RuneItems.RUNE_ORANGE.get()))) {
						damageItems(inv, player, RuneItems.RUNE_ORANGE.get(), 3, 1.0F);
					}
				}
			}
		}
		/**********************************************************************************************************
		 * Apply Rune effects END
		 *******************************************************************************************************/
	}
}