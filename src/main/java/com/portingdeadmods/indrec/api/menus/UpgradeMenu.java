package com.portingdeadmods.indrec.api.menus;

import org.jetbrains.annotations.ApiStatus;

public interface UpgradeMenu {
    Iterable<IRSlotItemHandler> getUpgradeSlots();

    @ApiStatus.Internal
    default void internalSetUpgradeSlotPositions(int startY) {
        int i = 0;
        for (IRSlotItemHandler slot : this.getUpgradeSlots()) {
            ((SlotAccessor)slot).setY(startY + i * 20);
            i++;
        }
    }
}
