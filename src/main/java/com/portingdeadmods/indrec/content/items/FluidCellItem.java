package com.portingdeadmods.indrec.content.items;

import com.portingdeadmods.indrec.IRConfig;
import com.portingdeadmods.indrec.api.fluid.SimpleFluidItem;
import com.portingdeadmods.indrec.registries.IRItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.IntSupplier;

public class FluidCellItem extends SimpleFluidItem {
    private final IntSupplier capacity;

    public FluidCellItem(Properties properties, IntSupplier capacity) {
        super(properties);
        this.capacity = capacity;
    }

    public static FluidCellItem defaultItem(Properties properties) {
        return new FluidCellItem(properties, () -> 1000);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);

        if (hit.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(stack);
        } else if (hit.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(stack);
        }

        BlockPos pos = hit.getBlockPos();
        Direction direction = hit.getDirection();
        BlockPos pos1 = pos.relative(direction);

        if (getFluid(stack).isEmpty()) {
            if (level.mayInteract(player, pos) && player.mayUseItemAt(pos1, direction, stack)) {
                BlockState state = level.getBlockState(pos);

                if (state.getBlock() instanceof BucketPickup bucketPickup) {
                    ItemStack fluidItem = bucketPickup.pickupBlock(player, level, pos, state);

                    if (fluidItem.getItem() instanceof BucketItem bucketItem && bucketItem.content != Fluids.EMPTY) {
                        player.awardStat(Stats.ITEM_USED.get(this));
                        bucketPickup.getPickupSound(state).ifPresent(soundevent -> player.playSound(soundevent, 1F, 1F));

                        ItemStack itemstack1 = ItemUtils.createFilledResult(stack, player, withFluid(new ItemStack(this), new FluidStack(bucketItem.content, 1000)));

                        if (!level.isClientSide()) {
                            CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, fluidItem);
                        }

                        return InteractionResultHolder.sidedSuccess(itemstack1, level.isClientSide());
                    }
                }
            }
        } else {
            BlockState blockstate = level.getBlockState(pos);
            BlockPos blockpos2 = canBlockContainFluid(player, level, pos, blockstate, getFluid(stack).getFluid()) ? pos : pos1;
            if (this.emptyContents(player, level, blockpos2, hit, stack, getFluid(stack).getFluid())) {
                if (player instanceof ServerPlayer) {
                    CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, blockpos2, stack);
                }

                player.awardStat(Stats.ITEM_USED.get(this));
                ItemStack itemstack1 = ItemUtils.createFilledResult(stack, player, getEmptySuccessItem(stack, player));
                return InteractionResultHolder.sidedSuccess(itemstack1, level.isClientSide());
            } else {
                return InteractionResultHolder.fail(stack);
            }
        }

        return InteractionResultHolder.fail(stack);
    }

    public static ItemStack getEmptySuccessItem(ItemStack fluidCellStack, Player player) {
        return !player.hasInfiniteMaterials() ? IRItems.FLUID_CELL.toStack() : fluidCellStack;
    }

    public static boolean canBlockContainFluid(@Nullable Player player, Level worldIn, BlockPos posIn, BlockState blockstate, Fluid fluid) {
        return blockstate.getBlock() instanceof LiquidBlockContainer && ((LiquidBlockContainer)blockstate.getBlock()).canPlaceLiquid(player, worldIn, posIn, blockstate, fluid);
    }


    public boolean emptyContents(@Nullable Player player, Level level, BlockPos pos, @Nullable BlockHitResult result, @Nullable ItemStack container, Fluid fluid) {
        Block var17;
        if (!(fluid instanceof FlowingFluid flowingfluid)) {
            return false;
        } else {
            boolean $$8;
            BlockState blockstate;
            boolean flag2;
            label78: {
                label77: {
                    blockstate = level.getBlockState(pos);
                    var17 = blockstate.getBlock();
                    $$8 = blockstate.canBeReplaced(fluid);
                    if (!blockstate.isAir() && !$$8) {
                        if (!(var17 instanceof LiquidBlockContainer liquidBlockContainer)) {
                            break label77;
                        }

                        if (!liquidBlockContainer.canPlaceLiquid(player, level, pos, blockstate, fluid)) {
                            break label77;
                        }
                    }

                    flag2 = true;
                    break label78;
                }

                flag2 = false;
            }

            Optional<FluidStack> containedFluidStack = Optional.ofNullable(container).flatMap(FluidUtil::getFluidContained);
            if (!flag2) {
                return result != null && this.emptyContents(player, level, result.getBlockPos().relative(result.getDirection()), null, container, fluid);
            } else if (containedFluidStack.isPresent() && fluid.getFluidType().isVaporizedOnPlacement(level, pos, containedFluidStack.get())) {
                fluid.getFluidType().onVaporize(player, level, pos, containedFluidStack.get());
                return true;
            } else if (level.dimensionType().ultraWarm() && fluid.is(FluidTags.WATER)) {
                int l = pos.getX();
                int i = pos.getY();
                int j = pos.getZ();
                level.playSound(player, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (level.random.nextFloat() - level.random.nextFloat()) * 0.8F);

                for(int k = 0; k < 8; ++k) {
                    level.addParticle(ParticleTypes.LARGE_SMOKE, (double)l + Math.random(), (double)i + Math.random(), (double)j + Math.random(), (double)0.0F, (double)0.0F, (double)0.0F);
                }

                return true;
            } else {
                if (var17 instanceof LiquidBlockContainer) {
                    LiquidBlockContainer liquidblockcontainer1 = (LiquidBlockContainer)var17;
                    if (liquidblockcontainer1.canPlaceLiquid(player, level, pos, blockstate, fluid)) {
                        liquidblockcontainer1.placeLiquid(level, pos, blockstate, flowingfluid.getSource(false));
                        this.playEmptySound(player, level, pos, fluid);
                        return true;
                    }
                }

                if (!level.isClientSide && $$8 && !blockstate.liquid()) {
                    level.destroyBlock(pos, true);
                }

                if (!level.setBlock(pos, fluid.defaultFluidState().createLegacyBlock(), 11) && !blockstate.getFluidState().isSource()) {
                    return false;
                } else {
                    this.playEmptySound(player, level, pos, fluid);
                    return true;
                }
            }
        }
    }

    protected void playEmptySound(@Nullable Player player, LevelAccessor level, BlockPos pos, Fluid fluid) {
        SoundEvent soundevent = fluid.getFluidType().getSound(player, level, pos, SoundActions.BUCKET_EMPTY);
        if (soundevent == null) {
            soundevent = fluid.is(FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY;
        }

        level.playSound(player, pos, soundevent, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(player, GameEvent.FLUID_PLACE, pos);
    }

    @Override
    public int getFluidCapacity() {
        return this.capacity.getAsInt();
    }

}