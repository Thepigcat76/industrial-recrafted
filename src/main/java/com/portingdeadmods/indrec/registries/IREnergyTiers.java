package com.portingdeadmods.indrec.registries;

import com.portingdeadmods.indrec.IRRegistries;
import com.portingdeadmods.indrec.IndustrialReclassified;
import com.portingdeadmods.indrec.api.energy.EnergyTier;
import com.portingdeadmods.indrec.impl.energy.EnergyTierImpl;
import net.minecraft.ChatFormatting;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class IREnergyTiers {
    public static final DeferredRegister<EnergyTier> ENERGY_TIERS = DeferredRegister.create(IRRegistries.ENERGY_TIER, IndustrialReclassified.MODID);

    public static final Supplier<EnergyTierImpl> NONE = ENERGY_TIERS.register("none", () -> new EnergyTierImpl(0, 0, ChatFormatting.GRAY.getColor(), 0));
    public static final Supplier<EnergyTierImpl> LOW = ENERGY_TIERS.register("low", () -> new EnergyTierImpl(32, 4_000, ChatFormatting.WHITE.getColor(), 1));
    public static final Supplier<EnergyTierImpl> MEDIUM = ENERGY_TIERS.register("medium", () -> new EnergyTierImpl(128, 16_000, ChatFormatting.GOLD.getColor(), 2));
    public static final Supplier<EnergyTierImpl> HIGH = ENERGY_TIERS.register("high", () -> new EnergyTierImpl(512, 32_000, ChatFormatting.BLUE.getColor(), 3));
    public static final Supplier<EnergyTierImpl> EXTREME = ENERGY_TIERS.register("extreme", () -> new EnergyTierImpl(2_048, 64_000, ChatFormatting.GREEN.getColor(), 4));
    public static final Supplier<EnergyTierImpl> INSANE = ENERGY_TIERS.register("insane", () -> new EnergyTierImpl(8_192, 128_000, ChatFormatting.RED.getColor(), 5));
    public static final Supplier<EnergyTierImpl> CREATIVE = ENERGY_TIERS.register("creative", () -> new EnergyTierImpl(Integer.MAX_VALUE, Integer.MAX_VALUE, ChatFormatting.LIGHT_PURPLE.getColor(), 6));
}