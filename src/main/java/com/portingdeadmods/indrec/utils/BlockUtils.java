package com.portingdeadmods.indrec.utils;

import net.minecraft.core.Direction;
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
}
