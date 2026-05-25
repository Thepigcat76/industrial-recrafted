package com.portingdeadmods.indrec.registries;

import com.portingdeadmods.indrec.IRCapabilities;
import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.api.blocks.MachineBlock;
import com.portingdeadmods.indrec.api.energy.EnergyHandler;
import com.portingdeadmods.indrec.content.items.FluidCellItem;
import com.portingdeadmods.indrec.content.items.FuelJetpackItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class IRCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, IndustrialRecrafted.MODID);

    public static final Supplier<CreativeModeTab> MAIN = TABS.register("main", () -> CreativeModeTab.builder()
            .icon(IRItems.BASIC_DRILL::toStack)
            .title(Component.literal(IndustrialRecrafted.MODNAME))
            .displayItems((params, output) -> {
                IRMachines.HELPER.addToCreativeTab(output::accept);
                IRItems.ITEMS.getCreativeTabItems().stream().map(Supplier::get).map(ItemStack::new).filter(item -> !(Block.byItem(item.getItem()) instanceof MachineBlock)).peek(stack -> {
                    EnergyHandler handler = stack.getCapability(IRCapabilities.ENERGY_ITEM);
                    if (handler != null) {
                        output.accept(stack.copy());
                        handler.setEnergyStored(handler.getEnergyCapacity());
                    } else if (stack.getItem() instanceof FluidCellItem) {
                        for (Fluid fluid : BuiltInRegistries.FLUID) {
                            if (fluid.isSource(fluid.defaultFluidState())) {
                                ItemStack newStack = stack.copy();
                                IFluidHandlerItem fluidHandlerItem = newStack.getCapability(Capabilities.FluidHandler.ITEM);
                                fluidHandlerItem.fill(new FluidStack(fluid, fluidHandlerItem.getTankCapacity(0)), IFluidHandler.FluidAction.EXECUTE);
                                output.accept(newStack);
                            }
                        }
                    } else if (stack.getItem() instanceof FuelJetpackItem) {
                        output.accept(stack.copy());
                        IFluidHandlerItem fluidHandlerItem = stack.getCapability(Capabilities.FluidHandler.ITEM);
                        fluidHandlerItem.fill(new FluidStack(IRFluids.BIO_FUEL.getStillFluid(), fluidHandlerItem.getTankCapacity(0)), IFluidHandler.FluidAction.EXECUTE);
                    }
                }).forEach(output::accept);
            })
            .build());
}
