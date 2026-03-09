package com.portingdeadmods.indrec.events;

import com.portingdeadmods.indrec.IndustrialReclassified;
import com.portingdeadmods.indrec.client.sounds.JetpackSound;
import com.portingdeadmods.indrec.content.items.AbstractJetpackItem;
import com.portingdeadmods.indrec.networking.serverbound.UpdateInputPayload;
import com.portingdeadmods.indrec.utils.InputHandler;
import com.portingdeadmods.indrec.utils.VecHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.concurrent.ThreadLocalRandom;

@EventBusSubscriber(modid = IndustrialReclassified.MODID, value = Dist.CLIENT)
public final class JetpackEvents {
    private static boolean up = false;
    private static boolean down = false;
    private static boolean forwards = false;
    private static boolean backwards = false;
    private static boolean left = false;
    private static boolean right = false;
    private static boolean sprint = false;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Pre event) {
        var mc = Minecraft.getInstance();
        var settings = mc.options;

        if (mc.getConnection() == null)
            return;

        boolean upNow = settings.keyJump.isDown();
        boolean downNow = settings.keyShift.isDown();
        boolean forwardsNow = settings.keyUp.isDown();
        boolean backwardsNow = settings.keyDown.isDown();
        boolean leftNow = settings.keyLeft.isDown();
        boolean rightNow = settings.keyRight.isDown();
        boolean sprintNow = settings.keySprint.isDown();

        if (upNow != up || downNow != down || forwardsNow != forwards || backwardsNow != backwards || leftNow != left || rightNow != right || sprintNow != sprint) {
            up = upNow;
            down = downNow;
            forwards = forwardsNow;
            backwards = backwardsNow;
            left = leftNow;
            right = rightNow;
            sprint = sprintNow;

            update(up, down, forwards, backwards, left, right, sprint);
        }

        renderParticles();
    }

    private static void update(boolean up, boolean down, boolean forwards, boolean backwards, boolean left, boolean right, boolean sprint) {
        LocalPlayer player = Minecraft.getInstance().player;

        PacketDistributor.sendToServer(new UpdateInputPayload(up, down, forwards, backwards, left, right, sprint));
        InputHandler.update(player, up, down, forwards, backwards, left, right, sprint);
    }

    private static void renderParticles() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.level != null && !mc.isPaused()) {
            ItemStack chest = mc.player.getItemBySlot(EquipmentSlot.CHEST);
            Item item = chest.getItem();

            if (!chest.isEmpty() && item instanceof AbstractJetpackItem && isFlying(mc.player)) {
                if (mc.options.particles().get() != ParticleStatus.MINIMAL) {
                    var playerPos = mc.player.position().add(0, 1.5, 0);

                    float random = (ThreadLocalRandom.current().nextFloat() - 0.5F) * 0.1F;
                    double[] sneakBonus = mc.player.isCrouching() ? new double[]{-0.30, -0.10} : new double[]{0, 0};

                    var vLeft = VecHelper.rotate(new Vec3(-0.18, -0.90 + sneakBonus[1], -0.30 + sneakBonus[0]), mc.player.yBodyRot, 0, 0);
                    var vRight = VecHelper.rotate(new Vec3(0.18, -0.90 + sneakBonus[1], -0.30 + sneakBonus[0]), mc.player.yBodyRot, 0, 0);

                    double speedSide = 0.14D;
                    var v = playerPos.add(vLeft).add(mc.player.getDeltaMovement().scale(speedSide));
                    mc.particleEngine.createParticle(ParticleTypes.FLAME, v.x, v.y, v.z, random, -0.2D, random);
                    mc.particleEngine.createParticle(ParticleTypes.SMOKE, v.x, v.y, v.z, random, -0.2D, random);

                    v = playerPos.add(vRight).add(mc.player.getDeltaMovement().scale(speedSide));
                    mc.particleEngine.createParticle(ParticleTypes.FLAME, v.x, v.y, v.z, random, -0.2D, random);
                    mc.particleEngine.createParticle(ParticleTypes.SMOKE, v.x, v.y, v.z, random, -0.2D, random);
                }

                if (!JetpackSound.playing(mc.player.getId())) {
                    mc.getSoundManager().play(new JetpackSound(mc.player));
                }
            }
        }
    }

    public static boolean isFlying(Player player) {
        if (player.isSpectator())
            return false;

        ItemStack stack = player.getItemBySlot(EquipmentSlot.CHEST);

        if (!stack.isEmpty() && stack.getItem() instanceof AbstractJetpackItem jetpackItem /* && isEngineOn(stack)*/) {
            if (jetpackItem.getFuelStored(stack) > 0 || player.isCreative()) {
                if (false /*isHovering(stack)*/) {
                    return !player.onGround();
                }

                return InputHandler.isHoldingUp(player);
            }
        }

        return false;
    }
}