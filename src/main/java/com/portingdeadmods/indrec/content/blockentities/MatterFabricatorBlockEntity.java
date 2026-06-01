package com.portingdeadmods.indrec.content.blockentities;

import com.portingdeadmods.indrec.IRCapabilities;
import com.portingdeadmods.indrec.IRConfig;
import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;
import com.portingdeadmods.indrec.api.energy.EnergyHandler;
import com.portingdeadmods.indrec.content.menus.MatterFabricatorMenu;
import com.portingdeadmods.indrec.data.maps.IRDataMaps;
import com.portingdeadmods.indrec.data.maps.MatterFabricatorAmplifier;
import com.portingdeadmods.indrec.impl.energy.EnergyHandlerImpl;
import com.portingdeadmods.indrec.impl.items.LimitedItemHandler;
import com.portingdeadmods.indrec.registries.IRItems;
import com.portingdeadmods.indrec.registries.IRMachines;
import com.portingdeadmods.indrec.registries.IRTranslations;
import com.portingdeadmods.portingdeadlibs.utils.capabilities.HandlerUtils;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MatterFabricatorBlockEntity extends MachineBlockEntity implements MenuProvider {
    private final IItemHandler exposedItemHandler;

    public MatterFabricatorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRMachines.MATTER_FABRICATOR, blockPos, blockState);
        this.addMachineEuStorage(EnergyHandlerImpl.NoDrain::new, this::onEuChanged);
        this.addItemHandler(HandlerUtils::newItemStackHandler, builder -> builder
                .slots(3)
                .validator((slot, item) -> switch (slot) {
                    case 0 -> item.getItemHolder().getData(IRDataMaps.MATTER_FABRICATOR_AMPLIFIERS) != null;
                    case 1 -> false;
                    case 2 -> item.getCapability(IRCapabilities.ENERGY_ITEM) != null;
                    default -> throw new IllegalArgumentException("Non existent slot " + slot + "on Matter Fabricator");
                })
                .onChange(this::onItemsChanged));
        this.exposedItemHandler = new LimitedItemHandler(this.getItemHandler(), IntSet.of(0), IntSet.of(1), IntSet.of(2));
    }

    @Override
    protected void onEuChanged(int oldAmount) {
        if (oldAmount == 0) {
            setActive(true);
        } else if (this.getEuStorage().getEnergyStored() <= 0) {
            setActive(false);
        }
    }

    @Override
    protected void tickRecipe() {
        int remainder = 0;

        ItemStack amplifierStack = this.getItemHandler().getStackInSlot(0);
        EnergyHandler euStorage = this.getEuStorage();

        boolean canInsertMatter = forceInsertItem((IItemHandlerModifiable) this.getItemHandler(), 1, IRItems.UU_MATTER.toStack(), true, this::onItemsChanged).isEmpty();

        if (!amplifierStack.isEmpty()) {
            MatterFabricatorAmplifier amplifier = amplifierStack.getItemHolder().getData(IRDataMaps.MATTER_FABRICATOR_AMPLIFIERS);
            int amplifierEnergy = (int) (IRConfig.matterFabricatorEnergyCapacity * amplifier.energyReduction());
            if (euStorage.getEnergyStored() + amplifierEnergy < euStorage.getEnergyCapacity() || canInsertMatter) {
                remainder = amplifierEnergy - euStorage.fillEnergy(amplifierEnergy, false);
                this.getItemHandler().extractItem(0, 1, false);
            }
        }

        if (euStorage.getEnergyStored() >= euStorage.getEnergyCapacity() && canInsertMatter) {
            euStorage.setEnergyStored(remainder);

            forceInsertItem((IItemHandlerModifiable) this.getItemHandler(), 1, IRItems.UU_MATTER.toStack(), false, this::onItemsChanged);
        }

    }

    @Override
    protected void onItemsChanged(int slot) {
        this.setChanged();
    }

    @Override
    public IItemHandler getItemHandlerOnSide(Direction direction) {
        return this.exposedItemHandler;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return IRTranslations.MATTER_FABRICATOR.component();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new MatterFabricatorMenu(i, inventory, this);
    }
}
