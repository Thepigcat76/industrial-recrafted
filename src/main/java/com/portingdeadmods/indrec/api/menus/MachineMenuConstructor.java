package com.portingdeadmods.indrec.api.menus;

import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

/**
 * Creates the server-side menu of a machine for its block entity
 */
@FunctionalInterface
public interface MachineMenuConstructor<BE extends MachineBlockEntity> {
    AbstractContainerMenu create(int containerId, Inventory inventory, BE blockEntity);
}
