package com.silverminer.moreore.common.objects.entitys;

import java.util.EnumSet;

import javax.annotation.Nullable;

import com.silverminer.moreore.init.items.ArmorItems;
import com.silverminer.moreore.init.items.RuneItems;
import com.silverminer.moreore.init.items.ToolItems;
import com.silverminer.moreore.util.EntityUtils;
import com.silverminer.moreore.util.network.MoreorePacketHandler;
import com.silverminer.moreore.util.network.PlayerInventoryChangePacket;
import com.silverminer.moreore.util.runes.RuneInventoryRegistry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;

public class GiantZombieKingEntity extends MonsterEntity {
	/**
	 * Die Phase in der sich der Riesen Zombie aktuell befindet. 0 = 1. Phase; 1 =
	 * Healing; 2 = 2. Phase
	 */
	private static final DataParameter<Integer> PHASE = EntityDataManager.defineId(GiantZombieKingEntity.class,
			DataSerializers.INT);
	private final ServerBossInfo bossInfo = (ServerBossInfo) (new ServerBossInfo(this.getDisplayName(),
			BossInfo.Color.GREEN, BossInfo.Overlay.PROGRESS)).setDarkenScreen(true);

	public GiantZombieKingEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
		super(type, worldIn);
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(0, new GiantZombieKingEntity.DoNothingGoal());
		this.goalSelector.addGoal(1, new SwimGoal(this));
		this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 50.0F));
		this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, ZombieEntity.class, SkeletonEntity.class))
				.setAlertOthers(ZombieEntity.class));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(PHASE, 0);
	}

	public int getPhase() {
		return this.entityData.get(PHASE);
	}

	public void setPhase(int phase) {
		this.entityData.set(PHASE, phase);
	}

	/**
	 * Called when the mob's health reaches 0.
	 */
	public void die(DamageSource cause) {
		if (cause == DamageSource.OUT_OF_WORLD) {
			super.die(cause);
		}
		if (this.getPhase() == 0) {
			this.setPhase(1);
			this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier(
					"Additional Health in phase 2", 100.0D, AttributeModifier.Operation.ADDITION));
			this.setHealth(this.getMaxHealth() / 500F);
			return;
		} else if (this.getPhase() == 2) {
			for (PlayerEntity player : bossInfo.getPlayers()) {
				int size = RuneInventoryRegistry.expandInventorySize(player.getUUID(), 1);
				MoreorePacketHandler.sendToAll(
						new PlayerInventoryChangePacket(RuneInventoryRegistry.writePlayerToNBT(player.getUUID())));
				if (!player.level.isClientSide())
					player.sendMessage(
							new TranslationTextComponent("entity.moreore.giant_zombie_king.runeInv_expand", size),
							this.getUUID());
			}
			super.die(cause);
		}
	}

	/**
	 * Called frequently so the entity can update its state every tick as required.
	 * For example, zombies and skeletons use this to react to sunlight and start to
	 * burn.
	 */
	public void aiStep() {
		super.aiStep();
		if (this.random.nextInt(1000) < 2) {
			EntityUtils.spawnZombies(this.getLastDamageSource(), this, this.getPhase() == 2 ? 0.25 : 0.125D, 2, 25,
					bossInfo.getPlayers().size(), this.getPhase() == 2 ? 1.0D : 0.05D, EntityType.SKELETON, true);
		}
		if (this.random.nextInt(1000) < 20) {
			EntityUtils.spawnZombies(this.getLastDamageSource(), this, this.getPhase() == 2 ? 0.5 : 0.25D, 5, 50,
					bossInfo.getPlayers().size(), this.getPhase() == 2 ? 1.0D : 0.1D, EntityType.ZOMBIE, true);
		}
		if (this.getPhase() == 1) {
			if (this.getHealth() == this.getMaxHealth()) {
				this.setDropChance(EquipmentSlotType.MAINHAND, 1.0F);
				this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(ToolItems.RAINBOW_SWORD.get()));
				this.setDropChance(EquipmentSlotType.HEAD, 1.0F);
				this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(ArmorItems.RAINBOW_HELMET.get()));
				this.setDropChance(EquipmentSlotType.CHEST, 1.0F);
				this.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(ArmorItems.RAINBOW_CHESTPLATE.get()));
				this.setDropChance(EquipmentSlotType.LEGS, 1.0F);
				this.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(ArmorItems.RAINBOW_LEGGINS.get()));
				this.setDropChance(EquipmentSlotType.FEET, 1.0F);
				this.setItemSlot(EquipmentSlotType.FEET, new ItemStack(ArmorItems.RAINBOW_BOOTS.get()));
				this.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier(
						"Additional Attack Damage in phase 2", 2.0D, AttributeModifier.Operation.ADDITION));
				this.setPhase(2);
				return;
			}
			this.heal(this.getMaxHealth() / 250F);
		}
	}

	protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHitIn) {
		super.dropCustomDeathLoot(source, looting, recentlyHitIn);
		ItemEntity itementity = this.spawnAtLocation(RuneItems.RUNE_BASE_BLOCK.get());
		if (itementity != null) {
			itementity.checkDespawn();
		}
	}

	/**
	 * Makes the entity despawn if requirements are reached
	 */
	public void checkDespawn() {
		if (this.level.getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) {
			this.remove();
		} else {
			this.noActionTime = 0;
		}
	}

	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return 13.050001F;
	}

	public static AttributeModifierMap setCustomAttributes() {
		return MonsterEntity.createMonsterAttributes().add(Attributes.MAX_HEALTH, 200.0D)
				.add(Attributes.MOVEMENT_SPEED, 1.0D)
				.add(Attributes.ATTACK_DAMAGE, 3.0D)
				.add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 1.0D).build();
	}

	@SuppressWarnings("deprecation")
	public float getBlockPathWeight(BlockPos pos, IWorldReader worldIn) {
		return worldIn.getBrightness(pos) - 0.5F;
	}

	@Override
	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
		if (compound.contains("phase", 3)) {
			this.setPhase(compound.getInt("phase"));
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("phase", this.getPhase());
	}

	public void setCustomName(@Nullable ITextComponent name) {
		super.setCustomName(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	/**
	 * Add the given player to the list of players tracking this entity. For
	 * instance, a player may track a boss in order to view its associated boss bar.
	 */
	public void startSeenByPlayer(ServerPlayerEntity player) {
		super.startSeenByPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	/**
	 * Removes the given player from the list of players tracking this entity. See
	 * {@link Entity#addTrackingPlayer} for more information on tracking.
	 */
	public void stopSeenByPlayer(ServerPlayerEntity player) {
		super.stopSeenByPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	protected void customServerAiStep() {
		super.customServerAiStep();
		this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
	}

	@Override
	/**
	 * Called when the entity is attacked.
	 */
	public boolean hurt(DamageSource source, float amount) {
		if (source != DamageSource.DROWN && !(source.getEntity() instanceof GiantZombieKingEntity)) {
			if ((!(this.getPhase() == 1 && source != DamageSource.OUT_OF_WORLD))) {
				if (!super.hurt(source, amount)) {
					return false;
				} else {
					if (this.random.nextInt(100) < 10) {
						return EntityUtils.spawnZombies(source, this, 0.25D, 3, 30, bossInfo.getPlayers().size(),
								this.getPhase() == 2 ? 0.8D : 0.05D, EntityType.SKELETON, true);
					} else {
						return EntityUtils.spawnZombies(source, this, 0.75D, 7, 100, bossInfo.getPlayers().size(),
								this.getPhase() == 2 ? 1.0D : 0.2D, EntityType.ZOMBIE, true);
					}
				}
			}
		}
		return false;
	}

	class DoNothingGoal extends Goal {
		public DoNothingGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
		}

		@Override
		public boolean canUse() {
			return GiantZombieKingEntity.this.getPhase() == 1;
		}
	}
}