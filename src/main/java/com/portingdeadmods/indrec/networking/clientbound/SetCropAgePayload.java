package com.portingdeadmods.indrec.networking.clientbound;

import com.portingdeadmods.indrec.IRCapabilities;
import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.api.energy.EnergyHandler;
import com.portingdeadmods.indrec.content.blockentities.CropBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetCropAgePayload(BlockPos pos, int age) implements CustomPacketPayload {
    public static final Type<SetCropAgePayload> TYPE = new Type<>(IndustrialRecrafted.rl("set_crop_age_payload"));
    public static final StreamCodec<? super RegistryFriendlyByteBuf, SetCropAgePayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            SetCropAgePayload::pos,
            ByteBufCodecs.INT,
            SetCropAgePayload::age,
            SetCropAgePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = context.player().level();
            if (level.getBlockEntity(pos) instanceof CropBlockEntity cropBE) {
                cropBE.setAge(this.age());
            }
        }).exceptionally(err -> {
            IndustrialRecrafted.LOGGER.error("Failed to handle SetCropAgePayload", err);
            return null;
        });
    }

}
