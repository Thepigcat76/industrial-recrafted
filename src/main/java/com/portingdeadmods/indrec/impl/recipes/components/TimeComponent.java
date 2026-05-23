package com.portingdeadmods.indrec.impl.recipes.components;

import com.mojang.serialization.Codec;
import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.api.recipes.RecipeComponent;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record TimeComponent(int time) implements RecipeComponent {
    public static final Codec<TimeComponent> CODEC = Codec.INT.xmap(TimeComponent::new, TimeComponent::time);
    public static final StreamCodec<RegistryFriendlyByteBuf, TimeComponent> STREAM_CODEC = ByteBufCodecs.INT.map(TimeComponent::new, TimeComponent::time).cast();
    public static final Type<TimeComponent> TYPE = new Type<>(IndustrialRecrafted.rl("time"), CODEC, STREAM_CODEC);

    @Override
    public Type<?> type() {
        return TYPE;
    }
}
