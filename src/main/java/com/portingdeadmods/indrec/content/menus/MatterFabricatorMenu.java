package com.portingdeadmods.indrec.content.menus;

import com.portingdeadmods.indrec.api.menus.MachineMenu;
import com.portingdeadmods.indrec.content.blockentities.MatterFabricatorBlockEntity;
import com.portingdeadmods.indrec.registries.IRMachines;
import com.portingdeadmods.indrec.registries.IRMenuTypes;
import com.portingdeadmods.indrec.utils.machines.IRMachine;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.NotNull;

public class MatterFabricatorMenu extends MachineMenu<MatterFabricatorBlockEntity> {
    public MatterFabricatorMenu(int containerId, @NotNull Inventory inv, @NotNull MatterFabricatorBlockEntity blockEntity) {
        super(IRMachines.MATTER_FABRICATOR.getMenuType(), containerId, inv, blockEntity);

        addPlayerInventory(inv, 83 + 21);
        addPlayerHotbar(inv, 141 + 21);
    }

    public MatterFabricatorMenu(int containerId, @NotNull Inventory inv, @NotNull FriendlyByteBuf byteBuf) {
        this(containerId, inv, (MatterFabricatorBlockEntity) inv.player.level().getBlockEntity(byteBuf.readBlockPos()));
    }
}
