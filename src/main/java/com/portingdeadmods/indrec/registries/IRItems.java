package com.portingdeadmods.indrec.registries;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.content.items.*;
import com.portingdeadmods.indrec.content.items.electric.*;
import com.portingdeadmods.portingdeadlibs.api.utils.PDLDeferredRegisterItems;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

public final class IRItems {
    public static final PDLDeferredRegisterItems ITEMS = PDLDeferredRegisterItems.createItemsRegister(IndustrialRecrafted.MODID);

    /* Raw Ores */
    public static final DeferredItem<Item> RAW_TIN = ITEMS.registerSimpleItem("raw_tin");
    public static final DeferredItem<Item> RAW_URANIUM = ITEMS.registerSimpleItem("raw_uranium");
    public static final DeferredItem<Item> RAW_IRIDIUM = ITEMS.registerSimpleItem("raw_iridium");
    /* Ingots */
    public static final DeferredItem<Item> TIN_INGOT = ITEMS.registerSimpleItem("tin_ingot");
    public static final DeferredItem<Item> REFINED_IRON_INGOT = ITEMS.registerSimpleItem("refined_iron_ingot");
    public static final DeferredItem<Item> BRONZE_INGOT = ITEMS.registerSimpleItem("bronze_ingot");
    public static final DeferredItem<Item> IRIDIUM_INGOT = ITEMS.registerSimpleItem("iridium_ingot");
    public static final DeferredItem<Item> URANIUM_INGOT = ITEMS.registerSimpleItem("uranium_ingot");
    public static final DeferredItem<Item> MIXED_METAL_INGOT = ITEMS.registerSimpleItem("mixed_metal_ingot");
    public static final DeferredItem<Item> IRIDIUM_ALLOY_INGOT = ITEMS.registerSimpleItem("iridium_alloy_ingot");
    /* Plates */
    public static final DeferredItem<Item> TIN_PLATE = ITEMS.registerSimpleItem("tin_plate");
    public static final DeferredItem<Item> COPPER_PLATE = ITEMS.registerSimpleItem("copper_plate");
    public static final DeferredItem<Item> DENSE_COPPER_PLATE = ITEMS.registerSimpleItem("dense_copper_plate");
    public static final DeferredItem<Item> ADVANCED_ALLOY_PLATE = ITEMS.registerSimpleItem("advanced_alloy_plate");
    /* Dusts */
    public static final DeferredItem<Item> TIN_DUST = ITEMS.registerSimpleItem("tin_dust");
    public static final DeferredItem<Item> BRONZE_DUST = ITEMS.registerSimpleItem("bronze_dust");
    public static final DeferredItem<Item> COPPER_DUST = ITEMS.registerSimpleItem("copper_dust");
    public static final DeferredItem<Item> IRON_DUST = ITEMS.registerSimpleItem("iron_dust");
    public static final DeferredItem<Item> GOLD_DUST = ITEMS.registerSimpleItem("gold_dust");
    public static final DeferredItem<Item> COAL_DUST = ITEMS.registerSimpleItem("coal_dust");
    /* Circuits */
    public static final DeferredItem<Item> BASIC_CIRCUIT = ITEMS.registerSimpleItem("basic_circuit");
    public static final DeferredItem<Item> ADVANCED_CIRCUIT = ITEMS.registerSimpleItem("advanced_circuit");
    /* Tools */
    public static final DeferredItem<TreetapItem> TREETAP = ITEMS.registerItem("treetap", TreetapItem::defaultItem);
    public static final DeferredItem<WrenchItem> WRENCH = ITEMS.registerItem("wrench", WrenchItem::defaultItem);
    public static final DeferredItem<CutterItem> CUTTER = ITEMS.registerItem("cutter", CutterItem::defaultItem);
    public static final DeferredItem<RemoteDetonatorItem> REMOTE_DETONATOR = ITEMS.registerItem("remote_detonator", RemoteDetonatorItem::defaultItem);
    /* Electric Tools */
    public static final DeferredItem<ElectricTreetapItem> ELECTRIC_TREETAP = ITEMS.register("electric_treetap", ElectricTreetapItem::defaultItem);
    public static final DeferredItem<ElectricWrenchItem> ELECTRIC_WRENCH = ITEMS.register("electric_wrench", ElectricWrenchItem::defaultItem);
    public static final DeferredItem<ElectricHoeItem> ELECTRIC_HOE = ITEMS.register("electric_hoe", ElectricHoeItem::defaultItem);
    public static final DeferredItem<MiningLaserItem> MINING_LASER = ITEMS.registerItem("mining_laser", MiningLaserItem::defaultItem);
    public static final DeferredItem<NanoSaberItem> NANO_SABER = ITEMS.registerItem("nano_saber", NanoSaberItem::defaultItem);
    public static final DeferredItem<ElectricDrillItem> BASIC_DRILL = ITEMS.registerItem("basic_drill", ElectricDrillItem::basicItem);
    public static final DeferredItem<ElectricDrillItem> ADVANCED_DRILL = ITEMS.registerItem("advanced_drill", ElectricDrillItem::advancedItem);
    public static final DeferredItem<ElectricChainsawItem> BASIC_CHAINSAW = ITEMS.registerItem("basic_chainsaw", ElectricChainsawItem::basicItem);
    public static final DeferredItem<ElectricChainsawItem> ADVANCED_CHAINSAW = ITEMS.registerItem("advanced_chainsaw", ElectricChainsawItem::advancedItem);
    /* Electric Armor */
    public static final DeferredItem<NanoArmorItem> NANO_HELMET = ITEMS.registerItem("nano_helmet", props -> NanoArmorItem.defaultItem(props, ArmorItem.Type.HELMET));
    public static final DeferredItem<Item> NANO_CHESTPLATE = ITEMS.registerItem("nano_chestplate", props -> NanoArmorItem.defaultItem(props, ArmorItem.Type.CHESTPLATE));
    public static final DeferredItem<Item> NANO_LEGGINGS = ITEMS.registerItem("nano_leggings", props -> NanoArmorItem.defaultItem(props, ArmorItem.Type.LEGGINGS));
    public static final DeferredItem<Item> NANO_BOOTS = ITEMS.registerItem("nano_boots", props -> NanoArmorItem.defaultItem(props, ArmorItem.Type.BOOTS));

