package com.silverminer.moreore.init.blocks;

import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.common.objects.blocks.BananaBlock;
import com.silverminer.moreore.common.objects.blocks.LetuceBlock;
import com.silverminer.moreore.common.objects.blocks.ModLogBlock;
import com.silverminer.moreore.common.objects.blocks.NutBushLeavesBlock;
import com.silverminer.moreore.common.objects.blocks.Sapling;
import com.silverminer.moreore.common.world.gen.tree.GoldTree;
import com.silverminer.moreore.common.world.gen.tree.IceTree;
import com.silverminer.moreore.common.world.gen.tree.NutBush;
import com.silverminer.moreore.common.world.gen.tree.SilverTree;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.TrapDoorBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.WoodButtonBlock;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BiologicBlocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MoreOre.MODID);

	public static final RegistryObject<Block> SILVER_SAPLING = InitBlocks.registerCutout("silver_saplings",
			() -> new Sapling(() -> new SilverTree(), Block.Properties.from(Blocks.OAK_SAPLING)));

	public static final RegistryObject<Block> SILVER_PLANKS = BLOCKS.register("silver_planks",
			() -> new Block(Block.Properties.from(Blocks.OAK_PLANKS)));

	public static final RegistryObject<Block> LETUCE = InitBlocks.registerCutout("letuce",
			() -> new LetuceBlock(Block.Properties.from(Blocks.WHEAT)));

	public static final RegistryObject<Block> STRIPPED_SILVER_LOG = BLOCKS.register("stripped_silver_logs",
			() -> new ModLogBlock(Block.Properties.from(Blocks.STRIPPED_OAK_LOG)));

	public static final RegistryObject<Block> SILVER_LOG = BLOCKS.register("silver_logs",
			() -> new ModLogBlock(Block.Properties.from(Blocks.OAK_LOG), () -> STRIPPED_SILVER_LOG.get()));

	public static final RegistryObject<Block> SILVER_LEAVES = InitBlocks.registerCutout("silver_leaves",
			() -> new LeavesBlock(Block.Properties.from(Blocks.OAK_LEAVES)));

	public static final RegistryObject<Block> GOLD_LEAVES = BLOCKS.register("gold_leaves",
			() -> new LeavesBlock(Block.Properties.from(Blocks.OAK_LEAVES)));

	public static final RegistryObject<Block> SILVER_WOOD = BLOCKS.register("silver_wood",
			() -> new Block(Block.Properties.from(Blocks.OAK_WOOD)));

	public static final RegistryObject<Block> BANANA = InitBlocks.registerCutout("banana",
			() -> new BananaBlock(Block.Properties.from(Blocks.COCOA)));

	public static final RegistryObject<Block> SILVER_DOOR = InitBlocks.registerCutout("silver_door",
			() -> new DoorBlock(Block.Properties.from(Blocks.OAK_DOOR)));

	public static final RegistryObject<Block> SILVER_STAIRS = BLOCKS.register("silver_stairs",
			() -> new StairsBlock(() -> BiologicBlocks.SILVER_PLANKS.get().getDefaultState(),
					Block.Properties.from(Blocks.OAK_STAIRS)));

	public static final RegistryObject<Block> SILVER_FENCE = BLOCKS.register("silver_fence",
			() -> new FenceBlock(Block.Properties.from(Blocks.OAK_FENCE)));

	public static final RegistryObject<Block> SILVER_FENCE_GATE = BLOCKS.register("silver_fence_gate",
			() -> new FenceGateBlock(Block.Properties.from(Blocks.OAK_FENCE_GATE)));

	public static final RegistryObject<Block> SILVER_SLAB = BLOCKS.register("silver_slab",
			() -> new SlabBlock(Block.Properties.from(BiologicBlocks.SILVER_PLANKS.get())));

	public static final RegistryObject<Block> SILVER_WALL = BLOCKS.register("silver_wall",
			() -> new WallBlock(Block.Properties.from(BiologicBlocks.SILVER_PLANKS.get())));

	public static final RegistryObject<Block> SILVER_TRAPDOOR = BLOCKS.register("silver_trapdoor",
			() -> new TrapDoorBlock(Block.Properties.from(Blocks.OAK_TRAPDOOR)));

	public static final RegistryObject<Block> GOLD_SAPLING = InitBlocks.registerCutout("gold_saplings",
			() -> new Sapling(() -> new GoldTree(), Block.Properties.from(Blocks.OAK_SAPLING)));

	public static final RegistryObject<Block> ICE_LEAVES = InitBlocks.registerCutout("ice_leaves",
			() -> new LeavesBlock(Block.Properties.from(Blocks.OAK_LEAVES)));

	public static final RegistryObject<Block> ICE_SAPLING = InitBlocks.registerCutout("ice_saplings",
			() -> new Sapling(() -> new IceTree(), Block.Properties.from(Blocks.OAK_SAPLING)));

	public static final RegistryObject<Block> GLASS_DOOR = InitBlocks.registerCutout("glass_door",
			() -> new DoorBlock(Block.Properties.from(Blocks.OAK_DOOR)));

	public static final RegistryObject<Block> SILVER_WOOD_BUTTON = BLOCKS.register("silver_wood_button",
			() -> new WoodButtonBlock(Block.Properties.from(Blocks.OAK_BUTTON)));

	public static final RegistryObject<Block> POTTET_ICE_SAPLING = BLOCKS.register("pottet_ice_saplings",
			() -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, () -> BiologicBlocks.ICE_SAPLING.get(),
					Block.Properties.from(Blocks.FLOWER_POT)));

	public static final RegistryObject<Block> POTTET_SILVER_SAPLING = BLOCKS.register("pottet_silver_saplings",
			() -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT,
					() -> BiologicBlocks.SILVER_SAPLING.get(), Block.Properties.from(Blocks.FLOWER_POT)));

	public static final RegistryObject<Block> POTTET_GOLD_SAPLING = BLOCKS.register("pottet_gold_saplings",
			() -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, () -> BiologicBlocks.GOLD_SAPLING.get(),
					Block.Properties.from(Blocks.POTTED_OAK_SAPLING)));

	public static final RegistryObject<Block> POTTET_NUT_BUSH_SAPLING = BLOCKS.register("pottet_nut_bush_sapling",
			() -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, () -> BiologicBlocks.NUT_BUSH_SAPLING.get(),
					Block.Properties.from(Blocks.POTTED_OAK_SAPLING)));

	public static final RegistryObject<Block> NUT_BUSH_SAPLING = InitBlocks.registerCutout("nut_bush_sapling",
			() -> new Sapling(() -> new NutBush(), Block.Properties.from(Blocks.OAK_SAPLING)));

	public static final RegistryObject<Block> NUT_BUSH_LOG = BLOCKS.register("nut_bush_log",
			() -> new ModLogBlock(Block.Properties.from(Blocks.OAK_LOG).notSolid()));

	public static final RegistryObject<Block> NUT_BUSH_LEAVES = InitBlocks.registerCutout("nut_bush_leaves",
			() -> new NutBushLeavesBlock(Block.Properties.from(Blocks.OAK_LEAVES)));
}