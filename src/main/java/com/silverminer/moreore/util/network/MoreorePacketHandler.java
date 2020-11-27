package com.silverminer.moreore.util.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.moreore.MoreOre;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class MoreorePacketHandler {

	protected static final Logger LOGGER = LogManager.getLogger(MoreorePacketHandler.class);

	/**
	 * Diese Zahl kann hochezählt werden, wenn sich etwas an den Packeten oder
	 * ähnliches ändert, damit Server und Client die gleichen Packete bzw. Versionen
	 * haben
	 */
	public static final String PROTOCOL_VERSION = "1.0";

	/**
	 * Das ist die Instanz über die Server und Client Kommunizieren.
	 */
	public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
			.named(new ResourceLocation(MoreOre.MODID, "main_channel"))
			.clientAcceptedVersions(PROTOCOL_VERSION::equals).serverAcceptedVersions(PROTOCOL_VERSION::equals)
			.networkProtocolVersion(() -> PROTOCOL_VERSION).simpleChannel();

	/**
	 * Hier müssen alle Packete registiert werden die irgendwann gesewndet werden
	 * sollen, damit Minecraft weiß wie es das Packet hand haben soll. id++ wird
	 * benutz um wirklich einmalige ids zu haben
	 */
	public static void register() {
		int id = 0;
		CHANNEL.registerMessage(id++, InventoryChangePacket.class, InventoryChangePacket::encode, InventoryChangePacket::decode,
				InventoryChangePacket::handle);
	}

	/**
	 * Diese Funktion sendet das registriete übergebene Packet an den übergebenen
	 * Spieler. Das Packet muss vorher in der {@link #register()} Funktion
	 * registiert werden
	 * 
	 * @param message
	 * @param player
	 */
	public static void sendTo(Object message, PlayerEntity player) {
		CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), message);
	}

	/**
	 * Diese Funktion sendet das registriete übergebene Packet an alle Klienten. Das
	 * Packet muss vorher in der {@link #register()} Funktion registiert werden
	 * 
	 * @param message
	 */
	public static void sendToAll(Object message) {
		CHANNEL.send(PacketDistributor.ALL.noArg(), message);
	}

	/**
	 * Diese Funktion sendet das übergebene Packet an vom Klienten zum Server. Das
	 * Packet muss vorher in der {@link #register()} Funktion registiert werden
	 * 
	 * @param message
	 */
	public static void sendToServer(Object message) {
		CHANNEL.sendToServer(message);
	}
}