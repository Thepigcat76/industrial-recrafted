package com.portingdeadmods.indrec.datagen;

import com.portingdeadmods.indrec.impl.recipes.MachineRecipeImpl;
import com.portingdeadmods.indrec.api.recipes.MachineRecipeLayout;
import com.portingdeadmods.indrec.api.recipes.RecipeComponent;
import com.portingdeadmods.indrec.registries.IRRecipeComponentFlags;
import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

public class MachineRecipeBuilder implements RecipeBuilder {
    private final MachineRecipeImpl.Builder<MachineRecipeImpl> builder;

    public MachineRecipeBuilder(MachineRecipeLayout<MachineRecipeImpl> layout) {
        this.builder = MachineRecipeImpl.builder(layout.getId());
    }

    public MachineRecipeBuilder component(RecipeComponent component) {
        this.builder.component(component);
        return this;
    }

    @Override
    public RecipeBuilder unlockedBy(String s, Criterion<?> criterion) {
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String s) {
        return this;
    }

    @Override
    public Item getResult() {
        return this.builder.build().getComponentByFlag(IRRecipeComponentFlags.ITEM_OUTPUT).getOutputs().getFirst().getItem();
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation resourceLocation) {
        recipeOutput.accept(resourceLocation, this.builder.build(), null);
    }
}
