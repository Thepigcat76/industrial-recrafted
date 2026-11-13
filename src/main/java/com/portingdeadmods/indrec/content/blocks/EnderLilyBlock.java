package com.portingdeadmods.indrec.content.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class EnderLilyBlock extends FlowerBlock {
    public static final MapCodec<EnderLilyBlock> CODEC = simpleCodec(EnderLilyBlock::new);

    public EnderLilyBlock(Properties properties) {
        super(MobEffects.SLOW_FALLING, 7.0F, properties);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);

        if (random.nextInt(3) == 0) {
            Vec3 center = state.getOffset(level, pos).add(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            double x = center.x + (random.nextDouble() - 0.5) * 0.5;
            double y = center.y + (random.nextDouble() - 0.5) * 0.5;
            double z = center.z + (random.nextDouble() - 0.5) * 0.5;

            level.addParticle(ParticleTypes.PORTAL, x, y, z, (random.nextDouble() - 0.5) * 2, -random.nextDouble(), (random.nextDouble() - 0.5) * 2);
        }
    }

    @Override
    public @NotNull MapCodec<? extends FlowerBlock> codec() {
        return CODEC;
    }
}
