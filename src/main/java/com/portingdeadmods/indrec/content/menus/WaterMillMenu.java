package com.portingdeadmods.indrec.content.menus;

import com.portingdeadmods.indrec.api.menus.MachineMenu;
import com.portingdeadmods.indrec.content.blockentities.WaterMillBlockEntity;
import com.portingdeadmods.indrec.registries.IRMachines;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class WaterMillMenu extends MachineMenu<WaterMillBlockEntity> {
    public WaterMillMenu(int containerId, @NotNull Inventory inv, @NotNull WaterMillBlockEntity blockEntity) {
        super(IRMachines.WATER_MILL.getMenuType(), containerId, inv, blockEntity);
    }

    public WaterMillMenu(int containerId, @NotNull Inventory inv, @NotNull FriendlyByteBuf byteBuf) {
        this(containerId, inv, (WaterMillBlockEntity) inv.player.level().getBlockEntity(byteBuf.readBlockPos()));
    }

}
