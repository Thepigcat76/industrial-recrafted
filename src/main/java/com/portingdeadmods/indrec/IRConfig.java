package com.portingdeadmods.indrec;

import com.portingdeadmods.portingdeadlibs.api.config.ConfigValue;

public final class IRConfig {
    private static final String BLOCKS_ENERGY_CAPACITY = "blocks.energy.capacity";

    @ConfigValue(key = "basic_generator", name = "Basic Generator Energy Capacity", comment = "The Energy Capacity of the Basic Generator", category = BLOCKS_ENERGY_CAPACITY)
    public static int basicGeneratorEnergyCapacity = 4_000;
    @ConfigValue(key = "geothermal_generator", name = "Geothermal Generator Energy Capacity", comment = "The Energy Capacity of the Geothermal Generator", category = BLOCKS_ENERGY_CAPACITY)
    public static int geothermalGeneratorEnergyCapacity = 16_000;
    @ConfigValue(key = "wind_mill", name = "Wind Mill Energy Capacity", comment = "The Energy Capacity of the Wind Mill", category = BLOCKS_ENERGY_CAPACITY)
    public static int windMillEnergyCapacity = 64_000;
    @ConfigValue(key = "water_mill", name = "Water Mill Energy Capacity", comment = "The Energy Capacity of the Water Mill", category = BLOCKS_ENERGY_CAPACITY)
    public static int waterMillEnergyCapacity = 16_000;
    @ConfigValue(key = "basic_solar_panel", name = "Basic Solar Panel Energy Capacity", comment = "The Energy Capacity of the Basic Solar Panel", category = BLOCKS_ENERGY_CAPACITY)
    public static int basicSolarPanelEnergyCapacity = 4_000;
    @ConfigValue(key = "nuclear_reactor", name = "Nuclear Reactor Energy Capacity", comment = "The Energy Capacity of the Nuclear Reactor", category = BLOCKS_ENERGY_CAPACITY)
    public static int nuclearReactorEnergyCapacity = 1_000_000;

    @ConfigValue(key = "battery_box", name = "Battery Box Energy Capacity", comment = "The Energy Capacity of the Battery Box", category = BLOCKS_ENERGY_CAPACITY)
    public static int batteryBoxEnergyCapacity = 40_000;
    @ConfigValue(key = "basic_energy_storage_unit", name = "Basic Energy Storage Unit Capacity", comment = "The Energy Capacity of the Basic Energy Storage Unit", category = BLOCKS_ENERGY_CAPACITY)
    public static int basicEnergyStorageUnitEnergyCapacity = 4_000_000;
    @ConfigValue(key = "advanced_energy_storage_unit", name = "Advanced Energy Storage Unit Capacity", comment = "The Energy Capacity of the Advanced Energy Storage Unit", category = BLOCKS_ENERGY_CAPACITY)
    public static int advancedEnergyStorageUnitEnergyCapacity = 40_000_000;

    @ConfigValue(key = "matter_fabricator", name = "Matter Fabricator Energy Capacity", comment = "The Energy Capacity of the Matter Fabricator, that needs to be filled to generate uu matter", category = BLOCKS_ENERGY_CAPACITY)
    public static int matterFabricatorEnergyCapacity = 1_000_000;

    @ConfigValue(key = "electric_furnace", name = "Electric Furnace Energy Capacity", comment = "The Energy Capacity of the Electric Furnace", category = BLOCKS_ENERGY_CAPACITY)
    public static int electricFurnaceEnergyCapacity = 4_000;
    @ConfigValue(key = "macerator", name = "Macerator Energy Capacity", comment = "The Energy Capacity of the Macerator", category = BLOCKS_ENERGY_CAPACITY)
    public static int maceratorEnergyCapacity = 4_000;
    @ConfigValue(key = "compressor", name = "Compressor Energy Capacity", comment = "The Energy Capacity of the Compressor", category = BLOCKS_ENERGY_CAPACITY)
    public static int compressorEnergyCapacity = 4_000;
    @ConfigValue(key = "recycler", name = "Recycler Energy Capacity", comment = "The Energy Capacity of the Recycler", category = BLOCKS_ENERGY_CAPACITY)
    public static int recyclerEnergyCapacity = 16_000;
    @ConfigValue(key = "extractor", name = "Extractor Energy Capacity", comment = "The Energy Capacity of the Extractor", category = BLOCKS_ENERGY_CAPACITY)
    public static int extractorEnergyCapacity = 4_000;
    @ConfigValue(key = "canning_machine", name = "Canning Machine Energy Capacity", comment = "The Energy Capacity of the Canning Machine", category = BLOCKS_ENERGY_CAPACITY)
    public static int canningMachineEnergyCapacity = 16_000;

