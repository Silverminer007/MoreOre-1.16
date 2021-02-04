package com.silverminer.moreore.common.portal;

import java.util.function.Function;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SPlaySoundEventPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;

public final class Utils {

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
	public static void teleportTo(Entity entity, RegistryKey<World> dimension, BlockPos destination, @Nullable MinecraftServer server) {
		if (entity == null || dimension == null || destination == null || entity.isBeingRidden()
				|| entity.isOnePlayerRiding() || !entity.isNonBoss())
			return;

		if(server == null)
			server = entity.getServer();
		if(server == null)
			return;
		ServerPlayerEntity player = (entity instanceof ServerPlayerEntity) ? (ServerPlayerEntity) entity : null;
		boolean interdimensional = (entity.world.getDimensionKey() != dimension);
		entity.setMotion(Vector3d.ZERO);

		if (interdimensional) {
			teleportEntityToDimension(server, entity, dimension, destination);
		} else if (player != null) {
			player.connection.setPlayerLocation(destination.getX() + 0.5d, destination.getY(),
					destination.getZ() + 0.5d, 0.0F, 0.0F);
			player.connection.sendPacket(new SPlaySoundEventPacket(1032, BlockPos.ZERO, 0, false));
		} else {
			entity.setLocationAndAngles(destination.getX() + 0.5d, destination.getY(), destination.getZ() + 0.5d, 0.0F,
					0.0F);
		}
	}

	private static void teleportEntityToDimension(MinecraftServer server, Entity entity,
			RegistryKey<World> destinationDimension, BlockPos destination) {
		entity.changeDimension(server.getWorld(destinationDimension), new ITeleporter() {
			@Override
			public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yaw,
					Function<Boolean, Entity> repositionEntity) {
				Entity repositionedEntity = repositionEntity.apply(false);
				repositionedEntity.setPositionAndUpdate(destination.getX(), destination.getY(), destination.getZ());
				return repositionedEntity;
			}
		});
	}
}