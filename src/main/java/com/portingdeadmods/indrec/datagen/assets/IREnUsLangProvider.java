package com.portingdeadmods.indrec.datagen.assets;

import com.portingdeadmods.indrec.IRRegistries;
import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.api.energy.EnergyTier;
import com.portingdeadmods.indrec.registries.*;
import com.portingdeadmods.portingdeadlibs.api.fluids.PDLFluid;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.function.Supplier;

public class IREnUsLangProvider extends LanguageProvider {
    public IREnUsLangProvider(PackOutput output) {
        super(output, IndustrialRecrafted.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        IRTranslations.TRANSLATIONS.getDefaultTranslations().forEach(this::add);

        addItem(IRItems.RAW_TIN, "Raw Tin");
        addItem(IRItems.RAW_URANIUM, "Raw Uranium");
        addItem(IRItems.RAW_IRIDIUM, "Raw Iridium");

        addItem(IRItems.TIN_INGOT, "Tin Ingot");
        addItem(IRItems.REFINED_IRON_INGOT, "Refined Iron Ingot");
        addItem(IRItems.URANIUM_INGOT, "Uranium Ingot");
        addItem(IRItems.BRONZE_INGOT, "Bronze Ingot");
        addItem(IRItems.IRIDIUM_INGOT, "Iridium Ingot");
        addItem(IRItems.MIXED_METAL_INGOT, "Mixed Metal Ingot");
        addItem(IRItems.IRIDIUM_ALLOY_INGOT, "Iridium Alloy Ingot");

        addItem(IRItems.TIN_PLATE, "Tin Plate");
        addItem(IRItems.COPPER_PLATE, "Copper Plate");
        addItem(IRItems.DENSE_COPPER_PLATE, "Dense Copper Plate");
        addItem(IRItems.ADVANCED_ALLOY_PLATE, "Advanced Alloy Plate");

        addItem(IRItems.TIN_DUST, "Tin Dust");
        addItem(IRItems.BRONZE_DUST, "Bronze Dust");
        addItem(IRItems.COPPER_DUST, "Copper Dust");
        addItem(IRItems.IRON_DUST, "Iron Dust");
        addItem(IRItems.GOLD_DUST, "Gold Dust");
        addItem(IRItems.COAL_DUST, "Coal Dust");

        addItem(IRItems.BASIC_CIRCUIT, "Basic Circuit");
        addItem(IRItems.ADVANCED_CIRCUIT, "Advanced Circuit");

        addItem(IRItems.WRENCH, "Wrench");
        addItem(IRItems.TREETAP, "Treetap");
        addItem(IRItems.CUTTER, "Cutter");
        addItem(IRItems.REMOTE_DETONATOR, "Remote Detonator");
        addItem(IRItems.DYNAMITE, "Dynamite");

        addItem(IRItems.ELECTRIC_TREETAP, "Electric Treetap");
        //addItem(IRItems.ELECTRIC_WRENCH, "Electric Wrench");
        addItem(IRItems.ELECTRIC_HOE, "Electric Hoe");
        addItem(IRItems.MINING_LASER, "Mining Laser");
        addItem(IRItems.NANO_SABER, "Nano Saber");
        addItem(IRItems.BASIC_DRILL, "Basic Drill");
        addItem(IRItems.ADVANCED_DRILL, "Advanced Drill");
        addItem(IRItems.BASIC_CHAINSAW, "Basic Chainsaw");
        addItem(IRItems.ADVANCED_CHAINSAW, "Advanced Chainsaw");

        addItem(IRItems.NANO_HELMET, "Nano Helmet");
        addItem(IRItems.NANO_CHESTPLATE, "Nano Chestplate");
        addItem(IRItems.NANO_LEGGINGS, "Nano Leggings");
        addItem(IRItems.NANO_BOOTS, "Nano Boots");

        addItem(IRItems.QUANTUM_HELMET, "Quantum Helmet");
        addItem(IRItems.QUANTUM_CHESTPLATE, "Quantum Chestplate");
        addItem(IRItems.QUANTUM_LEGGINGS, "Quantum Leggings");
        addItem(IRItems.QUANTUM_BOOTS, "Quantum Boots");

        addItem(IRItems.JETPACK, "Jetpack");
        addItem(IRItems.ELECTRIC_JETPACK, "Electric Jetpack");

        addItem(IRItems.REDSTONE_BATTERY, "Redstone Battery");
        addItem(IRItems.ENERGY_CRYSTAL, "Energy Crystal");
        addItem(IRItems.LAPOTRON_CRYSTAL, "Lapotoron Crystal");

        addItem(IRItems.FLUID_CELL, "Fluid Cell");
        addItem(IRItems.FUSE, "Fuse");
        addItem(IRItems.TIN_CAN, "Tin Can");
        addItem(IRItems.TIN_CAN_FOOD, "Tin Can With Food");

        addItem(IRItems.STICKY_RESIN, "Sticky Resin");
        addItem(IRItems.RUBBER, "Rubber");

        addItem(IRItems.COAL_BALL, "Coal Ball");
        addItem(IRItems.COMPRESSED_COAL_BALL, "Compressed Coal Ball");
        addItem(IRItems.GRAPHENE, "Graphene");
        addItem(IRItems.CARBON_FIBER, "Carbon Fiber");
        addItem(IRItems.CARBON_MESH, "Carbon Mesh");
        addItem(IRItems.CARBON_PLATE, "Carbon Plate");

        addItem(IRItems.SCRAP, "Scrap");
        addItem(IRItems.SCRAP_BOX, "Scrap Box");
        addItem(IRItems.UU_MATTER, "UU Matter");

        addItem(IRFluids.BIO_FUEL.getDeferredBucket(), "Bio Fuel Bucket");

        addItem(IRItems.PLANT_BALL, "Plant Ball");

        addItem(IRItems.SINGLE_URANIUM_FUEL_ROD, "Uranium Fuel Rod");
        addItem(IRItems.DOUBLE_URANIUM_FUEL_ROD, "Double Uranium Fuel Rod");
        addItem(IRItems.QUAD_URANIUM_FUEL_ROD, "Quad Uranium Fuel Rod");

        addBlock(IRBlocks.INDUSTRIAL_TNT, "Industrial Tnt");
        addBlock(IRBlocks.NUKE, "Nuke");

        addBlock(IRBlocks.MACHINE_FRAME, "Machine Frame");
        addBlock(IRBlocks.ADVANCED_MACHINE_FRAME, "Advanced Machine Frame");

        addBlock(IRMachines.BATTERY_BOX.getBlockSupplier(), "Battery Box");
        addBlock(IRMachines.BASIC_ENERGY_STORAGE_UNIT.getBlockSupplier(), "Basic Energy Storage Unit");
        addBlock(IRMachines.ADVANCED_ENERGY_STORAGE_UNIT.getBlockSupplier(), "Advanced Energy Storage Unit");

        addBlock(IRMachines.NUCLEAR_REACTOR.getBlockSupplier(), "Nuclear Reactor");
        addBlock(IRMachines.MATTER_FABRICATOR.getBlockSupplier(), "Matter Fabricator");
        addBlock(IRBlocks.NUCLEAR_REACTOR_CHAMBER, "Nuclear Reactor Chamber");

        addBlock(IRBlocks.TIN_CABLE, "Tin Cable");
        addBlock(IRBlocks.COPPER_CABLE, "Copper Cable");
        addBlock(IRBlocks.GOLD_CABLE, "Gold Cable");
        addBlock(IRBlocks.HV_CABLE, "HV Cable");
        addBlock(IRBlocks.GLASS_FIBRE_CABLE, "Glass Fibre Cable");
        addBlock(IRBlocks.BURNT_CABLE, "Burnt Cable");

        addBlock(IRBlocks.REINFORCED_DOOR, "Reinforced Door");
        addBlock(IRBlocks.REINFORCED_GLASS, "Reinforced Glass");
        addBlock(IRBlocks.REINFORCED_STONE, "Reinforced Stone");

        addBlock(IRBlocks.TIN_ORE, "Tin Ore");
        addBlock(IRBlocks.URANIUM_ORE, "Uranium Ore");
        addBlock(IRBlocks.IRIDIUM_ORE, "Iridium Ore");
        addBlock(IRBlocks.DEEPSLATE_TIN_ORE, "Deepslate Tin Ore");
        addBlock(IRBlocks.DEEPSLATE_URANIUM_ORE, "Deepslate Uranium Ore");
        addBlock(IRBlocks.DEEPSLATE_IRIDIUM_ORE, "Deepslate Iridium Ore");

        addBlock(IRBlocks.RUBBER_SHEET, "Rubber Sheet");
        addBlock(IRBlocks.STICKY_RESIN_SHEET, "Sticky Resin Sheet");

        add(IRMachines.BASIC_GENERATOR.getBlock(), "Basic Generator");
        add(IRMachines.GEOTHERMAL_GENERATOR.getBlock(), "Geothermal Generator");
        add(IRMachines.WIND_MILL.getBlock(), "Wind Mill");
        add(IRMachines.WATER_MILL.getBlock(), "Water Mill");
        add(IRMachines.ELECTRIC_FURNACE.getBlock(), "Electric Furnace");
        add(IRMachines.CHARGE_PAD.getBlock(), "Charge Pad");
        add(IRMachines.COMPRESSOR.getBlock(), "Compressor");
        add(IRMachines.RECYCLER.getBlock(), "Recycler");
        add(IRMachines.MACERATOR.getBlock(), "Macerator");
        add(IRMachines.EXTRACTOR.getBlock(), "Extractor");
        add(IRMachines.CANNING_MACHINE.getBlock(), "Canning Machine");
        add(IRMachines.BASIC_SOLAR_PANEL.getBlock(), "Basic Solar Panel");

        addBlock(IRBlocks.TIN_BLOCK, "Tin Block");
        addBlock(IRBlocks.URANIUM_BLOCK, "Uranium Block");
        addBlock(IRBlocks.BRONZE_BLOCK, "Bronze Block");

        addBlock(IRBlocks.RUBBER_TREE_LOG, "Rubber Tree Log");
        addBlock(IRBlocks.RUBBER_TREE_WOOD, "Rubber Tree Wood");
        addBlock(IRBlocks.STRIPPED_RUBBER_TREE_LOG, "Rubber Tree Stripped Log");
        addBlock(IRBlocks.STRIPPED_RUBBER_TREE_WOOD, "Rubber Tree Stripped Wood");
        addBlock(IRBlocks.RUBBER_TREE_LEAVES, "Rubber Tree Leaves");
        addBlock(IRBlocks.RUBBER_TREE_SAPLING, "Rubber Tree Sapling");
        addBlock(IRBlocks.RUBBER_TREE_PLANKS, "Rubber Tree Planks");
        addBlock(IRBlocks.RUBBER_TREE_DOOR, "Rubber Tree Door");
        addBlock(IRBlocks.RUBBER_TREE_TRAPDOOR, "Rubber Tree Trapdoor");
        addBlock(IRBlocks.RUBBER_TREE_FENCE, "Rubber Tree Fence");
        addBlock(IRBlocks.RUBBER_TREE_FENCE_GATE, "Rubber Tree Fence Gate");
        addBlock(IRBlocks.RUBBER_TREE_PRESSURE_PLATE, "Rubber Tree Pressure Plate");
        addBlock(IRBlocks.RUBBER_TREE_BUTTON, "Rubber Tree Button");
        addBlock(IRBlocks.RUBBER_TREE_SLAB, "Rubber Tree Slab");
        addBlock(IRBlocks.RUBBER_TREE_STAIRS, "Rubber Tree Stairs");

        addEnergyTier(IREnergyTiers.NONE, "None");
        addEnergyTier(IREnergyTiers.LOW, "Low");
        addEnergyTier(IREnergyTiers.MEDIUM, "Medium");
        addEnergyTier(IREnergyTiers.HIGH, "High");
        addEnergyTier(IREnergyTiers.EXTREME, "Extreme");
        addEnergyTier(IREnergyTiers.INSANE, "Insane");
        addEnergyTier(IREnergyTiers.CREATIVE, "Creative");

        addFluid(IRFluids.BIO_FUEL, "Bio Fuel");

    }

    private void addFluid(PDLFluid key, String val) {
        add("fluid." + IndustrialRecrafted.MODID + "." + key.getName(), val);
        add("fluid_type." + IndustrialRecrafted.MODID + "." + key.getName(), val);
    }

    private void addEnergyTier(Supplier<? extends EnergyTier> key, String val) {
        add("energy_tier." + IndustrialRecrafted.MODID + "." + IRRegistries.ENERGY_TIER.getKey(key.get()).getPath(), val);
    }

}
