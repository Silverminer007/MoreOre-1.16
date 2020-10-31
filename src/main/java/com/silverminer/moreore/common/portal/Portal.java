package com.silverminer.moreore.common.portal;

import com.silverminer.moreore.common.objects.blocks.SilverPortalBlock;
import com.silverminer.moreore.common.objects.blocks.SilverPortalFrameBlock;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a portal.<br>
 * Note: Corner1 and Corner4 must be diagonal to each other, same for Corner2
 * and Corner3.
 */
public class Portal implements INBTSerializable<CompoundNBT> {
	private RegistryKey<World> dimension;
	private Axis axis;
	private Corner corner1;
	private Corner corner2;
	private Corner corner3;
	private Corner corner4;

	public Portal() {
	}

	public Portal(RegistryKey<World> dimension, Axis axis, Corner corner1, Corner corner2,
			Corner corner3, Corner corner4) {
		this.dimension = dimension;
		this.axis = axis;
		this.corner1 = corner1;
		this.corner2 = corner2;
		this.corner3 = corner3;
		this.corner4 = corner4;
	}

	/**
	 * Gets the dimension the portal is located in.
	 * 
	 * @return A <code>DimensionType</code> representing the dimension.
	 */
	public RegistryKey<World> getDimension() {
		return dimension;
	}

	/**
	 * Gets the axis the portal is aligned to.
	 * 
	 * @return One of the {@link Axis} values or <code>null</code>.
	 */
	public Axis getAxis() {
		return axis;
	}

	/**
	 * Gets the portals first corner.
	 * 
	 * @return A {@link Corner} or <code>null</code>.
	 */
	public Corner getCorner1() {
		return corner1;
	}

	/**
	 * Gets the portals second corner.
	 * 
	 * @return A {@link Corner} or <code>null</code>.
	 */
	public Corner getCorner2() {
		return corner2;
	}

	/**
	 * Gets the portals third corner.
	 * 
	 * @return A {@link Corner} or <code>null</code>.
	 */
	public Corner getCorner3() {
		return corner3;
	}

	/**
	 * Gets the portals fourth corner.
	 * 
	 * @return A {@link Corner} or <code>null</code>.
	 */
	public Corner getCorner4() {
		return corner4;
	}

	/**
	 * Gets the positions of all blocks making up the portal.
	 * 
	 * @return An {@link Iterable} of {@link BlockPos}.
	 */
	public Iterable<BlockPos> getAllPositions() {
		return BlockPos.getAllInBoxMutable(corner1.getPos(), corner4.getPos());
	}

	/**
	 * Gets the positions of the actual portal blocks inside the portal frame.
	 * 
	 * @return An {@link Iterable} of {@link BlockPos}.
	 */
	public Iterable<BlockPos> getPortalPositions() {
		return BlockPos.getAllInBoxMutable(corner1.getInnerCornerPos(), corner4.getInnerCornerPos());
	}

	/**
	 * Gets the positions of all blocks making up the portals frame including the
	 * corners.
	 * 
	 * @return A list of {@link BlockPos}.
	 */
	public List<BlockPos> getFramePositions() {
		return getFramePositions(true);
	}

