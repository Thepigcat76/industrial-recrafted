package com.portingdeadmods.indrec.content.recipes.components.fluids;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.portingdeadmods.indrec.IndustrialReclassified;
import com.portingdeadmods.indrec.api.recipes.RecipeComponent;
import com.portingdeadmods.indrec.api.recipes.RecipeFlagType;
import com.portingdeadmods.indrec.content.recipes.flags.FluidOutputComponentFlag;
import com.portingdeadmods.indrec.registries.IRRecipeComponentFlags;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;
import java.util.Set;

public record FluidOutputComponent(FluidStack fluid, float chance) implements RecipeComponent, FluidOutputComponentFlag {
    public static final Codec<FluidOutputComponent> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            FluidStack.CODEC.fieldOf("fluid").forGetter(FluidOutputComponent::fluid),
            Codec.FLOAT.optionalFieldOf("chances", 1f).forGetter(FluidOutputComponent::chance)
    ).apply(inst, FluidOutputComponent::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, FluidOutputComponent> STREAM_CODEC = StreamCodec.composite(
            FluidStack.STREAM_CODEC,
            FluidOutputComponent::fluid,
            ByteBufCodecs.FLOAT,
            FluidOutputComponent::chance,
            FluidOutputComponent::new
    );
    public static final Type<FluidOutputComponent> TYPE = new Type<>(IndustrialReclassified.rl("fluid_output"), CODEC, STREAM_CODEC);
    public static final Set<RecipeFlagType<?>> FLAGS = Set.of(IRRecipeComponentFlags.FLUID_OUTPUT);

    public FluidOutputComponent(FluidStack fluid) {
        this(fluid, 1);
    }

    @Override
    public Type<?> type() {
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
    public List<FluidStack> getOutputs() {
        return List.of(fluid);
    }

    @Override
    public List<Float> getChances() {
        return List.of(chance);
    }

    @Override
    public boolean isOutputted(RandomSource random, int tank) {
        return random.nextFloat() < this.chance();
    }
}
