package com.portingdeadmods.indrec.content.menus;

import com.portingdeadmods.indrec.api.menus.MachineMenu;
import com.portingdeadmods.indrec.content.blockentities.WindMillBlockEntity;
import com.portingdeadmods.indrec.registries.IRMachines;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class WindMillMenu extends MachineMenu<WindMillBlockEntity> {
    public WindMillMenu(int containerId, @NotNull Inventory inv, @NotNull WindMillBlockEntity blockEntity) {
        super(IRMachines.WIND_MILL.getMenuType(), containerId, inv, blockEntity);
    }

    public WindMillMenu(int containerId, @NotNull Inventory inv, @NotNull FriendlyByteBuf byteBuf) {
        this(containerId, inv, (WindMillBlockEntity) inv.player.level().getBlockEntity(byteBuf.readBlockPos()));
    }
}
