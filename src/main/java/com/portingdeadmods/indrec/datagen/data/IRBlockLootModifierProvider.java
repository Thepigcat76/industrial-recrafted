package com.portingdeadmods.indrec.datagen.data;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.compat.guideme.GuideMECompat;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import com.portingdeadmods.indrec.registries.IRLootTables;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.concurrent.CompletableFuture;

public class IRBlockLootModifierProvider extends GlobalLootModifierProvider {
    public IRBlockLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, IndustrialRecrafted.MODID);
    }

    @Override
    protected void start() {
        add("dungeon_chest_modifier", new AddTableLootModifier(new LootItemCondition[]{
                LootTableIdCondition.builder(ResourceLocation.withDefaultNamespace("chests/simple_dungeon")).build(),
                LootItemRandomChanceCondition.randomChance(1.0f).build()
        }, IRLootTables.DUNGEON));
        // GuideME is optional, the modifier and its loot table are only loaded with GuideME installed
        add("dungeon_guide_chest_modifier", new AddTableLootModifier(new LootItemCondition[]{
                LootTableIdCondition.builder(ResourceLocation.withDefaultNamespace("chests/simple_dungeon")).build(),
                LootItemRandomChanceCondition.randomChance(0.35f).build()
        }, IRLootTables.DUNGEON_GUIDE), new ModLoadedCondition(GuideMECompat.MODID));
        add("village_plains_chest_modifier", new AddTableLootModifier(new LootItemCondition[]{
                LootTableIdCondition.builder(ResourceLocation.withDefaultNamespace("chests/village/village_plains_house")).build(),
                LootItemRandomChanceCondition.randomChance(1.0f).build()
        }, IRLootTables.VILLAGE_PLAINS));
        add("village_weaponsmith_chest_modifier", new AddTableLootModifier(new LootItemCondition[]{
                LootTableIdCondition.builder(ResourceLocation.withDefaultNamespace("chests/village/village_weaponsmith")).build(),
                LootItemRandomChanceCondition.randomChance(1.0f).build()
        }, IRLootTables.VILLAGE_BLACK_SMITH));
    }
}