package com.portingdeadmods.indrec.datagen;

import com.portingdeadmods.indrec.api.crops.Crop;
import com.portingdeadmods.indrec.content.recipes.CropCrossbreedingRecipe;
import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class CropCrossbreedingRecipeBuilder implements RecipeBuilder {
    private final ResourceKey<Crop> firstParent;
    private final ResourceKey<Crop> secondParent;
    private final ResourceKey<Crop> result;
    private float chance = 1;
    private boolean firstParentSpreads = true;
    private boolean secondParentSpreads = true;

    private CropCrossbreedingRecipeBuilder(ResourceKey<Crop> firstParent, ResourceKey<Crop> secondParent, ResourceKey<Crop> result) {
        this.firstParent = firstParent;
        this.secondParent = secondParent;
        this.result = result;
    }

    public static CropCrossbreedingRecipeBuilder of(ResourceKey<Crop> firstParent, ResourceKey<Crop> secondParent, ResourceKey<Crop> result) {
        return new CropCrossbreedingRecipeBuilder(firstParent, secondParent, result);
    }

    /**
     * @param chance chance (0-1) of the crossbreeding succeeding
     */
    public CropCrossbreedingRecipeBuilder chance(float chance) {
        this.chance = chance;
        return this;
    }

    public CropCrossbreedingRecipeBuilder firstParentSpreads(boolean spreads) {
        this.firstParentSpreads = spreads;
        return this;
    }

    public CropCrossbreedingRecipeBuilder secondParentSpreads(boolean spreads) {
        this.secondParentSpreads = spreads;
        return this;
    }

    @Override
    public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String group) {
        return this;
    }

    // The result is a crop, not an item
    @Override
    public Item getResult() {
        return Items.AIR;
    }

    /**
     * Saves the recipe using the result crop's id as recipe id
     */
    @Override
    public void save(RecipeOutput recipeOutput) {
        this.save(recipeOutput, this.result.location());
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        recipeOutput.accept(id, new CropCrossbreedingRecipe(this.firstParent, this.secondParent, this.result, this.chance, this.firstParentSpreads, this.secondParentSpreads), null);
    }
}
