package com.portingdeadmods.indrec.api.energy;

import com.portingdeadmods.indrec.IRRegistries;
import com.portingdeadmods.portingdeadlibs.utils.Utils;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public interface EnergyTier extends Comparable<EnergyTier> {
    int MAX_TIER_DIFFERENCE = 1;

    int order();

    int maxInput();

    int maxOutput();

    int color();

    int defaultCapacity();

    boolean special();

    @Override
    default int compareTo(@NotNull EnergyTier energyTier) {
        return Integer.compare(this.order(), energyTier.order());
    }

    default Component getDisplayName() {
        return Utils.registryTranslation(IRRegistries.ENERGY_TIER, this);
    }

    default EnergyTier raiseTier(int steps) {
        EnergyTier raised = this;
        for (EnergyTier tier : IRRegistries.ENERGY_TIER) {
            if (!tier.special() && tier.order() <= this.order() + steps && tier.compareTo(raised) > 0) {
                raised = tier;
            }
        }
        return raised;
    }

}