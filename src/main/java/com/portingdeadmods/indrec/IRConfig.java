package com.portingdeadmods.indrec;

import com.portingdeadmods.portingdeadlibs.api.config.ConfigValue;

public final class IRConfig {
    private static final String BLOCKS_ENERGY_CAPACITY = "blocks.energy.capacity";

    @ConfigValue(key = "basic_generator", name = "Basic Generator Energy Capacity", comment = "The Energy Capacity of the Basic Generator", category = BLOCKS_ENERGY_CAPACITY)
    public static int basicGeneratorEnergyCapacity = 4_000;
    @ConfigValue(key = "geothermal_generator", name = "Geothermal Generator Energy Capacity", comment = "The Energy Capacity of the Geothermal Generator", category = BLOCKS_ENERGY_CAPACITY)
    public static int geothermalGeneratorEnergyCapacity = 4_000;
    @ConfigValue(key = "wind_mill", name = "Wind Mill Energy Capacity", comment = "The Energy Capacity of the Wind Mill", category = BLOCKS_ENERGY_CAPACITY)
    public static int windMillEnergyCapacity = 16000;
    @ConfigValue(key = "water_mill", name = "Water Mill Energy Capacity", comment = "The Energy Capacity of the Water Mill", category = BLOCKS_ENERGY_CAPACITY)
    public static int waterMillEnergyCapacity = 16000;
    @ConfigValue(key = "basic_solar_panel", name = "Basic Solar Panel Energy Capacity", comment = "The Energy Capacity of the Basic Solar Panel", category = BLOCKS_ENERGY_CAPACITY)
    public static int basicSolarPanelEnergyCapacity = 4000;

    @ConfigValue(key = "battery_box", name = "Battery Box Energy Capacity", comment = "The Energy Capacity of the Battery Box", category = BLOCKS_ENERGY_CAPACITY)
    public static int batteryBoxEnergyCapacity = 40_000;
    @ConfigValue(key = "basic_energy_storage_unit", name = "Basic Energy Storage Unit Capacity", comment = "The Energy Capacity of the Basic Energy Storage Unit", category = BLOCKS_ENERGY_CAPACITY)
    public static int basicEnergyStorageUnitEnergyCapacity = 4_000_000;
    @ConfigValue(key = "advanced_energy_storage_unit", name = "Advanced Energy Storage Unit Capacity", comment = "The Energy Capacity of the Advanced Energy Storage Unit", category = BLOCKS_ENERGY_CAPACITY)
    public static int advancedEnergyStorageUnitEnergyCapacity = 40_000_000;

    private static final String BLOCKS_ENERGY_PRODUCTION = "blocks.energy.production";

    @ConfigValue(key = "basic_generator", name = "Basic Generator Energy Production", comment = "The amount of Energy produced by the Basic Generator", category = BLOCKS_ENERGY_PRODUCTION)
    public static int basicGeneratorEnergyProduction = 10;
    @ConfigValue(key = "geothermal_generator", name = "Geothermal Generator Energy Production", comment = "The amount of Energy produced by the Geothermal Generator", category = BLOCKS_ENERGY_PRODUCTION)
    public static int geothermalGeneratorEnergyProduction = 20;
    @ConfigValue(key = "basic_solar_panel", name = "Basic Solar Panel Energy Production", comment = "The amount of Energy produced by the Basic Solar Panel", category = BLOCKS_ENERGY_PRODUCTION)
    public static int basicSolarPanelEnergyProduction = 1;
    @ConfigValue(key = "water_mill", name = "Water Mill", comment = "The amount of Energy produced by the Water Mill", category = BLOCKS_ENERGY_PRODUCTION)
    public static int waterMillEnergyProduction = 6;
    @ConfigValue(key = "wind_mill", name = "Wind Mill", comment = "The amount of Energy produced by the Wind Mill", category = BLOCKS_ENERGY_PRODUCTION)
    public static int windMillEnergyProduction = 10;

    private static final String BLOCKS_FLUID_CAPACITY = "blocks.fluid.capacity";

