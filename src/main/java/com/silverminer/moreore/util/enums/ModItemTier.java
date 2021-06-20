package com.silverminer.moreore.util.enums;

import java.util.function.Supplier;

import com.silverminer.moreore.init.items.OreItems;
import com.silverminer.moreore.init.items.RuneItems;

import net.minecraft.item.IItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;

public enum ModItemTier implements IItemTier {

	EMERALD(3, 1561, 7.0F, 2.5F, 14, () -> {
		return Ingredient.of(Items.EMERALD);
	}), SAPHIR(3, 500, 7.0F, 2.5F, 14, () -> {
		return Ingredient.of(OreItems.SAPHIR.get());
	}), RUBIN(3, 500, 7.0F, 2.5F, 14, () -> {
		return Ingredient.of(OreItems.RUBIN.get());
	}), RAINBOW(4, 2378, 8.0F, 3.5F, 10, () -> {
		return Ingredient.of(RuneItems.RAINBOW_RUNE.get());
	});

	private final int harvestLevel;
	private final int maxUses;
	private final float efficiency;
	private final float attackDamage;
	private final int enchantability;
	private final LazyValue<Ingredient> repairMaterial;

	private ModItemTier(int harvestLevel, int maxUses, float efficiency, float attackDamage, int enchantability,
			Supplier<Ingredient> repairMaterial) {
		this.harvestLevel = harvestLevel;
		this.maxUses = maxUses;
		this.efficiency = efficiency;
		this.attackDamage = attackDamage;
		this.enchantability = enchantability;
		this.repairMaterial = new LazyValue<>(repairMaterial);
	}

	@Override
	public int getUses() {
		return this.maxUses;
	}

	@Override
	public float getSpeed() {
		return this.efficiency;
	}

	@Override
	public float getAttackDamageBonus() {
		return this.attackDamage;
	}

	@Override
	public int getLevel() {
		return this.harvestLevel;
	}

	@Override
	public int getEnchantmentValue() {
		return this.enchantability;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return this.repairMaterial.get();
	}
}
