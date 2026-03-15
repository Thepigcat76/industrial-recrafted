package com.portingdeadmods.indrec.api.energy.items;

import com.portingdeadmods.indrec.registries.IRDataComponents;
import com.portingdeadmods.indrec.api.energy.EnergyTier;
import com.portingdeadmods.indrec.impl.energy.ComponentEuStorage;
import com.portingdeadmods.indrec.utils.ItemBarUtils;
import com.portingdeadmods.indrec.utils.TooltipUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public abstract class SimpleEnergyItem extends Item implements EnergyItem {
    protected final IntSupplier defaultEnergyCapacity;
    private final Supplier<? extends EnergyTier> energyTier;

    public SimpleEnergyItem(Properties properties, Supplier<? extends EnergyTier> energyTier, IntSupplier defaultEnergyCapacity) {
        super(properties.stacksTo(1).component(IRDataComponents.ENERGY.get(), new ComponentEuStorage(defaultEnergyCapacity.getAsInt())));
        this.defaultEnergyCapacity = defaultEnergyCapacity;
        this.energyTier = energyTier;
    }

    @Override
    public int getDefaultCapacity() {
        return defaultEnergyCapacity.getAsInt();
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        return ItemBarUtils.energyBarWidth(itemStack);
    }

    @Override
    public int getBarColor(ItemStack itemStack) {
        return ItemBarUtils.energyBarColor(itemStack);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, ctx, tooltip, flag);
        TooltipUtils.addEnergyTooltip(tooltip, stack);
    }

    @Override
    public EnergyTier getEnergyTier() {
        return energyTier.get();
    }
}