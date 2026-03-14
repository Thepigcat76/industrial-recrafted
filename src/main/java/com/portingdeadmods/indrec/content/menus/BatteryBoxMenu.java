package com.portingdeadmods.indrec.content.menus;

import com.portingdeadmods.indrec.api.menus.MachineMenu;
import com.portingdeadmods.indrec.content.blockentities.EnergyStorageBlockEntity;
import com.portingdeadmods.indrec.registries.IRMachines;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class BatteryBoxMenu extends MachineMenu<EnergyStorageBlockEntity> {
    public BatteryBoxMenu(int containerId, @NotNull Inventory inv, @NotNull EnergyStorageBlockEntity blockEntity) {
        super(IRMachines.BATTERY_BOX.getMenuType(), containerId, inv, blockEntity);

        IItemHandler itemHandler = blockEntity.getItemHandler();
        addSlot(new ChargingSlot(itemHandler, 0, ChargingSlot.ChargeMode.DECHARGE, 39, 43));
        addSlot(new ChargingSlot(itemHandler, 1, ChargingSlot.ChargeMode.CHARGE, 176 - 36 - 19, 43));
        addPlayerInventory(inv, 83 + 21);
        addPlayerHotbar(inv, 141 + 21);

    }

    public BatteryBoxMenu(int containerId, @NotNull Inventory inv, RegistryFriendlyByteBuf byteBuf) {
        this(containerId, inv, (EnergyStorageBlockEntity) inv.player.level().getBlockEntity(byteBuf.readBlockPos()));
    }

    @Override
    protected int getMergeableSlotCount() {
        return 2;
    }
}