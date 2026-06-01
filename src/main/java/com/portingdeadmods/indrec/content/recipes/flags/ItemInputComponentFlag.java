package com.portingdeadmods.indrec.content.recipes.flags;

import com.portingdeadmods.indrec.api.recipes.RecipeComponentFlag;
import com.portingdeadmods.portingdeadlibs.api.recipes.IngredientWithCount;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface ItemInputComponentFlag extends RecipeComponentFlag {
    List<IngredientWithCount> getIngredients();

    List<Float> getChances();

    boolean test(List<ItemStack> items, boolean strict);

}
