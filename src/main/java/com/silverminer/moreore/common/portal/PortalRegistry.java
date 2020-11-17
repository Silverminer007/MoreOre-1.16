package com.silverminer.moreore.common.portal;

import com.google.common.collect.*;
import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.init.blocks.InitBlocks;
import com.silverminer.moreore.common.objects.blocks.SilverPortalBlock;
import com.silverminer.moreore.common.objects.blocks.SilverPortalFrameBlock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The central registration for all portals.
 */
public final class PortalRegistry {
	private static ImmutableMap<Direction, Direction[]> cornerSearchDirs;
	private static ListMultimap<BlockPos, Portal> portals;

	static {
		EnumMap<Direction, Direction[]> temp = Maps.newEnumMap(Direction.class);
		temp.put(Direction.DOWN, new Direction[] { Direction.SOUTH, Direction.EAST });
		temp.put(Direction.UP, new Direction[] { Direction.SOUTH, Direction.EAST });
		temp.put(Direction.NORTH, new Direction[] { Direction.DOWN, Direction.EAST });
		temp.put(Direction.SOUTH, new Direction[] { Direction.DOWN, Direction.EAST });
		temp.put(Direction.WEST, new Direction[] { Direction.DOWN, Direction.SOUTH });
		temp.put(Direction.EAST, new Direction[] { Direction.DOWN, Direction.SOUTH });
		cornerSearchDirs = Maps.immutableEnumMap(temp);

		portals = ArrayListMultimap.create();
	}

	/**
	 * :WARNING: Completely clears the registry. :WARNING: This does not deactivate
	 * any portals, meaning no portals blocks will get removed.
	 */
	public static void clear() {
		portals.clear();

		MoreOre.portalSaveData.markDirty();
	}

	/**
	 * Activate a portal at the specified position.
	 * 
	 * @param world The {@link World} the portal is located in.
	 * @param pos   The {@link BlockPos} of the a portal frame.
	 * @param side  The {@link Direction} representing the side of the portal frame
	 *              that was hit by the portal activator.
	 * @return <code>true</code> if a portal could be activated, otherwise
	 *         <code>false</code>.
	 */
	public static boolean activatePortal(World world, BlockPos pos, Direction side) {
		if (world == null || pos == null || side == null)
			return false;

		Corner corner = null;
		Corner corner1 = null;
		Corner corner2 = null;
		Corner corner3 = null;
		Corner corner4 = null;
		Direction firstSearchDir = null;

		// Find corners

		for (Direction searchDir : cornerSearchDirs.get(side)) {
			corner = findCorner(world, pos, searchDir, side);

			if (corner != null) {
				firstSearchDir = searchDir;
				corner1 = corner;
				break;
			}
		}
		if (corner1 == null)
			return false;

		corner2 = findCorner(world, pos, firstSearchDir.getOpposite(), side);
		if (corner2 == null)
			return false;

		corner3 = findCorner(world, corner1.getPos().offset(side), side, firstSearchDir.getOpposite());
		if (corner3 == null)
			return false;

		corner4 = findCorner(world, corner3.getPos().offset(firstSearchDir.getOpposite()), firstSearchDir.getOpposite(),
				side.getOpposite());
		if (corner4 == null || !corner4.equals(findCorner(world, corner2.getPos().offset(side), side, firstSearchDir)))
			return false;

		// Check size
		if (getDistance(corner1.getPos(), corner2.getPos()) > 10
				|| getDistance(corner1.getPos(), corner3.getPos()) > 10)
			return false;

		// Check address blocks validity

		BlockState addBlock1 = world.getBlockState(corner1.getPos());

		if (!isValidAddressBlock(addBlock1))
			return false;

		BlockState addBlock2 = world.getBlockState(corner2.getPos());

		if (!isValidAddressBlock(addBlock2))
			return false;

		BlockState addBlock3 = world.getBlockState(corner3.getPos());

		if (!isValidAddressBlock(addBlock3))
			return false;

		BlockState addBlock4 = world.getBlockState(corner4.getPos());

		if (!isValidAddressBlock(addBlock4))
			return false;
		// Determine portal axis

		int corner1Y = corner1.getPos().getY();

		boolean isHorizontal = (corner1Y == corner2.getPos().getY() && corner1Y == corner3.getPos().getY()
				&& corner1Y == corner4.getPos().getY());

		// Only relevant for vertical portals.
		Axis horizontalCornerFacing = (corner1.getFacingA().getAxis() != Axis.Y) ? corner1.getFacingA().getAxis()
				: corner1.getFacingB().getAxis();

		Axis portalAxis = isHorizontal ? Axis.Y : Utils.getOrthogonalTo(horizontalCornerFacing);

		// Create portal data structure
		Portal portal = new Portal(world.getDimensionKey(), portalAxis, corner1, corner2, corner3, corner4);
		portal.setDestinationDimension(world.getDimensionKey() == World.OVERWORLD
				? RegistryKey.getOrCreateKey(Registry.WORLD_KEY, MoreOre.SILVER_DIM_TYPE)
				: World.OVERWORLD);

		Iterable<BlockPos> portalPositions = portal.getPortalPositions();

		// Ensure that the inside of the frame only contains air blocks
		for (BlockPos checkPos : portalPositions) {
			if (!world.isAirBlock(checkPos))
				return false;
		}

		// Place portal blocks

		for (BlockPos portalPos : portalPositions) {
			world.setBlockState(portalPos,
					InitBlocks.SILVER_PORTAL.get().getDefaultState().with(SilverPortalBlock.AXIS, portalAxis));
		}

		// Register portal

		register(world, portal);

		return true;
	}

