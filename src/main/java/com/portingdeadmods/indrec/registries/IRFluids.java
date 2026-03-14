package com.portingdeadmods.indrec.registries;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.content.fluids.BioFuelFluid;
import com.portingdeadmods.portingdeadlibs.utils.FluidRegistrationHelper;

public final class IRFluids {
    public static final FluidRegistrationHelper HELPER = new FluidRegistrationHelper(IRBlocks.BLOCKS, IRItems.ITEMS, IndustrialRecrafted.MODID);

    public static final BioFuelFluid BIO_FUEL = HELPER.registerFluid(new BioFuelFluid("bio_fuel"));
}
