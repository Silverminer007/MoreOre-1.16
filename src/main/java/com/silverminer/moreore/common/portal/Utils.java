package com.silverminer.moreore.common.portal;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SPlayEntityEffectPacket;
import net.minecraft.network.play.server.SPlaySoundEventPacket;
import net.minecraft.network.play.server.SPlayerAbilitiesPacket;
import net.minecraft.network.play.server.SServerDifficultyPacket;
import net.minecraft.network.play.server.SSetExperiencePacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.IWorldInfo;

public final class Utils {
	private static final LanguageMap I18N = LanguageMap.getInstance();

	/**
	 * Gets the localized formatted string for the specified key.
	 *
	 * @param key        The key for the localized string.
	 * @param parameters Formatting arguments.
	 * @return The localized formatted string.
	 */
	public static String translate(String key, Object... parameters) {
		return String.format(I18N.func_230503_a_(key), parameters);// Test
	}

	/**
	 * Gets the localized formatted strings for the specified key and formatting
	 * arguments.
	 *
	 * @param key  The base key without an index (e.g. "myKey" gets "myKey0",
	 *             "myKey1" ... etc.).
	 * @param args Formatting arguments.
	 * @return A list of localized strings for the specified key, or an empty list
	 *         if the key was not found.
	 */
	public static ArrayList<StringTextComponent> multiLineTranslate(String key, Object... args) {
		ArrayList<StringTextComponent> lines = new ArrayList<>();

		if (key != null) {
			int x = 0;
			String currentKey = key + x;

			while (I18N.func_230506_b_(currentKey))// Test
			{
				lines.add(new StringTextComponent(String.format(I18N.func_230503_a_(currentKey), args)));// Test
				currentKey = key + ++x;
			}
		}

		return lines;
	}

	/**
	 * Gets the coordinate component of a BlockPos for the specified axis.
	 *
	 * @param pos  The coordinate to choose the component from.
	 * @param axis The axis representing the coordinate component to choose.
	 * @return <code>0</code> if either pos or axis are <code>null</code>, otherwise
	 *         the chosen coordinate component.
	 */
	public static int getAxisValue(BlockPos pos, Axis axis) {
		if (pos == null || axis == null)
			return 0;

		if (axis == Axis.X)
			return pos.getX();
		if (axis == Axis.Y)
			return pos.getY();
		if (axis == Axis.Z)
			return pos.getZ();

		return 0;
	}

	/**
	 * Gets the relative direction from one {@link BlockPos} to another.
	 *
	 * @param from The starting point.
	 * @param to   The end point.
	 * @return One of the {@link Direction} values or <code>null</code> if one of
	 *         the arguments was <code>null</code>.
	 */
	public static Direction getRelativeDirection(BlockPos from, BlockPos to) {
		if (from == null || to == null)
			return null;

		BlockPos directionVec = to.subtract(from);

		return Direction.getFacingFromVector(directionVec.getX(), directionVec.getY(), directionVec.getZ());
	}

	/**
	 * Gets the axis that is orthogonal to, and on the same plane as the specified
	 * one.
	 *
	 * @param axis The starting axis.
	 * @return One of the {@link Axis} values or <code>null</code> if the specified
	 *         axis was <code>null</code> or there is no other axis on the same
	 *         plane.
	 */
	public static Axis getOrthogonalTo(Axis axis) {
		if (axis == null || axis == Axis.Y)
			return null;

		return (axis == Axis.X) ? Axis.Z : Axis.X;
	}

