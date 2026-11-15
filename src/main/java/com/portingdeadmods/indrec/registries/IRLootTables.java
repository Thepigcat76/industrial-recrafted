package com.portingdeadmods.indrec.registries;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;

public final class IRLootTables {
    public static final ResourceKey<LootTable> SCRAP_BOX = key("scrap_box");
    public static final ResourceKey<LootTable> STICK_REED = cropKey("stick_reed");
    public static final ResourceKey<LootTable> WEED = cropKey("weed");
    public static final ResourceKey<LootTable> SUGAR_CANE = cropKey("sugar_cane");
    public static final ResourceKey<LootTable> GOLDEN_DANDELION = cropKey("golden_dandelion");
    public static final ResourceKey<LootTable> IRON_VINES = cropKey("iron_vines");
    public static final ResourceKey<LootTable> TIN_BLOSSOM = cropKey("tin_blossom");
    public static final ResourceKey<LootTable> COPRIUM = cropKey("coprium");
    public static final ResourceKey<LootTable> WITHER_ROSE = cropKey("wither_rose");
    public static final ResourceKey<LootTable> DANDELION = cropKey("dandelion");
    public static final ResourceKey<LootTable> POPPY = cropKey("poppy");
    public static final ResourceKey<LootTable> ENDER_LILY = cropKey("ender_lily");
    public static final ResourceKey<LootTable> RED_MUSHROOM = cropKey("red_mushroom");
    public static final ResourceKey<LootTable> BROWN_MUSHROOM = cropKey("brown_mushroom");

    public static final ResourceKey<LootTable> DUNGEON = key("chests/dungeon");
    // Handwritten in src/main/resources with a mod_loaded condition, as GuideME is optional. Added to dungeon chests by a loot modifier
    public static final ResourceKey<LootTable> DUNGEON_GUIDE = key("chests/dungeon_guide");
    public static final ResourceKey<LootTable> VILLAGE_PLAINS = key("chests/village_plains_house");
    public static final ResourceKey<LootTable> VILLAGE_BLACK_SMITH = key("chests/village_weaponsmith");


    private IRLootTables() {
    }

    private static @NotNull ResourceKey<LootTable> key(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE, IndustrialRecrafted.rl(path));
    }

    private static @NotNull ResourceKey<LootTable> cropKey(String name) {
        return key("crops/" + name);
    }
}
