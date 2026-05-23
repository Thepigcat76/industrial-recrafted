package com.portingdeadmods.indrec.client.screens;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.api.client.screens.MachineScreen;
import com.portingdeadmods.indrec.impl.energy.IRGenericEnergyWrapper;
import com.portingdeadmods.indrec.client.screens.widgets.BatterySlotWidget;
import com.portingdeadmods.indrec.content.menus.GeothermalGeneratorMenu;
import com.portingdeadmods.indrec.registries.IRTranslations;
import com.portingdeadmods.portingdeadlibs.client.screens.widgets.EnergyBarWidget;
import com.portingdeadmods.portingdeadlibs.client.screens.widgets.FluidTankWidget;
import com.portingdeadmods.portingdeadlibs.client.screens.widgets.RedstonePanelWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class GeothermalGeneratorScreen extends MachineScreen<GeothermalGeneratorMenu> {
    public static final ResourceLocation TEXTURE = IndustrialRecrafted.rl("textures/gui/geothermal_generator.png");

    public GeothermalGeneratorScreen(GeothermalGeneratorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();

        this.imageHeight = 186;
        this.inventoryLabelY = this.imageHeight - 94;
        super.init();
        EnergyBarWidget energyBarWidget = addRenderableOnly(
                new EnergyBarWidget(this.leftPos + 11, this.topPos + 17, new IRGenericEnergyWrapper(menu.blockEntity.getEuStorage()), IRTranslations.ENERGY_UNIT.component().getString(), true)
        );
        addPanelWidget(new RedstonePanelWidget(this.leftPos + this.imageWidth, this.topPos + 2));
        addRenderableOnly(new BatterySlotWidget(this.leftPos + 8, this.topPos + 14 + energyBarWidget.getHeight() + 5));

        FluidTankWidget fluidTankWidget = addRenderableWidget(
                new FluidTankWidget(this.leftPos + (this.imageWidth - 18) / 2 + 10, this.topPos + 23, FluidTankWidget.TankVariants.SMALL, menu.blockEntity)
        );

    }

    @Override
    public @NotNull ResourceLocation getBackgroundTexture() {
        return TEXTURE;
    }
}
