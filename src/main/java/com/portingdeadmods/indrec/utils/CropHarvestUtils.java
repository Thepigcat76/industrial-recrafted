package com.portingdeadmods.indrec.utils;

import com.portingdeadmods.indrec.api.energy.items.ElectricConsumerItem;
import com.portingdeadmods.indrec.tags.IRTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;

public final class CropHarvestUtils {
    private static final String AGE_PROPERTY = "age";

    private CropHarvestUtils() {
    }

    public static InteractionResult tryHarvest(Level level, BlockPos pos, ItemStack hoe, @Nullable Player player, InteractionHand hand) {
        BlockState state = level.getBlockState(pos);
        BlockState replantedState = getReplantedState(state);
        if (replantedState == null || !canUseHoe(hoe, player)) {
            return InteractionResult.PASS;
        }

        if (level instanceof ServerLevel serverLevel) {
            Item seed = state.getBlock().asItem();
            boolean seedRemoved = false;
            for (ItemStack drop : Block.getDrops(state, serverLevel, pos, level.getBlockEntity(pos), player, hoe)) {
                // One seed stays planted
                if (!seedRemoved && drop.is(seed)) {
                    drop.shrink(1);
                    seedRemoved = true;
                }
                if (!drop.isEmpty()) {
                    Block.popResource(level, pos, drop);
                }
            }
            level.setBlockAndUpdate(pos, replantedState);
            useHoe(hoe, player, hand);
        }
        level.playSound(player, pos, SoundEvents.CROP_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    public static boolean isHarvestable(BlockState state) {
        if (state.getBlock() instanceof CropBlock) return true;
        return state.is(IRTags.BlockTags.HOE_HARVESTABLE) && getAgeProperty(state) != null;
    }

    public static @Nullable BlockState getReplantedState(BlockState state) {
        if (state.getBlock() instanceof CropBlock crop) {
            return crop.isMaxAge(state) ? crop.getStateForAge(0) : null;
        }

        if (state.is(IRTags.BlockTags.HOE_HARVESTABLE)) {
            IntegerProperty age = getAgeProperty(state);
            if (age != null) {
                int maxAge = Collections.max(age.getPossibleValues());
                int minAge = Collections.min(age.getPossibleValues());

                return state.getValue(age) == maxAge ? state.setValue(age, minAge) : null;
            }
        }
        return null;
    }

    public static boolean canUseHoe(ItemStack hoe, @Nullable Player player) {
        return !(hoe.getItem() instanceof ElectricConsumerItem consumer) || consumer.canWork(hoe, player);
    }

    public static void useHoe(ItemStack hoe, @Nullable Player player, InteractionHand hand) {
        if (hoe.getItem() instanceof ElectricConsumerItem consumer) {
            consumer.consumeEnergy(hoe, player);
        } else if (player != null) {
            hoe.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
        }
    }

    private static @Nullable IntegerProperty getAgeProperty(BlockState state) {
        for (Property<?> property : state.getProperties()) {
            if (property instanceof IntegerProperty intProperty && property.getName().equals(AGE_PROPERTY)) {
                return intProperty;
            }
        }
        return null;
    }
}
