package com.portingdeadmods.indrec.content.recipes.components.energy;

import com.mojang.serialization.Codec;
import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.api.recipes.RecipeComponent;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record EnergyInputComponent(int energy) implements RecipeComponent {
    public static final Codec<EnergyInputComponent> CODEC = Codec.INT.xmap(EnergyInputComponent::new, EnergyInputComponent::energy);
    public static final StreamCodec<RegistryFriendlyByteBuf, EnergyInputComponent> STREAM_CODEC = ByteBufCodecs.INT.map(EnergyInputComponent::new, EnergyInputComponent::energy).cast();
    public static final Type<EnergyInputComponent> TYPE = new Type<>(IndustrialRecrafted.rl("energy_input"), CODEC, STREAM_CODEC);

    @Override
    public Type<?> type() {
        return TYPE;
    }
}
