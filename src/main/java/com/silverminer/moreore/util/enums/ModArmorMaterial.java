package com.silverminer.moreore.util.enums;

import java.util.function.Supplier;

import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.init.items.OreItems;
import com.silverminer.moreore.init.items.RuneItems;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public enum ModArmorMaterial implements IArmorMaterial {
	EMERALD(MoreOre.MODID + ":emerald", 35, new int[] { 3, 6, 8, 3 }, 12, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.5F,
			0.0F, () -> {
				return Ingredient.of(Items.EMERALD);
			}),
	SAPHIR(MoreOre.MODID + ":saphir", 22, new int[] { 2, 6, 6, 3 }, 10, SoundEvents.ARMOR_EQUIP_DIAMOND, 1.7F,
			0.0F, () -> {
				return Ingredient.of(OreItems.SAPHIR.get());
			}),
	RUBIN(MoreOre.MODID + ":rubin", 22, new int[] { 2, 5, 7, 3 }, 10, SoundEvents.ARMOR_EQUIP_GOLD, 1.7F, 0.0F,
			() -> {
				return Ingredient.of(OreItems.RUBIN.get());
			}),
	RAINBOW(MoreOre.MODID + ":rainbow", 39, new int[] { 3, 6, 8, 3 }, 15, SoundEvents.ARMOR_EQUIP_CHAIN, 3.5F,
			0.2F, () -> {
				return Ingredient.of(RuneItems.RAINBOW_RUNE.get());
			});

	private static final int[] MAX_DAMAGE_ARRAY = new int[] { 13, 15, 16, 11 };
	private final String name;
	private final int maxDamageFactor;
	private final int[] damageReductionAmountArray;
	private final int enchantability;
	private final SoundEvent soundEvent;
	private final float toughness;
	private final LazyValue<Ingredient> repairMaterial;
	private final float field_234660_o_;

	private ModArmorMaterial(String nameIn, int maxDamageFactorIn, int[] damageReductionAmountIn, int enchantabilityIn,
			SoundEvent soundEventIn, float toughnessIn, float p_i231593_9_, Supplier<Ingredient> repairMaterialIn) {
		this.name = nameIn;
		this.maxDamageFactor = maxDamageFactorIn;
		this.damageReductionAmountArray = damageReductionAmountIn;
		this.enchantability = enchantabilityIn;
		this.soundEvent = soundEventIn;
		this.toughness = toughnessIn;
		this.field_234660_o_ = p_i231593_9_;
		this.repairMaterial = new LazyValue<>(repairMaterialIn);
	}

	@Override
	public int getDurabilityForSlot(EquipmentSlotType slotIn) {
		return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor;
	}

	@Override
	public int getDefenseForSlot(EquipmentSlotType slotIn) {
		return this.damageReductionAmountArray[slotIn.getIndex()];
	}

	@Override
	public int getEnchantmentValue() {
		return this.enchantability;
	}

	@Override
	public SoundEvent getEquipSound() {
		return this.soundEvent;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return this.repairMaterial.get();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public float getToughness() {
		return this.toughness;
	}

	@Override
	public float getKnockbackResistance() {
		return this.field_234660_o_;
	}
}
