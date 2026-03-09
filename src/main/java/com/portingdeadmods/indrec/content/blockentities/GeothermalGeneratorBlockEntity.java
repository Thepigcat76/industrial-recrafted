package com.portingdeadmods.indrec.content.blockentities;

import com.portingdeadmods.indrec.IRCapabilities;
import com.portingdeadmods.indrec.IRConfig;
import com.portingdeadmods.indrec.api.blockentities.GeneratorBlockEntity;
import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;
import com.portingdeadmods.indrec.content.menus.GeothermalGeneratorMenu;
import com.portingdeadmods.indrec.content.recipes.MachineRecipeInput;
import com.portingdeadmods.indrec.impl.energy.EnergyHandlerImpl;
import com.portingdeadmods.indrec.registries.IREnergyTiers;
import com.portingdeadmods.indrec.registries.IRMachines;
import com.portingdeadmods.indrec.registries.IRTranslations;
import com.portingdeadmods.portingdeadlibs.utils.SerializerUtils;
import com.portingdeadmods.portingdeadlibs.utils.capabilities.HandlerUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GeothermalGeneratorBlockEntity extends MachineBlockEntity implements MenuProvider, GeneratorBlockEntity {
    public GeothermalGeneratorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRMachines.GEOTHERMAL_GENERATOR, blockPos, blockState);
        this.addEuStorage(EnergyHandlerImpl.NoFill::new, IREnergyTiers.MEDIUM, IRConfig.geothermalGeneratorEnergyCapacity, this::onEuChanged);
        this.addItemHandler(HandlerUtils::newItemStackHandler, builder -> builder
                .slots(3)
                .onChange(this::onItemsChanged)
                .validator((slot, stack) -> switch (slot) {
                    case 0 -> stack.getCapability(Capabilities.FluidHandler.ITEM) != null;
                    case 1 -> false;
                    case 2 -> stack.getCapability(IRCapabilities.ENERGY_ITEM) != null;
                    default -> throw new IllegalStateException("No slot 3");
                }));
        this.addHandler(Capabilities.FluidHandler.BLOCK, HandlerUtils.newFluidTank(
                (tank, fluid) -> true,
                tank -> IRConfig.geothermalGeneratorFluidCapacity,
                this::onFluidsChanged,
                1
        ), SerializerUtils::fluidTank);
    }

    @Override
    public void tick() {
        super.tick();

        IItemHandler itemHandler = getItemHandler();
        ItemStack drainStack = itemHandler.getStackInSlot(0);
        IFluidHandler itemFluidHandler = drainStack.getCapability(Capabilities.FluidHandler.ITEM);
        if (itemFluidHandler != null) {
            FluidStack fluidInItemFluidHandler = itemFluidHandler.getFluidInTank(0);
            if (!fluidInItemFluidHandler.isEmpty() && this.getFluidHandler().isFluidValid(0, fluidInItemFluidHandler)) {
                FluidStack fluidInBlockFluidHandler = this.getFluidHandler().getFluidInTank(0);
                int drainAmount = this.getFluidHandler().getTankCapacity(0) - fluidInBlockFluidHandler.getAmount();
                FluidStack drained = itemFluidHandler.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);
                this.getFluidHandler().fill(drained, IFluidHandler.FluidAction.EXECUTE);
                if (itemFluidHandler.getFluidInTank(0).isEmpty()) {
                    forceInsertItem((IItemHandlerModifiable) this.getItemHandler(), 1, itemHandler.getStackInSlot(1), false, this::onItemsChanged);
                }
            }
        }

        GeneratorBlockEntity.transportEnergy(level, worldPosition, this.getEuStorage());

    }

    @Override
    protected @NotNull MachineRecipeInput createRecipeInput() {
        return new MachineRecipeInput(this.getFluidHandler().getFluidInTank(0));
    }

    @Override
    public boolean shouldSpreadEnergy() {
        return true;
    }

    @Override
    public int getGenerationAmount() {
        return IRConfig.geothermalGeneratorEnergyProduction;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return IRTranslations.GEOTHERMAL_GENERATOR.component();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new GeothermalGeneratorMenu(i, inventory, this);
    }

}
