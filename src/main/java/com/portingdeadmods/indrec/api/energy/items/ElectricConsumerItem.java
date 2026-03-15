package com.portingdeadmods.indrec.api.energy.items;

import com.portingdeadmods.indrec.IRCapabilities;
import com.portingdeadmods.indrec.api.energy.EnergyHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface ElectricConsumerItem {
    default boolean requireEnergyToWork(ItemStack itemStack) {
        return requireEnergyToWork(itemStack, null);
    }

    boolean requireEnergyToWork(ItemStack itemStack, @Nullable Entity entity);

    default int getEnergyUsage(ItemStack itemStack) {
        return getEnergyUsage(itemStack, null);
    }

    int getEnergyUsage(ItemStack itemStack, @Nullable Entity entity);

    default boolean canWork(ItemStack stack) {
        return canWork(stack, null);
    }

    default boolean canWork(ItemStack stack, @Nullable Entity entity) {
        return canWork(stack.getCapability(IRCapabilities.ENERGY_ITEM), stack, entity);
    }

    default boolean canWork(EnergyHandler energyHandler, ItemStack stack, @Nullable Entity entity) {
        if (energyHandler != null && requireEnergyToWork(stack, entity)) {
            return energyHandler.getEnergyStored() >= getEnergyUsage(stack, entity);
        }
        return false;
    }

    default void consumeEnergy(ItemStack stack) {
        consumeEnergy(stack, null);
    }

    default void consumeEnergy(ItemStack stack, @Nullable Entity entity) {
        EnergyHandler energyStorage = stack.getCapability(IRCapabilities.ENERGY_ITEM);
        if (requireEnergyToWork(stack, entity)) {
            energyStorage.drainEnergy(getEnergyUsage(stack, entity), false);
        }
    }

}