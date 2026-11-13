package com.portingdeadmods.indrec.networking.clientbound;

import com.portingdeadmods.indrec.IRRegistries;
import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.api.crops.Crop;
import com.portingdeadmods.indrec.content.blockentities.CropBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetCropPayload(BlockPos pos, ResourceKey<Crop> crop) implements CustomPacketPayload {
    public static final Type<SetCropPayload> TYPE = new Type<>(IndustrialRecrafted.rl("set_crop_payload"));
    public static final StreamCodec<? super RegistryFriendlyByteBuf, SetCropPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            SetCropPayload::pos,
            ResourceKey.streamCodec(IRRegistries.CROP),
            SetCropPayload::crop,
            SetCropPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = context.player().level();
            if (level.getBlockEntity(pos) instanceof CropBlockEntity cropBE) {
                level.registryAccess().lookup(IRRegistries.CROP)
                        .flatMap(lookup -> lookup.get(this.crop))
                        .ifPresent(cropBE::setCrop);
            }
        }).exceptionally(err -> {
            IndustrialRecrafted.LOGGER.error("Failed to handle SetCropAgePayload", err);
            return null;
        });
    }

}
