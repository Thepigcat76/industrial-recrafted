package com.portingdeadmods.indrec.content.blockentities;

import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;
import com.portingdeadmods.indrec.registries.IRMachines;
import com.portingdeadmods.indrec.utils.machines.IRMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class NuclearReactorBlockEntity extends MachineBlockEntity {
    public NuclearReactorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRMachines.NUCLEAR_REACTOR, blockPos, blockState);
    }
}
