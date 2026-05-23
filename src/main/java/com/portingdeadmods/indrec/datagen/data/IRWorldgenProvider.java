package com.portingdeadmods.indrec.datagen.data;

import com.portingdeadmods.indrec.content.worldgen.IRWorldgenKeys;
import com.portingdeadmods.indrec.content.worldgen.RubberTreeFoliagePlacer;
import com.portingdeadmods.indrec.content.worldgen.RubberTreeTrunkPlacer;
import com.portingdeadmods.indrec.registries.IRBlocks;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class IRWorldgenProvider {
    private static final RuleTest STONE_ORE_REPLACEABLES = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
    private static final RuleTest DEEPSLATE_ORE_REPLACEABLES = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

    public static void bootstrapPlacedFeatures(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        context.register(IRWorldgenKeys.RUBBER_TREE_KEY.placedFeature(), new PlacedFeature(configuredFeatures.getOrThrow(IRWorldgenKeys.RUBBER_TREE_KEY.configuredFeature()),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(1, 0.0025F, 1), IRBlocks.RUBBER_TREE_SAPLING.get())));
        registerPlacedOre(context, IRWorldgenKeys.TIN_ORE_KEY,  18, -20, 120);
        registerPlacedOre(context, IRWorldgenKeys.URANIUM_ORE_KEY, 12, -64, -8);
        registerPlacedOre(context, IRWorldgenKeys.IRIDIUM_ORE_KEY, 4, -64, -40);
    }

    private static void registerPlacedOre(BootstrapContext<PlacedFeature> context, IRWorldgenKeys.Feature ore, int count, int minHeight, int maxHeight) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        context.register(ore.placedFeature(), new PlacedFeature(
                configuredFeatures.getOrThrow(ore.configuredFeature()),
                List.of(
                        CountPlacement.of(count),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(minHeight), VerticalAnchor.absolute(maxHeight)),
                        BiomeFilter.biome()
                )));
    }

    public static void bootstrapConfiguredFeatures(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        ResourceKey<ConfiguredFeature<?, ?>> rubberTreeConfig = IRWorldgenKeys.RUBBER_TREE_KEY.configuredFeature();
        context.register(rubberTreeConfig, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(IRBlocks.RUBBER_TREE_LOG.get()),
                new RubberTreeTrunkPlacer(2, 3, 1),
                BlockStateProvider.simple(IRBlocks.RUBBER_TREE_LEAVES.get()),
                new RubberTreeFoliagePlacer(ConstantInt.of(2), ConstantInt.of(1)),
                new TwoLayersFeatureSize(2, 0, 2)).build())
        );
        registerConfiguredOre(context, IRWorldgenKeys.TIN_ORE_KEY, IRBlocks.TIN_ORE.get(), IRBlocks.DEEPSLATE_TIN_ORE.get(), 8);
        registerConfiguredOre(context, IRWorldgenKeys.URANIUM_ORE_KEY, IRBlocks.URANIUM_ORE.get(), IRBlocks.DEEPSLATE_URANIUM_ORE.get(), 4);
        registerConfiguredOre(context, IRWorldgenKeys.IRIDIUM_ORE_KEY, IRBlocks.IRIDIUM_ORE.get(), IRBlocks.DEEPSLATE_IRIDIUM_ORE.get(), 3, 0.5f);
    }

    private static void registerConfiguredOre(BootstrapContext<ConfiguredFeature<?, ?>> context, IRWorldgenKeys.Feature ore, Block oreBlock, Block deepslateOreBlock, int size) {
        registerConfiguredOre(context, ore, oreBlock, deepslateOreBlock, size, 0);
    }

    private static void registerConfiguredOre(BootstrapContext<ConfiguredFeature<?, ?>> context, IRWorldgenKeys.Feature ore, Block oreBlock, Block deepslateOreBlock, int size, float discardChanceOnAirExposure) {
        List<OreConfiguration.TargetBlockState> oreConfiguration = List.of(
                OreConfiguration.target(STONE_ORE_REPLACEABLES, oreBlock.defaultBlockState()),
                OreConfiguration.target(DEEPSLATE_ORE_REPLACEABLES, deepslateOreBlock.defaultBlockState()));
        context.register(ore.configuredFeature(), new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(oreConfiguration, size, discardChanceOnAirExposure)));
    }

}