    @ConfigValue(key = "charge_pad", name = "Charge Pad Energy Capacity", comment = "The Energy Capacity of the Charge Pad", category = BLOCKS_ENERGY_CAPACITY)
    public static int chargePadEnergyCapacity = 1_000_000;
    @ConfigValue(key = "crop_manager", name = "Crop Manager Energy Capacity", comment = "The Energy Capacity of the Crop Manager", category = BLOCKS_ENERGY_CAPACITY)
    public static int cropManagerEnergyCapacity = 16_000;

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

    private static final String BLOCKS_ENERGY_TRANSFER = "blocks.energy.transfer";

    @ConfigValue(key = "charge_pad", name = "Charge Pad", comment = "The amount of Energy the Charge Pad transfers to items", category = BLOCKS_ENERGY_TRANSFER)
    public static int chargePadEnergyTransfer = 8_192;

    private static final String BLOCKS_FLUID_CAPACITY = "blocks.fluid.capacity";

    @ConfigValue(key = "geothermal_generator", name = "Geothermal Generator Fluid Capacity", comment = "The Fluid Capacity of the Geothermal Generator", category = BLOCKS_FLUID_CAPACITY)
    public static int geothermalGeneratorFluidCapacity = 1_000;

    private static final String CROP_MANAGER = "blocks.crop_manager";

    @ConfigValue(key = "range", name = "Crop Manager Range", comment = "The radius of the square around the Crop Manager in which crops are managed", category = CROP_MANAGER)
    public static int cropManagerRange = 4;
    @ConfigValue(key = "interval", name = "Crop Manager Interval", comment = "The amount of ticks between the Crop Manager harvesting crops and removing weeds", category = CROP_MANAGER)
    public static int cropManagerInterval = 20;
    @ConfigValue(key = "bonemeal_interval", name = "Crop Manager Bone Meal Interval", comment = "The amount of ticks between the Crop Manager applying bone meal to the crops", category = CROP_MANAGER)
    public static int cropManagerBonemealInterval = 100;
    @ConfigValue(key = "harvest_energy_usage", name = "Crop Manager Harvest Energy Usage", comment = "The Energy used by the Crop Manager to harvest a crop", category = CROP_MANAGER)
    public static int cropManagerHarvestEnergyUsage = 40;
    @ConfigValue(key = "bonemeal_energy_usage", name = "Crop Manager Bone Meal Energy Usage", comment = "The Energy used by the Crop Manager to apply bone meal to a crop", category = CROP_MANAGER)
    public static int cropManagerBonemealEnergyUsage = 20;
    @ConfigValue(key = "weed_removal_energy_usage", name = "Crop Manager Weed Removal Energy Usage", comment = "The Energy used by the Crop Manager to remove a weed", category = CROP_MANAGER)
    public static int cropManagerWeedRemovalEnergyUsage = 10;

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
    @ConfigValue(key = "crop_analyzer", name = "Crop Analyzer Energy Capacity", comment = "The Energy Capacity of the Crop Analyzer", category = ITEMS_ENERGY_CAPACITY)
    public static int cropAnalyzerCapacity = 10_000;
    @ConfigValue(key = "mining_laser", name = "Mining Laser Energy Capacity", comment = "The Energy Capacity of the Mining Laser", category = ITEMS_ENERGY_CAPACITY)
    public static int miningLaserCapacity = 64_000;

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
    @ConfigValue(key = "crop_analyzer", name = "Crop Analyzer Energy Usage", comment = "The Energy used by the Crop Analyzer to analyze a crop", category = ITEMS_ENERGY_USAGE)
    public static int cropAnalyzerEnergyUsage = 500;
    @ConfigValue(key = "mining_laser", name = "Mining Laser Energy Usage", comment = "The Energy used by the Mining Laser per tick while firing", category = ITEMS_ENERGY_USAGE)
    public static int miningLaserEnergyUsage = 32;
    @ConfigValue(key = "mining_laser_entity", name = "Mining Laser Entity Energy Usage", comment = "The Energy used by the Mining Laser per tick while its beam hits an entity, instead of the normal usage", category = ITEMS_ENERGY_USAGE)
    public static int miningLaserEntityEnergyUsage = 320;

