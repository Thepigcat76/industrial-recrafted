package com.portingdeadmods.indrec.content.blockentities;

import com.portingdeadmods.indrec.IRCapabilities;
import com.portingdeadmods.indrec.IRConfig;
import com.portingdeadmods.indrec.api.blockentities.GeneratorBlockEntity;
import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;
import com.portingdeadmods.indrec.content.menus.SolarPanelMenu;
import com.portingdeadmods.indrec.impl.recipes.MachineRecipeInput;
import com.portingdeadmods.indrec.impl.energy.EnergyHandlerImpl;
import com.portingdeadmods.indrec.registries.IREnergyTiers;
import com.portingdeadmods.indrec.registries.IRMachines;
import com.portingdeadmods.indrec.registries.IRTranslations;
import com.portingdeadmods.portingdeadlibs.utils.capabilities.HandlerUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SolarPanelBlockEntity extends MachineBlockEntity implements MenuProvider, GeneratorBlockEntity {
    public SolarPanelBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRMachines.BASIC_SOLAR_PANEL, blockPos, blockState);
        addEuStorage(EnergyHandlerImpl.NoFill::new, IREnergyTiers.LOW, IRConfig.basicSolarPanelEnergyCapacity, this::onEuChanged);
        this.addItemHandler(HandlerUtils::newItemStackHandler, builder -> builder
                .slots(1)
                .validator((slot, item) -> {
                    if (slot == 0) return item.getCapability(IRCapabilities.ENERGY_ITEM) != null;
                    throw new IllegalArgumentException("Non existent slot " + slot + "on Solar Panel");
                })
                .onChange(this::onItemsChanged));
    }

    @Override
    protected void onItemsChanged(int slot) {
    }

    @Override
    public void tickRecipe() {
        if (level.isDay() && level.canSeeSky(this.worldPosition.above())) {
            this.getEuStorage().forceFillEnergy(this.getGenerationAmount(), false);
        }

        GeneratorBlockEntity.transportEnergy(level, worldPosition, this.getEuStorage());
    }

    @Override
    protected void onEuChanged(int oldAmount) {
        this.updateData();
    }

    @Override
    protected @NotNull MachineRecipeInput createRecipeInput() {
        return new MachineRecipeInput();
    }

    @Override
    public boolean shouldSpreadEnergy() {
        return true;
    }

    @Override
    public int getGenerationAmount() {
        return IRConfig.basicSolarPanelEnergyProduction;
    }

    @Override
    public Component getDisplayName() {
        return IRTranslations.BASIC_SOLAR_PANEL.component();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new SolarPanelMenu(i, inventory, this);
    }
}
