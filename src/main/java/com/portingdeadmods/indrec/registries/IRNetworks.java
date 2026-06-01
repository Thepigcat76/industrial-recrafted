package com.portingdeadmods.indrec.registries;

import com.portingdeadmods.indrec.IRCapabilities;
import com.portingdeadmods.indrec.api.energy.EnergyHandler;
import com.portingdeadmods.indrec.content.networks.EnergyNetwork;
import com.portingdeadmods.indrec.content.networks.EnergyTransportHandler;
import com.thepigcat.transportlib.TransportLib;
import com.thepigcat.transportlib.api.TransferSpeed;
import com.thepigcat.transportlib.api.TransportNetwork;
import com.thepigcat.transportlib.impl.TransportNetworkImpl;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class IRNetworks {
    public static final DeferredRegister<TransportNetwork<?>> NETWORKS = DeferredRegister.create(TransportLib.NETWORK_REGISTRY, TransportLib.MODID);

    public static final Supplier<EnergyNetwork> ENERGY = NETWORKS.register("energy", () -> EnergyNetwork.build(TransportNetworkImpl.builder(EnergyTransportHandler.INSTANCE)
            //.synced(TieredEnergy.STREAM_CODEC)
            .interactorCheck((level, cablePos, interactorPos, dir) -> {
                BlockEntity be = level.getBlockEntity(interactorPos);
                if (be != null) {
                    EnergyHandler euHandler = level.getCapability(IRCapabilities.ENERGY_BLOCK, be.getBlockPos(), be.getBlockState(), be, dir);
                    IEnergyStorage feHandler = level.getCapability(Capabilities.EnergyStorage.BLOCK, be.getBlockPos(), be.getBlockState(), be, dir);
                    return euHandler != null || feHandler != null;
                }
                return false;
            })
            .transferSpeed(TransferSpeed::instant))
    );

}