    @ConfigValue(key = "nano_suit", name = "Nano Suit Energy Usage", comment = "The Energy Usage of the Nano Suit", category = ITEMS_ENERGY_USAGE)
    public static int nanoSuitEnergyUsage = 1_000;
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

    private static final String CROPS = "crops";

    @ConfigValue(key = "weed_growth_chance", name = "Weed Growth Chance", comment = "Chance per random tick for a weed to grow on empty crop sticks", category = CROPS)
    public static float weedGrowthChance = 0.15f;

    private static final String ITEMS_FLUID_CAPACITY = "items.fluid.capacity";

    @ConfigValue(name = "Play Drill Item Animation", comment = "Whether the Drill items should play an animation")
    public static boolean drillItemAnimation = true;
    @ConfigValue(name = "Play Chainsaw Item Animation", comment = "Whether the Chainsaw items should play an animation")
    public static boolean chainsawItemAnimation = true;

    @ConfigValue(name = "Nano Saber Attack Speed", comment = "The Attack Speed of the Nano Saber when activated")
    public static float nanoSaberAttackSpeed = 1.2F;
    @ConfigValue(name = "Nano Saber Attack Damage", comment = "The Attack Damage of the Nano Saber when activated")
    public static int nanoSaberAttackDamage = 19;

    @ConfigValue(name = "Mining Laser Range", comment = "The maximum distance in blocks the Mining Laser can reach")
    public static int miningLaserRange = 32;
    @ConfigValue(name = "Mining Laser Speed", comment = "The mining speed of the Mining Laser. For comparison: Diamond Pickaxe = 8")
    public static float miningLaserSpeed = 8.0F;
    @ConfigValue(name = "Mining Laser Entity Damage", comment = "The damage the Mining Laser deals per hit, entities can be hit about twice per second")
    public static float miningLaserEntityDamage = 6.0F;

    @ConfigValue(name = "Energy Conversion Factor", comment = "The factor for converting EU to FE (By default: 1 EU = 4 FE)")
    public static float energyConversionFactor = 4.0f;

    private static final String OVERCLOCKER_UPGRADE_CATEGORY = "upgrade.overclocker";

    @ConfigValue(key = "time_multiplier", name = "Overclocker Upgrade Time Multiplier", comment = "What each overclocker upgrade multiplies a machine's process time by. For example 0.7 = 70% of the time", category = OVERCLOCKER_UPGRADE_CATEGORY)
    public static float overclockerUpgradeTimeMultiplier = 0.7F;
    @ConfigValue(key = "energy_multiplier", name = "Overclocker Upgrade Energy Multiplier", comment = "What each overclocker upgrade multiplies a machine's energy usage per tick by. For example 1.6 = 160% of the power", category = OVERCLOCKER_UPGRADE_CATEGORY)
    public static float overclockerUpgradeEnergyMultiplier = 1.6F;

    private static final String ENERGY_UPGRADE_CATEGORY = "upgrade.energy";

    @ConfigValue(key = "capacity", name = "Energy Upgrade Capacity", comment = "How much energy capacity each energy upgrade adds to a machine", category = ENERGY_UPGRADE_CATEGORY)
    public static int energyUpgradeCapacity = 10_000;
}