package com.silverminer.moreore.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.moreore.util.network.InventoryChangePacket;
import com.silverminer.moreore.util.network.MoreorePacketHandler;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

/**
 * Responsible for saving/loading {@link PortalRegistry} data.
 */
public class RuneSaveData extends WorldSavedData {

	protected static final Logger LOGGER = LogManager.getLogger(RuneSaveData.class);
	private static final String DATA_NAME = "runes";

	public RuneSaveData() {
		super(DATA_NAME);
	}

	@Override
	public void read(CompoundNBT nbt) {
		RuneInventoryRegistry.readFromNBT(nbt);
		MoreorePacketHandler.sendToAll(new InventoryChangePacket(nbt));
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt) {
		RuneInventoryRegistry.writeToNBT(nbt);
		return nbt;
	}

	public static RuneSaveData get(ServerWorld world) {
		if (world == null)
			return null;
		LOGGER.debug("RuneSavedData read");
		DimensionSavedDataManager storage = world.getSavedData();

		return storage.getOrCreate(RuneSaveData::new, DATA_NAME);
	}
}