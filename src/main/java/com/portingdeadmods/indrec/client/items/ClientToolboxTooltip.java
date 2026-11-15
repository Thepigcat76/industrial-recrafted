package com.portingdeadmods.indrec.client.items;

import com.portingdeadmods.indrec.content.items.ToolboxItem;
import com.portingdeadmods.indrec.data.components.ToolboxContents;
import com.portingdeadmods.indrec.networking.serverbound.SelectToolboxToolPayload;
import com.portingdeadmods.indrec.registries.IRDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * Renders the tools of a toolbox in a bundle-like grid, highlighting the selected tool.
 * Also handles scrolling through the tools while hovering a toolbox.
 */
public class ClientToolboxTooltip implements ClientTooltipComponent {
    private static final ResourceLocation BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/background");
    private static final ResourceLocation SLOT_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/slot");
    private static final int COLUMNS = 4;
    private static final int ROWS = ToolboxContents.MAX_TOOLS / COLUMNS;
    private static final int SLOT_WIDTH = 18;
    private static final int SLOT_HEIGHT = 20;

    private final ToolboxContents contents;

    public ClientToolboxTooltip(ToolboxItem.Tooltip tooltip) {
        this.contents = tooltip.contents();
    }

    @Override
    public int getHeight() {
        return ROWS * SLOT_HEIGHT + 2 + 4;
    }

    @Override
    public int getWidth(Font font) {
        return COLUMNS * SLOT_WIDTH + 2;
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        guiGraphics.blitSprite(BACKGROUND_SPRITE, x, y, this.getWidth(font), ROWS * SLOT_HEIGHT + 2);
        for (int i = 0; i < ToolboxContents.MAX_TOOLS; i++) {
            int slotX = x + (i % COLUMNS) * SLOT_WIDTH + 1;
            int slotY = y + (i / COLUMNS) * SLOT_HEIGHT + 1;
            guiGraphics.blitSprite(SLOT_SPRITE, slotX, slotY, 0, SLOT_WIDTH, SLOT_HEIGHT);
            ItemStack stack = this.contents.getItemUnsafe(i);
            if (!stack.isEmpty()) {
                guiGraphics.renderItem(stack, slotX + 1, slotY + 1, i);
                guiGraphics.renderItemDecorations(font, stack, slotX + 1, slotY + 1);
                if (i == this.contents.selected()) {
                    AbstractContainerScreen.renderSlotHighlight(guiGraphics, slotX + 1, slotY + 1, 0);
                }
            } else if (!this.contents.getFilterUnsafe(i).isEmpty()) {
                renderGhostItem(guiGraphics, this.contents.getFilterUnsafe(i), slotX + 1, slotY + 1);
            }
        }
    }

    /**
     * Renders an item faded out, used to display the filter of a slot
     */
    public static void renderGhostItem(GuiGraphics guiGraphics, ItemStack stack, int x, int y) {
        guiGraphics.renderFakeItem(stack, x, y);
        // guiOverlay has no depth test and doesn't write depth, so the overlay covers the item
        // without hiding items rendered afterwards, like ingredients dragged from JEI
        guiGraphics.fill(RenderType.guiOverlay(), x, y, x + 16, y + 16, 0x808B8B8B);
        // Draw it now, otherwise the batched overlay could be drawn on top of things rendered later
        guiGraphics.flush();
    }

    public static void onMouseScroll(ScreenEvent.MouseScrolled.Pre event) {
        if (!(event.getScreen() instanceof AbstractContainerScreen<?> screen) || event.getScrollDeltaY() == 0) return;
        Slot slot = screen.getSlotUnderMouse();
        if (slot == null) return;

        ItemStack stack = slot.getItem();
        if (!(stack.getItem() instanceof ToolboxItem)) return;
        ToolboxContents contents = ToolboxItem.getContents(stack);
        if (contents.count() < 2) return;

        ToolboxContents newContents = contents.cycleSelected(event.getScrollDeltaY() < 0 ? 1 : -1);
        // Update the client stack directly so the tooltip updates immediately, the server syncs the same value back
        stack.set(IRDataComponents.TOOLBOX_CONTENTS, newContents);
        // Slots of the player's inventory are addressed by their inventory index, as the creative screen's slots
        // don't match the server's menu
        boolean playerInventory = slot.container instanceof Inventory inventory && inventory.player == Minecraft.getInstance().player;
        int index = playerInventory ? slot.getContainerSlot() : slot.index;
        PacketDistributor.sendToServer(new SelectToolboxToolPayload(index, playerInventory, newContents.selected()));
        event.setCanceled(true);
    }
}
