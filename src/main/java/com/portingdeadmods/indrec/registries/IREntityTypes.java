package com.portingdeadmods.indrec.registries;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.content.entities.DynamiteEntity;
import com.portingdeadmods.indrec.content.entities.IndustrialPrimedTntEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class IREntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, IndustrialRecrafted.MODID);

    public static final Supplier<EntityType<DynamiteEntity>> DYNAMITE = ENTITY_TYPES.register("dynamite",
            () -> EntityType.Builder.<DynamiteEntity>of(DynamiteEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build("dynamite"));
    public static final Supplier<EntityType<IndustrialPrimedTntEntity>> INDUSTRIAL_TNT = ENTITY_TYPES.register("industrial_tnt",
            () -> EntityType.Builder.<IndustrialPrimedTntEntity>of(IndustrialPrimedTntEntity::new, MobCategory.MISC)
                    .fireImmune().sized(0.98F, 0.98F).eyeHeight(0.15F).clientTrackingRange(10).updateInterval(10)
                    .build("industrial_tnt"));
}
