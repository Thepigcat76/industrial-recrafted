package com.portingdeadmods.indrec.utils;

import com.portingdeadmods.indrec.IRConfig;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public final class EnergyHelper {
    public static int convertEuToFe(int euEnergy) {
        return (int) (euEnergy * IRConfig.energyConversionFactor);
    }

    private static final DecimalFormat DF;

    public static String formatEnergy(int energy) {
        return DF.format(energy);
    }

    static {
        DecimalFormatSymbols sym = new DecimalFormatSymbols(Locale.ROOT);
        sym.setGroupingSeparator(',');

        DF = new DecimalFormat("#,###", sym);
        DF.setGroupingSize(3);
        DF.setGroupingUsed(true);
    }

}
