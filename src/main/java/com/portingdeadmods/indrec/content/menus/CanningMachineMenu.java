package com.portingdeadmods.indrec.content.menus;

import com.portingdeadmods.indrec.api.menus.MachineMenu;
import com.portingdeadmods.indrec.content.blockentities.CanningMachineBlockEntity;
import com.portingdeadmods.indrec.registries.IRMachines;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class CanningMachineMenu extends MachineMenu<CanningMachineBlockEntity> {
    public CanningMachineMenu(int containerId, @NotNull Inventory inv, @NotNull CanningMachineBlockEntity blockEntity) {
        super(IRMachines.CANNING_MACHINE.getMenuType(), containerId, inv, blockEntity);
        checkContainerSize(inv, 4);

        IItemHandler itemHandler = blockEntity.getItemHandler();

        this.addSlot(new SlotItemHandler(itemHandler, 0, 35, 45));
        this.addSlot(new SlotItemHandler(itemHandler, 1, 53, 45));
        this.addSlot(new SlotItemHandler(itemHandler, 2, 107, 45));
        this.addSlot(new ChargingSlot(itemHandler, 3, ChargingSlot.ChargeMode.DECHARGE, 9, 68));

        addPlayerInventory(inv, 83 + 21);
        addPlayerHotbar(inv, 141 + 21);
    }

    public CanningMachineMenu(int containerId, @NotNull Inventory inv, FriendlyByteBuf byteBuf) {
        this(containerId, inv, (CanningMachineBlockEntity) inv.player.level().getBlockEntity(byteBuf.readBlockPos()));
    }

}
