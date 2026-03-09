package com.portingdeadmods.indrec.impl.networks;

import com.mojang.serialization.Codec;
import com.portingdeadmods.indrec.IRCapabilities;
import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;
import com.portingdeadmods.indrec.api.energy.EnergyHandler;
import com.portingdeadmods.indrec.api.energy.EnergyTier;
import com.portingdeadmods.indrec.api.energy.TieredEnergy;
import com.portingdeadmods.indrec.registries.IREnergyTiers;
import com.thepigcat.transportlib.api.TransportNetwork;
import com.thepigcat.transportlib.api.Transporting;
import com.thepigcat.transportlib.api.TransportingHandler;
import com.thepigcat.transportlib.impl.TransportingImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EnergyTransportHandler implements TransportingHandler<TieredEnergy> {
    public static final EnergyTransportHandler INSTANCE = new EnergyTransportHandler();

    private EnergyTransportHandler() {
    }

    @Override
    public TieredEnergy defaultValue() {
        return new TieredEnergy(0, IREnergyTiers.NONE.get());
    }

    @Override
    public boolean validTransportValue(TieredEnergy value) {
        return value.energy() >= 0;
    }

    @Override
    public List<TieredEnergy> split(TieredEnergy value, int amount) {
        // TODO: Only split for interactors we can actually interact with
        List<Integer> split = splitNumberEvenly(value.energy(), amount);
        List<TieredEnergy> split1 = new ArrayList<>();
        for (Integer i : split) {
            split1.add(new TieredEnergy(i, value.tier()));
        }
        return split1;
    }

    @Override
    public @Nullable TieredEnergy join(TieredEnergy value0, TieredEnergy value1) {
        EnergyTier higherTier;
        if (value0.tier().order() > value1.tier().order()) {
            higherTier = value0.tier();
        } else {
            higherTier = value1.tier();
        }
        if (value1.energy() > 0 && value0.energy() > Integer.MAX_VALUE - value1.energy()) {
            return null;
        } else if (value1.energy() < 0 && value0.energy() < Integer.MIN_VALUE - value1.energy()) {
            return null;
        }
        return new TieredEnergy(value0.energy() + value1.energy(), higherTier);
    }

    @Override
    public TieredEnergy remove(TieredEnergy value, TieredEnergy toRemove) {
        return new TieredEnergy(Math.max(value.energy() - toRemove.energy(), this.defaultValue().energy()), value.tier());
    }

    @Override
    public Codec<TieredEnergy> valueCodec() {
        return TieredEnergy.CODEC;
    }

    @Override
    public TieredEnergy receive(ServerLevel level, BlockPos interactorPos, Direction direction, TieredEnergy value) {
        BlockEntity blockEntity = level.getBlockEntity(interactorPos);

        if (blockEntity != null) {
            EnergyHandler energyHandler = level.getCapability(IRCapabilities.ENERGY_BLOCK, interactorPos, blockEntity.getBlockState(), blockEntity, direction);
            if (blockEntity instanceof MachineBlockEntity machineBE && machineBE.isBurnt()) {
                return value;
            }

            if (energyHandler != null) {
                EnergyTier valueTier = value.tier();
                EnergyTier handlerTier = energyHandler.getEnergyTier();
                int tierDiff = valueTier.order() - handlerTier.order();
                if (valueTier.compareTo(handlerTier) < 0 && tierDiff > 1) {
                    if (blockEntity instanceof MachineBlockEntity machineBE) {
                        machineBE.setBurnt(true);
                    }
                }
                int filled = energyHandler.fillEnergy(value.energy(), false);
                return this.remove(value, new TieredEnergy(filled, value.tier()));
            }
        }
        return value;
    }

    @Override
    public Transporting<TieredEnergy> createTransporting(TransportNetwork<TieredEnergy> network) {
        return new TransportingImpl<>(network);
    }

    private static List<Integer> splitNumberEvenly(int number, int parts) {
        if (parts <= 0) {
            throw new IllegalArgumentException("Number of parts must be greater than 0");
        }

        List<Integer> result = NonNullList.withSize(parts, 0);
        int remainder = number % parts;
        int quotient = number / parts;

        for (int i = 0; i < remainder; i++) {
            result.set(i, quotient + 1);
        }

        for (int i = remainder; i < parts; i++) {
            result.set(i, quotient);
        }

        return result;
    }
}
