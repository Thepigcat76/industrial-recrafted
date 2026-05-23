package com.portingdeadmods.indrec.impl.recipes.components.items;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.api.recipes.RecipeComponent;
import com.portingdeadmods.indrec.api.recipes.RecipeFlagType;
import com.portingdeadmods.indrec.impl.recipes.flags.ItemInputComponentFlag;
import com.portingdeadmods.indrec.registries.IRRecipeComponentFlags;
import com.portingdeadmods.portingdeadlibs.api.recipes.IngredientWithCount;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;

public class DynamicItemInputComponent implements RecipeComponent, ItemInputComponentFlag {
    public static final Type<DynamicItemInputComponent> TYPE = new Type<>(IndustrialRecrafted.rl("dynamic_item_input"));
    public static final Set<RecipeFlagType<?>> FLAGS = Set.of(IRRecipeComponentFlags.ITEM_INPUT);

    private final BiPredicate<List<ItemStack>, Boolean> testFunction;

    public DynamicItemInputComponent(BiPredicate<List<ItemStack>, Boolean> testFunction) {
        this.testFunction = testFunction;
    }

    @Override
    public Type<?> type() {
        return TYPE;
    }

    @Override
    public Set<RecipeFlagType<?>> flags() {
        return FLAGS;
    }

    @Override
    public List<IngredientWithCount> getIngredients() {
        return List.of();
    }

    @Override
    public List<Float> getChances() {
        return List.of(1f);
    }

    @Override
    public boolean test(List<ItemStack> items, boolean strict) {
        return this.testFunction.test(items, strict);
    }
}
