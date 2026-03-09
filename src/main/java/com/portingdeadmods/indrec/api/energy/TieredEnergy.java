package com.portingdeadmods.indrec.api.energy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.portingdeadmods.indrec.IRRegistries;
import com.portingdeadmods.portingdeadlibs.utils.codec.CodecUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record TieredEnergy(int energy, EnergyTier tier) {
    public static final Codec<TieredEnergy> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.INT.fieldOf("energy").forGetter(TieredEnergy::energy),
            CodecUtils.registryCodec(IRRegistries.ENERGY_TIER).fieldOf("tier").forGetter(TieredEnergy::tier)
    ).apply(inst, TieredEnergy::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, TieredEnergy> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            TieredEnergy::energy,
            CodecUtils.registryStreamCodec(IRRegistries.ENERGY_TIER),
            TieredEnergy::tier,
            TieredEnergy::new
    );
}
