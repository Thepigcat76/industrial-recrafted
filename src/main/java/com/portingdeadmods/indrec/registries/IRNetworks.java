package com.portingdeadmods.indrec.registries;

import com.portingdeadmods.indrec.IRCapabilities;
import com.portingdeadmods.indrec.api.energy.TieredEnergy;
import com.portingdeadmods.indrec.impl.networks.EnergyNetwork;
import com.portingdeadmods.indrec.impl.networks.EnergyTransportHandler;
import com.thepigcat.transportlib.TransportLib;
import com.thepigcat.transportlib.api.TransferSpeed;
import com.thepigcat.transportlib.api.TransportNetwork;
import com.thepigcat.transportlib.impl.TransportNetworkImpl;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class IRNetworks {
    public static final DeferredRegister<TransportNetwork<?>> NETWORKS = DeferredRegister.create(TransportLib.NETWORK_REGISTRY, TransportLib.MODID);

    public static final Supplier<EnergyNetwork> ENERGY = NETWORKS.register("energy", () -> EnergyNetwork.build(TransportNetworkImpl.builder(EnergyTransportHandler.INSTANCE)
            //.synced(TieredEnergy.STREAM_CODEC)
            .interactorCheck((level, cablePos, interactorPos, dir) -> {
                BlockEntity be = level.getBlockEntity(interactorPos);
                if (be != null) {
                    return level.getCapability(IRCapabilities.ENERGY_BLOCK, be.getBlockPos(), be.getBlockState(), be, dir) != null;
                }
                return false;
            })
            .transferSpeed(TransferSpeed::instant))
    );

}
