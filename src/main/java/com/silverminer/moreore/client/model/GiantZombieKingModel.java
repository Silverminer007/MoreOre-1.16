package com.silverminer.moreore.client.model;

import com.silverminer.moreore.common.objects.entitys.GiantZombieKingEntity;

import net.minecraft.client.renderer.entity.model.AbstractZombieModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GiantZombieKingModel extends AbstractZombieModel<GiantZombieKingEntity> {
	public GiantZombieKingModel() {
		this(0.0F, false);
	}

	public GiantZombieKingModel(float modelSize, boolean p_i51066_2_) {
		super(modelSize, 0.0F, 64, p_i51066_2_ ? 32 : 64);
	}

	public boolean isAggressive(GiantZombieKingEntity entityIn) {
		return false;
	}
}