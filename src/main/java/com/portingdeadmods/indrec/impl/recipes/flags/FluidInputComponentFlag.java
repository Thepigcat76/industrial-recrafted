package com.portingdeadmods.indrec.impl.recipes.flags;

import com.portingdeadmods.indrec.api.recipes.RecipeComponentFlag;
import com.portingdeadmods.portingdeadlibs.api.recipes.FluidIngredientWithAmount;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public interface FluidInputComponentFlag extends RecipeComponentFlag {
    List<FluidIngredientWithAmount> getIngredients();

    List<Float> getChances();

    boolean test(List<FluidStack> fluids, boolean strict);

}
