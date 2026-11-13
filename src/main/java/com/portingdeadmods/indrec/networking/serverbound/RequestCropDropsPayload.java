package com.portingdeadmods.indrec.networking.serverbound;

import net.minecraft.world.item.ItemStack;
import com.portingdeadmods.indrec.content.items.electric.CropAnalyzerItem;
import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.networking.clientbound.CropDropsPayload;
import com.portingdeadmods.indrec.utils.CropAnalysisUtils;
import com.portingdeadmods.indrec.utils.CropDropsKey;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

/**
 * Sent by the client while hovering a crop with the crop analyzer, since loot tables only exist on the server
 */
public record RequestCropDropsPayload(BlockPos pos) implements CustomPacketPayload {
    public static final Type<RequestCropDropsPayload> TYPE = new Type<>(IndustrialRecrafted.rl("request_crop_drops"));
    public static final StreamCodec<ByteBuf, RequestCropDropsPayload> STREAM_CODEC = BlockPos.STREAM_CODEC.map(RequestCropDropsPayload::new, RequestCropDropsPayload::pos);

    @Override
    public @NotNull Type<RequestCropDropsPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            ItemStack analyzer = CropAnalyzerItem.getHeldAnalyzer(player);
            if (!(player.level() instanceof ServerLevel level)
                    || analyzer.isEmpty()
                    || !player.canInteractWithBlock(this.pos, 1)
                    || !level.isLoaded(this.pos)) return;

            CropDropsKey key = CropDropsKey.of(level, this.pos);
            // Don't reply for crops the held analyzer didn't analyze, since the client would cache the empty drops for that crop
            if (key != null && (!key.industrial() || CropAnalysisUtils.getAnalyzedCrop(level, this.pos, analyzer) != null)) {
                context.reply(new CropDropsPayload(key, CropAnalysisUtils.getPossibleDrops(level, this.pos, analyzer)));
            }
        }).exceptionally(err -> {
            IndustrialRecrafted.LOGGER.error("Failed to handle RequestCropDropsPayload", err);
            return null;
        });
    }
}
