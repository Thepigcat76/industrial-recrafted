package com.portingdeadmods.indrec.content.blockentities;

import com.portingdeadmods.indrec.api.blockentities.UpgradeBlockEntity;
import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;
import com.portingdeadmods.indrec.content.menus.ExtractorMenu;
import com.portingdeadmods.indrec.impl.energy.EnergyHandlerImpl;
import com.portingdeadmods.indrec.impl.items.LimitedItemHandler;
import com.portingdeadmods.indrec.registries.IRMachines;
import com.portingdeadmods.indrec.registries.IRSoundEvents;
import com.portingdeadmods.indrec.registries.IRTranslations;
import com.portingdeadmods.portingdeadlibs.utils.capabilities.HandlerUtils;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExtractorBlockEntity extends MachineBlockEntity implements MenuProvider, UpgradeBlockEntity {
    private final IItemHandler exposedItemHandler;

    public ExtractorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRMachines.EXTRACTOR, blockPos, blockState);
        this.addUpgradeItemHandler(UpgradeBlockEntity.DEFAULT_UPGRADE_SLOTS);
        this.addMachineEuStorage(EnergyHandlerImpl.NoDrain::new, this::onEuChanged);
        this.addItemHandler(HandlerUtils::newItemStackHandler, builder -> builder
                .slots(4)
                .validator((slot, item) -> switch (slot) {
                    case 0 -> true;
                    case 1, 2 -> false;
                    case 3 -> this.isValidChargingItem(item);
                    default -> throw new IllegalArgumentException("Non existent slot " + slot + "on Extractor");
                })
                .onChange(this::onItemsChanged));
        this.exposedItemHandler = new LimitedItemHandler(this.getItemHandler(), IntSet.of(0), IntSet.of(1, 2), IntSet.of(3));
    }

    @Override
    protected void playMachineSound() {
        // Not based on the progress, which skips over exact values when overclocked
        if (this.level.getGameTime() % 12 == 0) {
            this.level.playSound(null, worldPosition, IRSoundEvents.EXTRACTOR.get(), SoundSource.BLOCKS, 0.08f, 1f);
        }
    }

    @Override
    public IItemHandler getItemHandlerOnSide(Direction direction) {
        return this.exposedItemHandler;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return IRTranslations.EXTRACTOR.component();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        this.syncProgress();
        return new ExtractorMenu(i, inventory, this);
    }

}
