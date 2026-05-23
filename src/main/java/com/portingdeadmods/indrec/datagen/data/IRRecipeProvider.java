package com.portingdeadmods.indrec.datagen.data;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.impl.recipes.components.EnumRecipeComponent;
import com.portingdeadmods.indrec.impl.recipes.components.energy.EnergyOutputComponent;
import com.portingdeadmods.indrec.impl.recipes.components.fluids.FluidInputComponent;
import com.portingdeadmods.indrec.impl.recipes.layouts.CanningMachineRecipeLayout;
import com.portingdeadmods.indrec.registries.*;
import com.portingdeadmods.indrec.tags.IRTags;
import com.portingdeadmods.indrec.impl.recipes.components.energy.EnergyInputComponent;
import com.portingdeadmods.indrec.impl.recipes.components.TimeComponent;
import com.portingdeadmods.indrec.impl.recipes.components.items.ItemInputComponent;
import com.portingdeadmods.indrec.impl.recipes.components.items.ItemInputListComponent;
import com.portingdeadmods.indrec.impl.recipes.components.items.ItemOutputComponent;
import com.portingdeadmods.indrec.impl.recipes.components.items.ItemOutputListComponent;
import com.portingdeadmods.indrec.datagen.MachineRecipeBuilder;
import com.portingdeadmods.indrec.tags.CTags;
import com.portingdeadmods.indrec.utils.ItemStackBuilder;
import com.portingdeadmods.portingdeadlibs.api.data.PDLDataComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.portingdeadmods.indrec.IndustrialRecrafted.rl;

public class IRRecipeProvider extends RecipeProvider {

    public static final List<ItemLike> URANIUM_ORE_SMELTABLES = List.of(IRItems.RAW_URANIUM, IRBlocks.URANIUM_ORE, IRBlocks.DEEPSLATE_URANIUM_ORE);
    public static final List<ItemLike> TIN_ORE_SMELTABLES = List.of(IRItems.RAW_TIN, IRBlocks.TIN_ORE, IRItems.TIN_DUST, IRBlocks.DEEPSLATE_TIN_ORE);

    public IRRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    private void compressorRecipe(Ingredient input, int inputCount, ItemLike output, int outputCount, int time, int energyPerTick, RecipeOutput recipeOutput) {
        this.compressorRecipe()
                .component(new ItemInputComponent(input, inputCount))
                .component(new ItemOutputComponent(output, outputCount))
                .component(new TimeComponent(time))
                .component(new EnergyInputComponent(time * energyPerTick))
                .save(recipeOutput);
    }

    private void compressorRecipe(Ingredient input, ItemLike output, int time, int energyPerTick, RecipeOutput recipeOutput) {
        this.compressorRecipe(input, 1, output, 1, time, energyPerTick, recipeOutput);
    }

    private void compressorRecipe(ItemLike input, ItemLike output, int time, int energyPerTick, RecipeOutput recipeOutput) {
        this.compressorRecipe(Ingredient.of(input), 1, output, 1, time, energyPerTick, recipeOutput);
    }

    private void compressorRecipe(TagKey<Item> input, ItemLike output, int time, int energyPerTick, RecipeOutput recipeOutput) {
        this.compressorRecipe(Ingredient.of(input), 1, output, 1, time, energyPerTick, recipeOutput);
    }

    private void compressorRecipe(TagKey<Item> input, ItemLike output, RecipeOutput recipeOutput) {
        this.compressorRecipe(Ingredient.of(input), 1, output, 1, 200, 4, recipeOutput);
    }

    private void compressorRecipe(ItemLike input, ItemLike output, RecipeOutput recipeOutput) {
        this.compressorRecipe(Ingredient.of(input), 1, output, 1, 200, 4, recipeOutput);
    }

    private void maceratorRecipe(Ingredient input, int inputCount, ItemLike output, int outputCount, int time, int energyPerTick, RecipeOutput recipeOutput, ResourceLocation id) {
        this.maceratorRecipe()
                .component(new ItemInputComponent(input, inputCount))
                .component(new ItemOutputListComponent(output, outputCount))
                .component(new TimeComponent(time))
                .component(new EnergyInputComponent(time * energyPerTick))
                .save(recipeOutput, id);
    }

    private void maceratorRecipe(Ingredient input, int inputCount, ItemLike output, int outputCount, int time, int energyPerTick, RecipeOutput recipeOutput) {
        this.maceratorRecipe()
                .component(new ItemInputComponent(input, inputCount))
                .component(new ItemOutputListComponent(output, outputCount))
                .component(new TimeComponent(time))
                .component(new EnergyInputComponent(time * energyPerTick))
                .save(recipeOutput);
    }

    private void maceratorRecipe(Ingredient input, ItemLike output, int time, int energyPerTick, RecipeOutput recipeOutput) {
        this.maceratorRecipe(input, 1, output, 1, time, energyPerTick, recipeOutput);
    }

    private void maceratorRecipe(ItemLike input, ItemLike output, int time, int energyPerTick, RecipeOutput recipeOutput) {
        this.maceratorRecipe(Ingredient.of(input), 1, output, 1, time, energyPerTick, recipeOutput);
    }

    private void maceratorRecipe(TagKey<Item> input, ItemLike output, int time, int energyPerTick, RecipeOutput recipeOutput) {
        this.maceratorRecipe(Ingredient.of(input), 1, output, 1, time, energyPerTick, recipeOutput);
    }

    private void maceratorRecipe(TagKey<Item> input, ItemLike output, RecipeOutput recipeOutput, ResourceLocation id) {
        this.maceratorRecipe(Ingredient.of(input), 1, output, 1, 200, 4, recipeOutput, id);
    }

    private void maceratorRecipe(TagKey<Item> input, ItemLike output, RecipeOutput recipeOutput) {
        this.maceratorRecipe(Ingredient.of(input), 1, output, 1, 200, 4, recipeOutput);
    }

    private void maceratorRecipe(ItemLike input, ItemLike output, int outputCount, RecipeOutput recipeOutput) {
        this.maceratorRecipe(Ingredient.of(input), 1, output, outputCount, 200, 4, recipeOutput);
    }

    private void maceratorRecipe(TagKey<Item> input, ItemLike output, int outputCount, RecipeOutput recipeOutput) {
        this.maceratorRecipe(Ingredient.of(input), 1, output, outputCount, 200, 4, recipeOutput);
    }

    private void maceratorRecipe(ItemLike input, ItemLike output, RecipeOutput recipeOutput) {
        this.maceratorRecipe(Ingredient.of(input), 1, output, 1, 200, 4, recipeOutput);
    }

