package com.portingdeadmods.indrec.api.menus;

import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;
import com.portingdeadmods.indrec.api.blockentities.UpgradeBlockEntity;
import com.portingdeadmods.indrec.api.items.UpgradeItem;
import com.portingdeadmods.indrec.content.menus.ChargingSlot;
import com.portingdeadmods.portingdeadlibs.api.gui.menus.PDLAbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MachineMenu<BE extends MachineBlockEntity> extends PDLAbstractContainerMenu<BE> implements UpgradeMenu {
    private final List<IRSlotItemHandler> upgradeSlots;

    public MachineMenu(MenuType<?> menuType, int containerId, @NotNull Inventory inv, @NotNull BE blockEntity) {
        super(menuType, containerId, inv, blockEntity);
        this.upgradeSlots = new ArrayList<>();
        if (blockEntity instanceof UpgradeBlockEntity upgradeBlockEntity && upgradeBlockEntity.supportsUpgrades()) {
            for (int i = 0; i < upgradeBlockEntity.getUpgradeItemHandler().getSlots(); i++) {
                IRSlotItemHandler slot = new UpgradeSlot(upgradeBlockEntity, i, 179, 51 + i * 20);
                slot.setActive(false);
                this.addSlot(slot);
                this.upgradeSlots.add(slot);
            }
        }
    }

    @Override
    public List<IRSlotItemHandler> getUpgradeSlots() {
        return upgradeSlots;
    }

    public int getProgress() {
        return this.blockEntity.getProgress();
    }

    public int getMaxProgress() {
        return this.blockEntity.getMaxProgress();
    }

    @Override
    protected @NotNull Slot addSlot(@NotNull Slot slot) {
        if (slot instanceof ChargingSlot chargingSlot && this.blockEntity instanceof MachineBlockEntity machineBlockEntity) {
            machineBlockEntity.addChargingSlot(chargingSlot);
        }
        return super.addSlot(slot);
    }

    @Override
    protected boolean moveItemStackTo(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        if (startIndex >= this.upgradeSlots.size() || !(stack.getItem() instanceof UpgradeItem)) {
            return super.moveItemStackTo(stack, startIndex, endIndex, reverseDirection);
        }

        // Upgrades only go into upgrade slots when shift clicked. This also respects the slots' limits
        // that depend on the item, which PDL's merging ignores
        int countBefore = stack.getCount();
        for (IRSlotItemHandler upgradeSlot : this.upgradeSlots) {
            upgradeSlot.safeInsert(stack);
        }
        return stack.getCount() != countBefore;
    }

    @Override
    protected int getMergeableSlotCount() {
        // Upgrade slots are added before the machine's own slots
        return 3 + this.upgradeSlots.size();
    }

}
