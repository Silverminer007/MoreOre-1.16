package com.silverminer.moreore.common.portal;

import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.common.portal.registration.PortalRegistry;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

/**
 * Responsible for saving/loading {@link PortalRegistry} data.
 */
public class PortalWorldSaveData extends WorldSavedData
{
	private static final String DATA_NAME = MoreOre.MODID;
	
	public PortalWorldSaveData()
	{
		super(DATA_NAME);
	}
	
	@Override
	public void read(CompoundNBT nbt)
	{
		PortalRegistry.readFromNBT(nbt);
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt)
	{
		PortalRegistry.writeToNBT(nbt);
		return nbt;
	}

	public static PortalWorldSaveData get(ServerWorld world){
		if (world == null) return null;
		MoreOre.LOGGER.debug("PortalWorldSavedData read");
		DimensionSavedDataManager storage = world.getSavedData();

		return storage.getOrCreate(PortalWorldSaveData::new, DATA_NAME);
	}
}