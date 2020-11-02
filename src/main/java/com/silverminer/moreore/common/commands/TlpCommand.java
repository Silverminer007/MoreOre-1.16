package com.silverminer.moreore.common.commands;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import javax.annotation.Nullable;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.ILocationArgument;
import net.minecraft.command.arguments.LocationInput;
import net.minecraft.command.arguments.RotationArgument;
import net.minecraft.command.arguments.Vec3Argument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;

public class TlpCommand {

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralCommandNode<CommandSource> literalcommandnode = dispatcher
				.register(Commands.literal("teleportieren").requires((p_198816_0_) -> {
					return p_198816_0_.hasPermissionLevel(0);
				}).then(Commands.argument("targets", EntityArgument.entities())
						.then(Commands.argument("location", Vec3Argument.vec3()).executes((p_198807_0_) -> {
							return teleportToPos(p_198807_0_.getSource(),
									EntityArgument.getEntities(p_198807_0_, "targets"),
									p_198807_0_.getSource().getWorld(),
									Vec3Argument.getLocation(p_198807_0_, "location"), (ILocationArgument) null,
									(TlpCommand.Facing) null);
						}).then(Commands.argument("rotation", RotationArgument.rotation()).executes((p_198811_0_) -> {
							return teleportToPos(p_198811_0_.getSource(),
									EntityArgument.getEntities(p_198811_0_, "targets"),
									p_198811_0_.getSource().getWorld(),
									Vec3Argument.getLocation(p_198811_0_, "location"),
									RotationArgument.getRotation(p_198811_0_, "rotation"), (TlpCommand.Facing) null);
						})).then(Commands.literal("facing").then(Commands.literal("entity").then(
								Commands.argument("facingEntity", EntityArgument.entity()).executes((p_198806_0_) -> {
									return teleportToPos(p_198806_0_.getSource(),
											EntityArgument.getEntities(p_198806_0_, "targets"),
											p_198806_0_.getSource().getWorld(),
											Vec3Argument.getLocation(p_198806_0_, "location"), (ILocationArgument) null,
											new TlpCommand.Facing(EntityArgument.getEntity(p_198806_0_, "facingEntity"),
													EntityAnchorArgument.Type.FEET));
								}).then(Commands.argument("facingAnchor", EntityAnchorArgument.entityAnchor())
										.executes((p_198812_0_) -> {
											return teleportToPos(p_198812_0_.getSource(),
													EntityArgument.getEntities(p_198812_0_, "targets"),
													p_198812_0_.getSource().getWorld(),
													Vec3Argument.getLocation(p_198812_0_, "location"),
													(ILocationArgument) null,
													new TlpCommand.Facing(
															EntityArgument.getEntity(p_198812_0_, "facingEntity"),
															EntityAnchorArgument.getEntityAnchor(p_198812_0_,
																	"facingAnchor")));
										}))))
								.then(Commands.argument("facingLocation", Vec3Argument.vec3())
										.executes((p_198805_0_) -> {
											return teleportToPos(p_198805_0_.getSource(),
													EntityArgument.getEntities(p_198805_0_, "targets"),
													p_198805_0_.getSource().getWorld(),
													Vec3Argument.getLocation(p_198805_0_, "location"),
													(ILocationArgument) null, new TlpCommand.Facing(
															Vec3Argument.getVec3(p_198805_0_, "facingLocation")));
										}))))
						.then(Commands.argument("destination", EntityArgument.entity()).executes((p_198814_0_) -> {
							return teleportToEntity(p_198814_0_.getSource(),
									EntityArgument.getEntities(p_198814_0_, "targets"),
									EntityArgument.getEntity(p_198814_0_, "destination"));
						}))).then(Commands.argument("location", Vec3Argument.vec3()).executes((p_200560_0_) -> {
							return teleportToPos(p_200560_0_.getSource(),
									Collections.singleton(p_200560_0_.getSource().assertIsEntity()),
									p_200560_0_.getSource().getWorld(),
									Vec3Argument.getLocation(p_200560_0_, "location"), LocationInput.current(),
									(TlpCommand.Facing) null);
						})).then(Commands.argument("destination", EntityArgument.entity()).executes((p_200562_0_) -> {
							return teleportToEntity(p_200562_0_.getSource(),
									Collections.singleton(p_200562_0_.getSource().assertIsEntity()),
									EntityArgument.getEntity(p_200562_0_, "destination"));
						})));
		dispatcher.register(Commands.literal("tlp").requires((p_200556_0_) -> {
			return p_200556_0_.hasPermissionLevel(0);
		}).redirect(literalcommandnode));
	}

	private static int teleportToEntity(CommandSource source, Collection<? extends Entity> targets,
			Entity destination) {
		for (Entity entity : targets) {
			teleport(source, entity, (ServerWorld) destination.world, destination.getPosX(), destination.getPosY(),
					destination.getPosZ(), EnumSet.noneOf(SPlayerPositionLookPacket.Flags.class),
					destination.rotationYaw, destination.rotationPitch, (TlpCommand.Facing) null);
		}

		if (targets.size() == 1) {
			source.sendFeedback(new TranslationTextComponent("commands.teleport.success.entity.single",
					targets.iterator().next().getDisplayName(), destination.getDisplayName()), true);
		} else {
			source.sendFeedback(new TranslationTextComponent("commands.teleport.success.entity.multiple",
					targets.size(), destination.getDisplayName()), true);
		}

		return targets.size();
	}

	private static int teleportToPos(CommandSource source, Collection<? extends Entity> targets, ServerWorld worldIn,
			ILocationArgument position, @Nullable ILocationArgument rotationIn, @Nullable TlpCommand.Facing facing)
			throws CommandSyntaxException {
		Vector3d vec3d = position.getPosition(source);
		Vector2f vec2f = rotationIn == null ? null : rotationIn.getRotation(source);
		Set<SPlayerPositionLookPacket.Flags> set = EnumSet.noneOf(SPlayerPositionLookPacket.Flags.class);
		if (position.isXRelative()) {
			set.add(SPlayerPositionLookPacket.Flags.X);
		}

		if (position.isYRelative()) {
			set.add(SPlayerPositionLookPacket.Flags.Y);
		}

		if (position.isZRelative()) {
			set.add(SPlayerPositionLookPacket.Flags.Z);
		}

		if (rotationIn == null) {
			set.add(SPlayerPositionLookPacket.Flags.X_ROT);
			set.add(SPlayerPositionLookPacket.Flags.Y_ROT);
		} else {
			if (rotationIn.isXRelative()) {
				set.add(SPlayerPositionLookPacket.Flags.X_ROT);
			}

			if (rotationIn.isYRelative()) {
				set.add(SPlayerPositionLookPacket.Flags.Y_ROT);
			}
		}

		for (Entity entity : targets) {
			if (rotationIn == null) {
				teleport(source, entity, worldIn, vec3d.x, vec3d.y, vec3d.z, set, entity.rotationYaw,
						entity.rotationPitch, facing);
			} else {
				teleport(source, entity, worldIn, vec3d.x, vec3d.y, vec3d.z, set, vec2f.y, vec2f.x, facing);
			}
		}

		if (targets.size() == 1) {
			source.sendFeedback(new TranslationTextComponent("commands.teleport.success.location.single",
					targets.iterator().next().getDisplayName(), vec3d.x, vec3d.y, vec3d.z), true);
		} else {
			source.sendFeedback(new TranslationTextComponent("commands.teleport.success.location.multiple",
					targets.size(), vec3d.x, vec3d.y, vec3d.z), true);
		}

		return targets.size();
	}

	private static void teleport(CommandSource source, Entity entityIn, ServerWorld worldIn, double x, double y,
			double z, Set<SPlayerPositionLookPacket.Flags> relativeList, float yaw, float pitch,
			@Nullable TlpCommand.Facing facing) {
		if (entityIn instanceof ServerPlayerEntity) {
			if (source.getEntity() != null && source.getEntity() instanceof ServerPlayerEntity
					&& entityIn == source.getEntity()) {
				int level = ((ServerPlayerEntity) entityIn).experienceLevel;
				boolean allowteleport = false;
				if (!((ServerPlayerEntity) entityIn).isCreative()) {
					if (level > 3) {
						((ServerPlayerEntity) entityIn).setExperienceLevel(level - 3);
						allowteleport = true;
					} else
						allowteleport = false;
				} else
					allowteleport = true;
				if (allowteleport) {
					ChunkPos chunkpos = new ChunkPos(new BlockPos(x, y, z));
					worldIn.getChunkProvider().registerTicket(TicketType.POST_TELEPORT, chunkpos, 1,
							entityIn.getEntityId());
					entityIn.stopRiding();
					if (((ServerPlayerEntity) entityIn).isSleeping()) {
						((ServerPlayerEntity) entityIn).stopSleepInBed(true, true);
					}

					if (worldIn == entityIn.world) {
						((ServerPlayerEntity) entityIn).connection.setPlayerLocation(x, y, z, yaw, pitch, relativeList);
					} else {
						((ServerPlayerEntity) entityIn).teleport(worldIn, x, y, z, yaw, pitch);
					}

					entityIn.setRotationYawHead(yaw);
				}
			}
		}
	}

	static class Facing {
		private final Vector3d position;
		private final Entity entity;
		private final EntityAnchorArgument.Type anchor;

		public Facing(Entity entityIn, EntityAnchorArgument.Type anchorIn) {
			this.entity = entityIn;
			this.anchor = anchorIn;
			this.position = anchorIn.apply(entityIn);
		}

		public Facing(Vector3d positionIn) {
			this.entity = null;
			this.position = positionIn;
			this.anchor = null;
		}

		public void updateLook(CommandSource source, Entity entityIn) {
			if (this.entity != null) {
				if (entityIn instanceof ServerPlayerEntity) {
					((ServerPlayerEntity) entityIn).lookAt(source.getEntityAnchorType(), this.entity, this.anchor);
				} else {
					entityIn.lookAt(source.getEntityAnchorType(), this.position);
				}
			} else {
				entityIn.lookAt(source.getEntityAnchorType(), this.position);
			}

		}
	}
}