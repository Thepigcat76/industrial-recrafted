package com.portingdeadmods.indrec.content.explosions;

import com.mojang.datafixers.util.Pair;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DropAndKeepItemsExplosion extends Explosion {
    public DropAndKeepItemsExplosion(Level level, @Nullable Entity source, double x, double y, double z, float radius, BlockInteraction blockInteraction, ParticleOptions smallExplosionParticles, ParticleOptions largeExplosionParticles, Holder<SoundEvent> explosionSound) {
        super(level, source, getDefaultDamageSource(level, source), new IndustrialTntExplosionDamageCalculator(source), x, y, z, radius, false, blockInteraction, smallExplosionParticles, largeExplosionParticles, explosionSound);
    }

    public void finalizeExplosion(boolean spawnParticles) {
        if (this.level.isClientSide) {
            this.level.playLocalSound(this.x, this.y, this.z, this.explosionSound.value(), SoundSource.BLOCKS, 4.0F, (1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F) * 0.7F, false);
        }

        boolean flag = this.interactsWithBlocks();
        if (spawnParticles) {
            ParticleOptions particleoptions;
            if (!(this.radius < 2.0F) && flag) {
                particleoptions = this.largeExplosionParticles;
            } else {
                particleoptions = this.smallExplosionParticles;
            }

            this.level.addParticle(particleoptions, this.x, this.y, this.z, 1.0F, 0.0F, 0.0F);
        }

        if (flag) {
            this.level.getProfiler().push("explosion_blocks");
            List<Pair<ItemStack, BlockPos>> list = new ArrayList<>();
            Util.shuffle(this.toBlow, this.level.random);

            if (this.level instanceof ServerLevel serverLevel) {
                for (BlockPos blockPos : this.toBlow) {
                    BlockState state = this.level.getBlockState(blockPos);
                    for (ItemStack drop : Block.getDrops(state, serverLevel, blockPos, level.getBlockEntity(blockPos))) {
                        addOrAppendStack(list, drop, blockPos);
                    }
                    state.onExplosionHit(this.level, blockPos, this, (stack, pos) -> {});
                }
            }

            for(Pair<ItemStack, BlockPos> pair : list) {
                Block.popResource(this.level, pair.getSecond(), pair.getFirst());
            }

            this.level.getProfiler().pop();
        }

        if (this.fire) {
            for (BlockPos blockPos : this.toBlow) {
                if (this.random.nextInt(3) == 0 && this.level.getBlockState(blockPos).isAir() && this.level.getBlockState(blockPos.below()).isSolidRender(this.level, blockPos.below())) {
                    this.level.setBlockAndUpdate(blockPos, BaseFireBlock.getState(this.level, blockPos));
                }
            }
        }

    }

}
