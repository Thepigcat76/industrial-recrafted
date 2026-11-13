package com.portingdeadmods.indrec.api.crops.requirements;

import com.mojang.serialization.Codec;
import com.portingdeadmods.indrec.IRRegistries;
import com.portingdeadmods.indrec.api.crops.Crop;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.LevelReader;

public interface CropRequirement {
    Codec<CropRequirement> CODEC = IRRegistries.CROP_REQUIREMENT_SERIALIZER.byNameCodec()
            .dispatch(CropRequirement::serializer, CropRequirementSerializer::codec);
    StreamCodec<RegistryFriendlyByteBuf, CropRequirement> STREAM_CODEC = ByteBufCodecs.registry(IRRegistries.CROP_REQUIREMENT_SERIALIZER.key())
            .dispatch(CropRequirement::serializer, CropRequirementSerializer::streamCodec);

    boolean test(Holder<Crop> crop, LevelReader level, BlockPos pos);

    CropRequirementSerializer<?> serializer();
}
