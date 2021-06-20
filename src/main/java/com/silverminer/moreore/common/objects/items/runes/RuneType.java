package com.silverminer.moreore.common.objects.items.runes;

import javax.annotation.Nullable;

import com.silverminer.moreore.MoreOre;

public enum RuneType implements IRuneType {
	RAINBOW(100, "rainbow"), GREEN(100, "green"), ORANGE(100, "orange"), BLUE(100, "blue"), RED(100, "red"),
	YELLOW(250, "yellow"), GRAY(5, "gray"), BROWN(100, "brown"), PURPLE(100, "purple");

	private int maxUses;
	private final String descriptionKey;

	RuneType(int maxUsesIn, String nameIn) {
		this(maxUsesIn, nameIn, MoreOre.MODID);
	}

	RuneType(int maxUsesIn, String nameIn, String modid) {
		this.maxUses = maxUsesIn;
		this.descriptionKey = "runes."  + modid + "." + nameIn;
	}

	@Override
	public int getMaxUses() {
		return this.maxUses;
	}

	@Override
	public String getDescriptionKey() {
		return this.descriptionKey;
	}

	@Override
	public String getSerializedName() {
		return this.getDescriptionKey();
	}

	@Nullable
	public RuneType fromString(String key) {
		return this.fromStringOrDefault(key, null);
	}

	public RuneType fromStringOrDefault(String key, RuneType typeD) {
		for(RuneType type : RuneType.values()) {
			if(type.getSerializedName().equals(key)) {
				return type;
			}
		}
		return typeD;
	}
}