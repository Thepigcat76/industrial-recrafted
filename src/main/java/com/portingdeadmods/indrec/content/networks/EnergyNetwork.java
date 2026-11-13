package com.portingdeadmods.indrec.content.networks;

import com.portingdeadmods.indrec.IRCapabilities;
import com.portingdeadmods.indrec.api.blocks.PipeBlock;
import com.portingdeadmods.indrec.api.energy.EnergyHandler;
import com.portingdeadmods.indrec.api.energy.EnergyTier;
import com.portingdeadmods.indrec.api.energy.TieredEnergy;
import com.portingdeadmods.indrec.content.blocks.BurntCableBlock;
import com.portingdeadmods.indrec.content.blocks.CableBlock;
import com.portingdeadmods.indrec.registries.IRBlocks;
import com.portingdeadmods.indrec.utils.BlockUtils;
import com.thepigcat.transportlib.api.NetworkNode;
import com.thepigcat.transportlib.api.cache.NetworkRoute;
import com.thepigcat.transportlib.impl.NetworkNodeImpl;
import com.thepigcat.transportlib.impl.TransportNetworkImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class EnergyNetwork extends TransportNetworkImpl<TieredEnergy> {
    protected EnergyNetwork(Builder<TieredEnergy> builder) {
        super(builder);
    }

    // Routes are cached per position only, so we remember which directions they were computed for.
    // Senders like transformers transport into different directions, which need different routes
    private final Map<ServerLevel, Map<BlockPos, Set<Direction>>> routeDirections = new WeakHashMap<>();

    @Override
    public TieredEnergy transport(ServerLevel serverLevel, BlockPos pos, TieredEnergy value, Direction... directions) {
        Direction[] directions1;
        if (directions.length == 0) {
            directions1 = Direction.values();
        } else {
            directions1 = directions;
        }

        // Sided handlers (e.g. transformers) only expose a handler on some sides, so check the sides we send through.
        // The capability's side is the direction from the neighbor towards this block
        EnergyHandler euStorage = null;
        for (Direction direction : directions1) {
            euStorage = serverLevel.getCapability(IRCapabilities.ENERGY_BLOCK, pos, direction.getOpposite());
            if (euStorage != null) break;
        }
        if (euStorage != null) {
            this.invalidateRoutesIfDirectionsChanged(serverLevel, pos, directions1);
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
                                BlockUtils.playBurnSound(serverLevel, node.getPos());
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

    // TODO: Move the route handling below to transport lib, it only invalidates routes when nodes get removed
    @Override
    public void addNode(NetworkNode<TieredEnergy> node, ServerLevel level, BlockPos pos, Direction[] connections, boolean dead) {
        super.addNode(node, level, pos, connections, dead);
        this.invalidateRoutes(level);
    }

    @Override
    public void connect(ServerLevel level, BlockPos pos, Direction direction0, Direction direction1) {
        super.connect(level, pos, direction0, direction1);
        this.invalidateRoutes(level);
    }

    @Override
    public void removeInteractor(ServerLevel level, BlockPos interactorPos) {
        super.removeInteractor(level, interactorPos);

        // Nodes keep their connection otherwise, and the new routes would still lead to the removed interactor.
        // The interactor can also still be there, if only one of the cables connected to it got removed
        for (Direction direction : Direction.values()) {
            NetworkNode<TieredEnergy> node = this.getNodeAt(level, interactorPos.relative(direction));
            Direction nodeDirection = direction.getOpposite();
            if (node instanceof NetworkNodeImpl<TieredEnergy> nodeImpl && !this.checkForInteractorAt(level, node.getPos(), nodeDirection)) {
                nodeImpl.getInteractorConnections().remove(nodeDirection);
            }
        }
        this.invalidateRoutes(level);
    }

    private void invalidateRoutesIfDirectionsChanged(ServerLevel level, BlockPos pos, Direction[] directions) {
        Set<Direction> directionSet = EnumSet.noneOf(Direction.class);
        directionSet.addAll(Arrays.asList(directions));
        Set<Direction> previous = this.routeDirections.computeIfAbsent(level, k -> new HashMap<>()).put(pos.immutable(), directionSet);
        if (previous != null && !previous.equals(directionSet)) {
            List<NetworkRoute<TieredEnergy>> routes = this.getRouteCache(level).routes().get(pos);
            if (routes != null) {
                for (NetworkRoute<TieredEnergy> route : routes) {
                    route.setValid(false);
                }
            }
        }
    }

    private void invalidateRoutes(ServerLevel level) {
        for (List<NetworkRoute<TieredEnergy>> routes : this.getRouteCache(level).routes().values()) {
            for (NetworkRoute<TieredEnergy> route : routes) {
                route.setValid(false);
            }
        }
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
