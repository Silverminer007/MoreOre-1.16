package com.silverminer.moreore.init;

import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.world.gen.tree.treedecorator.ModTreeDecoratorType;

import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TreeDecotratorInit {
	public static final DeferredRegister<TreeDecoratorType<?>> TYPES = DeferredRegister.create(ForgeRegistries.TREE_DECORATOR_TYPES,
			MoreOre.MODID);
	public static final RegistryObject<TreeDecoratorType<?>> BANANA = TYPES.register("banana",
			() -> ModTreeDecoratorType.BANANA);
}
