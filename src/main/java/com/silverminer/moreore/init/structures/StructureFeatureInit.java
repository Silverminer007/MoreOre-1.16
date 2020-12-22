package com.silverminer.moreore.init.structures;

import java.util.ArrayList;

import com.google.common.collect.ImmutableList;
import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.common.world.gen.structures.AbstractStructure;
import com.silverminer.moreore.common.world.gen.structures.desert_tempel.DesertTempelStructure;
import com.silverminer.moreore.common.world.gen.structures.nut_bush_plantation.NutBushPlantationStructure;
import com.silverminer.moreore.common.world.gen.structures.runes.green.GreenDungeonStructure;
import com.silverminer.moreore.common.world.gen.structures.tempel.TempelStructure;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class StructureFeatureInit {
	public static final ArrayList<AbstractStructure<NoFeatureConfig>> STRUCTURES_LIST = new ArrayList<AbstractStructure<NoFeatureConfig>>();

	public static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister
			.create(ForgeRegistries.STRUCTURE_FEATURES, MoreOre.MODID);

	public static final RegistryObject<TempelStructure> TEMPEL = register(TempelStructure.SHORT_NAME,
			new TempelStructure(NoFeatureConfig.field_236558_a_));

	public static final RegistryObject<DesertTempelStructure> DESERT_TEMPEL = register(DesertTempelStructure.SHORT_NAME,
			new DesertTempelStructure(NoFeatureConfig.field_236558_a_));

	public static final RegistryObject<NutBushPlantationStructure> NUT_BUSH_PLANTATION = register(
			NutBushPlantationStructure.SHORT_NAME, new NutBushPlantationStructure(NoFeatureConfig.field_236558_a_));

	public static final RegistryObject<GreenDungeonStructure> GREEN_DUNGEON = register(GreenDungeonStructure.SHORT_NAME,
			new GreenDungeonStructure(NoFeatureConfig.field_236558_a_));

	private static <T extends AbstractStructure<NoFeatureConfig>> RegistryObject<T> register(String name, T structure) {
		if (!Structure.NAME_STRUCTURE_BIMAP.containsValue(structure)) {
			Structure.NAME_STRUCTURE_BIMAP.putIfAbsent(new ResourceLocation(MoreOre.MODID, name).toString(), structure);
		}
		if (!Structure.STRUCTURE_DECORATION_STAGE_MAP.containsValue(structure.getDecorationStage())) {
			Structure.STRUCTURE_DECORATION_STAGE_MAP.putIfAbsent(structure, structure.getDecorationStage());
		}

		Structure.field_236384_t_ = ImmutableList.<Structure<?>>builder().addAll(Structure.field_236384_t_)
				.add(structure).build();

		STRUCTURES_LIST.add(structure);

		return STRUCTURES.register(name, () -> structure);
	}
}