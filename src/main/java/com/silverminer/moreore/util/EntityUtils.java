package com.silverminer.moreore.util;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;

public class EntityUtils {
	protected static final Logger LOGGER = LogManager.getLogger(EntityUtils.class);

	public static boolean spawnZombies(@Nullable DamageSource damageSource, MobEntity entitySource, double spawnChance,
			int maxZombies, int spawnTries, int players, double equipChance, EntityType<? extends MobEntity> entitytype,
			boolean shouldSpawnInDay) {
		if (!(entitySource.world instanceof ServerWorld)) {
			return false;
		} else {
			ServerWorld serverworld = (ServerWorld) entitySource.world;
			LivingEntity livingentity = entitySource.getAttackTarget();
			if (livingentity == null && damageSource != null && damageSource.getTrueSource() instanceof LivingEntity) {
				livingentity = (LivingEntity) damageSource.getTrueSource();
			}

			int i = MathHelper.floor(entitySource.getPosX());
			int j = MathHelper.floor(entitySource.getPosY());
			int k = MathHelper.floor(entitySource.getPosZ());

			if ((double) entitySource.getRNG()
					.nextFloat() < (entitySource.getAttribute(Attributes.ZOMBIE_SPAWN_REINFORCEMENTS).getValue()
							* spawnChance)
					&& entitySource.world.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING)) {

				int spawnedZombies = 0;
				spawnTries = spawnTries + players * 10;
				maxZombies = maxZombies + players * 5;
				for (int l = 0; l < spawnTries; ++l) {
					int i1 = i + MathHelper.nextInt(entitySource.getRNG(), 7, 40)
							* MathHelper.nextInt(entitySource.getRNG(), -1, 1);
					int j1 = j + MathHelper.nextInt(entitySource.getRNG(), 7, 40)
							* MathHelper.nextInt(entitySource.getRNG(), -1, 1);
					int k1 = k + MathHelper.nextInt(entitySource.getRNG(), 7, 40)
							* MathHelper.nextInt(entitySource.getRNG(), -1, 1);
					BlockPos blockpos = new BlockPos(i1, j1, k1);
					MobEntity monster = entitytype.create(entitySource.world);
					EntitySpawnPlacementRegistry.PlacementType entityspawnplacementregistry$placementtype = EntitySpawnPlacementRegistry
							.getPlacementType(entitytype);
					boolean helmet = false;
					boolean canSpawn = false;
					if (monster instanceof MonsterEntity) {
						helmet = !MonsterEntity.isValidLightLevel(serverworld, blockpos, monster.getRNG())
								&& shouldSpawnInDay;
						canSpawn = serverworld.getDifficulty() != Difficulty.PEACEFUL
								&& MonsterEntity.canSpawnOn(entitytype, serverworld, SpawnReason.REINFORCEMENT,
										blockpos, monster.getRNG())
								&& shouldSpawnInDay;
					}
					if (WorldEntitySpawner.canCreatureTypeSpawnAtLocation(entityspawnplacementregistry$placementtype,
							entitySource.world, blockpos, entitytype) || canSpawn) {
						monster.setPosition((double) i1, (double) j1, (double) k1);
						if (!entitySource.world.isPlayerWithin((double) i1, (double) j1, (double) k1, 7.0D)
								&& entitySource.world.checkNoEntityCollision(monster)
								&& entitySource.world.hasNoCollisions(monster)
								&& !entitySource.world.containsAnyLiquid(monster.getBoundingBox())) {
							if (livingentity != null)
								monster.setAttackTarget(livingentity);
							monster.setCustomName(new TranslationTextComponent("name.moreore.trooper"));
							monster.onInitialSpawn(serverworld,
									entitySource.world.getDifficultyForLocation(monster.getPosition()),
									SpawnReason.REINFORCEMENT, (ILivingEntityData) null, (CompoundNBT) null);
							if (entitySource.getRNG().nextDouble() < equipChance
									&& monster.getItemStackFromSlot(EquipmentSlotType.MAINHAND) == ItemStack.EMPTY) {
								if (entitySource.getRNG().nextDouble() < equipChance / 16) {
									monster.setItemStackToSlot(EquipmentSlotType.MAINHAND,
											new ItemStack(Items.DIAMOND_SWORD));
								} else {
									monster.setItemStackToSlot(EquipmentSlotType.MAINHAND,
											new ItemStack(Items.IRON_SWORD));
								}
							}
							if ((entitySource.getRNG().nextDouble() < equipChance || helmet)
									&& monster.getItemStackFromSlot(EquipmentSlotType.HEAD) == ItemStack.EMPTY) {
								monster.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.GOLDEN_HELMET));
							}
							if (entitySource.getRNG().nextDouble() < equipChance
									&& monster.getItemStackFromSlot(EquipmentSlotType.CHEST) == ItemStack.EMPTY) {
								monster.setItemStackToSlot(EquipmentSlotType.CHEST,
										new ItemStack(Items.GOLDEN_CHESTPLATE));
							}
							if (entitySource.getRNG().nextDouble() < equipChance
									&& monster.getItemStackFromSlot(EquipmentSlotType.LEGS) == ItemStack.EMPTY) {
								monster.setItemStackToSlot(EquipmentSlotType.LEGS,
										new ItemStack(Items.GOLDEN_LEGGINGS));
							}
							if (entitySource.getRNG().nextDouble() < equipChance
									&& monster.getItemStackFromSlot(EquipmentSlotType.FEET) == ItemStack.EMPTY) {
								monster.setItemStackToSlot(EquipmentSlotType.FEET, new ItemStack(Items.GOLDEN_BOOTS));
							}
							serverworld.func_242417_l(monster);
							try {
								monster.getAttribute(Attributes.ZOMBIE_SPAWN_REINFORCEMENTS).applyPersistentModifier(
										new AttributeModifier("Zombie reinforcement callee charge", (double) 0.05F,
												AttributeModifier.Operation.ADDITION));
							} catch (NullPointerException e) {
							}
							if (spawnedZombies++ >= maxZombies) {
								break;
							}
						}
					}
				}
			}
		}
		return true;
	}
}