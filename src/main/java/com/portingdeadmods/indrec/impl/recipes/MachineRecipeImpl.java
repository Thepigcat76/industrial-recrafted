package com.portingdeadmods.indrec.impl.recipes;

import com.portingdeadmods.indrec.api.recipes.*;
import com.portingdeadmods.indrec.impl.recipes.components.TimeComponent;
import com.portingdeadmods.indrec.impl.recipes.components.energy.EnergyOutputComponent;
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

public class MachineRecipeImpl implements MachineRecipe<MachineRecipeInput>, PDLRecipe<MachineRecipeInput> {
    private RecipeSerializer<? extends MachineRecipeImpl> serializer;
    private RecipeType<? extends MachineRecipeImpl> type;
    private MachineRecipeLayout<MachineRecipeImpl> layout;
    private final Map<String, RecipeComponent> components;
    private final ResourceLocation id;

    public MachineRecipeImpl(ResourceLocation id, Map<String, RecipeComponent> components) {
        this.id = id;
        this.components = components;
    }

    public ResourceLocation id() {
        return id;
    }

    public MachineRecipeImpl(ResourceLocation id) {
        this(id, new LinkedHashMap<>());
    }

    public <R extends RecipeComponent> R getComponent(RecipeComponent.Type<R> type) {
        return this.getLayout().getComponent(this, type);
    }

    public <F extends RecipeComponentFlag> F getComponentByFlag(RecipeFlagType<F> flagType) {
        return this.getLayout().getComponentByFlag(this, flagType);
    }

    @Override
    public boolean matches(@NotNull MachineRecipeInput input, @NotNull Level level) {
        return matchesItems(input, level);
    }

    private <R extends MachineRecipeImpl> boolean matchesItems(MachineRecipeInput input, Level level) {
        MachineRecipeLayout<R> layout = (MachineRecipeLayout<R>) this.getLayout();
        return layout.matches((R) this, input, level);
    }

    @Override
    public boolean hasResultItem(HolderLookup.Provider provider) {
        return this.getComponentByFlag(IRRecipeComponentFlags.ITEM_OUTPUT) != null;
    }

    @Override
    public boolean hasResultFluid(HolderLookup.Provider provider) {
        return this.getComponentByFlag(IRRecipeComponentFlags.FLUID_OUTPUT) != null;
    }

    @Override
    public boolean hasResultEnergy(HolderLookup.Provider provider) {
        return this.getComponent(EnergyOutputComponent.TYPE) != null;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider provider) {
        return this.assembleResultItem(null, provider);
    }

    @Override
    public ItemStack assembleResultItem(MachineRecipeInput input, HolderLookup.Provider provider) {
        return createResultItem(input, provider);
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull MachineRecipeInput container, HolderLookup.@NotNull Provider provider) {
        return createResultItem(container, provider);
    }

    @Override
    public FluidStack getResultFluid(HolderLookup.Provider provider) {
        return createResultFluid(null, provider);
    }

    @Override
    public FluidStack assembleResultFluid(@NotNull MachineRecipeInput container, HolderLookup.Provider provider) {
        return createResultFluid(container, provider);
    }

    @Override
    public int getResultEnergy(HolderLookup.Provider provider) {
        return createResultEnergy(null, provider);
    }

    @Override
    public int assembleResultEnergy(@NotNull MachineRecipeInput container, HolderLookup.Provider provider) {
        return createResultEnergy(container, provider);
    }

    private <R extends MachineRecipeImpl> int createResultEnergy(MachineRecipeInput input, HolderLookup.Provider provider) {
        MachineRecipeLayout<R> layout = (MachineRecipeLayout<R>) this.getLayout();
        return layout.createResultEnergy((R) this, input, provider);
    }

    private <R extends MachineRecipeImpl> FluidStack createResultFluid(MachineRecipeInput input, HolderLookup.Provider provider) {
        MachineRecipeLayout<R> layout = (MachineRecipeLayout<R>) this.getLayout();
        return layout.createResultFluid((R) this, input, provider);
    }

    private <R extends MachineRecipeImpl> ItemStack createResultItem(MachineRecipeInput input, HolderLookup.Provider provider) {
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

    @Override
    public MachineRecipeLayout<MachineRecipeImpl> getLayout() {
        if (this.layout == null) {
            this.layout = (MachineRecipeLayout<MachineRecipeImpl>) RegisterRecipeLayoutEvent.LAYOUTS.get(this.id);
        }
        return this.layout;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        if (this.type == null) {
            this.type = getLayout().getRecipeType();
        }
        return this.type;
    }

    @Override
    public Map<String, RecipeComponent> getComponents() {
        return this.components;
    }

    public static <R extends MachineRecipeImpl> Builder<R> builder(ResourceLocation id, BiFunction<ResourceLocation, Map<String, RecipeComponent>, R> recipeFactory) {
        return new Builder<>(id, recipeFactory);
    }

    public static Builder<MachineRecipeImpl> builder(ResourceLocation id) {
        return new Builder<>(id, MachineRecipeImpl::new);
    }

    public static Builder<MachineRecipeImpl> builder(MachineRecipeLayout<MachineRecipeImpl> layout) {
        return new Builder<>(layout.getId(), MachineRecipeImpl::new);
    }

    @Override
    public boolean hasProgress() {
        return this.getComponent(TimeComponent.TYPE) != null;
    }

    @Override
    public int getMaxProgress() {
        return this.getComponent(TimeComponent.TYPE).time();
    }

    public static class Builder<R extends MachineRecipeImpl> {
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
