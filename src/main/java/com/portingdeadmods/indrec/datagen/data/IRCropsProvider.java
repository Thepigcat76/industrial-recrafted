package com.portingdeadmods.indrec.datagen.data;

import com.mojang.datafixers.util.Either;
import com.portingdeadmods.indrec.IRRegistries;
import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.api.crops.Crop;
import com.portingdeadmods.indrec.registries.IRLootTables;
import com.portingdeadmods.indrec.utils.CropBlockGetter;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootTable;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public final class IRCropsProvider {
    public static void bootstrap(BootstrapContext<Crop> context) {
        register("stick_reed", crop(4, Items.SUGAR_CANE, models -> {
            models.put(0, cropBlockModel("weed"));
        }, IRLootTables.STICK_REED), context);
        register("wheat", cropFromBlock(Blocks.WHEAT), context);
    }

    private static ResourceLocation cropBlockModel(String fileName) {
        return IndustrialRecrafted.rl("block/crop/" + fileName);
    }

    private static void register(String key, Crop crop, BootstrapContext<Crop> context) {
        context.register(ResourceKey.create(IRRegistries.CROP, IndustrialRecrafted.rl(key)), crop);
    }

    private static Crop crop(int stages, @Nullable Item seedItem, Consumer<Int2ObjectMap<ResourceLocation>> modelsForAgesFunction, ResourceKey<LootTable> lootTable) {
        Int2ObjectMap<ResourceLocation> modelsForAges = new Int2ObjectOpenHashMap<>();
        modelsForAgesFunction.accept(modelsForAges);
        Map<String, ResourceLocation> models = new HashMap<>();
        modelsForAges.forEach((k, v) -> {
            models.put("age=" + k, v);
        });
        return new Crop(stages, Optional.ofNullable(seedItem), new Crop.Models.ForAge(models), Either.right(lootTable));
    }

    private static Crop crop(int stages, @Nullable Item seedItem, Block modelBase, IntegerProperty ageProperty, ResourceKey<LootTable> lootTable) {
        return new Crop(stages, Optional.ofNullable(seedItem), new Crop.Models.BlockModel(modelBase, ageProperty.getName()), Either.right(lootTable));
    }

    private static Crop cropFromBlock(Block modelBlock) {
        if (modelBlock instanceof CropBlock cropBlock && modelBlock instanceof CropBlockGetter cropBlockGetter) {
            int stages = cropBlock.getMaxAge();
            Optional<Item> seedItem = Optional.of(cropBlockGetter.indrec$getSeedItem().asItem());
            ResourceLocation key = BuiltInRegistries.BLOCK.getKey(modelBlock);
            return new Crop(stages, seedItem, new Crop.Models.BlockModel(modelBlock, cropBlockGetter.indrec$getAgeProperty().getName()), Either.right(ResourceKey.create(Registries.LOOT_TABLE, key.withPrefix("block/"))));
        }
        return null;
    }

}
