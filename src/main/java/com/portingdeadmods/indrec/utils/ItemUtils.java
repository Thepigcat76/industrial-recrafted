package com.portingdeadmods.indrec.utils;

import com.portingdeadmods.indrec.registries.IRDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.PlayerMainInvWrapper;
import org.jetbrains.annotations.Nullable;

import static net.neoforged.neoforge.items.ItemHandlerHelper.insertItemStacked;

public final class ItemUtils {
    public static boolean isActive(ItemStack stack) {
        return stack.getOrDefault(IRDataComponents.ACTIVE, false);
    }

    public static boolean toggleActive(@Nullable Player player, ItemStack stack) {
        return toggleActive(player, stack, true);
    }

    public static boolean toggleActive(@Nullable Player player, ItemStack stack, boolean displayMessage) {
        if (stack.has(IRDataComponents.ACTIVE)) {
            boolean active = ItemUtils.isActive(stack);
            stack.set(IRDataComponents.ACTIVE, !active);
            if (displayMessage && player != null) {
                player.displayClientMessage(Component.literal("Toggled ").append(stack.getDisplayName()).append(": " + (!active ? "Active" : "Inactive")), true);
            }
            return !active;
        }
        return false;
    }

    public static ItemStack itemStackFromInteractionHand(InteractionHand interactionHand, Player player) {
        return switch (interactionHand) {
            case MAIN_HAND -> player.getMainHandItem();
            case OFF_HAND -> player.getOffhandItem();
            case null -> ItemStack.EMPTY;
        };
    }

    public static void giveItemToPlayerNoSound(Player player, ItemStack stack, int preferredSlot) {
        if (stack.isEmpty()) return;

        IItemHandler inventory = new PlayerMainInvWrapper(player.getInventory());
        Level level = player.level();

        ItemStack remainder = stack;
        if (preferredSlot >= 0 && preferredSlot < inventory.getSlots()) {
            remainder = inventory.insertItem(preferredSlot, stack, false);
        }
        if (!remainder.isEmpty()) {
            remainder = insertItemStacked(inventory, remainder, false);
        }

        if (!remainder.isEmpty() && !level.isClientSide) {
            ItemEntity entityitem = new ItemEntity(level, player.getX(), player.getY() + 0.5, player.getZ(), remainder);
            entityitem.setPickUpDelay(40);
            entityitem.setDeltaMovement(entityitem.getDeltaMovement().multiply(0, 1, 0));

            level.addFreshEntity(entityitem);
        }
    }

}