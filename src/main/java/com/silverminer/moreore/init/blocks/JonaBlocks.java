package com.silverminer.moreore.init.blocks;

import com.silverminer.moreore.objects.blocks.ErdbeerBuschBlock;
import com.silverminer.moreore.MoreOre;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class JonaBlocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MoreOre.MODID);

	public static final RegistryObject<Block> OHNEZAHN = BLOCKS.register("ohnezahn", 
			 () -> new Block(Block.Properties.from(Blocks.STONE)));

	public static final RegistryObject<Block> ERDBEER_BLOCK = BLOCKS.register("erdbeer_block", 
			 () -> new Block(Block.Properties.from(Blocks.STONE)));

	public static final RegistryObject<Block> ERDBEER_BUSCH = InitBlocks.registerCutout("erdbeer_busch_block", 
			() -> new ErdbeerBuschBlock(Block.Properties.create(Material.PLANTS).tickRandomly()
					.doesNotBlockMovement().sound(SoundType.SWEET_BERRY_BUSH)));
}
