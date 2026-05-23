package com.portingdeadmods.indrec.content.blockentities;

import com.portingdeadmods.indrec.IRCapabilities;
import com.portingdeadmods.indrec.IRConfig;
import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;
import com.portingdeadmods.indrec.api.energy.EnergyHandler;
import com.portingdeadmods.indrec.api.energy.TieredEnergy;
import com.portingdeadmods.indrec.content.blocks.CableBlock;
import com.portingdeadmods.indrec.content.menus.BatteryBoxMenu;
import com.portingdeadmods.indrec.impl.energy.EnergyHandlerImpl;
import com.portingdeadmods.indrec.impl.energy.EnergyHandlerWrapper;
import com.portingdeadmods.indrec.registries.IRNetworks;
import com.portingdeadmods.indrec.registries.IRTranslations;
import com.portingdeadmods.indrec.utils.BlockUtils;
import com.portingdeadmods.indrec.utils.machines.IRMachine;
import com.portingdeadmods.portingdeadlibs.utils.capabilities.HandlerUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class EnergyStorageBlockEntity extends MachineBlockEntity implements MenuProvider {
    private final EnergyHandlerWrapper.NoFill outputEnergyHandler;

    public EnergyStorageBlockEntity(IRMachine machine, int energyCapacity, BlockPos blockPos, BlockState blockState) {
        super(machine, blockPos, blockState);
        addEuStorage(EnergyHandlerImpl.NoDrain::new, this.machine::getEnergyTier, energyCapacity, this::onEuChanged);
        addItemHandler(HandlerUtils::newItemStackHandler, builder -> builder
                .slots(2)
                .validator((slot, item) -> item.getCapability(IRCapabilities.ENERGY_ITEM) != null)
                .onChange(this::onItemsChanged));

        this.outputEnergyHandler = new EnergyHandlerWrapper.NoFill(this.getEuStorage());
        this.spreadDirections = Set.of(this.getOutputDirection());
    }

    @Override
    public boolean shouldSpreadEnergy() {
        return true;
    }

    @Override
    protected void onItemsChanged(int slot) {
        //this.updateData();
    }

    public boolean supportsUpgrades() {
        return false;
    }

    @Override
    public int emitRedstoneLevel() {
        return BlockUtils.calcRedstoneFromEnergy(this.getEuStorage());
    }

    @Override
    public void tick() {
        super.tick();

        if (!level.isClientSide()) {
            if (this.getRedstoneSignalType().isActive(this.getRedstoneSignalStrength())) {
                EnergyHandler thisEnergyStorage = this.getEuStorage();
                if (level instanceof ServerLevel serverLevel) {
                    int min = Math.min(thisEnergyStorage.getEnergyTier().maxOutput(), thisEnergyStorage.getEnergyStored());
                    Direction outputDirection = getOutputDirection();
                    if (level.getBlockState(worldPosition.relative(outputDirection)).getBlock() instanceof CableBlock) {
                        TieredEnergy energy = new TieredEnergy(min, this.getEuStorage().getEnergyTier());
                        int remainder = IRNetworks.ENERGY.get().transport(serverLevel, this.worldPosition, energy, outputDirection)
                                .energy();
                        thisEnergyStorage.forceDrainEnergy(min - remainder, false);
                    }
                }
            }
        }
    }

    private @NotNull Direction getOutputDirection() {
        return this.getBlockState().getValue(BlockStateProperties.FACING);
    }

    @Override
    public Set<Direction> getSpreadDirections() {
        Direction outputDirection = this.getOutputDirection();
        if (!this.spreadDirections.contains(outputDirection))
            this.spreadDirections = Set.of(outputDirection);

        return this.spreadDirections;
    }

    @Override
    public EnergyHandler getEuHandlerOnSide(Direction direction) {
        Direction facing = getOutputDirection();
        if (facing.getOpposite() == direction) {
            return this.outputEnergyHandler;
        }
        return super.getEuHandlerOnSide(direction);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return IRTranslations.BATTERY_BOX.component();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new BatteryBoxMenu(i, inventory, this);
    }

}