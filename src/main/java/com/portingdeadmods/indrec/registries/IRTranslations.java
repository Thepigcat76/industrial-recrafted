package com.portingdeadmods.indrec.registries;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.portingdeadlibs.api.translations.DefaultTranslationCategory;
import com.portingdeadmods.portingdeadlibs.api.translations.DeferredTranslation;
import com.portingdeadmods.portingdeadlibs.api.translations.DeferredTranslationRegister;
import com.portingdeadmods.portingdeadlibs.api.translations.TranslatableConstant;
import net.minecraft.network.chat.Component;

public final class IRTranslations {
    public static final DeferredTranslationRegister TRANSLATIONS = DeferredTranslationRegister.createTranslations(IndustrialRecrafted.MODID);

    private static final DefaultTranslationCategory GENERAL_CATEGORY = TRANSLATIONS.createCategory("general");
    public static final DeferredTranslation<TranslatableConstant> FLUID_UNIT = GENERAL_CATEGORY.registerWithDefault("unit.fluid", "mb");
    public static final DeferredTranslation<TranslatableConstant> ENERGY_UNIT = GENERAL_CATEGORY.registerWithDefault("unit.energy", "EU");
    public static final DeferredTranslation<TranslatableConstant> HEAT_UNIT = GENERAL_CATEGORY.registerWithDefault("unit.heat", "HU");
    public static final DeferredTranslation<TranslatableConstant> ON = GENERAL_CATEGORY.registerWithDefault("on", "On");
    public static final DeferredTranslation<TranslatableConstant> OFF = GENERAL_CATEGORY.registerWithDefault("off", "Off");

    public static final DeferredTranslation<TranslatableConstant> ENERGY_NAME = GENERAL_CATEGORY.registerWithDefault("name.energy", "Energy");
    public static final DeferredTranslation<TranslatableConstant> HEAT_NAME = GENERAL_CATEGORY.registerWithDefault("name.heat", "Heat");
    public static final DeferredTranslation<TranslatableConstant> FLUID_NAME = GENERAL_CATEGORY.registerWithDefault("name.fluid", "Fluid");

    private static final DefaultTranslationCategory TOOLTIP_CATEGORY = TRANSLATIONS.createCategory("tooltip");
    public static final DeferredTranslation<TranslatableConstant> FILLABLE = TOOLTIP_CATEGORY.registerWithDefault("fillable", "Can be filled using a fluid container");
    public static final DeferredTranslation<TranslatableConstant> AOE_STATUS = TOOLTIP_CATEGORY.registerWithDefault("aoe_status", "3x3 Mining: %s");
    public static final DeferredTranslation<TranslatableConstant> TREE_CUTTER_STATUS = TOOLTIP_CATEGORY.registerWithDefault("tree_cutter_status", "Tree Cutter: %s");
    public static final DeferredTranslation<TranslatableConstant> ACTIVE = TOOLTIP_CATEGORY.registerWithDefault("active", "Active");
    public static final DeferredTranslation<TranslatableConstant> INACTIVE = TOOLTIP_CATEGORY.registerWithDefault("inactive", "Inactive");
    public static final DeferredTranslation<TranslatableConstant> SCRAP_BOX_TOOLTIP = TOOLTIP_CATEGORY.registerWithDefault("scrap_box_tooltip", "Right-Click to unbox the scrap");
    public static final DeferredTranslation<TranslatableConstant> SCRAP_TOOLTIP = TOOLTIP_CATEGORY.registerWithDefault("scrap_tooltip", "Can be produced in the recycler from any item");

    public static final DeferredTranslation<TranslatableConstant> ENERGY_STORED = TOOLTIP_CATEGORY.registerWithDefault("energy.stored", "Stored: ");
    public static final DeferredTranslation<TranslatableConstant> ENERGY_TIER = TOOLTIP_CATEGORY.registerWithDefault("energy.tier", "Tier: ");
    public static final DeferredTranslation<TranslatableConstant> ENERGY_AMOUNT = TOOLTIP_CATEGORY.registerWithDefault("energy.amount", "%d");
    public static final DeferredTranslation<TranslatableConstant> ENERGY_AMOUNT_WITH_CAPACITY = TOOLTIP_CATEGORY.registerWithDefault("energy.amount_with_capacity", "%s/%s");

