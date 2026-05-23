package com.portingdeadmods.indrec.content.items;

import com.portingdeadmods.indrec.utils.InputHandler;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractJetpackItem extends ArmorItem {
    public AbstractJetpackItem(Properties properties, Type type, Holder<ArmorMaterial> material) {
        super(material, type, properties);
    }

    public abstract int getFuelUsage(ItemStack stack, Entity entity);

    public abstract int getFuelStored(ItemStack stack);

    public abstract void drainFuel(ItemStack stack, int amount);

    /*
     * Jetpack logic is very much like Simply Jetpacks/Iron Jetpacks, since I used it to learn how to make this work
     * Credit to Tonius & Tomson124
     * https://github.com/Tomson124/SimplyJetpacks-2/blob/1.12/src/main/java/tonius/simplyjetpacks/item/rewrite/Itemjava
     */
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isSelected) {
        if (entity instanceof Player player) {
            var chest = player.getItemBySlot(EquipmentSlot.CHEST);
            if (chest.isEmpty() || chest != stack)
                return;

            var item = chest.getItem();
            if (item instanceof AbstractJetpackItem/* && JetpackUtils.isEngineOn(chest)*/) {
                var hover = false;//JetpackUtils.isHovering(chest);

                if (InputHandler.isHoldingUp(player) || hover && !player.onGround()) {
                    double motionY = player.getDeltaMovement().y();
                    double speedHoverDescend = 0.25D;
                    double speedHoverSlow = 0.075D;
                    double hoverSpeed = InputHandler.isHoldingDown(player) ? speedHoverDescend : speedHoverSlow;
                    double accelVert = 0.12D;
                    double currentAccel = accelVert * (motionY < 0.3D ? 2.5D : 1.0D);
                    double speedVert = 0.41D;
                    double currentSpeedVertical = speedVert * (player.isInWater() ? 0.4D : 1.0D);

                    double sprintFuel = 2.1D;
                    double usage = player.isSprinting() || InputHandler.isHoldingSprint(player) ? getFuelUsage(stack, entity) * sprintFuel : getFuelUsage(stack, entity);

                    if (!player.isCreative() && level.getGameTime() % 10 == 0) {
                        drainFuel(stack, (int) usage);
                    }

                    if (hover && player.isFallFlying()) {
                        player.stopFallFlying();
                    }

                    if (getFuelStored(stack) > 0 || player.isCreative()) {
                        double throttle = 1D;//JetpackUtils.getThrottle(stack);
                        double sprintSpeedVert = 1.05D;
                        double verticalSprintMulti = motionY >= 0 && InputHandler.isHoldingSprint(player) ? sprintSpeedVert : 1.0D;

                        if (InputHandler.isHoldingUp(player)) {
                            if (!hover) {
                                fly(player, Math.min(motionY + currentAccel, currentSpeedVertical) * throttle * verticalSprintMulti);
                            } else {
                                if (InputHandler.isHoldingDown(player)) {
                                    fly(player, Math.min(motionY + currentAccel, -speedHoverSlow));
                                } else {
                                    double speedHoverAscend = 0.27D;
                                    fly(player, Math.min(motionY + currentAccel, speedHoverAscend) * throttle * verticalSprintMulti);
                                }
                            }
                        } else {
                            fly(player, Math.min(motionY + currentAccel, -hoverSpeed));
                        }

                        double speedSide = 0.14D;
                        double speedSideways = (player.isCrouching() ? speedSide * 0.5F : speedSide) * throttle;
                        double sprintSpeed = 1.1D;
                        double speedForward = (player.isSprinting() ? speedSideways * sprintSpeed : speedSideways) * throttle;

                        if (!player.isFallFlying()) {
                            if (InputHandler.isHoldingForwards(player)) {
                                player.moveRelative(1, new Vec3(0, 0, speedForward));
                            }

                            if (InputHandler.isHoldingBackwards(player)) {
                                player.moveRelative(1, new Vec3(0, 0, -speedSideways * 0.8F));
                            }

                            if (InputHandler.isHoldingLeft(player)) {
                                player.moveRelative(1, new Vec3(speedSideways, 0, 0));
                            }

                            if (InputHandler.isHoldingRight(player)) {
                                player.moveRelative(1, new Vec3(-speedSideways, 0, 0));
                            }
                        }

                        if (!level.isClientSide()) {
                            player.fallDistance = 0.0F;

                            if (player instanceof ServerPlayer serverPlayer) {
                                serverPlayer.connection.aboveGroundTickCount = 0;
                            }
                        }
                    }
                }
            }
        }
    }

    private static void fly(Player player, double y) {
        var motion = player.getDeltaMovement();
        player.setDeltaMovement(motion.x(), y, motion.z());
    }
}
