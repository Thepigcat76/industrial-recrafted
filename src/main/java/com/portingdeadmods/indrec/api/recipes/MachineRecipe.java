package com.portingdeadmods.indrec.api.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface MachineRecipe {
    ResourceLocation id();

    Map<String, RecipeComponent> getComponents();

    MachineRecipeLayout<? extends MachineRecipe> getLayout();

    <R extends RecipeComponent> R getComponent(RecipeComponent.Type<R> type);

    <F extends RecipeComponentFlag> F getComponentByFlag(RecipeFlagType<F> flagType);

    boolean hasResultItem(HolderLookup.Provider provider);

    boolean hasResultFluid(HolderLookup.Provider provider);

    boolean hasResultEnergy(HolderLookup.Provider provider);

    default FluidStack getResultFluid(HolderLookup.Provider provider) {
        return this.assembleResultFluid(null, provider);
    }

    FluidStack assembleResultFluid(MachineRecipeInput input, HolderLookup.Provider provider);

    ItemStack getResultItem(HolderLookup.Provider provider);

    ItemStack assembleResultItem(MachineRecipeInput input, HolderLookup.Provider provider);

    default int getResultEnergy(HolderLookup.Provider provider) {
        return this.assembleResultEnergy(null, provider);
    }

    int assembleResultEnergy(MachineRecipeInput input, HolderLookup.Provider provider);

    boolean hasProgress();

    int getMaxProgress();

    record MachineRecipeInput(List<ItemStack> items, List<FluidStack> fluids) implements RecipeInput {
        public MachineRecipeInput(List<ItemStack> items) {
            this(items, List.of());
        }

        public MachineRecipeInput(FluidStack fluid) {
            this(List.of(), List.of(fluid));
        }

        public MachineRecipeInput(ItemStack item) {
            this(List.of(item));
        }

        public MachineRecipeInput() {
            this(List.of());
        }

        @Override
        public @NotNull ItemStack getItem(int i) {
            return this.items().get(i);
        }

        @Override
        public int size() {
            return this.items().size();
        }

        @Override
        public boolean isEmpty() {
            for (FluidStack fluid : this.fluids) {
                if (!fluid.isEmpty()) {
                    return false;
                }
            }
            return RecipeInput.super.isEmpty();
        }

    }
}
