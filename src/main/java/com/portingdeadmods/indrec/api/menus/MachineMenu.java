package com.portingdeadmods.indrec.api.menus;

import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;
import com.portingdeadmods.indrec.content.menus.ChargingSlot;
import com.portingdeadmods.portingdeadlibs.api.gui.menus.PDLAbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;

public class MachineMenu<BE extends MachineBlockEntity> extends PDLAbstractContainerMenu<BE> {
    public MachineMenu(MenuType<?> menuType, int containerId, @NotNull Inventory inv, @NotNull BE blockEntity) {
        super(menuType, containerId, inv, blockEntity);
    }

    @Override
    protected @NotNull Slot addSlot(@NotNull Slot slot) {
        if (slot instanceof ChargingSlot chargingSlot && this.blockEntity instanceof MachineBlockEntity machineBlockEntity) {
            machineBlockEntity.addChargingSlot(chargingSlot);
        }
        return super.addSlot(slot);
    }

    @Override
    protected int getMergeableSlotCount() {
        return 3;
    }
}
