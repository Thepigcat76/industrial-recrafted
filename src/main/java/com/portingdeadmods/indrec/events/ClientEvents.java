package com.portingdeadmods.indrec.events;

import net.minecraft.world.entity.player.Player;
import com.portingdeadmods.indrec.content.items.electric.CropAnalyzerItem;
import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.client.overlays.CropAnalyzerClientData;
import com.portingdeadmods.indrec.registries.IRBlocks;
import com.portingdeadmods.indrec.registries.IRItems;
import com.portingdeadmods.indrec.registries.IRMachines;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.Set;

@EventBusSubscriber(modid = IndustrialRecrafted.MODID, value = Dist.CLIENT)
public final class ClientEvents {
    public static final Set<ItemLike> WIP_ITEMS = Set.of(
            IRBlocks.NUKE,
            IRBlocks.NUCLEAR_REACTOR_CHAMBER,
            IRMachines.NUCLEAR_REACTOR,
            IRItems.SINGLE_URANIUM_FUEL_ROD,
            IRItems.DOUBLE_URANIUM_FUEL_ROD,
            IRItems.QUAD_URANIUM_FUEL_ROD
    );

    @SubscribeEvent
    private static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        CropAnalyzerClientData.clear();
    }

    @SubscribeEvent
    private static void onBuildItemTooltip(ItemTooltipEvent event) {
        Item item = event.getItemStack().getItem();
        if (WIP_ITEMS.stream().anyMatch(i -> i.asItem() == item)) {
            event.getToolTip().add(1, Component.literal("[WIP]").withStyle(ChatFormatting.RED));
        }

    }
}
