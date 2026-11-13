package com.portingdeadmods.indrec.api.crops;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public record SeedDrop(ItemStack item, float chance) {
    public static final Codec<SeedDrop> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ItemStack.CODEC.fieldOf("item").forGetter(SeedDrop::item),
            Codec.floatRange(0, 1).optionalFieldOf("chance", 1f).forGetter(SeedDrop::chance)
    ).apply(inst, SeedDrop::new));

    public Optional<ItemStack> drops(RandomSource random) {
        return this.chance >= 1 || random.nextFloat() < this.chance ? Optional.of(this.item.copy()) : Optional.empty();
    }
}
