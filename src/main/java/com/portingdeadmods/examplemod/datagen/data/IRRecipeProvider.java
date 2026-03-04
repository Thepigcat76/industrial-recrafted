package com.portingdeadmods.examplemod.datagen.data;

import com.portingdeadmods.examplemod.content.recipes.components.EnumRecipeComponent;
import com.portingdeadmods.examplemod.content.recipes.components.energy.EnergyOutputComponent;
import com.portingdeadmods.examplemod.content.recipes.components.fluids.FluidInputComponent;
import com.portingdeadmods.examplemod.content.recipes.layouts.CanningMachineRecipeLayout;
import com.portingdeadmods.examplemod.tags.IRTags;
import com.portingdeadmods.examplemod.IndustrialReclassified;
import com.portingdeadmods.examplemod.content.recipes.components.energy.EnergyInputComponent;
import com.portingdeadmods.examplemod.content.recipes.components.TimeComponent;
import com.portingdeadmods.examplemod.content.recipes.components.items.ItemInputComponent;
import com.portingdeadmods.examplemod.content.recipes.components.items.ItemInputListComponent;
import com.portingdeadmods.examplemod.content.recipes.components.items.ItemOutputComponent;
import com.portingdeadmods.examplemod.content.recipes.components.items.ItemOutputListComponent;
import com.portingdeadmods.examplemod.datagen.MachineRecipeBuilder;
import com.portingdeadmods.examplemod.registries.IRBlocks;
import com.portingdeadmods.examplemod.registries.IRItems;
import com.portingdeadmods.examplemod.registries.IRMachines;
import com.portingdeadmods.examplemod.registries.IRRecipeLayouts;
import com.portingdeadmods.examplemod.tags.CTags;
import net.minecraft.client.gui.screens.recipebook.SmeltingRecipeBookComponent;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.portingdeadmods.examplemod.IndustrialReclassified.rl;

public class IRRecipeProvider extends RecipeProvider {

    public static final List<ItemLike> URANIUM_ORE_SMELTABLES = List.of(IRItems.RAW_URANIUM, IRBlocks.URANIUM_ORE, IRBlocks.DEEPSLATE_URANIUM_ORE);
    public static final List<ItemLike> TIN_ORE_SMELTABLES = List.of(IRItems.RAW_TIN, IRBlocks.TIN_ORE, IRBlocks.DEEPSLATE_TIN_ORE);

