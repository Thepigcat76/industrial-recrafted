package com.portingdeadmods.indrec.impl.recipes.components;

import com.mojang.serialization.Codec;
import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.api.recipes.RecipeComponent;
import com.portingdeadmods.portingdeadlibs.utils.codec.CodecUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;

public record EnumRecipeComponent<E extends Enum<E> & StringRepresentable>(E value) implements RecipeComponent {
    public static final ResourceLocation ID = IndustrialRecrafted.rl("enum");

    @Override
    public Type<EnumRecipeComponent<E>> type() {
        return EnumRecipeComponent.createType(value.getClass());
    }

    public static <E extends Enum<E> & StringRepresentable> Type<EnumRecipeComponent<E>> createType(Class<?> clazz) {
        E[] enumConstants = (E[]) clazz.getEnumConstants();
        Codec<EnumRecipeComponent<E>> codec = StringRepresentable.fromEnum(() -> enumConstants).xmap(EnumRecipeComponent::new, EnumRecipeComponent::value);
        StreamCodec<RegistryFriendlyByteBuf, EnumRecipeComponent<E>> streamCodec = CodecUtils.enumStreamCodec((Class<E>) clazz).map(EnumRecipeComponent::new, EnumRecipeComponent::value).cast();
        return new Type<>(ID, codec, streamCodec);
    }

    private static <E extends Enum<E> & StringRepresentable> E[] getEnumConstants(Class<E> clazz) {
        return clazz.getEnumConstants();
    }

}
