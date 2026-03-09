package com.portingdeadmods.indrec.datagen.data;

import com.portingdeadmods.indrec.registries.IRBlocks;
import com.portingdeadmods.indrec.registries.IRItems;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class IRBlockLootTableProvider extends BlockLootSubProvider {
    private final Set<Block> knownBlocks = new ReferenceOpenHashSet<>();

    public IRBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.VANILLA_SET, registries);
    }

    @Override
    protected void generate() {
        dropSelf(IRBlocks.MACHINE_FRAME.get());
        dropSelf(IRBlocks.ADVANCED_MACHINE_FRAME.get());

        dropSelf(IRBlocks.NUCLEAR_REACTOR_CHAMBER.get());
        dropSelf(IRBlocks.NUCLEAR_REACTOR.get());

        dropSelf(IRBlocks.TIN_CABLE.get());
        dropSelf(IRBlocks.COPPER_CABLE.get());
        dropSelf(IRBlocks.GOLD_CABLE.get());
        dropSelf(IRBlocks.HV_CABLE.get());
        dropSelf(IRBlocks.GLASS_FIBRE_CABLE.get());

        dropSelf(IRBlocks.BRONZE_BLOCK.get());
        dropSelf(IRBlocks.URANIUM_BLOCK.get());
        dropSelf(IRBlocks.TIN_BLOCK.get());

        dropSelf(IRBlocks.REINFORCED_STONE.get());
        dropSelf(IRBlocks.REINFORCED_GLASS.get());

        add(IRBlocks.REINFORCED_DOOR.get(), createDoorTable(IRBlocks.REINFORCED_DOOR.get()));
        dropSelf(IRBlocks.INDUSTRIAL_TNT.get());

        add(IRBlocks.RUBBER_TREE_RESIN_HOLE.get(), createResinLogTable(IRBlocks.RUBBER_TREE_LOG.get(), IRItems.STICKY_RESIN.get()));

        dropSelf(IRBlocks.RUBBER_TREE_LOG.get());
        dropSelf(IRBlocks.STRIPPED_RUBBER_TREE_LOG.get());
        dropSelf(IRBlocks.RUBBER_TREE_WOOD.get());
        dropSelf(IRBlocks.STRIPPED_RUBBER_TREE_WOOD.get());
        dropSelf(IRBlocks.RUBBER_TREE_SAPLING.get());
        dropSelf(IRBlocks.RUBBER_TREE_BUTTON.get());
        dropSelf(IRBlocks.RUBBER_TREE_PLANKS.get());
        dropSelf(IRBlocks.RUBBER_TREE_PRESSURE_PLATE.get());
        dropSelf(IRBlocks.RUBBER_TREE_FENCE.get());
        dropSelf(IRBlocks.RUBBER_TREE_FENCE_GATE.get());
        dropSelf(IRBlocks.RUBBER_TREE_SLAB.get());
        add(IRBlocks.RUBBER_TREE_LEAVES.get(), block -> createLeavesDrops(block, IRBlocks.RUBBER_TREE_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
        add(IRBlocks.RUBBER_TREE_DOOR.get(), this::createDoorTable);
        
        dropSelf(IRBlocks.NUKE.get());

        add(IRBlocks.TIN_ORE.get(), createOreDrop(IRBlocks.TIN_ORE.get(), IRItems.RAW_TIN.get()));
        add(IRBlocks.DEEPSLATE_TIN_ORE.get(), createOreDrop(IRBlocks.DEEPSLATE_TIN_ORE.get(), IRItems.RAW_TIN.get()));

        add(IRBlocks.URANIUM_ORE.get(), createOreDrop(IRBlocks.URANIUM_ORE.get(), IRItems.RAW_URANIUM.get()));
        add(IRBlocks.DEEPSLATE_URANIUM_ORE.get(), createOreDrop(IRBlocks.DEEPSLATE_URANIUM_ORE.get(), IRItems.RAW_URANIUM.get()));

        dropOther(IRBlocks.IRIDIUM_ORE.get(), IRItems.RAW_IRIDIUM);
        dropOther(IRBlocks.DEEPSLATE_IRIDIUM_ORE.get(), IRItems.RAW_IRIDIUM);
    }

    private LootTable.Builder createResinLogTable(ItemLike logBlock, ItemLike resinItem) {
        return LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1F))
                                .add(LootItem.lootTableItem(logBlock))
                ).withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1F))
                                .add(LootItem.lootTableItem(resinItem))
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0F, 1F)))
                );
    }

    @Override
    public @NotNull Set<Block> getKnownBlocks() {
        return knownBlocks;
    }

    @Override
    protected void add(@NotNull Block block, @NotNull LootTable.Builder table) {
        super.add(block, table);
        knownBlocks.add(block);
    }

}
