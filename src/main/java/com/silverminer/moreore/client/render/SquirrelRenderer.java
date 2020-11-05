package com.silverminer.moreore.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.client.model.SquirrelModel;
import com.silverminer.moreore.common.objects.entitys.SquirrelEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SquirrelRenderer<T extends Entity> extends MobRenderer<SquirrelEntity, SquirrelModel<SquirrelEntity>> {
	protected static final ResourceLocation TEXTURE = new ResourceLocation(MoreOre.MODID,
			"textures/entity/squirrel.png");

	public SquirrelRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new SquirrelModel<SquirrelEntity>(), 0.5f);
//		this.addLayer(new SquirrelHeldItemLayer(this));
	}

	@Override
	public ResourceLocation getEntityTexture(SquirrelEntity entity) {
		return TEXTURE;
	}

	@OnlyIn(Dist.CLIENT)
	public class SquirrelHeldItemLayer extends LayerRenderer<SquirrelEntity, SquirrelModel<SquirrelEntity>> {
		public SquirrelHeldItemLayer(IEntityRenderer<SquirrelEntity, SquirrelModel<SquirrelEntity>> renderer) {
			super(renderer);
		}

		public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn,
				SquirrelEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks,
				float ageInTicks, float netHeadYaw, float headPitch) {
			boolean isChild = entitylivingbaseIn.isChild();
			matrixStackIn.push();
			if (isChild) {
				float f = 0.5F;
				matrixStackIn.scale(f, f, f);
			}
			matrixStackIn.translate(5.0D, isChild ? 3.0D : 0.5D, 0.0D);

			matrixStackIn.translate((double) ((this.getEntityModel()).head.rotationPointX / 16.0F),
					(double) ((this.getEntityModel()).head.rotationPointY / 16.0F),
					(double) ((this.getEntityModel()).head.rotationPointZ / 16.0F));
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(netHeadYaw));
			matrixStackIn.rotate(Vector3f.XP.rotationDegrees(headPitch));

			matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F));

			ItemStack itemstack = entitylivingbaseIn.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
			Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(entitylivingbaseIn, itemstack,
					ItemCameraTransforms.TransformType.GROUND, false, matrixStackIn, bufferIn, packedLightIn);
			matrixStackIn.pop();
		}
	}
}