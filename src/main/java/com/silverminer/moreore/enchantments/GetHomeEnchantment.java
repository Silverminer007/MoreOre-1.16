package com.silverminer.moreore.enchantments;

import com.silverminer.moreore.common.portal.Utils;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.Direction;

public class GetHomeEnchantment extends Enchantment {

	public GetHomeEnchantment(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType... slots) {
		super(rarityIn, typeIn, slots);
	}

	public int getMinEnchantability(int enchantmentLevel) {
		return 15 + (enchantmentLevel - 1) * 9;
	}

	public int getMaxEnchantability(int enchantmentLevel) {
		return super.getMinEnchantability(enchantmentLevel) + 50;
	}

	public int getMaxLevel() {
		return 1;
	}

	public boolean canApplyTogether(Enchantment ench) {
		return super.canApplyTogether(ench);
	}

	public void onEntityDamaged(LivingEntity user, Entity target, int level) {
		if (user instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) user;
			if (!player.getEntityWorld().isRemote()) {
				Utils.teleportTo(player, player.getEntityWorld().func_234923_W_(),
						((ServerPlayerEntity) player).func_241140_K_(), Direction.NORTH);
			}
		}
	}
}