package com.portingdeadmods.indrec.content.blocks;

import com.portingdeadmods.indrec.content.entities.IndustrialPrimedTntEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

public class IndustrialTntBlock extends TntBlock {
    public IndustrialTntBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onCaughtFire(BlockState state, Level world, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
        this.explode(world, pos, igniter);
    }

    protected void explode(Level level, BlockPos pos, LivingEntity entity) {
        if (!level.isClientSide) {
            IndustrialPrimedTntEntity primedtnt = new IndustrialPrimedTntEntity(level, (double)pos.getX() + (double)0.5F, pos.getY(), (double)pos.getZ() + (double)0.5F, entity);
            level.addFreshEntity(primedtnt);
            level.playSound(null, primedtnt.getX(), primedtnt.getY(), primedtnt.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(entity, GameEvent.PRIME_FUSE, pos);
        }
    }

    public void wasExploded(Level level, BlockPos pos, Explosion explosion) {
        if (!level.isClientSide) {
            IndustrialPrimedTntEntity primedtnt = new IndustrialPrimedTntEntity(level, (double)pos.getX() + (double)0.5F, pos.getY(), (double)pos.getZ() + (double)0.5F, explosion.getIndirectSourceEntity());
            int i = primedtnt.getFuse();
            primedtnt.setFuse((short)(level.random.nextInt(i / 4) + i / 8));
            level.addFreshEntity(primedtnt);
        }

    }
}
