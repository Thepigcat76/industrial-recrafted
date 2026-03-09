package com.portingdeadmods.indrec.client.screens;

import com.portingdeadmods.indrec.IndustrialReclassified;
import com.portingdeadmods.indrec.api.client.screens.MachineScreen;
import com.portingdeadmods.indrec.api.energy.IRGenericEnergyWrapper;
import com.portingdeadmods.indrec.content.menus.SolarPanelMenu;
import com.portingdeadmods.indrec.registries.IRTranslations;
import com.portingdeadmods.portingdeadlibs.client.screens.widgets.EnergyBarWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class SolarPanelScreen extends MachineScreen<SolarPanelMenu> {
    public SolarPanelScreen(SolarPanelMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        EnergyBarWidget energyBarWidget = this.addRenderableOnly(
                new EnergyBarWidget(this.leftPos + 11, this.topPos + 17, new IRGenericEnergyWrapper(menu.blockEntity.getEuStorage()), IRTranslations.ENERGY_UNIT.component().getString(), true)
        );
    }

    @Override
    public @NotNull ResourceLocation getBackgroundTexture() {
        return IndustrialReclassified.rl("");
    }
}
