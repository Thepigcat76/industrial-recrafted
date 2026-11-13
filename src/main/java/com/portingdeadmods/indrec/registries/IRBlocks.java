package com.portingdeadmods.indrec.registries;

import net.minecraft.world.effect.MobEffects;
import com.portingdeadmods.indrec.IRFeatureFlags;
import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.content.blocks.*;
import com.portingdeadmods.indrec.content.blocks.CropBlock;
import com.portingdeadmods.indrec.content.worldgen.IRTreeGrowers;
import com.portingdeadmods.portingdeadlibs.api.utils.PDLDeferredRegisterBlocks;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.Set;
import java.util.function.Supplier;

public final class IRBlocks {
    public static final PDLDeferredRegisterBlocks BLOCKS = PDLDeferredRegisterBlocks.createBlocksRegister(IndustrialRecrafted.MODID, IRItems.ITEMS);

    public static final BlockSetType RUBBER_SET_TYPE = BlockSetType.register(new BlockSetType(IndustrialRecrafted.MODID + ":rubber"));
    public static final BlockSetType REINFORCED_STONE_SET_TYPE = BlockSetType.register(new BlockSetType("reinforced_stone", false, false, false, BlockSetType.PressurePlateSensitivity.EVERYTHING, SoundType.METAL, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));
    public static final WoodType RUBBER_WOOD_TYPE = WoodType.register(new WoodType(IndustrialRecrafted.MODID + ":rubber", RUBBER_SET_TYPE));
    public static final BlockBehaviour.Properties MACHINE_FRAME_PROPS = BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK);

    public static final Set<Supplier<? extends Block>> CUSTOM_ITEM_MODELS;

    public static final DeferredBlock<Block> MACHINE_FRAME = BLOCKS.registerBlockWithItem("machine_frame", Block::new, MACHINE_FRAME_PROPS);
    public static final DeferredBlock<Block> ADVANCED_MACHINE_FRAME = BLOCKS.registerBlockWithItem("advanced_machine_frame", Block::new, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK));

    // Generators
    public static final DeferredBlock<ReactorChamberBlock> NUCLEAR_REACTOR_CHAMBER = BLOCKS.registerBlockWithItem("nuclear_reactor_chamber", ReactorChamberBlock::new, MACHINE_FRAME_PROPS);

    // Cables
    public static final BlockBehaviour.Properties CABLE_BLOCK_PROPS = BlockBehaviour.Properties.of()
            .sound(SoundType.WOOL)
            .mapColor(MapColor.COLOR_BLACK)
            .strength(0.8f);
    public static final DeferredBlock<CableBlock> TIN_CABLE = BLOCKS.registerWithItem("tin_cable",
            () -> new CableBlock(CABLE_BLOCK_PROPS, 6, IREnergyTiers.LOW));
    public static final DeferredBlock<CableBlock> COPPER_CABLE = BLOCKS.registerWithItem("copper_cable",
            () -> new CableBlock(CABLE_BLOCK_PROPS, 6, IREnergyTiers.MEDIUM));
    public static final DeferredBlock<CableBlock> GOLD_CABLE = BLOCKS.registerWithItem("gold_cable",
            () -> new CableBlock(CABLE_BLOCK_PROPS, 6, IREnergyTiers.HIGH));
    public static final DeferredBlock<CableBlock> HV_CABLE = BLOCKS.registerWithItem("hv_cable",
            () -> new CableBlock(CABLE_BLOCK_PROPS, 6, IREnergyTiers.EXTREME));
    public static final DeferredBlock<CableBlock> GLASS_FIBRE_CABLE = BLOCKS.registerWithItem("glass_fibre_cable",
            () -> new CableBlock(BlockBehaviour.Properties.ofFullCopy(HV_CABLE.get()).sound(SoundType.GLASS), 4, IREnergyTiers.INSANE));
    public static final DeferredBlock<CableBlock> BURNT_CABLE = BLOCKS.registerWithItem("burnt_cable",
            () -> new BurntCableBlock(CABLE_BLOCK_PROPS, 4, IREnergyTiers.NONE));

    // Storage Blocks
    public static final DeferredBlock<Block> TIN_BLOCK = BLOCKS.registerBlockWithItem("tin_block", Block::new, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK));
    public static final DeferredBlock<Block> URANIUM_BLOCK = BLOCKS.registerBlockWithItem("uranium_block", Block::new, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK));
    public static final DeferredBlock<Block> BRONZE_BLOCK = BLOCKS.registerBlockWithItem("bronze_block", Block::new, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK));

    // Sheets
    public static final DeferredBlock<LayeredSheetBlock> RUBBER_SHEET = BLOCKS.registerBlockWithItem("rubber_sheet", LayeredSheetBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BLACK_WOOL));
    public static final DeferredBlock<LayeredSheetBlock> STICKY_RESIN_SHEET = BLOCKS.registerBlockWithItem("sticky_resin_sheet", LayeredSheetBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.HONEY_BLOCK));

    // Crops
    //public static final DeferredBlock<Block> CROP_STICKS = BLOCKS.registerBlockWithItem("crop_sticks", Block::new, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS));

    // Reinforced Blocks
    public static final DeferredBlock<Block> REINFORCED_STONE = BLOCKS.registerBlockWithItem("reinforced_stone", Block::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
            .strength(12.0F, 400.0F));
    public static final DeferredBlock<Block> REINFORCED_GLASS = BLOCKS.registerBlockWithItem("reinforced_glass", Block::new, BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)
            .requiresCorrectToolForDrops()
            .sound(SoundType.STONE)
            .strength(12.0F, 400.0F));
    public static final DeferredBlock<DoorBlock> REINFORCED_DOOR = BLOCKS.registerWithItem("reinforced_door", () -> new DoorBlock(REINFORCED_STONE_SET_TYPE, BlockBehaviour.Properties.ofFullCopy(REINFORCED_STONE.get())));
    // Explosives
    public static final DeferredBlock<IndustrialTntBlock> INDUSTRIAL_TNT = BLOCKS.registerWithItem("industrial_tnt",
            () -> new IndustrialTntBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TNT)));
    // Rubber
    public static final DeferredBlock<RubberTreeLogBlock> RUBBER_TREE_LOG = BLOCKS.registerBlockWithItem("rubber_tree_log", RubberTreeLogBlock::defaultBlock);
    public static final DeferredBlock<RubberTreeLogBlock> STRIPPED_RUBBER_TREE_LOG = BLOCKS.registerBlockWithItem("stripped_rubber_tree_log", RubberTreeLogBlock::defaultBlock);
    public static final DeferredBlock<RubberTreeLogBlock> RUBBER_TREE_WOOD = BLOCKS.registerBlockWithItem("rubber_tree_wood", RubberTreeLogBlock::defaultBlock);
    public static final DeferredBlock<RubberTreeLogBlock> STRIPPED_RUBBER_TREE_WOOD = BLOCKS.registerBlockWithItem("stripped_rubber_tree_wood", RubberTreeLogBlock::defaultBlock);
    public static final DeferredBlock<RubberTreeLeavesBlock> RUBBER_TREE_LEAVES = BLOCKS.registerBlockWithItem("rubber_tree_leaves", RubberTreeLeavesBlock::defaultBlock);
    public static final DeferredBlock<RubberTreeResinHoleBlock> RUBBER_TREE_RESIN_HOLE = BLOCKS.registerBlock("rubber_tree_resin_hole", RubberTreeResinHoleBlock::defaultBlock);
    public static final DeferredBlock<SaplingBlock> RUBBER_TREE_SAPLING = BLOCKS.registerWithItem("rubber_tree_sapling",
            () -> new SaplingBlock(IRTreeGrowers.RUBBER, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)));
    public static final DeferredBlock<ButtonBlock> RUBBER_TREE_BUTTON = BLOCKS.registerWithItem("rubber_tree_button",
            () -> new ButtonBlock(RUBBER_SET_TYPE, 30, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON)));
    public static final DeferredBlock<Block> RUBBER_TREE_PLANKS = BLOCKS.registerWithItem("rubber_tree_planks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final DeferredBlock<StairBlock> RUBBER_TREE_STAIRS = BLOCKS.registerWithItem("rubber_tree_stairs",
            () -> new StairBlock(RUBBER_TREE_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS)));
    public static final DeferredBlock<DoorBlock> RUBBER_TREE_DOOR = BLOCKS.registerWithItem("rubber_tree_door",
            () -> new DoorBlock(RUBBER_SET_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR)));
    public static final DeferredBlock<PressurePlateBlock> RUBBER_TREE_PRESSURE_PLATE = BLOCKS.registerWithItem("rubber_tree_pressure_plate",
            () -> new PressurePlateBlock(RUBBER_SET_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE)));
    public static final DeferredBlock<FenceBlock> RUBBER_TREE_FENCE = BLOCKS.registerWithItem("rubber_tree_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR)));
    public static final DeferredBlock<TrapDoorBlock> RUBBER_TREE_TRAPDOOR = BLOCKS.registerWithItem("rubber_tree_trapdoor",
            () -> new TrapDoorBlock(RUBBER_SET_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR)));
    public static final DeferredBlock<FenceGateBlock> RUBBER_TREE_FENCE_GATE = BLOCKS.registerWithItem("rubber_tree_fence_gate",
            () -> new FenceGateBlock(RUBBER_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE)));
    public static final DeferredBlock<SlabBlock> RUBBER_TREE_SLAB = BLOCKS.registerWithItem("rubber_tree_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)));
    public static final DeferredBlock<FlowerBlock> GOLDEN_DANDELION = BLOCKS.registerWithItem("golden_dandelion",
            () -> new FlowerBlock(MobEffects.SATURATION, 0.35F, BlockBehaviour.Properties.ofFullCopy(Blocks.DANDELION)));
    public static final DeferredBlock<IronVinesBlock> IRON_VINES = BLOCKS.registerWithItem("iron_vines",
            () -> new IronVinesBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .sound(SoundType.METAL)
                    .strength(3.0F, 6.0F)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()
                    // Offset like flowers, the shape is dynamic so the collision follows the offset
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .dynamicShape()
                    .pushReaction(PushReaction.DESTROY)));
    // Same as iron vines, but without the offset
    public static final DeferredBlock<MetalPlantBlock> TIN_BLOSSOM = BLOCKS.registerWithItem("tin_blossom",
            () -> new MetalPlantBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .sound(SoundType.METAL)
                    .strength(3.0F, 6.0F)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY)));
    // Plants like flowers, only the sound is the one of copper blocks. They oxidize like copper blocks, see the data maps
    public static final DeferredBlock<WeatheringCopriumBlock> COPRIUM = BLOCKS.registerWithItem("coprium",
            () -> new WeatheringCopriumBlock(WeatheringCopper.WeatherState.UNAFFECTED, copriumProperties(MapColor.COLOR_ORANGE)));
    public static final DeferredBlock<WeatheringCopriumBlock> EXPOSED_COPRIUM = BLOCKS.registerWithItem("exposed_coprium",
            () -> new WeatheringCopriumBlock(WeatheringCopper.WeatherState.EXPOSED, copriumProperties(MapColor.TERRACOTTA_LIGHT_GRAY)));
    public static final DeferredBlock<WeatheringCopriumBlock> WEATHERED_COPRIUM = BLOCKS.registerWithItem("weathered_coprium",
            () -> new WeatheringCopriumBlock(WeatheringCopper.WeatherState.WEATHERED, copriumProperties(MapColor.WARPED_STEM)));
    public static final DeferredBlock<WeatheringCopriumBlock> OXIDIZED_COPRIUM = BLOCKS.registerWithItem("oxidized_coprium",
            () -> new WeatheringCopriumBlock(WeatheringCopper.WeatherState.OXIDIZED, copriumProperties(MapColor.WARPED_NYLIUM)));
    public static final DeferredBlock<MetalPlantBlock> WAXED_COPRIUM = BLOCKS.registerWithItem("waxed_coprium",
            () -> new MetalPlantBlock(copriumProperties(MapColor.COLOR_ORANGE)));
    public static final DeferredBlock<MetalPlantBlock> WAXED_EXPOSED_COPRIUM = BLOCKS.registerWithItem("waxed_exposed_coprium",
            () -> new MetalPlantBlock(copriumProperties(MapColor.TERRACOTTA_LIGHT_GRAY)));
    public static final DeferredBlock<MetalPlantBlock> WAXED_WEATHERED_COPRIUM = BLOCKS.registerWithItem("waxed_weathered_coprium",
            () -> new MetalPlantBlock(copriumProperties(MapColor.WARPED_STEM)));
    public static final DeferredBlock<MetalPlantBlock> WAXED_OXIDIZED_COPRIUM = BLOCKS.registerWithItem("waxed_oxidized_coprium",
            () -> new MetalPlantBlock(copriumProperties(MapColor.WARPED_NYLIUM)));
    public static final DeferredBlock<EnderLilyBlock> ENDER_LILY = BLOCKS.registerWithItem("ender_lily",
            () -> new EnderLilyBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LILY_OF_THE_VALLEY).mapColor(MapColor.COLOR_PURPLE)));
    public static final DeferredBlock<TntBlock> NUKE = BLOCKS.registerBlockWithItem("nuke", TntBlock::new);

    public static final DeferredBlock<Block> TIN_ORE = BLOCKS.registerWithItem("tin_ore",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE)));
    public static final DeferredBlock<Block> DEEPSLATE_TIN_ORE = BLOCKS.registerWithItem("deepslate_tin_ore",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE)));
    public static final DeferredBlock<Block> URANIUM_ORE = BLOCKS.registerWithItem("uranium_ore",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE)));
    public static final DeferredBlock<Block> DEEPSLATE_URANIUM_ORE = BLOCKS.registerWithItem("deepslate_uranium_ore",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE)));
    public static final DeferredBlock<Block> IRIDIUM_ORE = BLOCKS.registerWithItem("iridium_ore",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE)));
    public static final DeferredBlock<Block> DEEPSLATE_IRIDIUM_ORE = BLOCKS.registerWithItem("deepslate_iridium_ore",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE)));
    public static final DeferredBlock<CropBlock> CROP = BLOCKS.registerBlock("crop", CropBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .noCollission()
                    .instabreak()
                    .sound(SoundType.BAMBOO_WOOD)
                    .pushReaction(PushReaction.DESTROY));

    private static BlockBehaviour.Properties copriumProperties(MapColor mapColor) {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.DANDELION).mapColor(mapColor).sound(SoundType.COPPER);
    }

    static {
        CUSTOM_ITEM_MODELS = Set.of(
                RUBBER_TREE_FENCE, RUBBER_TREE_TRAPDOOR, RUBBER_TREE_BUTTON, RUBBER_TREE_SAPLING, GOLDEN_DANDELION, IRON_VINES, TIN_BLOSSOM, COPRIUM, EXPOSED_COPRIUM, WEATHERED_COPRIUM, OXIDIZED_COPRIUM, WAXED_COPRIUM, WAXED_EXPOSED_COPRIUM, WAXED_WEATHERED_COPRIUM, WAXED_OXIDIZED_COPRIUM, ENDER_LILY, RUBBER_TREE_DOOR, REINFORCED_DOOR, RUBBER_SHEET, STICKY_RESIN_SHEET, TIN_CABLE, COPPER_CABLE, GOLD_CABLE, HV_CABLE, GLASS_FIBRE_CABLE, BURNT_CABLE, IRMachines.BATTERY_BOX.getBlockSupplier(), IRMachines.BASIC_ENERGY_STORAGE_UNIT.getBlockSupplier(), IRMachines.ADVANCED_ENERGY_STORAGE_UNIT.getBlockSupplier()
        );
    }

}
