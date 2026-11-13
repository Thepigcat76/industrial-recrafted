package com.portingdeadmods.indrec.content.blockentities;

import com.portingdeadmods.indrec.IRConfig;
import com.portingdeadmods.indrec.api.blockentities.GeneratorBlockEntity;
import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;
import com.portingdeadmods.indrec.api.recipes.MachineRecipe;
import com.portingdeadmods.indrec.content.menus.GeothermalGeneratorMenu;
import com.portingdeadmods.indrec.impl.energy.EnergyHandlerImpl;
import com.portingdeadmods.indrec.registries.IRMachines;
import com.portingdeadmods.indrec.registries.IRRecipeLayouts;
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
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GeothermalGeneratorBlockEntity extends MachineBlockEntity implements MenuProvider, GeneratorBlockEntity {
    public GeothermalGeneratorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRMachines.GEOTHERMAL_GENERATOR, blockPos, blockState);
        this.addMachineEuStorage(EnergyHandlerImpl.NoFill::new, this::onEuChanged);
        this.addItemHandler(HandlerUtils::newItemStackHandler, builder -> builder
                .slots(3)
                .onChange(this::onItemsChanged)
                .validator((slot, stack) -> switch (slot) {
                    case 0 -> stack.getCapability(Capabilities.FluidHandler.ITEM) != null;
                    case 1 -> false;
                    case 2 -> this.isValidChargingItem(stack);
                    default -> throw new IllegalStateException("No slot 3");
                }));
        this.addHandler(Capabilities.FluidHandler.BLOCK, HandlerUtils.newFluidTank(
                (tank, fluid) -> level.getRecipeManager().getRecipeFor(IRRecipeLayouts.GEOTHERMAL_GENERATOR.getRecipeType(), new MachineRecipe.MachineRecipeInput(fluid), level).isPresent(),
                tank -> IRConfig.geothermalGeneratorFluidCapacity,
                this::onFluidsChanged,
                1
        ), SerializerUtils::fluidTank);
    }

    @Override
    public void tick() {
        if (!level.isClientSide()) {
            this.tickFluidInput();
        }

        GeneratorBlockEntity.transportEnergy(level, worldPosition, this.getEuStorage());

        super.tick();
    }

    private void tickFluidInput() {
        IItemHandler itemHandler = getItemHandler();
        ItemStack inputStack = itemHandler.getStackInSlot(0);
        if (inputStack.isEmpty()) return;

        // Fluid item handlers (e.g. FluidHandlerItemStack) refuse to drain stacks with count > 1,
        // so always operate on a single copied item and move it to the output slot afterwards
        ItemStack singleItem = inputStack.copyWithCount(1);
        IFluidHandlerItem itemFluidHandler = singleItem.getCapability(Capabilities.FluidHandler.ITEM);
        if (itemFluidHandler == null) return;

        IFluidHandler tank = this.getFluidHandler();
        FluidStack contained = itemFluidHandler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
        // Only empty the item if all of its fluid fits into the tank
        if (contained.isEmpty() || !tank.isFluidValid(0, contained) || tank.fill(contained, IFluidHandler.FluidAction.SIMULATE) != contained.getAmount()) {
            return;
        }

        FluidStack drained = itemFluidHandler.drain(contained, IFluidHandler.FluidAction.EXECUTE);
        ItemStack emptyContainer = itemFluidHandler.getContainer();
        IItemHandlerModifiable modifiableHandler = (IItemHandlerModifiable) itemHandler;
        if (!emptyContainer.isEmpty() && !forceInsertItem(modifiableHandler, 1, emptyContainer, true, this::onItemsChanged).isEmpty()) {
            // Output slot is full, wait until there is space for the empty container
            return;
        }

        itemHandler.extractItem(0, 1, false);
        if (!emptyContainer.isEmpty()) {
            forceInsertItem(modifiableHandler, 1, emptyContainer, false, this::onItemsChanged);
        }
        tank.fill(drained, IFluidHandler.FluidAction.EXECUTE);
    }

    @Override
    protected @NotNull MachineRecipe.MachineRecipeInput createRecipeInput() {
        return new MachineRecipe.MachineRecipeInput(this.getFluidHandler().getFluidInTank(0));
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
        this.syncProgress();
        return new GeothermalGeneratorMenu(i, inventory, this);
    }

}
