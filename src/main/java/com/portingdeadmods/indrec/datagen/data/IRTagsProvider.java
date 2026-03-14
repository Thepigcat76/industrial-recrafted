package com.portingdeadmods.indrec.datagen.data;

import com.mojang.datafixers.util.Either;
import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.registries.IRBlocks;
import com.portingdeadmods.indrec.tags.IRTags;
import com.portingdeadmods.indrec.tags.CTags;
import com.portingdeadmods.portingdeadlibs.api.fluids.PDLFluid;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class IRTagsProvider {
    public static void createTagProviders(DataGenerator generator, PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper, boolean isServer) {
        BlocksProvider provider = new BlocksProvider(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(isServer, provider);
        generator.addProvider(isServer, new ItemsProvider(packOutput, lookupProvider, provider.contentsGetter()));
        generator.addProvider(isServer, new FluidsProvider(packOutput, lookupProvider, existingFileHelper));
    }

    protected static class ItemsProvider extends ItemTagsProvider {
        public ItemsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags) {
            super(output, lookupProvider, blockTags);
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider provider) {
            CTags.ItemTags.TAGS.forEach(this::addTag);
            IRTags.ItemTags.TAGS.forEach(this::addTag);

            tag(ItemTags.PLANKS)
                    .add(IRBlocks.RUBBER_TREE_PLANKS.asItem());

            tag(ItemTags.LOGS_THAT_BURN)
                    .add(IRBlocks.RUBBER_TREE_LOG.asItem())
                    .add(IRBlocks.STRIPPED_RUBBER_TREE_LOG.asItem())
                    .add(IRBlocks.RUBBER_TREE_WOOD.asItem())
                    .add(IRBlocks.STRIPPED_RUBBER_TREE_WOOD.asItem());
        }

        private void addTag(TagKey<Item> itemTagKey, Supplier<List<Either<ItemLike, TagKey<Item>>>> listSupplier) {
            IntrinsicTagAppender<Item> tag = tag(itemTagKey);
            for (Either<ItemLike, TagKey<Item>> entry : listSupplier.get()) {
                entry.left().map(ItemLike::asItem).ifPresent(tag::add);
                entry.ifRight(tag::addTag);
            }
        }
    }

    protected static class BlocksProvider extends BlockTagsProvider {
        public BlocksProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, IndustrialRecrafted.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            IRTags.BlockTags.TAGS.forEach(this::addTag);
            this.tag(IRTags.BlockTags.WRENCHABLE)
                    .add(IRBlocks.TIN_CABLE.get())
                    .add(IRBlocks.COPPER_CABLE.get())
                    .add(IRBlocks.GOLD_CABLE.get())
                    .add(IRBlocks.HV_CABLE.get())
                    .add(IRBlocks.GLASS_FIBRE_CABLE.get());

            this.tag(BlockTags.NEEDS_STONE_TOOL)
                    .add(IRBlocks.TIN_ORE.get())
                    .add(IRBlocks.DEEPSLATE_TIN_ORE.get());
            this.tag(BlockTags.NEEDS_IRON_TOOL)
                    .add(IRBlocks.URANIUM_ORE.get())
                    .add(IRBlocks.DEEPSLATE_URANIUM_ORE.get());
            this.tag(BlockTags.NEEDS_DIAMOND_TOOL)
                    .add(IRBlocks.IRIDIUM_ORE.get())
                    .add(IRBlocks.DEEPSLATE_IRIDIUM_ORE.get());

            this.tag(BlockTags.LEAVES)
                    .add(IRBlocks.RUBBER_TREE_LEAVES.get());

            this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                    .add(IRBlocks.TIN_ORE.get())
                    .add(IRBlocks.DEEPSLATE_TIN_ORE.get())
                    .add(IRBlocks.URANIUM_ORE.get())
                    .add(IRBlocks.DEEPSLATE_URANIUM_ORE.get())
                    .add(IRBlocks.IRIDIUM_ORE.get())
                    .add(IRBlocks.DEEPSLATE_IRIDIUM_ORE.get());

            this.tag(BlockTags.MINEABLE_WITH_AXE)
                    .add(IRBlocks.RUBBER_TREE_RESIN_HOLE.get())
                    .add(IRBlocks.RUBBER_TREE_LOG.get())
                    .add(IRBlocks.STRIPPED_RUBBER_TREE_LOG.get())
                    .add(IRBlocks.RUBBER_TREE_WOOD.get())
                    .add(IRBlocks.STRIPPED_RUBBER_TREE_WOOD.get())
                    .add(IRBlocks.RUBBER_TREE_PLANKS.get())
                    .add(IRBlocks.RUBBER_TREE_DOOR.get())
                    .add(IRBlocks.RUBBER_TREE_TRAPDOOR.get())
                    .add(IRBlocks.RUBBER_TREE_STAIRS.get())
                    .add(IRBlocks.RUBBER_TREE_SLAB.get())
                    .add(IRBlocks.RUBBER_TREE_BUTTON.get())
                    .add(IRBlocks.RUBBER_TREE_PRESSURE_PLATE.get())
                    .add(IRBlocks.RUBBER_TREE_FENCE.get())
                    .add(IRBlocks.RUBBER_TREE_FENCE_GATE.get());
        }

        private void addTag(TagKey<Block> itemTagKey, Supplier<List<Either<Block, TagKey<Block>>>> listSupplier) {
            IntrinsicTagAppender<Block> tag = tag(itemTagKey);
            for (Either<Block, TagKey<Block>> entry : listSupplier.get()) {
                entry.ifLeft(tag::add);
                entry.ifRight(tag::addTag);
            }
        }
    }

    public static class FluidsProvider extends FluidTagsProvider {
        public FluidsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, provider, IndustrialRecrafted.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
        }

        private void tag(TagKey<Fluid> fluidTagKey, PDLFluid... fluids) {
            IntrinsicTagAppender<Fluid> tag = tag(fluidTagKey);
            for (PDLFluid fluid : fluids) {
                tag.add(fluid.getStillFluid());
            }
        }

        private void tag(TagKey<Fluid> fluidTagKey, Fluid... fluids) {
            IntrinsicTagAppender<Fluid> tag = tag(fluidTagKey);
            for (Fluid fluid : fluids) {
                tag.add(fluid);
            }
        }

        @SafeVarargs
        private void tag(TagKey<Fluid> fluidTagKey, TagKey<Fluid>... fluids) {
            IntrinsicTagAppender<Fluid> tag = tag(fluidTagKey);
            for (TagKey<Fluid> fluid : fluids) {
                tag.addTag(fluid);
            }
        }
    }
}
