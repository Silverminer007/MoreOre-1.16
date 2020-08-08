package com.silverminer.moreore.util.items;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

public class ComposterItems {
	private float chance;
	private RegistryObject<Item> item;

	public ComposterItems(RegistryObject<Item> item, float chance) {
		this.chance = chance;
		this.item = item;
	}

	public float getChance() {
		return this.chance;
	}

	public RegistryObject<Item> getItem() {
		return this.item;
	}
}