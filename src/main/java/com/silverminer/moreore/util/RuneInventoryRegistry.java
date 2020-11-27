package com.silverminer.moreore.util;

import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Maps;
import com.silverminer.moreore.MoreOre;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class RuneInventoryRegistry {
	protected static final Logger LOGGER = LogManager.getLogger(RuneInventoryRegistry.class);
	private static final Map<UUID, Inventory> RUNE_INVENTORY = Maps.newHashMap();
	private static final Map<UUID, Integer> RUNE_INVENTORY_SIZE = Maps.newHashMap();

	public static void clear() {
		RUNE_INVENTORY.clear();
		RUNE_INVENTORY_SIZE.clear();
		markDirty();
	}

	private static void markDirty() {
		if (MoreOre.runeSaveData != null)
			MoreOre.runeSaveData.markDirty();
	}

	public static boolean addPlayer(UUID uuid) {
		if (RUNE_INVENTORY.containsKey(uuid) && RUNE_INVENTORY_SIZE.containsKey(uuid)) {
			return false;
		} else {
			RUNE_INVENTORY.putIfAbsent(uuid, new Inventory(3));
			RUNE_INVENTORY_SIZE.putIfAbsent(uuid, 1);
			markDirty();
			return true;
		}
	}

	public static boolean setStackToSlot(UUID uuid, ItemStack stack, int slot) {
		addPlayer(uuid);
		if (RUNE_INVENTORY_SIZE.get(uuid) > slot) {
			RUNE_INVENTORY.get(uuid).setInventorySlotContents(slot, stack);
			markDirty();
			return true;
		}
		return false;
	}

	public static void removeStackFromSlot(UUID uuid, int slot) {
		RUNE_INVENTORY.get(uuid).removeStackFromSlot(slot);
		markDirty();
	}

	public static ItemStack getStackFromSlot(UUID uuid, int slot) {
		return RUNE_INVENTORY.get(uuid).getStackInSlot(slot);
	}

	public static Inventory getInventory(UUID uuid) {
		addPlayer(uuid);
		Inventory inv = new Inventory(3);
		for(ItemStack stack : RUNE_INVENTORY.get(uuid).func_233543_f_()) {
			inv.addItem(stack);
		}
		return inv == null ? new Inventory(3) : inv;
	}

	public static Inventory setInventory(UUID uuid, Inventory inv) {
		addPlayer(uuid);
		markDirty();
		return RUNE_INVENTORY.replace(uuid, inv);
	}

	public static int getInventorySize(UUID uuid) {
		return RUNE_INVENTORY_SIZE.get(uuid);
	}

	public static boolean setInventorySize(UUID uuid, int size) {
		if (isSizeValid(size)) {
			addPlayer(uuid);
			RUNE_INVENTORY_SIZE.replace(uuid, size);
			return true;
		}
		return false;
	}

	public static boolean isSizeValid(int size) {
		return size >= 0 && size <= 3;
	}

	public static CompoundNBT writeToNBT() {
		return writeToNBT(new CompoundNBT());
	}

	public static CompoundNBT writeToNBT(CompoundNBT tag) {
		int i = 0;
		for (UUID key : RUNE_INVENTORY.keySet()) {
			tag.put(String.valueOf(i++), writePlayerToNBT(key));
		}
		return tag;
	}

	public static CompoundNBT writePlayerToNBT(UUID uuid) {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putUniqueId("uuid", uuid);
		nbt.put("inventory", RUNE_INVENTORY.get(uuid).write());
		nbt.putInt("invSize", RUNE_INVENTORY_SIZE.get(uuid));
		return nbt;
	}

	public static void readFromNBT(CompoundNBT tag) {
		int i = 0;
		clear();
		while (tag.contains(String.valueOf(i))) {
			CompoundNBT nbt = tag.getCompound(String.valueOf(i));
			readPlayerFromNBT(nbt);
			i++;
		}
	}

	public static void readPlayerFromNBT(CompoundNBT tag) {
		UUID uuid = tag.getUniqueId("uuid");
		addPlayer(uuid);
		RUNE_INVENTORY.get(uuid).read(tag.getList("inventory", 10));
		RUNE_INVENTORY_SIZE.put(uuid, tag.getInt("invSize"));
	}
}