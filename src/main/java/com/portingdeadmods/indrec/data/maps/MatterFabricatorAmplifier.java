package com.portingdeadmods.indrec.data.maps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record MatterFabricatorAmplifier(float energyReduction) {
    public static final Codec<MatterFabricatorAmplifier> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.FLOAT.fieldOf("energy_reduction").forGetter(MatterFabricatorAmplifier::energyReduction)
    ).apply(inst, MatterFabricatorAmplifier::new));
}
