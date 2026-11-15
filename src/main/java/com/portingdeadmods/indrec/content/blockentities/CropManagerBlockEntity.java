package com.portingdeadmods.indrec.content.blockentities;

import com.portingdeadmods.indrec.IRConfig;
import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;
import com.portingdeadmods.indrec.api.crops.Crop;
import com.portingdeadmods.indrec.api.energy.items.ElectricConsumerItem;
import com.portingdeadmods.indrec.content.blocks.CropBlock;
import com.portingdeadmods.indrec.handlers.energy.EnergyHandlerImpl;
import com.portingdeadmods.indrec.handlers.items.LimitedItemHandler;
import com.portingdeadmods.indrec.registries.IRCrops;
import com.portingdeadmods.indrec.registries.IRMachines;
import com.portingdeadmods.portingdeadlibs.utils.capabilities.HandlerUtils;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.Util;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CropManagerBlockEntity extends MachineBlockEntity {
    public static final int GRID_SLOTS = 9;
    public static final int SIDE_SLOTS = 2;
    public static final int HOE_SLOT = GRID_SLOTS;
    public static final int BONEMEAL_SLOT = GRID_SLOTS + 1;
    public static final int BATTERY_SLOT = GRID_SLOTS + SIDE_SLOTS;

    private final IItemHandler exposedItemHandler;

    public CropManagerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRMachines.CROP_MANAGER, blockPos, blockState);
        this.addMachineEuStorage(EnergyHandlerImpl.NoDrain::new, this::onEuChanged);
        this.addItemHandler(HandlerUtils::newItemStackHandler, builder -> builder
                .slots(BATTERY_SLOT + 1)
                .validator((slot, item) -> {
                    if (slot == HOE_SLOT) return item.is(ItemTags.HOES);
                    if (slot == BONEMEAL_SLOT) return item.is(Items.BONE_MEAL);
                    if (slot < GRID_SLOTS) return false;
                    if (slot == BATTERY_SLOT) return this.isValidChargingItem(item);
                    throw new IllegalArgumentException("Non existent slot " + slot + " on Crop Manager");
                })
                .onChange(this::onItemsChanged));

        this.exposedItemHandler = new LimitedItemHandler(this.getItemHandler(),
                IntSet.of(HOE_SLOT, BONEMEAL_SLOT),
                IntSet.of(0, 1, 2, 3, 4, 5, 6, 7, 8),
                IntSet.of(BATTERY_SLOT));
    }

    @Override
    public IItemHandler getItemHandlerOnSide(Direction direction) {
        return this.exposedItemHandler;
    }

    @Override
    protected void tickRecipe() {
        if (!(this.level instanceof ServerLevel serverLevel)) return;

        long gameTime = serverLevel.getGameTime();
        boolean manageTick = gameTime % Math.max(1, IRConfig.cropManagerInterval) == 0;
        boolean bonemealTick = gameTime % Math.max(1, IRConfig.cropManagerBonemealInterval) == 0;
        if (!manageTick && !bonemealTick) return;

        List<CropBlockEntity> crops = this.collectCrops(serverLevel);
        if (crops.isEmpty()) return;

        if (manageTick) {
            this.removeWeeds(serverLevel, crops);
            this.harvestCrops(serverLevel, crops);
        }
        if (bonemealTick) {
            this.applyBonemeal(serverLevel, crops);
        }
    }

    private List<CropBlockEntity> collectCrops(ServerLevel level) {
        List<CropBlockEntity> crops = new ArrayList<>();
        int range = IRConfig.cropManagerRange;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                if (x == 0 && z == 0) continue;

                pos.setWithOffset(this.worldPosition, x, 0, z);
                if (!level.isLoaded(pos)) continue;
                if (level.getBlockEntity(pos) instanceof CropBlockEntity cropBE && cropBE.getCrop() != null) {
                    crops.add(cropBE);
                }
            }
        }
        return crops;
    }

    private void removeWeeds(ServerLevel level, List<CropBlockEntity> crops) {
        int energy = IRConfig.cropManagerWeedRemovalEnergyUsage;
        for (CropBlockEntity cropBE : crops) {
            if (!isWeed(cropBE.getCrop())) continue;
            if (!this.hasEnergy(energy)) return;

            List<ItemStack> drops = cropBE.createSeedDrop(level.getRandom()).map(List::of).orElse(List.of());
            if (!this.insertOutputs(drops, true)) continue;

            this.insertOutputs(drops, false);
            this.getEuStorage().forceDrainEnergy(energy, false);
            cropBE.removeCrop();
            level.playSound(null, cropBE.getBlockPos(), SoundEvents.CROP_BREAK, SoundSource.BLOCKS);
        }
    }

    private void harvestCrops(ServerLevel level, List<CropBlockEntity> crops) {
        int energy = IRConfig.cropManagerHarvestEnergyUsage;
        IItemHandlerModifiable itemHandler = (IItemHandlerModifiable) this.getItemHandler();
        for (CropBlockEntity cropBE : crops) {
            Holder<Crop> crop = cropBE.getCrop();
            // Weeds can't be harvested and may have just been removed
            if (crop == null || !crop.value().canHarvest() || cropBE.getAge() < cropBE.getMaxAge()) continue;

            ItemStack hoe = itemHandler.getStackInSlot(HOE_SLOT);
            if (!canUseHoe(hoe)) return;
            if (!this.hasEnergy(energy)) return;

            BlockPos cropPos = cropBE.getBlockPos();
            List<ItemStack> drops = CropBlock.getCropLoot(level, crop, cropBE.getAge(), cropBE.getBlockState(), hoe, cropPos, null);
            if (!this.insertOutputs(drops, true)) continue;

            this.insertOutputs(drops, false);
            this.getEuStorage().forceDrainEnergy(energy, false);
            this.useHoe(level, itemHandler, hoe);
            CropBlock.setAgeAndSync(level, cropPos, cropBE, 0);
            level.playSound(null, cropPos, SoundEvents.CROP_BREAK, SoundSource.BLOCKS);
        }
    }

    private void applyBonemeal(ServerLevel level, List<CropBlockEntity> crops) {
        IItemHandlerModifiable itemHandler = (IItemHandlerModifiable) this.getItemHandler();
        if (itemHandler.getStackInSlot(BONEMEAL_SLOT).isEmpty()) return;

        List<CropBlockEntity> targets = new ArrayList<>();
        for (CropBlockEntity cropBE : crops) {
            BlockState state = cropBE.getBlockState();
            if (cropBE.getCrop() != null && !isWeed(cropBE.getCrop())
                    && state.getBlock() instanceof CropBlock cropBlock
                    && cropBlock.isValidBonemealTarget(level, cropBE.getBlockPos(), state)) {
                targets.add(cropBE);
            }
        }

        Util.shuffle(targets, level.getRandom());
        targets.sort(Comparator.comparingDouble(cropBE -> (double) cropBE.getAge() / Math.max(1, cropBE.getMaxAge())));

        int energy = IRConfig.cropManagerBonemealEnergyUsage;
        for (CropBlockEntity cropBE : targets) {
            if (itemHandler.getStackInSlot(BONEMEAL_SLOT).isEmpty()) return;
            if (!this.hasEnergy(energy)) return;

            BlockPos cropPos = cropBE.getBlockPos();
            BlockState state = cropBE.getBlockState();
            CropBlock cropBlock = (CropBlock) state.getBlock();
            this.getEuStorage().forceDrainEnergy(energy, false);
            itemHandler.extractItem(BONEMEAL_SLOT, 1, false);
            if (cropBlock.isBonemealSuccess(level, level.getRandom(), cropPos, state)) {
                cropBlock.performBonemeal(level, level.getRandom(), cropPos, state);
            }
            level.levelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, cropPos, 15);
        }
    }

    private static boolean isWeed(Holder<Crop> crop) {
        return crop.is(IRCrops.WEED);
    }

    private static boolean canUseHoe(ItemStack hoe) {
        if (hoe.isEmpty() || !hoe.is(ItemTags.HOES)) return false;
        return !(hoe.getItem() instanceof ElectricConsumerItem consumer) || consumer.canWork(hoe);
    }

    private void useHoe(ServerLevel level, IItemHandlerModifiable itemHandler, ItemStack hoe) {
        if (hoe.getItem() instanceof ElectricConsumerItem consumer) {
            consumer.consumeEnergy(hoe);
        } else {
            hoe.hurtAndBreak(1, level, null, item -> {});
        }

        itemHandler.setStackInSlot(HOE_SLOT, hoe.isEmpty() ? ItemStack.EMPTY : hoe);
    }

    private boolean hasEnergy(int energy) {
        return this.getEuStorage().forceDrainEnergy(energy, true) == energy;
    }

    private boolean insertOutputs(List<ItemStack> stacks, boolean simulate) {
        IItemHandlerModifiable itemHandler = (IItemHandlerModifiable) this.getItemHandler();
        ItemStack[] slots = new ItemStack[GRID_SLOTS];
        for (int i = 0; i < GRID_SLOTS; i++) {
            slots[i] = itemHandler.getStackInSlot(i).copy();
        }

        for (ItemStack stack : stacks) {
            ItemStack remaining = stack.copy();
            // Fill up matching stacks first, then empty slots
            for (int i = 0; i < GRID_SLOTS && !remaining.isEmpty(); i++) {
                if (slots[i].isEmpty() || !ItemStack.isSameItemSameComponents(slots[i], remaining)) continue;

                int limit = Math.min(itemHandler.getSlotLimit(i), slots[i].getMaxStackSize());
                int moved = Math.min(remaining.getCount(), limit - slots[i].getCount());
                if (moved > 0) {
                    slots[i].grow(moved);
                    remaining.shrink(moved);
                }
            }
            for (int i = 0; i < GRID_SLOTS && !remaining.isEmpty(); i++) {
                if (!slots[i].isEmpty()) continue;

                int moved = Math.min(remaining.getCount(), Math.min(itemHandler.getSlotLimit(i), remaining.getMaxStackSize()));
                slots[i] = remaining.split(moved);
            }
            if (!remaining.isEmpty()) return false;
        }

        if (!simulate) {
            for (int i = 0; i < GRID_SLOTS; i++) {
                if (!ItemStack.matches(slots[i], itemHandler.getStackInSlot(i))) {
                    itemHandler.setStackInSlot(i, slots[i]);
                }
            }
        }
        return true;
    }
}
