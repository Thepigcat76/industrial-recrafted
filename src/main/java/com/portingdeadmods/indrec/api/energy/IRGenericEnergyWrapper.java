package com.portingdeadmods.indrec.api.energy;

import com.portingdeadmods.portingdeadlibs.api.capabilities.EnergyStorageWrapper;

public record IRGenericEnergyWrapper(EnergyHandler energyStorage) implements EnergyStorageWrapper {
    @Override
    public int getEnergyStored() {
        return energyStorage.getEnergyStored();
    }

    @Override
    public int getEnergyCapacity() {
        return energyStorage.getEnergyCapacity();
    }
}