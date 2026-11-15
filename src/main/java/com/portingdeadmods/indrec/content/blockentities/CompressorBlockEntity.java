package com.portingdeadmods.indrec.content.blockentities;

import com.portingdeadmods.indrec.api.blockentities.UpgradeBlockEntity;
import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;
import com.portingdeadmods.indrec.handlers.energy.EnergyHandlerImpl;
import com.portingdeadmods.indrec.handlers.items.LimitedItemHandler;
import com.portingdeadmods.indrec.registries.IRMachines;
import com.portingdeadmods.portingdeadlibs.utils.capabilities.HandlerUtils;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;

public class CompressorBlockEntity extends MachineBlockEntity implements UpgradeBlockEntity {
    private final IItemHandler exposedItemHandler;

    public CompressorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRMachines.COMPRESSOR, blockPos, blockState);
        this.addUpgradeItemHandler(UpgradeBlockEntity.DEFAULT_UPGRADE_SLOTS);
        this.addMachineEuStorage(EnergyHandlerImpl.NoDrain::new, this::onEuChanged);
        this.addItemHandler(HandlerUtils::newItemStackHandler, builder -> builder
                .slots(3)
                .validator((slot, item) -> switch (slot) {
                    case 0 -> true;
                    case 1 -> false;
                    case 2 -> this.isValidChargingItem(item);
                    default -> throw new IllegalArgumentException("Non existent slot " + slot + "on Compressor");
                })
                .onChange(this::onItemsChanged));
        this.exposedItemHandler = new LimitedItemHandler(this.getItemHandler(), IntSet.of(0), IntSet.of(1), IntSet.of(2));
    }

    @Override
    public IItemHandler getItemHandlerOnSide(Direction direction) {
        return this.exposedItemHandler;
    }
}
