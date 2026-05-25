package com.portingdeadmods.indrec.utils.machines;

import com.portingdeadmods.indrec.IRCapabilities;
import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;
import com.portingdeadmods.indrec.api.blocks.MachineBlock;
import com.portingdeadmods.indrec.api.energy.EnergyHandler;
import com.portingdeadmods.indrec.api.energy.EnergyTier;
import com.portingdeadmods.indrec.api.menus.MachineMenu;
import com.portingdeadmods.indrec.api.recipes.MachineRecipeLayout;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class IRMachine implements ItemLike {
    public static final Map<ResourceLocation, Supplier<BlockEntityType<? extends MachineBlockEntity>>> BLOCK_ENTITY_TYPES = Collections.synchronizedMap(new HashMap<>());
    public static final AtomicReference<MachineBlock.Builder> MACHINE_BLOCK_BUILDER = new AtomicReference<>();
    public static final Map<String, MachineBlock.Builder> MACHINE_BUILDERS = new HashMap<>();
    private final String name;
    private final Supplier<? extends EnergyTier> energyTierSupplier;
    private final Supplier<? extends MachineBlock> blockSupplier;
    private final Supplier<BlockItem> blockItemSupplier;
    private final Supplier<BlockEntityType<? extends MachineBlockEntity>> blockEntityTypeSupplier;
    private final @Nullable Supplier<MenuType<? extends MachineMenu<?>>> menuTypeSupplier;
    private final @Nullable MachineRecipeLayout<?> recipeLayout;

    private final List<Slot> inputSlots;
    private final List<Slot> outputSlots;
    private final List<Slot> catalystSlots;
    private final boolean addToCreative;

    private boolean allowEnergyInsert;
    private boolean allowEnergyExtract;

    private IntSupplier energyCapacity;

    public IRMachine(String name, Supplier<? extends EnergyTier> energyTierSupplier,
                     Supplier<? extends MachineBlock> blockSupplier, Supplier<BlockItem> blockItemSupplier,
                     Supplier<BlockEntityType<? extends MachineBlockEntity>> blockEntityTypeSupplier,
                     @Nullable Supplier<MenuType<? extends MachineMenu<?>>> menuTypeSupplier,
                     @Nullable MachineRecipeLayout<?> recipeLayout, List<Slot> inputSlots, List<Slot> outputSlots, List<Slot> catalystSlots, boolean addToCreative, boolean allowEnergyInsert, boolean allowEnergyExtract, IntSupplier energyCapacity) {
        this.name = name;
        this.energyTierSupplier = energyTierSupplier;
        this.blockSupplier = blockSupplier;
        this.blockItemSupplier = blockItemSupplier;
        this.blockEntityTypeSupplier = blockEntityTypeSupplier;
        this.menuTypeSupplier = menuTypeSupplier;
        this.recipeLayout = recipeLayout;
        this.addToCreative = addToCreative;

        this.inputSlots = inputSlots;
        this.outputSlots = outputSlots;
        this.catalystSlots = catalystSlots;

        this.allowEnergyInsert = allowEnergyInsert;
        this.allowEnergyExtract = allowEnergyExtract;

        this.energyCapacity = energyCapacity;
    }

    public boolean shouldAddToCreativeTab() {
        return this.addToCreative;
    }

    public List<Slot> getInputSlots() {
        return inputSlots;
    }

    public List<Slot> getOutputSlots() {
        return outputSlots;
    }

    public List<Slot> getCatalystSlots() {
        return catalystSlots;
    }

    public EnergyTier getEnergyTier() {
        return energyTierSupplier.get();
    }

    public MachineBlock getBlock() {
        return blockSupplier.get();
    }

    public BlockItem getBlockItem() {
        return blockItemSupplier.get();
    }

    public MachineRecipeLayout<?> getRecipeLayout() {
        return recipeLayout;
    }

    public BlockEntityType<? extends MachineBlockEntity> getBlockEntityType() {
        return blockEntityTypeSupplier.get();
    }

    public <T extends MachineMenu<?>> MenuType<T> getMenuType() {
        return menuTypeSupplier != null ? ((MenuType<T>) menuTypeSupplier.get()) : null;
    }

    public static Builder builder(Supplier<? extends EnergyTier> energyTierSupplier) {
        return new Builder(energyTierSupplier);
    }

    @Override
    public Item asItem() {
        return this.getBlockItem();
    }

    public String name() {
        return name;
    }

    public Supplier<? extends MachineBlock> getBlockSupplier() {
        return blockSupplier;
    }

    public Supplier<BlockItem> getBlockItemSupplier() {
        return blockItemSupplier;
    }

    public boolean allowInsertEnergy() {
        return this.allowEnergyInsert;
    }

    public boolean allowExtractEnergy() {
        return this.allowEnergyExtract;
    }

    public int getEnergyCapacity() {
        return this.energyCapacity.getAsInt();
    }

    public static class Builder {
        private TriFunction<String, MachineBlock.Builder, Supplier<? extends EnergyTier>, ? extends MachineBlock> blockFactory;
        private MachineBlock.Builder machineBlockBuilder;
        private Supplier<? extends BlockItem> blockItemSupplier;
        private IContainerFactory<? extends MachineMenu<?>> menuSupplier;
        private BlockEntityType.BlockEntitySupplier<? extends MachineBlockEntity> blockEntitySupplier;
        private final Supplier<? extends EnergyTier> energyTierSupplier;
        private MachineRecipeLayout<?> recipeLayout;
        private final List<Slot> inputSlots;
        private final List<Slot> outputSlots;
        private final List<Slot> catalystSlots;
        private boolean addToCreativeTab;
        private boolean allowEnergyInsert;
        private boolean allowEnergyExtract;
        private IntSupplier energyCapacity;

        private Builder(Supplier<? extends EnergyTier> energyTierSupplier) {
            this.energyTierSupplier = energyTierSupplier;
            this.inputSlots = new ArrayList<>();
            this.outputSlots = new ArrayList<>();
            this.catalystSlots = new ArrayList<>();
            this.addToCreativeTab = true;
        }

        public Builder energy(IntSupplier capacity, boolean allowInsert, boolean allowExtract) {
            this.energyCapacity = capacity;
            this.allowEnergyInsert = allowInsert;
            this.allowEnergyExtract = allowExtract;
            return this;
        }

        public Builder addInputSlot(Slot slot) {
            this.inputSlots.add(slot);
            return this;
        }

        public Builder addOutputSlot(Slot slot) {
            this.outputSlots.add(slot);
            return this;
        }

        public Builder addCatalystSlot(Slot slot) {
            this.catalystSlots.add(slot);
            return this;
        }

        public Builder addToCreativeTab(boolean addToCreativeTab) {
            this.addToCreativeTab = addToCreativeTab;
            return this;
        }

        public Builder blockEntity(BlockEntityType.BlockEntitySupplier<? extends MachineBlockEntity> blockEntitySupplier) {
            this.blockEntitySupplier = blockEntitySupplier;
            return this;
        }

        public <C> Builder blockEntity(TriFunction<BlockPos, BlockState, C, ? extends MachineBlockEntity> blockEntitySupplier, Supplier<C> context) {
            this.blockEntitySupplier = (pos, state) -> blockEntitySupplier.apply(pos, state, context.get());
            return this;
        }

        public Builder block(TriFunction<String, MachineBlock.Builder, Supplier<? extends EnergyTier>, ? extends MachineBlock> blockFactory, MachineBlock.Builder builder) {
            this.blockFactory = blockFactory;
            this.machineBlockBuilder = builder;
            return this;
        }

        public <T extends MachineBlockEntity> Builder menu(IContainerFactory<? extends MachineMenu<T>> menuSupplier) {
            this.menuSupplier = menuSupplier;
            return this;
        }

        public Builder blockItem(Function<Item.Properties, ? extends BlockItem> blockItemFunction, Item.Properties properties) {
            this.blockItemSupplier = () -> blockItemFunction.apply(properties);
            return this;
        }

        public Builder recipeLayout(MachineRecipeLayout<?> recipeLayout) {
            this.recipeLayout = recipeLayout;
            return this;
        }

        public IRMachine build(String name, MachineRegistrationHelper registrationHelper) {
            Objects.requireNonNull(this.blockFactory, "%s machine's block was not initialized".formatted(name));

            Supplier<? extends MachineBlock> blockSupplier = () -> {
                MACHINE_BLOCK_BUILDER.set(this.machineBlockBuilder);
                return this.blockFactory.apply(name, this.machineBlockBuilder, this.energyTierSupplier);
            };
            DeferredHolder<Block, ? extends MachineBlock> registeredBlock = registrationHelper.getBlockRegister().register(name, blockSupplier);
            if (this.blockItemSupplier == null) {
                this.blockItemSupplier = () -> new BlockItem(registeredBlock.get(), new Item.Properties());
            }

            DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends MachineBlockEntity>> typeSupplier;
            if (this.blockEntitySupplier != null) {
                typeSupplier = registrationHelper.getBlockEntityRegister().register(name,
                        () -> BlockEntityType.Builder.of(this.blockEntitySupplier, registeredBlock.get()).build(null));
                BLOCK_ENTITY_TYPES.put(ResourceLocation.fromNamespaceAndPath(registrationHelper.getBlockEntityRegister().getNamespace(), name), typeSupplier);
            } else {
                typeSupplier = null;
            }
            return new IRMachine(
                    name,
                    this.energyTierSupplier,
                    registeredBlock,
                    registrationHelper.getItemRegister().register(name, this.blockItemSupplier),
                    typeSupplier,
                    this.menuSupplier != null ? registrationHelper.getMenuTypeRegister().register(name, () -> IMenuTypeExtension.create(this.menuSupplier)) : null,
                    this.recipeLayout,
                    this.inputSlots,
                    this.outputSlots,
                    this.catalystSlots,
                    this.addToCreativeTab,
                    this.allowEnergyInsert,
                    this.allowEnergyExtract,
                    this.energyCapacity
            );
        }
    }

    public record Slot(int x, int y, Predicate<ItemStack> filter) {
        public static Slot withTagFilter(int x, int y, TagKey<Item> tag) {
            return new Slot(x, y, stack -> stack.is(tag));
        }

        public static Slot withEnergyFilter(int x, int y) {
            return new Slot(x, y, stack -> stack.getCapability(IRCapabilities.ENERGY_ITEM) != null);
        }

        public static Slot withEnergyFilter(int x, int y, int minEnergy) {
            return new Slot(x, y, stack -> {
                EnergyHandler energyHandler = stack.getCapability(IRCapabilities.ENERGY_ITEM);
                return energyHandler != null && energyHandler.getEnergyStored() >= minEnergy;
            });
        }

    }

}