    public static final DeferredTranslation<TranslatableConstant> TOOLBOX_FULLNESS = TOOLTIP_CATEGORY.registerWithDefault("toolbox.fullness", "Tools: %d/%d");
    public static final DeferredTranslation<TranslatableConstant> TOOLBOX_SELECTED = TOOLTIP_CATEGORY.registerWithDefault("toolbox.selected", "Selected: ");
    public static final DeferredTranslation<TranslatableConstant> TOOLBOX_INSERT_HINT = TOOLTIP_CATEGORY.registerWithDefault("toolbox.insert_hint", "Right-Click tools onto the toolbox to store them");
    public static final DeferredTranslation<TranslatableConstant> TOOLBOX_FILTER_HINT = TOOLTIP_CATEGORY.registerWithDefault("toolbox.filter_hint", "Press %s while holding to set slot filters");
    public static final DeferredTranslation<TranslatableConstant> TOOLBOX_COLLECT_HINT = TOOLTIP_CATEGORY.registerWithDefault("toolbox.collect_hint", "Right-Click to collect tools from your inventory into matching filtered slots");
    public static final DeferredTranslation<TranslatableConstant> TOOLBOX_SELECT_HINT = TOOLTIP_CATEGORY.registerWithDefault("toolbox.select_hint", "Scroll to select a tool, Right-Click to take it out");

    public static final DeferredTranslation<TranslatableConstant> SEED_BAG_CROP = TOOLTIP_CATEGORY.registerWithDefault("seed_bag.crop", "Crop: ");
    public static final DeferredTranslation<TranslatableConstant> SEED_BAG_UNKNOWN_CROP = TOOLTIP_CATEGORY.registerWithDefault("seed_bag.unknown_crop", "<unknown>");

    public static final DeferredTranslation<TranslatableConstant> CROP_ANALYZER_CROP = TOOLTIP_CATEGORY.registerWithDefault("crop_analyzer.crop", "Crop: ");
    public static final DeferredTranslation<TranslatableConstant> CROP_ANALYZER_UNKNOWN_CROP = TOOLTIP_CATEGORY.registerWithDefault("crop_analyzer.unknown_crop", "<unknown>");
    public static final DeferredTranslation<TranslatableConstant> CROP_ANALYZER_STAGE = TOOLTIP_CATEGORY.registerWithDefault("crop_analyzer.stage", "Stage: ");
    public static final DeferredTranslation<TranslatableConstant> CROP_ANALYZER_STAGE_VALUE = TOOLTIP_CATEGORY.registerWithDefault("crop_analyzer.stage_value", "%d/%d");
    public static final DeferredTranslation<TranslatableConstant> CROP_ANALYZER_DROPS = TOOLTIP_CATEGORY.registerWithDefault("crop_analyzer.drops", "Drops:");
    public static final DeferredTranslation<TranslatableConstant> CROP_ANALYZER_DROP_AVERAGE = TOOLTIP_CATEGORY.registerWithDefault("crop_analyzer.drop_average", "(avg %s)");
    public static final DeferredTranslation<TranslatableConstant> CROP_ANALYZER_BLOCK_BELOW_SOIL = TOOLTIP_CATEGORY.registerWithDefault("crop_analyzer.block_below_soil", "Block Below Soil:");
    public static final DeferredTranslation<TranslatableConstant> CROP_ANALYZER_SOIL = TOOLTIP_CATEGORY.registerWithDefault("crop_analyzer.soil", "Soil:");
    public static final DeferredTranslation<TranslatableConstant> CROP_ANALYZER_LIGHT = TOOLTIP_CATEGORY.registerWithDefault("crop_analyzer.light", "Light: ");
    public static final DeferredTranslation<TranslatableConstant> CROP_ANALYZER_LIGHT_EXACT = TOOLTIP_CATEGORY.registerWithDefault("crop_analyzer.light_exact", "%d");
    public static final DeferredTranslation<TranslatableConstant> CROP_ANALYZER_LIGHT_MIN = TOOLTIP_CATEGORY.registerWithDefault("crop_analyzer.light_min", "%d+");
    public static final DeferredTranslation<TranslatableConstant> CROP_ANALYZER_LIGHT_RANGE = TOOLTIP_CATEGORY.registerWithDefault("crop_analyzer.light_range", "%d-%d");
    public static final DeferredTranslation<TranslatableConstant> CROP_ANALYZER_LIGHT_CURRENT = TOOLTIP_CATEGORY.registerWithDefault("crop_analyzer.light_current", "(current %d)");
    public static final DeferredTranslation<TranslatableConstant> CROP_ANALYZER_HINT = TOOLTIP_CATEGORY.registerWithDefault("crop_analyzer.hint", "Hold right-click to analyze");
    public static final DeferredTranslation<TranslatableConstant> CROP_ANALYZER_ANALYZING = TOOLTIP_CATEGORY.registerWithDefault("crop_analyzer.analyzing", "Analyzing... %d%%");
    public static final DeferredTranslation<TranslatableConstant> CROP_ANALYZER_TAB_GENERAL = TOOLTIP_CATEGORY.registerWithDefault("crop_analyzer.tab.general", "General");
    public static final DeferredTranslation<TranslatableConstant> CROP_ANALYZER_TAB_REQUIREMENTS = TOOLTIP_CATEGORY.registerWithDefault("crop_analyzer.tab.requirements", "Requirements");
    public static final DeferredTranslation<TranslatableConstant> CROP_ANALYZER_TAB_HINT = TOOLTIP_CATEGORY.registerWithDefault("crop_analyzer.tab.hint", "Sneak + Scroll to switch tabs");
    public static final DeferredTranslation<TranslatableConstant> CROP_CROSSBREEDING = GENERAL_CATEGORY.registerWithDefault("crop_crossbreeding", "Crop Crossbreeding");
    public static final DeferredTranslation<TranslatableConstant> CROP_CROSSBREEDING_CAN_SPREAD = TOOLTIP_CATEGORY.registerWithDefault("crop_crossbreeding.can_spread", "Can spread onto the crossbreed sticks");
    public static final DeferredTranslation<TranslatableConstant> CROP_CROSSBREEDING_CHANCE = TOOLTIP_CATEGORY.registerWithDefault("crop_crossbreeding.chance", "Chance: %s%%");
    public static final DeferredTranslation<TranslatableConstant> CROP_MUTATION = GENERAL_CATEGORY.registerWithDefault("crop_mutation", "Crop Mutation");
    public static final DeferredTranslation<TranslatableConstant> CROP_MUTATION_CHANCE = TOOLTIP_CATEGORY.registerWithDefault("crop_mutation.chance", "Chance per random tick: %s%%");
    public static final DeferredTranslation<TranslatableConstant> CROP_INTERACTION = GENERAL_CATEGORY.registerWithDefault("crop_interaction", "Crop Interaction");
    public static final DeferredTranslation<TranslatableConstant> CROP_INTERACTION_REMAINDER = TOOLTIP_CATEGORY.registerWithDefault("crop_interaction.remainder", "Leaves: %s");
    public static final DeferredTranslation<TranslatableConstant> CROP_INTERACTION_NOT_CONSUMED = TOOLTIP_CATEGORY.registerWithDefault("crop_interaction.not_consumed", "Item is not consumed");
    public static final DeferredTranslation<TranslatableConstant> CROP_KEEPS_AGE = TOOLTIP_CATEGORY.registerWithDefault("crop.keeps_age", "Keeps the growth stage");
    public static final DeferredTranslation<TranslatableConstant> CROP_RESETS_AGE = TOOLTIP_CATEGORY.registerWithDefault("crop.resets_age", "Resets the growth stage");
    public static final DeferredTranslation<TranslatableConstant> CROP_ANALYZER_NOT_ENOUGH_ENERGY = TOOLTIP_CATEGORY.registerWithDefault("crop_analyzer.not_enough_energy", "Not enough energy to analyze");
    public static final DeferredTranslation<TranslatableConstant> MACHINE_BURNT = TOOLTIP_CATEGORY.registerWithDefault("machine_burnt", "This machine is burnt out. Right-Click it with a Fuse to repair it");

