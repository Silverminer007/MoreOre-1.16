package com.silverminer.moreore.common.objects.blocks;

import javax.annotation.Nullable;

import com.silverminer.moreore.common.objects.entitys.GiantZombieKingEntity;
import com.silverminer.moreore.common.tags.ModTags;
import com.silverminer.moreore.init.ModEntityTypesInit;
import com.silverminer.moreore.init.blocks.RuneBlocks;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.pattern.BlockMaterialMatcher;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.BlockStateMatcher;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CachedBlockInfo;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

public class RuneBaseBlock extends Block {

	@Nullable
	private static BlockPattern giantZombieKingPattern;

	public RuneBaseBlock(Properties properties) {
		super(properties);
	}

	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer,
			ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		checkGiantZombieKingSpawn(worldIn, pos, state);
	}

	public static void checkGiantZombieKingSpawn(World worldIn, BlockPos pos, BlockState blockstate) {
		if (!worldIn.isRemote) {
			boolean flag = blockstate.isIn(RuneBlocks.RUNE_BASE_BLOCK.get());
			if (flag && pos.getY() >= 0 && worldIn.getDifficulty() != Difficulty.PEACEFUL) {
				BlockPattern blockpattern = getOrCreateGiantZombieKing();
				BlockPattern.PatternHelper blockpattern$patternhelper = blockpattern.match(worldIn, pos);
				if (blockpattern$patternhelper != null) {
					for (int i = 0; i < blockpattern.getPalmLength(); ++i) {
						for (int j = 0; j < blockpattern.getThumbLength(); ++j) {
							CachedBlockInfo cachedblockinfo = blockpattern$patternhelper.translateOffset(i, j, 0);
							worldIn.setBlockState(cachedblockinfo.getPos(), Blocks.AIR.getDefaultState(), 2);
							worldIn.playEvent(2001, cachedblockinfo.getPos(),
									Block.getStateId(cachedblockinfo.getBlockState()));
						}
					}

					GiantZombieKingEntity zombieKing = ModEntityTypesInit.GIANT_ZOMBIE_KING.get().create(worldIn);
					BlockPos blockpos = blockpattern$patternhelper.translateOffset(1, 2, 0).getPos();
					zombieKing.setLocationAndAngles((double) blockpos.getX() + 0.5D, (double) blockpos.getY() + 0.55D,
							(double) blockpos.getZ() + 0.5D,
							blockpattern$patternhelper.getForwards().getAxis() == Direction.Axis.X ? 0.0F : 90.0F,
							0.0F);
					zombieKing.renderYawOffset = blockpattern$patternhelper.getForwards()
							.getAxis() == Direction.Axis.X ? 0.0F : 90.0F;

					for (ServerPlayerEntity serverplayerentity : worldIn.getEntitiesWithinAABB(ServerPlayerEntity.class,
							zombieKing.getBoundingBox().grow(50.0D))) {
						CriteriaTriggers.SUMMONED_ENTITY.trigger(serverplayerentity, zombieKing);
					}

					worldIn.addEntity(zombieKing);

					for (int k = 0; k < blockpattern.getPalmLength(); ++k) {
						for (int l = 0; l < blockpattern.getThumbLength(); ++l) {
							worldIn.func_230547_a_(blockpattern$patternhelper.translateOffset(k, l, 0).getPos(),
									Blocks.AIR);
						}
					}

				}
			}
		}
	}

	private static BlockPattern getOrCreateGiantZombieKing() {
		if (giantZombieKingPattern == null) {
			giantZombieKingPattern = BlockPatternBuilder.start().aisle("hrh", "ooo", "aca", "aca")
					.where('h',
							CachedBlockInfo.hasState(BlockStateMatcher.forBlock(Blocks.ZOMBIE_HEAD)
									.or(BlockStateMatcher.forBlock(Blocks.ZOMBIE_WALL_HEAD))))
					.where('r', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(RuneBlocks.RUNE_BASE_BLOCK.get())))
					.where('o', (cachedInfo) -> {
						return cachedInfo.getBlockState().isIn(ModTags.GIANT_ZOMBIE_SUMMON_BLOCKS);
					}).where('a', CachedBlockInfo.hasState(BlockMaterialMatcher.forMaterial(Material.AIR)))
					.where('c', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(Blocks.CRYING_OBSIDIAN))).build();
		}
		return giantZombieKingPattern;
	}
}