package com.portingdeadmods.indrec.content.explosions;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.EntityBasedExplosionDamageCalculator;
import net.minecraft.world.level.Explosion;

public class IndustrialTntExplosionDamageCalculator extends EntityBasedExplosionDamageCalculator {
    public IndustrialTntExplosionDamageCalculator(Entity source) {
        super(source);
    }

    @Override
    public boolean shouldDamageEntity(Explosion explosion, Entity entity) {
        if (entity instanceof ItemEntity) return false;
        return super.shouldDamageEntity(explosion, entity);
    }

    @Override
    public float getEntityDamageAmount(Explosion explosion, Entity entity) {
        return super.getEntityDamageAmount(explosion, entity) / 2;
    }

}
