package com.portingdeadmods.indrec.registries;

import com.portingdeadmods.indrec.IRRegistries;
import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.api.crops.requirements.CropRequirementSerializer;
import com.portingdeadmods.indrec.content.crops.requirements.BlockBelowSoilRequirement;
import com.portingdeadmods.indrec.content.crops.requirements.LightLevelRequirement;
import com.portingdeadmods.indrec.content.crops.requirements.SoilRequirement;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class IRCropRequirementSerializers {
    public static final DeferredRegister<CropRequirementSerializer<?>> SERIALIZERS = DeferredRegister.create(IRRegistries.CROP_REQUIREMENT_SERIALIZER, IndustrialRecrafted.MODID);

    public static final Supplier<CropRequirementSerializer<BlockBelowSoilRequirement>> BLOCK_BELOW_SOIL = SERIALIZERS.register("block_below_soil",
            () -> CropRequirementSerializer.of(BlockBelowSoilRequirement.CODEC, BlockBelowSoilRequirement.STREAM_CODEC));
    public static final Supplier<CropRequirementSerializer<LightLevelRequirement>> LIGHT_LEVEL = SERIALIZERS.register("light_level",
            () -> CropRequirementSerializer.of(LightLevelRequirement.CODEC, LightLevelRequirement.STREAM_CODEC));
    public static final Supplier<CropRequirementSerializer<SoilRequirement>> SOIL = SERIALIZERS.register("soil",
            () -> CropRequirementSerializer.of(SoilRequirement.CODEC, SoilRequirement.STREAM_CODEC));

    private IRCropRequirementSerializers() {
    }
}
