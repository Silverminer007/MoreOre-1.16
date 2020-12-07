package com.silverminer.moreore.init.items;

import com.silverminer.moreore.util.enums.ModItemTier;
import com.silverminer.moreore.util.items.ModItemGroups;
import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.common.objects.items.Golden_Trident;

import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ToolItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MoreOre.MODID);

	public static final RegistryObject<Item> GOLDEN_TRIDENT = ITEMS.register("golden_trident",
			() -> new Golden_Trident(new Item.Properties().group(ModItemGroups.TOOLS).maxStackSize(1)));

	public static final RegistryObject<Item> RAINBOW_SWORD = ITEMS.register("rainbow_sword",
			() -> new SwordItem(ModItemTier.RAINBOW, 3, -2.4F, new Item.Properties().group(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> EMERALD_SWORD = ITEMS.register("emerald_sword",
			() -> new SwordItem(ModItemTier.EMERALD, 3, -2.4F, new Item.Properties().group(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> RUBIN_SWORD = ITEMS.register("rubin_sword",
			() -> new SwordItem(ModItemTier.RUBIN, 3, -2.4F, new Item.Properties().group(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> SAPHIR_SWORD = ITEMS.register("saphir_sword",
			() -> new SwordItem(ModItemTier.SAPHIR, 3, -2.4F, new Item.Properties().group(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> RAINBOW_SHOVEL = ITEMS.register("rainbow_shovel",
			() -> new ShovelItem(ModItemTier.RAINBOW, 1.5F, -3.0F, new Item.Properties().group(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> EMERALD_SHOVEL = ITEMS.register("emerald_shovel",
			() -> new ShovelItem(ModItemTier.EMERALD, 1.5F, -3.0F, new Item.Properties().group(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> RUBIN_SHOVEL = ITEMS.register("rubin_shovel",
			() -> new ShovelItem(ModItemTier.RUBIN, 1.5F, -3.0F, new Item.Properties().group(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> SAPHIR_SHOVEL = ITEMS.register("saphir_shovel",
			() -> new ShovelItem(ModItemTier.SAPHIR, 1.5F, -3.0F, new Item.Properties().group(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> RAINBOW_PICKAXE = ITEMS.register("rainbow_pickaxe",
			() -> new PickaxeItem(ModItemTier.RAINBOW, 1, -2.8F, new Item.Properties().group(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> EMERALD_PICKAXE = ITEMS.register("emerald_pickaxe",
			() -> new PickaxeItem(ModItemTier.EMERALD, 1, -2.8F, new Item.Properties().group(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> RUBIN_PICKAXE = ITEMS.register("rubin_pickaxe",
			() -> new PickaxeItem(ModItemTier.RUBIN, 1, -2.8F, new Item.Properties().group(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> SAPHIR_PICKAXE = ITEMS.register("saphir_pickaxe",
			() -> new PickaxeItem(ModItemTier.SAPHIR, 1, -2.8F, new Item.Properties().group(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> RAINBOW_AXE = ITEMS.register("rainbow_axe",
			() -> new AxeItem(ModItemTier.RAINBOW, 5.5F, -3.5F, new Item.Properties().group(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> EMERALD_AXE = ITEMS.register("emerald_axe",
			() -> new AxeItem(ModItemTier.EMERALD, 4.5F, -3.5F, new Item.Properties().group(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> RUBIN_AXE = ITEMS.register("rubin_axe",
			() -> new AxeItem(ModItemTier.RUBIN, 4.5F, -2.5F, new Item.Properties().group(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> SAPHIR_AXE = ITEMS.register("saphir_axe",
			() -> new AxeItem(ModItemTier.SAPHIR, 4.5F, -2.5F, new Item.Properties().group(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> RAINBOW_HOE = ITEMS.register("rainbow_hoe",
			() -> new HoeItem(ModItemTier.RAINBOW, -4, 0.0F, new Item.Properties().group(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> EMERALD_HOE = ITEMS.register("emerald_hoe",
			() -> new HoeItem(ModItemTier.EMERALD, -3, 0.0F, new Item.Properties().group(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> RUBIN_HOE = ITEMS.register("rubin_hoe",
			() -> new HoeItem(ModItemTier.RUBIN, -1, 0.0F, new Item.Properties().group(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> SAPHIR_HOE = ITEMS.register("saphir_hoe",
			() -> new HoeItem(ModItemTier.SAPHIR, -1, 0.0F, new Item.Properties().group(ModItemGroups.TOOLS)));
}
