package com.portingdeadmods.indrec.content.items.upgrades;

import com.portingdeadmods.indrec.IRConfig;
import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;

public class OverclockerUpgradeItem extends MachineUpgradeItem {
    public OverclockerUpgradeItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void applyUpgrade(MachineBlockEntity machine, int count) {
        machine.setProgressIncrement((float) (1 / Math.pow(IRConfig.overclockerUpgradeTimeMultiplier, count)));
        machine.setEnergyUsageMultiplier((float) Math.pow(IRConfig.overclockerUpgradeEnergyMultiplier, count));
    }
}
