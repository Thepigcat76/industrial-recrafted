package com.portingdeadmods.indrec.content.menus;

import com.portingdeadmods.indrec.api.menus.MachineMenu;
import com.portingdeadmods.indrec.content.blockentities.ElectricFurnaceBlockEntity;
import com.portingdeadmods.indrec.registries.IRMachines;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class ElectricFurnaceMenu extends MachineMenu<ElectricFurnaceBlockEntity> {
    public ElectricFurnaceMenu(int containerId, @NotNull Inventory inv, @NotNull ElectricFurnaceBlockEntity blockEntity) {
        super(IRMachines.ELECTRIC_FURNACE.getMenuType(), containerId, inv, blockEntity);
        checkContainerSize(inv, 2);

        IItemHandler itemHandler = blockEntity.getItemHandler();

        this.addSlot(new SlotItemHandler(itemHandler, 0, 53, 45));
        this.addSlot(new SlotItemHandler(itemHandler, 1, 107, 45));
        this.addSlot(new ChargingSlot(itemHandler, 2, ChargingSlot.ChargeMode.DECHARGE, 9, 68));

        addPlayerInventory(inv, 83 + 21);
        addPlayerHotbar(inv, 141 + 21);
    }

    public ElectricFurnaceMenu(int containerId, @NotNull Inventory inv, @NotNull FriendlyByteBuf bytebuf) {
        this(containerId, inv, (ElectricFurnaceBlockEntity) inv.player.level().getBlockEntity(bytebuf.readBlockPos()));
    }

}
