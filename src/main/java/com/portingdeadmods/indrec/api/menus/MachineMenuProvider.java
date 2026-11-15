package com.portingdeadmods.indrec.api.menus;

import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;
import com.portingdeadmods.indrec.utils.machines.IRMachine;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Opens the menu of a machine, using the menu constructor and title registered with its {@link IRMachine}
 */
public record MachineMenuProvider(IRMachine machine, MachineBlockEntity blockEntity) implements MenuProvider {
    @Override
    public @NotNull Component getDisplayName() {
        return this.machine.getMenuTitle();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
        // Make sure the screen shows the current progress right away
        this.blockEntity.syncProgress();
        return this.machine.createMenu(containerId, inventory, this.blockEntity);
    }
}