	/**
	 * Teleport an entity to the specified position in the specified dimensionId
	 * facing the specified direction.
	 *
	 * @param entity      The entity to teleport. Can be any entity (item, mob,
	 *                    player).
	 * @param dimension   The dimension to port to.
	 * @param destination The position to port to.
	 * @param facing      The direction the entity should face after porting.
	 */
	public static void teleportTo(Entity entity, RegistryKey<World> dimension, BlockPos destination, Direction facing) {
		if (entity == null || dimension == null || destination == null || entity.isBeingRidden()
				|| entity.isOnePlayerRiding() || !entity.isNonBoss())
			return;

		ServerPlayerEntity player = (entity instanceof ServerPlayerEntity) ? (ServerPlayerEntity) entity : null;
		boolean interdimensional = (entity.world.func_234923_W_() != dimension);
		entity.setMotion(Vector3d.ZERO);

		if (player != null) {
			if (interdimensional) {
				teleportPlayerToDimension(player, dimension, destination, getYaw(facing), 0.0f);
			} else {
				player.connection.setPlayerLocation(destination.getX() + 0.5d, destination.getY(),
						destination.getZ() + 0.5d, getYaw(facing), 0.0f);
			}

			player.connection.sendPacket(new SPlaySoundEventPacket(1032, BlockPos.ZERO, 0, false));
		} else {
			if (interdimensional) {
				teleportNonPlayerEntityToDimension(entity, dimension, destination, getYaw(facing));
			} else {
				entity.setLocationAndAngles(destination.getX() + 0.5d, destination.getY(), destination.getZ() + 0.5d,
						getYaw(facing), 0.0f);
			}
		}
	}

	private static void teleportPlayerToDimension(ServerPlayerEntity player, RegistryKey<World> destinationDimension,
			BlockPos destination, float yaw, float pitch) {
		if (!net.minecraftforge.common.ForgeHooks.onTravelToDimension(player, destinationDimension))
			return;

		// Note: This field is normally not accessible (see accesstransformer.cfg in
		// META-INF folder).
		// Setting this flag circumvents at least a part of the shitty speed hack checks
		// in
		// net.minecraft.network.play.ServerPlayNetHandler#processPlayer() that cause
		// nothing but trouble.
		player.invulnerableDimensionChange = true;
		MinecraftServer server = player.getServer();
		if (server == null)
			return;

		@SuppressWarnings("unused")
		DimensionType originDimension = player.world.func_230315_m_();
		RegistryKey<World> startRegistryKey = player.world.func_234923_W_();// func_234923_W_() - returns the key of the
																			// player dimension
		ServerWorld originServerWorld = server.getWorld(startRegistryKey);// Eigentlich originDimension
//		player.dimension = destinationDimension;
		player.func_242111_a(destinationDimension, destination, 0.5f, false, false);
		ServerWorld destinationServerWorld = server.getWorld(destinationDimension);
		IWorldInfo worldInfo = player.world.getWorldInfo();
//		net.minecraftforge.fml.network.NetworkHooks.sendDimensionDataPacket(player.connection.netManager, player);
//The Method on top is Removed in Minecraft 1.16
		// player.connection.sendPacket(new SRespawnPacket(destinationDimension,
		// IWorldInfo.byHashing(worldInfo.getSeed()),//Diese Methode muss wieder
		// aktiviert werden, ich wei� nur nicht wie
		// worldInfo.getGenerator(), player.interactionManager.getGameType()));
		player.connection
				.sendPacket(new SServerDifficultyPacket(worldInfo.getDifficulty(), worldInfo.isDifficultyLocked()));
		PlayerList playerlist = server.getPlayerList();
		playerlist.updatePermissionLevel(player);
		originServerWorld.removeEntity(player, true);
		player.revive();
		player.setLocationAndAngles(destination.getX() + 0.5d, destination.getY(), destination.getZ() + 0.5d, yaw,
				pitch);
		player.setMotion(Vector3d.ZERO);
		player.setWorld(destinationServerWorld);
		destinationServerWorld.addDuringPortalTeleport(player);
		player.connection.setPlayerLocation(player.getPosX(), player.getPosY(), player.getPosZ(), yaw, pitch);
		player.interactionManager.setWorld(destinationServerWorld);
		player.connection.sendPacket(new SPlayerAbilitiesPacket(player.abilities));
		playerlist.sendWorldInfo(player, destinationServerWorld);
		playerlist.sendInventory(player);

		// Reapply effects like potions
		for (EffectInstance effectInstance : player.getActivePotionEffects()) {
			player.connection.sendPacket(new SPlayEntityEffectPacket(player.getEntityId(), effectInstance));
		}

		// Resend player XP otherwise the XP bar won't show up until XP is either gained
		// or lost
		player.connection.sendPacket(
				new SSetExperiencePacket(player.experience, player.experienceTotal, player.experienceLevel));

		net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerChangedDimensionEvent(player, startRegistryKey,
				destinationDimension);
	}

