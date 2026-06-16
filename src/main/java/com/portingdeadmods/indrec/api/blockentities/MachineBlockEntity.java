package com.portingdeadmods.indrec.api.blockentities;

import com.mojang.datafixers.util.Pair;
import com.portingdeadmods.indrec.IRCapabilities;
import com.portingdeadmods.indrec.api.blocks.MachineBlock;
import com.portingdeadmods.indrec.api.capabilities.StorageChangedListener;
import com.portingdeadmods.indrec.api.energy.EnergyHandler;
import com.portingdeadmods.indrec.api.energy.EnergyTier;
import com.portingdeadmods.indrec.content.menus.ChargingSlot;
import com.portingdeadmods.indrec.content.recipes.MachineRecipeImpl;
import com.portingdeadmods.indrec.content.recipes.MachineRecipeInput;
import com.portingdeadmods.indrec.api.recipes.MachineRecipeLayout;
import com.portingdeadmods.indrec.content.recipes.components.TimeComponent;
import com.portingdeadmods.indrec.content.recipes.components.energy.EnergyInputComponent;
import com.portingdeadmods.indrec.content.recipes.flags.FluidInputComponentFlag;
import com.portingdeadmods.indrec.content.recipes.flags.FluidOutputComponentFlag;
import com.portingdeadmods.indrec.content.recipes.flags.ItemInputComponentFlag;
import com.portingdeadmods.indrec.content.recipes.flags.ItemOutputComponentFlag;
import com.portingdeadmods.indrec.networking.clientbound.SetEnergyPayload;
import com.portingdeadmods.indrec.registries.IRRecipeComponentFlags;
import com.portingdeadmods.indrec.registries.IRSoundEvents;
import com.portingdeadmods.indrec.utils.machines.IRMachine;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.blockentities.RedstoneBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.utils.PDLBlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

// TODO: Secondary chance outputs are not working
// FIXME: When rejoining world, recipe doesn't load
public class MachineBlockEntity extends ContainerBlockEntity implements RedstoneBlockEntity, WrenchListenerBlockEntity {
    private final List<ChargingSlot> chargingSlots;
    private final List<BlockCapabilityCache<EnergyHandler, Direction>> euCaches;
    protected final IRMachine machine;
    private RedstoneSignalType redstoneSignalType = RedstoneSignalType.IGNORED;
    private int redstoneSignalStrength;
    protected MachineRecipeImpl cachedRecipe;
    protected float progress;
    protected float progressIncrement;
    protected boolean burnt;
    private boolean removedByWrench;
    protected Set<Direction> spreadDirections;
    private boolean syncEnergyThisTick;

    public MachineBlockEntity(IRMachine machine, BlockPos blockPos, BlockState blockState) {
        super(machine.getBlockEntityType(), blockPos, blockState);
        this.machine = machine;
        this.euCaches = new ArrayList<>();
        this.chargingSlots = new ArrayList<>();
        this.progressIncrement = 1F;
        this.spreadDirections = Set.of(Direction.values());
    }

    public MachineRecipeLayout<?> getRecipeLayout() {
        return this.machine.getRecipeLayout();
    }

    public MachineRecipeImpl getCachedRecipe() {
        return cachedRecipe;
    }