	/**
	 * Gets the positions of all blocks making up the portals frame. Inclusion of
	 * corner address blocks is optional.
	 * 
	 * @param includeCorners Determines if corner blocks should be included.
	 * @return A list of {@link BlockPos}.
	 */
	public List<BlockPos> getFramePositions(boolean includeCorners) {
		ArrayList<BlockPos> frame = new ArrayList<>();

		// Get relative directions of the first and forth corners to their adjacent
		// corners.

		Direction dir1To2 = Utils.getRelativeDirection(corner1.getPos(), corner2.getPos());
		Direction dir1To3 = Utils.getRelativeDirection(corner1.getPos(), corner3.getPos());
		Direction dir4To2 = Utils.getRelativeDirection(corner4.getPos(), corner2.getPos());
		Direction dir4To3 = Utils.getRelativeDirection(corner4.getPos(), corner3.getPos());

		// Offset the corner positions towards their adjacent corners and get all
		// positions
		// in between. This way we get all the frame positions without the corners
		// themselves.

		BlockPos from1 = corner1.getPos().offset(dir1To2);
		BlockPos to1 = corner2.getPos().offset(dir1To2.getOpposite());

		BlockPos from2 = corner1.getPos().offset(dir1To3);
		BlockPos to2 = corner3.getPos().offset(dir1To3.getOpposite());

		BlockPos from3 = corner4.getPos().offset(dir4To2);
		BlockPos to3 = corner2.getPos().offset(dir4To2.getOpposite());

		BlockPos from4 = corner4.getPos().offset(dir4To3);
		BlockPos to4 = corner3.getPos().offset(dir4To3.getOpposite());

		// BlockPos.getAllInBox() delivers wrong results (duplicates and missing
		// positions).
		// So I have to do this nonsense. Minecraft 1.14.4 (10.10.2019)
		for (BlockPos pos : BlockPos.getAllInBoxMutable(from1, to1))
			frame.add(pos.toImmutable());
		for (BlockPos pos : BlockPos.getAllInBoxMutable(from2, to2))
			frame.add(pos.toImmutable());
		for (BlockPos pos : BlockPos.getAllInBoxMutable(from3, to3))
			frame.add(pos.toImmutable());
		for (BlockPos pos : BlockPos.getAllInBoxMutable(from4, to4))
			frame.add(pos.toImmutable());

		if (includeCorners) {
			frame.add(corner1.getPos());
			frame.add(corner2.getPos());
			frame.add(corner3.getPos());
			frame.add(corner4.getPos());
		}

		return frame;
	}

	/**
	 * Determines if the portal is missing any blocks.
	 * 
	 * @param world The world.
	 * @return <code>true</code> if the portal is missing one or more blocks,
	 *         otherwise <code>false</code>.
	 */
	public boolean isDamaged(World world) {
		if (world == null)
			return false;

		for (BlockPos pos : getFramePositions(false)) {
			if (!(world.getBlockState(pos).getBlock() instanceof SilverPortalFrameBlock))
				return true;
		}

		for (BlockPos pos : getPortalPositions()) {
			if (!(world.getBlockState(pos).getBlock() instanceof SilverPortalBlock))
				return true;
		}

		return false;
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT tag = new CompoundNBT();
		tag.putString("dimension", (dimension.toString() != null) ? dimension.toString() : "");
		tag.putString("axis", axis.name());
		tag.put("corner1", corner1.serializeNBT());
		tag.put("corner2", corner2.serializeNBT());
		tag.put("corner3", corner3.serializeNBT());
		tag.put("corner4", corner4.serializeNBT());

		return tag;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		if (nbt == null)
			return;

		// Handle legacy dimension id tags. This allows old worlds to load properly.
		// :LegacyDimensionId
		if (nbt.contains("dimension", 8)) // Type 8 means string.
		{
			ResourceLocation dimensionRegistryKey = ResourceLocation.tryCreate(nbt.getString("dimension"));
			dimension = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, dimensionRegistryKey);
		} else {
			dimension = World.OVERWORLD;
		}

		axis = Axis.byName(nbt.getString("axis"));

		corner1 = new Corner();
		corner1.deserializeNBT(nbt.getCompound("corner1"));

		corner2 = new Corner();
		corner2.deserializeNBT(nbt.getCompound("corner2"));

		corner3 = new Corner();
		corner3.deserializeNBT(nbt.getCompound("corner3"));

		corner4 = new Corner();
		corner4.deserializeNBT(nbt.getCompound("corner4"));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dimension.hashCode();
		result = prime * result + ((axis == null) ? 0 : axis.hashCode());
		result = prime * result + ((corner1 == null) ? 0 : corner1.hashCode());
		result = prime * result + ((corner2 == null) ? 0 : corner2.hashCode());
		result = prime * result + ((corner3 == null) ? 0 : corner3.hashCode());
		result = prime * result + ((corner4 == null) ? 0 : corner4.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		Portal other = (Portal) obj;

		if (dimension != other.dimension)
			return false;

		if (axis != other.axis)
			return false;

		if (corner1 == null) {
			if (other.corner1 != null)
				return false;
		} else if (!corner1.equals(other.corner1))
			return false;

		if (corner2 == null) {
			if (other.corner2 != null)
				return false;
		} else if (!corner2.equals(other.corner2))
			return false;

		if (corner3 == null) {
			if (other.corner3 != null)
				return false;
		} else if (!corner3.equals(other.corner3))
			return false;

		if (corner4 == null) {
			if (other.corner4 != null)
				return false;
		} else if (!corner4.equals(other.corner4))
			return false;

		return true;
	}
}