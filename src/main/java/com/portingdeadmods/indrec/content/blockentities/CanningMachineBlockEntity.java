package com.portingdeadmods.indrec.content.blockentities;

import com.portingdeadmods.indrec.api.blockentities.UpgradeBlockEntity;
import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;
import com.portingdeadmods.indrec.api.recipes.MachineRecipe;
import com.portingdeadmods.indrec.handlers.energy.EnergyHandlerImpl;
import com.portingdeadmods.indrec.handlers.items.LimitedItemHandler;
import com.portingdeadmods.indrec.registries.IRMachines;
import com.portingdeadmods.portingdeadlibs.utils.capabilities.HandlerUtils;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CanningMachineBlockEntity extends MachineBlockEntity implements UpgradeBlockEntity {
    private final IItemHandler exposedItemHandler;

    public CanningMachineBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRMachines.CANNING_MACHINE, blockPos, blockState);
        this.addUpgradeItemHandler(UpgradeBlockEntity.DEFAULT_UPGRADE_SLOTS);
        this.addMachineEuStorage(EnergyHandlerImpl.NoDrain::new, this::onEuChanged);
        this.addItemHandler(HandlerUtils::newItemStackHandler, builder -> builder
                .slots(4)
                .validator((slot, item) -> switch (slot) {
                    case 0, 1 -> true;
                    case 2 -> false;
                    case 3 -> this.isValidChargingItem(item);
                    default -> throw new IllegalArgumentException("Non existent slot " + slot + "on Canning Machine");
                })
                .onChange(this::onItemsChanged));
        this.exposedItemHandler = new LimitedItemHandler(this.getItemHandler(), IntSet.of(0, 1), IntSet.of(2), IntSet.of(3));
    }

    @Override
    protected int getResultSlot() {
        return 2;
    }

    @Override
    protected @NotNull MachineRecipe.MachineRecipeInput createRecipeInput() {
        return new MachineRecipe.MachineRecipeInput(List.of(this.getItemHandler().getStackInSlot(0), this.getItemHandler().getStackInSlot(1)));
    }

    @Override
    public IItemHandler getItemHandlerOnSide(Direction direction) {
        return this.exposedItemHandler;
    }
}
