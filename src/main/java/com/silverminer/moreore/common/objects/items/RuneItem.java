package com.silverminer.moreore.common.objects.items;

import com.silverminer.moreore.common.objects.items.runes.IRuneType;

import net.minecraft.item.Item;

public class RuneItem extends Item {

	public RuneItem(IRuneType type, Properties properties) {
		this(type, properties, 100);
	}

	public RuneItem(IRuneType type, Properties properties, int maxDamage) {
		super(properties.defaultMaxDamage(maxDamage));
	}

}