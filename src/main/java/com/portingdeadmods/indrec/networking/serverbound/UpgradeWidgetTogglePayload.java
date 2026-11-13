package com.portingdeadmods.indrec.networking.serverbound;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.api.menus.IRSlotItemHandler;
import com.portingdeadmods.indrec.api.menus.UpgradeMenu;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UpgradeWidgetTogglePayload(boolean open) implements CustomPacketPayload {
    public static final Type<UpgradeWidgetTogglePayload> TYPE = new Type<>(IndustrialRecrafted.rl("upgrade_widget_toggle"));
    public static final StreamCodec<ByteBuf, UpgradeWidgetTogglePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            UpgradeWidgetTogglePayload::open,
            UpgradeWidgetTogglePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().containerMenu instanceof UpgradeMenu machineMenu) {
                for (IRSlotItemHandler upgradeSlot : machineMenu.getUpgradeSlots()) {
                    upgradeSlot.setActive(this.open);
                }
            }
        }).exceptionally(err -> {
            IndustrialRecrafted.LOGGER.error("Failed to handle UpgradeWidgetTogglePayload payload", err);
            return null;
        });
    }
}