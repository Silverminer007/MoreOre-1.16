package com.silverminer.moreore.common.objects.items;

import com.silverminer.moreore.common.objects.items.runes.IRuneType;

import net.minecraft.item.Item;

public class RuneItem extends Item {

	public RuneItem(IRuneType type, Properties properties) {
		super(properties.defaultMaxDamage(type.getMaxUses()));
	}

}