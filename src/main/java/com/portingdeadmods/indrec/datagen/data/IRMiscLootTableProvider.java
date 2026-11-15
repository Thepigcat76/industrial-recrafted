package com.portingdeadmods.indrec.datagen.data;

import com.portingdeadmods.indrec.registries.IRDataComponents;
import com.portingdeadmods.portingdeadlibs.api.data.PDLDataComponents;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.SetComponentsFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
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
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class IRMiscLootTableProvider implements LootTableSubProvider {
    public IRMiscLootTableProvider(HolderLookup.Provider provider) {

    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> generator) {
        generator.accept(IRLootTables.STICK_REED, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(IRItems.STICK_REED)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))));

        generator.accept(IRLootTables.SUGAR_CANE, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(Items.SUGAR_CANE)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))));

        generator.accept(IRLootTables.GOLDEN_DANDELION, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(IRBlocks.GOLDEN_DANDELION)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))));

        generator.accept(IRLootTables.IRON_VINES, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(IRBlocks.IRON_VINES)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))));

        generator.accept(IRLootTables.WITHER_ROSE, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(Items.WITHER_ROSE)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))));

        generator.accept(IRLootTables.DANDELION, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(Items.DANDELION)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))));

        generator.accept(IRLootTables.POPPY, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(Items.POPPY)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))));

        generator.accept(IRLootTables.RED_MUSHROOM, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(Items.RED_MUSHROOM)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))));

        generator.accept(IRLootTables.BROWN_MUSHROOM, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(Items.BROWN_MUSHROOM)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))));

        generator.accept(IRLootTables.ENDER_LILY, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(IRBlocks.ENDER_LILY)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))));

        generator.accept(IRLootTables.TIN_BLOSSOM, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(IRBlocks.TIN_BLOSSOM)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))));

        generator.accept(IRLootTables.COPRIUM, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(IRBlocks.COPRIUM)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4))))));

        generator.accept(IRLootTables.DUNGEON, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(UniformGenerator.between(2, 4))
                        .setBonusRolls(UniformGenerator.between(1, 3))
                        .add(LootItem.lootTableItem(IRItems.REFINED_IRON_INGOT)
                                .apply(countBetween(0, 2)))
                        .add(LootItem.lootTableItem(IRItems.TIN_INGOT)
                                .apply(countBetween(0, 4)))
                        .add(LootItem.lootTableItem(IRItems.ADVANCED_ALLOY_PLATE)
                                .apply(countBetween(0, 1)))
                        .add(LootItem.lootTableItem(IRItems.BASIC_CIRCUIT))
                        .add(LootItem.lootTableItem(IRItems.COPPER_PLATE)
                                .apply(countBetween(0, 2)))
                        .add(LootItem.lootTableItem(IRItems.REDSTONE_BATTERY)
                                .when(chance(0.35)))
                        .add(LootItem.lootTableItem(IRItems.CARBON_MESH)
                                .when(chance(0.25)))
                        .add(LootItem.lootTableItem(IRItems.FLUID_CELL)
                                .apply(fluid(Fluids.WATER, 1000))
                                .when(chance(0.35)))
                ));

        generator.accept(IRLootTables.VILLAGE_PLAINS, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(IRItems.CROP_STICKS)
                                .apply(countBetween(3, 6)))
                )
                .withPool(LootPool.lootPool()
                        .setRolls(UniformGenerator.between(2, 4))
                        .setBonusRolls(UniformGenerator.between(1, 3))
                        .add(LootItem.lootTableItem(IRItems.STICKY_RESIN)
                                .apply(countBetween(0, 2)))
                        .add(LootItem.lootTableItem(IRItems.PLANT_BALL)
                                .when(chance(0.45))
                                .apply(countBetween(0, 4)))
                        .add(LootItem.lootTableItem(IRBlocks.RUBBER_TREE_SAPLING)
                                .when(chance(0.55)))
                ));

        generator.accept(IRLootTables.VILLAGE_BLACK_SMITH, LootTable.lootTable()
//                .withPool(LootPool.lootPool()
//                        .add(LootItem.lootTableItem(IRItems.TOOLBOX)
//                                .when(chance(0.45)))
//                )
                .withPool(LootPool.lootPool()
                        .setRolls(UniformGenerator.between(2, 4))
                        .setBonusRolls(UniformGenerator.between(1, 3))
                        .add(LootItem.lootTableItem(IRItems.REFINED_IRON_INGOT)
                                .apply(countBetween(0, 2)))
                        .add(LootItem.lootTableItem(IRItems.TIN_PLATE)
                                .apply(countBetween(1, 3)))
                        .add(LootItem.lootTableItem(IRItems.ADVANCED_ALLOY_PLATE)
                                .when(chance(0.35)))
                        .add(LootItem.lootTableItem(IRItems.COAL_DUST)
                                .apply(countBetween(1, 3)))
                ));

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

    private static LootItemConditionalFunction.@NotNull Builder<?> fluid(FlowingFluid fluid, int amount) {
        return SetComponentsFunction.setComponent(PDLDataComponents.FLUID.get(), SimpleFluidContent.copyOf(new FluidStack(fluid, amount)));
    }

    private static LootItemCondition.@NotNull Builder chance(double chance) {
        return LootItemRandomChanceCondition.randomChance((float) chance);
    }

    private static LootItemConditionalFunction.@NotNull Builder<?> countBetween(int min, int max) {
        return SetItemCountFunction.setCount(UniformGenerator.between(min, max));
    }

    private static LootPoolSingletonContainer.@NotNull Builder<?> chanceItem(ItemLike item, int weight) {
        return LootItem.lootTableItem(item).setWeight(weight);
    }
}
