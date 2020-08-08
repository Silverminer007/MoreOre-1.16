package com.silverminer.moreore.util.enums;

import java.util.function.Supplier;
import com.silverminer.moreore.init.items.OreItems;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.util.SoundEvents;

public enum ModArmorMaterial implements IArmorMaterial {
	EMERALD(com.silverminer.moreore.MoreOre.MODID + ":emerald", 5, new int[] { 3, 6, 7, 3 }, 420,
			SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 6.9F, 0.0F, () -> {
				return Ingredient.fromItems(Items.EMERALD);
			}),
	SAPHIR(com.silverminer.moreore.MoreOre.MODID + ":saphir", 5, new int[] { 2, 6, 5, 3 }, 420,
			SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 6.9F, 0.0F, () -> {
				return Ingredient.fromItems(OreItems.SAPHIR.get());
			}),
	RUBIN(com.silverminer.moreore.MoreOre.MODID + ":rubin", 5, new int[] { 3, 5, 6, 2 }, 420,
			SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 6.9F, 0.0F, () -> {
				return Ingredient.fromItems(OreItems.RUBIN.get());
			}),
	RAINBOW(com.silverminer.moreore.MoreOre.MODID + ":rainbow", 5, new int[] { 4, 7, 7, 4 }, 420,
			SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 6.9F, 0.0F, () -> {
				return Ingredient.fromItems(OreItems.RAINBOW_RUNE.get());
			});

	private static final int[] MAX_DAMAGE_ARRAY = new int[] { 16, 16, 16, 16 };
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
	public int getDurability(EquipmentSlotType slotIn) {
		return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor;
	}

	@Override
	public int getDamageReductionAmount(EquipmentSlotType slotIn) {
		return this.damageReductionAmountArray[slotIn.getIndex()];
	}

	@Override
	public int getEnchantability() {
		return this.enchantability;
	}

	@Override
	public SoundEvent getSoundEvent() {
		return this.soundEvent;
	}

	@Override
	public Ingredient getRepairMaterial() {
		return this.repairMaterial.getValue();
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
	public float func_230304_f_() {
		return this.field_234660_o_;
	}
}
