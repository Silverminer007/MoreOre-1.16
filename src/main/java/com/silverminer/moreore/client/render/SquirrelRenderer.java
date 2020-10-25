package com.silverminer.moreore.client.render;

import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.client.model.SquirrelModel;
import com.silverminer.moreore.common.objects.entitys.SquirrelEntity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class SquirrelRenderer<T extends Entity> extends MobRenderer<SquirrelEntity, SquirrelModel<SquirrelEntity>> {
	protected static final ResourceLocation TEXTURE = new ResourceLocation(MoreOre.MODID,
			"textures/entity/squirrel.png");

	public SquirrelRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new SquirrelModel<SquirrelEntity>(), 0.5f);
	}

	@Override
	public ResourceLocation getEntityTexture(SquirrelEntity entity) {
		return TEXTURE;
	}
}
