package com.silverminer.moreore.objects.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BreakableBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.List;
import java.util.Random;

import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.init.items.OreItems;
import com.silverminer.moreore.common.portal.Config;
import com.silverminer.moreore.common.portal.Utils;
import com.silverminer.moreore.util.helpers.SpawnPositionHelper;
import com.silverminer.moreore.common.portal.registration.Portal;
import com.silverminer.moreore.common.portal.registration.PortalRegistry;

/**
 * Represents the actual portals in the center of the portal multiblock.
 */
public class SilverPortalBlock extends BreakableBlock {
	private static final VoxelShape X_AABB = Block.makeCuboidShape(6.0D, 0.0D, 0.0D, 10.0D, 16.0D, 16.0D);
	private static final VoxelShape Y_AABB = Block.makeCuboidShape(0.0D, 6.0D, 0.0D, 16.0D, 10.0D, 16.0D);
	private static final VoxelShape Z_AABB = Block.makeCuboidShape(0.0D, 0.0D, 6.0D, 16.0D, 16.0D, 10.0D);

	public static final EnumProperty<Axis> AXIS = EnumProperty.create("axis", Axis.class, Axis.X, Axis.Y, Axis.Z);

	public SilverPortalBlock() {
		super(Block.Properties.create(Material.PORTAL).doesNotBlockMovement().noDrops().hardnessAndResistance(-1.0F) // indestructible
																														// by
																														// normal
																														// means
				.func_235838_a_((light) -> {
					return 11;
				}).sound(SoundType.GLASS));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> stateBuilder) {
		stateBuilder.add(AXIS);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader blockReader, BlockPos pos, ISelectionContext selection) {
		Axis portalAxis = state.get(AXIS);

		switch (portalAxis) {
		case Y:
			return Y_AABB;
		case Z:
			return Z_AABB;
		case X:
		default:
			return X_AABB;
		}
	}

	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entity) {
		if (!worldIn.isRemote && entity.isAlive() && !entity.isPassenger() && !entity.isBeingRidden()
				&& entity.isNonBoss()
				&& VoxelShapes.compare(
						VoxelShapes.create(entity.getBoundingBox().offset((double) (-pos.getX()),
								(double) (-pos.getY()), (double) (-pos.getZ()))),
						state.getShape(worldIn, pos), IBooleanFunction.AND)) {
			if (entity.func_242280_ah())
				return;
			RegistryKey<World> dim = entity.world.func_234923_W_() == RegistryKey
					.func_240903_a_(Registry.field_239699_ae_, MoreOre.SILVER_DIM_TYPE) ? World.field_234918_g_
							: RegistryKey.func_240903_a_(Registry.field_239699_ae_, MoreOre.SILVER_DIM_TYPE);
			MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
			if(worldIn instanceof ServerWorld) {
				server = worldIn.getServer();
			}
			World world = server.getWorld(dim);
			BlockPos destinationPos = SpawnPositionHelper.calculate(pos, world);
			if (world.getWorldInfo().isHardcore()) {
				List<Portal> affectedPortals = PortalRegistry.getPortalsAt(pos, world.func_234923_W_());
				if (affectedPortals == null || affectedPortals.size() < 1)
					return;
				Portal firstPortal = affectedPortals.get(0);
				if (entity instanceof ServerPlayerEntity) {
					if (PortalRegistry.getPower(firstPortal) > 0) {
						Utils.teleportTo(entity, dim, destinationPos, Direction.NORTH);
						PortalRegistry.removePower(firstPortal, 1);
					}
				} else if (entity instanceof ItemEntity) {
					Item item = ((ItemEntity) entity).getItem().getItem();
					if (item == OreItems.RAINBOW_RUNE.get()) {
						PortalRegistry.addPower(firstPortal, 1);
						entity.remove();
					}
				}
			} else {
				Utils.teleportTo(entity, dim, destinationPos, Direction.NORTH);
			}
			entity.func_242279_ag();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onReplaced(BlockState oldState, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!world.isRemote) {
			// Deactivate damaged portals.

			List<Portal> affectedPortals = PortalRegistry.getPortalsAt(pos, world.func_234923_W_());
			if (affectedPortals == null || affectedPortals.size() < 1)
				return;
			Portal firstPortal = affectedPortals.get(0);

			if (firstPortal.isDamaged(world)) {
				PortalRegistry.deactivatePortal(world, pos);
			}
		}

		super.onReplaced(oldState, world, pos, newState, isMoving);
	}

	@Override
	public ItemStack getItem(IBlockReader reader, BlockPos pos, BlockState state) {
		return ItemStack.EMPTY;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		if (Config.ambientSoundEnabled.get() && rand.nextInt(100) == 0) {
			world.playSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D,
					SoundEvents.BLOCK_PORTAL_AMBIENT, SoundCategory.BLOCKS, 0.5F, rand.nextFloat() * 0.4F + 0.8F,
					false);
		}

		if (Config.particlesEnabled.get()) {
			for (int i = 0; i < 4; ++i) {
				double d0 = (double) ((float) pos.getX() + rand.nextFloat());
				double d1 = (double) ((float) pos.getY() + rand.nextFloat());
				double d2 = (double) ((float) pos.getZ() + rand.nextFloat());
				double d3 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
				double d4 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
				double d5 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
				int j = rand.nextInt(2) * 2 - 1;

				if (world.getBlockState(pos.west()).getBlock() != this
						&& world.getBlockState(pos.east()).getBlock() != this) {
					d0 = (double) pos.getX() + 0.5D + 0.25D * (double) j;
					d3 = (double) (rand.nextFloat() * 2.0F * (float) j);
				} else {
					d2 = (double) pos.getZ() + 0.5D + 0.25D * (double) j;
					d5 = (double) (rand.nextFloat() * 2.0F * (float) j);
				}

				world.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
			}
		}
	}
}