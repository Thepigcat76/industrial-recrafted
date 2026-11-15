package com.portingdeadmods.indrec.networking.serverbound;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.content.items.ToolboxItem;
import com.portingdeadmods.indrec.registries.IRDataComponents;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Selects a tool of a toolbox that is hovered in a container screen.
 *
 * @param slot            the slot index, either in the player's inventory or in the open menu
 * @param playerInventory whether {@code slot} is an index in the player's inventory instead of the open menu
 * @param selected        the index of the tool to select
 */
public record SelectToolboxToolPayload(int slot, boolean playerInventory, int selected) implements CustomPacketPayload {
    public static final Type<SelectToolboxToolPayload> TYPE = new Type<>(IndustrialRecrafted.rl("select_toolbox_tool"));
    public static final StreamCodec<ByteBuf, SelectToolboxToolPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SelectToolboxToolPayload::slot,
            ByteBufCodecs.BOOL,
            SelectToolboxToolPayload::playerInventory,
            ByteBufCodecs.VAR_INT,
            SelectToolboxToolPayload::selected,
            SelectToolboxToolPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            ItemStack stack = getStack(context.player());
            if (stack.getItem() instanceof ToolboxItem) {
                stack.set(IRDataComponents.TOOLBOX_CONTENTS, ToolboxItem.getContents(stack).withSelected(this.selected));
            }
        }).exceptionally(err -> {
            IndustrialRecrafted.LOGGER.error("Failed to handle SelectToolboxToolPayload payload", err);
            return null;
        });
    }

    private ItemStack getStack(Player player) {
        if (this.playerInventory) {
            Inventory inventory = player.getInventory();
            return this.slot >= 0 && this.slot < inventory.getContainerSize() ? inventory.getItem(this.slot) : ItemStack.EMPTY;
        }
        AbstractContainerMenu menu = player.containerMenu;
        return menu.isValidSlotIndex(this.slot) && this.slot >= 0 ? menu.getSlot(this.slot).getItem() : ItemStack.EMPTY;
    }
}
