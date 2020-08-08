package com.silverminer.moreore.world.dimensions;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraft.world.gen.NoiseChunkGenerator;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.settings.DimensionGeneratorSettings;

public class SilverEndChunkGenerator extends ChunkGenerator {
	@SuppressWarnings("unused")
	private final BlockPos spawnPoint;

	public SilverEndChunkGenerator(IWorld worldIn, BiomeProvider biomeProviderIn, SilverGenSettings settingsIn) {
		super(worldIn, biomeProviderIn, 8, 4, 128, settingsIn, true);
		this.spawnPoint = settingsIn.getSpawnPos();
	}

	protected void fillNoiseColumn(double[] noiseColumn, int noiseX, int noiseZ) {
		double d0 = 1368.824D;
		double d1 = 684.412D;
		double d2 = 17.110300000000002D;
		double d3 = 4.277575000000001D;
		int i = 64;
		int j = -3000;
		this.calcNoiseColumn(noiseColumn, noiseX, noiseZ, d0, d1, d2, d3, i, j);
	}

	protected double[] getBiomeNoiseColumn(int noiseX, int noiseZ) {
		return new double[] { (double) this.biomeProvider.func_222365_c(noiseX, noiseZ), 0.0D };
	}

	protected double func_222545_a(double p_222545_1_, double p_222545_3_, int p_222545_5_) {
		return 8.0D - p_222545_1_;
	}

	protected double func_222551_g() {
		return (double) ((int) super.func_222551_g() / 2);
	}

	protected double func_222553_h() {
		return 8.0D;
	}

	public int getGroundHeight() {
		return 50;
	}

	public int getSeaLevel() {
		return 0;
	}

	protected void makeBedrock(IChunk chunkIn, Random rand) {
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
		int i = chunkIn.getPos().getXStart();
		int j = chunkIn.getPos().getZStart();
		DimensionGeneratorSettings t = this.getSettings();
		if(!(t instanceof DimensionGeneratorSettings)) return;
		int k = ((SilverGenSettings) t).getBedrockFloorHeight();
		for (BlockPos blockpos : BlockPos.getAllInBoxMutable(i, 0, j, i + 15, 0, j + 15)) {
			if (k < 256) {
				for (int j1 = k + 4; j1 >= k; --j1) {
					if (j1 >= 2 && chunkIn.getBlockState(blockpos) == Blocks.AIR.getDefaultState()) {
						chunkIn.setBlockState(blockpos$mutable.setPos(blockpos.getX(), j1, blockpos.getZ()),
								((SilverGenSettings) t).getDefaultFluid(), false);
					}
					if (1 >= j1) {
						chunkIn.setBlockState(blockpos$mutable.setPos(blockpos.getX(), j1, blockpos.getZ()),
								Blocks.BEDROCK.getDefaultState(), false);
					}
					if (j1 == 2) {
						chunkIn.setBlockState(blockpos$mutable.setPos(blockpos.getX(), j1, blockpos.getZ()),
								Blocks.DIRT.getDefaultState(), false);
					}
					if (j1 == rand.nextInt(4) && j1 > 1) {
						chunkIn.setBlockState(blockpos$mutable.setPos(blockpos.getX(), j1, blockpos.getZ()),
								Blocks.DIRT.getDefaultState(), false);
					}
				}
			}
		}
	}

	@Override
	protected Codec<? extends ChunkGenerator> func_230347_a_() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChunkGenerator func_230349_a_(long p_230349_1_) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void generateSurface(WorldGenRegion p_225551_1_, IChunk p_225551_2_) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void func_230352_b_(IWorld p_230352_1_, StructureManager p_230352_2_, IChunk p_230352_3_) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int func_222529_a(int p_222529_1_, int p_222529_2_, Type heightmapType) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IBlockReader func_230348_a_(int p_230348_1_, int p_230348_2_) {
		// TODO Auto-generated method stub
		return null;
	}
}
