package com.silverminer.moreore.world.gen.structures;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import com.silverminer.moreore.MoreOre;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.jigsaw.IJigsawDeserializer;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;
import net.minecraft.world.gen.feature.template.*;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class ModSingleJigsawPiece extends SingleJigsawPiece {
	public static IJigsawDeserializer<SingleJigsawPiece> MOREORE_SINGLE_POOL_ELEMENT = IJigsawDeserializer
			.func_236851_a_("moreore_single_pool_element", ModSingleJigsawPiece.field_236838_b_);

	public ModSingleJigsawPiece(String location) {
		this(location, ImmutableList.of());
	}

	public ModSingleJigsawPiece(String location, List<StructureProcessor> processors) {
		this(location, processors, JigsawPattern.PlacementBehaviour.RIGID);
	}

	@SuppressWarnings("deprecation")
	public ModSingleJigsawPiece(String location, List<StructureProcessor> processors,
			JigsawPattern.PlacementBehaviour placementBehaviour) {
		super(location, processors);
	}

	public ModSingleJigsawPiece(Dynamic<?> dynamic) {
		super(dynamic);
	}

	@Override
	public boolean place(TemplateManager templateManager, @Nonnull IWorld world, ChunkGenerator generator,
			@Nonnull BlockPos position, @Nonnull Rotation rotation, @Nonnull MutableBoundingBox bounds,
			@Nonnull Random random) {
		Template template = templateManager.getTemplateDefaulted(this.location);
		PlacementSettings placementSettings = func_230379_a_(rotation, bounds, false);
		MoreOre.LOGGER.info(String.valueOf(position.getX()) + " " + String.valueOf(position.getY()) + " " + String.valueOf(position.getZ()));
		if (!template.addBlocksToWorld(world, position, placementSettings, 18)) {
			return false;
		} else {
			for (Template.BlockInfo blockInfo : Template.processBlockInfos(template, world, position, placementSettings,
					getDataMarkers(templateManager, position, rotation, false))) {
				this.handleDataMarker(world, blockInfo, position, rotation, random, bounds);
			}

			return true;
		}
	}

	@Override
	protected @Nonnull PlacementSettings func_230379_a_(@Nonnull Rotation rotation,
			@Nonnull MutableBoundingBox bounds, boolean idontknow) {
		PlacementSettings placementSettings = new PlacementSettings();
		placementSettings.setBoundingBox(bounds);
		placementSettings.setRotation(rotation);
		placementSettings.func_215223_c(true);
		placementSettings.setIgnoreEntities(false);
		placementSettings.addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
		placementSettings.addProcessor(JigsawReplacementStructureProcessor.INSTANCE);
		placementSettings.addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
		placementSettings.addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
		this.processors.forEach(placementSettings::addProcessor);
		this.getPlacementBehaviour().getStructureProcessors().forEach(placementSettings::addProcessor);
		return placementSettings;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public @Nonnull IJigsawDeserializer getType() {
		return MOREORE_SINGLE_POOL_ELEMENT;
	}
}
