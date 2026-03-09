package com.portingdeadmods.indrec.content.blocks;

import com.mojang.serialization.MapCodec;
import com.portingdeadmods.indrec.IRCapabilities;
import com.portingdeadmods.indrec.api.blocks.PipeBlock;
import com.portingdeadmods.indrec.api.energy.EnergyTier;
import com.portingdeadmods.indrec.api.energy.blocks.EnergyTierBlock;
import com.portingdeadmods.indrec.registries.IRNetworks;
import com.portingdeadmods.indrec.utils.TooltipUtils;
import com.portingdeadmods.portingdeadlibs.utils.capabilities.CapabilityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

public class CableBlock extends PipeBlock implements EnergyTierBlock {
    private final Supplier<? extends EnergyTier> energyTier;

    public CableBlock(Properties properties, int width, Supplier<? extends EnergyTier> energyTier) {
        super(properties, width);
        this.energyTier = energyTier;
    }

    protected boolean canTransport() {
        return true;
    }

    // TODO: Create nodes for connections to interactors
    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);

        if (level instanceof ServerLevel serverLevel && this.canTransport()) {
            int connectionsAmount = 0;
            boolean[] connections = new boolean[6];
            Direction[] directions = new Direction[6];
            Set<BlockPos> interactors = new HashSet<>();
            Set<Direction> interactorConnections = new HashSet<>();

            for (Direction dir : Direction.values()) {
                boolean connected = state.getValue(CONNECTION[dir.get3DDataValue()]);
                connections[dir.get3DDataValue()] = connected;
                if (connected) {
                    directions[dir.get3DDataValue()] = dir;
                    connectionsAmount++;
                    if (IRNetworks.ENERGY.get().checkForInteractorAt(serverLevel, pos, dir)) {
                        interactors.add(pos.relative(dir));
                        interactorConnections.add(dir);
                    }
                } else {
                    directions[dir.get3DDataValue()] = null;
                }
            }

            if ((connectionsAmount == 2
                    && ((connections[0] && connections[1])
                    || (connections[2] && connections[3])
                    || (connections[4] && connections[5]))) || connectionsAmount == 0) {
                if (IRNetworks.ENERGY.get().hasNodeAt(serverLevel, pos)) {
                    IRNetworks.ENERGY.get().removeNode(serverLevel, pos);
                }

                Direction direction0 = null;
                Direction direction1 = null;
                for (Direction direction : directions) {
                    if (direction != null) {
                        if (direction0 == null) {
                            direction0 = direction;
                        } else {
                            direction1 = direction;
                        }
                    }
                }

                if (!interactors.isEmpty()) {
                    IRNetworks.ENERGY.get().addNode(serverLevel, pos, directions, false);
                } else if (direction0 != null && direction1 != null) {
                    IRNetworks.ENERGY.get().connect(serverLevel, pos, direction0, direction1);
                }
            } else {
                IRNetworks.ENERGY.get().addNode(serverLevel, pos, directions, connectionsAmount == 1);
            }

        }

    }

    @Override
    public @NotNull BlockState updateShape(BlockState blockState, Direction facingDirection, BlockState facingBlockState, LevelAccessor level, BlockPos blockPos, BlockPos facingBlockPos) {
        if (level instanceof ServerLevel serverLevel) {
            if (IRNetworks.ENERGY.get().checkForInteractorAt(serverLevel, blockPos, facingDirection)) {
                int connectionsAmount = 0;
                Direction[] directions = new Direction[6];
                for (Direction direction : Direction.values()) {
                    boolean value = blockState.getValue(CONNECTION[direction.get3DDataValue()]);
                    if (value) {
                        directions[direction.get3DDataValue()] = direction;
                        connectionsAmount++;
                    }
                }

                IRNetworks.ENERGY.get().addNode(serverLevel, blockPos, directions, connectionsAmount == 1);
            }
        }
        return super.updateShape(blockState, facingDirection, facingBlockState, level, blockPos, facingBlockPos);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);

        if (level instanceof ServerLevel serverLevel) {
            if (!state.is(newState.getBlock())) {
                if (IRNetworks.ENERGY.get().hasNodeAt(serverLevel, pos)) {
                    IRNetworks.ENERGY.get().removeNode(serverLevel, pos);
                } else {
                    List<Direction> directions = getDirections(state);
                    if (directions.size() == 2) {
                        Direction direction0 = directions.getFirst();
                        Direction direction1 = directions.get(1);
                        if (direction0 == direction1.getOpposite()) {
                            IRNetworks.ENERGY.get().disconnect(serverLevel, pos, direction0, direction1);
                        }
                    }
                }

            }
        }

    }

    private static List<Direction> getDirections(BlockState state) {
        List<Direction> directions = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            if (state.getValue(CONNECTION[direction.get3DDataValue()])) {
                directions.add(direction);
            }
        }
        return directions;
    }

    @Override
    public EnergyTier getEnergyTier() {
        return this.energyTier.get();
    }

    @Override
    public boolean canConnectToPipe(BlockState connectTo) {
        return connectTo.getBlock() instanceof CableBlock cableBlock && cableBlock.getEnergyTier() == this.getEnergyTier();
    }

    @Override
    public boolean canConnectTo(BlockEntity connectTo) {
        return connectTo.getLevel().getCapability(IRCapabilities.ENERGY_BLOCK, connectTo.getBlockPos(), null) != null
                || CapabilityUtils.blockEntityCapability(Capabilities.EnergyStorage.BLOCK, connectTo) != null;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        TooltipUtils.addEnergyTierTooltip(tooltipComponents, this.getEnergyTier());

    }

    @Override
    protected MapCodec<? extends PipeBlock> codec() {
        return null;
    }
}