    @Override
    public void tick() {
        this.syncEnergyThisTick = false;

        super.tick();

        for (ChargingSlot chargingSlot : this.chargingSlots) {
            if (chargingSlot.getItem().isEmpty()) continue;

            this.tickChargingSlot(chargingSlot);
        }

        this.tickEnergySpreading();

        this.tickRecipe();

        if (this.syncEnergyThisTick) {
            PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) level, new ChunkPos(this.worldPosition), new SetEnergyPayload(this.worldPosition, this.getEuStorage().getEnergyStored()));
        }

    }

    public Set<Direction> getSpreadDirections() {
        return spreadDirections;
    }

    protected void tickEnergySpreading() {
        if (this.shouldSpreadEnergy() && !level.isClientSide()) {
            int amountPerBlock = this.getAmountPerBlock();

            for (BlockCapabilityCache<EnergyHandler, Direction> cache : this.euCaches) {
                if (this.getSpreadDirections().contains(cache.context())) {
                    EnergyHandler euHandler = cache.getCapability();
                    if (euHandler != null) {
                        int filled = euHandler.fillEnergy(amountPerBlock, false);
                        if (filled > 0) {
                            this.syncEnergyThisTick = true;
                        }
                        getEuStorage().forceDrainEnergy(filled, false);
                        PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) level, new ChunkPos(this.worldPosition.relative(cache.context())), new SetEnergyPayload(this.worldPosition.relative(cache.context()), euHandler.getEnergyStored()));
                        //level.sendBlockUpdated(getBlockPos().relative(cache.context()), getBlockState(), getBlockState(), 3);
                    }

                }
            }

        }
    }

    public boolean isBurnt() {
        return burnt;
    }

    public void setBurnt(boolean burnt) {
        this.burnt = burnt;
        this.updateData();
    }

    protected int getResultSlot() {
        return 1;
    }

    protected int getResultTank() {
        return 0;
    }

    protected @NotNull MachineRecipeInput createRecipeInput() {
        if (this.getItemHandler() == null) return new MachineRecipeInput();
        return new MachineRecipeInput(List.of(this.getItemHandler().getStackInSlot(0)));
    }

    protected void onItemsChanged(int slot) {
        //this.updateData();

        setChanged();
        this.refreshCachedRecipe();
    }

    protected void onFluidsChanged(int tank) {
        //this.updateData();

        setChanged();
        this.refreshCachedRecipe();
    }

    protected void onEuChanged(int oldAmount) {
        //this.updateData();

        setChanged();
        this.refreshCachedRecipe();
    }

    protected void playMachineSound() {
        if (this.progress % 12 == 0) {
            this.level.playSound(null, worldPosition, IRSoundEvents.MACHINE.get(), SoundSource.BLOCKS, 0.15f, 1f);
        }
    }

    protected void tickRecipe() {
        //if (!this.level.isClientSide()) {
        if (this.cachedRecipe != null) {
            if (this.hasProgressFinished()) {
                this.progress = 0;
                RegistryAccess provider = this.level.registryAccess();
                ItemStack resultItem = this.cachedRecipe.assemble(this.createRecipeInput(), provider);
                FluidStack resultFluid = this.cachedRecipe.assembleResultFluid(this.createRecipeInput(), provider);

                int resultEnergy = this.cachedRecipe.assembleResultEnergy(this.createRecipeInput(), provider);
                ItemOutputComponentFlag itemOutput = this.cachedRecipe.getComponentByFlag(IRRecipeComponentFlags.ITEM_OUTPUT);
                FluidOutputComponentFlag fluidOutput = this.cachedRecipe.getComponentByFlag(IRRecipeComponentFlags.FLUID_OUTPUT);

                ItemInputComponentFlag itemInput = this.cachedRecipe.getComponentByFlag(IRRecipeComponentFlags.ITEM_INPUT);
                FluidInputComponentFlag fluidInput = this.cachedRecipe.getComponentByFlag(IRRecipeComponentFlags.FLUID_INPUT);

                boolean hasResultEnergy = this.cachedRecipe.hasResultEnergy(provider);
                boolean hasResultItem = this.cachedRecipe.hasResultItem(provider);
                boolean hasResultFluid = this.cachedRecipe.hasResultFluid(provider);

                if (hasResultEnergy) {
                    this.getEuStorage().forceFillEnergy(resultEnergy, false);
                }

                if (!level.isClientSide()) {
                    if (hasResultItem && itemOutput.isOutputted(this.level.random, 0)) {
                        List<ItemStack> outputs = itemOutput.getOutputs();
                        for (int i = 0; i < outputs.size(); i++) {
                            ItemStack output = outputs.get(i);
                            if (itemOutput.isOutputted(this.level.random, i)) {
                                this.forceInsertItem((IItemHandlerModifiable) this.getItemHandler(), this.getResultSlot() + i, i == 0 ? resultItem.copy() : output.copy(), false, this::onItemsChanged);
                            }
                        }
                        this.updateData();
                    }
                }

                if (hasResultFluid && fluidOutput.isOutputted(this.level.random, 0)) {
                    this.forceFillTank(this.getFluidHandler(), resultFluid.copy(), IFluidHandler.FluidAction.EXECUTE, this::onFluidsChanged);
                }

                if (itemInput != null && !itemInput.getIngredients().isEmpty()) {
                    for (int i = 0; i < itemInput.getIngredients().size(); i++) {
                        this.getItemHandler().extractItem(i, itemInput.getIngredients().get(i).count(), false);
                    }
                }

                if (fluidInput != null && !fluidInput.getIngredients().isEmpty()) {
                    this.getFluidHandler().drain(fluidInput.getIngredients().getFirst().amount(), IFluidHandler.FluidAction.EXECUTE);
                }

                this.onRecipeComplete();

                if (this.cachedRecipe == null) {
                    setActive(false);
                }
            } else {
                boolean canRun = false;

                EnergyInputComponent inputComponent = this.cachedRecipe.getComponent(EnergyInputComponent.TYPE);
                if (inputComponent != null) {
                    int energy = inputComponent.energy() / this.cachedRecipe.getComponent(TimeComponent.TYPE).time();
                    int drained = this.getEuStorage().forceDrainEnergy(energy, false);

                    if (drained == energy) {
                        canRun = true;
                    }
                } else {
                    canRun = true;
                }


                if (canRun) {
                    this.progress += this.progressIncrement;
                    setActive(true);
                    this.playMachineSound();
                } else {
                    this.progress = 0;
                    setChanged();
                    setActive(false);
                }

            }
        } else {
            this.progress = 0;
            setChanged();
            setActive(false);
        }

        //}
    }

    public void setActive(boolean active) {
        if (MachineBlock.isActive(getBlockState()) != active) {
            level.setBlockAndUpdate(worldPosition, getBlockState().setValue(PDLBlockStateProperties.ACTIVE, active));
        }
    }

    protected void onRecipeComplete() {
    }

    protected boolean hasProgressFinished() {
        return this.cachedRecipe != null && (this.getProgress() >= this.getMaxProgress() || !this.cachedRecipe.hasProgress());
    }

    protected int getAmountPerBlock() {
        int blocks = 0;
        for (BlockCapabilityCache<EnergyHandler, Direction> cache : this.euCaches) {
            if (cache.getCapability() != null) {
                blocks++;
            }
        }
        int amountPerBlock;
        if (getEuStorage().getEnergyStored() >= getEuStorage().getMaxOutput() * blocks) {
            amountPerBlock = getEuStorage().getMaxOutput();
        } else {
            amountPerBlock = getEuStorage().getEnergyStored() / blocks;
        }
        return amountPerBlock;
    }

    public void addChargingSlot(ChargingSlot chargingSlot) {
        this.chargingSlots.add(chargingSlot);
    }

    protected void tickChargingSlot(ChargingSlot slot) {
        if (!level.isClientSide()) {
            ItemStack itemStack = slot.getItem();
            EnergyHandler energyStorage = this.getEuStorage();
            EnergyHandler itemEnergyStorage = itemStack.getCapability(IRCapabilities.ENERGY_ITEM);
            if (itemEnergyStorage != null) {
                if (slot.getMode() == ChargingSlot.ChargeMode.CHARGE) {
                    int filled = itemEnergyStorage.fillEnergy(Math.min(itemEnergyStorage.getMaxInput(), energyStorage.getMaxOutput()), true);
                    int drained = energyStorage.forceDrainEnergy(filled, true);
                    int newFilled = itemEnergyStorage.fillEnergy(drained, false);
                    energyStorage.forceDrainEnergy(newFilled, false);

                    if (newFilled > 0) {
                        this.syncEnergyThisTick = true;
                    }
                } else {
                    int drained = itemEnergyStorage.drainEnergy(Math.min(itemEnergyStorage.getMaxOutput(), energyStorage.getMaxInput()), true);
                    int filled = energyStorage.fillEnergy(drained, true);
                    int newDrained = itemEnergyStorage.drainEnergy(filled, false);
                    energyStorage.fillEnergy(newDrained, false);

                    if (newDrained > 0) {
                        this.syncEnergyThisTick = true;
                    }

                }
            }
        }
    }

    public int getProgress() {
        return (int) this.progress;
    }

    public int getMaxProgress() {
        return this.cachedRecipe != null && this.cachedRecipe.hasProgress() ? this.cachedRecipe.getComponent(TimeComponent.TYPE).time() : 0;
    }

    public boolean shouldSpreadEnergy() {
        return false;
    }

    protected <H extends EnergyHandler> H addEuStorage(H handler) {
        return this.addHandler(IRCapabilities.ENERGY_BLOCK.name(), handler);
    }

    protected final <T extends EnergyHandler & StorageChangedListener> void addMachineEuStorage(Function<Supplier<? extends EnergyTier>, T> energyHandlerConstructor, Consumer<Integer> onChangedFunction) {
        this.addEuStorage(energyHandlerConstructor, this.machine::getEnergyTier, this.machine.getEnergyCapacity(), onChangedFunction);
    }

    protected final <T extends EnergyHandler & StorageChangedListener> void addEuStorage(Function<Supplier<? extends EnergyTier>, T> energyHandlerConstructor, Supplier<? extends EnergyTier> energyTier, int energyCapacity, Consumer<Integer> onChangedFunction) {
        T storage = energyHandlerConstructor.apply(energyTier);
        storage.setOnChangedFunction(onChangedFunction);
        storage.setEnergyCapacity(energyCapacity);
        this.addEuStorage(storage);
    }

    public EnergyHandler getEuStorage() {
        return this.getHandler(IRCapabilities.ENERGY_BLOCK);
    }

    @Override
    public int emitRedstoneLevel() {
        return 0;
    }

    @Override
    public void setRedstoneSignalType(RedstoneSignalType redstoneSignalType) {
        this.redstoneSignalType = redstoneSignalType;
        this.updateData();
    }

    @Override
    public RedstoneSignalType getRedstoneSignalType() {
        return redstoneSignalType;
    }

    public void setRedstoneSignalStrength(int redstoneSignalStrength) {
        this.redstoneSignalStrength = redstoneSignalStrength;
    }

    public void initCapCache() {
        if (level instanceof ServerLevel serverLevel) {
            for (Direction direction : Direction.values()) {
                this.euCaches.add(BlockCapabilityCache.create(IRCapabilities.ENERGY_BLOCK, serverLevel, worldPosition.relative(direction), direction));
            }
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();

        this.initCapCache();
        this.refreshCachedRecipe();
    }

    @Override
    public void dropItems(IItemHandler handler) {
        if (!this.removedByWrench && handler != null) {
            super.dropItems(handler);
        } else {
            this.removedByWrench = false;
        }
    }

    public int getRedstoneSignalStrength() {
        return redstoneSignalStrength;
    }

    @Override
    protected void loadData(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadData(tag, provider);

        this.burnt = tag.getBoolean("burnt");
        this.redstoneSignalStrength = tag.getInt("signal_strength");
        this.redstoneSignalType = RedstoneSignalType.CODEC.decode(NbtOps.INSTANCE, tag.get("redstone_signal")).result().orElse(Pair.of(RedstoneSignalType.IGNORED, new CompoundTag())).getFirst();
        this.progress = tag.getFloat("progress");
    }

    @Override
    protected void saveData(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveData(tag, provider);

        tag.putBoolean("burnt", this.burnt);
        tag.putFloat("progress", this.progress);
        tag.putInt("signal_strength", this.redstoneSignalStrength);
        Optional<Tag> tag1 = RedstoneSignalType.CODEC.encodeStart(NbtOps.INSTANCE, this.redstoneSignalType).result();
        tag1.ifPresent(value -> {
            tag.put("redstone_signal", value);
        });
    }

    public @Nullable EnergyHandler getEuHandlerOnSide(@Nullable Direction direction) {
        return this.getEuStorage();
    }

    @Override
    public void beforeRemoveByWrench(Player player) {
        this.removedByWrench = true;
    }

    private void refreshCachedRecipe() {
        MachineRecipeInput recipeInput = this.createRecipeInput();
        if (this.getRecipeLayout() != null) {
            RecipeType<MachineRecipeImpl> recipeType = (RecipeType<MachineRecipeImpl>) this.getRecipeLayout().getRecipeType();
            MachineRecipeImpl recipe = this.level.getRecipeManager().getRecipeFor(recipeType, recipeInput, this.level)
                    .map(RecipeHolder::value)
                    .orElse(null);
            RegistryAccess provider = this.level.registryAccess();
            if (recipe != null) {
                if (recipe.hasResultItem(provider)) {
                    if (!forceInsertItem((IItemHandlerModifiable) this.getItemHandler(), this.getResultSlot(), recipe.assemble(recipeInput, provider).copy(), true, i -> {
                    }).isEmpty()) {
                        this.cachedRecipe = null;
                        return;
                    }
                }
                if (recipe.hasResultFluid(provider)) {
                    FluidStack resultFluid = recipe.assembleResultFluid(recipeInput, provider);
                    if (forceFillTank(this.getFluidHandler(), resultFluid.copy(), IFluidHandler.FluidAction.SIMULATE, i -> {
                    }) != resultFluid.getAmount()) {
                        this.cachedRecipe = null;
                        return;
                    }
                }
                if (recipe.hasResultEnergy(provider)) {
                    int resultEnergy = recipe.assembleResultEnergy(recipeInput, provider);
                    if (this.getEuStorage().forceFillEnergy(resultEnergy, true) != resultEnergy) {
                        this.cachedRecipe = null;
                        return;
                    }
                }
            }
            this.cachedRecipe = recipe;
        } else {
            this.cachedRecipe = null;
        }
    }

    public int forceFillTank(IFluidHandler fluidHandler, FluidStack resource, IFluidHandler.FluidAction action, Consumer<Integer> onChange) {
        if (resource.isEmpty()) {
            return 0;
        }

        FluidStack fluid = fluidHandler.getFluidInTank(0);
        int capacity = fluidHandler.getTankCapacity(0);

        if (action.simulate()) {
            if (fluid.isEmpty()) {
                return Math.min(capacity, resource.getAmount());
            }
            if (!FluidStack.isSameFluidSameComponents(fluid, resource)) {
                return 0;
            }
            return Math.min(capacity - fluid.getAmount(), resource.getAmount());
        }
        if (fluid.isEmpty()) {
            fluid = resource.copyWithAmount(Math.min(capacity, resource.getAmount()));
            onChange.accept(0);
            return fluid.getAmount();
        }
        if (!FluidStack.isSameFluidSameComponents(fluid, resource)) {
            return 0;
        }
        int filled = capacity - fluid.getAmount();

        if (resource.getAmount() < filled) {
            fluid.grow(resource.getAmount());
            filled = resource.getAmount();
        } else {
            fluid.setAmount(capacity);
        }
        if (filled > 0)
            onChange.accept(0);
        return filled;
    }

}
