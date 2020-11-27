package com.silverminer.moreore.common.inventory;

import net.minecraft.inventory.Inventory;

public class RuneInventory extends Inventory {
	private int activeSlots = 1;

	public RuneInventory() {
		super(3);
	}

	public int getActiveSlots() {
		return this.activeSlots;
	}

	public void setActiveSlots(int count) {
		this.activeSlots = (count < 0 ? 0 : (count > 3 ? 3 : count));
	}
}