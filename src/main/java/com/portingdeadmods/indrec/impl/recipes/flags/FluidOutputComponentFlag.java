package com.portingdeadmods.indrec.impl.recipes.flags;

import com.portingdeadmods.indrec.api.recipes.RecipeComponentFlag;
import net.minecraft.util.RandomSource;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public interface FluidOutputComponentFlag extends RecipeComponentFlag {
    List<FluidStack> getOutputs();

    List<Float> getChances();

    boolean isOutputted(RandomSource random, int tank);
}
