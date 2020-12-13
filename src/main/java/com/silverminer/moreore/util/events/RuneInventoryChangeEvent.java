package com.silverminer.moreore.util.events;

import java.util.UUID;
import com.silverminer.moreore.client.gui.container.RunetableContainer;

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
	private final UUID player;
	private final Inventory newInv;
	private final Inventory oldInv;

	public RuneInventoryChangeEvent(UUID player, Inventory newInv, Inventory oldInv) {
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

	public UUID getPlayer() {
		return player;
	}
}