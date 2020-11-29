package com.silverminer.moreore.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.moreore.client.model.GiantZombieKingModel;
import com.silverminer.moreore.common.objects.entitys.GiantZombieKingEntity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GiantZombieKingRenderer extends MobRenderer<GiantZombieKingEntity, BipedModel<GiantZombieKingEntity>> {
	private static final ResourceLocation ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/zombie.png");
	private final float scale;

	public GiantZombieKingRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new GiantZombieKingModel(), 0.5F * 8.0F);
		this.scale = 8.0F;
		this.addLayer(new HeldItemLayer<>(this));
		this.addLayer(new BipedArmorLayer<>(this, new GiantZombieKingModel(0.5F, true),
				new GiantZombieKingModel(1.0F, true)));
	}

	protected void preRenderCallback(GiantZombieKingEntity entitylivingbaseIn, MatrixStack matrixStackIn,
			float partialTickTime) {
		matrixStackIn.scale(this.scale, this.scale, this.scale);
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	public ResourceLocation getEntityTexture(GiantZombieKingEntity entity) {
		return ZOMBIE_TEXTURES;
	}
}