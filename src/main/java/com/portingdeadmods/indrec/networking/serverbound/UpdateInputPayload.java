package com.portingdeadmods.indrec.networking.serverbound;

import com.portingdeadmods.indrec.IndustrialReclassified;
import com.portingdeadmods.indrec.utils.InputHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record UpdateInputPayload(boolean up, boolean down, boolean forwards, boolean backwards, boolean left, boolean right, boolean sprint) implements CustomPacketPayload {
    public static final Type<UpdateInputPayload> TYPE = new Type<>(IndustrialReclassified.rl("update_inputs"));

    public static final StreamCodec<ByteBuf, UpdateInputPayload> STREAM_CODEC = StreamCodec.of(UpdateInputPayload::encode, UpdateInputPayload::decode);

    @Override
    public @NotNull Type<UpdateInputPayload> type() {
        return TYPE;
    }

    private static UpdateInputPayload decode(ByteBuf buf) {
        return new UpdateInputPayload(
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean()
        );
    }

    private static void encode(ByteBuf buf, UpdateInputPayload payload) {
        buf.writeBoolean(payload.up);
        buf.writeBoolean(payload.down);
        buf.writeBoolean(payload.forwards);
        buf.writeBoolean(payload.backwards);
        buf.writeBoolean(payload.left);
        buf.writeBoolean(payload.right);
        buf.writeBoolean(payload.sprint);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            var player = context.player();

            InputHandler.update(player, this.up, this.down, this.forwards, this.backwards, this.left, this.right, this.sprint);
        });
    }
}