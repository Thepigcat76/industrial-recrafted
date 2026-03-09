package com.portingdeadmods.indrec.content.blockentities;

import com.portingdeadmods.indrec.IRConfig;
import com.portingdeadmods.indrec.api.blockentities.GeneratorBlockEntity;
import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;
import com.portingdeadmods.indrec.content.menus.SolarPanelMenu;
import com.portingdeadmods.indrec.impl.energy.EnergyHandlerImpl;
import com.portingdeadmods.indrec.registries.IREnergyTiers;
import com.portingdeadmods.indrec.registries.IRMachines;
import com.portingdeadmods.indrec.registries.IRTranslations;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SolarPanelBlockEntity extends MachineBlockEntity implements MenuProvider, GeneratorBlockEntity {
    public SolarPanelBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRMachines.BASIC_SOLAR_PANEL, blockPos, blockState);
        addEuStorage(EnergyHandlerImpl.NoFill::new, IREnergyTiers.LOW, IRConfig.basicSolarPanelEnergyCapacity, this::onEuChanged);
    }

    @Override
    public void tick() {
        super.tick();

        if (level.isDay() && level.canSeeSky(this.worldPosition.above())) {
            this.getEuStorage().forceFillEnergy(this.getGenerationAmount(), false);
        }
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
