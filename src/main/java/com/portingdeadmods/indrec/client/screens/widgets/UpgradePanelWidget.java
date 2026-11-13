package com.portingdeadmods.indrec.client.screens.widgets;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.api.blockentities.UpgradeBlockEntity;
import com.portingdeadmods.indrec.api.menus.IRSlotItemHandler;
import com.portingdeadmods.indrec.api.menus.UpgradeMenu;
import com.portingdeadmods.indrec.networking.serverbound.UpgradeWidgetSetSlotPositionsPayload;
import com.portingdeadmods.indrec.networking.serverbound.UpgradeWidgetTogglePayload;
import com.portingdeadmods.portingdeadlibs.api.client.screens.widgets.MenuWidgetContext;
import com.portingdeadmods.portingdeadlibs.api.client.screens.widgets.PanelWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;

public class UpgradePanelWidget extends PanelWidget {
    public static final ResourceLocation WIDGET_SPRITE = IndustrialRecrafted.rl("widget/widget_upgrade_right");
    public static final ResourceLocation WIDGET_OPEN_SPRITE = IndustrialRecrafted.rl("widget/widget_upgrade_open");
    public static final int WIDGET_WIDTH = 32, WIDGET_HEIGHT = 32;
    public static final int WIDGET_OPEN_WIDTH = 32, WIDGET_OPEN_HEIGHT = 112;
    private static final int CLOSED_SLOTS_START_Y = 51;

    private UpgradeMenu upgradeMenu;

    public UpgradePanelWidget(int x, int y) {
        super(x, y, WIDGET_OPEN_WIDTH - 8, WIDGET_OPEN_HEIGHT - 8, WIDGET_WIDTH - 8, WIDGET_HEIGHT - 8);
        this.open = false;
    }

    @Override
    public void setContext(MenuWidgetContext context) {
        super.setContext(context);

        if (context.menu() instanceof UpgradeMenu menu) {
            this.upgradeMenu = menu;

            // The menu outlives this widget when the screen gets resized, so pick up the state of its slots
            for (IRSlotItemHandler upgradeSlot : menu.getUpgradeSlots()) {
                this.open = upgradeSlot.isActive();
                break;
            }
            if (this.open) {
                this.setSize(WIDGET_OPEN_WIDTH, WIDGET_OPEN_HEIGHT);
            }
            menu.internalSetUpgradeSlotPositions(CLOSED_SLOTS_START_Y);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean isHovered = mouseX >= this.getX()
                && mouseY >= this.getY()
                && mouseX < this.getX() + this.getClosedWidth()
                && mouseY < this.getY() + (this.getClosedHeight() - 4);

        if (isHovered) {
            this.open = !this.open;

            PacketDistributor.sendToServer(new UpgradeWidgetTogglePayload(open));
            for (IRSlotItemHandler upgradeSlot : this.upgradeMenu.getUpgradeSlots()) {
                upgradeSlot.setActive(this.open);
            }

            if (open) {
                this.setSize(WIDGET_OPEN_WIDTH, WIDGET_OPEN_HEIGHT);
            } else {
                this.setSize(WIDGET_WIDTH, WIDGET_HEIGHT);
            }
            this.context.onWidgetResizeFunc().accept(this);
            return super.mouseClicked(mouseX, mouseY, button);
        }
        return false;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int i1, float v) {
        if (open) {
            guiGraphics.blitSprite(WIDGET_OPEN_SPRITE, getX(), getY(), WIDGET_OPEN_WIDTH, WIDGET_OPEN_HEIGHT);
        } else {
            guiGraphics.blitSprite(WIDGET_SPRITE, getX(), getY(), WIDGET_WIDTH, WIDGET_HEIGHT);
        }
    }

    @Override
    public void onWidgetResized(PanelWidget resizedWidget) {
        super.onWidgetResized(resizedWidget);

        if (resizedWidget.isOpen()) {
            PacketDistributor.sendToServer(new UpgradeWidgetSetSlotPositionsPayload(27 + resizedWidget.getOpenHeight()));
            this.upgradeMenu.internalSetUpgradeSlotPositions(27 + resizedWidget.getOpenHeight());
        } else {
            PacketDistributor.sendToServer(new UpgradeWidgetSetSlotPositionsPayload(CLOSED_SLOTS_START_Y));
            this.upgradeMenu.internalSetUpgradeSlotPositions(CLOSED_SLOTS_START_Y);
        }

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

}