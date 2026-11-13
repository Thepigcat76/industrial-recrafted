package com.portingdeadmods.indrec.events;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.networking.clientbound.ClearCropDropsCachePayload;
import com.portingdeadmods.indrec.registries.IRBlocks;
import com.portingdeadmods.indrec.registries.IRItems;
import com.portingdeadmods.indrec.utils.BlockUtils;
import com.portingdeadmods.indrec.utils.CropAnalysisUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = IndustrialRecrafted.MODID)
public final class CommonEvents {
    @SubscribeEvent
    private static void onDatapackSync(OnDatapackSyncEvent event) {
        if (event.getPlayer() == null) {
            CropAnalysisUtils.clearCache();
            PacketDistributor.sendToAllPlayers(ClearCropDropsCachePayload.INSTANCE);
        }
    }

    @SubscribeEvent
    private static void onFarmlandTrample(BlockEvent.FarmlandTrampleEvent event) {
        LevelAccessor level = event.getLevel();
        BlockPos pos = event.getPos();

        if (level.getBlockState(pos.above()).is(IRBlocks.CROP)) {
            event.setCanceled(true);
        }

    }

    @SubscribeEvent
    private static void onServerStopped(ServerStoppedEvent event) {
        CropAnalysisUtils.clearCache();
    }

    @SubscribeEvent
    private static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = player.level();
        ItemStack mainHandItem = event.getItemStack();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);

        if (mainHandItem.is(IRItems.WRENCH)) {
            for (Property<?> prop : state.getProperties()) {
                if (prop instanceof DirectionProperty directionProperty && prop.getName().equals("facing")) {
                    BlockState rotatedState = BlockUtils.rotateBlock(state, directionProperty, state.getValue(directionProperty));
                    level.setBlock(pos, rotatedState, 3);
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ROTATE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);
                    return;
                }
            }
        }
    }
}
