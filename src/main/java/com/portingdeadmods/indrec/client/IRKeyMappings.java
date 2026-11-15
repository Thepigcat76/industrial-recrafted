package com.portingdeadmods.indrec.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.content.items.ToolboxItem;
import com.portingdeadmods.indrec.networking.serverbound.OpenToolboxPayload;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

public final class IRKeyMappings {
    public static final String CATEGORY = "key.categories." + IndustrialRecrafted.MODID;

    public static final KeyMapping OPEN_TOOLBOX = new KeyMapping("key." + IndustrialRecrafted.MODID + ".open_toolbox",
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, CATEGORY);

    private IRKeyMappings() {
    }

    public static void register(RegisterKeyMappingsEvent event) {
        event.register(OPEN_TOOLBOX);
    }

    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        while (OPEN_TOOLBOX.consumeClick()) {
            if (player == null || mc.screen != null) continue;
            for (InteractionHand hand : InteractionHand.values()) {
                if (player.getItemInHand(hand).getItem() instanceof ToolboxItem) {
                    PacketDistributor.sendToServer(new OpenToolboxPayload(hand));
                    break;
                }
            }
        }
    }
}
