package com.portingdeadmods.indrec.api.recipes;

import com.portingdeadmods.portingdeadlibs.api.recipes.PDLRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface MachineRecipe<I extends RecipeInput> {
    ResourceLocation id();

    Map<String, RecipeComponent> getComponents();

    MachineRecipeLayout<? extends MachineRecipe<I>> getLayout();

    <R extends RecipeComponent> R getComponent(RecipeComponent.Type<R> type);

    <F extends RecipeComponentFlag> F getComponentByFlag(RecipeFlagType<F> flagType);

    boolean hasResultItem(HolderLookup.Provider provider);

    boolean hasResultFluid(HolderLookup.Provider provider);

    boolean hasResultEnergy(HolderLookup.Provider provider);

    default FluidStack getResultFluid(HolderLookup.Provider provider) {
        return this.assembleResultFluid(null, provider);
    }

    FluidStack assembleResultFluid(I input, HolderLookup.Provider provider);

    ItemStack getResultItem(HolderLookup.Provider provider);

    ItemStack assembleResultItem(I input, HolderLookup.Provider provider);

    default int getResultEnergy(HolderLookup.Provider provider) {
        return this.assembleResultEnergy(null, provider);
    }

    int assembleResultEnergy(I input, HolderLookup.Provider provider);

    boolean hasProgress();

    int getMaxProgress();

}
