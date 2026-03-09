package com.portingdeadmods.indrec.content.worldgen;

import com.portingdeadmods.indrec.IndustrialReclassified;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class IRWorldgenKeys {
    public static final Feature RUBBER_TREE_KEY = registerFeature("rubber_tree");
    public static final ResourceKey<BiomeModifier> RUBBER_TREE_MODIFIER_KEY = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, IndustrialReclassified.rl("rubber_tree_modifier"));
    public static final Feature TIN_ORE_KEY = registerFeature("tin_ore");
    public static final ResourceKey<BiomeModifier> TIN_ORE_MODIFIER_KEY = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, IndustrialReclassified.rl("tin_ore_modifier"));
    public static final Feature URANIUM_ORE_KEY = registerFeature("uranium_ore");
    public static final ResourceKey<BiomeModifier> URANIUM_ORE_MODIFIER_KEY = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, IndustrialReclassified.rl("uranium_ore_modifier"));
    public static final Feature IRIDIUM_ORE_KEY = registerFeature("iridium_ore");
    public static final ResourceKey<BiomeModifier> IRIDIUM_ORE_MODIFIER_KEY = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, IndustrialReclassified.rl("iridium_ore_modifier"));

    public static ResourceKey<ConfiguredFeature<?, ?>> registerConfigKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(IndustrialReclassified.MODID, name));
    }

    public static ResourceKey<PlacedFeature> registerPlaceKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(IndustrialReclassified.MODID, name));
    }

    public static Feature registerFeature(String name) {
        return new Feature(registerConfigKey(name), registerPlaceKey(name));
    }

    public record Feature(ResourceKey<ConfiguredFeature<?, ?>> configuredFeature, ResourceKey<PlacedFeature> placedFeature) {
    }
}