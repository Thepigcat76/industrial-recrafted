package com.portingdeadmods.indrec.content.blockentities;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;
import com.portingdeadmods.indrec.api.energy.EnergyHandler;
import com.portingdeadmods.indrec.api.energy.EnergyTier;
import com.portingdeadmods.indrec.api.energy.TieredEnergy;
import com.portingdeadmods.indrec.content.blocks.CableBlock;
import com.portingdeadmods.indrec.content.blocks.TransformerBlock;
import com.portingdeadmods.indrec.handlers.energy.EnergyHandlerImpl;
import com.portingdeadmods.indrec.handlers.energy.EnergyHandlerWrapper;
import com.portingdeadmods.indrec.registries.IRNetworks;
import com.portingdeadmods.indrec.utils.machines.IRMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

public class TransformerBlockEntity extends MachineBlockEntity {
    private static final String INPUT_SIDE_KEY = "input_side";

    private final EnergyTier lowTier;
    private final EnergyTier highTier;

    private final EnergyHandler buffer;

    private final EnergyHandler lowSide;
    private final EnergyHandler highSide;

    private Side inputSide = Side.LOW;
    private long lastInsertTime = -1;
    private boolean sendingEnergy;

    public TransformerBlockEntity(BlockPos blockPos, BlockState blockState, IRMachine machine) {
        super(machine, blockPos, blockState);
        this.lowTier = machine.getEnergyTier();
        this.highTier = ((TransformerBlock) blockState.getBlock()).getHighTier();

        EnergyHandlerImpl buffer = new EnergyHandlerImpl(this.lowTier, this.lowTier.maxOutput());
        buffer.setOnChangedFunction(oldAmount -> this.setChanged());
        this.buffer = this.addHandler(IndustrialRecrafted.rl("buffer"), buffer);

        this.lowSide = new EnergyHandlerWrapper.TransformerSide(this.buffer, this.lowTier, simulate -> this.onInsert(Side.LOW, simulate));
        this.highSide = new EnergyHandlerWrapper.TransformerSide(this.buffer, this.highTier, simulate -> this.onInsert(Side.HIGH, simulate));
    }

    private boolean onInsert(Side side, boolean simulate) {
        // Routes through the cables can lead back to this transformer, which isn't energy coming from the outside
        if (this.sendingEnergy || this.isBurnt()) {
            return false;
        }
        if (simulate) {
            return true;
        }

        long time = this.level.getGameTime();
        if (time == this.lastInsertTime && side != this.inputSide) {
            this.setBurnt(true);
            return false;
        }
        this.lastInsertTime = time;
        this.inputSide = side;
        this.setChanged();
        return true;
    }

    @Override
    public boolean tryBurn(EnergyHandler handler, EnergyTier energyTier, int energy) {
        boolean overloaded = energy > 0 && energyTier.compareTo(handler.getEnergyTier()) > 0;
        if (overloaded) {
            this.setBurnt(true);
        }
        return overloaded;
    }

    private Direction getHighSideDirection() {
        return this.getBlockState().getValue(BlockStateProperties.FACING);
    }

    @Override
    protected void tickRecipe() {
        if (this.level instanceof ServerLevel serverLevel && this.buffer.getEnergyStored() > 0) {
            EnergyTier outputTier = this.inputSide == Side.LOW ? this.highTier : this.lowTier;
            this.sendingEnergy = true;
            for (Direction direction : Direction.values()) {
                if (this.canSpreadEnergy(direction)) {
                    this.pushEnergy(serverLevel, outputTier, direction);
                }
            }
            this.sendingEnergy = false;
        }
    }

    private void pushEnergy(ServerLevel level, EnergyTier tier, Direction direction) {
        int energy = this.buffer.getEnergyStored();
        if (energy > 0) {
            this.buffer.forceDrainEnergy(this.sendEnergy(level, energy, tier, direction), false);
        }
    }

    private int sendEnergy(ServerLevel level, int energy, EnergyTier tier, Direction direction) {
        BlockPos neighborPos = this.worldPosition.relative(direction);
        if (level.getBlockState(neighborPos).getBlock() instanceof CableBlock) {
            return energy - IRNetworks.ENERGY.get().transport(level, this.worldPosition, new TieredEnergy(energy, tier), direction).energy();
        }

        EnergyHandler neighborHandler = this.getNeighborEuHandler(direction);
        if (neighborHandler == null || !neighborHandler.isValidTier(tier)) {
            return 0;
        }
        if (level.getBlockEntity(neighborPos) instanceof MachineBlockEntity neighbor
                && (neighbor.isBurnt() || neighbor.tryBurn(neighborHandler, tier, energy))) {
            return 0;
        }
        return neighborHandler.fillEnergy(energy, false);
    }

    // tickRecipe handles spreading
    @Override
    protected void tickEnergySpreading() {
    }

    @Override
    public boolean canSpreadEnergy(Direction direction) {
        Direction highSideDirection = this.getHighSideDirection();
        return direction == (this.inputSide == Side.LOW ? highSideDirection : highSideDirection.getOpposite());
    }

    @Override
    public EnergyHandler getEuStorage() {
        return this.lowSide;
    }

    @Override
    public @Nullable EnergyHandler getEuHandlerOnSide(@Nullable Direction direction) {
        if (direction == this.getHighSideDirection().getOpposite()) {
            return this.highSide;
        } else if (direction == this.getHighSideDirection()) {
            return this.lowSide;
        }
        return null;
    }

    @Override
    protected void loadData(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadData(tag, provider);
        this.inputSide = tag.getBoolean(INPUT_SIDE_KEY) ? Side.HIGH : Side.LOW;
    }

    @Override
    protected void saveData(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveData(tag, provider);
        tag.putBoolean(INPUT_SIDE_KEY, this.inputSide == Side.HIGH);
    }

    private enum Side {
        LOW,
        HIGH
    }
}
