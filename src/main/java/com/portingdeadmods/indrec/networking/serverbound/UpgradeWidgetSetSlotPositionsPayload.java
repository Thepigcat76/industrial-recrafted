package com.portingdeadmods.indrec.networking.serverbound;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.api.menus.UpgradeMenu;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record UpgradeWidgetSetSlotPositionsPayload(int startY) implements CustomPacketPayload {
    public static final Type<UpgradeWidgetSetSlotPositionsPayload> TYPE = new Type<>(IndustrialRecrafted.rl("upgrade_widget_set_slot_positions"));
    public static final StreamCodec<ByteBuf, UpgradeWidgetSetSlotPositionsPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            UpgradeWidgetSetSlotPositionsPayload::startY,
            UpgradeWidgetSetSlotPositionsPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().containerMenu instanceof UpgradeMenu machineMenu) {
                machineMenu.internalSetUpgradeSlotPositions(this.startY);
            }
        }).exceptionally(err -> {
            IndustrialRecrafted.LOGGER.error("Failed to handle UpgradeWidgetSetSlotPositions payload", err);
            return null;
        });
    }
}