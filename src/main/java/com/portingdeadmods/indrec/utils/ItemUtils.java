package com.portingdeadmods.indrec.utils;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.PlayerMainInvWrapper;

import static net.neoforged.neoforge.items.ItemHandlerHelper.insertItemStacked;

public final class ItemUtils {
    public static ItemStack itemStackFromInteractionHand(InteractionHand interactionHand, Player player) {
        return switch (interactionHand) {
            case MAIN_HAND -> player.getMainHandItem();
            case OFF_HAND -> player.getOffhandItem();
            case null -> ItemStack.EMPTY;
        };
    }

    public static CustomData getImmutableTag(ItemStack itemStack) {
        return itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
    }

    public static void giveItemToPlayerNoSound(Player player, ItemStack stack, int preferredSlot) {
        if (stack.isEmpty()) return;

        IItemHandler inventory = new PlayerMainInvWrapper(player.getInventory());
        Level level = player.level();

        // try adding it into the inventory
        ItemStack remainder = stack;
        // insert into preferred slot first
        if (preferredSlot >= 0 && preferredSlot < inventory.getSlots()) {
            remainder = inventory.insertItem(preferredSlot, stack, false);
        }
        // then into the inventory in general
        if (!remainder.isEmpty()) {
            remainder = insertItemStacked(inventory, remainder, false);
        }

        // drop remaining itemstack into the level
        if (!remainder.isEmpty() && !level.isClientSide) {
            ItemEntity entityitem = new ItemEntity(level, player.getX(), player.getY() + 0.5, player.getZ(), remainder);
            entityitem.setPickUpDelay(40);
            entityitem.setDeltaMovement(entityitem.getDeltaMovement().multiply(0, 1, 0));

            level.addFreshEntity(entityitem);
        }
    }

}