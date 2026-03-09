package com.portingdeadmods.indrec.content.recipes;

import com.portingdeadmods.indrec.api.recipes.RecipeComponent;
import com.portingdeadmods.indrec.api.recipes.RecipeComponentFlag;
import com.portingdeadmods.indrec.api.recipes.RecipeFlagType;
import com.portingdeadmods.indrec.content.recipes.components.TimeComponent;
import com.portingdeadmods.indrec.content.recipes.components.energy.EnergyOutputComponent;
import com.portingdeadmods.indrec.registries.IRRecipeComponentFlags;
import com.portingdeadmods.portingdeadlibs.api.recipes.PDLRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class MachineRecipe implements PDLRecipe<MachineRecipeInput> {
    private RecipeSerializer<? extends MachineRecipe> serializer;
    private RecipeType<? extends MachineRecipe> type;
    private final Map<String, RecipeComponent> components;
    private final ResourceLocation id;

    public MachineRecipe(ResourceLocation id, Map<String, RecipeComponent> components) {
        this.id = id;
        this.components = components;
    }

    public ResourceLocation getId() {
        return id;
    }

    public MachineRecipe(ResourceLocation id) {
        this(id, new LinkedHashMap<>());
    }

    public <R extends RecipeComponent> R getComponent(RecipeComponent.Type<R> type) {
        for (RecipeComponent value : components.values()) {
            if (value.type().equals(type)) {
                return (R) value;
            }
        }
        return null;
    }

    public <F extends RecipeComponentFlag> F getComponentByFlag(RecipeFlagType<F> flagType) {
        for (RecipeComponent component : components.values()) {
            if (component.flags().contains(flagType)) {
                return (F) component;
            }
        }
        return null;
    }

    @Override
    public boolean matches(MachineRecipeInput machineRecipeInput, Level level) {
        return matchesItems(machineRecipeInput, level);
    }

    private <R extends MachineRecipe> boolean matchesItems(MachineRecipeInput machineRecipeInput, Level level) {
        MachineRecipeLayout<R> layout = (MachineRecipeLayout<R>) this.getLayout();
        return layout.matches((R) this, machineRecipeInput, level);
    }

    public boolean hasResultItem(HolderLookup.Provider provider) {
        return this.getComponentByFlag(IRRecipeComponentFlags.ITEM_OUTPUT) != null;
    }

    public boolean hasResultFluid(HolderLookup.Provider provider) {
        return this.getComponentByFlag(IRRecipeComponentFlags.FLUID_OUTPUT) != null;
    }

    public boolean hasResultEnergy(HolderLookup.Provider provider) {
        return this.getComponent(EnergyOutputComponent.TYPE) != null;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider provider) {
        return createResultItem(null, provider);
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull MachineRecipeInput container, HolderLookup.Provider provider) {
        return createResultItem(container, provider);
    }

    public FluidStack getResultFluid(HolderLookup.Provider provider) {
        return createResultFluid(null, provider);
    }

    public FluidStack assembleFluid(@NotNull MachineRecipeInput container, HolderLookup.Provider provider) {
        return createResultFluid(container, provider);
    }

    public int getResultEnergy(HolderLookup.Provider provider) {
        return createResultEnergy(null, provider);
    }

    public int assembleEnergy(@NotNull MachineRecipeInput container, HolderLookup.Provider provider) {
        return createResultEnergy(container, provider);
    }

    private <R extends MachineRecipe> int createResultEnergy(MachineRecipeInput input, HolderLookup.Provider provider) {
        MachineRecipeLayout<R> layout = (MachineRecipeLayout<R>) this.getLayout();
        return layout.createResultEnergy((R) this, input, provider);
    }

    private <R extends MachineRecipe> FluidStack createResultFluid(MachineRecipeInput input, HolderLookup.Provider provider) {
        MachineRecipeLayout<R> layout = (MachineRecipeLayout<R>) this.getLayout();
        return layout.createResultFluid((R) this, input, provider);
    }

    private <R extends MachineRecipe> ItemStack createResultItem(MachineRecipeInput input, HolderLookup.Provider provider) {
        MachineRecipeLayout<R> layout = (MachineRecipeLayout<R>) this.getLayout();
        return layout.createResultItem((R) this, input, provider);
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        if (this.serializer == null) {
            this.serializer = getLayout().getRecipeSerializer();
        }
        return this.serializer;
    }

    private MachineRecipeLayout<?> getLayout() {
        return RegisterRecipeLayoutEvent.LAYOUTS.get(this.id);
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        if (this.type == null) {
            this.type = getLayout().getRecipeType();
        }
        return this.type;
    }

    public Map<String, RecipeComponent> getComponents() {
        return this.components;
    }

    public static <R extends MachineRecipe> Builder<R> builder(ResourceLocation id, BiFunction<ResourceLocation, Map<String, RecipeComponent>, R> recipeFactory) {
        return new Builder<>(id, recipeFactory);
    }

    public static Builder<MachineRecipe> builder(ResourceLocation id) {
        return new Builder<>(id, MachineRecipe::new);
    }

    public static Builder<MachineRecipe> builder(MachineRecipeLayout<MachineRecipe> layout) {
        return new Builder<>(layout.getId(), MachineRecipe::new);
    }

    public boolean hasProgress() {
        return this.getComponent(TimeComponent.TYPE) != null;
    }

    public static class Builder<R extends MachineRecipe> {
        private final ResourceLocation id;
        private final BiFunction<ResourceLocation, Map<String, RecipeComponent>, R> recipeFactory;
        private final Map<String, RecipeComponent> components;

        private Builder(ResourceLocation id, BiFunction<ResourceLocation, Map<String, RecipeComponent>, R> recipeFactory) {
            this.id = id;
            this.recipeFactory = recipeFactory;
            this.components = new LinkedHashMap<>();
        }

        public Builder<R> component(RecipeComponent component) {
            this.components.put(this.getLayout().getComponentKey(component.type()), component);
            return this;
        }

        private MachineRecipeLayout<?> getLayout() {
            return RegisterRecipeLayoutEvent.LAYOUTS.get(this.id);
        }

        public R build() {
            return this.recipeFactory.apply(this.id, this.components);
        }

    }

}