    public static final DeferredItem<Item> QUANTUM_HELMET = ITEMS.registerItem("quantum_helmet", props -> QuantumArmorItem.defaultItem(props, ArmorItem.Type.HELMET));
    public static final DeferredItem<Item> QUANTUM_CHESTPLATE = ITEMS.registerItem("quantum_chestplate", props -> QuantumArmorItem.defaultItem(props, ArmorItem.Type.CHESTPLATE));
    public static final DeferredItem<Item> QUANTUM_LEGGINGS = ITEMS.registerItem("quantum_leggings", props -> QuantumArmorItem.defaultItem(props, ArmorItem.Type.LEGGINGS));
    public static final DeferredItem<Item> QUANTUM_BOOTS = ITEMS.registerItem("quantum_boots", props -> QuantumArmorItem.defaultItem(props, ArmorItem.Type.BOOTS));

    public static final DeferredItem<FuelJetpackItem> JETPACK = ITEMS.registerItem("jetpack", FuelJetpackItem::defaultItem);
    public static final DeferredItem<ElectricJetpackItem> ELECTRIC_JETPACK = ITEMS.registerItem("electric_jetpack", ElectricJetpackItem::defaultItem);
    /* Batteries */
    public static final DeferredItem<BatteryItem> REDSTONE_BATTERY = ITEMS.registerItem("redstone_battery", BatteryItem::batteryItem);
    public static final DeferredItem<BatteryItem> ENERGY_CRYSTAL = ITEMS.registerItem("energy_crystal", BatteryItem::energyCrystalItem);
    public static final DeferredItem<BatteryItem> LAPOTRON_CRYSTAL = ITEMS.registerItem("lapotron_crystal", BatteryItem::lapotronCrystalItem);

    /* Misc */
    public static final DeferredItem<Item> FLUID_CELL = ITEMS.registerItem("fluid_cell", FluidCellItem::defaultItem);
    public static final DeferredItem<Item> TIN_CAN = ITEMS.registerSimpleItem("tin_can");
    public static final DeferredItem<TinCanWithFoodItem> TIN_CAN_FOOD = ITEMS.registerItem("tin_can_food", TinCanWithFoodItem::defaultItem);
    public static final DeferredItem<Item> FUSE = ITEMS.registerSimpleItem("fuse");

    /* Coal is way too useful lol */
    public static final DeferredItem<Item> COAL_BALL = ITEMS.registerSimpleItem("coal_ball");
    public static final DeferredItem<Item> COMPRESSED_COAL_BALL = ITEMS.registerSimpleItem("compressed_coal_ball");
    public static final DeferredItem<Item> GRAPHENE = ITEMS.registerSimpleItem("graphene");
    public static final DeferredItem<Item> CARBON_FIBER = ITEMS.registerSimpleItem("carbon_fiber");
    public static final DeferredItem<Item> CARBON_MESH = ITEMS.registerSimpleItem("carbon_mesh");
    public static final DeferredItem<Item> CARBON_PLATE = ITEMS.registerSimpleItem("carbon_plate");

    public static final DeferredItem<Item> SCRAP = ITEMS.registerSimpleItem("scrap");
    public static final DeferredItem<ScrapBoxItem> SCRAP_BOX = ITEMS.registerItem("scrap_box", ScrapBoxItem::new);
    public static final DeferredItem<Item> UU_MATTER = ITEMS.registerSimpleItem("uu_matter");

    /* Rubber */
    public static final DeferredItem<Item> STICKY_RESIN = ITEMS.registerSimpleItem("stick_resin");
    public static final DeferredItem<Item> RUBBER = ITEMS.registerSimpleItem("rubber");

    /* Reactor */
    public static final DeferredItem<Item> SINGLE_URANIUM_FUEL_ROD = ITEMS.registerSimpleItem("single_uranium_fuel_rod");
    public static final DeferredItem<Item> DOUBLE_URANIUM_FUEL_ROD = ITEMS.registerSimpleItem("double_uranium_fuel_rod");
    public static final DeferredItem<Item> QUAD_URANIUM_FUEL_ROD = ITEMS.registerSimpleItem("quad_uranium_fuel_rod");

    /* Plant stuff - mmmmm yummy */
    public static final DeferredItem<Item> PLANT_BALL = ITEMS.registerSimpleItem("plant_ball");

}
