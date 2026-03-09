package com.portingdeadmods.indrec.datagen.data;

import com.portingdeadmods.indrec.registries.IRBlocks;
import com.portingdeadmods.indrec.registries.IRItems;
import com.portingdeadmods.indrec.registries.IRLootTables;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class IRMiscLootTableProvider implements LootTableSubProvider {
    public IRMiscLootTableProvider(HolderLookup.Provider provider) {

    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> generator) {
        generator.accept(IRLootTables.SCRAP_BOX,
                LootTable.lootTable().withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(chanceItem(Items.BONE, 1))
                        .add(chanceItem(Items.BREAD, 2))
                        .add(chanceItem(Items.CAKE, 1))
                        .add(chanceItem(IRItems.COAL_DUST, 1))
                        .add(chanceItem(IRItems.TIN_CAN_FOOD, 2))
                        .add(chanceItem(Items.COOKED_BEEF, 1))
                        .add(chanceItem(Items.COOKED_CHICKEN, 1))
                        .add(chanceItem(Items.COOKED_PORKCHOP, 1))
                        .add(chanceItem(IRItems.COPPER_DUST, 1))
                        .add(chanceItem(Blocks.COPPER_ORE, 1))
                        .add(chanceItem(Items.DIAMOND, 1))
                        .add(chanceItem(Items.DIRT, 5))
                        .add(chanceItem(Items.FEATHER, 1))
                        .add(chanceItem(Items.GRAVEL, 3))
                        .add(chanceItem(IRItems.GOLD_DUST, 1))
                        .add(chanceItem(Items.GOLDEN_HELMET, 1))
                        .add(chanceItem(Blocks.GOLD_ORE, 1))
                        .add(chanceItem(Items.GLOWSTONE_DUST, 1))
                        .add(chanceItem(Blocks.GRASS_BLOCK, 3))
                        .add(chanceItem(IRItems.IRON_DUST, 1))
                        .add(chanceItem(Items.IRON_ORE, 1))
                        .add(chanceItem(Items.LEATHER, 1))
                        .add(chanceItem(Items.MINECART, 1))
                        .add(chanceItem(Blocks.NETHERRACK, 2))
                        .add(chanceItem(IRItems.PLANT_BALL, 1))
                        .add(chanceItem(Blocks.PUMPKIN, 1))
                        .add(chanceItem(Items.APPLE, 2))
                        .add(chanceItem(Items.REDSTONE, 1))
                        .add(chanceItem(Items.ROTTEN_FLESH, 2))
                        .add(chanceItem(IRItems.RUBBER, 1))
                        .add(chanceItem(Blocks.SOUL_SAND, 1))
                        .add(chanceItem(IRItems.TIN_DUST, 1))
                        .add(chanceItem(IRBlocks.TIN_ORE, 1))
                        .add(chanceItem(Items.WOODEN_HOE, 5))
                        .add(chanceItem(Items.WOODEN_PICKAXE, 1))
                        .add(chanceItem(Items.WOODEN_SHOVEL, 1))
                        .add(chanceItem(Items.WOODEN_SWORD, 1))
                        .add(chanceItem(Items.STICK, 4))
                ));
    }

    private static LootPoolSingletonContainer.@NotNull Builder<?> chanceItem(ItemLike item, int weight) {
        return LootItem.lootTableItem(item).setWeight(weight);
    }
}
