package com.portingdeadmods.indrec.content.menus;

import com.portingdeadmods.indrec.api.menus.MachineMenu;
import com.portingdeadmods.indrec.content.blockentities.BasicGeneratorBlockEntity;
import com.portingdeadmods.indrec.registries.IRMachines;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class BasicGeneratorMenu extends MachineMenu<BasicGeneratorBlockEntity> {
    public BasicGeneratorMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, (BasicGeneratorBlockEntity) inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public BasicGeneratorMenu(int pContainerId, Inventory inv, BasicGeneratorBlockEntity entity) {
        super(IRMachines.BASIC_GENERATOR.getMenuType(), pContainerId, inv, entity);
        checkContainerSize(inv, 2);

        IItemHandler itemHandler = entity.getItemHandler();

        this.addSlot(new SlotItemHandler(itemHandler, 0, 80, 54));
        this.addSlot(new ChargingSlot(itemHandler, 1, ChargingSlot.ChargeMode.CHARGE, 9, 68));

        addPlayerInventory(inv, 83 + 21);
        addPlayerHotbar(inv, 141 + 21);
    }

    @Override
    protected int getMergeableSlotCount() {
        return 2;
    }
}
