package com.portingdeadmods.indrec.client.screens;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.api.client.screens.MachineScreen;
import com.portingdeadmods.indrec.api.energy.IRGenericEnergyWrapper;
import com.portingdeadmods.indrec.client.screens.widgets.BatterySlotWidget;
import com.portingdeadmods.indrec.content.menus.RecyclerMenu;
import com.portingdeadmods.indrec.registries.IRTranslations;
import com.portingdeadmods.portingdeadlibs.client.screens.widgets.EnergyBarWidget;
import com.portingdeadmods.portingdeadlibs.client.screens.widgets.RedstonePanelWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class RecyclerScreen extends MachineScreen<RecyclerMenu> {
    public static final ResourceLocation PROGRESS_ARROW_SPRITE = IndustrialRecrafted.rl("container/progress_arrow");
    private static final ResourceLocation TEXTURE = IndustrialRecrafted.rl("textures/gui/recycler.png");

    public RecyclerScreen(RecyclerMenu menu, Inventory playerInventory, Component title) {
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
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        float progress = (float) this.menu.blockEntity.getProgress() / this.menu.blockEntity.getMaxProgress();

        pGuiGraphics.blitSprite(PROGRESS_ARROW_SPRITE, 24, 16, 0, 0, this.getGuiLeft() + 76, this.getGuiTop() + 45, (int) (24 * progress), 16);
    }

    @Override
    public @NotNull ResourceLocation getBackgroundTexture() {
        return TEXTURE;
    }
}
