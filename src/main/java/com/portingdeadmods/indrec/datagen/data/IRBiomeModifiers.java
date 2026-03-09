package com.portingdeadmods.indrec.datagen.data;

import com.portingdeadmods.indrec.content.worldgen.IRWorldgenKeys;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;

public class IRBiomeModifiers {
    public static void bootstrapBiomeModifiers(BootstrapContext<BiomeModifier> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        context.register(IRWorldgenKeys.RUBBER_TREE_MODIFIER_KEY,
                new BiomeModifiers.AddFeaturesBiomeModifier(
                        biomes.getOrThrow(Tags.Biomes.IS_OVERWORLD),
                        HolderSet.direct(placedFeatures.getOrThrow(IRWorldgenKeys.RUBBER_TREE_KEY.placedFeature())),
                        GenerationStep.Decoration.VEGETAL_DECORATION
                )
        );
        context.register(IRWorldgenKeys.URANIUM_ORE_MODIFIER_KEY,
                new BiomeModifiers.AddFeaturesBiomeModifier(
                        biomes.getOrThrow(Tags.Biomes.IS_OVERWORLD),
                        HolderSet.direct(placedFeatures.getOrThrow(IRWorldgenKeys.URANIUM_ORE_KEY.placedFeature())),
                        GenerationStep.Decoration.UNDERGROUND_ORES
                )
        );
        context.register(IRWorldgenKeys.TIN_ORE_MODIFIER_KEY,
                new BiomeModifiers.AddFeaturesBiomeModifier(
                        biomes.getOrThrow(Tags.Biomes.IS_OVERWORLD),
                        HolderSet.direct(placedFeatures.getOrThrow(IRWorldgenKeys.TIN_ORE_KEY.placedFeature())),
                        GenerationStep.Decoration.UNDERGROUND_ORES
                )
        );
        context.register(IRWorldgenKeys.IRIDIUM_ORE_MODIFIER_KEY,
                new BiomeModifiers.AddFeaturesBiomeModifier(
                        biomes.getOrThrow(Tags.Biomes.IS_OVERWORLD),
                        HolderSet.direct(placedFeatures.getOrThrow(IRWorldgenKeys.IRIDIUM_ORE_KEY.placedFeature())),
                        GenerationStep.Decoration.UNDERGROUND_ORES
                )
        );
    }
}
