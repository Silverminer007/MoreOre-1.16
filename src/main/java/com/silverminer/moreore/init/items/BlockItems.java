package com.silverminer.moreore.init.items;

import com.silverminer.moreore.init.blocks.InitBlocks;
import com.silverminer.moreore.init.blocks.BiologicBlocks;
import com.silverminer.moreore.util.items.ModItemGroups;
import com.silverminer.moreore.MoreOre;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MoreOre.MODID);

	public static final RegistryObject<Item> LAVA_SPONGE = ITEMS.register("lava_sponge",
			() -> new BlockItem(InitBlocks.LAVA_SPONGE.get(), new Item.Properties().tab(ModItemGroups.MINERALIC)));

	public static final RegistryObject<Item> BURNED_OUT_LAVA_SPONGE = ITEMS.register("burned_out_lava_sponge",
			() -> new BlockItem(InitBlocks.BURNED_OUT_LAVA_SPONGE.get(),
					new Item.Properties().tab(ModItemGroups.MINERALIC)));

	public static final RegistryObject<Item> SILVER_PORTAL_FRAME = ITEMS.register("silver_portal_frame",
			() -> new BlockItem(InitBlocks.SILVER_PORTAL_FRAME.get(),
					new Item.Properties().tab(ModItemGroups.MINERALIC)));

	public static final RegistryObject<Item> SILVER_DOOR = ITEMS.register("silver_door",
			() -> new BlockItem(BiologicBlocks.SILVER_DOOR.get(), new Item.Properties().tab(ModItemGroups.BIOLOGIC)));

	public static final RegistryObject<Item> SILVER_STAIRS = ITEMS.register("silver_stairs",
			() -> new BlockItem(BiologicBlocks.SILVER_STAIRS.get(),
					new Item.Properties().tab(ModItemGroups.BIOLOGIC)));

	public static final RegistryObject<Item> SILVER_FENCE = ITEMS.register("silver_fence",
			() -> new BlockItem(BiologicBlocks.SILVER_FENCE.get(),
					new Item.Properties().tab(ModItemGroups.BIOLOGIC)));

	public static final RegistryObject<Item> SILVER_FENCE_GATE = ITEMS.register("silver_fence_gate",
			() -> new BlockItem(BiologicBlocks.SILVER_FENCE_GATE.get(),
					new Item.Properties().tab(ModItemGroups.BIOLOGIC)));

	public static final RegistryObject<Item> SILVER_SLAB = ITEMS.register("silver_slab",
			() -> new BlockItem(BiologicBlocks.SILVER_SLAB.get(), new Item.Properties().tab(ModItemGroups.BIOLOGIC)));

	public static final RegistryObject<Item> GLOW_BLUE_BLOCK = ITEMS.register("glow_blue_block",
			() -> new BlockItem(InitBlocks.GLOW_BLUE_BLOCK.get(), new Item.Properties().tab(ModItemGroups.BIOLOGIC)));

	public static final RegistryObject<Item> GLASS_DOOR = ITEMS.register("glass_door",
			() -> new BlockItem(BiologicBlocks.GLASS_DOOR.get(), new Item.Properties().tab(ModItemGroups.BIOLOGIC)));

	public static final RegistryObject<Item> BEAMER = ITEMS.register("beamer",
			() -> new BlockItem(InitBlocks.BEAMER.get(), new Item.Properties().tab(ModItemGroups.MINERALIC)));

	public static final RegistryObject<Item> PATROLPOINT = ITEMS.register("patrolpoint",
			() -> new BlockItem(InitBlocks.PATROLPOINT.get(), new Item.Properties().tab(ModItemGroups.MINERALIC)));
}
