package com.portingdeadmods.indrec.content.recipes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public record MachineRecipeInput(List<ItemStack> items, List<FluidStack> fluids) implements RecipeInput {
    public MachineRecipeInput(List<ItemStack> items) {
        this(items, List.of());
    }

    public MachineRecipeInput(FluidStack fluid) {
        this(List.of(), List.of(fluid));
    }

    public MachineRecipeInput(ItemStack item) {
        this(List.of(item));
    }

    @Override
    public ItemStack getItem(int i) {
        return this.items().get(i);
    }

    @Override
    public int size() {
        return this.items().size();
    }

    @Override
    public boolean isEmpty() {
        for(FluidStack fluid : this.fluids) {
            if (!fluid.isEmpty()) {
                return false;
            }
        }
        return RecipeInput.super.isEmpty();
    }
}
