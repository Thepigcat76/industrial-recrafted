package com.portingdeadmods.indrec.content.blocks;

import com.mojang.serialization.MapCodec;
import com.portingdeadmods.indrec.IRRegistries;
import com.portingdeadmods.indrec.api.crops.Crop;
import com.portingdeadmods.indrec.content.blockentities.CropBlockEntity;
import com.portingdeadmods.indrec.registries.IRBlockEntityTypes;
import com.portingdeadmods.portingdeadlibs.utils.BlockUtils;
import net.minecraft.core.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.util.TriState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.stream.Stream;

public class CropBlock extends BaseEntityBlock {
    public CropBlock(Properties properties) {
        super(properties.randomTicks());
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(CropBlock::new);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getBlock() instanceof FarmBlock;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState belowBlockState = level.getBlockState(blockpos);
        TriState soilDecision = belowBlockState.canSustainPlant(level, blockpos, Direction.UP, state);
        if (!soilDecision.isDefault()) {
            return soilDecision.isTrue();
        } else {
            return net.minecraft.world.level.block.CropBlock.hasSufficientLight(level, pos) && this.mayPlaceOn(belowBlockState, level, blockpos);
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return !state.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return state.getFluidState().isEmpty();
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return pathComputationType == PathComputationType.AIR && !this.hasCollision || super.isPathfindable(state, pathComputationType);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!stack.isEmpty()) {
            Optional<HolderLookup.RegistryLookup<Crop>> registryAccess = level.registryAccess().lookup(IRRegistries.CROP);
            if (registryAccess.isPresent()) {
                Stream<Holder.Reference<Crop>> elements = registryAccess.get().listElements();
                Holder.Reference<Crop> crop = elements.filter(holder -> stack.is(holder.value().seedItem().orElse(Items.AIR)))
                        .findFirst()
                        .orElse(null);
                if (crop != null) {
                    CropBlockEntity cropBE = BlockUtils.getBE(CropBlockEntity.class, level, pos);
                    cropBE.plant(crop);
                    return ItemInteractionResult.SUCCESS;
                }
            }
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

//    @Override
//    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
//        if (level.isAreaLoaded(pos, 1)) {
//            if (level.getRawBrightness(pos, 0) >= 9) {
//                CropBlockEntity cropBE = BlockUtils.getBE(CropBlockEntity.class, level, pos);
//                int i = cropBE.getAge();
//                if (i < cropBE.getMaxAge()) {
//                    float f = getGrowthSpeed(state, level, pos);
//                    if (CommonHooks.canCropGrow(level, pos, state, random.nextInt((int)(25.0F / f) + 1) == 0)) {
//                        level.setBlock(pos, this.getStateForAge(i + 1), 2);
//                        CommonHooks.fireCropGrowPost(level, pos, state);
//                    }
//                }
//            }
//
//        }
//    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return IRBlockEntityTypes.CROP.get().create(blockPos, blockState);
    }
}
