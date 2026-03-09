package com.portingdeadmods.indrec.datagen.data;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IRRecipeOutput implements RecipeOutput {
    private final RecipeOutput wrapped;

    public IRRecipeOutput(RecipeOutput wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public Advancement.@NotNull Builder advancement() {
        return this.wrapped.advancement();
    }

    @Override
    public void accept(@NotNull ResourceLocation resourceLocation, @NotNull Recipe<?> recipe, @Nullable AdvancementHolder advancementHolder, ICondition @NotNull ... iConditions) {
        String[] id = recipe.getType().toString().split(":");
        this.wrapped.accept(resourceLocation.withPrefix(id[id.length - 1] + "/"), recipe, advancementHolder, iConditions);
    }

}
