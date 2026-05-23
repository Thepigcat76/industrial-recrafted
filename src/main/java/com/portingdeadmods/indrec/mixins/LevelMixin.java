package com.portingdeadmods.indrec.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.portingdeadmods.indrec.api.entities.ExplodingEntity;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import javax.annotation.Nullable;

@Mixin(Level.class)
public class LevelMixin {
    @ModifyVariable(
            method = "explode(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;Lnet/minecraft/world/level/ExplosionDamageCalculator;DDDFZLnet/minecraft/world/level/Level$ExplosionInteraction;ZLnet/minecraft/core/particles/ParticleOptions;Lnet/minecraft/core/particles/ParticleOptions;Lnet/minecraft/core/Holder;)Lnet/minecraft/world/level/Explosion;",
            at = @At(
                    value = "STORE"
            ),
            name = "explosion")
    private Explosion replaceExplosion(
            Explosion originalExplosion,
            @Local(argsOnly = true) @Nullable Entity source,
            @Local(argsOnly = true, ordinal = 0) double x,
            @Local(argsOnly = true, ordinal = 1) double y,
            @Local(argsOnly = true, ordinal = 2) double z,
            @Local(argsOnly = true) float radius,
            @Local Explosion.BlockInteraction blockInteraction,
            @Local(argsOnly = true, ordinal = 0) ParticleOptions smallExplosionParticles,
            @Local(argsOnly = true, ordinal = 1) ParticleOptions largeExplosionParticles,
            @Local(argsOnly = true) Holder<SoundEvent> explosionSound
    ) {
        if (source instanceof ExplodingEntity explodingEntity) {
            return explodingEntity.createExplosion(
                    (Level) (Object) this,
                    source,
                    x, y, z,
                    radius,
                    null, // toBlow list, you can generate it in your createExplosion impl if needed
                    blockInteraction,
                    smallExplosionParticles,
                    largeExplosionParticles,
                    explosionSound
            );
        }
        return originalExplosion;
    }

}