	/**
	 * Teleport a non-player entity to the specified position in the specified
	 * dimension facing the specified direction.
	 * ({@link Entity#changeDimension(DimensionType)} without the hardcoded
	 * dimension specific vanilla code)
	 *
	 * @param entity      The entity to teleport. Can be any entity except players
	 *                    (e.g. item, mob).
	 * @param dimension   The dimension to port to.
	 * @param destination The position to port to.
	 * @param yaw         The rotation yaw the entity should have after porting.
	 */
	private static void teleportNonPlayerEntityToDimension(Entity entity, RegistryKey<World> dimension,
			BlockPos destination, float yaw) {
		if (!entity.world.isRemote && entity.isAlive() && entity.isNonBoss()) {
			MinecraftServer server = entity.getServer();
			ServerWorld startWorld = server.getWorld(entity.world.func_234923_W_());
			ServerWorld destinationWorld = server.getWorld(dimension);

			entity.world.getProfiler().startSection("changeDimension");

			entity.func_241206_a_(destinationWorld);
			entity.detach();

			entity.world.getProfiler().startSection("reposition");
			entity.world.getProfiler().endStartSection("reloading");

			Entity portedEntity = entity.getType().create(destinationWorld);

			if (portedEntity != null) {
				portedEntity.copyDataFromOld(entity);
				portedEntity.moveToBlockPosAndAngles(destination, yaw, portedEntity.rotationPitch);
				destinationWorld.addFromAnotherDimension(portedEntity);
			}

			entity.remove(false);
			entity.world.getProfiler().endSection();
			startWorld.resetUpdateEntityTick();
			destinationWorld.resetUpdateEntityTick();
			entity.world.getProfiler().endSection();
		}
	}

	/**
	 * Converts the specified facing to a degree value.
	 *
	 * @param facing The facing to convert.
	 * @return <code>0</code> if facing is <code>null</code>, otherwise a value
	 *         between <code>0</code> and <code>270</code> that is a multiple of
	 *         <code>90</code>.
	 */
	public static float getYaw(Direction facing) {
		if (facing == null)
			return 0;

		float yaw;

		switch (facing) {
		case EAST:
			yaw = 270.0f;
			break;
		case WEST:
			yaw = 90.0f;
			break;
		case NORTH:
			yaw = 180.0f;
			break;
		default:
			yaw = 0.0f;
			break;
		}

		return yaw;
	}

	/**
	 * Checks if a string is a valid representation of a ResourceLocation. Allowed
	 * characters in the namespace are: [a-z0-9_.-] Allowed characters in the path
	 * are: [a-z0-9._-/] Namespace and path are separated by [:].
	 *
	 * @param locationString The string to check.
	 * @return <code>true</code> if only valid characters where found, otherwise
	 *         <code>false</code>.
	 */
	public static boolean isValidResourceLocation(String locationString) {
		if (StringUtils.isNullOrEmpty(locationString))
			return false;

		String[] components = locationString.split(":", 2);
		if (components.length != 2 || components[0].length() == 0 || components[1].length() == 0)
			return false;

		return (components[0].chars()
				.allMatch(c -> (c == 95 || c == 45 || c >= 97 && c <= 122 || c >= 48 && c <= 57 || c == 46))
				&& components[1].chars().allMatch(
						c -> (c == 95 || c == 45 || c >= 97 && c <= 122 || c >= 48 && c <= 57 || c == 46 || c == 47)));
	}
}
