package com.portingdeadmods.indrec.content.items;

import com.portingdeadmods.indrec.registries.IRBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;

public class CropSticksItem extends BlockItem {
    public CropSticksItem(Properties properties) {
        super(IRBlocks.CROP.get(), properties);
    }

    @Override
    protected boolean canPlace(BlockPlaceContext context, BlockState state) {
        return super.canPlace(context, state) && context.getLevel().getBlockState(context.getClickedPos().relative(context.getClickedFace().getOpposite())).is(Blocks.FARMLAND);
    }
}
