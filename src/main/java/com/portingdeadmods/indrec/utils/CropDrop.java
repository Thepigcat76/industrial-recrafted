package com.portingdeadmods.indrec.utils;

import com.portingdeadmods.indrec.api.crops.SeedDrop;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record CropDrop(ItemStack stack, float chance, int minCount, int maxCount, float averageCount) {
    public static final StreamCodec<RegistryFriendlyByteBuf, CropDrop> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            CropDrop::stack,
            ByteBufCodecs.FLOAT,
            CropDrop::chance,
            ByteBufCodecs.VAR_INT,
            CropDrop::minCount,
            ByteBufCodecs.VAR_INT,
            CropDrop::maxCount,
            ByteBufCodecs.FLOAT,
            CropDrop::averageCount,
            CropDrop::new
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, List<CropDrop>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());

    public static CropDrop of(LootTableParser.LootDrop drop) {
        return new CropDrop(new ItemStack(drop.item()), (float) drop.chance(), drop.minCount(), drop.maxCount(), (float) drop.averageCount());
    }

    public static CropDrop of(SeedDrop seedDrop) {
        int count = seedDrop.item().getCount();
        return new CropDrop(seedDrop.item().copyWithCount(1), seedDrop.chance(), count, count, seedDrop.chance() * count);
    }
}
