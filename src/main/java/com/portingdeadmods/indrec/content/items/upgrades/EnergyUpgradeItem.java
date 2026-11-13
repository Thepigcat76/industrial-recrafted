package com.portingdeadmods.indrec.content.items.upgrades;

import com.portingdeadmods.indrec.IRConfig;
import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;

public class EnergyUpgradeItem extends MachineUpgradeItem {
    public EnergyUpgradeItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void applyUpgrade(MachineBlockEntity machine, int count) {
        machine.setEnergyCapacity(machine.getBaseEnergyCapacity() + count * IRConfig.energyUpgradeCapacity);
    }
}
