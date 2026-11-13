package com.portingdeadmods.indrec.content.items.upgrades;

import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;
import com.portingdeadmods.indrec.api.blockentities.UpgradeBlockEntity;
import com.portingdeadmods.indrec.api.items.UpgradeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class MachineUpgradeItem extends Item implements UpgradeItem {
    public MachineUpgradeItem(Properties properties) {
        super(properties);
    }

    protected abstract void applyUpgrade(MachineBlockEntity machine, int count);

    @Override
    public <BE extends BlockEntity & UpgradeBlockEntity> void onUpgradeAdded(BE blockEntity) {
        this.updateMachine(blockEntity);
    }

    @Override
    public <BE extends BlockEntity & UpgradeBlockEntity> void onUpgradeRemoved(BE blockEntity) {
        this.updateMachine(blockEntity);
    }

    @Override
    public <BE extends BlockEntity & UpgradeBlockEntity> void tickUpgrade(BE blockEntity) {
    }

    private <BE extends BlockEntity & UpgradeBlockEntity> void updateMachine(BE blockEntity) {
        if (blockEntity instanceof MachineBlockEntity machine) {
            this.applyUpgrade(machine, blockEntity.getUpgradeCount(this));
        }
    }
}
