package com.portingdeadmods.indrec.compat.jei;

import com.portingdeadmods.indrec.api.client.screens.MachineScreen;
import com.portingdeadmods.portingdeadlibs.api.client.screens.PDLAbstractContainerScreen;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.renderer.Rect2i;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WidgetBounds implements IGuiContainerHandler<PDLAbstractContainerScreen<?>> {
    @Override
    public @NotNull List<Rect2i> getGuiExtraAreas(PDLAbstractContainerScreen<?> containerScreen) {
        if (containerScreen instanceof MachineScreen<?> screen) {
            return screen.getBounds();
        }
        return IGuiContainerHandler.super.getGuiExtraAreas(containerScreen);
    }
}