	/**
	 * Deactivate the portal at the specified position.<br>
	 * If multiple portals share the same portal frame block, all those portals get
	 * deactivated.
	 * 
	 * @param world The {@link World} the portal is located in.
	 * @param pos   The {@link BlockPos} of one of the portals blocks (portal or
	 *              frame).
	 */
	public static void deactivatePortal(World world, BlockPos pos) {
		if (world == null || pos == null)
			return;

		// Copying of the list is required because unregister() changes it.
		List<Portal> affectedPortals = new ArrayList<>(portals.get(pos));

		// Unregister all affected portals first to avoid unnecessary deactivatePortal()
		// calls that would otherwise be caused by the destruction of the portal blocks.
		for (Portal portal : affectedPortals)
			unregister(world, portal);
		for (Portal portal : affectedPortals)
			destroyPortalBlocks(world, portal);
	}

	/**
	 * Determines if there is a portal at the specified position in the specified
	 * dimension.
	 * 
	 * @param pos       The {@link BlockPos} of a portal or frame block.
	 * @param dimension The dimension the portal is supposed to be in.
	 * @return <code>true</code> if the block at the specified position is part of a
	 *         registered portal, otherwise <code>false</code>.
	 */
	public static boolean isPortalAt(BlockPos pos, RegistryKey<World> dimension) {
		List<Portal> portals = getPortalsAt(pos, dimension);
		return (portals != null) && (portals.size() > 0);
	}

	/**
	 * Gets all registered portals and their positions.
	 *
	 * @return An immutable map containing all portals and their positions.
	 */
	public static ImmutableListMultimap<BlockPos, Portal> getPortals() {
		return ImmutableListMultimap.copyOf(portals);
	}

	/**
	 * Gets the portals registered at the specified position in the specified
	 * dimension.
	 * 
	 * @param pos       The {@link BlockPos} of a portal or frame block.
	 * @param dimension The dimension the portals should be in.
	 * @return A read-only list of found portals (may be empty) or <code>null</code>
	 *         if <code>pos</code> was <code>null</code>.
	 */
	public static List<Portal> getPortalsAt(BlockPos pos, RegistryKey<World> dimension) {
		if (pos == null)
			return null;

		List<Portal> foundPortals = portals.get(pos).stream().filter(portal -> portal.getDimension() == dimension)
				.collect(Collectors.toList());

		return Collections.unmodifiableList(foundPortals);
	}

	/**
	 * Gets all the portals registered in the specified dimension.
	 *
	 * @param dimension The dimension the portals should be in.
	 * @return A read-only list of found portals (may be empty).
	 */
	public static List<Portal> getPortalsInDimension(RegistryKey<World> dimension) {
		Set<Portal> uniquePortals = new HashSet<>(portals.values());

		return uniquePortals.stream().filter(portal -> portal.getDimension() == dimension).collect(Collectors.toList());
	}

	/**
	 * Destroys the specified portals portal blocks (the center).
	 * 
	 * @param world  The {@link World} the portal is located in.
	 * @param portal The {@link Portal}.
	 */
	private static void destroyPortalBlocks(World world, Portal portal) {
		for (BlockPos portalPos : portal.getPortalPositions()) {
			world.destroyBlock(portalPos, false);
		}
	}

	/**
	 * Gets the distance between 2 positions.
	 * 
	 * @param pos1 The first {@link BlockPos}.
	 * @param pos2 The second {@link BlockPos}.
	 * @return The distance or <code>-1</code> if either position was
	 *         <code>null</code>.
	 */
	private static int getDistance(BlockPos pos1, BlockPos pos2) {
		if (pos1 == null || pos2 == null)
			return -1;

		return Math.abs(pos1.getX() - pos2.getX() + pos1.getY() - pos2.getY() + pos1.getZ() - pos2.getZ()) + 1;
	}

	/**
	 * Find a corner starting at the specified position.
	 * 
	 * @param world        The {@link World}.
	 * @param startPos     The starting {@link BlockPos}.
	 * @param searchDir    The direction to search in.
	 * @param cornerFacing One of the directions the corner is enclosed by.
	 * @return A {@link Corner} or <code>null</code> if one of the parameters was
	 *         <code>null</code> or no corner could be found.
	 */
	private static Corner findCorner(World world, BlockPos startPos, Direction searchDir, Direction cornerFacing) {
		if (startPos == null || searchDir == null || cornerFacing == null)
			return null;

		BlockPos currentPos = startPos;
		int size = 0;

		do {
			if (!isPortalFrame(world, currentPos)) {
				if (isPortalFrame(world, currentPos.offset(cornerFacing))) {
					return new Corner(currentPos, searchDir.getOpposite(), cornerFacing);
				}

				break;
			}

			currentPos = currentPos.offset(searchDir);
			size++;
		} while (size <= 9);

		return null;
	}

