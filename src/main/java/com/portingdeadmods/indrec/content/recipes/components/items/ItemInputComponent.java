package com.portingdeadmods.indrec.content.recipes.components.items;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.registries.IRRecipeComponentFlags;
import com.portingdeadmods.indrec.api.recipes.RecipeComponent;
import com.portingdeadmods.indrec.content.recipes.flags.ItemInputComponentFlag;
import com.portingdeadmods.indrec.api.recipes.RecipeFlagType;
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

import java.util.List;
import java.util.Set;

public record ItemInputComponent(Ingredient ingredient, int count, float chance) implements RecipeComponent, ItemInputComponentFlag {
    public static final Codec<ItemInputComponent> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(ItemInputComponent::ingredient),
            Codec.INT.optionalFieldOf("count", 1).forGetter(ItemInputComponent::count),
            Codec.FLOAT.optionalFieldOf("chance", 1f).forGetter(ItemInputComponent::chance)
    ).apply(inst, ItemInputComponent::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemInputComponent> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            ItemInputComponent::ingredient,
            ByteBufCodecs.INT,
            ItemInputComponent::count,
            ByteBufCodecs.FLOAT,
            ItemInputComponent::chance,
            ItemInputComponent::new
    );
    public static final Type<ItemInputComponent> TYPE = new Type<>(IndustrialRecrafted.rl("item_input"), CODEC, STREAM_CODEC);
    public static final Set<RecipeFlagType<?>> FLAGS = Set.of(IRRecipeComponentFlags.ITEM_INPUT);

    public ItemInputComponent(Ingredient ingredient, int count) {
        this(ingredient, count, 1);
    }

    public ItemInputComponent(Ingredient ingredient) {
        this(ingredient, 1);
    }

    public ItemInputComponent(ItemLike item) {
        this(item, 1);
    }

    public ItemInputComponent(ItemLike item, int count) {
        this(Ingredient.of(item), count);
    }

    public ItemInputComponent(TagKey<Item> item, int count) {
        this(Ingredient.of(item), count);
    }

    public ItemInputComponent(TagKey<Item> tag) {
        this(tag, 1);
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
            return items.size() == 1 && this.test(items.getFirst());
        } else if (!items.isEmpty()) {
            for (ItemStack item : items) {
                if (this.test(item)) return true;
            }
        }
        return false;
    }

    public boolean test(ItemStack itemStack) {
        return this.ingredient().test(itemStack) && itemStack.getCount() >= this.count();
    }

    public boolean isConsumed(RandomSource random) {
        return random.nextFloat() < this.chance();
    }

    @Override
    public List<IngredientWithCount> getIngredients() {
        return List.of(new IngredientWithCount(this.ingredient(), this.count()));
    }

    @Override
    public List<Float> getChances() {
        return List.of(this.chance());
    }
}
