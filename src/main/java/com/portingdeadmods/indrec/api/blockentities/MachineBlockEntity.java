package com.portingdeadmods.indrec.api.blockentities;

import com.mojang.datafixers.util.Pair;
import com.portingdeadmods.indrec.IRCapabilities;
import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.api.blocks.MachineBlock;
import com.portingdeadmods.indrec.api.capabilities.StorageChangedListener;
import com.portingdeadmods.indrec.api.energy.EnergyHandler;
import com.portingdeadmods.indrec.api.energy.EnergyTier;
import com.portingdeadmods.indrec.api.items.UpgradeItem;
import com.portingdeadmods.indrec.api.recipes.MachineRecipe;
import com.portingdeadmods.indrec.api.recipes.MachineRecipeLayout;
import com.portingdeadmods.indrec.content.menus.ChargingSlot;
import com.portingdeadmods.indrec.content.recipes.MachineRecipeImpl;
import com.portingdeadmods.indrec.impl.energy.EnergyHandlerImpl;
import com.portingdeadmods.indrec.content.recipes.components.TimeComponent;
import com.portingdeadmods.indrec.content.recipes.components.energy.EnergyInputComponent;
import com.portingdeadmods.indrec.content.recipes.flags.FluidInputComponentFlag;
import com.portingdeadmods.indrec.content.recipes.flags.FluidOutputComponentFlag;
import com.portingdeadmods.indrec.content.recipes.flags.ItemInputComponentFlag;
import com.portingdeadmods.indrec.content.recipes.flags.ItemOutputComponentFlag;
import com.portingdeadmods.indrec.networking.clientbound.SetEnergyPayload;
import com.portingdeadmods.indrec.networking.clientbound.SetProgressPayload;
import com.portingdeadmods.indrec.registries.IRRecipeComponentFlags;
import com.portingdeadmods.indrec.registries.IRSoundEvents;
import com.portingdeadmods.indrec.utils.BlockUtils;
import com.portingdeadmods.indrec.utils.machines.IRMachine;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.blockentities.RedstoneBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.utils.PDLBlockStateProperties;
import com.portingdeadmods.portingdeadlibs.utils.capabilities.HandlerUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
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
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class MachineBlockEntity extends ContainerBlockEntity implements RedstoneBlockEntity, WrenchListenerBlockEntity {
    private static final int MIN_TICKS_TO_ACTIVATE = 5;
    private static final ResourceLocation UPGRADES_KEY = IndustrialRecrafted.rl("upgrades");

    private final List<ChargingSlot> chargingSlots;
    private final List<BlockCapabilityCache<EnergyHandler, Direction>> euCaches;
    protected final IRMachine machine;
    private RedstoneSignalType redstoneSignalType = RedstoneSignalType.IGNORED;
    private int redstoneSignalStrength;
    private boolean redstoneSignalChecked;
    protected RecipeHolder<MachineRecipeImpl> cachedRecipe;
    private ResourceLocation loadedRecipe;
    protected float progress;
    protected float maxProgress;
    protected float progressIncrement;
    protected float energyUsageMultiplier;
    protected boolean burnt;
    private boolean removedByWrench;
    protected boolean syncEnergyThisTick;
    protected boolean syncFluidsThisTick;
    private @Nullable ItemStackHandler upgradeItemHandler;
    // What the upgrade items have last been told about, to tell additions and removals apart
    private ItemStack @Nullable [] appliedUpgrades;

    public MachineBlockEntity(IRMachine machine, BlockPos blockPos, BlockState blockState) {
        super(machine.getBlockEntityType(), blockPos, blockState);
        this.machine = machine;
        this.euCaches = new ArrayList<>();
        this.chargingSlots = new ArrayList<>();
        this.progressIncrement = 1F;
        this.energyUsageMultiplier = 1F;
    }

    public MachineRecipeLayout<?> getRecipeLayout() {
        return this.machine.getRecipeLayout();
    }

    public @Nullable RecipeHolder<MachineRecipeImpl> getCachedRecipe() {
        return cachedRecipe;
    }

    public boolean hasCachedRecipe() {
        return cachedRecipe != null;
    }

    protected void syncProgress() {
        PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) this.level, new ChunkPos(this.worldPosition), new SetProgressPayload(this.worldPosition, this.progress, this.maxProgress));
    }

    @Override
    public void tick() {
        super.tick();

        for (ChargingSlot chargingSlot : this.chargingSlots) {
            if (chargingSlot.getItem().isEmpty()) continue;

            this.tickChargingSlot(chargingSlot);
        }

        this.tickEnergySpreading();

        // The signal isn't updated while the chunk is unloaded, so check it again after loading
        if (!this.redstoneSignalChecked && !this.level.isClientSide()) {
            this.updateRedstoneSignalStrength();
        }

        if (!this.burnt) {
            if (this.isRedstoneActive()) {
                this.tickRecipe();
            } else {
                this.tickRedstoneDisabled();
            }
            this.tickUpgrades();
        }

        if (this.syncEnergyThisTick) {
            PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) level, new ChunkPos(this.worldPosition), new SetEnergyPayload(this.worldPosition, this.getEuStorage().getEnergyStored()));
            // Reset only after syncing: energy can arrive from other block entities (e.g. cables)
            // between this machine's ticks, and clearing the flag at the start of tick() would drop that sync
            this.syncEnergyThisTick = false;
        }

        if (this.syncFluidsThisTick) {
            // Fluid tanks have no dedicated payload, so send a block entity update (at most once per tick)
            this.updateData();
            this.syncFluidsThisTick = false;
        }

    }

    public boolean canSpreadEnergy(Direction direction) {
        return true;
    }

    protected void tickEnergySpreading() {
        if (this.shouldSpreadEnergy() && !this.burnt && !level.isClientSide()) {
            int amountPerBlock = this.getAmountPerBlock();

            for (BlockCapabilityCache<EnergyHandler, Direction> cache : this.euCaches) {
                if (this.canSpreadEnergy(cache.context())) {
                    EnergyHandler euHandler = cache.getCapability();
                    if (euHandler != null) {
                        if (!euHandler.isValidTier(this.getEuStorage().getEnergyTier())) {
                            continue;
                        }

                        if (level.getBlockEntity(this.worldPosition.relative(cache.context())) instanceof MachineBlockEntity neighbor
                                && (neighbor.isBurnt() || neighbor.tryBurn(euHandler, this.getEuStorage().getEnergyTier(), amountPerBlock))) {
                            continue;
                        }

                        int filled = euHandler.fillEnergy(amountPerBlock, false);
                        if (filled > 0) {
                            this.syncEnergyThisTick = true;
                            getEuStorage().forceDrainEnergy(filled, false);
                            PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) level, new ChunkPos(this.worldPosition.relative(cache.context())), new SetEnergyPayload(this.worldPosition.relative(cache.context()), euHandler.getEnergyStored()));
                        }
                    }

                }
            }

        }
    }

    public boolean isBurnt() {
        return burnt;
    }

    public boolean tryBurn(EnergyHandler handler, EnergyTier energyTier, int energy) {
        boolean overloaded = energy > 0
                && !(handler instanceof EnergyHandler.NoFill)
                && energyTier.order() - handler.getEnergyTier().order() > EnergyTier.MAX_TIER_DIFFERENCE;
        if (overloaded) {
            this.setBurnt(true);
        }
        return overloaded;
    }

    public void setBurnt(boolean burnt) {
        if (burnt && !this.burnt) {
            BlockUtils.playBurnSound(this.level, this.worldPosition);
            this.setActive(false);
        }
        this.burnt = burnt;
        this.updateData();
    }

    protected int getResultSlot() {
        return 1;
    }

    protected int getResultTank() {
        return 0;
    }

    protected @NotNull MachineRecipe.MachineRecipeInput createRecipeInput() {
        if (this.getItemHandler() == null) return MachineRecipe.MachineRecipeInput.EMPTY;
        return new MachineRecipe.MachineRecipeInput(List.of(this.getItemHandler().getStackInSlot(0)));
    }

    protected void onItemsChanged(int slot) {
        setChanged();
        this.refreshCachedRecipe();
    }

    protected void onFluidsChanged(int tank) {
        if (!level.isClientSide)
            this.syncFluidsThisTick = true;
        setChanged();
        this.refreshCachedRecipe();
    }

    protected void onEuChanged(int oldAmount) {
        if (!level.isClientSide)
            this.syncEnergyThisTick = true;
        setChanged();
        this.refreshCachedRecipe();
    }

    /**
     * Call this after adding progress. A machine on a weak supply only runs for single ticks at a time, which
     * shouldn't toggle its block state (updating neighbors like cables every time) or play sounds.
     */
    protected void updateRunningState() {
        if (this.progress >= MIN_TICKS_TO_ACTIVATE * this.progressIncrement) {
            setActive(true);
            this.playMachineSound();
        }
    }

    protected void playMachineSound() {
        // Not based on the progress, which skips over exact values when overclocked
        if (this.level.getGameTime() % 12 == 0) {
            this.level.playSound(null, worldPosition, IRSoundEvents.MACHINE.get(), SoundSource.BLOCKS, 0.35f, 1f);
        }
    }

    /**
     * Called instead of {@link #tickRecipe()} while the redstone signal disables the machine.
     * The progress is kept, so the machine continues where it stopped once it gets enabled again
     */
    protected void tickRedstoneDisabled() {
        this.setActive(false);
    }

    protected void tickRecipe() {
        if (!this.level.isClientSide && this.level.getGameTime() % 30 == 0) {
            syncProgress();
        }

        if (this.hasCachedRecipe()) {
            MachineRecipeImpl recipe = this.cachedRecipe.value();
            if (this.hasProgressFinished()) {
                this.progress = 0;
                // Only the server rolls chances and changes the contents, clients get the result sent to them
                if (!this.level.isClientSide()) {
                    this.finishRecipe(recipe);
                }

                if (this.cachedRecipe == null) {
                    setActive(false);
                }
            } else {
                boolean canRun = false;

                EnergyInputComponent inputComponent = recipe.getComponent(EnergyInputComponent.TYPE);
                if (inputComponent != null) {
                    int energy = Math.round(inputComponent.energy() / recipe.getComponent(TimeComponent.TYPE).time() * this.energyUsageMultiplier);
                    // Energy stays in the machine until there's enough for a tick, so a weak supply fills it up
                    // a bit and then drops, instead of being used up without doing anything
                    if (this.getEuStorage().forceDrainEnergy(energy, true) == energy) {
                        if (!this.level.isClientSide()) {
                            this.getEuStorage().forceDrainEnergy(energy, false);
                        }
                        canRun = true;
                    }
                } else {
                    canRun = true;
                }


                if (canRun) {
                    this.progress += this.progressIncrement;
                    this.updateRunningState();
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

    }

    private void finishRecipe(MachineRecipeImpl recipe) {
        RegistryAccess provider = this.level.registryAccess();
        ItemStack resultItem = recipe.assemble(this.createRecipeInput(), provider);
        FluidStack resultFluid = recipe.assembleResultFluid(this.createRecipeInput(), provider);

        int resultEnergy = recipe.assembleResultEnergy(this.createRecipeInput(), provider);
        ItemOutputComponentFlag itemOutput = recipe.getComponentByFlag(IRRecipeComponentFlags.ITEM_OUTPUT);
        FluidOutputComponentFlag fluidOutput = recipe.getComponentByFlag(IRRecipeComponentFlags.FLUID_OUTPUT);

        ItemInputComponentFlag itemInput = recipe.getComponentByFlag(IRRecipeComponentFlags.ITEM_INPUT);
        FluidInputComponentFlag fluidInput = recipe.getComponentByFlag(IRRecipeComponentFlags.FLUID_INPUT);

        boolean hasResultEnergy = recipe.hasResultEnergy(provider);
        boolean hasResultItem = recipe.hasResultItem(provider);
        boolean hasResultFluid = recipe.hasResultFluid(provider);

        if (hasResultEnergy) {
            this.getEuStorage().forceFillEnergy(resultEnergy, false);
        }

        if (hasResultItem) {
            List<ItemStack> outputs = itemOutput.getOutputs();
            for (int i = 0; i < outputs.size(); i++) {
                ItemStack output = outputs.get(i);
                if (itemOutput.isOutputted(this.level.random, i)) {
                    this.forceInsertItem((IItemHandlerModifiable) this.getItemHandler(), this.getResultSlot() + i, i == 0 ? resultItem.copy() : output.copy(), false, this::onItemsChanged);
                }
            }
        }

        if (hasResultFluid && fluidOutput.isOutputted(this.level.random, 0)) {
            this.forceFillTank(this.getFluidHandler(), resultFluid.copy(), IFluidHandler.FluidAction.EXECUTE, this::onFluidsChanged);
        }

        if (itemInput != null) {
            if (!itemInput.getIngredients().isEmpty()) {
                for (int i = 0; i < itemInput.getIngredients().size(); i++) {
                    this.getItemHandler().extractItem(i, itemInput.getIngredients().get(i).count(), false);
                }
            } else if (itemInput.isSpecial()) {
                this.handleSpecialItemInput(itemInput);
            }
        }

        if (fluidInput != null && !fluidInput.getIngredients().isEmpty()) {
            this.getFluidHandler().drain(fluidInput.getIngredients().getFirst().amount(), IFluidHandler.FluidAction.EXECUTE);
        }

        this.updateData();
    }

    protected void handleSpecialItemInput(ItemInputComponentFlag itemInput) {
        throw new UnsupportedOperationException("Special ItemInputs are not supported for " + this.getClass().getName());
    }

    public void setActive(boolean active) {
        if (MachineBlock.isActive(getBlockState()) != active) {
            level.setBlockAndUpdate(worldPosition, getBlockState().setValue(PDLBlockStateProperties.ACTIVE, active));
        }
    }

    protected boolean hasProgressFinished() {
        return this.hasCachedRecipe() && (this.getProgress() >= this.getMaxProgress() || !this.cachedRecipe.value().hasProgress());
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

    protected boolean isValidChargingItem(ItemStack stack) {
        EnergyHandler itemEnergyStorage = stack.getCapability(IRCapabilities.ENERGY_ITEM);
        return itemEnergyStorage != null
                && Math.abs(itemEnergyStorage.getEnergyTier().order() - this.getEuStorage().getEnergyTier().order()) <= EnergyTier.MAX_TIER_DIFFERENCE;
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
        return (int) this.maxProgress;
    }

    private int getRecipeTime(MachineRecipe recipe) {
        return recipe.hasProgress() ? recipe.getComponent(TimeComponent.TYPE).time() : 0;
    }

    public boolean shouldSpreadEnergy() {
        return false;
    }

    protected <H extends EnergyHandler> H addEuStorage(H handler) {
        return this.addHandler(IRCapabilities.ENERGY_BLOCK.name(), handler);
    }

    protected final <T extends EnergyHandler & StorageChangedListener> void addMachineEuStorage(Function<Supplier<? extends EnergyTier>, T> energyHandlerConstructor, Consumer<Integer> onChangedFunction) {
        if (!this.machine.canStoreEnergy()) {
            throw new IllegalStateException(this.getClass().getName() + " cannot store energy. Tried adding eu storage anyways");
        }
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
        if (this.redstoneSignalStrength != redstoneSignalStrength) {
            this.redstoneSignalStrength = redstoneSignalStrength;
            // Clients run the machines too, so they need the signal
            if (this.level != null && !this.level.isClientSide()) {
                this.updateData();
            }
        }
    }

    /**
     * Reads the redstone signal this machine receives from its neighbors, only call this on the server
     */
    public void updateRedstoneSignalStrength() {
        this.redstoneSignalChecked = true;
        this.setRedstoneSignalStrength(this.level.getBestNeighborSignal(this.worldPosition));
    }

    /**
     * @return whether the machine may run with its current redstone signal and signal type
     */
    public boolean isRedstoneActive() {
        return this.redstoneSignalType.isActive(this.redstoneSignalStrength);
    }

    protected @Nullable EnergyHandler getNeighborEuHandler(Direction direction) {
        this.initCapCache();
        // The caches are created in the order of Direction.values()
        return this.euCaches.isEmpty() ? null : this.euCaches.get(direction.ordinal()).getCapability();
    }

    public void initCapCache() {
        if (level instanceof ServerLevel serverLevel && this.euCaches.isEmpty()) {
            for (Direction direction : Direction.values()) {
                this.euCaches.add(BlockCapabilityCache.create(IRCapabilities.ENERGY_BLOCK, serverLevel, worldPosition.relative(direction), direction));
            }
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();

        this.initCapCache();

        if (this.loadedRecipe != null) {
            Optional<RecipeHolder<?>> recipeHolder = this.level.getRecipeManager().byKey(this.loadedRecipe);
            recipeHolder.ifPresent(recipe -> {
                this.setCachedRecipe(recipe);
                if (recipe.value() instanceof MachineRecipe machineRecipe) {
                    this.setMaxProgress(this.getRecipeTime(machineRecipe));
                }
            });
        }

    }

    @Override
    public void dropItems(IItemHandler handler) {
        if (!this.removedByWrench && handler != null) {
            super.dropItems(handler);
            if (this.upgradeItemHandler != null) {
                super.dropItems(this.upgradeItemHandler);
            }
        } else {
            this.removedByWrench = false;
        }
    }

    public int getRedstoneSignalStrength() {
        return redstoneSignalStrength;
    }

    public ResourceLocation getRecipeId() {
        return this.cachedRecipe.id();
    }

    public void setCachedRecipe(RecipeHolder<?> recipe) {
        if (recipe == null) {
            this.cachedRecipe = null;
        } else if (recipe.value() instanceof MachineRecipeImpl machineRecipe) {
            this.cachedRecipe = new RecipeHolder<>(recipe.id(), machineRecipe);
        }
    }

    public void setMaxProgress(float maxProgress) {
        this.maxProgress = maxProgress;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        super.onDataPacket(net, pkt, lookupProvider);

        // Loading doesn't report changed items, and the client's recipe would be outdated without an open menu
        if (this.getRecipeLayout() != null) {
            this.refreshCachedRecipe();
        }
    }

    @Override
    protected void loadData(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadData(tag, provider);

        // Handlers don't report changes when they get loaded
        if (this.upgradeItemHandler != null) {
            for (int i = 0; i < this.upgradeItemHandler.getSlots(); i++) {
                this.applyUpgradeChange(i);
            }
        }

        this.burnt = tag.getBoolean("burnt");
        this.redstoneSignalStrength = tag.getInt("signal_strength");
        this.redstoneSignalType = RedstoneSignalType.CODEC.decode(NbtOps.INSTANCE, tag.get("redstone_signal")).result().orElse(Pair.of(RedstoneSignalType.IGNORED, new CompoundTag())).getFirst();
        this.progress = tag.getFloat("progress");
        if (tag.contains("current_recipe")) {
            this.loadedRecipe = ResourceLocation.parse(tag.getString("current_recipe"));
        }
    }

    @Override
    protected void saveData(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveData(tag, provider);

        tag.putBoolean("burnt", this.burnt);
        tag.putFloat("progress", this.progress);
        tag.putInt("signal_strength", this.redstoneSignalStrength);
        if (this.hasCachedRecipe()) {
            tag.putString("current_recipe", this.getRecipeId().toString());
        }
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
        if (this.getRecipeLayout() != null) {
            MachineRecipe.MachineRecipeInput recipeInput = this.createRecipeInput();
            RecipeType<MachineRecipeImpl> recipeType = (RecipeType<MachineRecipeImpl>) this.getRecipeLayout().getRecipeType();
            RecipeHolder<MachineRecipeImpl> recipeHolder = this.level.getRecipeManager().getRecipeFor(recipeType, recipeInput, this.level)
                    .orElse(null);
            RegistryAccess provider = this.level.registryAccess();
            if (recipeHolder != null) {
                MachineRecipeImpl recipe = recipeHolder.value();
                if (recipe.hasResultItem(provider)) {
                    if (!forceInsertItem((IItemHandlerModifiable) this.getItemHandler(), this.getResultSlot(), recipe.assemble(recipeInput, provider).copy(), true, i -> {
                    }).isEmpty()) {
                        this.setCachedRecipe(null);
                        this.setMaxProgress(0);
                        return;
                    }
                }
                if (recipe.hasResultFluid(provider)) {
                    FluidStack resultFluid = recipe.assembleResultFluid(recipeInput, provider);
                    if (forceFillTank(this.getFluidHandler(), resultFluid.copy(), IFluidHandler.FluidAction.SIMULATE, i -> {
                    }) != resultFluid.getAmount()) {
                        this.setCachedRecipe(null);
                        this.setMaxProgress(0);
                        return;
                    }
                }
                if (recipe.hasResultEnergy(provider)) {
                    int resultEnergy = recipe.assembleResultEnergy(recipeInput, provider);
                    if (this.getEuStorage().forceFillEnergy(resultEnergy, true) != resultEnergy) {
                        this.setCachedRecipe(null);
                        this.setMaxProgress(0);
                        return;
                    }
                }
            }
            this.setCachedRecipe(recipeHolder);
            if (recipeHolder != null) {
                this.setMaxProgress(this.getRecipeTime(recipeHolder.value()));
            } else {
                this.setMaxProgress(0);
            }
        } else {
            this.setCachedRecipe(null);
            this.setMaxProgress(0);
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

    public void setProgess(float progress) {
        this.progress = progress;
    }

    /**
     * Sets up upgrades for this machine, call this in the constructor of a machine that implements {@link UpgradeBlockEntity}.
     * That and adding the upgrade panel to its screen is all that's needed, this class has the interface's methods
     * already, which can be overridden to limit what the machine supports.
     */
    protected final void addUpgradeItemHandler(int slots) {
        if (!(this instanceof UpgradeBlockEntity)) {
            throw new IllegalStateException(this.getClass().getName() + " has to implement UpgradeBlockEntity to have upgrades");
        }

        // Not limited any further here, the upgrade slots apply the limit of each upgrade
        this.upgradeItemHandler = this.addHandler(UPGRADES_KEY, HandlerUtils.newItemStackHandler(this::isUpgradeValid, slot -> Item.DEFAULT_MAX_STACK_SIZE, this::onUpgradesChanged, slots));
        this.appliedUpgrades = new ItemStack[slots];
        Arrays.fill(this.appliedUpgrades, ItemStack.EMPTY);
    }

    public boolean supportsUpgrades() {
        return this.upgradeItemHandler != null;
    }

    public boolean supportsUpgrade(UpgradeItem upgrade) {
        return true;
    }

    public int getUpgradeSlots(UpgradeItem upgrade) {
        return this.upgradeItemHandler != null ? this.upgradeItemHandler.getSlots() : 0;
    }

    public @Nullable IItemHandler getUpgradeItemHandler() {
        return this.upgradeItemHandler;
    }

    private boolean isUpgradeValid(int slot, ItemStack stack) {
        return stack.getItem() instanceof UpgradeItem upgrade && this.supportsUpgrade(upgrade);
    }

    private void onUpgradesChanged(int slot) {
        this.applyUpgradeChange(slot);
        if (!this.level.isClientSide()) {
            this.updateData();
        } else {
            this.setChanged();
        }
    }

    private void applyUpgradeChange(int slot) {
        ItemStack previous = this.appliedUpgrades[slot];
        ItemStack current = this.upgradeItemHandler.getStackInSlot(slot);
        this.appliedUpgrades[slot] = current.copy();

        boolean sameItem = previous.is(current.getItem());
        if (previous.getItem() instanceof UpgradeItem upgrade && (!sameItem || current.getCount() < previous.getCount())) {
            upgrade.onUpgradeRemoved((MachineBlockEntity & UpgradeBlockEntity) this);
        }
        if (current.getItem() instanceof UpgradeItem upgrade && (!sameItem || current.getCount() > previous.getCount())) {
            upgrade.onUpgradeAdded((MachineBlockEntity & UpgradeBlockEntity) this);
        }
    }

    private void tickUpgrades() {
        if (this.upgradeItemHandler != null) {
            for (int i = 0; i < this.upgradeItemHandler.getSlots(); i++) {
                if (this.upgradeItemHandler.getStackInSlot(i).getItem() instanceof UpgradeItem upgrade) {
                    upgrade.tickUpgrade((MachineBlockEntity & UpgradeBlockEntity) this);
                }
            }
        }
    }

    public void setProgressIncrement(float progressIncrement) {
        this.progressIncrement = progressIncrement;
    }

    public void setEnergyUsageMultiplier(float energyUsageMultiplier) {
        this.energyUsageMultiplier = energyUsageMultiplier;
    }

    public EnergyTier getBaseEnergyTier() {
        return this.machine.getEnergyTier();
    }

    public void setEnergyTier(EnergyTier energyTier) {
        if (this.getEuStorage() instanceof EnergyHandlerImpl energyHandler) {
            energyHandler.setEnergyTier(energyTier);
        }
    }

    public int getBaseEnergyCapacity() {
        return this.machine.getEnergyCapacity();
    }

    public void setEnergyCapacity(int energyCapacity) {
        EnergyHandler energyStorage = this.getEuStorage();
        energyStorage.setEnergyCapacity(energyCapacity);
        // The level is missing while loading, where the stored energy can't be above the saved capacity anyway
        if (this.level != null && energyStorage.getEnergyStored() > energyCapacity) {
            energyStorage.setEnergyStored(energyCapacity);
        }
    }

}
