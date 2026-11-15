package com.portingdeadmods.indrec.content.blockentities;

import com.portingdeadmods.indrec.api.blockentities.GeneratorBlockEntity;
import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;
import com.portingdeadmods.indrec.api.recipes.MachineRecipe;
import com.portingdeadmods.indrec.handlers.energy.EnergyHandlerImpl;
import com.portingdeadmods.indrec.utils.machines.IRMachine;
import com.portingdeadmods.portingdeadlibs.utils.capabilities.HandlerUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntSupplier;

public class SolarPanelBlockEntity extends MachineBlockEntity implements GeneratorBlockEntity {
    private final IntSupplier energyProduction;

    public SolarPanelBlockEntity(IRMachine machine, IntSupplier energyProduction, BlockPos blockPos, BlockState blockState) {
        super(machine, blockPos, blockState);
        this.energyProduction = energyProduction;
        this.addMachineEuStorage(EnergyHandlerImpl.NoFill::new, this::onEuChanged);
        this.addItemHandler(HandlerUtils::newItemStackHandler, builder -> builder
                .slots(1)
                .validator((slot, item) -> {
                    if (slot == 0) return this.isValidChargingItem(item);
                    throw new IllegalArgumentException("Non existent slot " + slot + "on Solar Panel");
                })
                .onChange(this::onItemsChanged));
    }

    @Override
    public void tickRecipe() {
        if (!level.isClientSide() && level.isDay() && level.canSeeSky(this.worldPosition.above())) {
            this.getEuStorage().forceFillEnergy(this.getGenerationAmount(), false);
        }

        GeneratorBlockEntity.transportEnergy(level, worldPosition, this.getEuStorage());
    }

    @Override
    protected @NotNull MachineRecipe.MachineRecipeInput createRecipeInput() {
        return MachineRecipe.MachineRecipeInput.EMPTY;
    }

    @Override
    public boolean shouldSpreadEnergy() {
        return true;
    }

    @Override
    public int getGenerationAmount() {
        return this.energyProduction.getAsInt();
    }
}
