package com.portingdeadmods.indrec.api.energy.items;

import com.portingdeadmods.indrec.IRCapabilities;
import com.portingdeadmods.indrec.api.energy.EnergyHandler;
import com.portingdeadmods.indrec.api.energy.EnergyTier;
import net.minecraft.world.item.ItemStack;

public interface EnergyItem {
    default EnergyHandler getEnergyCap(ItemStack itemStack) {
        return itemStack.getCapability(IRCapabilities.ENERGY_ITEM);
    }

    // We have to pass the energy storage here, as it is not assigned to the capability yet
    default void initEnergyStorage(EnergyHandler energyStorage, ItemStack itemStack) {
    }

    default void onEnergyChanged(ItemStack itemStack, int oldAmount) {
    }

    default int getDefaultCapacity() {
        return this.getEnergyTier().defaultCapacity();
    }

    EnergyTier getEnergyTier();
}