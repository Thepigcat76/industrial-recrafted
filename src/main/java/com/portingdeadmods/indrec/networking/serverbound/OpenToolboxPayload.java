package com.portingdeadmods.indrec.networking.serverbound;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.content.items.ToolboxItem;
import com.portingdeadmods.indrec.content.menus.ToolboxFilterMenu;
import com.portingdeadmods.indrec.registries.IRTranslations;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record OpenToolboxPayload(InteractionHand hand) implements CustomPacketPayload {
    public static final Type<OpenToolboxPayload> TYPE = new Type<>(IndustrialRecrafted.rl("open_toolbox"));
    public static final StreamCodec<ByteBuf, OpenToolboxPayload> STREAM_CODEC = ByteBufCodecs.BOOL.map(
            mainHand -> new OpenToolboxPayload(mainHand ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND),
            payload -> payload.hand == InteractionHand.MAIN_HAND
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            ItemStack toolbox = player.getItemInHand(this.hand);
            if (toolbox.getItem() instanceof ToolboxItem) {
                player.openMenu(new SimpleMenuProvider((containerId, inventory, p) -> new ToolboxFilterMenu(containerId, inventory, this.hand), IRTranslations.TOOLBOX_FILTER.component()),
                        buf -> buf.writeEnum(this.hand));
            }
        }).exceptionally(err -> {
            IndustrialRecrafted.LOGGER.error("Failed to handle OpenToolboxPayload payload", err);
            return null;
        });
    }
}
