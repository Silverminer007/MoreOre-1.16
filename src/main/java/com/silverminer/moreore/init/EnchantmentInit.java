package com.silverminer.moreore.init;

import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.common.enchantments.GetHomeEnchantment;
import com.silverminer.moreore.init.items.ToolItems;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantmentInit {
	public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister
			.create(ForgeRegistries.ENCHANTMENTS, MoreOre.MODID);

	public static final RegistryObject<Enchantment> GET_HOME = ENCHANTMENTS.register("get_home",
			() -> new GetHomeEnchantment(Enchantment.Rarity.RARE,
					EnchantmentType.create("transportation", item -> item.equals(ToolItems.EMERALD_SWORD.get())),
					EquipmentSlotType.MAINHAND));
}