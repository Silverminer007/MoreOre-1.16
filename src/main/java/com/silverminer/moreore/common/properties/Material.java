package com.silverminer.moreore.common.properties;

import java.util.ArrayList;

import com.silverminer.moreore.common.objects.blocks.BeamerBlock;

import net.minecraft.block.BlockState;
import net.minecraft.util.IStringSerializable;

public enum Material implements IStringSerializable {
	BEAMER("beamer"), ANDESITE("andesite"), OAK_PLANKS("oak_planks"), COBBLESTONE("cobblestone"), STONE("stone"), EMERALD_ORE("emerald_ore"),
	DIRT("dirt"), ICE("ice"), DIAMOND_ORE("diamond_ore"), BOOKSHELF("bookshelf"), GOLD_BLOCK("gold_block"),
	RAINBOW_BLOCK("rainbow_block"), EMERALD_BLOCK("emerald_block"), DIAMOND_BLOCK("diamond_block"),
	IRON_BLOCK("iron_block"), ALEXANDRIT_BLOCK("alexandrit_block"), RAINBOW_ORE("rainbow_ore"),
	SPRUCE_PLANKS("spruce_planks"), ALEXANDRIT_ORE("alexandrit_ore"),
	SILVER_PORTAL_FRAME("silver_portal_frame"), OBSIDIAN("obsidian"), COAL_BLOCK("coal_block"),
	REDSTONE_BLOCK("redstone_block"), REDSTONE_ORE("redstone_ore"), WHITE_WOOL("white_wool"),
	CRAFTING_TABLE("crafting_table"), BIRCH_PLANKS("birch_planks");

	private final String name;

	Material(String string) {
		this.name = string;
	}

/*	@Override//Moved to func_176610_l()
	public String getName() {
		return this.name;
	}*/

	public static Material getNext(BlockState state) {
		ArrayList<Material> amaterials = new ArrayList<Material>();
		Material[] materials = Material.values();
		for (Material material : materials) {
			amaterials.add(material);
		}
		int pos = amaterials.indexOf(state.get(BeamerBlock.MATERIAL));
		if (pos == amaterials.size() - 1) {
			return materials[1];
		} else {
			return materials[pos + 1];
		}
	}

	@Override
	public String func_176610_l() {
		return this.name;
	}
}
