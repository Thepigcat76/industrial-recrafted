package com.portingdeadmods.indrec.content.menus;

import com.portingdeadmods.indrec.api.menus.MachineMenu;
import com.portingdeadmods.indrec.content.blockentities.SolarPanelBlockEntity;
import com.portingdeadmods.indrec.registries.IRMachines;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.items.ItemHandlerCopySlot;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class SolarPanelMenu extends MachineMenu<SolarPanelBlockEntity> {
    public SolarPanelMenu(int containerId, @NotNull Inventory inv, @NotNull SolarPanelBlockEntity blockEntity) {
        super(IRMachines.BASIC_SOLAR_PANEL.getMenuType(), containerId, inv, blockEntity);

        addSlot(new ChargingSlot(blockEntity.getItemHandler(), 0, ChargingSlot.ChargeMode.CHARGE, (176 - 16) / 2, 45));

        addPlayerInventory(inv);
        addPlayerHotbar(inv);
    }

    public SolarPanelMenu(int containerId, @NotNull Inventory inv, @NotNull FriendlyByteBuf byteBuf) {
        this(containerId, inv, (SolarPanelBlockEntity) inv.player.level().getBlockEntity(byteBuf.readBlockPos()));
    }
}