    @ConfigValue(key = "geothermal_generator", name = "Geothermal Generator Fluid Capacity", comment = "The Fluid Capacity of the Geothermal Generator", category = BLOCKS_FLUID_CAPACITY)
    public static int geothermalGeneratorFluidCapacity = 1000;

    private static final String ITEMS_ENERGY_CAPACITY = "items.energy.capacity";

    @ConfigValue(key = "basic_drill", name = "Basic Drill Energy Capacity", comment = "The Energy Capacity of the Basic Drill", category = ITEMS_ENERGY_CAPACITY)
    public static int basicDrillCapacity = 40_000;
    @ConfigValue(key = "basic_chainsaw", name = "Basic Chainsaw Energy Capacity", comment = "The Energy Capacity of the Basic Chainsaw", category = ITEMS_ENERGY_CAPACITY)
    public static int basicChainsawCapacity = 40_000;
    @ConfigValue(key = "basic_battery", name = "Basic Battery Energy Capacity", comment = "The Energy Capacity of the Basic Battery", category = ITEMS_ENERGY_CAPACITY)
    public static int basicBatteryCapacity = 10_000;
    @ConfigValue(key = "electric_hoe", name = "Electric Hoe Energy Capacity", comment = "The Energy Capacity of the Electric Hoe", category = ITEMS_ENERGY_CAPACITY)
    public static int electricHoeCapacity = 10_000;
    @ConfigValue(key = "electric_tree_tap", name = "Electric Tree Tap Energy Capacity", comment = "The Energy Capacity of the Electric Tree Tap", category = ITEMS_ENERGY_CAPACITY)
    public static int electricTreeTapCapacity = 10_000;
    @ConfigValue(key = "electric_wrench", name = "Electric Wrench Energy Capacity", comment = "The Energy Capacity of the Electric Wrench", category = ITEMS_ENERGY_CAPACITY)
    public static int electricWrenchCapacity = 10_000;


    @ConfigValue(key = "nano_suit", name = "Nano Suit Energy Capacity", comment = "The Energy Capacity of the Nano Suit", category = ITEMS_ENERGY_CAPACITY)
    public static int nanoSuitEnergyCapacity = 1_000_000;
    @ConfigValue(key = "quantum_suit", name = "Quantum Suit Energy Capacity", comment = "The Energy Capacity of the Quantum Suit", category = ITEMS_ENERGY_CAPACITY)
    public static int quantumSuitEnergyCapacity = 10_000_000;
    @ConfigValue(key = "jetpack", name = "Jetpack Energy Capacity", comment = "The Energy Capacity of the Jetpack", category = ITEMS_ENERGY_CAPACITY)
    public static int jetpackEnergyCapacity = 40_000;

    @ConfigValue(key = "nano_saber", name = "Nano Saber Energy Capacity", comment = "The Energy Capacity of the Nano Saber", category = ITEMS_ENERGY_CAPACITY)
    public static int nanoSaberCapacity = 400_000;
    @ConfigValue(key = "advanced_drill", name = "Advanced Drill Energy Capacity", comment = "The Energy Capacity of the Advanced Drill", category = ITEMS_ENERGY_CAPACITY)
    public static int advancedDrillCapacity = 400_000;
    @ConfigValue(key = "advanced_chainsaw", name = "Advanced Chainsaw Energy Capacity", comment = "The Energy Capacity of the Advanced Chainsaw", category = ITEMS_ENERGY_CAPACITY)
    public static int advancedChainsawCapacity = 400_000;
    @ConfigValue(key = "energy_crystal", name = "Energy Crystal Capacity", comment = "The Energy Capacity of the Energy Crystal", category = ITEMS_ENERGY_CAPACITY)
    public static int energyCrystalCapacity = 1_000_000;

    @ConfigValue(key = "lapotron_crystal", name = "Lapotron Crystal Energy Capacity", comment = "The Energy Capacity of the Lapotron Crystal", category = ITEMS_ENERGY_CAPACITY)
    public static int lapotronCrystalCapacity = 10_000_000;

    private static final String ITEMS_ENERGY_USAGE = "items.energy.usage";

