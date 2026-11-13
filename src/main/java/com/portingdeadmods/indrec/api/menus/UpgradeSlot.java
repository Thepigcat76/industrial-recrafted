package com.portingdeadmods.indrec.api.menus;

import com.portingdeadmods.indrec.api.blockentities.UpgradeBlockEntity;
import com.portingdeadmods.indrec.api.items.UpgradeItem;
import net.minecraft.world.item.ItemStack;

public class UpgradeSlot extends IRSlotItemHandler {
    private final UpgradeBlockEntity blockEntity;

    public UpgradeSlot(UpgradeBlockEntity blockEntity, int index, int xPosition, int yPosition) {
        super(blockEntity.getUpgradeItemHandler(), index, xPosition, yPosition);
        this.blockEntity = blockEntity;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        int maxStackSize = super.getMaxStackSize(stack);
        return stack.getItem() instanceof UpgradeItem upgrade ? Math.min(maxStackSize, this.blockEntity.getUpgradeMaxCount(upgrade)) : maxStackSize;
    }
}
