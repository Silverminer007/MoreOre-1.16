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
	public AgeableEntity func_241840_a(ServerWorld worldIn, AgeableEntity ageable) {
		VillageGuardian entity = new VillageGuardian(ModEntityTypesInit.VILLAGE_GUARDIAN.get(), worldIn);
		entity.onInitialSpawn(worldIn, worldIn.getDifficultyForLocation(new BlockPos(entity.getPositionVec())),
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
				new TemptGoal(this, 1.1D, Ingredient.fromItems(BlockItems.PATROLPOINT.get()), false));
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
		return AnimalEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 20.0D)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3D)
				.createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 7.0D).create();
	}

	/**
	 * Decrements the entity's air supply when underwater
	 */
	protected int decreaseAirSupply(int air) {
		return air;
	}

	protected void collideWithEntity(Entity entityIn) {
		if (entityIn instanceof IMob && !(entityIn instanceof CreeperEntity) && this.getRNG().nextInt(20) == 0) {
			this.setAttackTarget((LivingEntity) entityIn);
		}

		super.collideWithEntity(entityIn);
	}

	/**
	 * Called frequently so the entity can update its state every tick as required.
	 * For example, zombies and skeletons use this to react to sunlight and start to
	 * burn.
	 */
	@SuppressWarnings("deprecation")
	public void livingTick() {
		super.livingTick();
		if (this.attackTimer > 0) {
			--this.attackTimer;
		}

		if (horizontalMag(this.getMotion()) > (double) 2.5000003E-7F && this.rand.nextInt(5) == 0) {
			int i = MathHelper.floor(this.getPosX());
			int j = MathHelper.floor(this.getPosY() - (double) 0.2F);
			int k = MathHelper.floor(this.getPosZ());
			BlockPos pos = new BlockPos(i, j, k);
			BlockState blockstate = this.world.getBlockState(pos);
			if (!blockstate.isAir(this.world, pos)) {
				this.world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, blockstate).setPos(pos),
						this.getPosX() + ((double) this.rand.nextFloat() - 0.5D) * (double) this.getWidth(),
						this.getPosY() + 0.1D,
						this.getPosZ() + ((double) this.rand.nextFloat() - 0.5D) * (double) this.getWidth(),
						4.0D * ((double) this.rand.nextFloat() - 0.5D), 0.5D,
						((double) this.rand.nextFloat() - 0.5D) * 4.0D);
			}
		}
		if (this.getGrowingAge() != 0) {
			this.inLove = 0;
		}

		if (this.inLove > 0) {
			--this.inLove;
			if (this.inLove % 10 == 0) {
				double d0 = this.rand.nextGaussian() * 0.02D;
				double d1 = this.rand.nextGaussian() * 0.02D;
				double d2 = this.rand.nextGaussian() * 0.02D;
				this.world.addParticle(ParticleTypes.HEART, this.getPosXRandom(1.0D), this.getPosYRandom() + 0.5D,
						this.getPosZRandom(1.0D), d0, d1, d2);
			}
		}
		if (this.world.isRemote) {
			if (this.forcedAgeTimer > 0) {
				if (this.forcedAgeTimer % 4 == 0) {
					this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getPosXRandom(1.0D),
							this.getPosYRandom() + 0.5D, this.getPosZRandom(1.0D), 0.0D, 0.0D, 0.0D);
				}

				--this.forcedAgeTimer;
			}
		} else if (this.isAlive()) {
			int i = this.getGrowingAge();
			if (i < 0) {
				++i;
				this.setGrowingAge(i);
			} else if (i > 0) {
				--i;
				this.setGrowingAge(i);
			}
		}
	}

	public boolean canAttack(EntityType<?> typeIn) {
		return typeIn == EntityType.CREEPER || typeIn == EntityType.PLAYER ? false : super.canAttack(typeIn);
	}

	private float getAttackDamage() {
		return (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
	}

	public boolean attackEntityAsMob(Entity entityIn) {
		this.attackTimer = 10;
		this.world.setEntityState(this, (byte) 4);
		float f = this.getAttackDamage();
		float f1 = f > 0.0F ? f / 2.0F + (float) this.rand.nextInt((int) f) : 0.0F;
		boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f1);
		if (flag) {
			entityIn.setMotion(entityIn.getMotion().add(0.0D, (double) 0.4F, 0.0D));
			this.applyEnchantments(this, entityIn);
		}

		this.playSound(SoundEvents.ENTITY_IRON_GOLEM_ATTACK, 1.0F, 1.0F);
		return flag;
	}

	/**
	 * Handler for {@link World#setEntityState}
	 */
	@OnlyIn(Dist.CLIENT)
	public void handleStatusUpdate(byte id) {
		if (id == 4) {
			this.attackTimer = 10;
			this.playSound(SoundEvents.ENTITY_IRON_GOLEM_ATTACK, 1.0F, 1.0F);
		} else if (id == 18) {
			for (int i = 0; i < 7; ++i) {
				double d0 = this.rand.nextGaussian() * 0.02D;
				double d1 = this.rand.nextGaussian() * 0.02D;
				double d2 = this.rand.nextGaussian() * 0.02D;
				this.world.addParticle(ParticleTypes.HEART, this.getPosXRandom(1.0D), this.getPosYRandom() + 0.5D,
						this.getPosZRandom(1.0D), d0, d1, d2);
			}
		} else {
			super.handleStatusUpdate(id);
		}
	}

	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_IRON_GOLEM_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_IRON_GOLEM_DEATH;
	}

	protected void playStepSound(BlockPos pos, BlockState blockIn) {
		this.playSound(SoundEvents.ENTITY_IRON_GOLEM_STEP, 1.0F, 1.0F);
	}

	public boolean isNotColliding(IWorldReader worldIn) {
		BlockPos blockpos = new BlockPos(this.getPositionVec());
		BlockPos blockpos1 = blockpos.down();
		BlockState blockstate = worldIn.getBlockState(blockpos1);
		if (!blockstate.isTopSolid(worldIn, blockpos1, this, Direction.DOWN)) {// Could also be an other Direction? I
																				// don't know
			return false;
		} else {
			for (int i = 1; i < 3; ++i) {
				BlockPos blockpos2 = blockpos.up(i);
				BlockState blockstate1 = worldIn.getBlockState(blockpos2);
				if (!WorldEntitySpawner.func_234968_a_(worldIn, blockpos2, blockstate1, blockstate1.getFluidState(),
						ModEntityTypesInit.VILLAGE_GUARDIAN.get())) {
					return false;
				}
			}

			return WorldEntitySpawner.func_234968_a_(worldIn, blockpos, worldIn.getBlockState(blockpos),
					Fluids.EMPTY.getDefaultState(), ModEntityTypesInit.VILLAGE_GUARDIAN.get())
					&& worldIn.checkNoEntityCollision(this);
		}
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource source, float amount) {
		boolean flag = super.attackEntityFrom(source, amount);
		if (flag) {
			this.playSound(SoundEvents.ENTITY_IRON_GOLEM_DAMAGE, 1.0F, 1.0F);
		}
		return flag;
	}

	/**
	 * Checks if the parameter is an item which this animal can be fed to breed it
	 * (wheat, carrots or seeds depending on the animal type)
	 */
	public boolean isBreedingItem(ItemStack stack) {
		return stack.getItem() == FootItems.BANANA.get();
	}

	public boolean processInteract(PlayerEntity player, Hand hand) {
		ItemStack itemstack = player.getHeldItem(hand);
		if (this.isBreedingItem(itemstack)) {
			if (!this.world.isRemote && this.getGrowingAge() == 0 && this.canBreed()) {
				this.consumeItemFromStack(player, itemstack);
				this.setInLove(player);
				player.swing(hand, true);
				return true;
			}

			if (this.isChild()) {
				this.consumeItemFromStack(player, itemstack);
				this.ageUp((int) ((float) (-this.getGrowingAge() / 20) * 0.1F), true);
				return true;
			}
		}
		Item item = itemstack.getItem();
		if (item == BlockItems.PATROLPOINT.get()) {
			float f = this.getHealth();
			this.heal(25.0F);
			if (this.getHealth() == f) {
			} else {
				float f1 = 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
				this.playSound(SoundEvents.ENTITY_IRON_GOLEM_REPAIR, 1.0F, f1);
				if (!player.abilities.isCreativeMode) {
					itemstack.shrink(1);
				}
			}
		}
		if (item instanceof SpawnEggItem && ((SpawnEggItem) item).hasType(itemstack.getTag(), this.getType())) {
			if (!this.world.isRemote) {
				AgeableEntity ageableentity = this.func_241840_a((ServerWorld) this.world, this);
				if (ageableentity != null) {
					ageableentity.setGrowingAge(-24000);
					ageableentity.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), 0.0F, 0.0F);
					this.world.addEntity(ageableentity);
					if (itemstack.hasDisplayName()) {
						ageableentity.setCustomName(itemstack.getDisplayName());
					}

					this.onChildSpawnFromEgg(player, ageableentity);
					if (!player.abilities.isCreativeMode) {
						itemstack.shrink(1);
					}
				}
			}

			return true;
		} else {
			return false;
		}
	}

	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putInt("Age", this.getGrowingAge());
		compound.putInt("ForcedAge", this.forcedAge);
		compound.putInt("InLove", this.inLove);
		if (this.playerInLove != null) {
			compound.putUniqueId("LoveCause", this.playerInLove);
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.setGrowingAge(compound.getInt("Age"));
		this.forcedAge = compound.getInt("ForcedAge");
		this.inLove = compound.getInt("InLove");
		this.playerInLove = compound.hasUniqueId("LoveCause") ? compound.getUniqueId("LoveCause") : null;
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