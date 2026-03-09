package com.portingdeadmods.indrec.api.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public interface ExplodingEntity {
    Explosion createExplosion(Level level, @Nullable Entity source, double x, double y, double z, float radius, List<BlockPos> toBlow, Explosion.BlockInteraction blockInteraction, ParticleOptions smallExplosionParticles, ParticleOptions largeExplosionParticles, Holder<SoundEvent> explosionSound);
}
