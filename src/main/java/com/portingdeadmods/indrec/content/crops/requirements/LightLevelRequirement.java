package com.portingdeadmods.indrec.content.crops.requirements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.portingdeadmods.indrec.api.crops.Crop;
import com.portingdeadmods.indrec.api.crops.requirements.CropRequirement;
import com.portingdeadmods.indrec.api.crops.requirements.CropRequirementSerializer;
import com.portingdeadmods.indrec.registries.IRCropRequirementSerializers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.LevelReader;

/**
 * Requires the light level at the crop to be between {@code min} and {@code max} (both inclusive).
 * Uses the same light level as vanilla crops (the highest of block and sky light, without the sky darkening at night).
 */
public record LightLevelRequirement(int min, int max) implements CropRequirement {
    private static final Codec<Integer> LIGHT_CODEC = Codec.intRange(0, 15);
    public static final MapCodec<LightLevelRequirement> CODEC = RecordCodecBuilder.<LightLevelRequirement>mapCodec(inst -> inst.group(
            LIGHT_CODEC.optionalFieldOf("min", 0).forGetter(LightLevelRequirement::min),
            LIGHT_CODEC.optionalFieldOf("max", 15).forGetter(LightLevelRequirement::max)
    ).apply(inst, LightLevelRequirement::new)).validate(req -> req.min() <= req.max()
            ? DataResult.success(req)
            : DataResult.error(() -> "Min light level " + req.min() + " is greater than max light level " + req.max()));
    public static final StreamCodec<RegistryFriendlyByteBuf, LightLevelRequirement> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, LightLevelRequirement::min,
            ByteBufCodecs.VAR_INT, LightLevelRequirement::max,
            LightLevelRequirement::new
    );

    public static LightLevelRequirement of(int min, int max) {
        return new LightLevelRequirement(min, max);
    }

    public static LightLevelRequirement min(int min) {
        return new LightLevelRequirement(min, 15);
    }

    public static LightLevelRequirement max(int max) {
        return new LightLevelRequirement(0, max);
    }

    @Override
    public boolean test(Holder<Crop> crop, LevelReader level, BlockPos pos) {
        int light = level.getRawBrightness(pos, 0);
        return light >= this.min && light <= this.max;
    }

    @Override
    public CropRequirementSerializer<?> serializer() {
        return IRCropRequirementSerializers.LIGHT_LEVEL.get();
    }
}
