package com.portingdeadmods.indrec.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Implement this if you want your block to drop a special
 * items when picked up with a wrench. Still requires the
 * block to have the {@link com.portingdeadmods.indrec.tags.IRTags.BlockTags.WRENCHABLE}
 */
public interface CustomWrenchableBlock {
    ItemStack getCustomDropItem();

    default boolean canWrench(Level level, BlockPos blockPos, BlockState blockState) {
        return true;
    }
}