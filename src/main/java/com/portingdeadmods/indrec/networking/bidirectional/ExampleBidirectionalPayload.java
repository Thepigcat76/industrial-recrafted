package com.portingdeadmods.indrec.networking.bidirectional;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ExampleBidirectionalPayload(int payload) implements CustomPacketPayload {
    public static final Type<ExampleBidirectionalPayload> TYPE = new Type<>(IndustrialRecrafted.rl("example_bidirectional_payload"));
    public static final StreamCodec<? super RegistryFriendlyByteBuf, ExampleBidirectionalPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ExampleBidirectionalPayload::payload,
            ExampleBidirectionalPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void exampleBidirectionalAction(IPayloadContext context) {
        context.enqueueWork(() -> {
            Player p = context.player();
            Level l = p.level();

            if (l.isClientSide()) {
                LocalPlayer player = (LocalPlayer) p;
                ClientLevel level = (ClientLevel) l;

                // Logic
            } else {
                ServerPlayer player = (ServerPlayer) p;
                ServerLevel level = (ServerLevel) l;

                // Logic
            }
        }).exceptionally(err -> {
            IndustrialRecrafted.LOGGER.error("Failed to handle " + TYPE.id(), err);
            return null;
        });
    }

}
