package com.portingdeadmods.indrec.networking.clientbound;

import com.portingdeadmods.indrec.IRCapabilities;
import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.api.energy.EnergyHandler;
import com.portingdeadmods.indrec.api.energy.TieredEnergy;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetEnergyPayload(BlockPos pos, int energy) implements CustomPacketPayload {
    public static final Type<SetEnergyPayload> TYPE = new Type<>(IndustrialRecrafted.rl("set_energy_payload"));
    public static final StreamCodec<? super RegistryFriendlyByteBuf, SetEnergyPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            SetEnergyPayload::pos,
            ByteBufCodecs.INT,
            SetEnergyPayload::energy,
            SetEnergyPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = context.player().level();
            EnergyHandler energyHandler = level.getCapability(IRCapabilities.ENERGY_BLOCK, this.pos(), null);
            if (energyHandler != null) {
                energyHandler.setEnergyStored(energy);
            }
        }).exceptionally(err -> {
            IndustrialRecrafted.LOGGER.error("Failed to handle SetEnergyPayload", err);
            return null;
        });
    }

}
