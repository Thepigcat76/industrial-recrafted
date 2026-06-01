package com.portingdeadmods.indrec.api.recipes;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import com.portingdeadmods.indrec.content.recipes.MachineRecipeInput;
import com.portingdeadmods.indrec.content.recipes.components.energy.EnergyOutputComponent;
import com.portingdeadmods.indrec.content.recipes.flags.FluidInputComponentFlag;
import com.portingdeadmods.indrec.content.recipes.flags.FluidOutputComponentFlag;
import com.portingdeadmods.indrec.content.recipes.flags.ItemInputComponentFlag;
import com.portingdeadmods.indrec.content.recipes.flags.ItemOutputComponentFlag;
import com.portingdeadmods.indrec.registries.IRRecipeComponentFlags;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class MachineRecipeLayout<R extends MachineRecipe<?> & Recipe<?>> {
    private final ResourceLocation id;
    private BiFunction<ResourceLocation, Map<String, RecipeComponent>, R> recipeFactory;
    private RecipeType<R> recipeType;
    private RecipeSerializer<R> recipeSerializer;
    private final Map<RecipeComponent.Type<?>, String> components;
    private final Map<RecipeComponent.Type<?>, Supplier<? extends RecipeComponent>> defaultComponentValues;

    public MachineRecipeLayout(ResourceLocation id, RecipeType<R> recipeType, BiFunction<ResourceLocation, Map<String, RecipeComponent>, R> recipeFactory) {
        this.id = id;
        this.recipeFactory = recipeFactory;
        this.components = new LinkedHashMap<>();
        this.defaultComponentValues = new LinkedHashMap<>();
        this.recipeType = recipeType;
    }

    public MachineRecipeLayout(ResourceLocation id, BiFunction<ResourceLocation, Map<String, RecipeComponent>, R> recipeFactory) {
        this(id, null, recipeFactory);
    }

    protected <C extends RecipeComponent> void addComponent(RecipeComponent.Type<C> type, String id) {
        this.components.put(type, id);
    }

    protected <C extends RecipeComponent> void addComponent(RecipeComponent.Type<C> type, String id, Supplier<C> defaultComponent) {
        this.addComponent(type, id);
        this.defaultComponentValues.put(type, defaultComponent);
    }

    public String getComponentKey(RecipeComponent.Type<?> componentType) {
        return this.components.get(componentType);
    }

    public Map<RecipeComponent.Type<?>, String> getComponents() {
        return components;
    }

    public ResourceLocation getId() {
        return id;
    }

    private MapCodec<R> createMapCodec(BiFunction<ResourceLocation, Map<String, RecipeComponent>, R> factory) {
        return new MapCodec<>() {
            @Override
            public <T> Stream<T> keys(DynamicOps<T> ops) {
                return components.values().stream().map(ops::createString);
            }

            @Override
            public <T> DataResult<R> decode(DynamicOps<T> ops, MapLike<T> input) {
                Map<String, RecipeComponent> recipeComponents = new LinkedHashMap<>();

                for (Map.Entry<RecipeComponent.Type<?>, String> entry : components.entrySet()) {
                    T val = input.get(entry.getValue());
                    RecipeComponent.Type<?> type = entry.getKey();
                    Codec<? extends RecipeComponent> codec = type.codec();
                    Supplier<? extends RecipeComponent> defaultComponentValue = defaultComponentValues.get(entry.getKey());
                    if (codec != null) {
                        DataResult<? extends Pair<? extends RecipeComponent, T>> result = codec.decode(ops, val);
                        if (result.isSuccess()) {
                            RecipeComponent component = result.getOrThrow().getFirst();
                            recipeComponents.put(entry.getValue(), component);
                        } else {
                            if (defaultComponentValue != null) {
                                recipeComponents.put(entry.getValue(), defaultComponentValue.get());
                            } else {
                                return DataResult.error(() -> "Failed to decode Recipe Component: " + result.error().get().message(), factory.apply(id, recipeComponents));
                            }
                        }
                    } else {
                        if (defaultComponentValue != null) {
                            recipeComponents.put(entry.getValue(), defaultComponentValue.get());
                        } else {
                            return DataResult.error(() -> "Failed to decode Recipe Component, neither a codec nor default value was provided for component of type: " + entry.getValue(), factory.apply(id, recipeComponents));
                        }
                    }
                }
                return DataResult.success(factory.apply(id, recipeComponents));
            }

            @Override
            public <T> RecordBuilder<T> encode(R input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
                for (Map.Entry<String, ? extends RecipeComponent> entry : input.getComponents().entrySet()) {
                    RecipeComponent.Type<?> type = entry.getValue().type();
                    Codec<RecipeComponent> rawCodec = type.rawCodec();
                    if (rawCodec != null) {
                        DataResult<T> result = rawCodec.encodeStart(ops, entry.getValue());
                        if (result.isSuccess()) {
                            if (entry.getKey() == null) {
                                System.out.println("Oops key is null");
                            }
                            prefix.add(entry.getKey(), result.getOrThrow());
                        } else {
                            throw new IllegalStateException("Failed to encode recipe");
                        }
                    }
                }
                return prefix;
            }
        };
    }

    public R createRecipe(ResourceLocation id, Map<String, RecipeComponent> components) {
        return this.recipeFactory.apply(id, components);
    }

    public RecipeSerializer<R> getRecipeSerializer() {
        if (this.recipeSerializer == null) {
            MapCodec<R> mapCodec = this.createMapCodec(this::createRecipe);
            StreamCodec<RegistryFriendlyByteBuf, R> streamCodec = ByteBufCodecs.fromCodecWithRegistriesTrusted(mapCodec.codec()).cast();
            this.recipeSerializer = new RecipeSerializer<>() {
                @Override
                public @NotNull MapCodec<R> codec() {
                    return mapCodec;
                }

                @Override
                public @NotNull StreamCodec<RegistryFriendlyByteBuf, R> streamCodec() {
                    return streamCodec;
                }
            };
        }
        return this.recipeSerializer;
    }

    public RecipeType<R> getRecipeType() {
        if (this.recipeType == null) {
            this.recipeType = RecipeType.simple(this.id);
        }
        return this.recipeType;
    }

    /* Recipe related methods */

    public boolean matches(R recipe, MachineRecipeInput input, Level level) {
        ItemInputComponentFlag inputComp = recipe.getComponentByFlag(IRRecipeComponentFlags.ITEM_INPUT);
        if (inputComp != null && !inputComp.test(input.items(), false)) {
            return false;
        }
        FluidInputComponentFlag fluidInputComp = recipe.getComponentByFlag(IRRecipeComponentFlags.FLUID_INPUT);
        if (fluidInputComp != null && !fluidInputComp.test(input.fluids(), false)) {
            return false;
        }
        return true;
    }

    public ItemStack createResultItem(R recipe, @Nullable MachineRecipeInput input, HolderLookup.Provider provider) {
        ItemOutputComponentFlag output = recipe.getComponentByFlag(IRRecipeComponentFlags.ITEM_OUTPUT);
        if (output != null) {
            return output.getOutputs().getFirst();
        }
        return ItemStack.EMPTY;
    }

    public FluidStack createResultFluid(R recipe, @Nullable MachineRecipeInput input, HolderLookup.Provider provider) {
        FluidOutputComponentFlag output = recipe.getComponentByFlag(IRRecipeComponentFlags.FLUID_OUTPUT);
        if (output != null) {
            return output.getOutputs().getFirst();
        }
        return FluidStack.EMPTY;
    }

    public int createResultEnergy(R recipe, @Nullable MachineRecipeInput input, HolderLookup.Provider provider) {
        EnergyOutputComponent output = recipe.getComponent(EnergyOutputComponent.TYPE);
        if (output != null) {
            return output.energy();
        }
        return 0;
    }

    public <C extends RecipeComponent> C getComponent(R recipe, RecipeComponent.Type<C> type) {
        for (RecipeComponent value : recipe.getComponents().values()) {
            if (value.type().equals(type)) {
                return (C) value;
            }
        }
        return null;
    }

    public <F extends RecipeComponentFlag> F getComponentByFlag(R recipe, RecipeFlagType<F> flagType) {
        for (RecipeComponent component : recipe.getComponents().values()) {
            if (component.flags().contains(flagType)) {
                return (F) component;
            }
        }
        return null;
    }

}
