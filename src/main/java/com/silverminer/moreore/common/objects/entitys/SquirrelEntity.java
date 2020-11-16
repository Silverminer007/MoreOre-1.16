package com.silverminer.moreore.common.objects.entitys;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.mojang.datafixers.util.Pair;
import com.silverminer.moreore.common.objects.blocks.NutBushLeavesBlock;
import com.silverminer.moreore.init.ModEntityTypesInit;
import com.silverminer.moreore.init.blocks.BiologicBlocks;
import com.silverminer.moreore.init.items.TreeItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.ClimberPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class SquirrelEntity extends AnimalEntity {
	private static final DataParameter<Boolean> IS_CLIMBING = EntityDataManager.createKey(SquirrelEntity.class,
			DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> IS_SITTING = EntityDataManager.createKey(SquirrelEntity.class,
			DataSerializers.BOOLEAN);
	/**
	 * Wheater the Squirrel eats the Nut or places it
	 */
	private static final DataParameter<Boolean> EAT_NUT = EntityDataManager.createKey(SquirrelEntity.class,
			DataSerializers.BOOLEAN);
	private static final DataParameter<Direction> NEAREST_CLIMBABLE_BLOCK = EntityDataManager
			.createKey(SquirrelEntity.class, DataSerializers.DIRECTION);
	private static final Predicate<ItemEntity> TRUSTED_TARGET_SELECTOR = (item) -> {
		return !item.cannotPickup() && item.isAlive() && item.getItem().getItem() == TreeItems.NUTS.get();
	};

	public SquirrelEntity(EntityType<? extends AnimalEntity> type, World worldIn) {
		super(type, worldIn);
	}

	/**
	 * Returns new PathNavigateGround instance
	 */
	protected PathNavigator createNavigator(World worldIn) {
		return new ClimberPathNavigator(this, worldIn);
	}

	@Override
	public AgeableEntity func_241840_a(ServerWorld worldIn, AgeableEntity ageable) {
		SquirrelEntity entity = new SquirrelEntity(ModEntityTypesInit.SQUIRREL.get(), worldIn);
		entity.onInitialSpawn(worldIn, worldIn.getDifficultyForLocation(new BlockPos(entity.getPositionVec())),
				SpawnReason.BREEDING, (ILivingEntityData) null, (CompoundNBT) null);
		return entity;
	}

	@Override
	public void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal());
		this.goalSelector.addGoal(1, new FollowParentGoal(this, 0.5D));
		this.goalSelector.addGoal(2, new BreedGoal(this, 0.5D));
		this.goalSelector.addGoal(3, new TemptGoal(this, 0.7D, Ingredient.fromItems(TreeItems.NUTS.get()), false));
		this.goalSelector.addGoal(4, new PanicGoal(this, 0.9D));

		this.goalSelector.addGoal(4,
				new AvoidEntityGoal<WolfEntity>(this, WolfEntity.class, 10, 0.5D, 1.0D, (entity) -> {
					return !this.isSitting() && !this.isSleeping();
				}));
		this.goalSelector.addGoal(4, new AvoidEntityGoal<CatEntity>(this, CatEntity.class, 10, 0.5D, 1.0D, (entity) -> {
			return !this.isSitting() && !this.isSleeping();
		}));
		this.goalSelector.addGoal(4,
				new AvoidEntityGoal<OcelotEntity>(this, OcelotEntity.class, 10, 0.5D, 1.0D, (entity) -> {
					return !this.isSitting() && !this.isSleeping();
				}));
		this.goalSelector.addGoal(4, new AvoidEntityGoal<FoxEntity>(this, FoxEntity.class, 10, 0.5D, 1.0D, (entity) -> {
			return !this.isSitting() && !this.isSleeping();
		}));
		this.goalSelector.addGoal(4,
				new AvoidEntityGoal<PolarBearEntity>(this, PolarBearEntity.class, 10, 0.5D, 1.0D, (entity) -> {
					return !this.isSitting() && !this.isSleeping();
				}));

		this.goalSelector.addGoal(5, new PlaceNutGoal(0.5D, 50, 256));
		this.goalSelector.addGoal(5, new EatNutGoal());
		this.goalSelector.addGoal(6, new FindNutGoal(0.5D, 50, 256));
		this.goalSelector.addGoal(6, new FindNutsFromGroundGoal());
		this.goalSelector.addGoal(7, new ClimbOnTreeGoal(0.5D, 50, 256));
		this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 0.5D, 0.1F));
	}

	protected void registerData() {
		super.registerData();
		this.dataManager.register(IS_CLIMBING, false);
		this.dataManager.register(IS_SITTING, false);
		this.dataManager.register(EAT_NUT, false);
		this.dataManager.register(NEAREST_CLIMBABLE_BLOCK, Direction.DOWN);
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void tick() {
		super.tick();
		if (this.getItemStackFromSlot(EquipmentSlotType.MAINHAND).getCount() == 0) {
			this.setItemStackToSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
		}
		if (!this.world.isRemote) {
			if (this.collidedHorizontally || this.collidedVertically) {
				this.setBesideClimbableBlock(true);
				this.setNearestDirection(Direction.NORTH);
				for (Direction d : Direction.getFacingDirections(this)) {
					BlockPos pos = this.getPosition().add(d.getDirectionVec());
					BlockState state = this.world.getBlockState(pos);
					if (state.getBlock().isIn(BlockTags.LOGS) || state.getBlock().isIn(BlockTags.LEAVES)) {
						this.setBesideClimbableBlock(true);
						this.setNearestDirection(d);
						return;
					}
				}
			}
			this.setBesideClimbableBlock(false);
			this.setNearestDirection(Direction.DOWN);
		}
	}

	/**
	 * Returns true if this entity should move as if it were on a ladder (either
	 * because it's actually on a ladder, or for AI reasons)
	 */
	public boolean isOnLadder() {
		return this.isBesideClimbableBlock();
	}

	public boolean isBesideClimbableBlock() {
		return this.dataManager.get(IS_CLIMBING);
	}

	public void setBesideClimbableBlock(boolean climbing) {
		this.dataManager.set(IS_CLIMBING, climbing);
	}

	public Direction getNearestDirection() {
		return this.dataManager.get(NEAREST_CLIMBABLE_BLOCK);
	}

	public void setNearestDirection(Direction d) {
		this.dataManager.set(NEAREST_CLIMBABLE_BLOCK, d);
	}

	public void resetPosition() {
		this.setStitting(false);
	}

	public void setSitting() {
		this.setStitting(true);
	}

	public void setStitting(boolean sitting) {
		this.dataManager.set(IS_SITTING, sitting);
	}

	public boolean isNormal() {
		return !this.isSitting();
	}

	public boolean isSitting() {
		return this.dataManager.get(IS_SITTING);
	}

	public boolean isEatingNut() {
		return this.dataManager.get(EAT_NUT);
	}

	public void setEatingNut(boolean eatingNut) {
		this.dataManager.set(EAT_NUT, eatingNut);
	}

	public double getPosYEye() {
		return this.isSitting() ? super.getPosYEye() + 0.2D : super.getPosYEye();
	}

	/**
	 * Checks if the parameter is an item which this animal can be fed to breed it
	 * (wheat, carrots or seeds depending on the animal type)
	 */
	public boolean isBreedingItem(ItemStack stack) {
		return stack.getItem() == TreeItems.NUTS.get();
	}

	public boolean processInteract(PlayerEntity player, Hand hand) {
		ItemStack itemstack = player.getHeldItem(hand);
		if (this.isBreedingItem(itemstack)) {
			if (this.setInLove(itemstack, player, hand))
				return true;
		}
		Item item = itemstack.getItem();
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

	private boolean setInLove(ItemStack item, @Nullable PlayerEntity player, @Nullable Hand hand) {
		if (!this.world.isRemote && this.getGrowingAge() == 0 && this.canBreed()) {
			if (player != null)
				this.consumeItemFromStack(player, item);
			this.setInLove(player);
			if (player != null && hand != null)
				player.swing(hand, true);
			return true;
		}

		if (this.isChild()) {
			if (player != null)
				this.consumeItemFromStack(player, item);
			else
				this.getItemStackFromSlot(EquipmentSlotType.MAINHAND).shrink(1);
			this.ageUp((int) ((float) (-this.getGrowingAge() / 20) * 0.1F), true);
			return true;
		}
		return false;
	}

	public static AttributeModifierMap setCustomAttributes() {
		return AnimalEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 6.0D)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.5D)
				.createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 2.0D).create();
	}

	public void writeAdditional(CompoundNBT compound) {
		compound.putBoolean("EatNut", this.isEatingNut());
		super.writeAdditional(compound);
	}

	public void readAdditional(CompoundNBT compound) {
		if (compound.contains("EatNut", 1)) {
			this.setEatingNut(compound.getBoolean("EatNut"));
		}
		super.readAdditional(compound);
	}

	public void generateWillEatNut() {
		// Die Tempetaur durch 2.1 teilen um bei einer temperatur von 1.0 ca. 50 %
		// wahrscheinlichkeit vom essen zu haben, aber bei 2.0 (Wüste) nicht bei 0%
		// plazieren zu sein
		this.setEatingNut(
				this.getRNG().nextFloat() <= (this.world.getBiome(this.getPosition()).getTemperature() / 2.1));
	}

	public class FindNutGoal extends MoveToBlockGoal {
		protected int timer;

		public FindNutGoal(double speed, int searchlenght, int maxY) {
			super(SquirrelEntity.this, speed, searchlenght, maxY);
		}

		/**
		 * Return true to set given position as destination
		 */
		protected boolean shouldMoveTo(IWorldReader worldIn, BlockPos pos) {
			BlockState blockstate = worldIn.getBlockState(pos);
			boolean flag = blockstate.getBlock() == BiologicBlocks.NUT_BUSH_LEAVES.get();
			if (flag) {
				flag = blockstate.get(NutBushLeavesBlock.AGE) == 1;
			}
			return flag && worldIn.getBlockState(pos.up()).getBlock() == Blocks.AIR;
		}

		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void tick() {
			if (!this.shouldMoveTo(this.creature.world, this.destinationBlock)) {
				this.searchForDestination();
			}
			super.tick();
			if (this.getIsAboveDestination()) {
				if (this.timer >= 40) {
					this.pickNuts();
				} else {
					++this.timer;
				}
			}
		}

		protected void pickNuts() {
			if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(SquirrelEntity.this.world,
					this.creature)) {
				BlockState blockstate = this.creature.world.getBlockState(this.destinationBlock);
				if (blockstate.getBlock() == BiologicBlocks.NUT_BUSH_LEAVES.get()) {
					int j = 1 + this.creature.world.rand.nextInt(2);
					ItemStack itemstack = SquirrelEntity.this.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
					if (itemstack.isEmpty()) {
						this.creature.setItemStackToSlot(EquipmentSlotType.MAINHAND,
								new ItemStack(TreeItems.NUTS.get()));
						SquirrelEntity.this.generateWillEatNut();
						--j;
					}

					if (j > 0) {
						Block.spawnAsEntity(SquirrelEntity.this.world, this.destinationBlock,
								new ItemStack(TreeItems.NUTS.get(), j));
					}

					SquirrelEntity.this.playSound(SoundEvents.ITEM_SWEET_BERRIES_PICK_FROM_BUSH, 1.0F, 1.0F);
					SquirrelEntity.this.world.setBlockState(this.destinationBlock,
							blockstate.with(NutBushLeavesBlock.AGE, 0), 2);
				}
			}
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state
		 * necessary for execution in this method as well.
		 */
		public boolean shouldExecute() {
			return !SquirrelEntity.this.isSitting()
					&& SquirrelEntity.this.getItemStackFromSlot(EquipmentSlotType.MAINHAND) == ItemStack.EMPTY
					&& !SquirrelEntity.this.isSleeping() && super.shouldExecute();
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void startExecuting() {
			this.timer = 0;
			super.startExecuting();
		}
	}

	public class ClimbOnTreeGoal extends MoveToBlockGoal {
		protected int timer;
		protected BlockPos lastTree;

		public ClimbOnTreeGoal(double speed, int searchlenght, int maxY) {
			super(SquirrelEntity.this, speed, searchlenght, maxY);
		}

		/**
		 * Return true to set given position as destination
		 */
		protected boolean shouldMoveTo(IWorldReader worldIn, BlockPos pos) {
			Block block = worldIn.getBlockState(pos).getBlock();
			boolean rightBlock = (block.isIn(BlockTags.LOGS) || block.isIn(BlockTags.LEAVES));
			boolean lastTreeAway = (this.lastTree != null ? !pos.withinDistance(this.lastTree, 10) : true);
			boolean air = (worldIn.getBlockState(pos.up()).getBlock() == Blocks.AIR);
			return rightBlock && lastTreeAway && air;
		}

		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void tick() {
			if (!this.shouldMoveTo(this.creature.world, this.destinationBlock)
					|| this.destinationBlock == BlockPos.ZERO) {
				this.searchForDestination();
			}
			super.tick();
			if (this.getIsAboveDestination()) {
				if (this.timer >= 200) {
					this.lastTree = this.creature.getPosition();
					this.searchForDestination();
					this.timer = 0;
				} else {
					++this.timer;
				}
			}
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state
		 * necessary for execution in this method as well.
		 */
		public boolean shouldExecute() {
			return !SquirrelEntity.this.isSitting()
					&& SquirrelEntity.this.getItemStackFromSlot(EquipmentSlotType.MAINHAND) == ItemStack.EMPTY
					&& !SquirrelEntity.this.isSleeping() && super.shouldExecute();
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void startExecuting() {
			this.timer = 0;
			this.destinationBlock = BlockPos.ZERO;
			super.startExecuting();
		}
	}

	public class PlaceNutGoal extends MoveToBlockGoal {
		protected int timer;
		protected int searchLength2;
		protected boolean hasDestination = false;

		public PlaceNutGoal(double speed, int searchlenght, int maxY) {
			super(SquirrelEntity.this, speed, searchlenght, maxY);
			this.searchLength2 = searchlenght;
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state
		 * necessary for execution in this method as well.
		 */
		public boolean shouldExecute() {
			return SquirrelEntity.this.getItemStackFromSlot(EquipmentSlotType.MAINHAND).getItem() == TreeItems.NUTS
					.get() && !SquirrelEntity.this.isSleeping() && super.shouldExecute()
					&& !SquirrelEntity.this.isEatingNut() && !SquirrelEntity.this.isSitting();
		}

		/**
		 * Return true to set given position as destination
		 */
		protected boolean shouldMoveTo(IWorldReader worldIn, BlockPos pos) {
			return worldIn.getBlockState(pos).getBlock().isIn(BlockTags.BAMBOO_PLANTABLE_ON)
					&& worldIn.getBlockState(pos.up()).getBlock() == Blocks.AIR;
		}

		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void tick() {
			super.tick();
			if (this.getIsAboveDestination()) {
				if (this.timer >= 50) {
					this.placeNuts();
				} else {
					++this.timer;
					this.addBlockParticles(SquirrelEntity.this.world.getBlockState(this.destinationBlock), 15,
							SquirrelEntity.this);
				}
			}
		}

		protected void placeNuts() {
			if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(SquirrelEntity.this.world, this.creature)
					&& this.creature.getItemStackFromSlot(EquipmentSlotType.MAINHAND).getItem() == TreeItems.NUTS
							.get()) {
				this.creature.world.setBlockState(this.func_241846_j(),
						BiologicBlocks.NUT_BUSH_SAPLING.get().getDefaultState());
				this.creature.getItemStackFromSlot(EquipmentSlotType.MAINHAND).shrink(1);
				SquirrelEntity.this.generateWillEatNut();
			}
		}

		public void startExecuting() {
			this.timer = 0;
			this.hasDestination = false;
			super.startExecuting();
		}

		/**
		 * Searches and sets new destination block and returns true if a suitable block
		 * (specified in
		 * {@link net.minecraft.entity.ai.EntityAIMoveToBlock#shouldMoveTo(World, BlockPos)
		 * EntityAIMoveToBlock#shouldMoveTo(World, BlockPos)}) can be found.
		 */
		protected boolean searchForDestination() {
			if (this.hasDestination)
				return true;
			int i = this.searchLength2;
			BlockPos blockpos = this.creature.getPosition();
			BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

			for (int k = this.field_203112_e; k <= 256; k = k > 0 ? -k : 1 - k) {
				for (int l = 0; l < i; ++l) {
					for (int i1 = 0; i1 <= l; i1 = i1 > 0 ? -i1 : 1 - i1) {
						for (int j1 = i1 < l && i1 > -l ? l : 0; j1 <= l; j1 = j1 > 0 ? -j1 : 1 - j1) {
							blockpos$mutable.setAndOffset(blockpos, i1, k - 1, j1);
							if (this.creature.isWithinHomeDistanceFromPosition(blockpos$mutable)
									&& this.shouldMoveTo(this.creature.world, blockpos$mutable)
									&& this.creature.getRNG().nextFloat() < (this.timer++ / Math.pow(i * 2, 3))) {
								this.destinationBlock = blockpos$mutable;
								this.hasDestination = true;
								return true;
							}
						}
					}
				}
			}

			return false;
		}

		private void addBlockParticles(BlockState block, int count, SquirrelEntity squirrel) {
			for (int i = 0; i < count; ++i) {
				Vector3d vector3d = new Vector3d(((double) squirrel.rand.nextFloat() - 0.5D) * 0.1D,
						Math.random() * 0.1D + 0.1D, 0.0D);
				vector3d = vector3d.rotatePitch(-squirrel.rotationPitch * ((float) Math.PI / 180F));
				vector3d = vector3d.rotateYaw(-squirrel.rotationYaw * ((float) Math.PI / 180F));
				double d0 = (double) (-squirrel.rand.nextFloat()) * 0.6D - 0.3D;
				Vector3d vector3d1 = new Vector3d(((double) squirrel.rand.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
				vector3d1 = vector3d1.rotatePitch(-squirrel.rotationPitch * ((float) Math.PI / 180F));
				vector3d1 = vector3d1.rotateYaw(-squirrel.rotationYaw * ((float) Math.PI / 180F));
				vector3d1 = vector3d1.add(squirrel.getPosX(), squirrel.getPosY(), squirrel.getPosZ());
				// Forge: Fix MC-2518 spawnParticle is nooped on server, need to use server
				// specific variant
				if (squirrel.world instanceof ServerWorld)
					((ServerWorld) squirrel.world).spawnParticle(new BlockParticleData(ParticleTypes.BLOCK, block),
							vector3d1.x, vector3d1.y, vector3d1.z, 1, vector3d.x, vector3d.y + 0.05D, vector3d.z, 0.0D);
				else
					squirrel.world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, block), vector3d1.x,
							vector3d1.y, vector3d1.z, vector3d.x, vector3d.y + 0.05D, vector3d.z);
			}
		}
	}

	public class EatNutGoal extends Goal {
		protected int nutTimer;
		protected int timer;

		public EatNutGoal() {
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state
		 * necessary for execution in this method as well.
		 */
		public boolean shouldExecute() {
			return SquirrelEntity.this.getItemStackFromSlot(EquipmentSlotType.MAINHAND).getItem() == TreeItems.NUTS
					.get() && !SquirrelEntity.this.isSleeping() && SquirrelEntity.this.isEatingNut();
		}

		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void tick() {
			super.tick();
			if (SquirrelEntity.this.isEatingNut()) {
				SquirrelEntity.this.setSitting();
				this.eatNut();
			}
		}

		protected void eatNut() {
			if (SquirrelEntity.this.getItemStackFromSlot(EquipmentSlotType.MAINHAND).getItem() == TreeItems.NUTS
					.get()) {
				this.nutTimer++;
				SquirrelEntity squirrel = SquirrelEntity.this;
				if (this.nutTimer >= 80) {
					this.onFoodEaten(squirrel.getEntityWorld(),
							squirrel.getItemStackFromSlot(EquipmentSlotType.MAINHAND), squirrel);
					squirrel.heal(0.5F);
					squirrel.getItemStackFromSlot(EquipmentSlotType.MAINHAND).shrink(1);
					squirrel.resetPosition();
					SquirrelEntity.this.generateWillEatNut();
				} else {
					this.addItemParticles(SquirrelEntity.this.getItemStackFromSlot(EquipmentSlotType.MAINHAND), 5,
							squirrel);
					this.playSound(
							squirrel.getEatSound(SquirrelEntity.this.getItemStackFromSlot(EquipmentSlotType.MAINHAND)),
							0.5F + 0.5F * (float) squirrel.rand.nextInt(2),
							(squirrel.rand.nextFloat() - squirrel.rand.nextFloat()) * 0.2F + 1.0F, squirrel);
				}
			}
		}

		public ItemStack onFoodEaten(World world, ItemStack item, SquirrelEntity entity) {
			if (item.isFood()) {
				world.playSound((PlayerEntity) null, entity.getPosX(), entity.getPosY(), entity.getPosZ(),
						entity.getEatSound(item), SoundCategory.NEUTRAL, 1.0F,
						1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.4F);
				this.applyFoodEffects(item, world, entity);
				item.shrink(1);
			}
			return item;
		}

		private void applyFoodEffects(ItemStack itemStack, World world, LivingEntity entity) {
			Item item = itemStack.getItem();
			if (item.isFood()) {
				for (Pair<EffectInstance, Float> pair : item.getFood().getEffects()) {
					if (!world.isRemote && pair.getFirst() != null && world.rand.nextFloat() < pair.getSecond()) {
						entity.addPotionEffect(new EffectInstance(pair.getFirst()));
					}
				}
			}
		}

		private void addItemParticles(ItemStack stack, int count, SquirrelEntity squirrel) {
			for (int i = 0; i < count; ++i) {
				Vector3d vector3d = new Vector3d(((double) squirrel.rand.nextFloat() - 0.5D) * 0.1D - 0.15,
						Math.random() * 0.1D + 0.1D, 0.3D);
				vector3d = vector3d.rotatePitch(-squirrel.rotationPitch * ((float) Math.PI / 180F));
				vector3d = vector3d.rotateYaw(-squirrel.rotationYaw * ((float) Math.PI / 180F));
				double d0 = (double) (-squirrel.rand.nextFloat()) * 0.6D - 0.3D;
				Vector3d vector3d1 = new Vector3d(((double) squirrel.rand.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
				vector3d1 = vector3d1.rotatePitch(-squirrel.rotationPitch * ((float) Math.PI / 180F));
				vector3d1 = vector3d1.rotateYaw(-squirrel.rotationYaw * ((float) Math.PI / 180F));
				vector3d1 = vector3d1.add(squirrel.getPosX(), squirrel.getPosYEye(), squirrel.getPosZ());
				// Forge: Fix MC-2518 spawnParticle is nooped on server, need to use server
				// specific variant
				if (squirrel.world instanceof ServerWorld)
					((ServerWorld) squirrel.world).spawnParticle(new ItemParticleData(ParticleTypes.ITEM, stack),
							vector3d1.x, vector3d1.y, vector3d1.z, 1, vector3d.x, vector3d.y + 0.05D, vector3d.z, 0.0D);
				else
					squirrel.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, stack), vector3d1.x,
							vector3d1.y, vector3d1.z, vector3d.x, vector3d.y + 0.05D, vector3d.z);
			}
		}

		public void playSound(SoundEvent soundIn, float volume, float pitch, SquirrelEntity squirrel) {
			if (!squirrel.isSilent()) {
				squirrel.world.playSound((PlayerEntity) null, squirrel.getPosX(), squirrel.getPosY(),
						squirrel.getPosZ(), soundIn, squirrel.getSoundCategory(), volume, pitch);
			}
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void startExecuting() {
			this.nutTimer = 0;
		}
	}

	public class SwimGoal extends net.minecraft.entity.ai.goal.SwimGoal {
		public SwimGoal() {
			super(SquirrelEntity.this);
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state
		 * necessary for execution in this method as well.
		 */
		public boolean shouldExecute() {
			return SquirrelEntity.this.isInWater() && SquirrelEntity.this.func_233571_b_(FluidTags.WATER) > 0.25D
					|| SquirrelEntity.this.isInLava();
		}
	}

	public class FindNutsFromGroundGoal extends Goal {
		public FindNutsFromGroundGoal() {
			this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state
		 * necessary for execution in this method as well.
		 */
		public boolean shouldExecute() {
			if (SquirrelEntity.this.isSitting()) {
				return false;
			}
			if (!SquirrelEntity.this.getItemStackFromSlot(EquipmentSlotType.MAINHAND).isEmpty()) {
				return false;
			} else {
				if (SquirrelEntity.this.getRNG().nextInt(10) != 0) {
					return false;
				} else {
					List<ItemEntity> list = SquirrelEntity.this.world.getEntitiesWithinAABB(ItemEntity.class,
							SquirrelEntity.this.getBoundingBox().grow(8.0D, 8.0D, 8.0D),
							SquirrelEntity.TRUSTED_TARGET_SELECTOR);
					return !list.isEmpty()
							&& SquirrelEntity.this.getItemStackFromSlot(EquipmentSlotType.MAINHAND).isEmpty();
				}
			}
		}

		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void tick() {
			List<ItemEntity> list = SquirrelEntity.this.world.getEntitiesWithinAABB(ItemEntity.class,
					SquirrelEntity.this.getBoundingBox().grow(8.0D, 8.0D, 8.0D),
					SquirrelEntity.TRUSTED_TARGET_SELECTOR);
			ItemStack itemstack = SquirrelEntity.this.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
			if (itemstack.isEmpty() && !list.isEmpty()) {
				SquirrelEntity.this.getNavigator().tryMoveToEntityLiving(list.get(0), 0.5D);
			}
			if (itemstack.isEmpty() && SquirrelEntity.this.getDistanceSq(list.get(0)) <= 1.0F) {
				SquirrelEntity.this.setItemStackToSlot(EquipmentSlotType.MAINHAND, list.get(0).getItem());
				SquirrelEntity.this.generateWillEatNut();
				list.get(0).remove();
			}
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void startExecuting() {
			List<ItemEntity> list = SquirrelEntity.this.world.getEntitiesWithinAABB(ItemEntity.class,
					SquirrelEntity.this.getBoundingBox().grow(8.0D, 8.0D, 8.0D),
					SquirrelEntity.TRUSTED_TARGET_SELECTOR);
			if (!list.isEmpty()) {
				SquirrelEntity.this.getNavigator().tryMoveToEntityLiving(list.get(0), 0.5D);
			}
		}
	}

	public class FollowParentGoal extends net.minecraft.entity.ai.goal.FollowParentGoal {
		public FollowParentGoal(AnimalEntity animal, double speed) {
			super(animal, speed);
		}

		public boolean shouldExecute() {
			return super.shouldExecute() && !SquirrelEntity.this.isSitting();
		}
	}

	public class BreedGoal extends net.minecraft.entity.ai.goal.BreedGoal {
		public BreedGoal(AnimalEntity animal, double speed) {
			super(animal, speed);
		}

		public boolean shouldExecute() {
			return super.shouldExecute() && !SquirrelEntity.this.isSitting();
		}
	}

	public class PanicGoal extends net.minecraft.entity.ai.goal.PanicGoal {
		public PanicGoal(AnimalEntity animal, double speed) {
			super(animal, speed);
		}

		public boolean shouldExecute() {
			return super.shouldExecute() && !SquirrelEntity.this.isSitting();
		}
	}

	public class TemptGoal extends net.minecraft.entity.ai.goal.TemptGoal {
		public TemptGoal(CreatureEntity creatureIn, double speedIn, Ingredient temptItemsIn,
				boolean scaredByPlayerMovementIn) {
			this(creatureIn, speedIn, scaredByPlayerMovementIn, temptItemsIn);
		}

		public TemptGoal(CreatureEntity creatureIn, double speedIn, boolean scaredByPlayerMovementIn,
				Ingredient temptItemsIn) {
			super(creatureIn, speedIn, scaredByPlayerMovementIn, temptItemsIn);
		}

		public boolean shouldExecute() {
			return super.shouldExecute() && !SquirrelEntity.this.isSitting();
		}
	}

	public class WaterAvoidingRandomWalkingGoal extends net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal {
		public WaterAvoidingRandomWalkingGoal(CreatureEntity creature, double speedIn) {
			this(creature, speedIn, 0.001F);
		}

		public WaterAvoidingRandomWalkingGoal(CreatureEntity creature, double speedIn, float probabilityIn) {
			super(creature, speedIn, probabilityIn);
		}

		public boolean shouldExecute() {
			return super.shouldExecute() && !SquirrelEntity.this.isSitting();
		}
	}
}