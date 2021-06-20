package com.silverminer.moreore.common.world.gen.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.StandingSignBlock;
import net.minecraft.block.TrapDoorBlock;
import net.minecraft.block.WallSignBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class ColorStructurePiece extends AbstractStructurePiece {
	protected static final ArrayList<Block> WOOLS = Lists.newArrayList(Blocks.WHITE_WOOL, Blocks.ORANGE_WOOL,
			Blocks.MAGENTA_WOOL, Blocks.LIGHT_BLUE_WOOL, Blocks.YELLOW_WOOL, Blocks.LIME_WOOL, Blocks.PINK_WOOL,
			Blocks.GRAY_WOOL, Blocks.LIGHT_GRAY_WOOL, Blocks.BLACK_WOOL, Blocks.CYAN_WOOL, Blocks.PURPLE_WOOL,
			Blocks.BLUE_WOOL, Blocks.BROWN_WOOL, Blocks.GREEN_WOOL, Blocks.RED_WOOL);

	protected static final ArrayList<Block> TERRACOTTAS = Lists.newArrayList(Blocks.TERRACOTTA, Blocks.WHITE_TERRACOTTA,
			Blocks.ORANGE_TERRACOTTA, Blocks.MAGENTA_TERRACOTTA, Blocks.LIGHT_BLUE_TERRACOTTA, Blocks.YELLOW_TERRACOTTA,
			Blocks.LIME_TERRACOTTA, Blocks.PINK_TERRACOTTA, Blocks.GRAY_TERRACOTTA, Blocks.LIGHT_GRAY_TERRACOTTA,
			Blocks.BLACK_TERRACOTTA, Blocks.CYAN_TERRACOTTA, Blocks.PURPLE_TERRACOTTA, Blocks.BLUE_TERRACOTTA,
			Blocks.BROWN_TERRACOTTA, Blocks.GREEN_TERRACOTTA, Blocks.RED_TERRACOTTA);

	protected static final ArrayList<Block> GLAZED_TERRACOTTAS = Lists.newArrayList(Blocks.WHITE_GLAZED_TERRACOTTA,
			Blocks.ORANGE_GLAZED_TERRACOTTA, Blocks.MAGENTA_GLAZED_TERRACOTTA, Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA,
			Blocks.YELLOW_GLAZED_TERRACOTTA, Blocks.LIME_GLAZED_TERRACOTTA, Blocks.PINK_GLAZED_TERRACOTTA,
			Blocks.GRAY_GLAZED_TERRACOTTA, Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA, Blocks.BLACK_GLAZED_TERRACOTTA,
			Blocks.CYAN_GLAZED_TERRACOTTA, Blocks.PURPLE_GLAZED_TERRACOTTA, Blocks.BLUE_GLAZED_TERRACOTTA,
			Blocks.BROWN_GLAZED_TERRACOTTA, Blocks.GREEN_GLAZED_TERRACOTTA, Blocks.RED_GLAZED_TERRACOTTA);

	protected static final ArrayList<Block> CONCRETE = Lists.newArrayList(Blocks.WHITE_CONCRETE, Blocks.ORANGE_CONCRETE,
			Blocks.MAGENTA_CONCRETE, Blocks.LIGHT_BLUE_CONCRETE, Blocks.YELLOW_CONCRETE, Blocks.LIME_CONCRETE,
			Blocks.PINK_CONCRETE, Blocks.GRAY_CONCRETE, Blocks.LIGHT_GRAY_CONCRETE, Blocks.BLACK_CONCRETE,
			Blocks.CYAN_CONCRETE, Blocks.PURPLE_CONCRETE, Blocks.BLUE_CONCRETE, Blocks.BROWN_CONCRETE,
			Blocks.GREEN_CONCRETE, Blocks.RED_CONCRETE);

	protected static final ArrayList<Block> CONCRETE_POWDERS = Lists.newArrayList(Blocks.WHITE_CONCRETE_POWDER,
			Blocks.ORANGE_CONCRETE_POWDER, Blocks.MAGENTA_CONCRETE_POWDER, Blocks.LIGHT_BLUE_CONCRETE_POWDER,
			Blocks.YELLOW_CONCRETE_POWDER, Blocks.LIME_CONCRETE_POWDER, Blocks.PINK_CONCRETE_POWDER,
			Blocks.GRAY_CONCRETE_POWDER, Blocks.LIGHT_GRAY_CONCRETE_POWDER, Blocks.BLACK_CONCRETE_POWDER,
			Blocks.CYAN_CONCRETE_POWDER, Blocks.PURPLE_CONCRETE_POWDER, Blocks.BLUE_CONCRETE_POWDER,
			Blocks.BROWN_CONCRETE_POWDER, Blocks.GREEN_CONCRETE_POWDER, Blocks.RED_CONCRETE_POWDER);
	protected static final ArrayList<Block> PLANKS = Lists.newArrayList(Blocks.OAK_PLANKS, Blocks.SPRUCE_PLANKS,
			Blocks.DARK_OAK_PLANKS, Blocks.BIRCH_PLANKS, Blocks.ACACIA_PLANKS, Blocks.JUNGLE_PLANKS);

	protected static final ArrayList<Block> STONES = Lists.newArrayList(Blocks.COBBLESTONE, Blocks.STONE_BRICKS,
			Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.MOSSY_COBBLESTONE, Blocks.MOSSY_STONE_BRICKS,
			Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, Blocks.CRACKED_STONE_BRICKS);

	protected static final ArrayList<Block> BEES = Lists.newArrayList(Blocks.BEEHIVE, Blocks.BEE_NEST);
	protected final HashMap<Block, Block> COLORS = new HashMap<Block, Block>();
	protected boolean defaultValue = true;

	protected final ArrayList<BlockPos> CHANGED_POS = new ArrayList<BlockPos>();

	public ColorStructurePiece(IStructurePieceType pieceType, TemplateManager templateManager,
			ResourceLocation location, BlockPos pos, Rotation rotation, int componentTypeIn, boolean defaultValue) {
		super(pieceType, templateManager, location, pos, rotation, componentTypeIn);
		this.defaultValue = defaultValue;
	}

	public ColorStructurePiece(IStructurePieceType pieceType, TemplateManager templateManager, CompoundNBT cNBT) {
		super(pieceType, templateManager, cNBT);
		if (cNBT.contains("DefaultValue"))
			this.defaultValue = cNBT.getBoolean("DefaultValue");
	}

	/**
	 * (abstract) Helper method to read subclass data from NBT
	 */
	protected void addAdditionalSaveData(CompoundNBT tagCompound) {
		super.addAdditionalSaveData(tagCompound);
		tagCompound.putBoolean("DefaultValue", this.defaultValue);
	}

	public boolean postProcess(ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGen,
			Random rand, MutableBoundingBox mbb, ChunkPos chunkPos, BlockPos blockPos) {
		boolean flag = super.postProcess(world, structureManager, chunkGen, rand, mbb, chunkPos, blockPos);
		if (this.useRandomVarianting()) {
			if (this.overwriteWool()) {
				for (Block block : WOOLS) {
					if (COLORS.get(block) == null)
						COLORS.put(block, WOOLS.get(rand.nextInt(WOOLS.size())));
					BlockState newBlock = COLORS.get(block).defaultBlockState();
					for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
							this.placeSettings, block)) {
						if (block == Blocks.BLACK_WOOL)
							newBlock = WOOLS.get(rand.nextInt(WOOLS.size())).defaultBlockState();
						this.changeBlock(template$blockinfo.pos, newBlock, world);
					}
				}
			}
			if (this.overwriteWood()) {
				for (Block block : PLANKS) {
					if (COLORS.get(block) == null)
						COLORS.put(block, PLANKS.get(rand.nextInt(PLANKS.size())));
					Block newBlock = COLORS.get(block);
					if (this.overwritePlanks()) {
						for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
								this.placeSettings, block)) {
							this.changeBlock(template$blockinfo.pos, newBlock.defaultBlockState(), world);
						}
					}
					if (this.overwriteSlabs()) {
						for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
								this.placeSettings, this.getSlab(block))) {
							this.changeBlock(template$blockinfo.pos, this.getSlab(newBlock).defaultBlockState()
									.setValue(SlabBlock.TYPE, template$blockinfo.state.getValue(SlabBlock.TYPE)), world);
						}
					}
					if (this.overwriteFences()) {
						for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
								this.placeSettings, this.getFence(block))) {
							this.changeBlock(template$blockinfo.pos, this.getFence(newBlock).defaultBlockState()
									.setValue(FenceBlock.NORTH, template$blockinfo.state.getValue(FenceBlock.NORTH))
									.setValue(FenceBlock.EAST, template$blockinfo.state.getValue(FenceBlock.EAST))
									.setValue(FenceBlock.SOUTH, template$blockinfo.state.getValue(FenceBlock.SOUTH))
									.setValue(FenceBlock.WEST, template$blockinfo.state.getValue(FenceBlock.WEST))
									.setValue(FenceBlock.WATERLOGGED, template$blockinfo.state.getValue(FenceBlock.WATERLOGGED)),
									world);
						}
					}
					if (this.overwriteLogs()) {
						for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
								this.placeSettings, this.getLog(block, false))) {
							this.changeBlock(template$blockinfo.pos,
									this.getLog(newBlock, false).defaultBlockState().setValue(RotatedPillarBlock.AXIS,
											template$blockinfo.state.getValue(RotatedPillarBlock.AXIS)),
									world);
						}
					}
					if (this.overwriteStrippedLogs()) {
						for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
								this.placeSettings, this.getLog(block, true))) {
							this.changeBlock(template$blockinfo.pos,
									this.getLog(newBlock, true).defaultBlockState().setValue(RotatedPillarBlock.AXIS,
											template$blockinfo.state.getValue(RotatedPillarBlock.AXIS)),
									world);
						}
					}
					if (this.overwriteTrapdoors()) {
						for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
								this.placeSettings, this.getTrapdoor(block))) {
							this.changeBlock(template$blockinfo.pos, this.getTrapdoor(newBlock).defaultBlockState()
									.setValue(TrapDoorBlock.HALF, template$blockinfo.state.getValue(TrapDoorBlock.HALF))
									.setValue(TrapDoorBlock.OPEN, template$blockinfo.state.getValue(TrapDoorBlock.OPEN))
									.setValue(TrapDoorBlock.POWERED, template$blockinfo.state.getValue(TrapDoorBlock.POWERED))
									.setValue(TrapDoorBlock.WATERLOGGED,
											template$blockinfo.state.getValue(TrapDoorBlock.WATERLOGGED))
									.setValue(TrapDoorBlock.FACING,
											template$blockinfo.state.getValue(TrapDoorBlock.FACING)),
									world);
						}
					}
					if (this.overwriteDoors()) {
						for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
								this.placeSettings, this.getDoor(block))) {
							this.changeBlock(template$blockinfo.pos,
									this.getDoor(newBlock).defaultBlockState()
											.setValue(DoorBlock.OPEN, template$blockinfo.state.getValue(DoorBlock.OPEN))
											.setValue(DoorBlock.HINGE, template$blockinfo.state.getValue(DoorBlock.HINGE))
											.setValue(DoorBlock.FACING, template$blockinfo.state.getValue(DoorBlock.FACING))
											.setValue(DoorBlock.HALF, template$blockinfo.state.getValue(DoorBlock.HALF))
											.setValue(DoorBlock.POWERED, template$blockinfo.state.getValue(DoorBlock.POWERED)),
									world);
						}
					}
					if (this.overwriteStairs()) {
						for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
								this.placeSettings, this.getStairs(block))) {
							this.changeBlock(template$blockinfo.pos,
									this.getStairs(newBlock).defaultBlockState()
											.setValue(StairsBlock.WATERLOGGED,
													template$blockinfo.state.getValue(StairsBlock.WATERLOGGED))
											.setValue(StairsBlock.FACING, template$blockinfo.state.getValue(StairsBlock.FACING))
											.setValue(StairsBlock.HALF, template$blockinfo.state.getValue(StairsBlock.HALF))
											.setValue(StairsBlock.SHAPE, template$blockinfo.state.getValue(StairsBlock.SHAPE)),
									world);
						}
					}
					if (this.overwriteSigns()) {
						for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
								this.placeSettings, this.getSign(block, true))) {
							this.changeBlock(template$blockinfo.pos, this.getSign(newBlock, true).defaultBlockState()
									.setValue(AbstractSignBlock.WATERLOGGED,
											template$blockinfo.state.getValue(AbstractSignBlock.WATERLOGGED))
									.setValue(WallSignBlock.FACING, template$blockinfo.state.getValue(WallSignBlock.FACING)),
									world);
						}
						for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
								this.placeSettings, this.getSign(block, false))) {
							this.changeBlock(template$blockinfo.pos,
									this.getSign(newBlock, true).defaultBlockState()
											.setValue(AbstractSignBlock.WATERLOGGED,
													template$blockinfo.state.getValue(AbstractSignBlock.WATERLOGGED))
											.setValue(StandingSignBlock.ROTATION,
													template$blockinfo.state.getValue(StandingSignBlock.ROTATION)),
									world);
						}
					}
					if (this.overwriteButtons()) {
						for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
								this.placeSettings, this.getButton(block))) {
							this.changeBlock(template$blockinfo.pos,
									this.getButton(newBlock).defaultBlockState().setValue(AbstractButtonBlock.POWERED,
											template$blockinfo.state.getValue(AbstractButtonBlock.POWERED)),
									world);
						}
					}
				}
			}
			if (this.overwriteTerracotta()) {
				for (Block block : TERRACOTTAS) {
					if (COLORS.get(block) == null)
						COLORS.put(block, TERRACOTTAS.get(rand.nextInt(TERRACOTTAS.size())));
					BlockState newBlock = COLORS.get(block).defaultBlockState();
					for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
							this.placeSettings, block)) {
						if (block == Blocks.BLACK_TERRACOTTA)
							newBlock = TERRACOTTAS.get(rand.nextInt(TERRACOTTAS.size())).defaultBlockState();
						this.changeBlock(template$blockinfo.pos, newBlock, world);
					}
				}
			}
			if (this.overwriteGlazedTerracotta()) {
				for (Block block : GLAZED_TERRACOTTAS) {
					if (COLORS.get(block) == null)
						COLORS.put(block, GLAZED_TERRACOTTAS.get(rand.nextInt(GLAZED_TERRACOTTAS.size())));
					BlockState newBlock = COLORS.get(block).defaultBlockState();
					for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
							this.placeSettings, block)) {
						if (block == Blocks.BLACK_GLAZED_TERRACOTTA)
							newBlock = GLAZED_TERRACOTTAS.get(rand.nextInt(GLAZED_TERRACOTTAS.size()))
									.defaultBlockState();
						this.changeBlock(template$blockinfo.pos, newBlock, world);
					}
				}
			}
			if (this.overwriteConcrete()) {
				for (Block block : CONCRETE) {
					if (COLORS.get(block) == null)
						COLORS.put(block, CONCRETE.get(rand.nextInt(CONCRETE.size())));
					BlockState newBlock = COLORS.get(block).defaultBlockState();
					for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
							this.placeSettings, block)) {
						if (block == Blocks.BLACK_CONCRETE)
							newBlock = CONCRETE.get(rand.nextInt(CONCRETE.size())).defaultBlockState();
						this.changeBlock(template$blockinfo.pos, newBlock, world);
					}
				}
			}
			if (this.overwriteConcretePowder()) {
				for (Block block : CONCRETE_POWDERS) {
					if (COLORS.get(block) == null)
						COLORS.put(block, CONCRETE_POWDERS.get(rand.nextInt(CONCRETE_POWDERS.size())));
					BlockState newBlock = COLORS.get(block).defaultBlockState();
					for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
							this.placeSettings, block)) {
						if (block == Blocks.BLACK_CONCRETE_POWDER)
							newBlock = CONCRETE_POWDERS.get(rand.nextInt(CONCRETE_POWDERS.size())).defaultBlockState();
						this.changeBlock(template$blockinfo.pos, newBlock, world);
					}
				}
			}
			if (this.overwriteStone()) {
				for (Block block : STONES) {
					if (COLORS.get(block) == null)
						COLORS.put(block, STONES.get(rand.nextInt(STONES.size())));
					Block newBlock = COLORS.get(block);
					Block newBlock2 = newBlock;
					for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
							this.placeSettings, block)) {
						newBlock2 = newBlock;
						if (rand.nextFloat() < this.getStoneChangeChance())
							newBlock2 = STONES.get(rand.nextInt(STONES.size()));
						this.changeBlock(template$blockinfo.pos, newBlock2.defaultBlockState(), world);
					}
					if (this.overwriteSlabs()) {
						for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
								this.placeSettings, this.getSlab(block))) {
							newBlock2 = newBlock;
							if (rand.nextFloat() < this.getStoneChangeChance())
								newBlock2 = STONES.get(rand.nextInt(STONES.size()));
							this.changeBlock(template$blockinfo.pos, this.getSlab(newBlock2).defaultBlockState()
									.setValue(SlabBlock.TYPE, template$blockinfo.state.getValue(SlabBlock.TYPE)), world);
						}
					}
					if (this.overwriteStairs()) {
						for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
								this.placeSettings, this.getStairs(block))) {
							newBlock2 = newBlock;
							if (rand.nextFloat() < this.getStoneChangeChance())
								newBlock2 = STONES.get(rand.nextInt(STONES.size()));
							this.changeBlock(template$blockinfo.pos,
									this.getStairs(newBlock2).defaultBlockState()
											.setValue(StairsBlock.WATERLOGGED,
													template$blockinfo.state.getValue(StairsBlock.WATERLOGGED))
											.setValue(StairsBlock.FACING, template$blockinfo.state.getValue(StairsBlock.FACING))
											.setValue(StairsBlock.HALF, template$blockinfo.state.getValue(StairsBlock.HALF))
											.setValue(StairsBlock.SHAPE, template$blockinfo.state.getValue(StairsBlock.SHAPE)),
									world);
						}
					}
				}
			}
		}
		if (this.overwriteBeehives()) {
			for (Block block : BEES) {
				if (COLORS.get(block) == null)
					COLORS.put(block, BEES.get(rand.nextInt(BEES.size())));
				BlockState newBlock = COLORS.get(block).defaultBlockState();
				for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
						this.placeSettings, block)) {
					this.changeBlock(template$blockinfo.pos,
							newBlock.setValue(BeehiveBlock.FACING, template$blockinfo.state.getValue(BeehiveBlock.FACING)).setValue(
									BeehiveBlock.HONEY_LEVEL, template$blockinfo.state.getValue(BeehiveBlock.HONEY_LEVEL)),
							world);
				}
			}
		}
		return flag;
	}

	protected abstract boolean useRandomVarianting();

	protected boolean changeBlock(BlockPos pos, BlockState state, ISeedReader world) {
		if (!CHANGED_POS.contains(pos) && this.validateBlock(pos, state, world)) {
			CHANGED_POS.add(pos);
			world.setBlock(pos, state, 3);
		}
		return false;
	}

	public boolean validateBlock(BlockPos pos, BlockState newState, ISeedReader world) {
		return true;
	}

	protected Block getSlab(Block plank) {
		if (plank == Blocks.OAK_PLANKS) {
			return Blocks.OAK_SLAB;
		} else if (plank == Blocks.DARK_OAK_PLANKS) {
			return Blocks.DARK_OAK_SLAB;
		} else if (plank == Blocks.BIRCH_PLANKS) {
			return Blocks.BIRCH_SLAB;
		} else if (plank == Blocks.JUNGLE_PLANKS) {
			return Blocks.JUNGLE_SLAB;
		} else if (plank == Blocks.ACACIA_PLANKS) {
			return Blocks.ACACIA_SLAB;
		} else if (plank == Blocks.COBBLESTONE) {
			return Blocks.COBBLESTONE_SLAB;
		} else if (plank == Blocks.STONE_BRICKS) {
			return Blocks.STONE_BRICK_SLAB;
		} else if (plank == Blocks.POLISHED_BLACKSTONE_BRICKS) {
			return Blocks.POLISHED_BLACKSTONE_BRICK_SLAB;
		} else if (plank == Blocks.MOSSY_COBBLESTONE) {
			return Blocks.MOSSY_COBBLESTONE_SLAB;
		} else if (plank == Blocks.MOSSY_STONE_BRICKS) {
			return Blocks.MOSSY_STONE_BRICK_SLAB;
		} else if (plank == Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS) {
			return Blocks.POLISHED_BLACKSTONE_BRICK_SLAB;
		} else if (plank == Blocks.CRACKED_STONE_BRICKS) {
			return Blocks.STONE_BRICK_SLAB;
		} else {
			return Blocks.SPRUCE_SLAB;
		}
	}

	protected Block getButton(Block plank) {
		if (plank == Blocks.OAK_PLANKS) {
			return Blocks.OAK_BUTTON;
		} else if (plank == Blocks.DARK_OAK_PLANKS) {
			return Blocks.DARK_OAK_BUTTON;
		} else if (plank == Blocks.BIRCH_PLANKS) {
			return Blocks.BIRCH_BUTTON;
		} else if (plank == Blocks.JUNGLE_PLANKS) {
			return Blocks.JUNGLE_BUTTON;
		} else if (plank == Blocks.ACACIA_PLANKS) {
			return Blocks.ACACIA_BUTTON;
		} else {
			return Blocks.SPRUCE_BUTTON;
		}
	}

	protected Block getFence(Block plank) {
		if (plank == Blocks.OAK_PLANKS) {
			return Blocks.OAK_FENCE;
		} else if (plank == Blocks.DARK_OAK_PLANKS) {
			return Blocks.DARK_OAK_FENCE;
		} else if (plank == Blocks.BIRCH_PLANKS) {
			return Blocks.BIRCH_FENCE;
		} else if (plank == Blocks.JUNGLE_PLANKS) {
			return Blocks.JUNGLE_FENCE;
		} else if (plank == Blocks.ACACIA_PLANKS) {
			return Blocks.ACACIA_FENCE;
		} else {
			return Blocks.SPRUCE_FENCE;
		}
	}

	protected Block getLog(Block plank, boolean stripped) {
		if (plank == Blocks.OAK_PLANKS) {
			if (stripped)
				return Blocks.STRIPPED_OAK_LOG;
			else
				return Blocks.OAK_LOG;
		} else if (plank == Blocks.DARK_OAK_PLANKS) {
			if (stripped)
				return Blocks.STRIPPED_DARK_OAK_LOG;
			else
				return Blocks.DARK_OAK_LOG;
		} else if (plank == Blocks.BIRCH_PLANKS) {
			if (stripped)
				return Blocks.STRIPPED_BIRCH_LOG;
			else
				return Blocks.BIRCH_LOG;
		} else if (plank == Blocks.JUNGLE_PLANKS) {
			if (stripped)
				return Blocks.STRIPPED_JUNGLE_LOG;
			else
				return Blocks.JUNGLE_LOG;
		} else if (plank == Blocks.ACACIA_PLANKS) {
			if (stripped)
				return Blocks.STRIPPED_ACACIA_LOG;
			else
				return Blocks.ACACIA_LOG;
		} else {
			if (stripped)
				return Blocks.STRIPPED_SPRUCE_LOG;
			else
				return Blocks.SPRUCE_LOG;
		}
	}

	protected Block getTrapdoor(Block plank) {
		if (plank == Blocks.OAK_PLANKS) {
			return Blocks.OAK_TRAPDOOR;
		} else if (plank == Blocks.DARK_OAK_PLANKS) {
			return Blocks.DARK_OAK_TRAPDOOR;
		} else if (plank == Blocks.BIRCH_PLANKS) {
			return Blocks.BIRCH_TRAPDOOR;
		} else if (plank == Blocks.JUNGLE_PLANKS) {
			return Blocks.JUNGLE_TRAPDOOR;
		} else if (plank == Blocks.ACACIA_PLANKS) {
			return Blocks.ACACIA_TRAPDOOR;
		} else {
			return Blocks.SPRUCE_TRAPDOOR;
		}
	}

	protected Block getDoor(Block plank) {
		if (plank == Blocks.OAK_PLANKS) {
			return Blocks.OAK_DOOR;
		} else if (plank == Blocks.DARK_OAK_PLANKS) {
			return Blocks.DARK_OAK_DOOR;
		} else if (plank == Blocks.BIRCH_PLANKS) {
			return Blocks.BIRCH_DOOR;
		} else if (plank == Blocks.JUNGLE_PLANKS) {
			return Blocks.JUNGLE_DOOR;
		} else if (plank == Blocks.ACACIA_PLANKS) {
			return Blocks.ACACIA_DOOR;
		} else {
			return Blocks.SPRUCE_DOOR;
		}
	}

	protected Block getStairs(Block plank) {
		if (plank == Blocks.OAK_PLANKS) {
			return Blocks.OAK_STAIRS;
		} else if (plank == Blocks.DARK_OAK_PLANKS) {
			return Blocks.DARK_OAK_STAIRS;
		} else if (plank == Blocks.BIRCH_PLANKS) {
			return Blocks.BIRCH_STAIRS;
		} else if (plank == Blocks.JUNGLE_PLANKS) {
			return Blocks.JUNGLE_STAIRS;
		} else if (plank == Blocks.ACACIA_PLANKS) {
			return Blocks.ACACIA_STAIRS;
		} else if (plank == Blocks.COBBLESTONE) {
			return Blocks.COBBLESTONE_STAIRS;
		} else if (plank == Blocks.STONE_BRICKS) {
			return Blocks.STONE_BRICK_STAIRS;
		} else if (plank == Blocks.POLISHED_BLACKSTONE_BRICKS) {
			return Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS;
		} else if (plank == Blocks.MOSSY_COBBLESTONE) {
			return Blocks.MOSSY_COBBLESTONE_STAIRS;
		} else if (plank == Blocks.MOSSY_STONE_BRICKS) {
			return Blocks.MOSSY_STONE_BRICK_STAIRS;
		} else if (plank == Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS) {
			return Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS;
		} else if (plank == Blocks.CRACKED_STONE_BRICKS) {
			return Blocks.STONE_BRICK_STAIRS;
		} else {
			return Blocks.SPRUCE_STAIRS;
		}
	}

	protected Block getSign(Block plank, boolean wall) {
		if (plank == Blocks.OAK_PLANKS) {
			if (wall)
				return Blocks.OAK_WALL_SIGN;
			else
				return Blocks.OAK_SIGN;
		} else if (plank == Blocks.DARK_OAK_PLANKS) {
			if (wall)
				return Blocks.DARK_OAK_WALL_SIGN;
			else
				return Blocks.DARK_OAK_SIGN;
		} else if (plank == Blocks.BIRCH_PLANKS) {
			if (wall)
				return Blocks.BIRCH_WALL_SIGN;
			else
				return Blocks.BIRCH_SIGN;
		} else if (plank == Blocks.JUNGLE_PLANKS) {
			if (wall)
				return Blocks.JUNGLE_WALL_SIGN;
			else
				return Blocks.JUNGLE_SIGN;
		} else if (plank == Blocks.ACACIA_PLANKS) {
			if (wall)
				return Blocks.ACACIA_WALL_SIGN;
			else
				return Blocks.ACACIA_SIGN;
		} else {
			if (wall)
				return Blocks.SPRUCE_WALL_SIGN;
			else
				return Blocks.SPRUCE_SIGN;
		}
	}

	@Override
	protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand,
			MutableBoundingBox sbb) {
		Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(function));
		if (block != Blocks.AIR) {
			worldIn.setBlock(pos, block.defaultBlockState(), 2);
			CHANGED_POS.add(pos);
		}
	}

	public boolean overwriteWool() {
		return this.defaultValue;
	}

	public boolean overwriteWood() {
		return this.defaultValue;
	}

	public boolean overwritePlanks() {
		return this.defaultValue;
	}

	public boolean overwriteSlabs() {
		return this.defaultValue;
	}

	public boolean overwriteFences() {
		return this.defaultValue;
	}

	public boolean overwriteLogs() {
		return this.defaultValue;
	}

	public boolean overwriteStrippedLogs() {
		return this.defaultValue;
	}

	public boolean overwriteSigns() {
		return this.defaultValue;
	}

	public boolean overwriteStairs() {
		return this.defaultValue;
	}

	public boolean overwriteDoors() {
		return this.defaultValue;
	}

	public boolean overwriteTrapdoors() {
		return this.defaultValue;
	}

	public boolean overwriteTerracotta() {
		return this.defaultValue;
	}

	public boolean overwriteGlazedTerracotta() {
		return this.defaultValue;
	}

	public boolean overwriteConcretePowder() {
		return this.defaultValue;
	}

	public boolean overwriteConcrete() {
		return this.defaultValue;
	}

	public boolean overwriteStone() {
		return this.defaultValue;
	}

	public float getStoneChangeChance() {
		return 0.05F;
	}

	public boolean overwriteButtons() {
		return this.defaultValue;
	}

	public boolean overwriteBeehives() {
		return this.defaultValue;
	}
}