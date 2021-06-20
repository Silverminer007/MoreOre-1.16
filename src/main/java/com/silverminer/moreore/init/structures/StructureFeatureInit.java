package com.silverminer.moreore.init.structures;

import java.util.ArrayList;

import com.google.common.collect.ImmutableList;
import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.common.world.gen.structures.AbstractStructure;
import com.silverminer.moreore.common.world.gen.structures.nut_bush_plantation.NutBushPlantationStructure;
import com.silverminer.moreore.common.world.gen.structures.runes.blue.BlueRuneStructure;
import com.silverminer.moreore.common.world.gen.structures.runes.brown.BrownLandingstageStructure;
import com.silverminer.moreore.common.world.gen.structures.runes.gray.GiantStructure;
import com.silverminer.moreore.common.world.gen.structures.runes.green.GreenDungeonStructure;
import com.silverminer.moreore.common.world.gen.structures.runes.orange.OrangeShrineStructure;
import com.silverminer.moreore.common.world.gen.structures.runes.purple.PurplehouseStructure;
import com.silverminer.moreore.common.world.gen.structures.runes.red.BlackSmithStructure;
import com.silverminer.moreore.common.world.gen.structures.runes.yellow.DesertTempelStructure;
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
			new TempelStructure(NoFeatureConfig.CODEC));

	public static final RegistryObject<DesertTempelStructure> DESERT_TEMPEL = register(DesertTempelStructure.SHORT_NAME,
			new DesertTempelStructure(NoFeatureConfig.CODEC));

	public static final RegistryObject<NutBushPlantationStructure> NUT_BUSH_PLANTATION = register(
			NutBushPlantationStructure.SHORT_NAME, new NutBushPlantationStructure(NoFeatureConfig.CODEC));

	public static final RegistryObject<GreenDungeonStructure> GREEN_DUNGEON = register(GreenDungeonStructure.SHORT_NAME,
			new GreenDungeonStructure(NoFeatureConfig.CODEC));

	public static final RegistryObject<OrangeShrineStructure> ORANGE_SHRINE = register(OrangeShrineStructure.SHORT_NAME,
			new OrangeShrineStructure(NoFeatureConfig.CODEC));

	public static final RegistryObject<BrownLandingstageStructure> BROWN_LANDINGSTAGE = register(
			BrownLandingstageStructure.SHORT_NAME, new BrownLandingstageStructure(NoFeatureConfig.CODEC));

	public static final RegistryObject<PurplehouseStructure> PURPLE_HOUSE = register(PurplehouseStructure.SHORT_NAME,
			new PurplehouseStructure(NoFeatureConfig.CODEC));

	public static final RegistryObject<GiantStructure> GIANT = register(GiantStructure.SHORT_NAME,
			new GiantStructure(NoFeatureConfig.CODEC));

	public static final RegistryObject<BlueRuneStructure> BLUE_RUNE = register(BlueRuneStructure.SHORT_NAME,
			new BlueRuneStructure(NoFeatureConfig.CODEC));

	public static final RegistryObject<BlackSmithStructure> BLACK_SMITH = register(BlackSmithStructure.SHORT_NAME,
			new BlackSmithStructure(NoFeatureConfig.CODEC));

	private static <T extends AbstractStructure<NoFeatureConfig>> RegistryObject<T> register(String name, T structure) {
		if (!Structure.STRUCTURES_REGISTRY.containsValue(structure)) {
			Structure.STRUCTURES_REGISTRY.putIfAbsent(new ResourceLocation(MoreOre.MODID, name).toString(), structure);
		}
		if (!Structure.STEP.containsValue(structure.step())) {
			Structure.STEP.putIfAbsent(structure, structure.step());
		}

		Structure.NOISE_AFFECTING_FEATURES = ImmutableList.<Structure<?>>builder().addAll(Structure.NOISE_AFFECTING_FEATURES)
				.add(structure).build();

		STRUCTURES_LIST.add(structure);

		return STRUCTURES.register(name, () -> structure);
	}
}