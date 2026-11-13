package com.portingdeadmods.indrec.api.blockentities;

import com.portingdeadmods.indrec.api.items.UpgradeItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

public interface UpgradeBlockEntity {
    int DEFAULT_MAX_UPGRADES = 4;
    int DEFAULT_UPGRADE_SLOTS = 4;

    boolean supportsUpgrades();

    boolean supportsUpgrade(UpgradeItem upgrade);

    default int getUpgradeMaxCount(UpgradeItem upgrade) {
        int maxUpgradeCount = upgrade.getMaxUpgradeCount();
        return maxUpgradeCount == -1 ? DEFAULT_MAX_UPGRADES : maxUpgradeCount;
    }

    int getUpgradeSlots(UpgradeItem upgrade);

    IItemHandler getUpgradeItemHandler();

    /**
     * Inserts a single upgrade from the stack, preferring slots that already hold that upgrade.
     * Respects the upgrade's limit per slot like the upgrade slots in the menu do. Doesn't shrink the stack.
     *
     * @return whether the upgrade was (or, when simulating, could be) inserted
     */
    default boolean insertUpgrade(ItemStack stack, boolean simulate) {
        if (!this.supportsUpgrades() || !(stack.getItem() instanceof UpgradeItem upgrade)) return false;

        IItemHandler handler = this.getUpgradeItemHandler();
        int maxCount = this.getUpgradeMaxCount(upgrade);
        ItemStack single = stack.copyWithCount(1);
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack slotStack = handler.getStackInSlot(i);
            if (!slotStack.isEmpty() && slotStack.getCount() < maxCount && handler.insertItem(i, single, simulate).isEmpty()) {
                return true;
            }
        }
        for (int i = 0; i < handler.getSlots(); i++) {
            if (handler.getStackInSlot(i).isEmpty() && handler.insertItem(i, single, simulate).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    default boolean hasUpgrade(UpgradeItem upgrade) {
        for (int i = 0; i < this.getUpgradeItemHandler().getSlots(); i++) {
            ItemStack stack = this.getUpgradeItemHandler().getStackInSlot(i);
            if (stack.getItem() == upgrade) {
                return true;
            }
        }
        return false;
    }

    default int getUpgradeCount(UpgradeItem upgrade) {
        int count = 0;
        for (int i = 0; i < this.getUpgradeItemHandler().getSlots(); i++) {
            ItemStack stack = this.getUpgradeItemHandler().getStackInSlot(i);
            if (stack.getItem() == upgrade) {
                count += stack.getCount();
            }
        }
        return count;
    }
}
