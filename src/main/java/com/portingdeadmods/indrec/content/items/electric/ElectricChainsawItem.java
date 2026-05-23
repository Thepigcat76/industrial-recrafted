package com.portingdeadmods.indrec.content.items.electric;

import com.portingdeadmods.indrec.IRConfig;
import com.portingdeadmods.indrec.api.energy.items.ElectricDiggerItem;
import com.portingdeadmods.indrec.api.energy.EnergyTier;
import com.portingdeadmods.indrec.registries.IRDataComponents;
import com.portingdeadmods.indrec.registries.IREnergyTiers;
import com.portingdeadmods.indrec.registries.IRTranslations;
import com.portingdeadmods.indrec.utils.ItemUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class ElectricChainsawItem extends ElectricDiggerItem {
    private final boolean hasTreeCutter;

    public ElectricChainsawItem(Properties properties, float attackSpeed, float baseAttackDamage, Tier tier, Supplier<? extends EnergyTier> energyTier, IntSupplier energyUsage, IntSupplier defaultEnergyCapacity, boolean hasTreeCutter) {
        super(properties, attackSpeed, baseAttackDamage, tier, BlockTags.MINEABLE_WITH_AXE, energyTier, energyUsage, defaultEnergyCapacity);
        this.hasTreeCutter = hasTreeCutter;
    }

    public static ElectricChainsawItem basicItem(Properties properties) {
        return new ElectricChainsawItem(properties, -2.8F, 5, Tiers.IRON, IREnergyTiers.LOW, () -> IRConfig.basicChainsawEnergyUsage, () -> IRConfig.basicChainsawCapacity, false);
    }

    public static ElectricChainsawItem advancedItem(Properties properties) {
        return new ElectricChainsawItem(properties.component(IRDataComponents.ACTIVE, false), -2.8F, 7, Tiers.DIAMOND, IREnergyTiers.HIGH, () -> IRConfig.advancedChainsawEnergyUsage, () -> IRConfig.advancedChainsawCapacity, true);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_AXE_ACTIONS.contains(itemAbility);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {
        ItemStack stack = player.getItemInHand(interactionHand);
        if (this.hasTreeCutter && player.isShiftKeyDown()) {
            boolean active = ItemUtils.toggleActive(stack);
            player.displayClientMessage(IRTranslations.TOGGLED_TREE_CUTTER.component(active ? IRTranslations.ON.component().getString() : IRTranslations.OFF.component().getString()).withStyle(ChatFormatting.GRAY), true);
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        Player player = context.getPlayer();
        if (playerHasShieldUseIntent(context)) {
            return InteractionResult.PASS;
        } else {
            Optional<BlockState> optional = this.evaluateNewBlockState(level, blockpos, player, level.getBlockState(blockpos), context);
            if (optional.isEmpty()) {
                return InteractionResult.PASS;
            } else {
                ItemStack itemstack = context.getItemInHand();
                if (player instanceof ServerPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, blockpos, itemstack);
                }

                level.setBlock(blockpos, (BlockState)optional.get(), 11);
                level.gameEvent(GameEvent.BLOCK_CHANGE, blockpos, GameEvent.Context.of(player, (BlockState)optional.get()));
                if (player != null) {
                    itemstack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.getHand()));
                }

                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
    }

    private static boolean playerHasShieldUseIntent(UseOnContext context) {
        Player player = context.getPlayer();
        return context.getHand().equals(InteractionHand.MAIN_HAND) && player.getOffhandItem().is(Items.SHIELD) && !player.isSecondaryUseActive();
    }

    private Optional<BlockState> evaluateNewBlockState(Level level, BlockPos pos, @Nullable Player player, BlockState state, UseOnContext p_40529_) {
        Optional<BlockState> optional = Optional.ofNullable(state.getToolModifiedState(p_40529_, ItemAbilities.AXE_STRIP, false));
        if (optional.isPresent()) {
            level.playSound(player, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            return optional;
        } else {
            Optional<BlockState> optional1 = Optional.ofNullable(state.getToolModifiedState(p_40529_, ItemAbilities.AXE_SCRAPE, false));
            if (optional1.isPresent()) {
                level.playSound(player, pos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.levelEvent(player, 3005, pos, 0);
                return optional1;
            } else {
                Optional<BlockState> optional2 = Optional.ofNullable(state.getToolModifiedState(p_40529_, ItemAbilities.AXE_WAX_OFF, false));
                if (optional2.isPresent()) {
                    level.playSound(player, pos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.levelEvent(player, 3004, pos, 0);
                    return optional2;
                } else {
                    return Optional.empty();
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
        if (this.hasTreeCutter) {
            String activeComponent;
            if (ItemUtils.isActive(stack)) {
                activeComponent = ChatFormatting.GREEN + IRTranslations.ACTIVE.component().getString() + ChatFormatting.RESET;
            } else {
                activeComponent = ChatFormatting.RED + IRTranslations.INACTIVE.component().getString() + ChatFormatting.RESET;
            }
            tooltip.add(IRTranslations.TREE_CUTTER_STATUS.component(activeComponent).withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, ctx, tooltip, flag);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        if (this.hasTreeCutter && ItemUtils.isActive(stack) && miningEntity instanceof Player player) {
            if (!player.isShiftKeyDown()) {
                this.cutTree(player, pos, stack);
            }
        }
        return super.mineBlock(stack, level, state, pos, miningEntity);
    }

    private void cutTree(Player player, BlockPos pos, ItemStack stack) {
        Level level = player.level();
        BlockState state = level.getBlockState(pos);
        if (state.is(BlockTags.LOGS)) {
            this.cutTree(level, player, stack, pos, new int[]{0});
        }
    }

    private void cutTree(Level level, Player player, ItemStack stack, BlockPos pos, int[] blocksBroken) {
        for (BlockPos offsetPos : get3x3x3MiningArea(pos)) {
            BlockState state = level.getBlockState(offsetPos);

            if (state.is(BlockTags.LOGS) && blocksBroken[0] < 64 && this.canWork(stack, player)) {
                level.destroyBlock(offsetPos, true, player);
                this.consumeEnergy(stack, player);
                ++blocksBroken[0];
                cutTree(level, player, stack, offsetPos, blocksBroken);
            }
        }
    }

    public static Iterable<BlockPos> get3x3x3MiningArea(BlockPos center) {
        return BlockPos.betweenClosed(center.offset(-1, -1, -1), center.offset(1, 1, 1));
    }

}