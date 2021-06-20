package com.silverminer.moreore.util.items;

import java.util.function.Supplier;

import com.silverminer.moreore.init.items.FootItems;
import com.silverminer.moreore.init.items.OreItems;
import com.silverminer.moreore.init.items.RuneItems;
import com.silverminer.moreore.init.items.ToolItems;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroups extends ItemGroup {

	private final Supplier<ItemStack> iconSupplier;
	public static final ItemGroup TOOLS = new ModItemGroups("tools",
			() -> new ItemStack(ToolItems.EMERALD_PICKAXE.get()));
	public static final ItemGroup MINERALIC = new ModItemGroups("mineralic",
			() -> new ItemStack(OreItems.SILVER_ORE.get()));

	public static final ItemGroup BIOLOGIC = new ModItemGroups("biologic", () -> new ItemStack(FootItems.BANANA.get()));

	public static final ItemGroup RUNES = new ModItemGroups("runes", () -> new ItemStack(RuneItems.RUNE_BASE.get()));

	public ModItemGroups(final String name, final Supplier<ItemStack> iconSupplier) {
		super(name);
		this.iconSupplier = iconSupplier;
	}

	@Override
	public ItemStack makeIcon() {
		return iconSupplier.get();
	}

}
