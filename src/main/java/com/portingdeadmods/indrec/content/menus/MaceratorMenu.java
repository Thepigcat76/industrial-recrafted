package com.portingdeadmods.indrec.content.menus;

import com.portingdeadmods.indrec.api.menus.MachineMenu;
import com.portingdeadmods.indrec.content.blockentities.MaceratorBlockEntity;
import com.portingdeadmods.indrec.registries.IRMachines;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class MaceratorMenu extends MachineMenu<MaceratorBlockEntity> {
    public MaceratorMenu(int containerId, @NotNull Inventory inv, @NotNull MaceratorBlockEntity blockEntity) {
        super(IRMachines.MACERATOR.getMenuType(), containerId, inv, blockEntity);
        checkContainerSize(inv, 4);

        IItemHandler itemHandler = blockEntity.getItemHandler();

        this.addSlot(new SlotItemHandler(itemHandler, 0, 53, 45));
        this.addSlot(new SlotItemHandler(itemHandler, 1, 107, 45));
        this.addSlot(new SlotItemHandler(itemHandler, 2, 107, 66));
        this.addSlot(new ChargingSlot(itemHandler, 3, ChargingSlot.ChargeMode.DECHARGE, 9, 68));

        addPlayerInventory(inv, 83 + 21);
        addPlayerHotbar(inv, 141 + 21);
    }

    public MaceratorMenu(int containerId, @NotNull Inventory inv, @NotNull FriendlyByteBuf byteBuf) {
        this(containerId, inv, (MaceratorBlockEntity) inv.player.level().getBlockEntity(byteBuf.readBlockPos()));
    }

}
