package com.portingdeadmods.indrec.content.items.electric;

import com.portingdeadmods.indrec.IRConfig;
import com.portingdeadmods.indrec.api.energy.items.ElectricDiggerItem;
import com.portingdeadmods.indrec.api.energy.EnergyHandler;
import com.portingdeadmods.indrec.api.energy.EnergyTier;
import com.portingdeadmods.indrec.registries.IRDataComponents;
import com.portingdeadmods.indrec.registries.IREnergyTiers;
import com.portingdeadmods.indrec.registries.IRTranslations;
import com.portingdeadmods.indrec.tags.IRTags;
import com.portingdeadmods.indrec.utils.ItemUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class ElectricDrillItem extends ElectricDiggerItem {
    private final boolean hasAoeMining;

    public ElectricDrillItem(Properties properties, float attackSpeed, float baseAttackDamage, Tier tier, Supplier<? extends EnergyTier> energyTier, IntSupplier energyUsage, IntSupplier energyCapacity, boolean hasAoeMining) {
        super(properties, attackSpeed, baseAttackDamage, tier, IRTags.BlockTags.MINEABLE_WITH_DRILL, energyTier, energyUsage, energyCapacity);
        this.hasAoeMining = hasAoeMining;
    }

    public static ElectricDrillItem basicItem(Properties properties) {
        return new ElectricDrillItem(properties, -2.8F, 1, Tiers.IRON, IREnergyTiers.LOW, () -> IRConfig.basicDrillEnergyUsage, () -> IRConfig.basicDrillCapacity, false);
    }

    public static ElectricDrillItem advancedItem(Properties properties) {
        return new ElectricDrillItem(properties.component(IRDataComponents.ACTIVE, false), -2.8F, 1, Tiers.DIAMOND, IREnergyTiers.HIGH, () -> IRConfig.advancedDrillEnergyUsage, () -> IRConfig.advancedDrillCapacity, true);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_PICKAXE_ACTIONS.contains(itemAbility) || ItemAbilities.DEFAULT_SHOVEL_ACTIONS.contains(itemAbility);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {
        ItemStack stack = player.getItemInHand(interactionHand);
        if (this.hasAoeMining && player.isShiftKeyDown()) {
            boolean active = ItemUtils.toggleActive(stack);
            player.displayClientMessage(IRTranslations.TOGGLED_AOE.component(active ? IRTranslations.ON.component().getString() : IRTranslations.OFF.component().getString()).withStyle(ChatFormatting.GRAY), true);
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
        if (this.hasAoeMining) {
            String activeComponent;
            if (ItemUtils.isActive(stack)) {
                activeComponent = ChatFormatting.GREEN + IRTranslations.ACTIVE.component().getString() + ChatFormatting.RESET;
            } else {
                activeComponent = ChatFormatting.RED + IRTranslations.INACTIVE.component().getString() + ChatFormatting.RESET;
            }
            tooltip.add(IRTranslations.AOE_STATUS.component(activeComponent).withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, ctx, tooltip, flag);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        if (this.hasAoeMining && ItemUtils.isActive(stack) && miningEntity instanceof Player player) {
            BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
            if (!player.isShiftKeyDown()) {
                EnergyHandler batterySource = findActiveBatteryEnergy(player);
                int drillEnergy = getEnergyCap(stack).getEnergyStored();
                int batteryEnergy = batterySource != null ? batterySource.getEnergyStored() : 0;
                int availableEnergy = Math.max(drillEnergy, batteryEnergy);
                this.mine3x3(player, pos, stack, hitResult.getDirection(), availableEnergy, this.getEnergyUsage(stack, miningEntity));
            }
        }
        return super.mineBlock(stack, level, state, pos, miningEntity);
    }

    private void mine3x3(LivingEntity miningEntity, BlockPos pos, ItemStack stack, Direction hitFace, int stored, int costPerBlock) {
        Level level = miningEntity.level();
        int blocksToBreak = Math.min(stored / costPerBlock, 9);

        Iterable<BlockPos> blocksToMine = get3x3MiningArea(pos, hitFace);

        EnergyHandler batterySource = miningEntity instanceof Player player ? findActiveBatteryEnergy(player) : null;

        boolean drainedFirst = true;
        for (BlockPos targetPos : blocksToMine) {
            if (blocksToBreak > 0 && canMine(level, targetPos, level.getBlockState(targetPos))) {
                boolean hasEnergy;
                if (batterySource != null && batterySource.getEnergyStored() >= costPerBlock) {
                    hasEnergy = true;
                } else {
                    hasEnergy = this.canWork(stack, miningEntity);
                }

                if (!hasEnergy) break;

                level.destroyBlock(targetPos, true);
                --blocksToBreak;

                if (!drainedFirst) {
                    if (batterySource != null && batterySource.getEnergyStored() >= costPerBlock) {
                        batterySource.drainEnergy(costPerBlock, false);
                    } else {
                        this.consumeEnergy(stack, miningEntity);
                    }
                } else {
                    drainedFirst = false;
                }
            }
        }
    }

    private static boolean canMine(Level level, BlockPos pos, BlockState state) {
        return (state.is(BlockTags.MINEABLE_WITH_PICKAXE) || state.is(BlockTags.MINEABLE_WITH_SHOVEL)) && level.getBlockEntity(pos) == null;
    }

    public static Iterable<BlockPos> get3x3MiningArea(BlockPos center, Direction hitFace) {
        return switch (hitFace) {
            case NORTH, SOUTH -> BlockPos.betweenClosed(center.offset(-1, -1, 0), center.offset(1, 1, 0));
            case EAST, WEST -> BlockPos.betweenClosed(center.offset(0, -1, -1), center.offset(0, 1, 1));
            default -> BlockPos.betweenClosed(center.offset(-1, 0, -1), center.offset(1, 0, 1));
        };
    }

}