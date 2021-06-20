package com.silverminer.moreore.init;

import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.common.objects.entitys.GiantZombieKingEntity;
import com.silverminer.moreore.common.objects.entitys.SquirrelEntity;
import com.silverminer.moreore.common.objects.entitys.VillageGuardian;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityTypesInit {
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES,
			MoreOre.MODID);

	public static final RegistryObject<EntityType<VillageGuardian>> VILLAGE_GUARDIAN = ENTITIES.register(
			"village_guardian",
			() -> EntityType.Builder.<VillageGuardian>of(VillageGuardian::new, EntityClassification.CREATURE)
					.sized(0.9f, 2.2f)
					.build(new ResourceLocation(MoreOre.MODID, "textures/entity/village_guardian.png").toString()));

	public static final RegistryObject<EntityType<SquirrelEntity>> SQUIRREL = ENTITIES.register("squirrel",
			() -> EntityType.Builder.<SquirrelEntity>of(SquirrelEntity::new, EntityClassification.CREATURE)
					.sized(0.5f, 0.5f)
					.build(new ResourceLocation(MoreOre.MODID, "textures/entity/squirrel.png").toString()));

	public static final RegistryObject<EntityType<GiantZombieKingEntity>> GIANT_ZOMBIE_KING = ENTITIES.register(
			"giant_zombie_king",
			() -> EntityType.Builder.<GiantZombieKingEntity>of(GiantZombieKingEntity::new, EntityClassification.MONSTER)
					.fireImmune().sized(4.8F, 16.0F)
					.build(new ResourceLocation("textures/entity/zombie/zombie.png").toString()));
}