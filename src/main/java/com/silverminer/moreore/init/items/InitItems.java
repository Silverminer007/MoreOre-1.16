package com.silverminer.moreore.init.items;

import com.silverminer.moreore.util.items.ComposterItems;
import com.silverminer.moreore.util.items.ModItemGroups;

import java.util.function.Supplier;

import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.common.objects.items.FuelItem;
import com.silverminer.moreore.common.objects.items.ModSpawnEggItem;
import com.silverminer.moreore.init.ModEntityTypesInit;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MoreOre.MODID);

	public static final RegistryObject<Item> FUEL = ITEMS.register("fuel",
			() -> new FuelItem(new Item.Properties().tab(ModItemGroups.MINERALIC)));

	public static final RegistryObject<Item> VILLAGE_GUARDIAN_SPAWNEGG = ITEMS.register("village_guardian_spawnegg",
			() -> new ModSpawnEggItem(() -> ModEntityTypesInit.VILLAGE_GUARDIAN.get(), 0xbb7907, 0xcdcdcd,
					new Item.Properties().tab(ModItemGroups.BIOLOGIC)));

	public static final RegistryObject<Item> SQUIRREL_SPAWNEGG = ITEMS.register("squirrel_spawnegg",
			() -> new ModSpawnEggItem(() -> ModEntityTypesInit.SQUIRREL.get(), 0x6f3000, 0x381800,
					new Item.Properties().tab(ModItemGroups.BIOLOGIC)));

	public static final RegistryObject<Item> GIANT_ZOMBIE_KING_SPAWNEGG = ITEMS.register("giant_zombie_king_spawnegg",
			() -> new ModSpawnEggItem(() -> ModEntityTypesInit.GIANT_ZOMBIE_KING.get(), 0x799c65, 0x00afaf,
					new Item.Properties().tab(ModItemGroups.BIOLOGIC)));

	public static <T extends Item> RegistryObject<Item> registerCompostItems(String key, Supplier<T> item,
			float chance) {
		RegistryObject<Item> temp = ITEMS.register(key, item);
		MoreOre.composterItems.add(new ComposterItems(temp, chance));
		return temp;
	}
}