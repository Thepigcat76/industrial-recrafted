package com.portingdeadmods.indrec.client.screens;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.api.client.screens.MachineScreen;
import com.portingdeadmods.indrec.client.screens.widgets.BatterySlotWidget;
import com.portingdeadmods.indrec.client.screens.widgets.IREnergyBarWidget;
import com.portingdeadmods.indrec.impl.energy.IRGenericEnergyWrapper;
import com.portingdeadmods.indrec.content.menus.SolarPanelMenu;
import com.portingdeadmods.indrec.registries.IRTranslations;
import com.portingdeadmods.portingdeadlibs.client.screens.widgets.EnergyBarWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class SolarPanelScreen extends MachineScreen<SolarPanelMenu> {
    public static final ResourceLocation TEXTURE = IndustrialRecrafted.rl("textures/gui/solar_panel.png");

    public SolarPanelScreen(SolarPanelMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();

        IREnergyBarWidget energyBarWidget = addRenderableOnly(
                new IREnergyBarWidget(this.leftPos + 11, this.topPos + 17, 48, 12, new IRGenericEnergyWrapper(menu.blockEntity.getEuStorage()), IRTranslations.ENERGY_UNIT.component().getString())
        ).setHasBorder(true).setOrientation(IREnergyBarWidget.Orientation.HORIZONTAL);
        energyBarWidget.setPosition((this.width - energyBarWidget.getWidth()) / 2, this.topPos + 28);

        addRenderableOnly(new BatterySlotWidget((this.width - 18) / 2, energyBarWidget.getY() + 16));
    }

    @Override
    public @NotNull ResourceLocation getBackgroundTexture() {
        return TEXTURE;
    }
}
