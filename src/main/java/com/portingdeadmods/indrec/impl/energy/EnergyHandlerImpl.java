package com.portingdeadmods.indrec.impl.energy;

import com.portingdeadmods.indrec.api.capabilities.StorageChangedListener;
import com.portingdeadmods.indrec.api.energy.EnergyHandler;
import com.portingdeadmods.indrec.api.energy.EnergyTier;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class EnergyHandlerImpl implements EnergyHandler, StorageChangedListener, INBTSerializable<CompoundTag> {
    private int energy;
    private int capacity;
    private final EnergyTier energyTier;
    private Consumer<Integer> changedFunction = amount -> {};

    public EnergyHandlerImpl(Supplier<? extends EnergyTier> energyTier) {
        this.energyTier = energyTier.get();
        this.capacity = this.energyTier.defaultCapacity();
    }

    public EnergyHandlerImpl(EnergyTier energyTier, int capacity) {
        this.capacity = capacity;
        this.energyTier = energyTier;
    }

    @Override
    public EnergyTier getEnergyTier() {
        return this.energyTier;
    }

    @Override
    public int getEnergyStored() {
        return this.energy;
    }

    @Override
    public void onChanged(int oldAmount) {
        this.changedFunction.accept(oldAmount);
    }

    @Override
    public void setEnergyStored(int value) {
        if (this.energy != value) {
            int stored = energy;
            this.energy = value;
            onChanged(stored);
        }
    }

    @Override
    public int getEnergyCapacity() {
        return this.capacity;
    }

    @Override
    public void setEnergyCapacity(int value) {
        this.capacity = value;
    }

    @Override
    public void setOnChangedFunction(Consumer<Integer> onChangedFunction) {
        this.changedFunction = onChangedFunction;
    }

    @Override
    public @NotNull CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("energy", this.energy);
        tag.putInt("capacity", this.capacity);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        this.energy = tag.getInt("energy");
        this.capacity = tag.getInt("capacity");
    }

    public static class NoDrain extends EnergyHandlerImpl implements EnergyHandler.NoDrain {
        public NoDrain(Supplier<? extends EnergyTier> energyTier) {
            super(energyTier);
        }

        @Override
        public int drainEnergy(int value, boolean simulate) {
            return EnergyHandler.NoDrain.super.drainEnergy(value, simulate);
        }

    }

    public static class NoFill extends EnergyHandlerImpl implements EnergyHandler.NoFill {
        public NoFill(Supplier<? extends EnergyTier> energyTier) {
            super(energyTier);
        }

        @Override
        public int fillEnergy(int value, boolean simulate) {
            return EnergyHandler.NoFill.super.fillEnergy(value, simulate);
        }

    }

}
