package com.silverminer.moreore.util.runes;

import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Maps;
import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.util.events.RuneInventoryChangeEvent;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.MinecraftForge;

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
			MoreOre.runeSaveData.setDirty();
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
			RUNE_INVENTORY.get(uuid).setItem(slot, stack);
			markDirty();
			return true;
		}
		return false;
	}

	public static void removeStackFromSlot(UUID uuid, int slot) {
		RUNE_INVENTORY.get(uuid).removeItemNoUpdate(slot);
		markDirty();
	}

	public static ItemStack getStackFromSlot(UUID uuid, int slot) {
		return RUNE_INVENTORY.get(uuid).getItem(slot);
	}

	public static Inventory getInventory(UUID uuid) {
		addPlayer(uuid);
		return RUNE_INVENTORY.get(uuid);
	}

	public static Inventory setInventory(PlayerEntity player, Inventory inv) {
		MinecraftForge.EVENT_BUS.post(new RuneInventoryChangeEvent(player, inv, getInventory(player.getUUID())));
		return setInventory(player.getUUID(), inv);
	}

	private static Inventory setInventory(UUID uuid, Inventory inv) {
		addPlayer(uuid);
		markDirty();
		return RUNE_INVENTORY.replace(uuid, inv);
	}

	public static int getInventorySize(UUID uuid) {
		addPlayer(uuid);
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

	public static int expandInventorySize(UUID uuid, int amount) {
		int oldSize = getInventorySize(uuid);
		int size = oldSize + amount;
		if (!isSizeValid(size)) {
			size = size < 0 ? 0 : size > 3 ? 3 : size;
		}
		setInventorySize(uuid, size);
		return size;
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
		nbt.putUUID("uuid", uuid);
		nbt.put("inventory", RUNE_INVENTORY.get(uuid).createTag());
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
		UUID uuid = tag.getUUID("uuid");
		addPlayer(uuid);
		RUNE_INVENTORY.get(uuid).fromTag(tag.getList("inventory", 10));
		RUNE_INVENTORY_SIZE.put(uuid, tag.getInt("invSize"));
	}
}