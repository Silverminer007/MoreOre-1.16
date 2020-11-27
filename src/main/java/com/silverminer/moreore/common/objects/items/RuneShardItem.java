package com.silverminer.moreore.common.objects.items;

import java.util.function.Supplier;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

public class RuneShardItem extends Item {
	private final Supplier<RegistryObject<Item>> runeResult;
	public RuneShardItem(Properties properties, Supplier<RegistryObject<Item>> runeResult) {
		super(properties);
		this.runeResult = runeResult;
	}

	public Item getRuneResult() {
		return this.runeResult.get().get();
	}
}