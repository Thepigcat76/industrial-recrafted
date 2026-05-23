package com.portingdeadmods.indrec.registries;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.impl.recipes.flags.FluidInputComponentFlag;
import com.portingdeadmods.indrec.impl.recipes.flags.FluidOutputComponentFlag;
import com.portingdeadmods.indrec.impl.recipes.flags.ItemInputComponentFlag;
import com.portingdeadmods.indrec.impl.recipes.flags.ItemOutputComponentFlag;
import com.portingdeadmods.indrec.api.recipes.RecipeFlagType;

public final class IRRecipeComponentFlags {
    public static final RecipeFlagType<ItemInputComponentFlag> ITEM_INPUT = new RecipeFlagType<>(IndustrialRecrafted.rl("item_input"));
    public static final RecipeFlagType<ItemOutputComponentFlag> ITEM_OUTPUT = new RecipeFlagType<>(IndustrialRecrafted.rl("item_output"));
    public static final RecipeFlagType<FluidInputComponentFlag> FLUID_INPUT = new RecipeFlagType<>(IndustrialRecrafted.rl("fluid_input"));
    public static final RecipeFlagType<FluidOutputComponentFlag> FLUID_OUTPUT = new RecipeFlagType<>(IndustrialRecrafted.rl("fluid_output"));
}
