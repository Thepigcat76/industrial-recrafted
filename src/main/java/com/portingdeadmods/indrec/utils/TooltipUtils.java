package com.portingdeadmods.indrec.utils;

import com.portingdeadmods.indrec.IRCapabilities;
import com.portingdeadmods.indrec.IRRegistries;
import com.portingdeadmods.indrec.api.energy.EnergyHandler;
import com.portingdeadmods.indrec.api.energy.EnergyTier;
import com.portingdeadmods.indrec.registries.IRTranslations;
import com.portingdeadmods.portingdeadlibs.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import java.util.List;

public final class TooltipUtils {

    public static void addEnergyTooltip(List<Component> tooltip, ItemStack itemStack) {
        EnergyHandler energyStorage = itemStack.getCapability(IRCapabilities.ENERGY_ITEM);
        if (energyStorage != null) {
            tooltip.add(
                    IRTranslations.ENERGY_STORED.component()
                            .withStyle(ChatFormatting.GRAY)
                            .append(IRTranslations.ENERGY_AMOUNT_WITH_CAPACITY.component(energyStorage.getEnergyStored(), energyStorage.getEnergyCapacity())
                                    .withColor(FastColor.ARGB32.color(255, 245, 192, 89)))
                            .append(" ")
                            .append(IRTranslations.ENERGY_UNIT.component()
                                    .withColor(FastColor.ARGB32.color(255, 245, 192, 89)))
            );
            EnergyTier tier = energyStorage.getEnergyTier();
            addEnergyTierTooltip(tooltip, tier);
        }
    }

    public static void addEnergyTierTooltip(List<Component> tooltip, EnergyTier tier) {
        if (tier != null) {
            tooltip.add(
                    IRTranslations.ENERGY_TIER.component()
                            .withStyle(ChatFormatting.GRAY)
                            .append(Utils.registryTranslation(IRRegistries.ENERGY_TIER, tier).copy().withColor(tier.color()))
            );
        }
    }

    public static void addFluidToolTip(List<Component> tooltip, ItemStack itemStack) {
        IFluidHandlerItem item = itemStack.getCapability(Capabilities.FluidHandler.ITEM);

        if (item != null && !item.getFluidInTank(0).isEmpty()) {
            tooltip.add(IRTranslations.FLUID_TYPE.component()
                    .withStyle(ChatFormatting.GRAY)
                    .append(item.getFluidInTank(0).getHoverName().copy()
                            .withColor(-1)));
            tooltip.add(IRTranslations.FLUID_STORED.component()
                    .withStyle(ChatFormatting.GRAY)
                    .append(IRTranslations.FLUID_AMOUNT_WITH_CAPACITY.component(item.getFluidInTank(0).getAmount(), item.getTankCapacity(0))
                            .withStyle(ChatFormatting.WHITE))
                    .append(" ")
                    .append(IRTranslations.FLUID_UNIT.component()
                            .withStyle(ChatFormatting.WHITE)));
        }
    }

    public static void addFluidToolTipAlways(List<Component> tooltip, ItemStack itemStack, int capacity) {
        IFluidHandlerItem item = itemStack.getCapability(Capabilities.FluidHandler.ITEM);

        MutableComponent component;
        int stored;
        if (item != null && !item.getFluidInTank(0).isEmpty()) {
            component = item.getFluidInTank(0).getHoverName().copy();
            stored = item.getFluidInTank(0).getAmount();
        } else {
            component = Component.literal("Empty");
            stored = 0;
        }
        tooltip.add(IRTranslations.FLUID_TYPE.component()
                .withStyle(ChatFormatting.GRAY)
                .append(component.withColor(-1)));
        tooltip.add(IRTranslations.FLUID_STORED.component()
                .withStyle(ChatFormatting.GRAY)
                .append(IRTranslations.FLUID_AMOUNT_WITH_CAPACITY.component(stored, capacity)
                        .withStyle(ChatFormatting.WHITE))
                .append(" ")
                .append(IRTranslations.FLUID_UNIT.component()
                        .withStyle(ChatFormatting.WHITE)));

    }

}