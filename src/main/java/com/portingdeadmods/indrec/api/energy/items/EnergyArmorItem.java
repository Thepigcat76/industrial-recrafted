package com.portingdeadmods.indrec.api.energy.items;

import com.portingdeadmods.indrec.IRDataComponents;
import com.portingdeadmods.indrec.api.energy.EnergyTier;
import com.portingdeadmods.indrec.impl.energy.ComponentEuStorage;
import com.portingdeadmods.indrec.utils.ItemBarUtils;
import com.portingdeadmods.indrec.utils.TooltipUtils;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class EnergyArmorItem extends ArmorItem implements EnergyItem {
    private final Supplier<? extends EnergyTier> energyTier;
    private final IntSupplier energyUsage;
    private final IntSupplier energyCapacity;

    public EnergyArmorItem(Properties properties, Holder<ArmorMaterial> material, Type type, Supplier<? extends EnergyTier> energyTier, IntSupplier energyUsage, IntSupplier energyCapacity) {
        super(material, type, properties.stacksTo(1).component(IRDataComponents.ENERGY.get(), new ComponentEuStorage(energyCapacity.getAsInt())));
        this.energyTier = energyTier;
        this.energyUsage = energyUsage;
        this.energyCapacity = energyCapacity;
    }

    public int getEnergyUsage() {
        return this.energyUsage.getAsInt();
    }

    public int getEnergyCapacity() {
        return this.energyCapacity.getAsInt();
    }

    @Override
    public EnergyTier getEnergyTier() {
        return this.energyTier.get();
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

}
