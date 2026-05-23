package com.portingdeadmods.indrec.utils;

import com.portingdeadmods.indrec.IRConfig;

public final class EnergyHelper {
    public static int convertEuToFe(int euEnergy) {
        return (int) (euEnergy * IRConfig.energyConversionFactor);
    }
}