    public static final DeferredTranslation<TranslatableConstant> FLUID_TYPE = TOOLTIP_CATEGORY.registerWithDefault("fluid.type", "Fluid: ");
    public static final DeferredTranslation<TranslatableConstant> FLUID_STORED = TOOLTIP_CATEGORY.registerWithDefault("fluid.stored", "Stored: ");
    public static final DeferredTranslation<TranslatableConstant> FLUID_AMOUNT = TOOLTIP_CATEGORY.registerWithDefault("fluid.amount", "%d");
    public static final DeferredTranslation<TranslatableConstant> FLUID_AMOUNT_WITH_CAPACITY = TOOLTIP_CATEGORY.registerWithDefault("fluid.amount_with_capacity", "%d/%d");
    public static final DeferredTranslation<TranslatableConstant> EMPTY_FLUID = TOOLTIP_CATEGORY.registerWithDefault("empty_fluid", "Alt + Shift Click to empty");

    public static final DeferredTranslation<TranslatableConstant> HEAT_STORED = TOOLTIP_CATEGORY.registerWithDefault("heat.stored", "Stored: ");
    public static final DeferredTranslation<TranslatableConstant> HEAT_AMOUNT = TOOLTIP_CATEGORY.registerWithDefault("heat.amount", "%.1f");
    public static final DeferredTranslation<TranslatableConstant> HEAT_AMOUNT_WITH_CAPACITY = TOOLTIP_CATEGORY.registerWithDefault("heat.amount_with_capacity", "%.1f/%.1f");

