package com.silverminer.moreore.init.blocks;

import com.silverminer.moreore.objects.blocks.BeamerBlock;
import com.silverminer.moreore.objects.blocks.BurnedOutLavaSpongeBlock;
import com.silverminer.moreore.objects.blocks.LavaSpongeBlock;
import com.silverminer.moreore.objects.blocks.PatrolPointBlock;
import com.silverminer.moreore.objects.blocks.SilverPortalBlock;
import com.silverminer.moreore.objects.blocks.SilverPortalFrameBlock;

import java.util.function.Supplier;

import com.silverminer.moreore.MoreOre;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitBlocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MoreOre.MODID);

	public static final RegistryObject<Block> GLOWING_STONE = BLOCKS.register("glowing_stone", 
			 () -> new Block(Block.Properties.from(Blocks.GLOWSTONE)));//Diesen Block gibt es nur als Block

	public static final RegistryObject<Block> GLOW_BLUE_BLOCK = BLOCKS.register("glow_blue_block", 
			 () -> new Block(Block.Properties.from(Blocks.GLOWSTONE)));

	public static final RegistryObject<Block> SILVER_PORTAL = BLOCKS.register("silver_portal", 
			 () -> new SilverPortalBlock());

	public static final RegistryObject<Block> LAVA_SPONGE = BLOCKS.register("lava_sponge", 
			() -> new LavaSpongeBlock(Block.Properties.from(Blocks.SPONGE)));

	public static final RegistryObject<Block> BURNED_OUT_LAVA_SPONGE = BLOCKS.register("burned_out_lava_sponge", 
			() -> new BurnedOutLavaSpongeBlock(Block.Properties.from(Blocks.WET_SPONGE)));

	public static final RegistryObject<Block> SILVER_PORTAL_FRAME = BLOCKS.register("silver_portal_frame", 
			() -> new SilverPortalFrameBlock(Block.Properties.create(Material.ROCK, MaterialColor.BLACK)
					.hardnessAndResistance(50.0f, 200.0f)
					.sound(SoundType.STONE)
					.harvestLevel(3)));

	public static final RegistryObject<Block> BEAMER = BLOCKS.register("beamer", 
			() -> new BeamerBlock(Block.Properties.from(Blocks.STONE)));

	public static final RegistryObject<Block> PATROLPOINT = BLOCKS.register("patrolpoint", 
			() -> new PatrolPointBlock(Block.Properties.from(Blocks.STONE)));

	public static <T extends Block> RegistryObject<Block> registerCutout(String key, Supplier<T> block) {
		RegistryObject<Block> temp = BLOCKS.register(key, block);
		MoreOre.cutoutBlocks.add(temp);
		return temp;
	}
}