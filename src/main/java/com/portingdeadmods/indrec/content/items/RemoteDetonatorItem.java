package com.portingdeadmods.indrec.content.items;

import com.portingdeadmods.indrec.registries.IRDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class RemoteDetonatorItem extends Item {
    public RemoteDetonatorItem(Properties properties) {
        super(properties);
    }

    public static RemoteDetonatorItem defaultItem(Properties properties) {
        return new RemoteDetonatorItem(properties.component(IRDataComponents.TARGET_POS, Optional.empty()));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        BlockPos targetPos = getTargetPos(stack);
        if (targetPos != null) {
            BlockState blockState = level.getBlockState(targetPos);
            if (blockState.getBlock() instanceof TntBlock tntBlock) {
                tntBlock.onCaughtFire(blockState, level, targetPos, null, player);
                level.removeBlock(targetPos, false);
            } else {
                player.displayClientMessage(Component.literal("Failed to detonate Block").withStyle(ChatFormatting.RED), true);
            }
            setTargetPos(stack, null);
            return InteractionResultHolder.success(stack);
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        BlockPos targetPos = context.getClickedPos();
        setTargetPos(stack, targetPos);
        context.getPlayer().displayClientMessage(Component.literal("Set Detonator Position to (x: %d, y: %d, z: %d)".formatted(targetPos.getX(), targetPos.getY(), targetPos.getZ())), true);
        return InteractionResult.SUCCESS;
    }

    private static void setTargetPos(ItemStack stack, BlockPos targetPos) {
        stack.set(IRDataComponents.TARGET_POS, Optional.ofNullable(targetPos));
    }

    private static @Nullable BlockPos getTargetPos(ItemStack stack) {
        Optional<BlockPos> value = stack.getOrDefault(IRDataComponents.TARGET_POS, Optional.empty());
        return value.orElse(null);
    }

}
