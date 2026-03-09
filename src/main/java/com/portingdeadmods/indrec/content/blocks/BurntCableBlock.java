package com.portingdeadmods.indrec.content.blocks;

import com.portingdeadmods.indrec.api.energy.EnergyTier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.function.Supplier;

public class BurntCableBlock extends CableBlock {
    public static final BooleanProperty BURNT = BooleanProperty.create("burnt");

    public BurntCableBlock(Properties properties, int width, Supplier<? extends EnergyTier> energyTier) {
        super(properties, width, energyTier);
        this.registerDefaultState(this.defaultBlockState().setValue(BURNT, false));
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(BURNT)) {
            for (int i = 0; i < 3; i++) {
                double x = pos.getX() + 0.3 + random.nextDouble() * 0.4;
                double y = pos.getY() + 0.3 + random.nextDouble() * 0.4;
                double z = pos.getZ() + 0.3 + random.nextDouble() * 0.4;
                if (random.nextInt(2) == 0) {
                    level.addParticle(random.nextInt(7) == 0 ? ParticleTypes.LARGE_SMOKE : ParticleTypes.SMOKE, x, y, z, 0D, 0D, 0D);
                }
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(BURNT));
    }

    @Override
    public EnergyTier getEnergyTier() {
        return null;
    }

    @Override
    protected boolean canTransport() {
        return false;
    }

    @Override
    public boolean canConnectTo(BlockEntity connectTo) {
        return false;
    }

    @Override
    public boolean canConnectToPipe(BlockState connectTo) {
        return false;
    }

}