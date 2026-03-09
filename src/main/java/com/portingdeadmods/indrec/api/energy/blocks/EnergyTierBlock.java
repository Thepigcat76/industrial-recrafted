package com.portingdeadmods.indrec.api.energy.blocks;

import com.portingdeadmods.indrec.api.energy.EnergyTier;
import org.jetbrains.annotations.Nullable;

public interface EnergyTierBlock {
    @Nullable EnergyTier getEnergyTier();
}