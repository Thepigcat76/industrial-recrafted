package com.portingdeadmods.indrec.content.items.electric;

import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Holder;
import com.portingdeadmods.indrec.api.crops.Crop;
import com.portingdeadmods.indrec.data.components.AnalyzedCropsComponent;
import com.portingdeadmods.indrec.registries.IRDataComponents;
import com.portingdeadmods.indrec.IRCapabilities;
import com.portingdeadmods.indrec.IRConfig;
import com.portingdeadmods.indrec.api.energy.EnergyHandler;
import com.portingdeadmods.indrec.api.energy.EnergyTier;
import com.portingdeadmods.indrec.api.energy.items.ElectricConsumerItem;
import com.portingdeadmods.indrec.api.energy.items.SimpleEnergyItem;
import com.portingdeadmods.indrec.content.blockentities.CropBlockEntity;
import com.portingdeadmods.indrec.networking.clientbound.AnalyzeProgressPayload;
import com.portingdeadmods.indrec.registries.IREnergyTiers;
import com.portingdeadmods.indrec.registries.IRTranslations;
import com.portingdeadmods.indrec.utils.CropAnalysisUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class CropAnalyzerItem extends SimpleEnergyItem implements ElectricConsumerItem {
    /**
     * Time in ticks right click needs to be held down to analyze a crop
     */
    public static final int ANALYZE_DURATION = 3 * 20;

    public CropAnalyzerItem(Properties properties, Supplier<? extends EnergyTier> energyTier, IntSupplier defaultEnergyCapacity) {
        super(properties, energyTier, defaultEnergyCapacity);
    }

    public static CropAnalyzerItem defaultItem(Properties properties) {
        return new CropAnalyzerItem(properties.component(IRDataComponents.ANALYZED_CROPS, AnalyzedCropsComponent.EMPTY),
                IREnergyTiers.LOW, () -> IRConfig.cropAnalyzerCapacity);
    }

    public static boolean hasAnalyzed(ItemStack analyzer, Holder<Crop> crop) {
        return crop.unwrapKey().map(key -> hasAnalyzed(analyzer, key)).orElse(false);
    }

    public static boolean hasAnalyzed(ItemStack analyzer, ResourceKey<Crop> crop) {
        return analyzer.getOrDefault(IRDataComponents.ANALYZED_CROPS, AnalyzedCropsComponent.EMPTY).contains(crop);
    }

    public static ItemStack getHeldAnalyzer(Player player) {
        if (player.getMainHandItem().getItem() instanceof CropAnalyzerItem) return player.getMainHandItem();
        if (player.getOffhandItem().getItem() instanceof CropAnalyzerItem) return player.getOffhandItem();
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null || CropAnalysisUtils.getAnalyzableCrop(context.getLevel(), context.getClickedPos(), context.getItemInHand()) == null) {
            return InteractionResult.PASS;
        }

        if (!canWork(context.getItemInHand(), player)) {
            if (!context.getLevel().isClientSide()) {
                player.displayClientMessage(IRTranslations.CROP_ANALYZER_NOT_ENOUGH_ENERGY.component().withStyle(ChatFormatting.RED), true);
            }
            return InteractionResult.FAIL;
        }

        player.startUsingItem(context.getHand());
        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new AnalyzeProgressPayload(0));
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        if (getHoveredAnalyzableCrop(level, entity, stack) == null) {
            entity.stopUsingItem();
        } else if (entity instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new AnalyzeProgressPayload(getUseDuration(stack, entity) - remainingUseDuration));
        }
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide()) {
            CropBlockEntity cropBE = getHoveredAnalyzableCrop(level, entity, stack);
            if (cropBE != null && canWork(stack, entity)) {
                consumeEnergy(stack, entity);
                cropBE.getCrop().unwrapKey().ifPresent(key -> stack.update(IRDataComponents.ANALYZED_CROPS, AnalyzedCropsComponent.EMPTY, crops -> crops.with(key)));
                level.playSound(null, cropBE.getBlockPos(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.5f, 1.5f);
            }
        }
        return stack;
    }

    private static @Nullable CropBlockEntity getHoveredAnalyzableCrop(Level level, LivingEntity entity, ItemStack analyzer) {
        if (!(entity instanceof Player player)) return null;

        HitResult hitResult = player.pick(player.blockInteractionRange(), 1, false);
        if (hitResult instanceof BlockHitResult blockHitResult && hitResult.getType() == HitResult.Type.BLOCK) {
            return CropAnalysisUtils.getAnalyzableCrop(level, blockHitResult.getBlockPos(), analyzer);
        }
        return null;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return ANALYZE_DURATION;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(IRTranslations.CROP_ANALYZER_TAB_HINT.component().withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, ctx, tooltip, flag);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public boolean requireEnergyToWork(ItemStack itemStack, @Nullable Entity entity) {
        return true;
    }

    @Override
    public int getEnergyUsage(ItemStack itemStack, @Nullable Entity entity) {
        return IRConfig.cropAnalyzerEnergyUsage;
    }

    @Override
    public void consumeEnergy(ItemStack stack, @Nullable Entity entity) {
        EnergyHandler energyHandler = stack.getCapability(IRCapabilities.ENERGY_ITEM);
        if (energyHandler != null) {
            energyHandler.forceDrainEnergy(getEnergyUsage(stack, entity), false);
        }
    }
}
