package com.portingdeadmods.indrec.networking.serverbound;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.content.menus.ToolboxFilterMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Sets the filter of a slot in the open {@link ToolboxFilterMenu}, used for dragging items from JEI onto filter slots
 */
public record SetToolboxFilterPayload(int slot, ItemStack filter) implements CustomPacketPayload {
    public static final Type<SetToolboxFilterPayload> TYPE = new Type<>(IndustrialRecrafted.rl("set_toolbox_filter"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SetToolboxFilterPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SetToolboxFilterPayload::slot,
            ItemStack.OPTIONAL_STREAM_CODEC,
            SetToolboxFilterPayload::filter,
            SetToolboxFilterPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().containerMenu instanceof ToolboxFilterMenu menu) {
                menu.setFilter(this.slot, this.filter);
            }
        }).exceptionally(err -> {
            IndustrialRecrafted.LOGGER.error("Failed to handle SetToolboxFilterPayload payload", err);
            return null;
        });
    }
}
