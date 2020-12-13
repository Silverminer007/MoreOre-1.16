package com.silverminer.moreore.util.events;

import com.silverminer.moreore.client.gui.container.RunetableContainer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
/**
 * On Player (or Game) is changing RuneInventory
 *
 * The event is fired when player closes {@link RunetableContainer} and Inventory is updated and when rune inside it breaks
 * The event is fired on the {@link MinecraftForge#EVENT_BUS}
 */
public class RuneInventoryChangeEvent extends Event {
	private final PlayerEntity player;
	private final Inventory newInv;
	private final Inventory oldInv;

	public RuneInventoryChangeEvent(PlayerEntity player, Inventory newInv, Inventory oldInv) {
		super();
		this.player = player;
		this.newInv = newInv;
		this.oldInv = oldInv;
	}

	public Inventory getOldInv() {
		return oldInv;
	}

	public Inventory getNewInv() {
		return newInv;
	}

	public PlayerEntity getPlayer() {
		return player;
	}
}