    @Override
    protected void buildRecipes(RecipeOutput output0) {
        RecipeOutput output = new IRRecipeOutput(output0);

        super.buildRecipes(output);

        compressingRecipes(output);

        maceratingRecipes(output);

        extractingRecipes(output);

        canningRecipes(output);

        electricToolCraftingRecipes(output);

        armorCraftingRecipes(output);

        batteryCraftingRecipes(output);

        circuitCraftingRecipes(output);

        machineFrameCraftingRecipes(output);

        reinforcedBlocksCraftingRecipes(output);

        machineCraftingRecipes(output);

        generatorCraftingRecipes(output);

        toolCraftingRecipes(output);

        reactorComponentCraftingRecipes(output);

        cableCraftingRecipes(output);

        rubberWoodCraftingRecipes(output);

        explosivesCraftingRecipes(output);

        componentsCraftingRecipes(output);

        energyStorageUnitCraftingRecipes(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IRItems.BRONZE_DUST, 3)
                .requires(CTags.ItemTags.DUSTS_TIN)
                .requires(CTags.ItemTags.DUSTS_COPPER)
                .requires(CTags.ItemTags.DUSTS_COPPER)
                .unlockedBy("has_tin_dust", has(CTags.ItemTags.DUSTS_TIN))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.SCRAP_BOX.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', IRItems.SCRAP)
                .unlockedBy("has_scrap", has(IRItems.SCRAP))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.FLUID_CELL.get(), 4)
                .pattern(" T ")
                .pattern("TGT")
                .pattern(" T ")
                .define('T', CTags.ItemTags.PLATES_TIN)
                .define('G', Tags.Items.GLASS_PANES)
                .unlockedBy("has_tin_plate", has(CTags.ItemTags.PLATES_TIN))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.TIN_CAN.get(), 6)
                .pattern("# #")
                .pattern("###")
                .define('#', CTags.ItemTags.PLATES_TIN)
                .unlockedBy("has_tin_ingot", has(CTags.ItemTags.PLATES_TIN))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.IRIDIUM_ALLOY_INGOT.get())
                .pattern("IAI")
                .pattern("ADA")
                .pattern("IAI")
                .define('I', CTags.ItemTags.RAW_MATERIALS_IRIDIUM)
                .define('A', CTags.ItemTags.PLATES_ADVANCED_ALLOY)
                .define('D', Tags.Items.GEMS_DIAMOND)
                .unlockedBy("has_iridium", has(CTags.ItemTags.RAW_MATERIALS_IRIDIUM))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.FUSE.get(), 16)
                .pattern("I")
                .pattern("G")
                .pattern("I")
                .define('I', CTags.ItemTags.INGOTS_REFINED_IRON)
                .define('G', Tags.Items.GLASS_PANES_COLORLESS)
                .unlockedBy("has_refined_iron", has(CTags.ItemTags.INGOTS_REFINED_IRON))
                .save(output);

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(Tags.Items.INGOTS_IRON), RecipeCategory.MISC, IRItems.REFINED_IRON_INGOT, 0.7f, 100)
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .save(output, rl("refined_iron_blasting"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Tags.Items.INGOTS_IRON), RecipeCategory.MISC, IRItems.REFINED_IRON_INGOT, 0.7f, 200)
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .save(output, rl("refined_iron_smelting"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(IRItems.STICKY_RESIN), RecipeCategory.MISC, IRItems.RUBBER, 0.7f, 200)
                .unlockedBy("has_rubber", has(IRItems.STICKY_RESIN))
                .save(output);

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(CTags.ItemTags.DUSTS_BRONZE), RecipeCategory.MISC, IRItems.BRONZE_INGOT, 0.7f, 200)
                .unlockedBy("has_bronze_dust", has(CTags.ItemTags.DUSTS_BRONZE))
                .save(output);

        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.BRONZE_INGOT, RecipeCategory.BUILDING_BLOCKS, IRBlocks.BRONZE_BLOCK);
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.URANIUM_INGOT, RecipeCategory.BUILDING_BLOCKS, IRBlocks.URANIUM_BLOCK);
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.TIN_INGOT, RecipeCategory.BUILDING_BLOCKS, IRBlocks.TIN_BLOCK);

        oreSmelting(output, URANIUM_ORE_SMELTABLES, RecipeCategory.MISC, IRItems.URANIUM_INGOT, 0.7F, 200, "uranium_ingot");
        oreSmelting(output, TIN_ORE_SMELTABLES, RecipeCategory.MISC, IRItems.TIN_INGOT, 0.7F, 200, "tin_ingot");

        oreBlasting(output, URANIUM_ORE_SMELTABLES, RecipeCategory.MISC, IRItems.URANIUM_INGOT, 0.7F, 100, "uranium_ingot");
        oreBlasting(output, TIN_ORE_SMELTABLES, RecipeCategory.MISC, IRItems.TIN_INGOT, 0.7F, 100, "tin_ingot");

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(IRItems.IRON_DUST), RecipeCategory.MISC, Items.IRON_INGOT, 0.7f, 200)
                .unlockedBy("has_iron_dust", has(IRItems.IRON_DUST))
                .save(output, IndustrialRecrafted.rl("iron_ingot_smelting_from_dust"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(IRItems.COPPER_DUST), RecipeCategory.MISC, Items.COPPER_INGOT, 0.7f, 200)
                .unlockedBy("has_copper_dust", has(IRItems.COPPER_DUST))
                .save(output, IndustrialRecrafted.rl("copper_ingot_smelting_from_dust"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(IRItems.GOLD_DUST), RecipeCategory.MISC, Items.GOLD_INGOT, 0.7f, 200)
                .unlockedBy("has_gold_dust", has(IRItems.GOLD_DUST))
                .save(output, IndustrialRecrafted.rl("gold_ingot_smelting_from_dust"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.MIXED_METAL_INGOT.get())
                .pattern("III")
                .pattern("BBB")
                .pattern("TTT")
                .define('I', CTags.ItemTags.INGOTS_REFINED_IRON)
                .define('B', CTags.ItemTags.INGOTS_BRONZE)
                .define('T', CTags.ItemTags.INGOTS_TIN)
                .unlockedBy("has_bronze", has(CTags.ItemTags.INGOTS_BRONZE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, IRBlocks.STICKY_RESIN_SHEET, 3)
                .pattern("RR")
                .define('R', IRItems.STICKY_RESIN)
                .unlockedBy("has_rubber", has(IRItems.STICKY_RESIN))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, IRBlocks.RUBBER_SHEET, 3)
                .pattern("RR")
                .define('R', IRItems.RUBBER)
                .unlockedBy("has_rubber", has(IRItems.RUBBER))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IRItems.DYNAMITE, 3)
                .requires(Tags.Items.GUNPOWDERS)
                .requires(Tags.Items.STRINGS)
                .requires(Items.PAPER)
                .unlockedBy("has_gunpowder", has(Tags.Items.GUNPOWDERS))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IRItems.REMOTE_DETONATOR)
                .requires(CTags.ItemTags.INGOTS_REFINED_IRON)
                .requires(Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
                .save(output);

        this.recyclerRecipe().save(output, IndustrialRecrafted.rl("scrap_from_recycling"));

        plantBallRecipe(Tags.Items.CROPS, "crops", output);
        plantBallRecipe(ItemTags.SAPLINGS, "sapling", output);
        plantBallRecipe(ItemTags.LEAVES, "leaves", output);

        this.geothermalGeneratorRecipe()
                .component(new FluidInputComponent(FluidTags.LAVA, 1))
                .component(new EnergyOutputComponent(20))
                .save(output, IndustrialRecrafted.rl("geothermal_energy_from_lava"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRMachines.CHARGE_PAD.asItem())
                .pattern(" D ")
                .pattern("BMB")
                .pattern(" C ")
                .define('D', IRItems.DENSE_COPPER_PLATE)
                .define('M', IRBlocks.MACHINE_FRAME)
                .define('B', IRItems.REDSTONE_BATTERY)
                .define('C', IRItems.BASIC_CIRCUIT)
                .unlockedBy("has_dense_copper_plate", has(IRItems.DENSE_COPPER_PLATE))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IRItems.SCRAP)
                .requires(IRBlocks.BURNT_CABLE)
                .unlockedBy("has_burnt_cable", has(IRBlocks.BURNT_CABLE))
                .save(output);

    }

    private void energyStorageUnitCraftingRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRMachines.BATTERY_BOX)
                .pattern("PBP")
                .pattern("BCB")
                .pattern("PBP")
                .define('P', ItemTags.PLANKS)
                .define('C', IRBlocks.COPPER_CABLE)
                .define('B', IRItems.REDSTONE_BATTERY)
                .unlockedBy("has_redstone_battery", has(IRItems.REDSTONE_BATTERY))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRMachines.BASIC_ENERGY_STORAGE_UNIT)
                .pattern("CBC")
                .pattern("BMB")
                .pattern("CBC")
                .define('M', IRBlocks.MACHINE_FRAME)
                .define('C', IRBlocks.GOLD_CABLE)
                .define('B', IRItems.ENERGY_CRYSTAL)
                .unlockedBy("has_energy_crystal", has(IRItems.ENERGY_CRYSTAL))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRMachines.ADVANCED_ENERGY_STORAGE_UNIT)
                .pattern("CBC")
                .pattern("BMB")
                .pattern("CBC")
                .define('M', IRBlocks.ADVANCED_MACHINE_FRAME)
                .define('C', IRBlocks.GLASS_FIBRE_CABLE)
                .define('B', IRItems.LAPOTRON_CRYSTAL)
                .unlockedBy("has_redstone_battery", has(IRItems.LAPOTRON_CRYSTAL))
                .save(output);
    }

    private static void componentsCraftingRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.COAL_BALL)
                .pattern("DDD")
                .pattern("DFD")
                .pattern("DDD")
                .define('D', CTags.ItemTags.DUSTS_COAL)
                .define('F', Items.FLINT)
                .unlockedBy("has_coal_dust", has(CTags.ItemTags.DUSTS_COAL))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.GRAPHENE)
                .pattern("CCC")
                .pattern("COC")
                .pattern("CCC")
                .define('C', IRItems.COMPRESSED_COAL_BALL)
                .define('O', Items.OBSIDIAN)
                .unlockedBy("has_compressed_coal_ball", has(IRItems.COMPRESSED_COAL_BALL))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IRItems.CARBON_FIBER)
                .requires(CTags.ItemTags.DUSTS_COAL)
                .requires(CTags.ItemTags.DUSTS_COAL)
                .requires(CTags.ItemTags.DUSTS_COAL)
                .requires(CTags.ItemTags.DUSTS_COAL)
                .unlockedBy("has_coal_dust", has(CTags.ItemTags.DUSTS_COAL))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IRItems.CARBON_MESH)
                .requires(IRItems.CARBON_FIBER, 2)
                .unlockedBy("has_carbon_fiber", has(IRItems.CARBON_FIBER))
                .save(output);
    }

    private static void explosivesCraftingRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRBlocks.INDUSTRIAL_TNT.get(), 6)
                .pattern("###")
                .pattern("TTT")
                .pattern("###")
                .define('#', Items.FLINT)
                .define('T', Items.TNT)
                .unlockedBy("has_tnt", has(Items.TNT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRBlocks.NUKE)
                .pattern("UCU")
                .pattern("TMT")
                .pattern("UCU")
                .define('T', Items.TNT)
                .define('U', IRItems.QUAD_URANIUM_FUEL_ROD)
                .define('M', IRBlocks.ADVANCED_MACHINE_FRAME)
                .define('C', IRItems.ADVANCED_CIRCUIT)
                .unlockedBy("has_item", has(IRItems.QUAD_URANIUM_FUEL_ROD))
                .save(output);
    }

    private void cableCraftingRecipes(RecipeOutput output) {
        cableRecipe(CTags.ItemTags.INGOTS_TIN, IRBlocks.TIN_CABLE, output);
        cableRecipe(Tags.Items.INGOTS_COPPER, IRBlocks.COPPER_CABLE, output);
        cableRecipe(Tags.Items.INGOTS_GOLD, IRBlocks.GOLD_CABLE, output);
        cableRecipe(CTags.ItemTags.INGOTS_REFINED_IRON, IRBlocks.HV_CABLE, 2, output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRBlocks.GLASS_FIBRE_CABLE, 4)
                .pattern("GGG")
                .pattern("RDR")
                .pattern("GGG")
                .define('G', Tags.Items.GLASS_BLOCKS)
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND))
                .save(output);
    }

    private void machineCraftingRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRMachines.EXTRACTOR.getBlock())
                .pattern("TMT")
                .pattern("TCT")
                .define('T', IRItems.TREETAP)
                .define('M', IRBlocks.MACHINE_FRAME)
                .define('C', IRItems.BASIC_CIRCUIT)
                .unlockedBy("has_basic_circuit", has(IRItems.BASIC_CIRCUIT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRMachines.MACERATOR.getBlock())
                .pattern("FFF")
                .pattern("OMO")
                .pattern(" C ")
                .define('F', Items.FLINT)
                .define('M', IRBlocks.MACHINE_FRAME)
                .define('O', Tags.Items.COBBLESTONES)
                .define('C', IRItems.BASIC_CIRCUIT)
                .unlockedBy("has_basic_circuit", has(IRItems.BASIC_CIRCUIT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRMachines.COMPRESSOR.getBlock())
                .pattern("S S")
                .pattern("SMS")
                .pattern("SCS")
                .define('S', Tags.Items.STONES)
                .define('M', IRBlocks.MACHINE_FRAME)
                .define('C', IRItems.BASIC_CIRCUIT)
                .unlockedBy("has_basic_circuit", has(IRItems.BASIC_CIRCUIT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRMachines.RECYCLER.getBlock())
                .pattern("IOI")
                .pattern("ICI")
                .define('I', CTags.ItemTags.INGOTS_REFINED_IRON)
                .define('O', Blocks.COMPOSTER)
                .define('C', IRMachines.COMPRESSOR)
                .unlockedBy("has_compressor", has(IRMachines.COMPRESSOR))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRMachines.CANNING_MACHINE.getBlock())
                .pattern("AMA")
                .pattern("ACA")
                .define('A', IRItems.FLUID_CELL)
                .define('M', IRBlocks.MACHINE_FRAME)
                .define('C', IRItems.BASIC_CIRCUIT)
                .unlockedBy("has_basic_circuit", has(IRItems.BASIC_CIRCUIT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRMachines.ELECTRIC_FURNACE.getBlockItem())
                .pattern(" F ")
                .pattern("RMR")
                .pattern(" C ")
                .define('C', IRItems.BASIC_CIRCUIT)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('F', Blocks.FURNACE)
                .define('M', IRBlocks.MACHINE_FRAME)
                .unlockedBy("has_basic_circuit", has(IRItems.BASIC_CIRCUIT))
                .save(output);
    }

    private void reinforcedBlocksCraftingRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, IRBlocks.REINFORCED_STONE, 8)
                .pattern("SSS")
                .pattern("SAS")
                .pattern("SSS")
                .define('S', Tags.Items.STONES)
                .define('A', CTags.ItemTags.PLATES_ADVANCED_ALLOY)
                .unlockedBy("has_advanced_alloy_plate", has(CTags.ItemTags.PLATES_ADVANCED_ALLOY))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, IRBlocks.REINFORCED_GLASS, 8)
                .pattern("GGG")
                .pattern("GAG")
                .pattern("GGG")
                .define('G', Tags.Items.GLASS_BLOCKS)
                .define('A', CTags.ItemTags.PLATES_ADVANCED_ALLOY)
                .unlockedBy("has_advanced_alloy_plate", has(CTags.ItemTags.PLATES_ADVANCED_ALLOY))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, IRBlocks.REINFORCED_DOOR, 3)
                .pattern("SS")
                .pattern("SS")
                .pattern("SS")
                .define('S', IRBlocks.REINFORCED_STONE)
                .unlockedBy("has_reinforced_stone", has(IRBlocks.REINFORCED_STONE))
                .save(output);
    }

    private void toolCraftingRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, IRItems.WRENCH.get())
                .pattern(" # ")
                .pattern(" ##")
                .pattern("#  ")
                .define('#', CTags.ItemTags.INGOTS_BRONZE)
                .unlockedBy("has_bronze", has(CTags.ItemTags.INGOTS_BRONZE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, IRItems.TREETAP.get())
                .pattern(" S ")
                .pattern("###")
                .pattern("#  ")
                .define('S', Tags.Items.RODS_WOODEN)
                .define('#', ItemTags.PLANKS)
                .unlockedBy("has_bronze", has(Tags.Items.RODS_WOODEN))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, IRItems.CUTTER.get())
                .pattern("# #")
                .pattern(" # ")
                .pattern("S S")
                .define('S', Tags.Items.RODS_WOODEN)
                .define('#', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .save(output);
    }

    private void batteryCraftingRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.REDSTONE_BATTERY.get())
                .pattern(" C ")
                .pattern("TRT")
                .pattern("TRT")
                .define('C', IRBlocks.COPPER_CABLE)
                .define('T', CTags.ItemTags.INGOTS_TIN)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("has_copper_cable", has(IRBlocks.COPPER_CABLE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.ENERGY_CRYSTAL)
                .pattern("RRR")
                .pattern("RDR")
                .pattern("RRR")
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('D', Tags.Items.GEMS_DIAMOND)
                .unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.LAPOTRON_CRYSTAL)
                .pattern("LAL")
                .pattern("LEL")
                .pattern("LAL")
                .define('L', Tags.Items.GEMS_LAPIS)
                .define('A', IRItems.ADVANCED_CIRCUIT)
                .define('E', IRItems.ENERGY_CRYSTAL)
                .unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND))
                .save(output);
    }

    private void reactorComponentCraftingRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, IRItems.DOUBLE_URANIUM_FUEL_ROD.get())
                .pattern("UDU")
                .define('D', IRItems.DENSE_COPPER_PLATE)
                .define('U', IRItems.SINGLE_URANIUM_FUEL_ROD)
                .unlockedBy("has_uranium_fuel_rod", has(IRItems.SINGLE_URANIUM_FUEL_ROD))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, IRItems.QUAD_URANIUM_FUEL_ROD.get())
                .pattern("UDU")
                .define('D', IRItems.DENSE_COPPER_PLATE)
                .define('U', IRItems.DOUBLE_URANIUM_FUEL_ROD)
                .unlockedBy("has_uranium_fuel_rod", has(IRItems.DOUBLE_URANIUM_FUEL_ROD))
                .save(output);
    }

    private void circuitCraftingRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.BASIC_CIRCUIT.get())
                .pattern("CCC")
                .pattern("RIR")
                .pattern("CCC")
                .define('C', IRBlocks.COPPER_CABLE)
                .define('I', CTags.ItemTags.INGOTS_REFINED_IRON)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("has_copper_cable", has(IRBlocks.COPPER_CABLE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.ADVANCED_CIRCUIT.get())
                .pattern("RGR")
                .pattern("LCL")
                .pattern("RGR")
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('G', Tags.Items.DUSTS_GLOWSTONE)
                .define('L', Tags.Items.GEMS_LAPIS)
                .define('C', IRItems.BASIC_CIRCUIT)
                .unlockedBy("has_basic_circuit", has(IRItems.BASIC_CIRCUIT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.ADVANCED_CIRCUIT.get())
                .pattern("RLR")
                .pattern("GCG")
                .pattern("RLR")
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('G', Tags.Items.DUSTS_GLOWSTONE)
                .define('L', Tags.Items.GEMS_LAPIS)
                .define('C', IRItems.BASIC_CIRCUIT)
                .unlockedBy("has_basic_circuit", has(IRItems.BASIC_CIRCUIT))
                .save(output, IndustrialRecrafted.rl("advanced_circuit1"));
    }

    private void machineFrameCraftingRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRBlocks.MACHINE_FRAME.get())
                .pattern("###")
                .pattern("#F#")
                .pattern("###")
                .define('F', IRItems.FUSE)
                .define('#', CTags.ItemTags.INGOTS_REFINED_IRON)
                .unlockedBy("has_refined_iron", has(CTags.ItemTags.INGOTS_REFINED_IRON))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRBlocks.ADVANCED_MACHINE_FRAME.get())
                .pattern(" C ")
                .pattern("AMA")
                .pattern(" C ")
                .define('C', CTags.ItemTags.PLATES_CARBON)
                .define('A', CTags.ItemTags.PLATES_ADVANCED_ALLOY)
                .define('M', IRBlocks.MACHINE_FRAME)
                .unlockedBy("has_advanced_alloy_plate", has(CTags.ItemTags.PLATES_ADVANCED_ALLOY))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRBlocks.ADVANCED_MACHINE_FRAME.get())
                .pattern(" A ")
                .pattern("CMC")
                .pattern(" A ")
                .define('C', CTags.ItemTags.PLATES_CARBON)
                .define('A', CTags.ItemTags.PLATES_ADVANCED_ALLOY)
                .define('M', IRBlocks.MACHINE_FRAME)
                .unlockedBy("has_advanced_alloy_plate", has(IRItems.ADVANCED_ALLOY_PLATE))
                .save(output, IndustrialRecrafted.rl("advanced_machine_frame1"));
    }

    private void electricToolCraftingRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.BASIC_DRILL.get())
                .pattern(" I ")
                .pattern("ICI")
                .pattern("IBI")
                .define('I', CTags.ItemTags.INGOTS_REFINED_IRON)
                .define('C', IRItems.BASIC_CIRCUIT)
                .define('B', IRItems.REDSTONE_BATTERY)
                .unlockedBy("has_circuit", has(IRItems.BASIC_CIRCUIT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.ADVANCED_DRILL.get())
                .pattern(" D ")
                .pattern("DRD")
                .pattern(" C ")
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('C', IRItems.ADVANCED_CIRCUIT)
                .define('R', IRItems.BASIC_DRILL)
                .unlockedBy("has_basic_drill", has(IRItems.BASIC_DRILL))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.BASIC_CHAINSAW.get())
                .pattern(" II")
                .pattern("ICI")
                .pattern("BI ")
                .define('I', CTags.ItemTags.INGOTS_REFINED_IRON)
                .define('C', IRItems.BASIC_CIRCUIT)
                .define('B', IRItems.REDSTONE_BATTERY)
                .unlockedBy("has_circuit", has(IRItems.BASIC_CIRCUIT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.ADVANCED_CHAINSAW.get())
                .pattern(" D ")
                .pattern("DHD")
                .pattern(" C ")
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('C', IRItems.ADVANCED_CIRCUIT)
                .define('H', IRItems.BASIC_CHAINSAW)
                .unlockedBy("has_basic_chainsaw", has(IRItems.BASIC_CHAINSAW))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.NANO_SABER.get())
                .pattern("GA ")
                .pattern("GA ")
                .pattern("CEC")
                .define('G', Tags.Items.DUSTS_GLOWSTONE)
                .define('A', CTags.ItemTags.PLATES_ADVANCED_ALLOY)
                .define('C', CTags.ItemTags.PLATES_CARBON)
                .define('E', IRItems.ENERGY_CRYSTAL)
                .unlockedBy("has_energy_crystal", has(IRItems.ENERGY_CRYSTAL))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.MINING_LASER.get())
                .pattern("RRE")
                .pattern("AAC")
                .pattern(" AA")
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('A', CTags.ItemTags.PLATES_ADVANCED_ALLOY)
                .define('C', IRItems.ADVANCED_CIRCUIT)
                .define('E', IRItems.ENERGY_CRYSTAL)
                .unlockedBy("has_energy_crystal", has(IRItems.ENERGY_CRYSTAL))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IRItems.ELECTRIC_HOE.get())
                .requires(IRItems.REDSTONE_BATTERY)
                .requires(ItemTags.HOES)
                .unlockedBy("has_redstone_battery", has(IRItems.REDSTONE_BATTERY))
                .save(output);

