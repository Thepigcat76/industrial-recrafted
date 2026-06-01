package com.portingdeadmods.indrec;

import com.portingdeadmods.indrec.api.crops.Crop;
import com.portingdeadmods.indrec.api.energy.EnergyTier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegistryBuilder;

public final class IRRegistries {
    public static final Registry<EnergyTier> ENERGY_TIER =
            new RegistryBuilder<>(ResourceKey.<EnergyTier>createRegistryKey(IndustrialRecrafted.rl("energy_tier"))).create();

    public static final ResourceKey<Registry<Crop>> CROP = ResourceKey.createRegistryKey(IndustrialRecrafted.rl("crop"));
}