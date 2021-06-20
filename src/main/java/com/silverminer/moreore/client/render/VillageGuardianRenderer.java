package com.silverminer.moreore.client.render;

import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.client.model.VillageGuardianModel;
import com.silverminer.moreore.common.objects.entitys.VillageGuardian;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class VillageGuardianRenderer<T extends Entity> extends MobRenderer<VillageGuardian, VillageGuardianModel<VillageGuardian>> {
	protected static final ResourceLocation TEXTURE = new ResourceLocation(MoreOre.MODID,
			"textures/entity/village_guardian.png");

	public VillageGuardianRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new VillageGuardianModel<VillageGuardian>(0.0F), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(VillageGuardian entity) {
		return TEXTURE;
	}
}
