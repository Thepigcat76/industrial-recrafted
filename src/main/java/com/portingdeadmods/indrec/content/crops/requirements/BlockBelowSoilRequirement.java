package com.portingdeadmods.indrec.content.crops.requirements;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.portingdeadmods.indrec.api.crops.Crop;
import com.portingdeadmods.indrec.api.crops.requirements.CropRequirement;
import com.portingdeadmods.indrec.api.crops.requirements.CropRequirementSerializer;
import com.portingdeadmods.indrec.registries.IRCropRequirementSerializers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.LevelReader;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

/**
 * Requires the block directly below the soil the crop is planted on to be one of the given blocks.
 */
public record BlockBelowSoilRequirement(HolderSet<Block> blocks) implements CropRequirement {
    public static final MapCodec<BlockBelowSoilRequirement> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("blocks").forGetter(BlockBelowSoilRequirement::blocks)
    ).apply(inst, BlockBelowSoilRequirement::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, BlockBelowSoilRequirement> STREAM_CODEC = ByteBufCodecs.holderSet(Registries.BLOCK)
            .map(BlockBelowSoilRequirement::new, BlockBelowSoilRequirement::blocks);

    public static BlockBelowSoilRequirement of(Block... blocks) {
        return new BlockBelowSoilRequirement(HolderSet.direct(Block::builtInRegistryHolder, blocks));
    }

    public static BlockBelowSoilRequirement of(TagKey<Block> tag) {
        return new BlockBelowSoilRequirement(BuiltInRegistries.BLOCK.getOrCreateTag(tag));
    }

    @Override
    public boolean test(Holder<Crop> crop, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below(2)).is(this.blocks);
    }

    @Override
    public CropRequirementSerializer<?> serializer() {
        return IRCropRequirementSerializers.BLOCK_BELOW_SOIL.get();
    }
}
