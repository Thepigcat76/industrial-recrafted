package com.portingdeadmods.indrec.networking.clientbound;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.client.overlays.CropAnalyzerClientData;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record AnalyzeProgressPayload(int ticks) implements CustomPacketPayload {
    public static final Type<AnalyzeProgressPayload> TYPE = new Type<>(IndustrialRecrafted.rl("analyze_progress"));
    public static final StreamCodec<ByteBuf, AnalyzeProgressPayload> STREAM_CODEC = ByteBufCodecs.VAR_INT.map(AnalyzeProgressPayload::new, AnalyzeProgressPayload::ticks);

    @Override
    public @NotNull Type<AnalyzeProgressPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> CropAnalyzerClientData.setServerAnalyzeTicks(this.ticks));
    }
}
