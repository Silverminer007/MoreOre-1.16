package com.silverminer.moreore.init;

import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.world.gen.structures.DesertTempelStructure;
import com.silverminer.moreore.world.gen.structures.SchoolStructure;
import com.silverminer.moreore.world.gen.structures.TempelStructure;

import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class StructureFeatureInit {
	public static final DeferredRegister<Structure<?>> FEATURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, MoreOre.MODID);

	public static final RegistryObject<Structure<NoFeatureConfig>> TEMPEL = register(TempelStructure.SHORT_NAME,
			new TempelStructure(NoFeatureConfig.field_236558_a_));

	public static final RegistryObject<Structure<NoFeatureConfig>> SCHOOL = register(SchoolStructure.SHORT_NAME,
			new SchoolStructure(NoFeatureConfig.field_236558_a_));

	public static final RegistryObject<Structure<NoFeatureConfig>> DESERT_TEMPEL = register(DesertTempelStructure.SHORT_NAME,
			new DesertTempelStructure(NoFeatureConfig.field_236558_a_));

	private static <T extends Structure<?>> RegistryObject<T> register(String name, T feature) {
		return FEATURES.register(name, () -> feature);
	}
}