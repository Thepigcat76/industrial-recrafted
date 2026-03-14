package com.portingdeadmods.indrec.content.recipes.components.items;

import com.mojang.serialization.Codec;
import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.api.recipes.RecipeComponent;
import com.portingdeadmods.indrec.api.recipes.RecipeFlagType;
import com.portingdeadmods.indrec.content.recipes.flags.ItemInputComponentFlag;
import com.portingdeadmods.indrec.registries.IRRecipeComponentFlags;
import com.portingdeadmods.indrec.utils.IRRecipeUtils;
import com.portingdeadmods.portingdeadlibs.api.recipes.IngredientWithCount;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public record ItemInputListComponent(List<ItemInputComponent> inputs) implements RecipeComponent, ItemInputComponentFlag {
    public static final Codec<ItemInputListComponent> CODEC = ItemInputComponent.CODEC.listOf().xmap(ItemInputListComponent::new, ItemInputListComponent::inputs);
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemInputListComponent> STREAM_CODEC = ItemInputComponent.STREAM_CODEC.apply(ByteBufCodecs.list()).map(ItemInputListComponent::new, ItemInputListComponent::inputs);
    public static final Type<ItemInputListComponent> TYPE = new Type<>(IndustrialRecrafted.rl("item_input_list"), CODEC, STREAM_CODEC);
    public static final Set<RecipeFlagType<?>> FLAGS = Set.of(IRRecipeComponentFlags.ITEM_INPUT);

    public ItemInputListComponent(Ingredient ingredient, int count) {
        this(List.of(new ItemInputComponent(ingredient, count, 1)));
    }

    public ItemInputListComponent(Ingredient ...ingredients) {
        this(Arrays.stream(ingredients).map(ItemInputComponent::new).toList());
    }

    public ItemInputListComponent(ItemLike ...items) {
        this(Arrays.stream(items).map(ItemInputComponent::new).toList());
    }

    public ItemInputListComponent(TagKey<Item> tag) {
        this(Ingredient.of(tag), 1);
    }

    @Override
    public Set<RecipeFlagType<?>> flags() {
        return FLAGS;
    }

    @Override
    public Type<?> type() {
        return TYPE;
    }

    @Override
    public boolean test(List<ItemStack> items, boolean strict) {
        if (strict) {
            return items.size() == this.inputs.size() && this.test(items);
        } else if (!items.isEmpty()) {
            return this.test(items);
        }
        return false;
    }

    public boolean test(List<ItemStack> items) {
        return IRRecipeUtils.matches(items, this.inputs());
    }

    public boolean isConsumed(RandomSource random, int slot) {
        return random.nextFloat() < this.inputs.get(slot).chance();
    }

    @Override
    public List<IngredientWithCount> getIngredients() {
        return this.inputs().stream().map(i -> new IngredientWithCount(i.ingredient(), i.count())).toList();
    }

    @Override
    public List<Float> getChances() {
        return this.inputs().stream().map(ItemInputComponent::chance).toList();
    }

}
