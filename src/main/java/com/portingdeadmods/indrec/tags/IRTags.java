package com.portingdeadmods.indrec.tags;

import net.minecraft.world.level.block.Blocks;
import com.mojang.datafixers.util.Either;
import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.registries.IRBlocks;
import com.portingdeadmods.indrec.registries.IRMachines;
import com.portingdeadmods.indrec.utils.machines.IRMachine;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class IRTags {
    public static class ItemTags {
        public static final Map<TagKey<Item>, Supplier<List<Either<ItemLike, TagKey<Item>>>>> TAGS = new HashMap<>();

        public static final TagKey<Item> RUBBER_LOGS = createTag("rubber_logs", () -> List.of(
                item(IRBlocks.RUBBER_TREE_LOG),
                item(IRBlocks.STRIPPED_RUBBER_TREE_LOG),
                item(IRBlocks.RUBBER_TREE_WOOD),
                item(IRBlocks.STRIPPED_RUBBER_TREE_WOOD)
        ));

        public static final TagKey<Item> COPRIUM = createTag("coprium", () -> List.of(
                item(IRBlocks.COPRIUM),
                item(IRBlocks.EXPOSED_COPRIUM),
                item(IRBlocks.WEATHERED_COPRIUM),
                item(IRBlocks.OXIDIZED_COPRIUM),
                item(IRBlocks.WAXED_COPRIUM),
                item(IRBlocks.WAXED_EXPOSED_COPRIUM),
                item(IRBlocks.WAXED_WEATHERED_COPRIUM),
                item(IRBlocks.WAXED_OXIDIZED_COPRIUM)
        ));

        private static TagKey<Item> createTag(String id, Supplier<List<Either<ItemLike, TagKey<Item>>>> items) {
            TagKey<Item> tag = TagKey.create(Registries.ITEM, IndustrialRecrafted.rl(id));
            TAGS.put(tag, items);
            return tag;
        }

        private static Either<ItemLike, TagKey<Item>> item(ItemLike item) {
            return Either.left(item);
        }

        private static Either<ItemLike, TagKey<Item>> tag(TagKey<Item> tag) {
            return Either.right(tag);
        }

    }

    public static class BlockTags {
        public static final Map<TagKey<Block>, Supplier<List<Either<Block, TagKey<Block>>>>> TAGS = new HashMap<>();

        public static final TagKey<Block> WRENCHABLE = createTag("wrenchable", () -> IRMachines.HELPER.getMachines().stream()
                .map(IRMachine::getBlock)
                .map(BlockTags::block)
                .toList());
        public static final TagKey<Block> VALID_INDUSTRIAL_SOIL = createTag("valid_industrial_soil", () -> List.of(block(Blocks.FARMLAND), block(Blocks.SOUL_SAND), block(Blocks.END_STONE)));
        /**
         * Crop-like blocks with an integer {@code age} property, that hoes can harvest with right-click once fully grown.
         * All {@link net.minecraft.world.level.block.CropBlock}s are always supported
         */
        public static final TagKey<Block> HOE_HARVESTABLE = createTag("hoe_harvestable", () -> List.of(block(Blocks.NETHER_WART), block(Blocks.COCOA)));
        public static final TagKey<Block> MINEABLE_WITH_DRILL = createTag("mineable_with_drill", () -> List.of(tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_SHOVEL), tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_PICKAXE)));

        private static TagKey<Block> createTag(String id, Supplier<List<Either<Block, TagKey<Block>>>> items) {
            TagKey<Block> tag = TagKey.create(Registries.BLOCK, IndustrialRecrafted.rl(id));
            TAGS.put(tag, items);
            return tag;
        }

        private static Either<Block, TagKey<Block>> block(Block item) {
            return Either.left(item);
        }

        private static Either<Block, TagKey<Block>> tag(TagKey<Block> tag) {
            return Either.right(tag);
        }
    }

}
