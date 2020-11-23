package com.silverminer.moreore.init;

import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.client.container.RunetableContainer;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerTypesInit {
	public static final DeferredRegister<ContainerType<?>> CONTAINER = DeferredRegister
			.create(ForgeRegistries.CONTAINERS, MoreOre.MODID);

	/**
	 * Registers the color Block container to be in the game
	 */
	public static final RegistryObject<ContainerType<RunetableContainer>> RUNE_TABLE = CONTAINER.register("rune_table",
			() -> new ContainerType<>(RunetableContainer::new));
}