package com.portingdeadmods.indrec.utils;

import com.portingdeadmods.indrec.content.crops.requirements.LightLevelRequirement;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Crop analyzer info of a non-industrial crop block (vanilla or modded): {@link CropBlock}s, pumpkin/melon stems, nether wart and sweet berry bushes.
 *
 * @param name             the name shown as the crop's name, stems use the name of their fruit
 * @param lootTable        the loot table of the harvested crop, stems use the loot table of their fruit
 * @param fullyGrownState  the state the loot table is evaluated for
 * @param soil             the blocks the crop can be planted on
 * @param light            the light level the crop needs to grow, null if it grows at any light level
 */
public record VanillaCropInfo(Component name, int age, int maxAge, ResourceKey<LootTable> lootTable, BlockState fullyGrownState,
                              List<Block> soil, @Nullable LightLevelRequirement light) {
    /**
     * Same as {@link com.portingdeadmods.indrec.content.blocks.CropBlock#DEFAULT_MIN_LIGHT_LEVEL}, used by vanilla crops, stems and berry bushes
     */
    private static final LightLevelRequirement CROP_LIGHT = LightLevelRequirement.min(com.portingdeadmods.indrec.content.blocks.CropBlock.DEFAULT_MIN_LIGHT_LEVEL);

    public static boolean isSupported(BlockState state) {
        Block block = state.getBlock();
        return block instanceof CropBlock || block instanceof StemBlock || block instanceof AttachedStemBlock
                || block instanceof NetherWartBlock || block instanceof SweetBerryBushBlock;
    }

    public static @Nullable VanillaCropInfo of(BlockState state) {
        Block block = state.getBlock();
        if (block instanceof CropBlock crop) {
            return new VanillaCropInfo(crop.getName(), crop.getAge(state), crop.getMaxAge(), crop.getLootTable(),
                    crop.getStateForAge(crop.getMaxAge()), List.of(Blocks.FARMLAND), CROP_LIGHT);
        }
        if (block instanceof StemBlock stem) {
            return ofStem(stem.fruit, state.getValue(StemBlock.AGE));
        }
        if (block instanceof AttachedStemBlock attachedStem) {
            // A stem only attaches to its fruit once fully grown
            return ofStem(attachedStem.fruit, StemBlock.MAX_AGE);
        }
        if (block instanceof NetherWartBlock) {
            return new VanillaCropInfo(block.getName(), state.getValue(NetherWartBlock.AGE), NetherWartBlock.MAX_AGE, block.getLootTable(),
                    state.setValue(NetherWartBlock.AGE, NetherWartBlock.MAX_AGE), List.of(Blocks.SOUL_SAND), null);
        }
        if (block instanceof SweetBerryBushBlock) {
            // Bushes can be planted on any dirt or farmland
            List<Block> soil = new ArrayList<>();
            BuiltInRegistries.BLOCK.getTagOrEmpty(BlockTags.DIRT).forEach(holder -> soil.add(holder.value()));
            if (!soil.contains(Blocks.FARMLAND)) soil.add(Blocks.FARMLAND);
            return new VanillaCropInfo(block.getName(), state.getValue(SweetBerryBushBlock.AGE), SweetBerryBushBlock.MAX_AGE, block.getLootTable(),
                    state.setValue(SweetBerryBushBlock.AGE, SweetBerryBushBlock.MAX_AGE), List.copyOf(soil), CROP_LIGHT);
        }
        return null;
    }

    private static @Nullable VanillaCropInfo ofStem(ResourceKey<Block> fruitKey, int age) {
        // Harvesting a stem crop means breaking its fruit, the stem itself stays
        Block fruit = BuiltInRegistries.BLOCK.getHolder(fruitKey).map(Holder::value).orElse(null);
        if (fruit == null) return null;
        return new VanillaCropInfo(fruit.getName(), age, StemBlock.MAX_AGE, fruit.getLootTable(), fruit.defaultBlockState(),
                List.of(Blocks.FARMLAND), CROP_LIGHT);
    }
}
