package com.portingdeadmods.indrec.data.components;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public record CannedFood(ResourceKey<Item> cannedFood) {
    public static final Codec<CannedFood> CODEC = ResourceKey.codec(Registries.ITEM).xmap(CannedFood::new, CannedFood::cannedFood);
    public static final StreamCodec<? super RegistryFriendlyByteBuf,CannedFood> STREAM_CODEC = ResourceKey.streamCodec(Registries.ITEM).map(CannedFood::new, CannedFood::cannedFood);
}
