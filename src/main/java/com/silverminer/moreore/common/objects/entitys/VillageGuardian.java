package com.silverminer.moreore.common.objects.entitys;

import java.util.UUID;

import com.silverminer.moreore.common.objects.entitys.goals.MoveToPatrolPointGoal;
import com.silverminer.moreore.init.ModEntityTypesInit;
import com.silverminer.moreore.init.blocks.InitBlocks;
import com.silverminer.moreore.init.items.BlockItems;
import com.silverminer.moreore.init.items.FootItems;

import net.minecraft.block.BlockState;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.MoveThroughVillageGoal;
import net.minecraft.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.ReturnToVillageGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class VillageGuardian extends AnimalEntity {
	private int inLove;
	private UUID playerInLove;
	protected int forcedAge;
	protected int forcedAgeTimer;
	private int attackTimer;

	public VillageGuardian(EntityType<? extends AnimalEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	public AgeableEntity getBreedOffspring(ServerWorld worldIn, AgeableEntity ageable) {
		VillageGuardian entity = new VillageGuardian(ModEntityTypesInit.VILLAGE_GUARDIAN.get(), worldIn);
		entity.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(new BlockPos(entity.blockPosition())),
				SpawnReason.BREEDING, (ILivingEntityData) null, (CompoundNBT) null);
		return entity;
	}

	public void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
		this.goalSelector.addGoal(3, new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
		this.goalSelector.addGoal(3, new MoveToPatrolPointGoal(InitBlocks.PATROLPOINT.get(), this, 0.9D, 50));
		this.goalSelector.addGoal(3, new ReturnToVillageGoal(this, 0.6D, false));
		this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(2,
				new TemptGoal(this, 1.1D, Ingredient.of(BlockItems.PATROLPOINT.get()), false));
		this.goalSelector.addGoal(4, new MoveThroughVillageGoal(this, 0.6D, false, 4, () -> {
			return false;
		}));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 0.6D));
		this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(3,
				new NearestAttackableTargetGoal<>(this, MobEntity.class, 5, false, false, (p_213619_0_) -> {
					return p_213619_0_ instanceof IMob && !(p_213619_0_ instanceof CreeperEntity);
				}));
	}

	public static AttributeModifierMap setCustomAttributes() {
		return AnimalEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, 20.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.3D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 1.0D).add(Attributes.FOLLOW_RANGE, 5.0)
				.add(Attributes.ATTACK_DAMAGE, 7.0D).build();
	}

	/**
	 * Decrements the entity's air supply when underwater
	 */
	protected int decreaseAirSupply(int air) {
		return air;
	}

	public void push(Entity entityIn) {
		if (entityIn instanceof IMob && !(entityIn instanceof CreeperEntity) && this.getRandom().nextInt(20) == 0) {
			this.setTarget((LivingEntity) entityIn);
		}

		super.setLastHurtMob(entityIn);
	}

	/**
	 * Called frequently so the entity can update its state every tick as required.
	 * For example, zombies and skeletons use this to react to sunlight and start to
	 * burn.
	 */
	@SuppressWarnings("deprecation")
	public void aiStep() {
		super.aiStep();
		if (this.attackTimer > 0) {
			--this.attackTimer;
		}

		if (getHorizontalDistanceSqr(this.getDeltaMovement()) > (double) 2.5000003E-7F && this.random.nextInt(5) == 0) {
			int i = MathHelper.floor(this.getX());
			int j = MathHelper.floor(this.getY() - (double) 0.2F);
			int k = MathHelper.floor(this.getZ());
			BlockPos pos = new BlockPos(i, j, k);
			BlockState blockstate = this.level.getBlockState(pos);
			if (!blockstate.isAir(this.level, pos)) {
				this.level.addParticle(new BlockParticleData(ParticleTypes.BLOCK, blockstate).setPos(pos),
						this.getX() + ((double) this.random.nextFloat() - 0.5D) * (double) this.getBbWidth(),
						this.getY() + 0.1D,
						this.getZ() + ((double) this.random.nextFloat() - 0.5D) * (double) this.getBbWidth(),
						4.0D * ((double) this.random.nextFloat() - 0.5D), 0.5D,
						((double) this.random.nextFloat() - 0.5D) * 4.0D);
			}
		}
		if (this.getAge() != 0) {
			this.inLove = 0;
		}

		if (this.inLove > 0) {
			--this.inLove;
			if (this.inLove % 10 == 0) {
				double d0 = this.random.nextGaussian() * 0.02D;
				double d1 = this.random.nextGaussian() * 0.02D;
				double d2 = this.random.nextGaussian() * 0.02D;
				this.level.addParticle(ParticleTypes.HEART, this.getRandomX(1.0D), this.getRandomY() + 0.5D,
						this.getRandomZ(1.0D), d0, d1, d2);
			}
		}
		if (this.level.isClientSide()) {
			if (this.forcedAgeTimer > 0) {
				if (this.forcedAgeTimer % 4 == 0) {
					this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D),
							this.getRandomY() + 0.5D, this.getRandomZ(1.0D), 0.0D, 0.0D, 0.0D);
				}

				--this.forcedAgeTimer;
			}
		} else if (this.isAlive()) {
			int i = this.getAge();
			if (i < 0) {
				++i;
				this.setAge(i);
			} else if (i > 0) {
				--i;
				this.setAge(i);
			}
		}
	}

	public boolean canAttackType(EntityType<?> typeIn) {
		return typeIn == EntityType.CREEPER || typeIn == EntityType.PLAYER ? false : super.canAttackType(typeIn);
	}

	private float getAttackDamage() {
		return (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
	}

	@Override
	public boolean doHurtTarget(Entity entityIn) {
		this.attackTimer = 10;
		this.level.broadcastEntityEvent(this, (byte) 4);
		float f = this.getAttackDamage();
		float f1 = f > 0.0F ? f / 2.0F + (float) this.random.nextInt((int) f) : 0.0F;
		boolean flag = entityIn.hurt(DamageSource.mobAttack(this), f1);
		if (flag) {
			entityIn.setDeltaMovement(entityIn.getDeltaMovement().add(0.0D, (double) 0.4F, 0.0D));
			this.doEnchantDamageEffects(this, entityIn);
		}

		this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
		return flag;
	}

	/**
	 * Handler for {@link World#setEntityState}
	 */
	@OnlyIn(Dist.CLIENT)
	public void handleEntityEvent(byte id) {
		if (id == 4) {
			this.attackTimer = 10;
			this.playSound(SoundEvents.IRON_GOLEM_DAMAGE, 1.0F, 1.0F);
		} else if (id == 18) {
			for (int i = 0; i < 7; ++i) {
				double d0 = this.random.nextGaussian() * 0.02D;
				double d1 = this.random.nextGaussian() * 0.02D;
				double d2 = this.random.nextGaussian() * 0.02D;
				this.level.addParticle(ParticleTypes.HEART, this.getRandomX(1.0D), this.getRandomY() + 0.5D,
						this.getRandomZ(1.0D), d0, d1, d2);
			}
		} else {
			super.handleEntityEvent(id);
		}
	}

	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.IRON_GOLEM_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.IRON_GOLEM_DEATH;
	}

	protected void playStepSound(BlockPos pos, BlockState blockIn) {
		this.playSound(SoundEvents.IRON_GOLEM_STEP, 1.0F, 1.0F);
	}

	public boolean isNotColliding(IWorldReader worldIn) {
		BlockPos blockpos = this.blockPosition();
		BlockPos blockpos1 = blockpos.below();
		BlockState blockstate = worldIn.getBlockState(blockpos1);
		if (!blockstate.entityCanStandOnFace(worldIn, blockpos1, this, Direction.UP)) {// Could also be an other Direction? I
																				// don't know
			return false;
		} else {
			for (int i = 1; i < 3; ++i) {
				BlockPos blockpos2 = blockpos.above(i);
				BlockState blockstate1 = worldIn.getBlockState(blockpos2);
				if (!WorldEntitySpawner.isValidEmptySpawnBlock(worldIn, blockpos2, blockstate1, blockstate1.getFluidState(),
						ModEntityTypesInit.VILLAGE_GUARDIAN.get())) {
					return false;
				}
			}

			return WorldEntitySpawner.isValidEmptySpawnBlock(worldIn, blockpos, worldIn.getBlockState(blockpos),
					Fluids.EMPTY.defaultFluidState(), ModEntityTypesInit.VILLAGE_GUARDIAN.get())
					&& worldIn.noCollision(this);
		}
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean hurt(DamageSource source, float amount) {
		boolean flag = super.hurt(source, amount);
		if (flag) {
			this.playSound(SoundEvents.IRON_GOLEM_DAMAGE, 1.0F, 1.0F);
		}
		return flag;
	}

	/**
	 * Checks if the parameter is an item which this animal can be fed to breed it
	 * (wheat, carrots or seeds depending on the animal type)
	 */
	public boolean isFood(ItemStack stack) {
		return stack.getItem() == FootItems.BANANA.get();
	}

	public boolean processInteract(PlayerEntity player, Hand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (this.isFood(itemstack)) {
			if (!this.level.isClientSide() && this.getAge() == 0 && this.canBreed()) {
				this.usePlayerItem(player, itemstack);
				this.setInLove(player);
				player.swing(hand, true);
				return true;
			}

			if (this.isBaby()) {
				this.usePlayerItem(player, itemstack);
				this.ageUp((int) ((float) (-this.getAge() / 20) * 0.1F), true);
				return true;
			}
		}
		Item item = itemstack.getItem();
		if (item == BlockItems.PATROLPOINT.get()) {
			float f = this.getHealth();
			this.heal(25.0F);
			if (this.getHealth() == f) {
			} else {
				float f1 = 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F;
				this.playSound(SoundEvents.IRON_GOLEM_REPAIR, 1.0F, f1);
				if (!player.abilities.instabuild) {
					itemstack.shrink(1);
				}
			}
		}
		if (item instanceof SpawnEggItem && ((SpawnEggItem) item).spawnsEntity(itemstack.getTag(), this.getType())) {
			if (!this.level.isClientSide()) {
				AgeableEntity ageableentity = this.getBreedOffspring((ServerWorld) this.level, this);
				if (ageableentity != null) {
					ageableentity.setAge(-24000);
					ageableentity.moveTo(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
					this.level.addFreshEntity(ageableentity);
					if (itemstack.hasCustomHoverName()) {
						ageableentity.setCustomName(itemstack.getDisplayName());
					}

					this.onOffspringSpawnedFromEgg(player, ageableentity);
					if (!player.abilities.instabuild) {
						itemstack.shrink(1);
					}
				}
			}

			return true;
		} else {
			return false;
		}
	}

	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("Age", this.getAge());
		compound.putInt("ForcedAge", this.forcedAge);
		compound.putInt("InLove", this.inLove);
		if (this.playerInLove != null) {
			compound.putUUID("LoveCause", this.playerInLove);
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		this.setAge(compound.getInt("Age"));
		this.forcedAge = compound.getInt("ForcedAge");
		this.inLove = compound.getInt("InLove");
		this.playerInLove = compound.hasUUID("LoveCause") ? compound.getUUID("LoveCause") : null;
	}

	public static class AgeableData implements ILivingEntityData {
		private int field_226254_a_;
		private boolean field_226255_b_ = true;
		private float field_226256_c_ = 0.05F;

		public int func_226257_a_() {
			return this.field_226254_a_;
		}

		public void func_226260_b_() {
			++this.field_226254_a_;
		}

		public boolean func_226261_c_() {
			return this.field_226255_b_;
		}

		public void func_226259_a_(boolean p_226259_1_) {
			this.field_226255_b_ = p_226259_1_;
		}

		public float func_226262_d_() {
			return this.field_226256_c_;
		}

		public void func_226258_a_(float p_226258_1_) {
			this.field_226256_c_ = p_226258_1_;
		}
	}
}