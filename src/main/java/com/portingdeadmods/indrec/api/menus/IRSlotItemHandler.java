package com.portingdeadmods.indrec.api.menus;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class IRSlotItemHandler extends SlotItemHandler {
    private boolean active = true;

    public IRSlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    // Shift clicking doesn't check whether a slot is active, and items shouldn't move into a hidden slot
    @Override
    public boolean mayPlace(ItemStack stack) {
        return this.active && super.mayPlace(stack);
    }

}
