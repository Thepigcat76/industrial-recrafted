package com.portingdeadmods.indrec.api.blockentities;

import com.portingdeadmods.indrec.api.energy.EnergyHandler;
import com.portingdeadmods.indrec.api.energy.TieredEnergy;
import com.portingdeadmods.indrec.registries.IRNetworks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public interface GeneratorBlockEntity {
    int getGenerationAmount();

    static void transportEnergy(Level level, BlockPos pos, EnergyHandler energyHandler) {
        if (!level.isClientSide()) {
            if (level instanceof ServerLevel serverLevel) {
                int min = Math.min(energyHandler.getEnergyTier().maxOutput(), energyHandler.getEnergyStored());
                TieredEnergy remainder = IRNetworks.ENERGY.get().transport(serverLevel, pos, new TieredEnergy(min, energyHandler.getEnergyTier()));
                energyHandler.drainEnergy(min - remainder.energy(), false);
            }
        }
    }

}
