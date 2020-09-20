package com.silverminer.moreore.objects.entitys;

import com.silverminer.moreore.init.ModEntityTypesInit;
import com.silverminer.moreore.init.blocks.BiologicBlocks;
import com.silverminer.moreore.init.items.FootItems;
import com.silverminer.moreore.objects.blocks.NutBushBlock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class SquirrelEntity extends AnimalEntity {

	public SquirrelEntity(EntityType<? extends AnimalEntity> type, World worldIn) {
		super(type, worldIn);
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
		this.goalSelector.addGoal(0, new SwimGoal(this));
//		this.goalSelector.addGoal(1, new FollowParentGoal(this, 0.9D));
		this.goalSelector.addGoal(2, new BreedGoal(this, 0.9D));
		this.goalSelector.addGoal(3, new TemptGoal(this, 0.9D, Ingredient.fromItems(FootItems.LETUCE.get()), false));
		this.goalSelector.addGoal(4, new PanicGoal(this, 0.9D));
		this.goalSelector.addGoal(5, new EatNutGoal(0.9D, 25, 256));
	}

	/**
	 * Checks if the parameter is an item which this animal can be fed to breed it
	 * (wheat, carrots or seeds depending on the animal type)
	 */
	public boolean isBreedingItem(ItemStack stack) {
		return stack.getItem() == FootItems.LETUCE.get();
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

	public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
		return AnimalEntity.func_233666_p_().func_233815_a_(Attributes.field_233818_a_, 5.0D)
				.func_233815_a_(Attributes.field_233821_d_, 0.3D).func_233815_a_(Attributes.field_233820_c_, 1.0D)
				.func_233815_a_(Attributes.field_233823_f_, 7.0D);
	}

	public class EatNutGoal extends MoveToBlockGoal {
		protected int timer;

		public EatNutGoal(double speed, int searchlenght, int maxY) {
			super(SquirrelEntity.this, speed, searchlenght, maxY);
		}

		public double getTargetDistanceSq() {
			return 2.0D;
		}

		public boolean shouldMove() {
			return this.timeoutCounter % 100 == 0;
		}

		/**
		 * Return true to set given position as destination
		 */
		protected boolean shouldMoveTo(IWorldReader worldIn, BlockPos pos) {
			BlockState blockstate = worldIn.getBlockState(pos);
			return blockstate.getBlock() == BiologicBlocks.NUT_BUSH.get() && blockstate.get(NutBushBlock.AGE) >= 2;
		}

		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void tick() {
			if (this.getIsAboveDestination()) {
				if (this.timer >= 40) {
					this.eatNuts();
				} else {
					++this.timer;
				}
			} else if (!this.getIsAboveDestination() && SquirrelEntity.this.rand.nextFloat() < 0.05F) {
				SquirrelEntity.this.playSound(SoundEvents.ENTITY_FOX_SNIFF, 1.0F, 1.0F);
			}

			super.tick();
		}

		protected void eatNuts() {
			if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(SquirrelEntity.this.world,
					SquirrelEntity.this)) {
				BlockState blockstate = SquirrelEntity.this.world.getBlockState(this.destinationBlock);
				if (blockstate.getBlock() == BiologicBlocks.NUT_BUSH.get()) {
					int i = blockstate.get(NutBushBlock.AGE);
					blockstate.with(NutBushBlock.AGE, Integer.valueOf(1));
					int j = 1 + SquirrelEntity.this.world.rand.nextInt(2) + (i == 3 ? 1 : 0);
					ItemStack itemstack = SquirrelEntity.this.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
					if (itemstack.isEmpty()) {
						SquirrelEntity.this.setItemStackToSlot(EquipmentSlotType.MAINHAND,
								new ItemStack(FootItems.NUTS.get()));
						--j;
					}

					if (j > 0) {
						Block.spawnAsEntity(SquirrelEntity.this.world, this.destinationBlock,
								new ItemStack(FootItems.NUTS.get(), j));
					}

					SquirrelEntity.this.playSound(SoundEvents.ITEM_SWEET_BERRIES_PICK_FROM_BUSH, 1.0F, 1.0F);
					SquirrelEntity.this.world.setBlockState(this.destinationBlock,
							blockstate.with(NutBushBlock.AGE, Integer.valueOf(1)), 2);
				}
			}
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state
		 * necessary for execution in this method as well.
		 */
		public boolean shouldExecute() {
			return !SquirrelEntity.this.isSleeping() && super.shouldExecute();
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void startExecuting() {
			this.timer = 0;
			super.startExecuting();
		}
	}
}