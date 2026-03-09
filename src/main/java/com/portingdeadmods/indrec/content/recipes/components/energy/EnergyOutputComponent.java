package com.portingdeadmods.indrec.content.recipes.components.energy;

import com.mojang.serialization.Codec;
import com.portingdeadmods.indrec.IndustrialReclassified;
import com.portingdeadmods.indrec.api.recipes.RecipeComponent;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record EnergyOutputComponent(int energy) implements RecipeComponent {
    public static final Codec<EnergyOutputComponent> CODEC = Codec.INT.xmap(EnergyOutputComponent::new, EnergyOutputComponent::energy);
    public static final StreamCodec<RegistryFriendlyByteBuf, EnergyOutputComponent> STREAM_CODEC = ByteBufCodecs.INT.map(EnergyOutputComponent::new, EnergyOutputComponent::energy).cast();
    public static final Type<EnergyOutputComponent> TYPE = new Type<>(IndustrialReclassified.rl("energy_output"), CODEC, STREAM_CODEC);

    @Override
    public Type<?> type() {
        return TYPE;
    }
}
