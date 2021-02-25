package com.silverminer.moreore.common.objects.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BreakableBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.NameTagItem;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.List;
import java.util.Random;

import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.common.portal.Portal;
import com.silverminer.moreore.common.portal.PortalRegistry;
import com.silverminer.moreore.common.portal.Utils;
import com.silverminer.moreore.util.helpers.SpawnPositionHelper;

/**
 * Represents the actual portals in the center of the portal multiblock.
 */
public class SilverPortalBlock extends BreakableBlock {
	private static final VoxelShape X_AABB = Block.makeCuboidShape(6.0D, 0.0D, 0.0D, 10.0D, 16.0D, 16.0D);
	private static final VoxelShape Y_AABB = Block.makeCuboidShape(0.0D, 6.0D, 0.0D, 16.0D, 10.0D, 16.0D);
	private static final VoxelShape Z_AABB = Block.makeCuboidShape(0.0D, 0.0D, 6.0D, 16.0D, 16.0D, 10.0D);

	public static final EnumProperty<Axis> AXIS = EnumProperty.create("axis", Axis.class, Axis.X, Axis.Y, Axis.Z);

	public SilverPortalBlock() {
		// indestructible by normal means
		super(Block.Properties.create(Material.PORTAL).doesNotBlockMovement().noDrops().hardnessAndResistance(-1.0F)
				.setLightLevel((light) -> {
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

	private int timer = 0;

	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entity) {
		if (!worldIn.isRemote && entity.isAlive() && !entity.isPassenger() && !entity.isBeingRidden()
				&& entity.isNonBoss()
				&& VoxelShapes.compare(
						VoxelShapes.create(entity.getBoundingBox().offset((double) (-pos.getX()),
								(double) (-pos.getY()), (double) (-pos.getZ()))),
						state.getShape(worldIn, pos), IBooleanFunction.AND)) {

			RegistryKey<World> destination = null;
			MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
			if (worldIn instanceof ServerWorld) {
				server = worldIn.getServer();
			}
			List<Portal> affectedPortals = PortalRegistry.getPortalsAt(pos, worldIn.getDimensionKey());
			if (affectedPortals != null && !(affectedPortals.size() < 1)) {
				Portal firstPortal = affectedPortals.get(0);
				destination = firstPortal.getDestinationDimension();

				try {
					if (entity instanceof ItemEntity) {
						ItemEntity itemE = ((ItemEntity) entity);
						if (itemE.getItem().getItem() instanceof NameTagItem) {
							String text = itemE.getItem().getDisplayName().getUnformattedComponentText();
							RegistryKey<World> newDim = RegistryKey.getOrCreateKey(Registry.WORLD_KEY,
									new ResourceLocation(text));
							if (server.getWorld(newDim) != null && newDim != firstPortal.getDestinationDimension()
									&& newDim != World.THE_END) {
								PortalRegistry.unregister(worldIn, firstPortal);
								PortalRegistry.register(worldIn, (firstPortal.setDestinationDimension(newDim)));
								worldIn.getEntitiesWithinAABB(PlayerEntity.class,
										entity.getBoundingBox().grow(10.0D, 10.0D, 10.0D))
										.forEach(player -> player.sendMessage(
												new TranslationTextComponent("moreore.portal.set_dest_dim", newDim.getLocation()),
												entity.getUniqueID()));
								itemE.getItem().shrink(1);
								return;
							}
						}
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}

			RegistryKey<World> silverDim = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, MoreOre.SILVER_DIM_TYPE);
			if (destination == null) {
				destination = entity.world.getDimensionKey() == World.OVERWORLD ? silverDim : World.OVERWORLD;
			}
			if (entity.func_242280_ah())
				return;
			LOGGER.info("Destinantion: {}", destination);
			World newWorld = server.getWorld(destination);
			BlockPos destinationPos = SpawnPositionHelper.calculate(pos, newWorld);
			if (!Utils.teleportTo(entity, destination, destinationPos, server)) {
				if (entity instanceof PlayerEntity && timer == 0)
					((PlayerEntity) entity).sendMessage(new TranslationTextComponent("moreore.portal.dim_unaccessable"),
							entity.getUniqueID());
				timer += timer > 100 ? -timer : 1;
			} else {
				entity.func_242279_ag();
				if (entity instanceof PlayerEntity)
					((PlayerEntity) entity).sendMessage(new TranslationTextComponent("moreore.portal.teleported_to_dim",
							destination.getLocation().toString()), entity.getUniqueID());
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onReplaced(BlockState oldState, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!world.isRemote) {
			// Deactivate damaged portals.

			List<Portal> affectedPortals = PortalRegistry.getPortalsAt(pos, world.getDimensionKey());
			if (affectedPortals == null || affectedPortals.size() < 1) {
				return;
			}
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
		if (rand.nextInt(100) == 0) {
			world.playSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D,
					SoundEvents.BLOCK_PORTAL_AMBIENT, SoundCategory.BLOCKS, 0.5F, rand.nextFloat() * 0.4F + 0.8F,
					false);
		}

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

	@SuppressWarnings("deprecation")
	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player,
			Hand hand, BlockRayTraceResult hit) {
		if (player.getHeldItem(hand).getItem() instanceof NameTagItem) {
			List<Portal> affectedPortals = PortalRegistry.getPortalsAt(pos.toImmutable(), world.getDimensionKey());
			if (affectedPortals != null && !(affectedPortals.size() < 1)) {
				Portal firstPortal = affectedPortals.get(0);
				RegistryKey<World> dim = firstPortal.getDestinationDimension();
				String text = dim.getLocation().toString().replaceAll("minecraft:", "");
				if (!text.isEmpty()) {
					player.getHeldItem(hand).setDisplayName(new StringTextComponent(text));
					return ActionResultType.SUCCESS;
				}
			}
		}

		return super.onBlockActivated(state, world, pos, player, hand, hit);
	}
}