package com.silverminer.moreore.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

// Made with Blockbench 3.7.2
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

public class SquirrelModel<T extends Entity> extends AgeableModel<T> {
	private final ModelRenderer body;
	private final ModelRenderer belly;
	public final ModelRenderer head;
	private final ModelRenderer ears;
	private final ModelRenderer legs;
	private final ModelRenderer legsBack;
	private final ModelRenderer legsFront;
	private final ModelRenderer tail;
	private final ModelRenderer tail1;
	private final ModelRenderer part_r1;
	private final ModelRenderer tail2;
	private final ModelRenderer tail3;

	public SquirrelModel() {
		textureWidth = 64;
		textureHeight = 64;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 24.0F, 0.0F);
		setRotationAngle(body, 0.0F, 3.1416F, 0.0F);

		belly = new ModelRenderer(this);
		belly.setRotationPoint(-4.0F, -2.0F, 0.0F);
		body.addChild(belly);
		belly.setTextureOffset(0, 0).addBox(-4.0F, -6.0F, 0.0F, 8.0F, 6.0F, 12.0F, 0.0F, false);

		legs = new ModelRenderer(this);
		legs.setRotationPoint(-2.0F, 0.0F, 4.0F);
		belly.addChild(legs);

		legsBack = new ModelRenderer(this);
		legsBack.setRotationPoint(2.0F, -1.0F, -3.0F);
		legs.addChild(legsBack);
		legsBack.setTextureOffset(26, 18).addBox(1.8F, -1.0F, -0.8F, 2.0F, 4.0F, 2.0F, 0.0F, false);
		legsBack.setTextureOffset(18, 18).addBox(-3.8F, -1.0F, -0.8F, 2.0F, 4.0F, 2.0F, 0.0F, false);

		legsFront = new ModelRenderer(this);
		legsFront.setRotationPoint(2.0F, -1.0F, 7.0F);
		legs.addChild(legsFront);
		legsFront.setTextureOffset(0, 6).addBox(1.8F, -1.0F, -1.2F, 2.0F, 4.0F, 2.0F, 0.0F, false);
		legsFront.setTextureOffset(0, 0).addBox(-3.8F, -1.0F, -1.2F, 2.0F, 4.0F, 2.0F, 0.0F, false);

		head = new ModelRenderer(this);
		head.setRotationPoint(-4.0F, -7.0F, 12.0F);
		body.addChild(head);
		head.setTextureOffset(0, 30).addBox(-3.0F, -5.0F, -1.0F, 6.0F, 6.0F, 6.0F, 0.0F, false);

		ears = new ModelRenderer(this);
		ears.setRotationPoint(0.0F, -4.0F, 1.0F);
		head.addChild(ears);
		ears.setTextureOffset(8, 4).addBox(-2.8F, -3.0F, -1.0F, 0.0F, 4.0F, 2.0F, 0.0F, false);
		ears.setTextureOffset(8, 0).addBox(2.8F, -3.0F, -1.0F, 0.0F, 4.0F, 2.0F, 0.0F, false);

		tail = new ModelRenderer(this);
		tail.setRotationPoint(-4.0F, -8.0F, 0.0F);
		body.addChild(tail);

		tail1 = new ModelRenderer(this);
		tail1.setRotationPoint(0.0F, 0.0F, 0.0F);
		tail.addChild(tail1);
		setRotationAngle(tail1, 0.1745F, 0.0F, 0.0F);
		tail1.setTextureOffset(28, 0).addBox(-3.0F, -5.0F, -5.0F, 6.0F, 6.0F, 6.0F, 0.0F, false);

		tail2 = new ModelRenderer(this);
		tail2.setRotationPoint(0.0F, -4.0F, 0.0F);
		tail1.addChild(tail2);
		setRotationAngle(tail2, -0.1745F, 0.0F, 0.0F);
		tail2.setTextureOffset(18, 24).addBox(-3.0F, -7.0F, -5.0F, 6.0F, 6.0F, 6.0F, 0.0F, false);

		tail3 = new ModelRenderer(this);
		tail3.setRotationPoint(0.0F, -6.0F, 0.0F);
		tail2.addChild(tail3);
		setRotationAngle(tail3, -0.1309F, 0.0F, 0.0436F);

		part_r1 = new ModelRenderer(this);
		part_r1.setRotationPoint(0.0F, -1.0F, 0.5F);
		tail3.addChild(part_r1);
		setRotationAngle(part_r1, 0.0F, 0.0F, -0.0436F);
		part_r1.setTextureOffset(0, 18).addBox(-3.0F, -6.0F, -5.5F, 6.0F, 6.0F, 6.0F, 0.0F, false);
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red,
			float green, float blue, float alpha) {
		float bodyScale = this.isChild ? 4.0F : 2.0F;
		matrixStack.push();
		float f1 = 1.0F / bodyScale;
		matrixStack.scale(f1, f1, f1);
		matrixStack.translate(0.0D, this.isChild ? 4.5D : 1.5D, 0.0D);
		this.getBodyParts().forEach((modelRenderer) -> {
			modelRenderer.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		});
		matrixStack.pop();
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	@Override
	protected Iterable<ModelRenderer> getHeadParts() {
		return ImmutableList.of();
	}

	@Override
	protected Iterable<ModelRenderer> getBodyParts() {
		return ImmutableList.of(this.body);
	}

	@Override
	public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks,
			float netHeadYaw, float headPitch) {
		this.tail.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 0.5F * limbSwingAmount + 0.3F;
		this.tail1.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F - (float) Math.PI) * 0.5F * limbSwingAmount;
		this.tail2.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 0.5F * limbSwingAmount;
		this.legsBack.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.legsFront.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
		this.head.rotateAngleX = headPitch * ((float) Math.PI / 180F) * 0.2F;
		this.head.rotateAngleY = netHeadYaw * ((float) Math.PI / 180F);
	}
}