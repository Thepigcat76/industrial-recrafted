package com.portingdeadmods.indrec.api.energy;

import com.portingdeadmods.indrec.IRRegistries;
import com.portingdeadmods.portingdeadlibs.utils.Utils;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public interface EnergyTier extends Comparable<EnergyTier> {
    int order();

    int maxInput();

    int maxOutput();

    int color();

    int defaultCapacity();

    @Override
    default int compareTo(@NotNull EnergyTier energyTier) {
        return Integer.compare(this.order(), energyTier.order());
    }

    default Component getDisplayName() {
        return Utils.registryTranslation(IRRegistries.ENERGY_TIER, this);
    }

}