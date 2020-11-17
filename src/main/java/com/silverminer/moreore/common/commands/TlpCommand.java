package com.silverminer.moreore.common.commands;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import javax.annotation.Nullable;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;

public class TlpCommand {
	private static final SimpleCommandExceptionType INVALID_POS_EXCEPTION = new SimpleCommandExceptionType(
			new TranslationTextComponent("commands.teleport.invalidPosition"));

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralCommandNode<CommandSource> literalcommandnode = dispatcher
				.register(Commands.literal("teleportieren").requires((source) -> {
					return source.hasPermissionLevel(0);
				}).then(Commands.argument("targets", EntityArgument.entities())
						.then(Commands.argument("location", Vec3Argument.vec3()).executes((command) -> {
							return teleportToPos(command.getSource(), EntityArgument.getEntities(command, "targets"),
									command.getSource().getWorld(), Vec3Argument.getLocation(command, "location"),
									(ILocationArgument) null);
						}).then(Commands.argument("rotation", RotationArgument.rotation()).executes((command) -> {
							return teleportToPos(command.getSource(), EntityArgument.getEntities(command, "targets"),
									command.getSource().getWorld(), Vec3Argument.getLocation(command, "location"),
									RotationArgument.getRotation(command, "rotation"));
						}))).then(Commands.argument("destination", EntityArgument.entity()).executes((command) -> {
							return teleportToEntity(command.getSource(), EntityArgument.getEntities(command, "targets"),
									EntityArgument.getEntity(command, "destination"));
						}))).then(Commands.argument("location", Vec3Argument.vec3()).executes((command) -> {
							return teleportToPos(command.getSource(),
									Collections.singleton(command.getSource().assertIsEntity()),
									command.getSource().getWorld(), Vec3Argument.getLocation(command, "location"),
									LocationInput.current());
						})).then(Commands.argument("destination", EntityArgument.entity()).executes((command) -> {
							return teleportToEntity(command.getSource(),
									Collections.singleton(command.getSource().assertIsEntity()),
									EntityArgument.getEntity(command, "destination"));
						})));
		dispatcher.register(Commands.literal("tlp").requires((source) -> {
			return source.hasPermissionLevel(0);
		}).redirect(literalcommandnode));
	}

	private static int teleportToEntity(CommandSource source, Collection<? extends Entity> targets, Entity destination)
			throws CommandSyntaxException {
		if (!(source.getEntity() instanceof ServerPlayerEntity))
			return 0;
		int level = ((ServerPlayerEntity) source.getEntity()).experienceLevel;
		int neededLevels = targets.size() * 3;
		if (level < neededLevels && !((ServerPlayerEntity) source.getEntity()).isCreative()) {
			if (targets.size() == 1) {
				source.sendFeedback(new TranslationTextComponent("commands.tlp.failed.entity.notEnoughLevel.single",
						targets.iterator().next().getDisplayName(), destination.getDisplayName()), true);
			} else {
				source.sendFeedback(new TranslationTextComponent("commands.tlp.failed.entity.notEnoughLevel.multiple",
						targets.size(), destination.getDisplayName()), true);
			}
			return 0;
		}
		for (Entity entity : targets) {
			teleport(source, entity, (ServerWorld) destination.world, destination.getPosX(), destination.getPosY(),
					destination.getPosZ(), EnumSet.noneOf(SPlayerPositionLookPacket.Flags.class),
					destination.rotationYaw, destination.rotationPitch);
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
			ILocationArgument position, @Nullable ILocationArgument rotationIn) throws CommandSyntaxException {
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

		if (!(source.getEntity() instanceof ServerPlayerEntity))
			return 0;
		int level = ((ServerPlayerEntity) source.getEntity()).experienceLevel;
		int neededLevels = targets.size() * 3;
		if (level < neededLevels && !((ServerPlayerEntity) source.getEntity()).isCreative()) {
			if (targets.size() == 1) {
				source.sendFeedback(new TranslationTextComponent("commands.tlp.failed.location.notEnoughLevel.single",
						targets.iterator().next().getDisplayName(), vec3d.x, vec3d.y, vec3d.z), true);
			} else {
				source.sendFeedback(new TranslationTextComponent("commands.tlp.failed.location.notEnoughLevel.multiple",
						targets.size(), vec3d.x, vec3d.y, vec3d.z), true);
			}
			return 0;
		}

		for (Entity entity : targets) {
			if (rotationIn == null) {
				teleport(source, entity, worldIn, vec3d.x, vec3d.y, vec3d.z, set, entity.rotationYaw,
						entity.rotationPitch);
			} else {
				teleport(source, entity, worldIn, vec3d.x, vec3d.y, vec3d.z, set, vec2f.y, vec2f.x);
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
			double z, Set<SPlayerPositionLookPacket.Flags> relativeList, float yaw, float pitch)
			throws CommandSyntaxException {
		BlockPos blockpos = new BlockPos(x, y, z);
		if (!World.isInvalidPosition(blockpos)) {
			throw INVALID_POS_EXCEPTION.create();
		} else {
			if (source.getEntity() instanceof ServerPlayerEntity) {
				ServerPlayerEntity player = ((ServerPlayerEntity) source.getEntity());
				boolean allowteleport = false;
				if (!player.isCreative()) {
					if (player.experienceLevel >= 3) {
						((ServerPlayerEntity) source.getEntity()).addExperienceLevel(-3);
						allowteleport = true;
					} else
						allowteleport = false;
				} else
					allowteleport = true;
				if (allowteleport) {
					if (entityIn instanceof ServerPlayerEntity) {
						ChunkPos chunkpos = new ChunkPos(new BlockPos(x, y, z));
						worldIn.getChunkProvider().registerTicket(TicketType.POST_TELEPORT, chunkpos, 1,
								entityIn.getEntityId());
						entityIn.stopRiding();
						if (((ServerPlayerEntity) entityIn).isSleeping()) {
							((ServerPlayerEntity) entityIn).stopSleepInBed(true, true);
						}

						if (worldIn == entityIn.world) {
							((ServerPlayerEntity) entityIn).connection.setPlayerLocation(x, y, z, yaw, pitch,
									relativeList);
						} else {
							((ServerPlayerEntity) entityIn).teleport(worldIn, x, y, z, yaw, pitch);
						}

						entityIn.setRotationYawHead(yaw);
					} else {
						float f1 = MathHelper.wrapDegrees(yaw);
						float f = MathHelper.wrapDegrees(pitch);
						f = MathHelper.clamp(f, -90.0F, 90.0F);
						if (worldIn == entityIn.world) {
							entityIn.setLocationAndAngles(x, y, z, f1, f);
							entityIn.setRotationYawHead(f1);
						} else {
							entityIn.detach();
							Entity entity = entityIn;
							entityIn = entityIn.getType().create(worldIn);
							if (entityIn == null) {
								return;
							}

							entityIn.copyDataFromOld(entity);
							entityIn.setLocationAndAngles(x, y, z, f1, f);
							entityIn.setRotationYawHead(f1);
							worldIn.addFromAnotherDimension(entityIn);
						}
					}
				}
			}
		}
	}
}