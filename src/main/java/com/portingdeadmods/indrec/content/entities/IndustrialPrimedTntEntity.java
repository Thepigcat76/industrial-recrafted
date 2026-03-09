package com.portingdeadmods.indrec.content.entities;

import com.portingdeadmods.indrec.api.entities.ExplodingEntity;
import com.portingdeadmods.indrec.content.explosions.DropAndKeepItemsExplosion;
import com.portingdeadmods.indrec.registries.IRBlocks;
import com.portingdeadmods.indrec.registries.IREntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class IndustrialPrimedTntEntity extends PrimedTnt implements ExplodingEntity {
    public IndustrialPrimedTntEntity(EntityType<IndustrialPrimedTntEntity> entityType, Level level) {
        super(entityType, level);
    }

    public IndustrialPrimedTntEntity(Level level, double x, double y, double z, @Nullable LivingEntity owner) {
        this(IREntityTypes.INDUSTRIAL_TNT.get(), level);
        this.setPos(x, y, z);
        double d0 = level.random.nextDouble() * (double)((float)Math.PI * 2F);
        this.setDeltaMovement(-Math.sin(d0) * 0.02, 0.2F, -Math.cos(d0) * 0.02);
        this.setFuse(80);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.owner = owner;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_FUSE_ID, 80);
        builder.define(DATA_BLOCK_STATE_ID, IRBlocks.INDUSTRIAL_TNT.get().defaultBlockState());
    }

    @Override
    public Explosion createExplosion(Level level, @org.jetbrains.annotations.Nullable Entity source, double x, double y, double z, float radius, List<BlockPos> toBlow, Explosion.BlockInteraction blockInteraction, ParticleOptions smallExplosionParticles, ParticleOptions largeExplosionParticles, Holder<SoundEvent> explosionSound) {
        return new DropAndKeepItemsExplosion(level, source, x, y, z, radius * 0.8F, blockInteraction, smallExplosionParticles, largeExplosionParticles, explosionSound);
    }
}
