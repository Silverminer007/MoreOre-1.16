package com.silverminer.moreore.common.enchantments;

import com.silverminer.moreore.common.portal.Utils;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class GetHomeEnchantment extends Enchantment {

	public GetHomeEnchantment(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType... slots) {
		super(rarityIn, typeIn, slots);
	}

	public int getMinCost(int enchantmentLevel) {
		return 15 + (enchantmentLevel - 1) * 9;
	}

	public int getMaxCost(int enchantmentLevel) {
		return super.getMinCost(enchantmentLevel) + 50;
	}

	public int getMaxLevel() {
		return 1;
	}

	public boolean checkCompatibility(Enchantment ench) {
		return super.checkCompatibility(ench);
	}

	public void onEntityDamaged(LivingEntity user, Entity target, int level) {
		if (user instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) user;
			Utils.teleportTo(player, player.getLevel().dimension(), player.getRespawnPosition(), null);
		}
	}
}