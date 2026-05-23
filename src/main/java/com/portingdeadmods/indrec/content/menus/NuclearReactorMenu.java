package com.portingdeadmods.indrec.content.menus;

import com.portingdeadmods.indrec.api.menus.MachineMenu;
import com.portingdeadmods.indrec.content.blockentities.NuclearReactorBlockEntity;
import com.portingdeadmods.indrec.registries.IRMachines;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.NotNull;

public class NuclearReactorMenu extends MachineMenu<NuclearReactorBlockEntity> {
    public NuclearReactorMenu(int containerId, @NotNull Inventory inv, @NotNull FriendlyByteBuf byteBuf) {
        super(IRMachines.NUCLEAR_REACTOR.getMenuType(), containerId, inv, (NuclearReactorBlockEntity) inv.player.level().getBlockEntity(byteBuf.readBlockPos()));
    }

    public NuclearReactorMenu(int containerId, @NotNull Inventory inv, @NotNull NuclearReactorBlockEntity blockEntity) {
        super(IRMachines.NUCLEAR_REACTOR.getMenuType(), containerId, inv, blockEntity);
    }
}
