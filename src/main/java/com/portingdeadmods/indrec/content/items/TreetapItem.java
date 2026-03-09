package com.portingdeadmods.indrec.content.items;

import com.portingdeadmods.indrec.content.blocks.RubberTreeResinHoleBlock;
import com.portingdeadmods.indrec.registries.IRBlocks;
import com.portingdeadmods.indrec.registries.IRItems;
import com.portingdeadmods.portingdeadlibs.utils.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class TreetapItem extends Item {
    public TreetapItem(Properties properties) {
        super(properties);
    }

    public static TreetapItem defaultItem(Properties properties) {
        return new TreetapItem(properties.durability(80));
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();
        BlockPos blockPos = useOnContext.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);
        if (blockState.is(IRBlocks.RUBBER_TREE_RESIN_HOLE.get()) && blockState.getValue(RubberTreeResinHoleBlock.RESIN)) {
            level.setBlockAndUpdate(blockPos, blockState.setValue(RubberTreeResinHoleBlock.RESIN, false));
            ItemStack resinDrop = new ItemStack(IRItems.STICKY_RESIN.get());
            RandomSource random = useOnContext.getLevel().getRandom();
            int randomInt = random.nextInt(1, 4);
            resinDrop.setCount(randomInt);
            ItemUtils.giveItemToPlayerNoSound(useOnContext.getPlayer(), resinDrop);
            level.playSound(null, blockPos, SoundEvents.HONEY_BLOCK_PLACE, SoundSource.PLAYERS);
            useOnContext.getItemInHand().hurtAndBreak(1, useOnContext.getPlayer(), LivingEntity.getSlotForHand(useOnContext.getHand()));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }
}