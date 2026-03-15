package com.portingdeadmods.indrec.impl.energy;

import com.portingdeadmods.indrec.registries.IRDataComponents;
import com.portingdeadmods.indrec.api.energy.EnergyHandler;
import com.portingdeadmods.indrec.api.energy.items.EnergyItem;
import com.portingdeadmods.indrec.api.energy.EnergyTier;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public record ItemEnergyHandlerWrapper(ItemStack itemStack, Supplier<? extends EnergyTier> energyTier) implements EnergyHandler {
    public ItemEnergyHandlerWrapper(ItemStack itemStack, Supplier<? extends EnergyTier> energyTier, int initialCapacity) {
        this(itemStack, energyTier);
        this.setEnergyCapacity(initialCapacity);
        if (itemStack.getItem() instanceof EnergyItem energyItem) {
            energyItem.initEnergyStorage(this, itemStack);
        }
    }

    @Override
    public EnergyTier getEnergyTier() {
        return energyTier.get();
    }

    @Override
    public void onChanged(int oldAmount) {
        if (itemStack.getItem() instanceof EnergyItem energyItem) {
            energyItem.onEnergyChanged(itemStack, oldAmount);
        }
    }

    @Override
    public int getEnergyStored() {
        ComponentEuStorage componentEuStorage = itemStack.get(IRDataComponents.ENERGY);
        if (componentEuStorage != null)
            return componentEuStorage.energyStored();
        else
            throw new NullPointerException("Failed to get energy component for items: "
                    + itemStack.getItem()
                    + " please add it under the items properties using .component(...) or preferably inherit one of the electric items classes");
    }

    @Override
    public void setEnergyStored(int value) {
        int energyStored = getEnergyStored();
        itemStack.set(IRDataComponents.ENERGY, new ComponentEuStorage(value, getEnergyCapacity()));
        onChanged(energyStored);
    }

    @Override
    public int getEnergyCapacity() {
        ComponentEuStorage componentEuStorage = itemStack.get(IRDataComponents.ENERGY);
        if (componentEuStorage != null)
            return componentEuStorage.energyCapacity();
        else
            throw new NullPointerException("Failed to get energy component for items: "
                    + itemStack.getItem()
                    + " please add it under the items properties using .component(...) or preferably inherit one of the electric items classes");
    }

    @Override
    public void setEnergyCapacity(int value) {
        itemStack.set(IRDataComponents.ENERGY, new ComponentEuStorage(getEnergyStored(), value));
    }
}