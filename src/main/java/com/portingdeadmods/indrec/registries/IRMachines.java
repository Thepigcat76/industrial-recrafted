package com.portingdeadmods.indrec.registries;


import com.portingdeadmods.indrec.IRConfig;
import com.portingdeadmods.indrec.api.blocks.MachineBlock;
import com.portingdeadmods.indrec.api.energy.EnergyTier;
import com.portingdeadmods.indrec.content.blockentities.*;
import com.portingdeadmods.indrec.content.blocks.TransformerBlock;
import com.portingdeadmods.indrec.content.menus.*;
import com.portingdeadmods.indrec.utils.machines.IRMachine;
import com.portingdeadmods.indrec.utils.machines.MachineRegistrationHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public final class IRMachines {
    public static final MachineRegistrationHelper HELPER = new MachineRegistrationHelper(IRBlocks.BLOCKS, IRItems.ITEMS, IRBlockEntityTypes.BLOCK_ENTITY_TYPES, IRMenuTypes.MENU_TYPES);

    public static final IRMachine ELECTRIC_FURNACE = HELPER.registerMachine("electric_furnace", IRMachine.builder(IREnergyTiers.LOW)
            .block(MachineBlock::new, MachineBlock.builder()
                    .activatable()
                    .rotatableHorizontal()
                    .ticking()
            )
            .energy(() -> IRConfig.electricFurnaceEnergyCapacity, true, false)
            .blockEntity(ElectricFurnaceBlockEntity::new)
            .menu(ElectricFurnaceMenu::new));
    public static final IRMachine CHARGE_PAD = HELPER.registerMachine("charge_pad", IRMachine.builder(IREnergyTiers.HIGH)
            .block(MachineBlock::new, MachineBlock.builder()
                    .activatable()
                    .rotatableHorizontal()
                    .ticking()
            )
            .energy(() -> IRConfig.chargePadEnergyCapacity, true, false)
            .blockEntity(ChargePadBlockEntity::new));
    public static final IRMachine CROP_MANAGER = HELPER.registerMachine("crop_manager", IRMachine.builder(IREnergyTiers.MEDIUM)
            .block(MachineBlock::new, MachineBlock.builder()
                    .rotatableHorizontal()
                    .ticking()
            )
            .energy(() -> IRConfig.cropManagerEnergyCapacity, true, false)
            .blockEntity(CropManagerBlockEntity::new)
            .menu(CropManagerMenu::new));
    public static final IRMachine COMPRESSOR = HELPER.registerMachine("compressor", IRMachine.builder(IREnergyTiers.LOW)
            .block(MachineBlock::new, MachineBlock.builder()
                    .activatable()
                    .rotatableHorizontal()
                    .ticking()
            )
            .energy(() -> IRConfig.compressorEnergyCapacity, true, false)
            .recipeLayout(IRRecipeLayouts.COMPRESSOR)
            .blockEntity(CompressorBlockEntity::new)
            .menu(CompressorMenu::new));
    public static final IRMachine RECYCLER = HELPER.registerMachine("recycler", IRMachine.builder(IREnergyTiers.MEDIUM)
            .block(MachineBlock::new, MachineBlock.builder()
                    .activatable()
                    .rotatableHorizontal()
                    .ticking()
            )
            .energy(() -> IRConfig.recyclerEnergyCapacity, true, false)
            .recipeLayout(IRRecipeLayouts.RECYCLER)
            .blockEntity(RecyclerBlockEntity::new)
            .menu(RecyclerMenu::new));
    public static final IRMachine MACERATOR = HELPER.registerMachine("macerator", IRMachine.builder(IREnergyTiers.LOW)
            .block(MachineBlock::new, MachineBlock.builder()
                    .activatable()
                    .rotatableHorizontal()
                    .ticking()
            )
            .energy(() -> IRConfig.maceratorEnergyCapacity, true, false)
            .recipeLayout(IRRecipeLayouts.MACERATOR)
            .blockEntity(MaceratorBlockEntity::new)
            .menu(MaceratorMenu::new));
    public static final IRMachine EXTRACTOR = HELPER.registerMachine("extractor", IRMachine.builder(IREnergyTiers.LOW)
            .block(MachineBlock::new, MachineBlock.builder()
                    .activatable()
                    .rotatableHorizontal()
                    .ticking()
            )
            .energy(() -> IRConfig.extractorEnergyCapacity, true, false)
            .recipeLayout(IRRecipeLayouts.EXTRACTOR)
            .blockEntity(ExtractorBlockEntity::new)
            .menu(ExtractorMenu::new));
    public static final IRMachine CANNING_MACHINE = HELPER.registerMachine("canning_machine", IRMachine.builder(IREnergyTiers.MEDIUM)
            .block(MachineBlock::new, MachineBlock.builder()
                    .activatable()
                    .rotatableHorizontal()
                    .ticking()
            )
            .energy(() -> IRConfig.canningMachineEnergyCapacity, true, false)
            .recipeLayout(IRRecipeLayouts.CANNING_MACHINE)
            .blockEntity(CanningMachineBlockEntity::new)
            .menu(CanningMachineMenu::new));
    public static final IRMachine MATTER_FABRICATOR = HELPER.registerMachine("matter_fabricator", IRMachine.builder(IREnergyTiers.INSANE)
            .block(MachineBlock::new, MachineBlock.builder()
                    .activatable()
                    .rotatableHorizontal()
                    .ticking()
            )
            .energy(() -> IRConfig.matterFabricatorEnergyCapacity, true, false)
            .blockEntity(MatterFabricatorBlockEntity::new)
            .menu(MatterFabricatorMenu::new));
    public static final IRMachine BASIC_SOLAR_PANEL = HELPER.registerMachine("basic_solar_panel", IRMachine.builder(IREnergyTiers.LOW)
            .block(MachineBlock::new, MachineBlock.builder()
                    .activatable()
                    .rotatableHorizontal()
                    .ticking()
            )
            .energy(() -> IRConfig.basicSolarPanelEnergyCapacity, false, true)
            .blockEntity(SolarPanelBlockEntity::new)
            .menu(SolarPanelMenu::new));
    public static final IRMachine BASIC_GENERATOR = HELPER.registerMachine("basic_generator", IRMachine.builder(IREnergyTiers.LOW)
            .block(MachineBlock::new, MachineBlock.builder()
                    .activatable()
                    .rotatableHorizontal()
                    .ticking()
            )
            .energy(() -> IRConfig.basicGeneratorEnergyCapacity, false, true)
            .blockEntity(BasicGeneratorBlockEntity::new)
            .menu(BasicGeneratorMenu::new));
    public static final IRMachine WATER_MILL = HELPER.registerMachine("water_mill", IRMachine.builder(IREnergyTiers.MEDIUM)
            .block(MachineBlock::new, MachineBlock.builder()
                    .activatable()
                    .rotatableHorizontal()
                    .ticking()
            )
            .energy(() -> IRConfig.waterMillEnergyCapacity, false, true)
            .blockEntity(WaterMillBlockEntity::new)
            .menu(WaterMillMenu::new));
    public static final IRMachine GEOTHERMAL_GENERATOR = HELPER.registerMachine("geothermal_generator", IRMachine.builder(IREnergyTiers.MEDIUM)
            .block(MachineBlock::new, MachineBlock.builder()
                    .activatable()
                    .rotatableHorizontal()
                    .ticking()
            )
            .energy(() -> IRConfig.geothermalGeneratorEnergyCapacity, false, true)
            .recipeLayout(IRRecipeLayouts.GEOTHERMAL_GENERATOR)
            .blockEntity(GeothermalGeneratorBlockEntity::new)
            .menu(GeothermalGeneratorMenu::new));
    public static final IRMachine WIND_MILL = HELPER.registerMachine("wind_mill", IRMachine.builder(IREnergyTiers.HIGH)
            .block(MachineBlock::new, MachineBlock.builder()
                    .activatable()
                    .rotatableHorizontal()
                    .ticking()
            )
            .energy(() -> IRConfig.windMillEnergyCapacity, false, true)
            .blockEntity(WindMillBlockEntity::new)
            .menu(WindMillMenu::new));
    public static final IRMachine NUCLEAR_REACTOR = HELPER.registerMachine("nuclear_reactor", IRMachine.builder(IREnergyTiers.EXTREME)
            .block(MachineBlock::new, MachineBlock.builder()
                    .activatable()
                    .rotatableHorizontal()
                    .ticking()
            )
            .energy(() -> IRConfig.nuclearReactorEnergyCapacity, true, false)
            .blockEntity(NuclearReactorBlockEntity::new)
            .menu(NuclearReactorMenu::new));
    public static final IRMachine BATTERY_BOX = HELPER.registerMachine("battery_box", IRMachine.builder(IREnergyTiers.LOW)
            .block(MachineBlock::new, MachineBlock.builder()
                    .properties(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS))
                    .activatable()
                    .rotatable()
                    .ticking()
            )
            .energy(() -> IRConfig.batteryBoxEnergyCapacity, true, true)
            .blockEntity(EnergyStorageBlockEntity::new, () -> IRMachines.BATTERY_BOX)
            .menu(BatteryBoxMenu::new));
    public static final IRMachine BASIC_ENERGY_STORAGE_UNIT = HELPER.registerMachine("basic_energy_storage_unit", IRMachine.builder(IREnergyTiers.HIGH)
            .block(MachineBlock::new, MachineBlock.builder()
                    .activatable()
                    .rotatable()
                    .ticking()
            )
            .energy(() -> IRConfig.basicEnergyStorageUnitEnergyCapacity, true, true)
            .blockEntity(EnergyStorageBlockEntity::new, () -> IRMachines.BASIC_ENERGY_STORAGE_UNIT)
            .menu(BatteryBoxMenu::new));
    public static final IRMachine ADVANCED_ENERGY_STORAGE_UNIT = HELPER.registerMachine("advanced_energy_storage_unit", IRMachine.builder(IREnergyTiers.INSANE)
            .block(MachineBlock::new, MachineBlock.builder()
                    .activatable()
                    .rotatable()
                    .ticking()
            )
            .energy(() -> IRConfig.advancedEnergyStorageUnitEnergyCapacity, true, true)
            .blockEntity(EnergyStorageBlockEntity::new, () -> IRMachines.ADVANCED_ENERGY_STORAGE_UNIT)
            .menu(BatteryBoxMenu::new));
    public static final IRMachine LOW_TRANSFORMER = HELPER.registerMachine("low_transformer", IRMachine.builder(IREnergyTiers.LOW)
            .block(makeTransformerBlock(IREnergyTiers.MEDIUM), MachineBlock.builder()
                    .properties(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS))
                    .activatable()
                    .rotatable()
                    .ticking()
            )
            .energy(null, true, true)
            .blockEntity(TransformerBlockEntity::new, () -> IRMachines.LOW_TRANSFORMER));
    public static final IRMachine MEDIUM_TRANSFORMER = HELPER.registerMachine("medium_transformer", IRMachine.builder(IREnergyTiers.MEDIUM)
            .block(makeTransformerBlock(IREnergyTiers.HIGH), MachineBlock.builder()
                    .activatable()
                    .rotatable()
                    .ticking()
            )
            .energy(null, true, true)
            .blockEntity(TransformerBlockEntity::new, () -> IRMachines.MEDIUM_TRANSFORMER));
    public static final IRMachine HIGH_TRANSFORMER = HELPER.registerMachine("high_transformer", IRMachine.builder(IREnergyTiers.HIGH)
            .block(makeTransformerBlock(IREnergyTiers.EXTREME), MachineBlock.builder()
                    .activatable()
                    .rotatable()
                    .ticking()
            )
            .energy(null, true, true)
            .blockEntity(TransformerBlockEntity::new, () -> IRMachines.HIGH_TRANSFORMER));
    public static final IRMachine EXTREME_TRANSFORMER = HELPER.registerMachine("extreme_transformer", IRMachine.builder(IREnergyTiers.EXTREME)
            .block(makeTransformerBlock(IREnergyTiers.INSANE), MachineBlock.builder()
                    .activatable()
                    .rotatable()
                    .ticking()
            )
            .energy(null, true, true)
            .blockEntity(TransformerBlockEntity::new, () -> IRMachines.EXTREME_TRANSFORMER));

    public static @NotNull TriFunction<String, MachineBlock.Builder, Supplier<? extends EnergyTier>, MachineBlock> makeTransformerBlock(Supplier<? extends EnergyTier> higherTier) {
        return (name, builder, inputTier) -> new TransformerBlock(name, builder, inputTier, higherTier);
    }
}