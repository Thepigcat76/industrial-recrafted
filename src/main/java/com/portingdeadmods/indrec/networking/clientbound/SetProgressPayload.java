package com.portingdeadmods.indrec.networking.clientbound;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetProgressPayload(BlockPos pos, float progress, float maxProgress) implements CustomPacketPayload {
    public static final Type<SetProgressPayload> TYPE = new Type<>(IndustrialRecrafted.rl("set_progress_payload"));
    public static final StreamCodec<? super RegistryFriendlyByteBuf, SetProgressPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            SetProgressPayload::pos,
            ByteBufCodecs.FLOAT,
            SetProgressPayload::progress,
            ByteBufCodecs.FLOAT,
            SetProgressPayload::maxProgress,
            SetProgressPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = context.player().level();
            if (level.getBlockEntity(this.pos()) instanceof MachineBlockEntity machineBE) {
                machineBE.setProgess(this.progress);
                machineBE.setMaxProgress(this.maxProgress);
            }
        }).exceptionally(err -> {
            IndustrialRecrafted.LOGGER.error("Failed to handle SetProgressPayload", err);
            return null;
        });
    }

}
