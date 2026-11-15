package com.portingdeadmods.indrec.content.blockentities;

import com.portingdeadmods.indrec.api.blockentities.UpgradeBlockEntity;
import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;
import com.portingdeadmods.indrec.api.recipes.MachineRecipe;
import com.portingdeadmods.indrec.content.recipes.flags.ItemInputComponentFlag;
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

public class RecyclerBlockEntity extends MachineBlockEntity implements UpgradeBlockEntity {
    private final IItemHandler exposedItemHandler;

    public RecyclerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRMachines.RECYCLER, blockPos, blockState);
        this.addUpgradeItemHandler(UpgradeBlockEntity.DEFAULT_UPGRADE_SLOTS);
        this.addMachineEuStorage(EnergyHandlerImpl.NoDrain::new, this::onEuChanged);
        this.addItemHandler(HandlerUtils::newItemStackHandler, builder -> builder
                .slots(3)
                .validator((slot, item) -> switch (slot) {
                    case 0 -> true;
                    case 1 -> false;
                    case 2 -> this.isValidChargingItem(item);
                    default -> throw new IllegalArgumentException("Non existent slot " + slot + "on Recycler");
                })
                .onChange(this::onItemsChanged));
        this.exposedItemHandler = new LimitedItemHandler(this.getItemHandler(), IntSet.of(0), IntSet.of(1), IntSet.of(2));
    }



    @Override
    protected @NotNull MachineRecipe.MachineRecipeInput createRecipeInput() {
        return new MachineRecipe.MachineRecipeInput(this.getItemHandler().getStackInSlot(0));
    }

    @Override
    protected void handleSpecialItemInput(ItemInputComponentFlag itemInput) {
        this.getItemHandler().extractItem(0, 1, false);
    }

    @Override
    public IItemHandler getItemHandlerOnSide(Direction direction) {
        return this.exposedItemHandler;
    }
}
