package com.silverminer.moreore.world.dimensions;

import java.util.function.BiFunction;

import net.minecraft.world.World;
import net.minecraft.world.Dimension;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.ModDimension;

public class ModSilverDimension extends ModDimension{

	@Override
	public BiFunction<World, DimensionType, ? extends Dimension> getFactory() {
		return SilverDimension::new;
	}
	
}