//        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IRItems.ELECTRIC_WRENCH.get())
//                .requires(IRItems.REDSTONE_BATTERY)
//                .requires(IRItems.WRENCH)
//                .unlockedBy("has_redstone_battery", has(IRItems.REDSTONE_BATTERY))
//                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IRItems.ELECTRIC_TREETAP.get())
                .requires(IRItems.REDSTONE_BATTERY)
                .requires(IRItems.TREETAP)
                .unlockedBy("has_redstone_battery", has(IRItems.REDSTONE_BATTERY))
                .save(output);
    }

    private void canningRecipes(RecipeOutput output) {
        this.canningMachineRecipe()
                .component(new ItemInputListComponent(Ingredient.of(IRItems.FLUID_CELL), Ingredient.of(CTags.ItemTags.INGOTS_URANIUM)))
                .component(new ItemOutputComponent(IRItems.SINGLE_URANIUM_FUEL_ROD))
                .component(new TimeComponent(200))
                .component(new EnergyInputComponent(800))
                .save(output);

        this.canningMachineRecipe()
                .component(new EnumRecipeComponent<>(CanningMachineRecipeLayout.Variant.FOOD_CANNING))
                .component(new TimeComponent(200))
                .component(new EnergyInputComponent(800))
                .save(output, IndustrialRecrafted.rl("food_canning"));

        this.canningMachineRecipe()
                .component(new ItemInputListComponent(new ItemInputComponent(IRItems.FLUID_CELL), new ItemInputComponent(IRItems.PLANT_BALL)))
                .component(new ItemOutputComponent(ItemStackBuilder.of(IRItems.FLUID_CELL)
                        .component(PDLDataComponents.FLUID, SimpleFluidContent.copyOf(IRFluids.BIO_FUEL.toStack()))
                        .build()))
                .component(new TimeComponent(200))
                .component(new EnergyInputComponent(800))
                .save(output);

    }

    private void extractingRecipes(RecipeOutput output) {
        this.extractorRecipe()
                .component(new ItemInputComponent(IRItems.STICKY_RESIN))
                .component(new ItemOutputListComponent(IRItems.RUBBER, 3))
                .component(new TimeComponent(200))
                .component(new EnergyInputComponent(800))
                .save(output, IndustrialRecrafted.rl("sticky_resin_extracting"));

        this.extractorRecipe()
                .component(new ItemInputComponent(Items.MAGMA_CREAM))
                .component(new ItemOutputListComponent(new ItemOutputComponent(Items.SLIME_BALL), new ItemOutputComponent(Items.BLAZE_POWDER, 0.25f)))
                .component(new TimeComponent(200))
                .component(new EnergyInputComponent(800))
                .save(output);
        this.extractorRecipe()
                .component(new ItemInputComponent(Items.SEA_PICKLE))
                .component(new ItemOutputListComponent(new ItemOutputComponent(Items.SEAGRASS), new ItemOutputComponent(Items.GLOWSTONE_DUST, 0.25f)))
                .component(new TimeComponent(200))
                .component(new EnergyInputComponent(800))
                .save(output);

        this.extractorRecipe()
                .component(new ItemInputComponent(IRBlocks.RUBBER_TREE_SAPLING))
                .component(new ItemOutputListComponent(IRItems.STICKY_RESIN))
                .component(new TimeComponent(200))
                .component(new EnergyInputComponent(800))
                .save(output, IndustrialRecrafted.rl("sticky_resin_from_sapling"));

        this.extractorRecipe()
                .component(new ItemInputComponent(IRBlocks.RUBBER_TREE_LEAVES))
                .component(new ItemOutputListComponent(new ItemOutputComponent(IRItems.STICKY_RESIN, 0.35f), new ItemOutputComponent(IRBlocks.RUBBER_TREE_SAPLING, 0.05f)))
                .component(new TimeComponent(200))
                .component(new EnergyInputComponent(800))
                .save(output, IndustrialRecrafted.rl("sticky_resin_from_leaves"));

        this.extractorRecipe()
                .component(new ItemInputComponent(IRBlocks.RUBBER_TREE_LOG))
                .component(new ItemOutputListComponent(new ItemOutputComponent(IRItems.STICKY_RESIN, 0.8f)))
                .component(new TimeComponent(200))
                .component(new EnergyInputComponent(800))
                .save(output, IndustrialRecrafted.rl("sticky_resin_from_log"));

        this.extractorRecipe()
                .component(new ItemInputComponent(Items.SUGAR_CANE))
                .component(new ItemOutputListComponent(Items.SUGAR, 2))
                .component(new TimeComponent(200))
                .component(new EnergyInputComponent(800))
                .save(output);

        this.extractorRecipe()
                .component(new ItemInputComponent(Items.GRAVEL))
                .component(new ItemOutputListComponent(Items.FLINT))
                .component(new TimeComponent(200))
                .component(new EnergyInputComponent(800))
                .save(output);
    }

    private void compressingRecipes(RecipeOutput output) {
        this.compressorRecipe(IRItems.MIXED_METAL_INGOT, IRItems.ADVANCED_ALLOY_PLATE, output);
        this.compressorRecipe(IRItems.IRIDIUM_ALLOY_INGOT, IRItems.IRIDIUM_INGOT, 800, 10, output);
        this.compressorRecipe(IRItems.CARBON_MESH, IRItems.CARBON_PLATE, output);

        this.compressorRecipe(CTags.ItemTags.INGOTS_TIN, IRItems.TIN_PLATE, output);
        this.compressorRecipe(Tags.Items.INGOTS_COPPER, IRItems.COPPER_PLATE, output);

        this.compressorRecipe(Ingredient.of(IRItems.COPPER_PLATE), 8, IRItems.DENSE_COPPER_PLATE, 1, 200, 4, output);

        this.compressorRecipe(Ingredient.of(Tags.Items.CROPS_SUGAR_CANE), 3, Items.PAPER, 5, 200, 4, output);
        this.compressorRecipe(DataComponentIngredient.of(false, PDLDataComponents.FLUID, SimpleFluidContent.copyOf(new FluidStack(Fluids.WATER, 1000)), IRItems.FLUID_CELL, IRItems.JETPACK), 1, Items.SNOWBALL, 1, 200, 4, output);

        this.compressorRecipe(Ingredient.of(Blocks.SNOW_BLOCK), 4, Items.ICE, 1, 200, 4, output);

        this.compressorRecipe()
                .component(new ItemInputComponent(IRItems.COAL_BALL))
                .component(new ItemOutputComponent(IRItems.COMPRESSED_COAL_BALL))
                .component(new TimeComponent(200))
                .component(new EnergyInputComponent(800))
                .save(output);

        this.compressorRecipe()
                .component(new ItemInputComponent(IRItems.GRAPHENE))
                .component(new ItemOutputComponent(Items.DIAMOND))
                .component(new TimeComponent(200))
                .component(new EnergyInputComponent(800))
                .save(output);
    }

    private void maceratingRecipes(RecipeOutput output) {
        this.maceratorRecipe(CTags.ItemTags.INGOTS_TIN, IRItems.TIN_DUST, output);
        this.maceratorRecipe(CTags.ItemTags.INGOTS_BRONZE, IRItems.BRONZE_DUST, output);
        this.maceratorRecipe(Tags.Items.INGOTS_COPPER, IRItems.COPPER_DUST, output);
        this.maceratorRecipe(Tags.Items.INGOTS_GOLD, IRItems.GOLD_DUST, output);
        this.maceratorRecipe(Tags.Items.INGOTS_IRON, IRItems.IRON_DUST, output);
        this.maceratorRecipe(ItemTags.COALS, IRItems.COAL_DUST, output);

        this.maceratorRecipe(ItemTags.WOOL, Items.STRING, 4, output);
        this.maceratorRecipe(Tags.Items.BONES, Items.BONE_MEAL, 5, output);
        this.maceratorRecipe(Tags.Items.RODS_BLAZE, Items.BLAZE_POWDER, 5, output);

        this.maceratorRecipe(Tags.Items.STONES, Items.COBBLESTONE, output);
        this.maceratorRecipe(Tags.Items.COBBLESTONES, Items.GRAVEL, output);
        this.maceratorRecipe(Tags.Items.GRAVELS, Items.SAND, output);
        this.maceratorRecipe(Tags.Items.SANDSTONE_BLOCKS, Items.SAND, output, IndustrialRecrafted.rl("sand_from_sandstone"));

        this.maceratorRecipe(Blocks.MELON, Items.MELON_SLICE, 8, output);
        this.maceratorRecipe(Blocks.PUMPKIN, Items.PUMPKIN_SEEDS, 4, output);

        oreMacerationRecipe(Tags.Items.ORES_COPPER, Tags.Items.RAW_MATERIALS_COPPER, IRItems.COPPER_DUST, "copper", output);
        oreMacerationRecipe(Tags.Items.ORES_IRON, Tags.Items.RAW_MATERIALS_IRON, IRItems.IRON_DUST, "iron", output);
        oreMacerationRecipe(Tags.Items.ORES_GOLD, Tags.Items.RAW_MATERIALS_GOLD, IRItems.GOLD_DUST, "gold", output);
        oreMacerationRecipe(CTags.ItemTags.ORES_TIN, CTags.ItemTags.RAW_MATERIALS_TIN, IRItems.TIN_DUST, "tin", output);

        this.maceratorRecipe(Tags.Items.ORES_COAL, Items.COAL, 3, output);
        this.maceratorRecipe(Tags.Items.ORES_LAPIS, Items.LAPIS_LAZULI, 12, output);
        this.maceratorRecipe(Tags.Items.ORES_REDSTONE, Items.REDSTONE, 10, output);

        this.maceratorRecipe(Blocks.GLOWSTONE, Items.GLOWSTONE_DUST, 4, output);

        this.maceratorRecipe()
                .component(new ItemInputComponent(IRBlocks.IRIDIUM_ORE))
                .component(new ItemOutputListComponent(new ItemOutputComponent(IRItems.RAW_IRIDIUM), new ItemOutputComponent(IRItems.RAW_IRIDIUM, 0.5f)))
                .component(new TimeComponent(800))
                .component(new EnergyInputComponent(3200))
                .save(output);

        this.maceratorRecipe()
                .component(new ItemInputComponent(IRBlocks.DEEPSLATE_IRIDIUM_ORE))
                .component(new ItemOutputListComponent(new ItemOutputComponent(IRItems.RAW_IRIDIUM), new ItemOutputComponent(IRItems.RAW_IRIDIUM, 0.5f)))
                .component(new TimeComponent(800))
                .component(new EnergyInputComponent(3200))
                .save(output, IndustrialRecrafted.rl("raw_iridium_from_deepslate_iridium_ore"));
    }

    private static void plantBallRecipe(TagKey<Item> plantTag, String group, RecipeOutput output) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IRItems.PLANT_BALL)
                .requires(plantTag)
                .requires(plantTag)
                .requires(plantTag)
                .requires(plantTag)
                .unlockedBy("has_plant", has(plantTag))
                .save(output, IndustrialRecrafted.rl("plant_ball_from_" + group));
    }

    private static void cableRecipe(TagKey<Item> ingotTag, ItemLike cableBlock, RecipeOutput output) {
        cableRecipe(ingotTag, cableBlock, 6, output);
    }

    private static void cableRecipe(TagKey<Item> ingotTag, ItemLike cableBlock, int count, RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, cableBlock, count)
                .pattern("RRR")
                .pattern("III")
                .pattern("RRR")
                .define('R', IRItems.RUBBER)
                .define('I', ingotTag)
                .unlockedBy("has_rubber", has(IRItems.RUBBER))
                .save(output);
    }

    private void oreMacerationRecipe(TagKey<Item> oreTag, TagKey<Item> rawTag, ItemLike result, String oreName, RecipeOutput output) {
        this.maceratorRecipe()
                .component(new ItemInputComponent(oreTag))
                .component(new ItemOutputListComponent(result, 2))
                .component(new TimeComponent(200))
                .component(new EnergyInputComponent(800))
                .save(output, IndustrialRecrafted.rl(oreName + "_dust_from_ore_maceration"));
        this.maceratorRecipe()
                .component(new ItemInputComponent(rawTag))
                .component(new ItemOutputListComponent(new ItemOutputComponent(result), new ItemOutputComponent(result, 0.33f)))
                .component(new TimeComponent(200))
                .component(new EnergyInputComponent(800))
                .save(output, IndustrialRecrafted.rl(oreName + "_dust_from_raw_ore_maceration"));
    }

    private static void armorCraftingRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.NANO_HELMET.get())
                .pattern("CEC")
                .pattern("C C")
                .define('C', CTags.ItemTags.PLATES_CARBON)
                .define('E', IRItems.ENERGY_CRYSTAL)
                .unlockedBy("has_energy_crystal", has(IRItems.ENERGY_CRYSTAL))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.NANO_CHESTPLATE.get())
                .pattern("C C")
                .pattern("CEC")
                .pattern("CCC")
                .define('C', CTags.ItemTags.PLATES_CARBON)
                .define('E', IRItems.ENERGY_CRYSTAL)
                .unlockedBy("has_energy_crystal", has(IRItems.ENERGY_CRYSTAL))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.NANO_LEGGINGS.get())
                .pattern("CCC")
                .pattern("E E")
                .pattern("C C")
                .define('C', CTags.ItemTags.PLATES_CARBON)
                .define('E', IRItems.ENERGY_CRYSTAL)
                .unlockedBy("has_energy_crystal", has(IRItems.ENERGY_CRYSTAL))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.NANO_BOOTS.get())
                .pattern("E E")
                .pattern("C C")
                .define('C', CTags.ItemTags.PLATES_CARBON)
                .define('E', IRItems.ENERGY_CRYSTAL)
                .unlockedBy("has_energy_crystal", has(IRItems.ENERGY_CRYSTAL))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.QUANTUM_HELMET.get())
                .pattern(" N ")
                .pattern("ILI")
                .pattern("AGA")
                .define('N', IRItems.NANO_HELMET)
                .define('I', IRItems.IRIDIUM_INGOT)
                .define('L', IRItems.LAPOTRON_CRYSTAL)
                .define('A', IRItems.ADVANCED_CIRCUIT)
                .define('G', IRBlocks.REINFORCED_GLASS)
                .unlockedBy("has_iridium_ingot", has(IRItems.IRIDIUM_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.QUANTUM_CHESTPLATE.get())
                .pattern("ANA")
                .pattern("ILI")
                .pattern("IAI")
                .define('N', IRItems.NANO_CHESTPLATE)
                .define('I', IRItems.IRIDIUM_INGOT)
                .define('L', IRItems.LAPOTRON_CRYSTAL)
                .define('A', CTags.ItemTags.PLATES_ADVANCED_ALLOY)
                .unlockedBy("has_iridium_ingot", has(IRItems.IRIDIUM_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.QUANTUM_LEGGINGS.get())
                .pattern("MLM")
                .pattern("INI")
                .pattern("G G")
                .define('N', IRItems.NANO_LEGGINGS)
                .define('I', IRItems.IRIDIUM_INGOT)
                .define('L', IRItems.LAPOTRON_CRYSTAL)
                .define('M', IRBlocks.ADVANCED_MACHINE_FRAME)
                .define('G', Tags.Items.DUSTS_GLOWSTONE)
                .unlockedBy("has_iridium_ingot", has(IRItems.IRIDIUM_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.QUANTUM_BOOTS.get())
                .pattern("INI")
                .pattern("BLB")
                .define('N', IRItems.NANO_BOOTS)
                .define('I', IRItems.IRIDIUM_INGOT)
                .define('L', IRItems.LAPOTRON_CRYSTAL)
                .define('B', Items.LEATHER_BOOTS)
                .unlockedBy("has_iridium_ingot", has(IRItems.IRIDIUM_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, IRItems.JETPACK.get())
                .pattern("PCP")
                .pattern("PBP")
                .pattern("R R")
                .define('P', CTags.ItemTags.INGOTS_REFINED_IRON)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('C', IRItems.BASIC_CIRCUIT)
                .define('B', IRItems.FLUID_CELL)
                .unlockedBy("has_circuit", has(IRItems.BASIC_CIRCUIT.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, IRItems.ELECTRIC_JETPACK.get())
                .pattern("PCP")
                .pattern("PBP")
                .pattern("R R")
                .define('P', CTags.ItemTags.PLATES_ADVANCED_ALLOY)
                .define('R', Tags.Items.DUSTS_GLOWSTONE)
                .define('C', IRItems.ADVANCED_CIRCUIT)
                .define('B', IRItems.REDSTONE_BATTERY)
                .unlockedBy("has_circuit", has(IRItems.ADVANCED_CIRCUIT.get()))
                .save(output);
    }

    private void rubberWoodCraftingRecipes(RecipeOutput output) {
        planksFromLog(output, IRBlocks.RUBBER_TREE_PLANKS, IRTags.ItemTags.RUBBER_LOGS, 4);
        slab(output, RecipeCategory.BUILDING_BLOCKS, IRBlocks.RUBBER_TREE_SLAB, IRBlocks.RUBBER_TREE_PLANKS);
        pressurePlate(output, IRBlocks.RUBBER_TREE_PRESSURE_PLATE, IRBlocks.RUBBER_TREE_PLANKS);
        woodFromLogs(output, IRBlocks.RUBBER_TREE_WOOD, IRBlocks.RUBBER_TREE_LOG);
        woodFromLogs(output, IRBlocks.STRIPPED_RUBBER_TREE_WOOD, IRBlocks.STRIPPED_RUBBER_TREE_WOOD);
        stairBuilder(IRBlocks.RUBBER_TREE_STAIRS, Ingredient.of(IRBlocks.RUBBER_TREE_PLANKS))
                .unlockedBy("has_planks", has(IRBlocks.RUBBER_TREE_PLANKS))
                .save(output);
        buttonBuilder(IRBlocks.RUBBER_TREE_BUTTON, Ingredient.of(IRBlocks.RUBBER_TREE_PLANKS))
                .unlockedBy("has_planks", has(IRBlocks.RUBBER_TREE_PLANKS))
                .save(output);
        fenceBuilder(IRBlocks.RUBBER_TREE_FENCE, Ingredient.of(IRBlocks.RUBBER_TREE_PLANKS))
                .unlockedBy("has_planks", has(IRBlocks.RUBBER_TREE_PLANKS))
                .save(output);
        fenceGateBuilder(IRBlocks.RUBBER_TREE_FENCE_GATE, Ingredient.of(IRBlocks.RUBBER_TREE_PLANKS))
                .unlockedBy("has_planks", has(IRBlocks.RUBBER_TREE_PLANKS))
                .save(output);
        trapdoorBuilder(IRBlocks.RUBBER_TREE_TRAPDOOR, Ingredient.of(IRBlocks.RUBBER_TREE_PLANKS))
                .unlockedBy("has_planks", has(IRBlocks.RUBBER_TREE_PLANKS))
                .save(output);
        doorBuilder(IRBlocks.RUBBER_TREE_DOOR, Ingredient.of(IRBlocks.RUBBER_TREE_PLANKS))
                .unlockedBy("has_planks", has(IRBlocks.RUBBER_TREE_PLANKS))
                .save(output);
    }

    private MachineRecipeBuilder compressorRecipe() {
        return new MachineRecipeBuilder(IRRecipeLayouts.COMPRESSOR);
    }

    private MachineRecipeBuilder maceratorRecipe() {
        return new MachineRecipeBuilder(IRRecipeLayouts.MACERATOR);
    }

    private MachineRecipeBuilder extractorRecipe() {
        return new MachineRecipeBuilder(IRRecipeLayouts.EXTRACTOR);
    }

    private MachineRecipeBuilder recyclerRecipe() {
        return new MachineRecipeBuilder(IRRecipeLayouts.RECYCLER);
    }

    private MachineRecipeBuilder geothermalGeneratorRecipe() {
        return new MachineRecipeBuilder(IRRecipeLayouts.GEOTHERMAL_GENERATOR);
    }

    private MachineRecipeBuilder canningMachineRecipe() {
        return new MachineRecipeBuilder(IRRecipeLayouts.CANNING_MACHINE);
    }

    private static void generatorCraftingRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRMachines.BASIC_SOLAR_PANEL.getBlockItem())
                .pattern("CGC")
                .pattern("GCG")
                .pattern("IBI")
                .define('C', CTags.ItemTags.DUSTS_COAL)
                .define('G', Tags.Items.GLASS_BLOCKS)
                .define('I', IRItems.BASIC_CIRCUIT)
                .define('B', IRMachines.BASIC_GENERATOR.getBlockItem())
                .unlockedBy("has_basic_circuit", has(IRItems.BASIC_CIRCUIT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRMachines.BASIC_GENERATOR.getBlock())
                .pattern("B")
                .pattern("M")
                .pattern("F")
                .define('B', IRItems.REDSTONE_BATTERY)
                .define('M', IRBlocks.MACHINE_FRAME)
                .define('F', Blocks.FURNACE)
                .unlockedBy("has_redstone_battery", has(IRItems.REDSTONE_BATTERY))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRMachines.GEOTHERMAL_GENERATOR.getBlock())
                .pattern("GCG")
                .pattern("GCG")
                .pattern("IBI")
                .define('B', IRMachines.BASIC_GENERATOR.getBlock())
                .define('I', IRItems.REFINED_IRON_INGOT)
                .define('G', Tags.Items.GLASS_BLOCKS)
                .define('C', IRItems.FLUID_CELL)
                .unlockedBy("has_basic_generator", has(IRMachines.BASIC_GENERATOR.getBlock()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRBlocks.NUCLEAR_REACTOR_CHAMBER)
                .pattern(" C ")
                .pattern("CMC")
                .pattern(" C ")
                .define('M', IRBlocks.ADVANCED_MACHINE_FRAME)
                .define('C', IRItems.DENSE_COPPER_PLATE)
                .unlockedBy("has_dense_copper_plates", has(IRItems.DENSE_COPPER_PLATE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRMachines.NUCLEAR_REACTOR)
                .pattern(" C ")
                .pattern("RRR")
                .pattern(" G ")
                .define('G', IRMachines.BASIC_GENERATOR)
                .define('C', IRItems.ADVANCED_CIRCUIT)
                .define('R', IRBlocks.NUCLEAR_REACTOR_CHAMBER)
                .unlockedBy("has_nuclear_reactor_chamber", has(IRBlocks.NUCLEAR_REACTOR_CHAMBER))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRMachines.WATER_MILL.getBlock())
                .pattern(" P ")
                .pattern("PGP")
                .pattern("CPC")
                .define('P', ItemTags.PLANKS)
                .define('G', IRMachines.BASIC_GENERATOR)
                .define('C', IRItems.BASIC_CIRCUIT)
                .unlockedBy("has_basic_generator", has(IRMachines.BASIC_GENERATOR))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRMachines.WIND_MILL.getBlock())
                .pattern(" P ")
                .pattern("PGP")
                .pattern("CPC")
                .define('P', CTags.ItemTags.INGOTS_REFINED_IRON)
                .define('G', IRMachines.BASIC_GENERATOR)
                .define('C', IRItems.ADVANCED_CIRCUIT)
                .unlockedBy("has_basic_generator", has(IRMachines.BASIC_GENERATOR))
                .save(output);

    }
}
