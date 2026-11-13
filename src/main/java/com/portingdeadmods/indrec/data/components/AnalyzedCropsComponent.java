package com.portingdeadmods.indrec.data.components;

import com.mojang.serialization.Codec;
import com.portingdeadmods.indrec.IRRegistries;
import com.portingdeadmods.indrec.api.crops.Crop;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record AnalyzedCropsComponent(Set<ResourceKey<Crop>> crops) {
    public static final AnalyzedCropsComponent EMPTY = new AnalyzedCropsComponent(Set.of());
    public static final Codec<AnalyzedCropsComponent> CODEC = ResourceKey.codec(IRRegistries.CROP).listOf()
            .xmap(list -> new AnalyzedCropsComponent(Set.copyOf(list)), component -> List.copyOf(component.crops()));
    public static final StreamCodec<ByteBuf, AnalyzedCropsComponent> STREAM_CODEC = ResourceKey.streamCodec(IRRegistries.CROP)
            .apply(ByteBufCodecs.collection(HashSet::new))
            .map(set -> new AnalyzedCropsComponent(Set.copyOf(set)), component -> new HashSet<>(component.crops()));

    public AnalyzedCropsComponent {
        crops = Set.copyOf(crops);
    }

    public boolean contains(ResourceKey<Crop> crop) {
        return this.crops.contains(crop);
    }

    public AnalyzedCropsComponent with(ResourceKey<Crop> crop) {
        if (this.contains(crop)) return this;
        Set<ResourceKey<Crop>> crops = new HashSet<>(this.crops);
        crops.add(crop);
        return new AnalyzedCropsComponent(crops);
    }
}
