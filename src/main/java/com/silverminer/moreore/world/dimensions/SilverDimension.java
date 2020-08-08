package com.silverminer.moreore.world.dimensions;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerEntity.SleepResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.Dimension;
import net.minecraft.world.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;

public class SilverDimension extends Dimension{

	private final World world;
	public SilverDimension(World world, DimensionType type) {
		super(world, type, 0.0f);
		this.world = world;
	}
	@Override
	public ChunkGenerator<?> createChunkGenerator() {
//		return new SilverNetherChunkGenerator(world, new SilverBiomeProvider(), new SilverGenSettings());//Für Nether Generator
		return new SilverEndChunkGenerator(this.world, new SilverBiomeProvider(0, false, false), new SilverGenSettings());//Für End Generator
//		return new SilverOverworldChunkGenerator(world, new SilverBiomeProvider(), new SilverGenSettings());//Für Overworld Generator
	}
	@Override
	public BlockPos findSpawn(ChunkPos chunkPosIn, boolean checkValid) {
		return null;
	}
	@Override
	public BlockPos findSpawn(int posX, int posZ, boolean checkValid) {
		return null;
	}
	@Override
	public float calculateCelestialAngle(long worldTime, float partialTicks) {
		int j = 6000;
		float f1 = (j + partialTicks) / 24000.0f - 0.25f;
		if (f1 < 0.0f) {
			f1 += 1.0f;
		}

		if (f1 > 1.0f) {
			f1 -= 1.0f;
		}

		float f2 = f1;
		f1 = 1.0f - (float) ((Math.cos(f1 * Math.PI) + 1.0d) / 2.0d);
		f1 = f2 + (f1 - f2) / 3.0f;
		return f1;
	}

	@Override
	public boolean isSurfaceWorld() {
		return false;
	}

	@Override
	public Vector3d getFogColor(float celestialAngle, float partialTicks) {
		return Vector3d.ZERO;
	}

	@Override
	public boolean canRespawnHere() {
		return true;
	}

	@Override
	public boolean doesXZShowFog(int x, int z) {
		return false;
	}

	@Override
	public SleepResult canSleepAt(PlayerEntity player, BlockPos pos) {
		return SleepResult.OBSTRUCTED;
	}

	@Override
	public int getActualHeight() {
		return 256;
	}
}
