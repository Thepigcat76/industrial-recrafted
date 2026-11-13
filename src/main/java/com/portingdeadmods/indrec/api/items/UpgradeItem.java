package com.portingdeadmods.indrec.api.items;

import com.portingdeadmods.indrec.api.blockentities.UpgradeBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface UpgradeItem {
    // Return -1 for no maximum upgrade count
    default int getMaxUpgradeCount() {
        return -1;
    }

    <BE extends BlockEntity & UpgradeBlockEntity> void onUpgradeAdded(BE blockEntity);

    <BE extends BlockEntity & UpgradeBlockEntity> void onUpgradeRemoved(BE blockEntity);

    <BE extends BlockEntity & UpgradeBlockEntity> void tickUpgrade(BE blockEntity);
}
