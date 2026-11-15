package com.portingdeadmods.indrec.client.screens;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.api.client.screens.MachineScreen;
import com.portingdeadmods.indrec.client.screens.widgets.BatterySlotWidget;
import com.portingdeadmods.indrec.client.screens.widgets.IREnergyBarWidget;
import com.portingdeadmods.indrec.content.menus.CropManagerMenu;
import com.portingdeadmods.indrec.handlers.energy.IRGenericEnergyWrapper;
import com.portingdeadmods.indrec.registries.IRTranslations;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class CropManagerScreen extends MachineScreen<CropManagerMenu> {
    public static final ResourceLocation TEXTURE = IndustrialRecrafted.rl("textures/gui/crop_manager.png");

    public CropManagerScreen(CropManagerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        this.imageHeight = 186;
        this.inventoryLabelY = this.imageHeight - 94;
        super.init();

        IREnergyBarWidget energyBarWidget = addRenderableOnly(
                new IREnergyBarWidget(this.leftPos + 11, this.topPos + 17, 12, 48, new IRGenericEnergyWrapper(menu.blockEntity.getEuStorage()), IRTranslations.ENERGY_UNIT.component().getString())
                        .setHasBorder(true)
        );

        addRenderableOnly(new BatterySlotWidget(this.leftPos + 8, this.topPos + 14 + energyBarWidget.getHeight() + 5));
    }

    @Override
    public @NotNull ResourceLocation getBackgroundTexture() {
        return TEXTURE;
    }
}
