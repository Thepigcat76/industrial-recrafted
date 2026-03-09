package com.portingdeadmods.indrec.content.menus;

import com.portingdeadmods.indrec.api.menus.MachineMenu;
import com.portingdeadmods.indrec.content.blockentities.GeothermalGeneratorBlockEntity;
import com.portingdeadmods.indrec.registries.IRMachines;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class GeothermalGeneratorMenu extends MachineMenu<GeothermalGeneratorBlockEntity> {
    public GeothermalGeneratorMenu(int containerId, @NotNull Inventory inv, @NotNull GeothermalGeneratorBlockEntity blockEntity) {
        super(IRMachines.GEOTHERMAL_GENERATOR.getMenuType(), containerId, inv, blockEntity);
        // do stuff
        IItemHandler handler = blockEntity.getItemHandler();
        this.addSlot(new SlotItemHandler(handler, 0, 70, 20));
        this.addSlot(new SlotItemHandler(handler, 1, 70, 64));
        this.addSlot(new ChargingSlot(handler, 2, ChargingSlot.ChargeMode.CHARGE, 9, 68));

        addPlayerInventory(inv, 83 + 21);
        addPlayerHotbar(inv, 141 + 21);
    }

    public GeothermalGeneratorMenu(int containerId, @NotNull Inventory inv, FriendlyByteBuf byteBuf) {
        this(containerId, inv, (GeothermalGeneratorBlockEntity) inv.player.level().getBlockEntity(byteBuf.readBlockPos()));
    }

}
