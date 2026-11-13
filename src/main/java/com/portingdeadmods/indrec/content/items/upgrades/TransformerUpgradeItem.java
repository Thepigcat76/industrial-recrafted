package com.portingdeadmods.indrec.content.items.upgrades;

import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;

public class TransformerUpgradeItem extends MachineUpgradeItem {
    public TransformerUpgradeItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getMaxUpgradeCount() {
        return 1;
    }

    @Override
    protected void applyUpgrade(MachineBlockEntity machine, int count) {
        machine.setEnergyTier(machine.getBaseEnergyTier().raiseTier(count));
    }
}