    public IRRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output0) {
        RecipeOutput output = new IRRecipeOutput(output0);

        super.buildRecipes(output);

        this.compressorRecipe()
                .component(new ItemInputComponent(IRItems.MIXED_METAL_INGOT))
                .component(new ItemOutputComponent(IRItems.ADVANCED_ALLOY_PLATE))
                .component(new TimeComponent(200))
                .component(new EnergyInputComponent(800))
                .save(output);
        this.compressorRecipe()
                .component(new ItemInputComponent(IRItems.IRIDIUM_ALLOY_INGOT))
                .component(new ItemOutputComponent(IRItems.IRIDIUM_INGOT))
                .component(new TimeComponent(800))
                .component(new EnergyInputComponent(3200))
                .save(output);
        this.compressorRecipe()
                .component(new ItemInputComponent(IRItems.CARBON_MESH))
                .component(new ItemOutputComponent(IRItems.CARBON_PLATE))
                .component(new TimeComponent(200))
                .component(new EnergyInputComponent(800))
                .save(output);
        this.compressorRecipe()
                .component(new ItemInputComponent(IRItems.TIN_INGOT))
                .component(new ItemOutputComponent(IRItems.TIN_PLATE))
                .component(new TimeComponent(200))
                .component(new EnergyInputComponent(800))
                .save(output);
        this.compressorRecipe()
                .component(new ItemInputComponent(Tags.Items.INGOTS_COPPER))
                .component(new ItemOutputComponent(IRItems.COPPER_PLATE))
                .component(new TimeComponent(200))
                .component(new EnergyInputComponent(800))
                .save(output);
        this.compressorRecipe()
                .component(new ItemInputComponent(IRItems.COPPER_PLATE, 8))
                .component(new ItemOutputComponent(IRItems.DENSE_COPPER_PLATE))
                .component(new TimeComponent(200))
                .component(new EnergyInputComponent(800))
                .save(output);

        this.maceratorRecipe()
                .component(new ItemInputComponent(IRItems.TIN_INGOT))
                .component(new ItemOutputListComponent(IRItems.TIN_DUST))
                .component(new TimeComponent(200))
                .component(new EnergyInputComponent(800))
                .save(output);
        this.maceratorRecipe()
                .component(new ItemInputComponent(Tags.Items.INGOTS_COPPER))
                .component(new ItemOutputListComponent(IRItems.COPPER_DUST))
                .component(new TimeComponent(200))
                .component(new EnergyInputComponent(800))
                .save(output);
        this.maceratorRecipe()
                .component(new ItemInputComponent(Tags.Items.INGOTS_IRON))
                .component(new ItemOutputListComponent(IRItems.IRON_DUST))
                .component(new TimeComponent(200))
                .component(new EnergyInputComponent(800))
                .save(output);
        this.maceratorRecipe()
                .component(new ItemInputComponent(Tags.Items.INGOTS_GOLD))
                .component(new ItemOutputListComponent(IRItems.GOLD_DUST))
                .component(new TimeComponent(200))
                .component(new EnergyInputComponent(800))
                .save(output);
        this.maceratorRecipe()
                .component(new ItemInputComponent(ItemTags.COALS))
                .component(new ItemOutputListComponent(IRItems.COAL_DUST))
                .component(new TimeComponent(200))
                .component(new EnergyInputComponent(800))
                .save(output);

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
                .save(output, IndustrialReclassified.rl("raw_iridium_from_deepslate_iridium_ore"));

        oreMacerationRecipe(Tags.Items.ORES_COPPER, Tags.Items.RAW_MATERIALS_COPPER, IRItems.COPPER_DUST, "copper", output);
        oreMacerationRecipe(Tags.Items.ORES_IRON, Tags.Items.RAW_MATERIALS_IRON, IRItems.IRON_DUST, "iron", output);
        oreMacerationRecipe(Tags.Items.ORES_GOLD, Tags.Items.RAW_MATERIALS_GOLD, IRItems.GOLD_DUST, "gold", output);

        this.extractorRecipe()
                .component(new ItemInputComponent(IRItems.STICKY_RESIN))
                .component(new ItemOutputListComponent(IRItems.RUBBER, 3))
                .component(new TimeComponent(200))
                .component(new EnergyInputComponent(800))
                .save(output, IndustrialReclassified.rl("sticky_resin_extracting"));

        this.canningMachineRecipe()
                .component(new ItemInputListComponent(Ingredient.of(IRItems.FLUID_CELL), Ingredient.of(CTags.ItemTags.INGOTS_URANIUM)))
                .component(new ItemOutputComponent(IRItems.SINGLE_URANIUM_FUEL_ROD))
                .component(new TimeComponent(200))
                .component(new EnergyInputComponent(800))
                .save(output);

        this.canningMachineRecipe()
                .component(new EnumRecipeComponent<>(CanningMachineRecipeLayout.Variant.FOOD_CANNING))
                .component(new ItemInputListComponent(IRItems.TIN_CAN))
                .component(new TimeComponent(200))
                .component(new EnergyInputComponent(800))
                .save(output, IndustrialReclassified.rl("food_canning"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IRItems.BRONZE_DUST, 3)
                .requires(CTags.ItemTags.DUSTS_TIN)
                .requires(CTags.ItemTags.DUSTS_COPPER)
                .requires(CTags.ItemTags.DUSTS_COPPER)
                .unlockedBy("has_tin_dust", has(CTags.ItemTags.DUSTS_TIN))
                .save(output);

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

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IRItems.ELECTRIC_HOE.get())
                .requires(IRItems.REDSTONE_BATTERY)
                .requires(ItemTags.HOES)
                .unlockedBy("has_redstone_battery", has(IRItems.REDSTONE_BATTERY))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IRItems.ELECTRIC_WRENCH.get())
                .requires(IRItems.REDSTONE_BATTERY)
                .requires(IRItems.WRENCH)
                .unlockedBy("has_redstone_battery", has(IRItems.REDSTONE_BATTERY))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IRItems.ELECTRIC_TREETAP.get())
                .requires(IRItems.REDSTONE_BATTERY)
                .requires(IRItems.TREETAP)
                .unlockedBy("has_redstone_battery", has(IRItems.REDSTONE_BATTERY))
                .save(output);

        armorRecipes(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.REDSTONE_BATTERY.get())
                .pattern(" C ")
                .pattern("TRT")
                .pattern("TRT")
                .define('C', IRBlocks.COPPER_CABLE)
                .define('T', CTags.ItemTags.INGOTS_TIN)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("has_copper_cable", has(IRBlocks.COPPER_CABLE))
                .save(output);

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
                .save(output, IndustrialReclassified.rl("advanced_circuit1"));

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

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRBlocks.INDUSTRIAL_TNT.get(), 6)
                .pattern("###")
                .pattern("TTT")
                .pattern("###")
                .define('#', Items.FLINT)
                .define('T', Items.TNT)
                .unlockedBy("has_tnt", has(Items.TNT))
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

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(Tags.Items.INGOTS_IRON), RecipeCategory.MISC, IRItems.REFINED_IRON_INGOT, 0.7f, 100)
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .save(output, rl("refined_iron_blasting"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Tags.Items.INGOTS_IRON), RecipeCategory.MISC, IRItems.REFINED_IRON_INGOT, 0.7f, 200)
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .save(output, rl("refined_iron_smelting"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(IRItems.STICKY_RESIN), RecipeCategory.MISC, IRItems.RUBBER, 0.7f, 200)
                .unlockedBy("has_rubber", has(IRItems.STICKY_RESIN))
                .save(output);

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
                .save(output, IndustrialReclassified.rl("advanced_machine_frame1"));

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

        generatorCraftingRecipes(output);

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

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.MIXED_METAL_INGOT.get())
                .pattern("III")
                .pattern("BBB")
                .pattern("TTT")
                .define('I', CTags.ItemTags.INGOTS_REFINED_IRON)
                .define('B', CTags.ItemTags.INGOTS_BRONZE)
                .define('T', CTags.ItemTags.INGOTS_TIN)
                .unlockedBy("has_bronze", has(CTags.ItemTags.INGOTS_BRONZE))
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

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.COAL_BALL)
                .pattern("DDD")
                .pattern("DFD")
                .pattern("DDD")
                .define('D', CTags.ItemTags.DUSTS_COAL)
                .define('F', Items.FLINT)
                .unlockedBy("has_coal_dust", has(CTags.ItemTags.DUSTS_COAL))
                .save(output);

        this.compressorRecipe()
                .component(new ItemInputComponent(IRItems.COAL_BALL))
                .component(new ItemOutputComponent(IRItems.COMPRESSED_COAL_BALL))
                .component(new TimeComponent(200))
                .component(new EnergyInputComponent(800))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.GRAPHENE)
                .pattern("CCC")
                .pattern("COC")
                .pattern("CCC")
                .define('C', IRItems.COMPRESSED_COAL_BALL)
                .define('O', Items.OBSIDIAN)
                .unlockedBy("has_compressed_coal_ball", has(IRItems.COMPRESSED_COAL_BALL))
                .save(output);

        this.compressorRecipe()
                .component(new ItemInputComponent(IRItems.GRAPHENE))
                .component(new ItemOutputComponent(Items.DIAMOND))
                .component(new TimeComponent(200))
                .component(new EnergyInputComponent(800))
                .save(output);

        this.recyclerRecipe().save(output, IndustrialReclassified.rl("scrap_from_recycling"));

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

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRMachines.CANNING_MACHINE.getBlock())
                .pattern("AMA")
                .pattern("ACA")
                .define('A', IRItems.FLUID_CELL)
                .define('M', IRBlocks.MACHINE_FRAME)
                .define('C', IRItems.BASIC_CIRCUIT)
                .unlockedBy("has_basic_circuit", has(IRItems.BASIC_CIRCUIT))
                .save(output);

        plantBallRecipe(Tags.Items.CROPS, "crops", output);
        plantBallRecipe(ItemTags.SAPLINGS, "sapling", output);
        plantBallRecipe(ItemTags.LEAVES, "leaves", output);

        this.geothermalGeneratorRecipe()
                .component(new FluidInputComponent(FluidTags.LAVA, 1))
                .component(new EnergyOutputComponent(20))
                .save(output, IndustrialReclassified.rl("geothermal_energy_from_lava"));

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

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRBlocks.NUCLEAR_REACTOR_CHAMBER)
                .pattern(" C ")
                .pattern("CMC")
                .pattern(" C ")
                .define('M', IRBlocks.ADVANCED_MACHINE_FRAME)
                .define('C', IRItems.DENSE_COPPER_PLATE)
                .unlockedBy("has_dense_copper_plates", has(IRItems.DENSE_COPPER_PLATE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRBlocks.NUCLEAR_REACTOR)
                .pattern(" C ")
                .pattern("RRR")
                .pattern(" G ")
                .define('G', IRMachines.BASIC_GENERATOR)
                .define('C', IRItems.ADVANCED_CIRCUIT)
                .define('R', IRBlocks.NUCLEAR_REACTOR_CHAMBER)
                .unlockedBy("has_nuclear_reactor_chamber", has(IRBlocks.NUCLEAR_REACTOR_CHAMBER))
                .save(output);

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

        cableRecipe(CTags.ItemTags.INGOTS_TIN, IRBlocks.TIN_CABLE, output);
        cableRecipe(Tags.Items.INGOTS_COPPER, IRBlocks.COPPER_CABLE, output);
        cableRecipe(Tags.Items.INGOTS_GOLD, IRBlocks.GOLD_CABLE, output);
        cableRecipe(CTags.ItemTags.INGOTS_REFINED_IRON, IRBlocks.HV_CABLE, output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRBlocks.GLASS_FIBRE_CABLE, 4)
                .pattern("GGG")
                .pattern("RDR")
                .pattern("GGG")
                .define('G', Tags.Items.GLASS_BLOCKS)
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IRItems.SCRAP)
                .requires(IRBlocks.BURNT_CABLE)
                .unlockedBy("has_burnt_cable", has(IRBlocks.BURNT_CABLE))
                .save(output);

        this.rubberWoodRecipes(output);

    }

    private static void plantBallRecipe(TagKey<Item> plantTag, String group, RecipeOutput output) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IRItems.PLANT_BALL)
                .requires(Tags.Items.CROPS)
                .requires(Tags.Items.CROPS)
                .requires(Tags.Items.CROPS)
                .requires(Tags.Items.CROPS)
                .unlockedBy("has_plant", has(plantTag))
                .save(output, IndustrialReclassified.rl("plant_ball_from_" + group));
    }

    private static void cableRecipe(TagKey<Item> ingotTag, ItemLike cableBlock, RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, cableBlock, 6)
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
                .save(output, IndustrialReclassified.rl(oreName + "_dust_from_ore_maceration"));
        this.maceratorRecipe()
                .component(new ItemInputComponent(rawTag, 4))
                .component(new ItemOutputListComponent(result, 3))
                .component(new TimeComponent(200))
                .component(new EnergyInputComponent(800))
                .save(output, IndustrialReclassified.rl(oreName + "_dust_from_raw_ore_maceration"));
    }

    private static void armorRecipes(RecipeOutput output) {
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
    }

    private void rubberWoodRecipes(RecipeOutput output) {
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
    }
}
