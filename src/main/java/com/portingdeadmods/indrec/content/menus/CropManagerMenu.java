package com.portingdeadmods.indrec.content.menus;

import com.portingdeadmods.indrec.api.menus.MachineMenu;
import com.portingdeadmods.indrec.content.blockentities.CropManagerBlockEntity;
import com.portingdeadmods.indrec.registries.IRMachines;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class CropManagerMenu extends MachineMenu<CropManagerBlockEntity> {
    public CropManagerMenu(int containerId, @NotNull Inventory inv, @NotNull CropManagerBlockEntity blockEntity) {
        super(IRMachines.CROP_MANAGER.getMenuType(), containerId, inv, blockEntity);

        IItemHandler itemHandler = blockEntity.getItemHandler();

        // 3x3 grid, like the dropper
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                addSlot(new SlotItemHandler(itemHandler, row * 3 + col, 44 + col * 18, 26 + row * 18));
            }
        }

        // 2 stacked slots right of the center: hoes (charged if electric) and bone meal
        addSlot(new ChargingSlot(itemHandler, CropManagerBlockEntity.HOE_SLOT, ChargingSlot.ChargeMode.CHARGE, 116, 35));
        addSlot(new SlotItemHandler(itemHandler, CropManagerBlockEntity.BONEMEAL_SLOT, 116, 53));

        addSlot(new ChargingSlot(itemHandler, CropManagerBlockEntity.BATTERY_SLOT, ChargingSlot.ChargeMode.DECHARGE, 9, 68));

        addPlayerInventory(inv, 83 + 21);
        addPlayerHotbar(inv, 141 + 21);
    }

    @Override
    protected int getMergeableSlotCount() {
        return CropManagerBlockEntity.BATTERY_SLOT + 1;
    }

    public CropManagerMenu(int containerId, @NotNull Inventory inv, @NotNull FriendlyByteBuf byteBuf) {
        this(containerId, inv, (CropManagerBlockEntity) inv.player.level().getBlockEntity(byteBuf.readBlockPos()));
    }
}
