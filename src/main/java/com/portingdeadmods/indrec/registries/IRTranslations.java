package com.portingdeadmods.indrec.registries;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.portingdeadlibs.api.translations.DefaultTranslationCategory;
import com.portingdeadmods.portingdeadlibs.api.translations.DeferredTranslation;
import com.portingdeadmods.portingdeadlibs.api.translations.DeferredTranslationRegister;
import com.portingdeadmods.portingdeadlibs.api.translations.TranslatableConstant;

import java.util.HashMap;

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
    public static final DeferredTranslation<TranslatableConstant> BASIC_SOLAR_PANEL = MENUS_CATEGORY.registerWithDefault("basic_solar_panel", "Basic Solar Panel");
    public static final DeferredTranslation<TranslatableConstant> COMPRESSOR = MENUS_CATEGORY.registerWithDefault("compressor", "Compressor");
    public static final DeferredTranslation<TranslatableConstant> MACERATOR = MENUS_CATEGORY.registerWithDefault("macerator", "Macerator");
    public static final DeferredTranslation<TranslatableConstant> EXTRACTOR = MENUS_CATEGORY.registerWithDefault("extractor", "Extractor");
    public static final DeferredTranslation<TranslatableConstant> RECYCLER = MENUS_CATEGORY.registerWithDefault("recycler", "Recycler");
    public static final DeferredTranslation<TranslatableConstant> BATTERY_BOX = MENUS_CATEGORY.registerWithDefault("battery_box", "Battery Box");
    public static final DeferredTranslation<TranslatableConstant> CANNING_MACHINE = MENUS_CATEGORY.registerWithDefault("canning_machine", "Canning Machine");

    private static final DefaultTranslationCategory MESSAGES_CATEGORY = TRANSLATIONS.createCategory("messages");
    public static final DeferredTranslation<TranslatableConstant> GUIDE_ME_MISSING = MESSAGES_CATEGORY.registerWithDefault("guide_me_missing", "GuideME must be installed to access the guide book!");
    public static final DeferredTranslation<TranslatableConstant> TOGGLED_AOE = MESSAGES_CATEGORY.registerWithDefault("toggled_aoe", "Toggled AOE Mining: %s");
    public static final DeferredTranslation<TranslatableConstant> TOGGLED_TREE_CUTTER = MESSAGES_CATEGORY.registerWithDefault("toggled_tree_cutter", "Toggled Tree Cutter: %s");

}