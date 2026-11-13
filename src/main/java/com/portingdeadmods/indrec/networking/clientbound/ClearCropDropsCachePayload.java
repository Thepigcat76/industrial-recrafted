package com.portingdeadmods.indrec.networking.clientbound;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.client.overlays.CropAnalyzerClientData;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

/**
 * Sent after the server reloaded its data, since loot tables and crops might have changed
 */
public record ClearCropDropsCachePayload() implements CustomPacketPayload {
    public static final ClearCropDropsCachePayload INSTANCE = new ClearCropDropsCachePayload();
    public static final Type<ClearCropDropsCachePayload> TYPE = new Type<>(IndustrialRecrafted.rl("clear_crop_drops_cache"));
    public static final StreamCodec<ByteBuf, ClearCropDropsCachePayload> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public @NotNull Type<ClearCropDropsCachePayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(CropAnalyzerClientData::clear);
    }
}
