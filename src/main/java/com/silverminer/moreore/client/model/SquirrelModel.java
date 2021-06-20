package com.silverminer.moreore.client.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.silverminer.moreore.common.objects.entitys.SquirrelEntity;

import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

// Made with Blockbench 3.7.2
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

public class SquirrelModel<T extends SquirrelEntity> extends AgeableModel<T> {
	protected static final Logger LOGGER = LogManager.getLogger(SquirrelModel.class);
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
		texWidth = 64;
		texHeight = 64;

		body = new ModelRenderer(this);
		body.setPos(0.0F, 24.0F, 0.0F);
		setRotationAngle(body, 0.0F, 3.1416F, 0.0F);

		belly = new ModelRenderer(this);
		belly.setPos(-4.0F, -2.0F, 0.0F);
		body.addChild(belly);
		belly.setTexSize(0, 0).addBox(-4.0F, -6.0F, 0.0F, 8.0F, 6.0F, 12.0F, 0.0F, false);

		legs = new ModelRenderer(this);
		legs.setPos(-2.0F, 0.0F, 4.0F);
		belly.addChild(legs);

		legsBack = new ModelRenderer(this);
		legsBack.setPos(2.0F, -1.0F, -3.0F);
		legs.addChild(legsBack);
		legsBack.setTexSize(26, 18).addBox(1.8F, -1.0F, -0.8F, 2.0F, 4.0F, 2.0F, 0.0F, false);
		legsBack.setTexSize(18, 18).addBox(-3.8F, -1.0F, -0.8F, 2.0F, 4.0F, 2.0F, 0.0F, false);

		legsFront = new ModelRenderer(this);
		legsFront.setPos(2.0F, -1.0F, 7.0F);
		legs.addChild(legsFront);
		legsFront.setTexSize(0, 6).addBox(1.8F, -1.0F, -1.2F, 2.0F, 4.0F, 2.0F, 0.0F, false);
		legsFront.setTexSize(0, 0).addBox(-3.8F, -1.0F, -1.2F, 2.0F, 4.0F, 2.0F, 0.0F, false);

		head = new ModelRenderer(this);
		head.setPos(-4.0F, -7.0F, 12.0F);
		body.addChild(head);
		head.setTexSize(0, 30).addBox(-3.0F, -5.0F, -1.0F, 6.0F, 6.0F, 6.0F, 0.0F, false);

		ears = new ModelRenderer(this);
		ears.setPos(0.0F, -4.0F, 1.0F);
		head.addChild(ears);
		ears.setTexSize(8, 4).addBox(-2.8F, -3.0F, -1.0F, 0.0F, 4.0F, 2.0F, 0.0F, false);
		ears.setTexSize(8, 0).addBox(2.8F, -3.0F, -1.0F, 0.0F, 4.0F, 2.0F, 0.0F, false);

		tail = new ModelRenderer(this);
		tail.setPos(-4.0F, -8.0F, 0.0F);
		body.addChild(tail);

		tail1 = new ModelRenderer(this);
		tail1.setPos(0.0F, 0.0F, 0.0F);
		tail.addChild(tail1);
		setRotationAngle(tail1, 0.1745F, 0.0F, 0.0F);
		tail1.setTexSize(28, 0).addBox(-3.0F, -5.0F, -5.0F, 6.0F, 6.0F, 6.0F, 0.0F, false);

		tail2 = new ModelRenderer(this);
		tail2.setPos(0.0F, -4.0F, 0.0F);
		tail1.addChild(tail2);
		setRotationAngle(tail2, -0.1745F, 0.0F, 0.0F);
		tail2.setTexSize(18, 24).addBox(-3.0F, -7.0F, -5.0F, 6.0F, 6.0F, 6.0F, 0.0F, false);

		tail3 = new ModelRenderer(this);
		tail3.setPos(0.0F, -6.0F, 0.0F);
		tail2.addChild(tail3);
		setRotationAngle(tail3, -0.1309F, 0.0F, 0.0436F);

		part_r1 = new ModelRenderer(this);
		part_r1.setPos(0.0F, -1.0F, 0.5F);
		tail3.addChild(part_r1);
		setRotationAngle(part_r1, 0.0F, 0.0F, -0.0436F);
		part_r1.setTexSize(0, 18).addBox(-3.0F, -6.0F, -5.5F, 6.0F, 6.0F, 6.0F, 0.0F, false);
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red,
			float green, float blue, float alpha) {
		float bodyScale = this.young ? 4.0F : 2.0F;
		matrixStack.pushPose();
		float f1 = 1.0F / bodyScale;
		matrixStack.scale(f1, f1, f1);
		matrixStack.translate(0.0D, this.young ? 4.5D : 1.5D, 0.0D);
		this.bodyParts().forEach((modelRenderer) -> {
			modelRenderer.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		});
		matrixStack.popPose();
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}

	@Override
	protected Iterable<ModelRenderer> headParts() {
		return ImmutableList.of();
	}

	@Override
	protected Iterable<ModelRenderer> bodyParts() {
		return ImmutableList.of(this.body);
	}

	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks,
			float netHeadYaw, float headPitch) {
		if (entityIn.isNormal()) {
			this.tail.xRot = MathHelper.cos(limbSwing * 0.6662F) * 0.5F * limbSwingAmount + 0.3F;
			this.tail1.xRot = MathHelper.cos(limbSwing * 0.6662F - (float) Math.PI) * 0.5F * limbSwingAmount;
			this.tail2.xRot = MathHelper.cos(limbSwing * 0.6662F) * 0.5F * limbSwingAmount;
			this.legsBack.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
			this.legsFront.xRot = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F
					* limbSwingAmount;
			this.head.xRot = headPitch * ((float) Math.PI / 180F) * 0.2F;
			this.head.xRot = netHeadYaw * ((float) Math.PI / 180F);
			this.body.xRot = 0.0F;
		}
	}

	public void setLivingAnimations(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
		body.setPos(0.0F, 24.0F, 0.0F);
		if (entityIn.isSitting()) {
			setRotationAngle(this.body, (float) Math.PI / 5F, (float) Math.PI, 0.0F);
			setRotationAngle(this.head, -0.9163F, 0.0F, 0.0F);
			this.legsBack.xRot = -(float) Math.PI / 5F;
			this.legsFront.xRot = (float) Math.PI / 4F;
			this.tail.xRot = -0.3927F;
			this.tail1.xRot = 0.1745F;
			this.tail2.xRot = -0.1745F;
		} else if (entityIn.isBesideClimbableBlock()) {
			this.tail.xRot = MathHelper.cos(limbSwing * 0.6662F) * 0.5F * limbSwingAmount + 0.3F;
			this.tail1.xRot = MathHelper.cos(limbSwing * 0.6662F - (float) Math.PI) * 0.5F * limbSwingAmount;
			this.tail2.xRot = MathHelper.cos(limbSwing * 0.6662F) * 0.5F * limbSwingAmount;
			this.legsBack.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
			this.legsFront.xRot = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F
					* limbSwingAmount;
			this.head.xRot = 0.0F;
			this.head.yRot = 0.0F;
/*			if (entityIn.getNearestDirection() == Direction.UP) {
				body.setPos(0.0F, 10.0F, 0.0F);
				body.xRot = (float) Math.PI;
			} else if (entityIn.getNearestDirection() == Direction.DOWN) {
				setRotationAngle(this.body, 0.0F, (float) Math.PI, 0.0F);
			} else {
				this.body.xRot = (float) Math.PI / -2.0F;
			}*/
		}
	}
}