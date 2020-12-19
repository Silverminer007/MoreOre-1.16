package com.silverminer.moreore.common.objects.items.runes;

import net.minecraft.util.IStringSerializable;

public interface IRuneType extends IStringSerializable{
	int getMaxUses();
	String getDescriptionKey();
}