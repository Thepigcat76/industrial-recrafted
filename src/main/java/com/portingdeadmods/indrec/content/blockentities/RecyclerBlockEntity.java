package com.portingdeadmods.indrec.content.blockentities;

import com.portingdeadmods.indrec.IRCapabilities;
import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;
import com.portingdeadmods.indrec.content.menus.RecyclerMenu;
import com.portingdeadmods.indrec.content.recipes.MachineRecipeInput;
import com.portingdeadmods.indrec.impl.energy.EnergyHandlerImpl;
import com.portingdeadmods.indrec.impl.items.LimitedItemHandler;
import com.portingdeadmods.indrec.registries.*;
import com.portingdeadmods.portingdeadlibs.utils.capabilities.HandlerUtils;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RecyclerBlockEntity extends MachineBlockEntity implements MenuProvider {
    private final IItemHandler exposedItemHandler;

    public RecyclerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRMachines.RECYCLER, blockPos, blockState);
        this.addEuStorage(EnergyHandlerImpl.NoDrain::new, IREnergyTiers.LOW, 4000, this::onEuChanged);
        this.addItemHandler(HandlerUtils::newItemStackHandler, builder -> builder
                .slots(3)
                .validator((slot, item) -> switch (slot) {
                    case 0 -> true;
                    case 1 -> false;
                    case 2 -> item.getCapability(IRCapabilities.ENERGY_ITEM) != null;
                    default -> throw new IllegalArgumentException("Non existent slot " + slot + "on Compressor");
                })
                .onChange(this::onItemsChanged));
        this.exposedItemHandler = new LimitedItemHandler(this.getItemHandler(), IntSet.of(0), IntSet.of(1), IntSet.of(2));
    }

    @Override
    public Component getDisplayName() {
        return IRTranslations.RECYCLER.component();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new RecyclerMenu(i, inventory, this);
    }

    @Override
    protected @NotNull MachineRecipeInput createRecipeInput() {
        return new MachineRecipeInput(this.getItemHandler().getStackInSlot(0));
    }

    @Override
    public IItemHandler getItemHandlerOnSide(Direction direction) {
        return this.exposedItemHandler;
    }

}
