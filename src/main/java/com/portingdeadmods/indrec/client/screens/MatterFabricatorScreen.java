package com.portingdeadmods.indrec.client.screens;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.api.client.screens.MachineScreen;
import com.portingdeadmods.indrec.client.screens.widgets.BatterySlotWidget;
import com.portingdeadmods.indrec.client.screens.widgets.IREnergyBarWidget;
import com.portingdeadmods.indrec.content.menus.MatterFabricatorMenu;
import com.portingdeadmods.indrec.impl.energy.IRGenericEnergyWrapper;
import com.portingdeadmods.indrec.registries.IRTranslations;
import com.portingdeadmods.portingdeadlibs.client.screens.widgets.RedstonePanelWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class MatterFabricatorScreen extends MachineScreen<MatterFabricatorMenu> {
    public static final ResourceLocation TEXTURE = IndustrialRecrafted.rl("textures/gui/matter_fabricator.png");

    public MatterFabricatorScreen(MatterFabricatorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        this.imageHeight = 186;
        this.inventoryLabelY = this.imageHeight - 94;
        super.init();

        IREnergyBarWidget energyBarWidget = addRenderableOnly(
                new IREnergyBarWidget(0, 0, 96, 12, new IRGenericEnergyWrapper(menu.blockEntity.getEuStorage()), IRTranslations.ENERGY_UNIT.component().getString())
        ).setHasBorder(true).setOrientation(IREnergyBarWidget.Orientation.HORIZONTAL);
        energyBarWidget.setPosition((this.width - energyBarWidget.getWidth()) / 2, this.topPos + 51);

        addPanelWidget(new RedstonePanelWidget(this.leftPos + this.imageWidth, this.topPos + 2));
        addRenderableOnly(new BatterySlotWidget((this.width - energyBarWidget.getWidth()) / 2 - 18 - 4, this.topPos + 31 + energyBarWidget.getHeight() + 5));
    }

    @Override
    public @NotNull ResourceLocation getBackgroundTexture() {
        return TEXTURE;
    }
}
