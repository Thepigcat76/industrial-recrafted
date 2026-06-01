package com.portingdeadmods.indrec.content.recipes.components.items;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.registries.IRRecipeComponentFlags;
import com.portingdeadmods.indrec.api.recipes.RecipeComponent;
import com.portingdeadmods.indrec.content.recipes.flags.ItemOutputComponentFlag;
import com.portingdeadmods.indrec.api.recipes.RecipeFlagType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.Set;

public record ItemOutputComponent(ItemStack item, float chance) implements RecipeComponent, ItemOutputComponentFlag {
    public static final Codec<ItemOutputComponent> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ItemStack.OPTIONAL_CODEC.fieldOf("items").forGetter(ItemOutputComponent::item),
            Codec.FLOAT.optionalFieldOf("chance", 1f).forGetter(ItemOutputComponent::chance)
    ).apply(inst, ItemOutputComponent::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemOutputComponent> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            ItemOutputComponent::item,
            ByteBufCodecs.FLOAT,
            ItemOutputComponent::chance,
            ItemOutputComponent::new
    );
    public static final Type<ItemOutputComponent> TYPE = new Type<>(IndustrialRecrafted.rl("item_output"), CODEC, STREAM_CODEC);
    public static final Set<RecipeFlagType<?>> FLAGS = Set.of(IRRecipeComponentFlags.ITEM_OUTPUT);

    public ItemOutputComponent(ItemStack item) {
        this(item, 1);
    }

    public ItemOutputComponent(ItemLike item) {
        this(item, 1);
    }

    public ItemOutputComponent(ItemLike item, int count) {
        this(new ItemStack(item, count), 1f);
    }

    public ItemOutputComponent(ItemLike item, float chance) {
        this(new ItemStack(item), chance);
    }

    @Override
    public RecipeComponent.Type<?> type() {
        return TYPE;
    }

    @Override
    public Set<RecipeFlagType<?>> flags() {
        return FLAGS;
    }

    public boolean isOutputted(RandomSource random) {
        return random.nextFloat() < this.chance();
    }

    @Override
    public List<ItemStack> getOutputs() {
        return List.of(this.item());
    }

    @Override
    public List<Float> getChances() {
        return List.of(this.chance());
    }

    @Override
    public boolean isOutputted(RandomSource random, int slot) {
        return random.nextFloat() < this.chance();
    }
}
