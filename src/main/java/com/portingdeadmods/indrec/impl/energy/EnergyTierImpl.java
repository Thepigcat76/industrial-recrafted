package com.portingdeadmods.indrec.impl.energy;

import com.portingdeadmods.indrec.api.energy.EnergyTier;

public record EnergyTierImpl(int maxInput, int maxOutput, int defaultCapacity, int color, int order, boolean special) implements EnergyTier {
    public EnergyTierImpl(int maxTransfer, int defaultCapacity, int color, int order) {
        this(maxTransfer, maxTransfer, defaultCapacity, color, order, false);
    }
    public EnergyTierImpl(int maxTransfer, int defaultCapacity, int color, int order, boolean special) {
        this(maxTransfer, maxTransfer, defaultCapacity, color, order, special);
    }
}
