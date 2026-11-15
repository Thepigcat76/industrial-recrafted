package com.portingdeadmods.indrec.client.screens;

import com.portingdeadmods.indrec.client.items.ClientToolboxTooltip;
import com.portingdeadmods.indrec.content.menus.ToolboxFilterMenu;
import com.portingdeadmods.indrec.data.components.ToolboxContents;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ToolboxFilterScreen extends AbstractContainerScreen<ToolboxFilterMenu> {
    private static final ResourceLocation CONTAINER_BACKGROUND = ResourceLocation.withDefaultNamespace("textures/gui/container/generic_54.png");
    private static final int BACKGROUND_COLOR = 0xFFC6C6C6;

    public ToolboxFilterScreen(ToolboxFilterMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = ToolboxFilterMenu.IMAGE_HEIGHT;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;
        guiGraphics.blit(CONTAINER_BACKGROUND, x, y, 0, 0, this.imageWidth, 17);
        guiGraphics.blit(CONTAINER_BACKGROUND, x, y + 17, 0, 17, this.imageWidth, 18);
        guiGraphics.fill(x + 7, y + 17, x + this.imageWidth - 7, y + 35, BACKGROUND_COLOR);
        for (int i = 0; i < ToolboxContents.MAX_TOOLS; i++) {
            guiGraphics.blit(CONTAINER_BACKGROUND, x + ToolboxFilterMenu.FILTER_SLOTS_X - 1 + i * 18, y + ToolboxFilterMenu.FILTER_SLOTS_Y - 1, 7, 17, 18, 18);
        }
        guiGraphics.blit(CONTAINER_BACKGROUND, x, y + 35, 0, 126, this.imageWidth, 96);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        if (this.hoveredSlot instanceof ToolboxFilterMenu.FilterSlot) return;
        super.renderTooltip(guiGraphics, x, y);
    }

    @Override
    protected void renderSlot(GuiGraphics guiGraphics, Slot slot) {
        if (slot instanceof ToolboxFilterMenu.FilterSlot) {
            ItemStack filter = slot.getItem();
            if (!filter.isEmpty()) {
                ClientToolboxTooltip.renderGhostItem(guiGraphics, filter, slot.x, slot.y);
            }
            return;
        }
        super.renderSlot(guiGraphics, slot);
    }
}
