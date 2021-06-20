package com.silverminer.moreore.util.network;

import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.moreore.util.runes.RuneInventoryRegistry;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class InventoryChangePacket {
	protected static final Logger LOGGER = LogManager.getLogger(InventoryChangePacket.class);

	private final CompoundNBT nbt;

	public InventoryChangePacket(CompoundNBT nbt) {
		this.nbt = nbt;
	}

	public static void encode(InventoryChangePacket pkt, PacketBuffer buf) {
		buf.writeNbt(pkt.nbt);
	}

	public static InventoryChangePacket decode(PacketBuffer buf) {
		return new InventoryChangePacket(buf.readNbt());
	}

	public static void handle(InventoryChangePacket pkt, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> Handle.handleServer(pkt.nbt)));
		context.setPacketHandled(true);
	}

	public static class Handle {

		public static DistExecutor.SafeRunnable handleServer(CompoundNBT nbt) {
			return new DistExecutor.SafeRunnable() {

				private static final long serialVersionUID = 1L;

				@Override
				public void run() {
					RuneInventoryRegistry.readFromNBT(nbt);
				}
			};
		}
	}
}