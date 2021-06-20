package com.silverminer.moreore.common.objects.items;

import java.util.List;

import com.silverminer.moreore.common.objects.items.runes.IRuneType;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class RuneItem extends Item {
	private final IRuneType type;

	public RuneItem(IRuneType type, Properties properties) {
		super(properties.durability(type.getMaxUses()));
		this.type = type;
	}

	public IRuneType getRuneType() {
		return type;
	}

	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (flagIn.isAdvanced()) {
			tooltip.add(new TranslationTextComponent(this.getRuneType().getDescriptionKey()));
		}
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}
}