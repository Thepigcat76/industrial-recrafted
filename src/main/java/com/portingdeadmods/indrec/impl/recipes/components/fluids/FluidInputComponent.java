package com.portingdeadmods.indrec.impl.recipes.components.fluids;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.api.recipes.RecipeComponent;
import com.portingdeadmods.indrec.api.recipes.RecipeFlagType;
import com.portingdeadmods.indrec.impl.recipes.flags.FluidInputComponentFlag;
import com.portingdeadmods.indrec.registries.IRRecipeComponentFlags;
import com.portingdeadmods.portingdeadlibs.api.recipes.FluidIngredientWithAmount;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.TagFluidIngredient;

import java.util.List;
import java.util.Set;

public record FluidInputComponent(FluidIngredient ingredient, int amount, float chance) implements RecipeComponent, FluidInputComponentFlag {
    public static final Codec<FluidInputComponent> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            FluidIngredient.CODEC.fieldOf("ingredient").forGetter(FluidInputComponent::ingredient),
            Codec.INT.optionalFieldOf("amount", 1000).forGetter(FluidInputComponent::amount),
            Codec.FLOAT.optionalFieldOf("chances", 1f).forGetter(FluidInputComponent::chance)
    ).apply(inst, FluidInputComponent::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, FluidInputComponent> STREAM_CODEC = StreamCodec.composite(
            FluidIngredient.STREAM_CODEC,
            FluidInputComponent::ingredient,
            ByteBufCodecs.INT,
            FluidInputComponent::amount,
            ByteBufCodecs.FLOAT,
            FluidInputComponent::chance,
            FluidInputComponent::new
    );
    public static final Type<FluidInputComponent> TYPE = new Type<>(IndustrialRecrafted.rl("fluid_input"), CODEC, STREAM_CODEC);
    public static final Set<RecipeFlagType<?>> FLAGS = Set.of(IRRecipeComponentFlags.FLUID_INPUT);

    public FluidInputComponent(FluidIngredient ingredient, int amount) {
        this(ingredient, amount, 1);
    }

    public FluidInputComponent(Fluid fluid, int amount) {
        this(FluidIngredient.of(fluid), amount, 1);
    }

    public FluidInputComponent(TagKey<Fluid> fluid, int amount) {
        this(new TagFluidIngredient(fluid), amount, 1);
    }

    @Override
    public Type<?> type() {
        return TYPE;
    }

    @Override
    public Set<RecipeFlagType<?>> flags() {
        return FLAGS;
    }

    public boolean test(FluidStack fluidStack) {
        return this.ingredient().test(fluidStack) && fluidStack.getAmount() >= this.amount();
    }

    public boolean isConsumed(RandomSource random) {
        return random.nextFloat() < this.chance();
    }

    @Override
    public List<FluidIngredientWithAmount> getIngredients() {
        return List.of(new FluidIngredientWithAmount(ingredient, amount));
    }

    @Override
    public List<Float> getChances() {
        return List.of(chance);
    }

    @Override
    public boolean test(List<FluidStack> fluids, boolean strict) {
        if (strict) {
            return fluids.size() == 1 && this.ingredient.test(fluids.getFirst()) && fluids.getFirst().getAmount() >= this.amount;
        }
        for (FluidStack fluid : fluids) {
            return this.test(fluid);
        }
        return false;
    }

}
