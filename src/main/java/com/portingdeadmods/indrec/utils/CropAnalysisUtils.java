package com.portingdeadmods.indrec.utils;

import com.portingdeadmods.indrec.content.items.electric.CropAnalyzerItem;
import com.portingdeadmods.indrec.content.blockentities.CropBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class CropAnalysisUtils {
    /**
     * Drops per crop kind, cleared when data is reloaded or the server stops
     */
    private static final Map<CropDropsKey, List<CropDrop>> DROPS_CACHE = new ConcurrentHashMap<>();

    private CropAnalysisUtils() {
    }

    public static boolean isVanillaCrop(BlockState state) {
        return state.getBlock() instanceof net.minecraft.world.level.block.CropBlock;
    }

    public static @Nullable CropBlockEntity getPlantedCrop(BlockGetter level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof CropBlockEntity cropBE && cropBE.getCrop() != null ? cropBE : null;
    }

    public static @Nullable CropBlockEntity getAnalyzedCrop(BlockGetter level, BlockPos pos, ItemStack analyzer) {
        CropBlockEntity cropBE = getPlantedCrop(level, pos);
        return cropBE != null && CropAnalyzerItem.hasAnalyzed(analyzer, cropBE.getCrop()) ? cropBE : null;
    }

    /**
     * @return the crop block entity at the position, if it has a crop planted that the analyzer didn't analyze yet
     */
    public static @Nullable CropBlockEntity getAnalyzableCrop(BlockGetter level, BlockPos pos, ItemStack analyzer) {
        CropBlockEntity cropBE = getPlantedCrop(level, pos);
        return cropBE != null && !CropAnalyzerItem.hasAnalyzed(analyzer, cropBE.getCrop()) ? cropBE : null;
    }

    /**
     * @param analyzer the crop analyzer used to look at the crop, industrial crops only reveal their drops to analyzers that analyzed them
     * @return the drops of the fully grown crop, most likely first
     */
    public static List<CropDrop> getPossibleDrops(ServerLevel level, BlockPos pos, ItemStack analyzer) {
        CropDropsKey key = CropDropsKey.of(level, pos);
        if (key == null) return List.of();
        // Unanalyzed industrial crops don't reveal their drops, don't cache that result
        if (key.industrial() && getAnalyzedCrop(level, pos, analyzer) == null) return List.of();

        return DROPS_CACHE.computeIfAbsent(key, k -> collectPossibleDrops(level, pos));
    }

    public static void clearCache() {
        DROPS_CACHE.clear();
    }

    private static List<CropDrop> collectPossibleDrops(ServerLevel level, BlockPos pos) {
        List<CropDrop> drops = new ArrayList<>();
        LootTable lootTable = getLootTable(level, pos);
        if (lootTable != null) {
            // The parser evaluates the table for a fully grown crop harvested without a tool
            LootTableParser.parse(lootTable, level.registryAccess()).stream()
                    .sorted(Comparator.comparingDouble(LootTableParser.LootDrop::chance).reversed())
                    .map(CropDrop::of)
                    .forEach(drops::add);
        }

        // Dropped in addition to the loot when the crop is removed
        CropBlockEntity cropBE = getPlantedCrop(level, pos);
        if (cropBE != null) {
            cropBE.getCrop().value().seedDrop().ifPresent(seedDrop -> drops.add(CropDrop.of(seedDrop)));
        }
        return List.copyOf(drops);
    }

    private static @Nullable LootTable getLootTable(ServerLevel level, BlockPos pos) {
        ReloadableServerRegistries.Holder lootTables = level.getServer().reloadableRegistries();
        CropBlockEntity cropBE = getPlantedCrop(level, pos);
        if (cropBE != null) {
            return cropBE.getCrop().value().loot()
                    .map(loot -> loot.map(table -> table, lootTables::getLootTable))
                    .orElse(null);
        }
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof net.minecraft.world.level.block.CropBlock cropBlock) {
            return lootTables.getLootTable(cropBlock.getLootTable());
        }
        return null;
    }
}