    @ConfigValue(key = "basic_drill", name = "Basic Drill Energy Usage", comment = "The Energy Usage of the Basic Drill", category = ITEMS_ENERGY_USAGE)
    public static int basicDrillEnergyUsage = 80;
    @ConfigValue(key = "basic_chainsaw", name = "Basic Chainsaw Energy Usage", comment = "The Energy Usage of the Basic Chainsaw", category = ITEMS_ENERGY_USAGE)
    public static int basicChainsawEnergyUsage = 80;
    @ConfigValue(key = "electric_hoe", name = "Electric Hoe Energy Usage", comment = "The Energy Usage of the Electric Hoe", category = ITEMS_ENERGY_USAGE)
    public static int electricHoeEnergyUsage = 80;
    @ConfigValue(key = "electric_tree_tap", name = "Electric Tree Tap Energy Usage", comment = "The Energy Usage of the Electric Tree Tap", category = ITEMS_ENERGY_USAGE)
    public static int electricTreeTapEnergyUsage = 80;

    @ConfigValue(key = "nano_suit", name = "Nano Suit Energy Usage", comment = "The Energy Usage of the Nano Suit", category = ITEMS_ENERGY_USAGE)
    public static int nanoSuitEnergyUsage = 1000;
    @ConfigValue(key = "quantum_suit", name = "Quantum Suit Energy Usage", comment = "The Energy Usage of the Quantum Suit", category = ITEMS_ENERGY_USAGE)
    public static int quantumSuitEnergyUsage = 10_000;
    @ConfigValue(key = "jetpack", name = "Jetpack Energy Usage", comment = "The Energy Usage of the Jetpack", category = ITEMS_ENERGY_USAGE)
    public static int jetpackEnergyUsage = 32;

    @ConfigValue(key = "nano_saber", name = "Nano Saber Energy Usage", comment = "The Energy Usage of the Nano Saber", category = ITEMS_ENERGY_USAGE)
    public static int nanoSaberEnergyUsage = 80;
    @ConfigValue(key = "advanced_drill", name = "Advanced Drill Energy Usage", comment = "The Energy Usage of the Advanced Drill", category = ITEMS_ENERGY_USAGE)
    public static int advancedDrillEnergyUsage = 800;
    @ConfigValue(key = "advanced_chainsaw", name = "Advanced Chainsaw Energy Usage", comment = "The Energy Usage of the Advanced Chainsaw", category = ITEMS_ENERGY_USAGE)
    public static int advancedChainsawEnergyUsage = 800;

    private static final String ITEMS_FLUID_CAPACITY = "items.fluid.capacity";

    @ConfigValue(name = "Play Drill Item Animation", comment = "Whether the Drill items should play an animation")
    public static boolean drillItemAnimation = true;
    @ConfigValue(name = "Play Chainsaw Item Animation", comment = "Whether the Chainsaw items should play an animation")
    public static boolean chainsawItemAnimation = true;

    @ConfigValue(name = "Nano Saber Attack Speed", comment = "The Attack Speed of the Nano Saber when activated")
    public static float nanoSaberAttackSpeed = 1.2F;
    @ConfigValue(name = "Nano Saber Attack Damage", comment = "The Attack Damage of the Nano Saber when activated")
    public static int nanoSaberAttackDamage = 19;

    @ConfigValue(name = "Energy Conversion Factor", comment = "The factor for converting EU to FE (By default: 1 EU = 4 FE)")
    public static float energyConversionFactor = 4.0f;

//    public static final String OVERCLOCKER_UPGRADE_CATEGORY = "upgrade.overclocker";
//
//    @ConfigValue(key = "speed", name = "Overclocker Upgrade Speed", comment = "How much the overclocker upgrade speeds up a machine. For example 0.1 = 10%", category = OVERCLOCKER_UPGRADE_CATEGORY)
//    public static float overclockerUpgradeSpeed = 0.45F;
//    @ConfigValue(key = "energy", name = "Overclocker Upgrade Energy", comment = "How much more energy a machine with the overclocker upgrade consumes. For example 0.1 = 10%", category = OVERCLOCKER_UPGRADE_CATEGORY)
//    public static float overclockerUpgradeEnergy = 0.6F;
}