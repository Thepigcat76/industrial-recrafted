package com.portingdeadmods.indrec.api.energy.items;

import com.portingdeadmods.indrec.registries.IRDataComponents;
import com.portingdeadmods.indrec.api.energy.EnergyHandler;
import com.portingdeadmods.indrec.api.energy.EnergyTier;
import com.portingdeadmods.indrec.impl.energy.ComponentEuStorage;
import com.portingdeadmods.indrec.utils.ItemBarUtils;
import com.portingdeadmods.indrec.utils.TooltipUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class ElectricArmorItem extends ArmorItem implements EnergyItem, ElectricConsumerItem {
    private final Supplier<? extends EnergyTier> energyTier;
    private final IntSupplier energyUsage;
    private final IntSupplier energyCapacity;

    public ElectricArmorItem(Properties properties, Holder<ArmorMaterial> material, Type type, Supplier<? extends EnergyTier> energyTier, IntSupplier energyUsage, IntSupplier energyCapacity) {
        super(material, type, properties
                .stacksTo(1)
                .component(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY)
                .component(IRDataComponents.ENERGY.get(), new ComponentEuStorage(energyCapacity.getAsInt())));
        this.energyTier = energyTier;
        this.energyUsage = energyUsage;
        this.energyCapacity = energyCapacity;
    }

    @Override
    public @NotNull ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        return stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
    }

    @Override
    public void initEnergyStorage(EnergyHandler energyStorage, ItemStack itemStack) {
        if (!this.canWork(energyStorage, itemStack, null) && requireEnergyToWork(itemStack)) {
            itemStack.remove(DataComponents.ATTRIBUTE_MODIFIERS);
        } else {
            itemStack.set(DataComponents.ATTRIBUTE_MODIFIERS, this.defaultModifiers.get());
        }
    }

    @Override
    public void onEnergyChanged(ItemStack itemStack, int oldAmount) {
        if (!this.canWork(itemStack)) {
            itemStack.remove(DataComponents.ATTRIBUTE_MODIFIERS);
        } else if (oldAmount == 0) {
            itemStack.set(DataComponents.ATTRIBUTE_MODIFIERS, this.defaultModifiers.get());
        }
    }

    public int getEnergyCapacity() {
        return this.energyCapacity.getAsInt();
    }

    @Override
    public EnergyTier getEnergyTier() {
        return this.energyTier.get();
    }

    @Override
    public int getBarWidth(@NotNull ItemStack itemStack) {
        return ItemBarUtils.energyBarWidth(itemStack);
    }

    @Override
    public int getBarColor(@NotNull ItemStack itemStack) {
        return ItemBarUtils.energyBarColor(itemStack);
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public int getMaxStackSize(@NotNull ItemStack stack) {
        return 1;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, ctx, tooltip, flag);

        TooltipUtils.addEnergyTooltip(tooltip, stack);
    }

    @Override
    public boolean requireEnergyToWork(ItemStack itemStack, @Nullable Entity entity) {
        return true;
    }

    public int getEnergyUsage() {
        return this.energyUsage.getAsInt();
    }

    @Override
    public int getEnergyUsage(ItemStack itemStack, @Nullable Entity entity) {
        return this.getEnergyUsage();
    }

}
