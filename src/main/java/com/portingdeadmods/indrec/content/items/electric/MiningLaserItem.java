package com.portingdeadmods.indrec.content.items.electric;

import com.portingdeadmods.indrec.IRConfig;
import com.portingdeadmods.indrec.api.energy.EnergyTier;
import com.portingdeadmods.indrec.api.energy.items.ElectricConsumerItem;
import com.portingdeadmods.indrec.api.energy.items.SimpleEnergyItem;
import com.portingdeadmods.indrec.registries.IREnergyTiers;
import com.portingdeadmods.indrec.registries.IRParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class MiningLaserItem extends SimpleEnergyItem implements ElectricConsumerItem {
    private static final int USE_DURATION = 72000;
    private static final float MINING_SPEED_DIVISOR = 30f;
    private static final int DESTROY_STAGES = 10;

    private static final Map<Player, MiningProgress> MINING_PROGRESS = new WeakHashMap<>();

    public MiningLaserItem(Properties properties, Supplier<? extends EnergyTier> energyTier, IntSupplier defaultEnergyCapacity) {
        super(properties, energyTier, defaultEnergyCapacity);
    }

    public static MiningLaserItem defaultItem(Properties properties) {
        return new MiningLaserItem(properties, IREnergyTiers.EXTREME, () -> IRConfig.miningLaserCapacity);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResultHolder.pass(stack);
        }
        if (!canWork(stack, player)) {
            return InteractionResultHolder.fail(stack);
        }

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity entity, @NotNull ItemStack stack, int remainingUseDuration) {
        if (!(entity instanceof Player player) || !canWork(stack, player)) {
            entity.stopUsingItem();
            return;
        }

        HitResult hitResult = traceBeam(player, 1f);
        boolean hitBlock = hitResult.getType() == HitResult.Type.BLOCK && hitResult instanceof BlockHitResult;

        if (!level.isClientSide()) {
            ServerPlayer serverPlayer = (ServerPlayer) player;

            if (hitResult instanceof EntityHitResult entityHit) {
                getEnergyCap(stack).drainEnergy(IRConfig.miningLaserEntityEnergyUsage, false);
                entityHit.getEntity().hurt(level.damageSources().playerAttack(player), IRConfig.miningLaserEntityDamage);
            } else {
                consumeEnergy(stack, player);
            }

            if (hitBlock) {
                tickMining(serverPlayer.serverLevel(), serverPlayer, ((BlockHitResult) hitResult).getBlockPos());
            } else {
                clearMiningProgress(serverPlayer.serverLevel(), player);
            }
        } else {
            Vec3 location = hitResult.getLocation();
            level.addParticle(IRParticleTypes.LASER_BEAM.get(), location.x, location.y, location.z, player.getId(), 0, 0);
            if (hitBlock) {
                spawnImpactParticles(level, ((BlockHitResult) hitResult).getBlockPos(), hitResult.getLocation());
            }
        }
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
        if (entity.level() instanceof ServerLevel serverLevel && entity instanceof Player player) {
            clearMiningProgress(serverLevel, player);
        }
    }

    public static HitResult traceBeam(Player player, float partialTick) {
        HitResult blockHit = player.pick(IRConfig.miningLaserRange, partialTick, false);
        Vec3 start = player.getEyePosition(partialTick);
        Vec3 end = blockHit.getLocation();
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(player, start, end, new AABB(start, end).inflate(1),
                target -> target instanceof LivingEntity && target.isPickable() && !target.isSpectator(), start.distanceToSqr(end));
        return entityHit != null ? entityHit : blockHit;
    }

    private static void tickMining(ServerLevel level, ServerPlayer player, BlockPos pos) {
        MiningProgress progress = MINING_PROGRESS.get(player);
        if (progress != null && !progress.pos.equals(pos)) {
            clearMiningProgress(level, player);
            progress = null;
        }

        BlockState state = level.getBlockState(pos);
        float hardness = state.getDestroySpeed(level, pos);
        if (hardness < 0 || !level.mayInteract(player, pos)) {
            return;
        }

        if (progress == null) {
            progress = new MiningProgress(pos);
            MINING_PROGRESS.put(player, progress);
        }

        progress.amount += hardness == 0 ? 1 : IRConfig.miningLaserSpeed / hardness / MINING_SPEED_DIVISOR;

        if (progress.amount >= 1) {
            clearMiningProgress(level, player);

            if (player.gameMode.destroyBlock(pos)) {
                player.connection.send(new ClientboundLevelEventPacket(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state), false));
            }
        } else {
            level.destroyBlockProgress(getDestroyProgressId(player), pos, (int) (progress.amount * DESTROY_STAGES));
        }
    }

    private static void clearMiningProgress(ServerLevel level, Player player) {
        MiningProgress progress = MINING_PROGRESS.remove(player);
        if (progress != null) {
            level.destroyBlockProgress(getDestroyProgressId(player), progress.pos, -1);
        }
    }

    private static int getDestroyProgressId(Player player) {
        return ~player.getId();
    }

    private static void spawnImpactParticles(Level level, BlockPos pos, Vec3 location) {
        BlockState state = level.getBlockState(pos);
        BlockParticleOption particle = new BlockParticleOption(ParticleTypes.BLOCK, state);
        for (int i = 0; i < 2; i++) {
            level.addParticle(particle, location.x, location.y, location.z, level.random.nextGaussian() * 0.1, 0.1, level.random.nextGaussian() * 0.1);
        }
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return USE_DURATION;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public boolean isCorrectToolForDrops(@NotNull ItemStack stack, @NotNull BlockState state) {
        return true;
    }

    @Override
    public boolean shouldCauseReequipAnimation(@NotNull ItemStack oldStack, @NotNull ItemStack newStack, boolean slotChanged) {
        return slotChanged || !newStack.is(oldStack.getItem());
    }

    @Override
    public boolean requireEnergyToWork(ItemStack itemStack, @Nullable Entity entity) {
        return true;
    }

    @Override
    public int getEnergyUsage(ItemStack itemStack, @Nullable Entity entity) {
        return IRConfig.miningLaserEnergyUsage;
    }

    private static final class MiningProgress {
        private final BlockPos pos;
        private float amount;

        private MiningProgress(BlockPos pos) {
            this.pos = pos;
        }
    }
}
