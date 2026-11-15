package com.portingdeadmods.indrec.utils;

import com.portingdeadmods.indrec.content.blockentities.CropBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Identifies the kind of crop whose drops are cached, since drops don't depend on the crop's position.
 *
 * @param industrial whether {@code id} is the id of an industrial crop (crop registry entry, including ones added by other mods/datapacks),
 *                   or of a crop block supported by {@link VanillaCropInfo} (vanilla or modded) otherwise
 */
public record CropDropsKey(boolean industrial, ResourceLocation id) {
    public static final StreamCodec<ByteBuf, CropDropsKey> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            CropDropsKey::industrial,
            ResourceLocation.STREAM_CODEC,
            CropDropsKey::id,
            CropDropsKey::new
    );

    public static @Nullable CropDropsKey of(BlockGetter level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof CropBlockEntity cropBE && cropBE.getCrop() != null) {
            return cropBE.getCrop().unwrapKey().map(key -> new CropDropsKey(true, key.location())).orElse(null);
        }
        BlockState state = level.getBlockState(pos);
        if (VanillaCropInfo.isSupported(state)) {
            return new CropDropsKey(false, BuiltInRegistries.BLOCK.getKey(state.getBlock()));
        }
        return null;
    }
}
