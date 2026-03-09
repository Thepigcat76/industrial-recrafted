package com.portingdeadmods.examplemod.tags;

import com.mojang.datafixers.util.Either;
import com.portingdeadmods.examplemod.registries.IRBlocks;
import com.portingdeadmods.examplemod.registries.IRItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class CTags {
    public static class ItemTags {
        public static final Map<TagKey<Item>, Supplier<List<Either<ItemLike, TagKey<Item>>>>> TAGS = new HashMap<>();

        public static final TagKey<Item> ORES_TIN = createTag("ores/tin", () -> List.of(item(IRBlocks.TIN_ORE)));
        public static final TagKey<Item> ORES = createTag("ores", () -> List.of(tag(ORES_TIN)));

        public static final TagKey<Item> PLATES_COPPER = createTag("plates/copper", () -> List.of(item(IRItems.COPPER_PLATE)));
        public static final TagKey<Item> PLATES_TIN = createTag("plates/tin", () -> List.of(item(IRItems.TIN_PLATE)));
        public static final TagKey<Item> PLATES_CARBON = createTag("plates/carbon", () -> List.of(item(IRItems.CARBON_PLATE)));
        public static final TagKey<Item> PLATES_ADVANCED_ALLOY = createTag("plates/advanced_alloy", () -> List.of(item(IRItems.ADVANCED_ALLOY_PLATE)));
        public static final TagKey<Item> PLATES = createTag("plates", () -> List.of(tag(PLATES_COPPER), tag(PLATES_TIN), tag(PLATES_CARBON)));

        public static final TagKey<Item> INGOTS_URANIUM = createTag("ingots/uranium", () -> List.of(item(IRItems.URANIUM_INGOT)));
        public static final TagKey<Item> INGOTS_TIN = createTag("ingots/tin", () -> List.of(item(IRItems.TIN_INGOT)));
        public static final TagKey<Item> INGOTS_REFINED_IRON = createTag("ingots/refined_iron", () -> List.of(item(IRItems.REFINED_IRON_INGOT)));
        public static final TagKey<Item> INGOTS_IRIDIUM = createTag("ingots/iridium", () -> List.of(item(IRItems.IRIDIUM_INGOT)));
        public static final TagKey<Item> INGOTS_BRONZE = createTag("ingots/bronze", () -> List.of(item(IRItems.BRONZE_INGOT)));
        public static final TagKey<Item> INGOTS = createTag("ingots", () -> List.of(tag(INGOTS_URANIUM), tag(INGOTS_TIN), tag(INGOTS_REFINED_IRON), tag(INGOTS_IRIDIUM), tag(INGOTS_BRONZE)));

        public static final TagKey<Item> DUSTS_GOLD = createTag("dusts/gold", () -> List.of(item(IRItems.GOLD_DUST)));
        public static final TagKey<Item> DUSTS_COAL = createTag("dusts/coal", () -> List.of(item(IRItems.COAL_DUST)));
        public static final TagKey<Item> DUSTS_IRON = createTag("dusts/iron", () -> List.of(item(IRItems.IRON_DUST)));
        public static final TagKey<Item> DUSTS_COPPER = createTag("dusts/copper", () -> List.of(item(IRItems.COPPER_DUST)));
        public static final TagKey<Item> DUSTS_TIN = createTag("dusts/tin", () -> List.of(item(IRItems.TIN_DUST)));
        public static final TagKey<Item> DUSTS_BRONZE = createTag("dusts/bronze", () -> List.of(item(IRItems.BRONZE_DUST)));
        public static final TagKey<Item> DUSTS = createTag("dusts", () -> List.of(tag(DUSTS_COPPER), tag(DUSTS_TIN), tag(DUSTS_IRON), tag(DUSTS_COAL), tag(DUSTS_GOLD)));

        public static final TagKey<Item> RAW_MATERIALS_IRIDIUM = createTag("raw_materials/iridium", () -> List.of(item(IRItems.RAW_IRIDIUM)));
        public static final TagKey<Item> RAW_MATERIALS_TIN = createTag("raw_materials/tin", () -> List.of(item(IRItems.RAW_TIN)));
        public static final TagKey<Item> RAW_MATERIALS = createTag("raw_materials", () -> List.of(tag(RAW_MATERIALS_IRIDIUM), tag(RAW_MATERIALS_TIN)));

        private static TagKey<Item> createTag(String id, Supplier<List<Either<ItemLike, TagKey<Item>>>> items) {
            TagKey<Item> tag = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", id));
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
}
