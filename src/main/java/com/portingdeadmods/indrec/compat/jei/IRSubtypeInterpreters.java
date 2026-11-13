package com.portingdeadmods.indrec.compat.jei;

import com.portingdeadmods.indrec.IRCapabilities;
import com.portingdeadmods.indrec.api.energy.EnergyHandler;
import com.portingdeadmods.indrec.registries.IRDataComponents;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.Nullable;

public final class IRSubtypeInterpreters {
    public static final ISubtypeInterpreter<ItemStack> FLUID = new ISubtypeInterpreter<>() {
        @Override
        public @Nullable Object getSubtypeData(ItemStack stack, UidContext context) {
            Fluid fluid = getFluid(stack);
            return fluid == Fluids.EMPTY ? null : fluid;
        }

        @Override
        public String getLegacyStringSubtypeInfo(ItemStack stack, UidContext context) {
            Fluid fluid = getFluid(stack);
            return fluid == Fluids.EMPTY ? "" : BuiltInRegistries.FLUID.getKey(fluid).toString();
        }

        private static Fluid getFluid(ItemStack stack) {
            IFluidHandlerItem handler = stack.getCapability(Capabilities.FluidHandler.ITEM);
            if (handler == null) return Fluids.EMPTY;
            for (int i = 0; i < handler.getTanks(); i++) {
                FluidStack fluidStack = handler.getFluidInTank(i);
                if (!fluidStack.isEmpty()) return fluidStack.getFluid();
            }
            return Fluids.EMPTY;
        }
    };

    public static final ISubtypeInterpreter<ItemStack> ENERGY = new ISubtypeInterpreter<>() {
        @Override
        public @Nullable Object getSubtypeData(ItemStack stack, UidContext context) {
            // Recipes should ignore the charge, so that a charged item still matches recipe inputs/outputs
            if (context == UidContext.Recipe) return null;
            return isFullyCharged(stack) ? Boolean.TRUE : null;
        }

        @Override
        public String getLegacyStringSubtypeInfo(ItemStack stack, UidContext context) {
            if (context == UidContext.Recipe) return "";
            return isFullyCharged(stack) ? "charged" : "";
        }

        private static boolean isFullyCharged(ItemStack stack) {
            EnergyHandler handler = stack.getCapability(IRCapabilities.ENERGY_ITEM);
            return handler != null && handler.getEnergyCapacity() > 0 && handler.getEnergyStored() >= handler.getEnergyCapacity();
        }
    };

    private IRSubtypeInterpreters() {
    }
}
