package com.silverminer.moreore.init.items;

import com.silverminer.moreore.util.enums.ModArmorMaterial;
import com.silverminer.moreore.util.items.ModItemGroups;
import com.silverminer.moreore.MoreOre;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ArmorItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MoreOre.MODID);
	public static final RegistryObject<Item> EMERALD_HELMET = ITEMS.register("emerald_helmet", 
			() ->  new ArmorItem(ModArmorMaterial.EMERALD, EquipmentSlotType.HEAD, new Item.Properties()
					.tab(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> EMERALD_CHESTPLATE = ITEMS.register("emerald_chestplate", 
			() ->  new ArmorItem(ModArmorMaterial.EMERALD, EquipmentSlotType.CHEST, new Item.Properties()
					.tab(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> EMERALD_LEGGINS = ITEMS.register("emerald_leggins", 
			() ->  new ArmorItem(ModArmorMaterial.EMERALD, EquipmentSlotType.LEGS, new Item.Properties()
					.tab(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> EMERALD_BOOTS = ITEMS.register("emerald_boots", 
			() ->  new ArmorItem(ModArmorMaterial.EMERALD, EquipmentSlotType.FEET, new Item.Properties()
					.tab(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> RAINBOW_HELMET = ITEMS.register("rainbow_helmet", 
			() ->  new ArmorItem(ModArmorMaterial.RAINBOW, EquipmentSlotType.HEAD, new Item.Properties()
					.tab(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> RAINBOW_CHESTPLATE = ITEMS.register("rainbow_chestplate", 
			() ->  new ArmorItem(ModArmorMaterial.RAINBOW, EquipmentSlotType.CHEST, new Item.Properties()
					.tab(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> RAINBOW_LEGGINS = ITEMS.register("rainbow_leggins", 
			() ->  new ArmorItem(ModArmorMaterial.RAINBOW, EquipmentSlotType.LEGS, new Item.Properties()
					.tab(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> RAINBOW_BOOTS = ITEMS.register("rainbow_boots", 
			() ->  new ArmorItem(ModArmorMaterial.RAINBOW, EquipmentSlotType.FEET, new Item.Properties()
					.tab(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> SAPHIR_HELMET = ITEMS.register("saphir_helmet", 
			() ->  new ArmorItem(ModArmorMaterial.SAPHIR, EquipmentSlotType.HEAD, new Item.Properties()
					.tab(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> SAPHIR_CHESTPLATE = ITEMS.register("saphir_chestplate", 
			() ->  new ArmorItem(ModArmorMaterial.SAPHIR, EquipmentSlotType.CHEST, new Item.Properties()
					.tab(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> SAPHIR_LEGGINS = ITEMS.register("saphir_leggins", 
			() ->  new ArmorItem(ModArmorMaterial.SAPHIR, EquipmentSlotType.LEGS, new Item.Properties()
					.tab(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> SAPHIR_BOOTS = ITEMS.register("saphir_boots", 
			() ->  new ArmorItem(ModArmorMaterial.SAPHIR, EquipmentSlotType.FEET, new Item.Properties()
					.tab(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> RUBIN_HELMET = ITEMS.register("rubin_helmet", 
			() ->  new ArmorItem(ModArmorMaterial.RUBIN, EquipmentSlotType.HEAD, new Item.Properties()
					.tab(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> RUBIN_CHESTPLATE = ITEMS.register("rubin_chestplate", 
			() ->  new ArmorItem(ModArmorMaterial.RUBIN, EquipmentSlotType.CHEST, new Item.Properties()
					.tab(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> RUBIN_LEGGINS = ITEMS.register("rubin_leggins", 
			() ->  new ArmorItem(ModArmorMaterial.RUBIN, EquipmentSlotType.LEGS, new Item.Properties()
					.tab(ModItemGroups.TOOLS)));

	public static final RegistryObject<Item> RUBIN_BOOTS = ITEMS.register("rubin_boots", 
			() ->  new ArmorItem(ModArmorMaterial.RUBIN, EquipmentSlotType.FEET, new Item.Properties()
					.tab(ModItemGroups.TOOLS)));
}