	/**
	 * Registers the specified portal.
	 * 
	 * @param world       The {@link World}.
	 * @param portal      The {@link Portal} to register.
	 */
	public static void register(World world, Portal portal) {
		if (world == null || portal == null)
			return;

		for (BlockPos portalPos : portal.getAllPositions()) {
			portals.put(portalPos.toImmutable(), portal);
		}

		// Trigger save of portal data

		MoreOre.portalSaveData.markDirty();
	}

	/**
	 * Unregisters the specified portal.
	 * 
	 * @param world  The {@link World}.
	 * @param portal The {@link Portal} to unregister.
	 */
	public static void unregister(World world, Portal portal) {
		if (world == null || portal == null)
			return;

		for (BlockPos portalPos : portal.getAllPositions()) {
			portals.remove(portalPos, portal);
		}

		// Trigger save of portal data

		MoreOre.portalSaveData.markDirty();
	}

	/**
	 * Checks if the block at the specified position is a portal frame.
	 * 
	 * @param world The {@link World}.
	 * @param pos   The {@link BlockPos} to check.
	 * @return <code>true</code> if the block is a portal frame (power gauges also
	 *         count as part of the frame), otherwise <code>false</code>.
	 */
	private static boolean isPortalFrame(World world, BlockPos pos) {
		if (world == null || pos == null)
			return false;

		Block block = world.getBlockState(pos).getBlock();

		return (block instanceof SilverPortalFrameBlock);
	}

	/**
	 * Checks if the specified block can be used in a portal address.<br>
	 * Valid blocks may not have TileEntities and must be full blocks.
	 * 
	 * @param state The {@link BlockState} of the block to check.
	 * @return <code>true</code> if the block is valid, otherwise
	 *         <code>false</code>.
	 */
	@SuppressWarnings("deprecation")
	private static boolean isValidAddressBlock(BlockState state) {
		return (state != null && !state.getBlock().hasTileEntity(state) && !state.isAir());
	}

	/**
	 * Writes the registry data to a NBT compound tag.
	 * 
	 * @param nbt The {@link CompoundNBT} to save the registry data in.
	 */
	public static void writeToNBT(CompoundNBT nbt) {
		if (nbt == null)
			return;

		CompoundNBT portalsTag = new CompoundNBT();
		CompoundNBT portalBlocksTag = new CompoundNBT();
		CompoundNBT subTag;

		// Serialization of all Portals into a list.

		int i = 0;
		HashMap<Portal, Integer> portalIDs = Maps.newHashMap();
		Set<Portal> uniquePortals = new HashSet<>(portals.values());

		for (Portal portal : uniquePortals) {
			portalIDs.put(portal, i);
			portalsTag.put(String.valueOf(i++), portal.serializeNBT());
		}

		i = 0;
		int x = 0;

		// Serialization of BlockPos to Portal map.

		for (BlockPos pos : portals.keySet()) {
			subTag = new CompoundNBT();
			subTag.putLong("pos", pos.toLong());

			for (Portal portal : portals.get(pos)) {
				subTag.putInt("portal" + x++, portalIDs.get(portal));
			}

			x = 0;

			portalBlocksTag.put(String.valueOf(i++), subTag);
		}

		nbt.put("portals", portalsTag);
		nbt.put("portalBlocks", portalBlocksTag);
	}

	/**
	 * Reads the registry data from a NBT compound tag.
	 * 
	 * @param nbt The {@link CompoundNBT} to read the registry data from.
	 */
	public static void readFromNBT(CompoundNBT nbt) {
		if (nbt == null)
			return;

		portals.clear();

		CompoundNBT portalsTag = nbt.getCompound("portals");
		CompoundNBT portalBlocksTag = nbt.getCompound("portalBlocks");

		int i = 0;
		String key;
		CompoundNBT tag;
		Portal portal;

		// Get the portals and their IDs.

		HashMap<Integer, Portal> portalIDs = Maps.newHashMap();

		while (portalsTag.contains(key = String.valueOf(i))) {
			tag = portalsTag.getCompound(key);

			portal = new Portal();
			portal.deserializeNBT(tag);

			portalIDs.put(i++, portal);
		}

		// Deserialization of BlockPos to Portal map.
		i = 0;
		String subKey;
		BlockPos portalPos;

		while (portalBlocksTag.contains(key = String.valueOf(i++))) {
			tag = portalBlocksTag.getCompound(key);

			portalPos = BlockPos.fromLong(tag.getLong("pos"));

			int x = 0;
			while (tag.contains(subKey = "portal" + x++)) {
				portal = portalIDs.get(tag.getInt(subKey));
				portals.put(portalPos, portal);
			}
		}
	}
}
