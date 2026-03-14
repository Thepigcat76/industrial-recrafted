package com.portingdeadmods.indrec.api.blocks;

import com.mojang.serialization.MapCodec;
import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;
import com.portingdeadmods.indrec.api.energy.EnergyTier;
import com.portingdeadmods.indrec.api.energy.blocks.EnergyTierBlock;
import com.portingdeadmods.indrec.registries.IRBlocks;
import com.portingdeadmods.indrec.registries.IRItems;
import com.portingdeadmods.indrec.utils.TooltipUtils;
import com.portingdeadmods.indrec.utils.machines.IRMachine;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.blocks.ContainerBlock;
import com.portingdeadmods.portingdeadlibs.api.utils.PDLBlockStateProperties;
import com.portingdeadmods.portingdeadlibs.utils.BlockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class MachineBlock extends ContainerBlock implements EnergyTierBlock {
    private final Supplier<? extends EnergyTier> energyTier;
    private BlockEntityType<? extends MachineBlockEntity> blockEntityType;
    private final boolean ticking;
    private final boolean rotatableHorizontal;
    private final boolean rotatable;
    private final boolean activatable;

    public MachineBlock(String name, Builder builder, Supplier<? extends EnergyTier> energyTier) {
        super(builder.properties);
        this.ticking = builder.ticking;
        this.rotatableHorizontal = builder.rotatableHorizontal;
        this.rotatable = builder.rotatable;
        this.activatable = builder.activatable;
        this.energyTier = energyTier;

        BlockState defaultState = this.defaultBlockState();

        if (this.rotatable) {
            defaultState = defaultState.setValue(BlockStateProperties.FACING, Direction.NORTH);
        }

        if (this.rotatableHorizontal) {
            defaultState = defaultState.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH);
        }

        if (this.activatable) {
            defaultState = defaultState.setValue(PDLBlockStateProperties.ACTIVE, false);
        }
        registerDefaultState(defaultState);
    }

    public static boolean isActive(BlockState state) {
        return state.hasProperty(PDLBlockStateProperties.ACTIVE) && state.getValue(PDLBlockStateProperties.ACTIVE);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        MachineBlockEntity be = BlockUtils.getBE(MachineBlockEntity.class, level, pos);

        if (be != null) {
            double x = pos.getX();
            double y = pos.getY();
            double z = pos.getZ();

            if (be.isBurnt()) {
                if (random.nextInt(8) == 0) {
                    level.playLocalSound(x + 0.5D, y + 0.5D, z + 0.5D, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
                }

                for (int i = 0; i < 5; i++) {
                    level.addParticle(ParticleTypes.SMOKE, x + random.nextFloat(), y + 1D, z + random.nextFloat(), 0D, 0D, 0D);

                    level.addParticle(ParticleTypes.SMOKE, x, y + 0.05D + random.nextFloat(), z + random.nextFloat(), 0D, 0D, 0D);
                    level.addParticle(ParticleTypes.SMOKE, x + 1D, y + 0.05D + random.nextFloat(), z + random.nextFloat(), 0D, 0D, 0D);
                    level.addParticle(ParticleTypes.SMOKE, x + random.nextFloat(), y + 0.05D + random.nextFloat(), z, 0D, 0D, 0D);
                    level.addParticle(ParticleTypes.SMOKE, x + random.nextFloat(), y + 0.05D + random.nextFloat(), z + 1D, 0D, 0D, 0D);

                    if (random.nextInt(8) == 0) {
                        level.addParticle(ParticleTypes.FLAME, x, y + 0.05D + random.nextFloat(), z + random.nextFloat(), 0D, 0D, 0D);
                    }

                    if (random.nextInt(8) == 0) {
                        level.addParticle(ParticleTypes.FLAME, x + 1D, y + 0.05D + random.nextFloat(), z + random.nextFloat(), 0D, 0D, 0D);
                    }

                    if (random.nextInt(8) == 0) {
                        level.addParticle(ParticleTypes.FLAME, x + random.nextFloat(), y + 0.05D + random.nextFloat(), z, 0D, 0D, 0D);
                    }

                    if (random.nextInt(8) == 0) {
                        level.addParticle(ParticleTypes.FLAME, x + random.nextFloat(), y + 0.05D + random.nextFloat(), z + 1D, 0D, 0D, 0D);
                    }
                }

                level.addParticle(ParticleTypes.FLAME, x + random.nextFloat(), y + 1.1D, z + random.nextFloat(), 0D, 0D, 0D);
                level.addParticle(ParticleTypes.LARGE_SMOKE, x + 0.5D, y + 1D, z + 0.5D, 0D, 0D, 0D);
            }
        }

    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        MachineBlockEntity be = BlockUtils.getBE(MachineBlockEntity.class, level, pos);
        if (be != null && be.isBurnt() && stack.is(IRItems.FUSE)) {
            stack.shrink(1);
            be.setBurnt(false);
            level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 0.5f, 0.75f);
            return ItemInteractionResult.SUCCESS;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        MachineBlockEntity be = BlockUtils.getBE(MachineBlockEntity.class, level, pos);
        if (be != null && !be.isBurnt()) {
            return super.useWithoutItem(state, level, pos, player, hitResult);
        }
        return InteractionResult.FAIL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        Builder builder1 = IRMachine.MACHINE_BLOCK_BUILDER.get();

        if (builder1.rotatable) {
            builder.add(BlockStateProperties.FACING);
        }

        if (builder1.rotatableHorizontal) {
            builder.add(BlockStateProperties.HORIZONTAL_FACING);
        }

        if (builder1.activatable) {
            builder.add(PDLBlockStateProperties.ACTIVE);
        }

        super.createBlockStateDefinition(builder);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state != null) {
            if (this.rotatableHorizontal) {
                return state.setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
            } else if (this.rotatable) {
                return state.setValue(BlockStateProperties.FACING, context.getNearestLookingDirection().getOpposite());
            }
        }
        return null;
    }

    @Override
    public boolean tickingEnabled() {
        return this.ticking;
    }

    @Override
    public BlockEntityType<? extends ContainerBlockEntity> getBlockEntityType() {
        if (this.blockEntityType == null) {
            this.blockEntityType = IRMachine.BLOCK_ENTITY_TYPES.get(BuiltInRegistries.BLOCK.getKey(this)).get();
        }
        return this.blockEntityType;
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);

        BlockUtils.getBE(MachineBlockEntity.class, level, pos).initCapCache();
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        if (this.activatable && isActive(state)) {
            return 7;
        }
        return super.getLightEmission(state, level, pos);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public @Nullable EnergyTier getEnergyTier() {
        return this.energyTier.get();
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        TooltipUtils.addEnergyTierTooltip(tooltipComponents, this.getEnergyTier());

    }

    public static class Builder {
        private BlockBehaviour.Properties properties = IRBlocks.MACHINE_FRAME_PROPS;
        private boolean ticking;
        private boolean activatable;
        private boolean rotatableHorizontal;
        private boolean rotatable;

        private Builder() {
        }

        public Builder ticking() {
            this.ticking = true;
            return this;
        }

        public Builder properties(BlockBehaviour.Properties properties) {
            this.properties = properties;
            return this;
        }

        public Builder rotatableHorizontal() {
            this.rotatableHorizontal = true;
            return this;
        }

        public Builder rotatable() {
            this.rotatable = true;
            return this;
        }

        public Builder activatable() {
            this.activatable = true;
            return this;
        }

    }

}
