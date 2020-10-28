package com.silverminer.moreore.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

// Made with Blockbench 3.5.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

public class SquirrelModel<T extends Entity> extends AgeableModel<T> {
	private final ModelRenderer squirrel;
	private final ModelRenderer legs;
	private final ModelRenderer feet;
	private final ModelRenderer body;
	private final ModelRenderer head;
	private final ModelRenderer arms;

	public SquirrelModel() {
		this.textureWidth = 24;
		this.textureHeight = 16;

		this.squirrel = new ModelRenderer(this);
		this.squirrel.setRotationPoint(0.0F, 24.0F, 0.0F);
		this.setRotationAngle(this.squirrel, -0.0873F, 0.0F, 0.0F);

		this.legs = new ModelRenderer(this);
		this.legs.setRotationPoint(0.0F, -4.0F, 0.0F);
		this.squirrel.addChild(this.legs);
		this.legs.setTextureOffset(0, 0).addBox(-4.0F, -1.0F, -4.0F, 1.0F, 4.0F, 4.0F, 0.0F, false);
		this.legs.setTextureOffset(0, 0).addBox(2.0F, -1.0F, -4.0F, 1.0F, 4.0F, 4.0F, 0.0F, false);

		this.feet = new ModelRenderer(this);
		this.feet.setRotationPoint(0.0F, 3.0F, -1.0F);
		this.legs.addChild(this.feet);
		this.setRotationAngle(this.feet, 0.0873F, 0.0F, 0.0F);
		this.feet.setTextureOffset(0, 0).addBox(2.0F, 0.0F, -1.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
		this.feet.setTextureOffset(0, 0).addBox(-4.0F, 0.0F, -1.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);

		this.body = new ModelRenderer(this);
		this.body.setRotationPoint(-1.0F, -4.0F, -2.0F);
		this.squirrel.addChild(this.body);
		this.setRotationAngle(this.body, -1.309F, 0.0F, 0.0F);
		this.body.setTextureOffset(0, 0).addBox(-2.0F, -8.0F, -2.0F, 5.0F, 9.0F, 4.0F, 0.0F, false);

		this.head = new ModelRenderer(this);
		this.head.setRotationPoint(0.0F, -8.0F, 0.0F);
		this.body.addChild(this.head);
		this.setRotationAngle(this.head, -0.0873F, 0.0F, 0.0F);
		this.head.setTextureOffset(0, 0).addBox(-1.0F, -3.0F, -1.0F, 3.0F, 3.0F, 4.0F, 0.0F, false);
		this.head.setTextureOffset(0, 0).addBox(0.0F, -1.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		this.arms = new ModelRenderer(this);
		this.arms.setRotationPoint(0.0F, -6.0F, 0.0F);
		this.body.addChild(this.arms);
		this.arms.setTextureOffset(0, 0).addBox(-3.0F, -1.0F, 0.0F, 1.0F, 2.0F, 4.0F, 0.0F, false);
		this.arms.setTextureOffset(0, 0).addBox(3.0F, -1.0F, 0.0F, 1.0F, 2.0F, 4.0F, 0.0F, false);
	}

	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red,
			float green, float blue, float alpha) {
		super.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	public Iterable<ModelRenderer> getParts() {
		return ImmutableList.of(this.arms, this.body, this.feet, this.head, this.legs);
	}

	public ModelRenderer getModelHead() {
		return this.head;
	}

	@Override
	protected Iterable<ModelRenderer> getHeadParts() {
		return ImmutableList.of();
	}

	@Override
	protected Iterable<ModelRenderer> getBodyParts() {
		return this.getParts();
	}

	@Override
	public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks,
			float netHeadYaw, float headPitch) {
		// TODO Auto-generated method stub

	}
}