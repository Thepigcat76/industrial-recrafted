package com.portingdeadmods.indrec.content.entities;

import com.portingdeadmods.indrec.registries.IRItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class DynamiteEntity extends Snowball {
    public DynamiteEntity(EntityType<? extends Snowball> entityType, Level level) {
        super(entityType, level);
    }

    public DynamiteEntity(Level level, LivingEntity livingEntity) {
        super(level, livingEntity);
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return IRItems.DYNAMITE.asItem();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        this.level().explode(
                this,
                Explosion.getDefaultDamageSource(this.level(), this),
                null,
                this.getX(),
                this.getY(0.0625),
                this.getZ(),
                3.0F,
                false,
                Level.ExplosionInteraction.TNT
        );
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        this.level().explode(
                this,
                Explosion.getDefaultDamageSource(this.level(), this),
                null,
                this.getX(),
                this.getY(0.0625),
                this.getZ(),
                4.0F,
                false,
                Level.ExplosionInteraction.TNT
        );
    }
}