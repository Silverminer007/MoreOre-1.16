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
		if (!(entitySource.level instanceof ServerWorld)) {
			return false;
		} else {
			ServerWorld serverworld = (ServerWorld) entitySource.level;
			LivingEntity livingentity = entitySource.getTarget();
			if (livingentity == null && damageSource != null && damageSource.getEntity() instanceof LivingEntity) {
				livingentity = (LivingEntity) damageSource.getEntity();
			}

			int i = MathHelper.floor(entitySource.getX());
			int j = MathHelper.floor(entitySource.getY());
			int k = MathHelper.floor(entitySource.getZ());

			if ((double) entitySource.getRandom()
					.nextFloat() < (entitySource.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).getValue()
							* spawnChance)
					&& entitySource.level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {

				int spawnedZombies = 0;
				spawnTries = spawnTries + players * 10;
				maxZombies = maxZombies + players * 5;
				for (int l = 0; l < spawnTries; ++l) {
					int i1 = i + MathHelper.nextInt(entitySource.getRandom(), 7, 40)
							* MathHelper.nextInt(entitySource.getRandom(), -1, 1);
					int j1 = j + MathHelper.nextInt(entitySource.getRandom(), 7, 40)
							* MathHelper.nextInt(entitySource.getRandom(), -1, 1);
					int k1 = k + MathHelper.nextInt(entitySource.getRandom(), 7, 40)
							* MathHelper.nextInt(entitySource.getRandom(), -1, 1);
					BlockPos blockpos = new BlockPos(i1, j1, k1);
					MobEntity monster = entitytype.create(entitySource.level);
					EntitySpawnPlacementRegistry.PlacementType entityspawnplacementregistry$placementtype = EntitySpawnPlacementRegistry
							.getPlacementType(entitytype);
					boolean helmet = false;
					boolean canSpawn = false;
					if (monster instanceof MonsterEntity) {
						helmet = !MonsterEntity.isDarkEnoughToSpawn(serverworld, blockpos, monster.getRandom())
								&& shouldSpawnInDay;
						canSpawn = serverworld.getDifficulty() != Difficulty.PEACEFUL
								&& MonsterEntity.checkMobSpawnRules(entitytype, serverworld, SpawnReason.REINFORCEMENT,
										blockpos, monster.getRandom())
								&& shouldSpawnInDay;
					}
					if (WorldEntitySpawner.canSpawnAtBody(entityspawnplacementregistry$placementtype,
							entitySource.level, blockpos, entitytype) || canSpawn) {
						monster.setPos((double) i1, (double) j1, (double) k1);
						if (!entitySource.level.hasNearbyAlivePlayer((double) i1, (double) j1, (double) k1, 7.0D)
								&& entitySource.level.noCollision(monster)
								&& !entitySource.level.containsAnyLiquid(monster.getBoundingBox())) {
							if (livingentity != null)
								monster.setTarget(livingentity);
							monster.setCustomName(new TranslationTextComponent("name.moreore.trooper"));
							monster.finalizeSpawn(serverworld,
									entitySource.level.getCurrentDifficultyAt(monster.blockPosition()),
									SpawnReason.REINFORCEMENT, (ILivingEntityData) null, (CompoundNBT) null);
							if (entitySource.getRandom().nextDouble() < equipChance
									&& monster.getItemBySlot(EquipmentSlotType.MAINHAND) == ItemStack.EMPTY) {
								if (entitySource.getRandom().nextDouble() < equipChance / 16) {
									monster.setItemSlot(EquipmentSlotType.MAINHAND,
											new ItemStack(Items.DIAMOND_SWORD));
								} else {
									monster.setItemSlot(EquipmentSlotType.MAINHAND,
											new ItemStack(Items.IRON_SWORD));
								}
							}
							if ((entitySource.getRandom().nextDouble() < equipChance || helmet)
									&& monster.getItemBySlot(EquipmentSlotType.HEAD) == ItemStack.EMPTY) {
								monster.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.GOLDEN_HELMET));
							}
							if (entitySource.getRandom().nextDouble() < equipChance
									&& monster.getItemBySlot(EquipmentSlotType.CHEST) == ItemStack.EMPTY) {
								monster.setItemSlot(EquipmentSlotType.CHEST,
										new ItemStack(Items.GOLDEN_CHESTPLATE));
							}
							if (entitySource.getRandom().nextDouble() < equipChance
									&& monster.getItemBySlot(EquipmentSlotType.LEGS) == ItemStack.EMPTY) {
								monster.setItemSlot(EquipmentSlotType.LEGS,
										new ItemStack(Items.GOLDEN_LEGGINGS));
							}
							if (entitySource.getRandom().nextDouble() < equipChance
									&& monster.getItemBySlot(EquipmentSlotType.FEET) == ItemStack.EMPTY) {
								monster.setItemSlot(EquipmentSlotType.FEET, new ItemStack(Items.GOLDEN_BOOTS));
							}
							serverworld.addFreshEntity(monster);
							try {
								monster.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).addPermanentModifier(
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