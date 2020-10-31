package com.silverminer.moreore.init.blocks;

import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.common.objects.blocks.GemstoneOreBlock;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class OreBlocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MoreOre.MODID);

	public static final RegistryObject<Block> SILVER_ORE = BLOCKS.register("silver_ore", 
			() -> new GemstoneOreBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)
					.setLightLevel((light) -> {
						return 8;
					}).sound(SoundType.METAL), 4));

	public static final RegistryObject<Block> ALEXANDRIT_ORE = BLOCKS.register("alexandrit_ore", 
			() -> new GemstoneOreBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)
					.sound(SoundType.METAL), 7));

	public static final RegistryObject<Block> RAINBOW_ORE = BLOCKS.register("rainbow_ore", 
			() -> new GemstoneOreBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)
					.sound(SoundType.METAL), 5));

	public static final RegistryObject<Block> RAINBOW_BLOCK = BLOCKS.register("rainbow_block", 
			() -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)
					.sound(SoundType.METAL)));

	public static final RegistryObject<Block> SILVER_BLOCK = BLOCKS.register("silver_block", 
			() -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)
					.sound(SoundType.METAL)));

	public static final RegistryObject<Block> ALEXANDRIT_BLOCK = BLOCKS.register("alexandrit_block", 
			() -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)
					.sound(SoundType.METAL)));

	public static final RegistryObject<Block> CAT_GOLD_BLOCK = BLOCKS.register("cat_gold_block", 
			() -> new Block(Block.Properties.from(Blocks.GOLD_BLOCK)));

	public static final RegistryObject<Block> RUBIN_ORE = BLOCKS.register("rubin_ore", 
			() -> new GemstoneOreBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)
					.sound(SoundType.METAL), 6));

	public static final RegistryObject<Block> RUBIN_BLOCK = BLOCKS.register("rubin_block", 
			() -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)
					.sound(SoundType.METAL)));

	public static final RegistryObject<Block> SAPHIR_ORE = BLOCKS.register("saphir_ore", 
			() -> new GemstoneOreBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)
					.sound(SoundType.METAL), 6));

	public static final RegistryObject<Block> SAPHIR_BLOCK = BLOCKS.register("saphir_block", 
			() -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)
					.sound(SoundType.METAL)));
}
