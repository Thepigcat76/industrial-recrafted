package com.portingdeadmods.indrec.impl.energy;

import com.portingdeadmods.indrec.api.energy.EnergyTier;

public record EnergyTierImpl(int maxInput, int maxOutput, int defaultCapacity, int color, int order) implements EnergyTier {
    public EnergyTierImpl(int maxTransfer, int defaultCapacity, int color, int order) {
        this(maxTransfer, maxTransfer, defaultCapacity, color, order);
    }
}
