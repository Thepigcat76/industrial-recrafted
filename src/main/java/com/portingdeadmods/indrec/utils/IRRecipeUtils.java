package com.portingdeadmods.indrec.utils;

import com.portingdeadmods.indrec.content.recipes.components.items.ItemInputComponent;
import com.portingdeadmods.portingdeadlibs.api.recipes.IngredientWithCount;
import com.portingdeadmods.portingdeadlibs.utils.RecipeUtils;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class IRRecipeUtils {
    public static boolean matches(List<ItemStack> items, List<ItemInputComponent> inputs) {
        return RecipeUtils.compareItems(items, inputs.stream()
                .map(input -> new IngredientWithCount(input.ingredient(), input.count()))
                .toList());
    }
}
