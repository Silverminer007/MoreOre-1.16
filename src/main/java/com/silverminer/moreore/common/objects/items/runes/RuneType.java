package com.silverminer.moreore.common.objects.items.runes;

public enum RuneType implements IRuneType {
	RAINBOW(100, ""), GREEN(100, ""), ORANGE(100, ""), BLUE(100, ""), RED(100, ""), YELLOW(250, ""), GRAY(5, ""),
	BROWN(100, ""), PURPLE(100, "");

	private int maxUses;
	private String description;

	RuneType(int maxUsesIn, String descriptionIn) {
		this.maxUses = maxUsesIn;
		this.description = descriptionIn;
	}

	@Override
	public int getMaxUses() {
		return this.maxUses;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

}