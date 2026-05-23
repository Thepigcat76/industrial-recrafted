package com.portingdeadmods.indrec.impl.recipes.flags;

import com.portingdeadmods.indrec.api.recipes.RecipeComponentFlag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface ItemOutputComponentFlag extends RecipeComponentFlag {
    List<ItemStack> getOutputs();

    List<Float> getChances();

    boolean isOutputted(RandomSource random, int slot);

}
