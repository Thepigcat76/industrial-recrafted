package com.portingdeadmods.indrec.api.client.screens;

import com.portingdeadmods.indrec.api.menus.MachineMenu;
import com.portingdeadmods.portingdeadlibs.api.client.screens.PDLAbstractContainerScreen;
import com.portingdeadmods.portingdeadlibs.api.client.screens.widgets.MenuWidgetContext;
import com.portingdeadmods.portingdeadlibs.api.client.screens.widgets.PanelWidget;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public abstract class MachineScreen<T extends MachineMenu<?>> extends PDLAbstractContainerScreen<T> {
    protected final List<PanelWidget> panelWidgets;

    public MachineScreen(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.panelWidgets = new ArrayList<>();
    }

    @Override
    protected void init() {
        // Widgets get created again whenever the screen is resized
        this.panelWidgets.clear();
        super.init();
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeft, int guiTop, int mouseButton) {
        for (Rect2i bounds : this.getBounds()) {
            if (bounds.contains((int) mouseX, (int) mouseY)) {
                return false;
            }
        }
        return super.hasClickedOutside(mouseX, mouseY, guiLeft, guiTop, mouseButton);
    }

    public void addPanelWidget(PanelWidget widget) {
        widget.visitWidgets(this::addRenderableWidget);
        this.panelWidgets.add(widget);
        widget.setContext(new MenuWidgetContext(this.menu, this::onWidgetResize));
    }

    private void onWidgetResize(PanelWidget widget) {
        List<PanelWidget> widgetsToResize = this.panelWidgets.stream().filter(widget1 -> widget1.getY() > widget.getY()).toList();
        for (PanelWidget widget1 : widgetsToResize) {
            if (!widget.isOpen()){
                widget1.setY(widget1.getOriginalY());
            } else {
                widget1.setY(widget.getY() + widget.getOpenHeight() + 2);
            }
            widget1.onWidgetResized(widget);
        }
    }

    public List<Rect2i> getBounds() {
        return this.panelWidgets.stream().map(PanelWidget::getBounds).toList();
    }

}