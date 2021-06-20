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
import net.minecraft.client.renderer.model.ModelRenderer;
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
		super(renderManagerIn, new SquirrelModel<SquirrelEntity>(), 0.3F);
		this.addLayer(new SquirrelHeldItemLayer(this));
	}

	@Override
	public ResourceLocation getTextureLocation(SquirrelEntity entity) {
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
			if (entitylivingbaseIn.isBaby())
				return;
			matrixStackIn.pushPose();
			ModelRenderer head = this.getParentModel().head;
			matrixStackIn.translate((double) (head.x / 16.0F), (double) (head.y / 16.0F),
					(double) (head.z / 16.0F));
			if (!entitylivingbaseIn.isSitting()) {
				matrixStackIn.translate(0.365D, 1.725D, -1.325D);
			} else {
				matrixStackIn.translate(0.365D, 1.6D, -1.1D);
			}
			float f = 0.25F;
			matrixStackIn.scale(f, f, f);
			matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(netHeadYaw));
			matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(headPitch - 90.0F));
			matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(180.0F));

			ItemStack itemstack = entitylivingbaseIn.getItemBySlot(EquipmentSlotType.MAINHAND);
			Minecraft.getInstance().getItemInHandRenderer().renderItem(entitylivingbaseIn, itemstack,
					ItemCameraTransforms.TransformType.GROUND, false, matrixStackIn, bufferIn, packedLightIn);
			matrixStackIn.popPose();
		}
	}
}