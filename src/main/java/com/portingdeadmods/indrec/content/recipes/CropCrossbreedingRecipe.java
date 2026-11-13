package com.portingdeadmods.indrec.content.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.portingdeadmods.indrec.IRRegistries;
import com.portingdeadmods.indrec.api.crops.Crop;
import com.portingdeadmods.indrec.registries.IRRecipes;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Two parent crops (in any order) can crossbreed into the result crop.
 * <p>
 * JSON: {@code {"type": "indrec:crop_crossbreeding", "parents": ["indrec:wheat", "indrec:weed"], "result": "indrec:stick_reed",
 * "chance": 0.25, "first_parent_spreads": true, "second_parent_spreads": false}}
 *
 * @param chance              chance (0-1) of the crossbreeding succeeding, defaults to 1
 * @param firstParentSpreads  whether the first parent can spread onto the crossbreed sticks instead of the result being planted, defaults to true
 * @param secondParentSpreads whether the second parent can spread onto the crossbreed sticks instead of the result being planted, defaults to true
 */
public record CropCrossbreedingRecipe(ResourceKey<Crop> firstParent, ResourceKey<Crop> secondParent,
                                      ResourceKey<Crop> result, float chance,
                                      boolean firstParentSpreads, boolean secondParentSpreads) implements Recipe<CropCrossbreedingRecipe.Input> {
    private static final Codec<ResourceKey<Crop>> CROP_KEY_CODEC = ResourceKey.codec(IRRegistries.CROP);

    public static final MapCodec<CropCrossbreedingRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            CROP_KEY_CODEC.listOf(2, 2).fieldOf("parents").forGetter(recipe -> List.of(recipe.firstParent(), recipe.secondParent())),
            CROP_KEY_CODEC.fieldOf("result").forGetter(CropCrossbreedingRecipe::result),
            Codec.floatRange(0, 1).optionalFieldOf("chance", 1f).forGetter(CropCrossbreedingRecipe::chance),
            Codec.BOOL.optionalFieldOf("first_parent_spreads", true).forGetter(CropCrossbreedingRecipe::firstParentSpreads),
            Codec.BOOL.optionalFieldOf("second_parent_spreads", true).forGetter(CropCrossbreedingRecipe::secondParentSpreads)
    ).apply(inst, (parents, result, chance, firstSpreads, secondSpreads) ->
            new CropCrossbreedingRecipe(parents.get(0), parents.get(1), result, chance, firstSpreads, secondSpreads)));
    public static final StreamCodec<RegistryFriendlyByteBuf, CropCrossbreedingRecipe> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(IRRegistries.CROP),
            CropCrossbreedingRecipe::firstParent,
            ResourceKey.streamCodec(IRRegistries.CROP),
            CropCrossbreedingRecipe::secondParent,
            ResourceKey.streamCodec(IRRegistries.CROP),
            CropCrossbreedingRecipe::result,
            ByteBufCodecs.FLOAT,
            CropCrossbreedingRecipe::chance,
            ByteBufCodecs.BOOL,
            CropCrossbreedingRecipe::firstParentSpreads,
            ByteBufCodecs.BOOL,
            CropCrossbreedingRecipe::secondParentSpreads,
            CropCrossbreedingRecipe::new
    );

    public List<ResourceKey<Crop>> spreadingParents() {
        List<ResourceKey<Crop>> parents = new ArrayList<>(2);
        if (this.firstParentSpreads) parents.add(this.firstParent);
        if (this.secondParentSpreads) parents.add(this.secondParent);
        return parents;
    }

    @Override
    public boolean matches(Input input, @NotNull Level level) {
        return (input.first().is(this.firstParent) && input.second().is(this.secondParent))
                || (input.first().is(this.secondParent) && input.second().is(this.firstParent));
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull Input input, HolderLookup.@NotNull Provider registries) {
        return this.getResultItem(registries);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    /**
     * @return the seed of the result crop, or an empty stack if it has none
     */
    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider registries) {
        return registries.lookup(IRRegistries.CROP)
                .flatMap(lookup -> lookup.get(this.result))
                .flatMap(crop -> crop.value().createSeedStack())
                .orElse(ItemStack.EMPTY);
    }

    // Not shown in the recipe book
    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return IRRecipes.CROP_CROSSBREEDING_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return IRRecipes.CROP_CROSSBREEDING_TYPE.get();
    }

    public record Input(Holder<Crop> first, Holder<Crop> second) implements RecipeInput {
        @Override
        public @NotNull ItemStack getItem(int index) {
            return ItemStack.EMPTY;
        }

        @Override
        public int size() {
            return 0;
        }
    }

    public static final class Serializer implements RecipeSerializer<CropCrossbreedingRecipe> {
        @Override
        public @NotNull MapCodec<CropCrossbreedingRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, CropCrossbreedingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
