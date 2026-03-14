package com.portingdeadmods.indrec.impl.networks;

import com.portingdeadmods.indrec.IRCapabilities;
import com.portingdeadmods.indrec.api.blocks.PipeBlock;
import com.portingdeadmods.indrec.api.energy.EnergyHandler;
import com.portingdeadmods.indrec.api.energy.EnergyTier;
import com.portingdeadmods.indrec.api.energy.TieredEnergy;
import com.portingdeadmods.indrec.content.blocks.BurntCableBlock;
import com.portingdeadmods.indrec.content.blocks.CableBlock;
import com.portingdeadmods.indrec.registries.IRBlocks;
import com.portingdeadmods.portingdeadlibs.utils.capabilities.CapabilityUtils;
import com.thepigcat.transportlib.api.NetworkNode;
import com.thepigcat.transportlib.api.cache.NetworkRoute;
import com.thepigcat.transportlib.impl.TransportNetworkImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class EnergyNetwork extends TransportNetworkImpl<TieredEnergy> {
    protected EnergyNetwork(Builder<TieredEnergy> builder) {
        super(builder);
    }

    @Override
    public TieredEnergy transport(ServerLevel serverLevel, BlockPos pos, TieredEnergy value, Direction... directions) {
        // TODO: Get cap of block instead of be
        EnergyHandler euStorage = serverLevel.getCapability(IRCapabilities.ENERGY_BLOCK, pos, null);//CapabilityUtils.blockEntityCapability(IRCapabilities.ENERGY_BLOCK, blockEntity);
        if (euStorage != null) {
            Direction[] directions1;
            if (directions.length == 0) {
                directions1 = Direction.values();
            } else {
                directions1 = directions;
            }
            for (Direction direction : directions1) {
                NetworkNode<TieredEnergy> node = this.getNodeAt(serverLevel, pos.relative(direction));
                if (node != null) {
                    EnergyTier cableTier = this.getEnergyTierOfCable(serverLevel, pos.relative(direction));
                    if (cableTier != null) {
                        EnergyTier valueTier = value.tier();
                        int tierDiff = valueTier.order() - cableTier.order();
                        if (valueTier.compareTo(cableTier) > 0 && tierDiff > 1 && value.energy() > 0) {
                            List<NetworkRoute<TieredEnergy>> routes = this.getRouteCache(serverLevel).routes().get(pos);
                            if (routes != null && !routes.isEmpty()) {
                                serverLevel.setBlockAndUpdate(node.getPos(), copyConnections(serverLevel.getBlockState(node.getPos()), IRBlocks.BURNT_CABLE.get().defaultBlockState()
                                        .setValue(BurntCableBlock.BURNT, true)));
                                return value;
                            }
                        }
                    }
                }
            }

            return super.transport(serverLevel, pos, value, directions);
        }
        return value;
    }

    // TODO: Move this to transport lib
    @Override
    public @Nullable NetworkNode<TieredEnergy> findNextNode(@Nullable NetworkNode<TieredEnergy> selfNode, ServerLevel serverLevel, BlockPos pos, Direction direction, Set<BlockPos> ignoredNodes) {
        BlockPos neighborPos = pos.relative(direction);
        if (this.hasInteractorAt(serverLevel, neighborPos)) {
            return null;
        }
        return super.findNextNode(selfNode, serverLevel, pos, direction, ignoredNodes);
    }

    private static BlockState copyConnections(BlockState prevState, BlockState newState) {
        BlockState state0 = newState;
        for (Direction direction : Direction.values()) {
            if (prevState.hasProperty(PipeBlock.CONNECTION[direction.get3DDataValue()])) {
                state0 = state0.setValue(PipeBlock.CONNECTION[direction.get3DDataValue()], prevState.getValue(PipeBlock.CONNECTION[direction.get3DDataValue()]));
            }
        }
        return state0;
    }

    private EnergyTier getEnergyTierOfCable(ServerLevel level, BlockPos pos) {
        return level.getBlockState(pos).getBlock() instanceof CableBlock energyTierBlock ? energyTierBlock.getEnergyTier() : null;
    }

    public static EnergyNetwork build(Builder<TieredEnergy> builder) {
        return new EnergyNetwork(builder);
    }

}
