package com.silverminer.moreore.init;

import java.util.ArrayList;

import com.google.common.collect.ImmutableList;
import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.common.world.gen.structures.AbstractStructure;
import com.silverminer.moreore.common.world.gen.structures.DesertTempelStructure;
import com.silverminer.moreore.common.world.gen.structures.TempelStructure;

import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class StructureFeatureInit {
	public static final ArrayList<AbstractStructure<NoFeatureConfig>> STRUCTURES_LIST = new ArrayList<AbstractStructure<NoFeatureConfig>>();

	public static final DeferredRegister<Structure<?>> FEATURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, MoreOre.MODID);

	public static final RegistryObject<TempelStructure> TEMPEL = register(TempelStructure.SHORT_NAME,
			new TempelStructure(NoFeatureConfig.field_236558_a_));

	public static final RegistryObject<DesertTempelStructure> DESERT_TEMPEL = register(DesertTempelStructure.SHORT_NAME,
			new DesertTempelStructure(NoFeatureConfig.field_236558_a_));

	private static <T extends AbstractStructure<NoFeatureConfig>> RegistryObject<T> register(String name, T structure) {
		if (!Structure.field_236365_a_.containsValue(structure)) {
			Structure.field_236365_a_.putIfAbsent(MoreOre.MODID + ":" + name, structure);
		}
		if (!Structure.field_236385_u_.containsValue(structure.getDecorationStage())) {
			Structure.field_236385_u_.putIfAbsent(structure, structure.getDecorationStage());
		}

		Structure.field_236384_t_ = ImmutableList.<Structure<?>>builder().addAll(Structure.field_236384_t_)
				.add(structure).build();

		STRUCTURES_LIST.add(structure);

		return FEATURES.register(name, () -> structure);
	}
}