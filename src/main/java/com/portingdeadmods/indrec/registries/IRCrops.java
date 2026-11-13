package com.portingdeadmods.indrec.registries;

import com.portingdeadmods.indrec.IRRegistries;
import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.api.crops.Crop;
import net.minecraft.resources.ResourceKey;

public final class IRCrops {
    public static final ResourceKey<Crop> STICK_REED = key("stick_reed");
    public static final ResourceKey<Crop> WEED = key("weed");
    public static final ResourceKey<Crop> WHEAT = key("wheat");
    public static final ResourceKey<Crop> BEETROOTS = key("beetroots");
    public static final ResourceKey<Crop> CARROTS = key("carrots");
    public static final ResourceKey<Crop> POTATOES = key("potatoes");
    public static final ResourceKey<Crop> MELON = key("melon");
    public static final ResourceKey<Crop> PUMPKIN = key("pumpkin");
    public static final ResourceKey<Crop> NETHER_WART = key("nether_wart");
    public static final ResourceKey<Crop> RED_MUSHROOM = key("red_mushroom");
    public static final ResourceKey<Crop> BROWN_MUSHROOM = key("brown_mushroom");
    public static final ResourceKey<Crop> GOLDEN_DANDELION = key("golden_dandelion");
    public static final ResourceKey<Crop> IRON_VINES = key("iron_vines");
    public static final ResourceKey<Crop> TIN_BLOSSOM = key("tin_blossom");
    public static final ResourceKey<Crop> COPRIUM = key("coprium");
    // Oxidized variants of coprium, can't be planted or harvested, they only drop their oxidized coprium
    public static final ResourceKey<Crop> EXPOSED_COPRIUM = key("exposed_coprium");
    public static final ResourceKey<Crop> WEATHERED_COPRIUM = key("weathered_coprium");
    public static final ResourceKey<Crop> OXIDIZED_COPRIUM = key("oxidized_coprium");
    public static final ResourceKey<Crop> WAXED_COPRIUM = key("waxed_coprium");
    public static final ResourceKey<Crop> WITHER_ROSE = key("wither_rose");
    public static final ResourceKey<Crop> DANDELION = key("dandelion");
    public static final ResourceKey<Crop> POPPY = key("poppy");
    public static final ResourceKey<Crop> ENDER_LILY = key("ender_lily");

    private IRCrops() {
    }

    private static ResourceKey<Crop> key(String name) {
        return ResourceKey.create(IRRegistries.CROP, IndustrialRecrafted.rl(name));
    }
}
