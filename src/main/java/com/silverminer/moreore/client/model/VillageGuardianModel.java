package com.silverminer.moreore.client.model;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VillageGuardianModel<T extends Entity> extends AgeableModel<T> {
	protected ModelRenderer villagerHead;
	protected ModelRenderer field_217151_b;
	protected final ModelRenderer field_217152_f;
	protected final ModelRenderer villagerBody;
	protected final ModelRenderer field_217153_h;
	protected final ModelRenderer villagerArms;
	protected final ModelRenderer rightVillagerLeg;
	protected final ModelRenderer leftVillagerLeg;
	protected final ModelRenderer villagerNose;

	public VillageGuardianModel(float scale) {
		this(scale, 64, 64);
	}

	public VillageGuardianModel(float p_i51059_1_, int p_i51059_2_, int p_i51059_3_) {
		this.villagerHead = (new ModelRenderer(this)).setTexSize(p_i51059_2_, p_i51059_3_);
		this.villagerHead.setPos(0.0F, 0.0F, 0.0F);
		this.villagerHead.setTexSize(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, p_i51059_1_);
		this.field_217151_b = (new ModelRenderer(this)).setTexSize(p_i51059_2_, p_i51059_3_);
		this.field_217151_b.setPos(0.0F, 0.0F, 0.0F);
		this.field_217151_b.setTexSize(32, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, p_i51059_1_ + 0.5F);
		this.villagerHead.addChild(this.field_217151_b);
		this.field_217152_f = (new ModelRenderer(this)).setTexSize(p_i51059_2_, p_i51059_3_);
		this.field_217152_f.setPos(0.0F, 0.0F, 0.0F);
		this.field_217152_f.setTexSize(30, 47).addBox(-8.0F, -8.0F, -6.0F, 16.0F, 16.0F, 1.0F, p_i51059_1_);
		this.field_217152_f.xRot = (-(float) Math.PI / 2F);
		this.field_217151_b.addChild(this.field_217152_f);
		this.villagerNose = (new ModelRenderer(this)).setTexSize(p_i51059_2_, p_i51059_3_);
		this.villagerNose.setPos(0.0F, -2.0F, 0.0F);
		this.villagerNose.setTexSize(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, p_i51059_1_);
		this.villagerHead.addChild(this.villagerNose);
		this.villagerBody = (new ModelRenderer(this)).setTexSize(p_i51059_2_, p_i51059_3_);
		this.villagerBody.setPos(0.0F, 0.0F, 0.0F);
		this.villagerBody.setTexSize(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, p_i51059_1_);
		this.field_217153_h = (new ModelRenderer(this)).setTexSize(p_i51059_2_, p_i51059_3_);
		this.field_217153_h.setPos(0.0F, 0.0F, 0.0F);
		this.field_217153_h.setTexSize(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 18.0F, 6.0F, p_i51059_1_ + 0.5F);
		this.villagerBody.addChild(this.field_217153_h);
		this.villagerArms = (new ModelRenderer(this)).setTexSize(p_i51059_2_, p_i51059_3_);
		this.villagerArms.setPos(0.0F, 2.0F, 0.0F);
		this.villagerArms.setTexSize(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, p_i51059_1_);
		this.villagerArms.setTexSize(44, 22).addBox(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, p_i51059_1_, true);
		this.villagerArms.setTexSize(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, p_i51059_1_);
		this.rightVillagerLeg = (new ModelRenderer(this, 0, 22)).setTexSize(p_i51059_2_, p_i51059_3_);
		this.rightVillagerLeg.setPos(-2.0F, 12.0F, 0.0F);
		this.rightVillagerLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, p_i51059_1_);
		this.leftVillagerLeg = (new ModelRenderer(this, 0, 22)).setTexSize(p_i51059_2_, p_i51059_3_);
		this.leftVillagerLeg.mirror = true;
		this.leftVillagerLeg.setPos(2.0F, 12.0F, 0.0F);
		this.leftVillagerLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, p_i51059_1_);
	}

	public Iterable<ModelRenderer> getParts() {
		return ImmutableList.of(this.villagerHead, this.villagerBody, this.rightVillagerLeg, this.leftVillagerLeg,
				this.villagerArms);
	}

	public ModelRenderer getModelHead() {
		return this.villagerHead;
	}

	public void func_217146_a(boolean p_217146_1_) {
		this.villagerHead.visible = p_217146_1_;
		this.field_217151_b.visible = p_217146_1_;
		this.field_217152_f.visible = p_217146_1_;
	}

	@Override
	protected Iterable<ModelRenderer> headParts() {
		return ImmutableList.of();
	}

	@Override
	protected Iterable<ModelRenderer> bodyParts() {
		return ImmutableList.of(this.leftVillagerLeg, this.rightVillagerLeg, this.villagerArms, this.villagerBody,
				this.villagerHead);
	}

	/**
	 * Sets this entity's model rotation angles
	 */
	@Override
	public void setupAnim(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks,
			float netHeadYaw, float headPitch) {
		boolean flag = false;
		if (entityIn instanceof AbstractVillagerEntity) {
			flag = ((AbstractVillagerEntity) entityIn).getHeadRotSpeed() > 0;
		}

		this.villagerHead.yRot = netHeadYaw * ((float) Math.PI / 180F);
		this.villagerHead.xRot = headPitch * ((float) Math.PI / 180F);
		if (flag) {
			this.villagerHead.zRot = 0.3F * MathHelper.sin(0.45F * ageInTicks);
			this.villagerHead.xRot = 0.4F;
		} else {
			this.villagerHead.zRot = 0.0F;
		}

		this.villagerArms.y = 3.0F;
		this.villagerArms.z = -1.0F;
		this.villagerArms.xRot = -0.75F;
		this.rightVillagerLeg.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
		this.leftVillagerLeg.xRot = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F
				* limbSwingAmount * 0.5F;
		this.rightVillagerLeg.yRot = 0.0F;
		this.leftVillagerLeg.yRot = 0.0F;
	}
}