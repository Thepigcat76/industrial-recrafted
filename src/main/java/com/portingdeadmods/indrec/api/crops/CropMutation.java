package com.portingdeadmods.indrec.api.crops;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.portingdeadmods.indrec.IRRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;

public record CropMutation(ResourceKey<Crop> result, float chance, boolean keepAge) {
    public static final Codec<CropMutation> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ResourceKey.codec(IRRegistries.CROP).fieldOf("result").forGetter(CropMutation::result),
            Codec.floatRange(0, 1).optionalFieldOf("chance", 1f).forGetter(CropMutation::chance),
            Codec.BOOL.optionalFieldOf("keep_age", true).forGetter(CropMutation::keepAge)
    ).apply(inst, CropMutation::new));

    public static CropMutation of(ResourceKey<Crop> result, float chance) {
        return new CropMutation(result, chance, true);
    }

    public static CropMutation of(ResourceKey<Crop> result, float chance, boolean keepAge) {
        return new CropMutation(result, chance, keepAge);
    }

    public boolean mutates(RandomSource random) {
        return this.chance >= 1 || random.nextFloat() < this.chance;
    }

}
