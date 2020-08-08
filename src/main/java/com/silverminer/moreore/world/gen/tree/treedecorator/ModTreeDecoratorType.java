package com.silverminer.moreore.world.gen.tree.treedecorator;

import com.mojang.serialization.Codec;

import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

public class ModTreeDecoratorType<P extends TreeDecorator> extends TreeDecoratorType<P> {
	public static final TreeDecoratorType<BananaTreeDecorator> BANANA = func_236877_a_("banana",
			BananaTreeDecorator.field_236866_a_);

	public ModTreeDecoratorType(Codec<P> p_i225872_1_) {
		super(p_i225872_1_);
	}
}