    public static final DeferredTranslation<TranslatableConstant> MELTING_NOT_POSSIBLE = TOOLTIP_CATEGORY.registerWithDefault("melting.not_possible", "Melting not possible");
    public static final DeferredTranslation<TranslatableConstant> MELTING_PROGRESS = TOOLTIP_CATEGORY.registerWithDefault("melting.progress", "%.1f/%.1f");

    public static final DeferredTranslation<TranslatableConstant> MULTIBLOCK_HINT = TOOLTIP_CATEGORY.registerWithDefault("multiblock.hint", "This is a multiblock, look at the Blueprint for building instructions");
    public static final DeferredTranslation<TranslatableConstant> CLAY_CASTING_MOLD = TOOLTIP_CATEGORY.registerWithDefault("casting_mold", "Hold SHIFT and Scroll + Right Click to select the Casting Mold");

    private static final DefaultTranslationCategory JEI_CATEGORY = TRANSLATIONS.createCategory("jei");
    public static final DeferredTranslation<TranslatableConstant> CRUCIBLE_SMELTING = JEI_CATEGORY.registerWithDefault("crucible", "Crucible Smelting");
    public static final DeferredTranslation<TranslatableConstant> CASTING = JEI_CATEGORY.registerWithDefault("casting", "Casting");
    public static final DeferredTranslation<TranslatableConstant> MOLD_CASTING = JEI_CATEGORY.registerWithDefault("mold_casting", "Mold Casting");
    public static final DeferredTranslation<TranslatableConstant> ENERGY_USAGE = JEI_CATEGORY.registerWithDefault("energy.usage", "%s/t: %d");
    public static final DeferredTranslation<TranslatableConstant> ITEM_CONSUMED = JEI_CATEGORY.registerWithDefault("items.consumed", "Item is consumed");

    private static final DefaultTranslationCategory MENUS_CATEGORY = TRANSLATIONS.createCategory("menus");
    public static final DeferredTranslation<TranslatableConstant> BASIC_GENERATOR = MENUS_CATEGORY.registerWithDefault("basic_generator", "Basic Generator");
    public static final DeferredTranslation<TranslatableConstant> GEOTHERMAL_GENERATOR = MENUS_CATEGORY.registerWithDefault("geothermal_generator", "Geothermal Generator");
    public static final DeferredTranslation<TranslatableConstant> ELECTRIC_FURNACE = MENUS_CATEGORY.registerWithDefault("electric_furnace", "Electric Furnace");
    public static final DeferredTranslation<TranslatableConstant> CROP_MANAGER = MENUS_CATEGORY.registerWithDefault("crop_manager", "Crop Manager");
    public static final DeferredTranslation<TranslatableConstant> BASIC_SOLAR_PANEL = MENUS_CATEGORY.registerWithDefault("basic_solar_panel", "Basic Solar Panel");
    public static final DeferredTranslation<TranslatableConstant> COMPRESSOR = MENUS_CATEGORY.registerWithDefault("compressor", "Compressor");
    public static final DeferredTranslation<TranslatableConstant> MACERATOR = MENUS_CATEGORY.registerWithDefault("macerator", "Macerator");
    public static final DeferredTranslation<TranslatableConstant> EXTRACTOR = MENUS_CATEGORY.registerWithDefault("extractor", "Extractor");
    public static final DeferredTranslation<TranslatableConstant> RECYCLER = MENUS_CATEGORY.registerWithDefault("recycler", "Recycler");
    public static final DeferredTranslation<TranslatableConstant> BATTERY_BOX = MENUS_CATEGORY.registerWithDefault("battery_box", "Battery Box");
    public static final DeferredTranslation<TranslatableConstant> CANNING_MACHINE = MENUS_CATEGORY.registerWithDefault("canning_machine", "Canning Machine");
    public static final DeferredTranslation<TranslatableConstant> MATTER_FABRICATOR = MENUS_CATEGORY.registerWithDefault("matter_fabricator", "Matter Fabricator");
    public static final DeferredTranslation<TranslatableConstant> TOOLBOX_FILTER = MENUS_CATEGORY.registerWithDefault("toolbox_filter", "Set Toolbox Filter");

    private static final DefaultTranslationCategory MESSAGES_CATEGORY = TRANSLATIONS.createCategory("messages");
    public static final DeferredTranslation<TranslatableConstant> GUIDE_ME_MISSING = MESSAGES_CATEGORY.registerWithDefault("guide_me_missing", "GuideME must be installed to access the guide book!");
    public static final DeferredTranslation<TranslatableConstant> TOGGLED_AOE = MESSAGES_CATEGORY.registerWithDefault("toggled_aoe", "Toggled AOE Mining: %s");
    public static final DeferredTranslation<TranslatableConstant> TOGGLED_TREE_CUTTER = MESSAGES_CATEGORY.registerWithDefault("toggled_tree_cutter", "Toggled Tree Cutter: %s");

}