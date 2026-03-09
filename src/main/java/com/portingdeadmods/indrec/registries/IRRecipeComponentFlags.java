package com.portingdeadmods.indrec.registries;

import com.portingdeadmods.indrec.IndustrialReclassified;
import com.portingdeadmods.indrec.content.recipes.flags.FluidInputComponentFlag;
import com.portingdeadmods.indrec.content.recipes.flags.FluidOutputComponentFlag;
import com.portingdeadmods.indrec.content.recipes.flags.ItemInputComponentFlag;
import com.portingdeadmods.indrec.content.recipes.flags.ItemOutputComponentFlag;
import com.portingdeadmods.indrec.api.recipes.RecipeFlagType;

public final class IRRecipeComponentFlags {
    public static final RecipeFlagType<ItemInputComponentFlag> ITEM_INPUT = new RecipeFlagType<>(IndustrialReclassified.rl("item_input"));
    public static final RecipeFlagType<ItemOutputComponentFlag> ITEM_OUTPUT = new RecipeFlagType<>(IndustrialReclassified.rl("item_output"));
    public static final RecipeFlagType<FluidInputComponentFlag> FLUID_INPUT = new RecipeFlagType<>(IndustrialReclassified.rl("fluid_input"));
    public static final RecipeFlagType<FluidOutputComponentFlag> FLUID_OUTPUT = new RecipeFlagType<>(IndustrialReclassified.rl("fluid_output"));
}
