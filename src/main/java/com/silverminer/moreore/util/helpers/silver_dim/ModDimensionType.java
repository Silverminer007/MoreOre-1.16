package com.silverminer.moreore.util.helpers.silver_dim;

import java.util.OptionalLong;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.IBiomeMagnifier;

public class ModDimensionType extends DimensionType {

	public ModDimensionType(OptionalLong fixedTime, boolean hasSkyLight, boolean hasCeiling, boolean ultrawarm,
			boolean natural, double coordinateScale, boolean hasDragonFight, boolean piglinSafe, boolean bedWorks,
			boolean respawnAnchorWorks, boolean hasRaids, int logicalHeight, IBiomeMagnifier magnifier,
			ResourceLocation infiniburn, ResourceLocation effects, float ambientLight) {
		super(fixedTime, hasSkyLight, hasCeiling, ultrawarm, natural, coordinateScale, hasDragonFight, piglinSafe, bedWorks,
				respawnAnchorWorks, hasRaids, logicalHeight, magnifier, infiniburn, effects, ambientLight);
	}

}