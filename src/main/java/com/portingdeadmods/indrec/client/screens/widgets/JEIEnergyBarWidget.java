package com.portingdeadmods.indrec.client.screens.widgets;

import com.portingdeadmods.portingdeadlibs.api.capabilities.EnergyStorageWrapper;
import com.portingdeadmods.portingdeadlibs.client.screens.widgets.EnergyBarWidget;
import net.minecraft.client.gui.GuiGraphics;

public class JEIEnergyBarWidget extends EnergyBarWidget {
    public JEIEnergyBarWidget(int x, int y, EnergyStorageWrapper wrapper, String energyUnit, boolean hasBorder) {
        super(x, y, wrapper, energyUnit, hasBorder);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.isHovered = false;
        super.renderWidget(guiGraphics, mouseX, mouseY, delta);

    }

}
