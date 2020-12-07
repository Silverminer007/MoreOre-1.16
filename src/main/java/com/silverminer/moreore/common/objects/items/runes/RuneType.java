package com.silverminer.moreore.common.objects.items.runes;

public enum RuneType implements IRuneType {
	RAINBOW(100, ""), GREEN(100, "");
	private int maxUses;
	private String description;

	RuneType(int maxUsesIn, String descriptionIn){
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