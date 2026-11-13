package com.portingdeadmods.indrec.networking.clientbound;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.client.overlays.CropAnalyzerClientData;
import com.portingdeadmods.indrec.utils.CropDropsKey;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import com.portingdeadmods.indrec.utils.CropDrop;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record CropDropsPayload(CropDropsKey key, List<CropDrop> drops) implements CustomPacketPayload {
    public static final Type<CropDropsPayload> TYPE = new Type<>(IndustrialRecrafted.rl("crop_drops"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CropDropsPayload> STREAM_CODEC = StreamCodec.composite(
            CropDropsKey.STREAM_CODEC,
            CropDropsPayload::key,
            CropDrop.LIST_STREAM_CODEC,
            CropDropsPayload::drops,
            CropDropsPayload::new
    );

    @Override
    public @NotNull Type<CropDropsPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> CropAnalyzerClientData.setDrops(this.key, this.drops)).exceptionally(err -> {
            IndustrialRecrafted.LOGGER.error("Failed to handle CropDropsPayload", err);
            return null;
        });
    }
}
