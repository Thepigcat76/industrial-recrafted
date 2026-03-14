package com.portingdeadmods.indrec.utils;

import com.portingdeadmods.indrec.api.energy.EnergyHandler;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import java.util.List;

public final class BlockUtils {
    public static BlockState rotateBlock(BlockState state, DirectionProperty prop, Comparable<?> currentValue) {
        List<Direction> directions = prop.getPossibleValues().stream().toList();
        int currentDirectionIndex = directions.indexOf(currentValue);
        int nextDirectionIndex = (currentDirectionIndex + 1) % directions.size();
        Direction nextDirection = directions.get(nextDirectionIndex);
        return state.setValue(prop, nextDirection);
    }

    public static int calcRedstoneFromEnergy(EnergyHandler energyStorage) {
        if (energyStorage == null) {
            return 0;
        } else {
            return Mth.floor(((float) energyStorage.getEnergyStored() / energyStorage.getEnergyCapacity()) * 14.0F) + (energyStorage.getEnergyStored() > 0 ? 1 : 0);
        }
    }

}
