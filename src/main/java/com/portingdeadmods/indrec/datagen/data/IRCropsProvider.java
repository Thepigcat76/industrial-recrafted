package com.portingdeadmods.indrec.datagen.data;

import com.portingdeadmods.indrec.api.crops.CropMutation;
import com.portingdeadmods.indrec.content.crops.requirements.BlockBelowSoilRequirement;
import com.portingdeadmods.indrec.content.crops.requirements.LightLevelRequirement;
import com.portingdeadmods.indrec.registries.IRBlocks;
import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.api.crops.Crop;
import com.portingdeadmods.indrec.api.crops.CropBuilder;
import com.portingdeadmods.indrec.api.crops.CropInteraction;
import com.portingdeadmods.indrec.content.crops.requirements.SoilRequirement;
import com.portingdeadmods.indrec.registries.IRCrops;
import com.portingdeadmods.indrec.registries.IRItems;
import com.portingdeadmods.indrec.registries.IRLootTables;
import com.portingdeadmods.indrec.tags.CTags;
import com.portingdeadmods.indrec.utils.CropBlockGetter;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.Items;
import java.util.Arrays;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import net.neoforged.neoforge.common.crafting.CompoundIngredient;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.Holder;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.common.Tags;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class IRCropsProvider {
    public static void bootstrap(BootstrapContext<Crop> context) {
        register(context, IRCrops.STICK_REED, CropBuilder.of(4, modelsForAge(models -> {
                    models.put(0, cropBlockModel("stick_reed"));
                    models.put(1, cropBlockModel("stick_reed"));
                    models.put(2, cropBlockModel("stick_reed"));
                    models.put(3, cropBlockModel("stick_reed"));
                }), IRLootTables.STICK_REED));
        register(context, IRCrops.WEED, CropBuilder.of(5, modelsForAge(models -> {
                    models.put(0, cropBlockModel("weed_stage_0"));
                    models.put(1, cropBlockModel("weed_stage_1"));
                    models.put(2, cropBlockModel("weed_stage_2"));
                    models.put(3, cropBlockModel("weed_stage_3"));
                    models.put(4, cropBlockModel("weed_stage_4"));
                }), IRLootTables.WEED)
                .canHarvest(false)
                .seedDrop(IRItems.PLANT_BALL, 0.2f));
        register(context, IRCrops.GOLDEN_DANDELION, CropBuilder.of(5, modelsForAge(models -> {
                    models.put(0, cropBlockModel("golden_dandelion_stage0"));
                    models.put(1, cropBlockModel("golden_dandelion_stage1"));
                    models.put(2, cropBlockModel("golden_dandelion_stage2"));
                    models.put(3, cropBlockModel("golden_dandelion_stage3"));
                    models.put(4, cropBlockModel("golden_dandelion_stage4"));
                }), IRLootTables.GOLDEN_DANDELION)
                .requirements(BlockBelowSoilRequirement.of(Tags.Blocks.ORES_GOLD))
                .seedItem(IRBlocks.GOLDEN_DANDELION));
        register(context, IRCrops.IRON_VINES, CropBuilder.of(7, modelsForAge(models -> {
                    for (int age = 0; age < 7; age++) {
                        models.put(age, cropBlockModel("iron_vines_stage" + age));
                    }
                }), IRLootTables.IRON_VINES)
                .requirements(BlockBelowSoilRequirement.of(Tags.Blocks.ORES_IRON))
                .seedItem(IRBlocks.IRON_VINES));
        register(context, IRCrops.TIN_BLOSSOM, CropBuilder.of(6, modelsForAge(models -> {
                    for (int age = 0; age < 6; age++) {
                        models.put(age, cropBlockModel("tin_blossom_stage" + age));
                    }
                }), IRLootTables.TIN_BLOSSOM)
                .requirements(BlockBelowSoilRequirement.of(CTags.BlockTags.ORES_TIN))
                .seedItem(IRBlocks.TIN_BLOSSOM));
        register(context, IRCrops.COPRIUM, copriumCrop(IRBlocks.COPRIUM)
                .mutations(new CropMutation(IRCrops.EXPOSED_COPRIUM, 0.1f, true))
                .interactions(CropInteraction.of(Ingredient.of(Items.HONEYCOMB), IRCrops.WAXED_COPRIUM)
                        .withSound(SoundEvents.HONEYCOMB_WAX_ON)));
        // Same as coprium, but doesn't oxidize
        register(context, IRCrops.WAXED_COPRIUM, copriumCrop(IRBlocks.WAXED_COPRIUM));
        register(context, IRCrops.EXPOSED_COPRIUM, oxidizedCopriumCrop("exposed_coprium", IRBlocks.EXPOSED_COPRIUM, IRCrops.WEATHERED_COPRIUM));
        register(context, IRCrops.WEATHERED_COPRIUM, oxidizedCopriumCrop("weathered_coprium", IRBlocks.WEATHERED_COPRIUM, IRCrops.OXIDIZED_COPRIUM));
        register(context, IRCrops.OXIDIZED_COPRIUM, oxidizedCopriumCrop("oxidized_coprium", IRBlocks.OXIDIZED_COPRIUM, null));
        register(context, IRCrops.WITHER_ROSE, CropBuilder.of(5, modelsForAge(models -> {
                    for (int age = 0; age < 5; age++) {
                        models.put(age, cropBlockModel("wither_rose_stage" + age));
                    }
                }), IRLootTables.WITHER_ROSE)
                .requirements(SoilRequirement.of(Blocks.SOUL_SAND))
                .seedItem(Items.WITHER_ROSE));
        register(context, IRCrops.DANDELION, CropBuilder.of(5, modelsForAge(models -> {
                    for (int age = 0; age < 5; age++) {
                        models.put(age, cropBlockModel("dandelion_stage" + age));
                    }
                }), IRLootTables.DANDELION)
                .seedItem(Items.DANDELION));
        register(context, IRCrops.POPPY, CropBuilder.of(5, modelsForAge(models -> {
                    for (int age = 0; age < 5; age++) {
                        models.put(age, cropBlockModel("poppy_stage" + age));
                    }
                }), IRLootTables.POPPY)
                .interactions(CropInteraction.of(potionIngredient(Potions.WEAKNESS, Potions.LONG_WEAKNESS), IRCrops.WITHER_ROSE)
                        .withRemainder(Items.GLASS_BOTTLE)
                        .withSound(SoundEvents.BOTTLE_EMPTY))
                .seedItem(Items.POPPY));
        register(context, IRCrops.ENDER_LILY, CropBuilder.of(8, modelsForAge(models -> {
                    for (int age = 0; age < 8; age++) {
                        models.put(age, cropBlockModel("ender_lily_stage" + age));
                    }
                }), IRLootTables.ENDER_LILY)
                .requirements(SoilRequirement.of(Blocks.END_STONE))
                .seedItem(IRBlocks.ENDER_LILY));
        register(context, IRCrops.RED_MUSHROOM, CropBuilder.of(4, modelsForAge(models -> {
                    for (int age = 0; age < 4; age++) {
                        models.put(age, cropBlockModel("red_mushroom_stage" + age));
                    }
                }), IRLootTables.RED_MUSHROOM)
                .requirements(LightLevelRequirement.max(7))
                .seedItem(Items.RED_MUSHROOM));
        register(context, IRCrops.BROWN_MUSHROOM, CropBuilder.of(4, modelsForAge(models -> {
                    for (int age = 0; age < 4; age++) {
                        models.put(age, cropBlockModel("brown_mushroom_stage" + age));
                    }
                }), IRLootTables.BROWN_MUSHROOM)
                .requirements(LightLevelRequirement.max(7))
                .seedItem(Items.BROWN_MUSHROOM));
        register(context, IRCrops.MELON, stemCrop("melon_crop", Items.MELON_SEEDS, Blocks.MELON.getLootTable()));
        register(context, IRCrops.PUMPKIN, stemCrop("pumpkin_crop", Items.PUMPKIN_SEEDS, Blocks.PUMPKIN.getLootTable()));
        register(context, IRCrops.WHEAT, cropFromBlock(Blocks.WHEAT));
        register(context, IRCrops.CARROTS, cropFromBlock(Blocks.CARROTS));
        register(context, IRCrops.POTATOES, cropFromBlock(Blocks.POTATOES));
        register(context, IRCrops.BEETROOTS, cropFromBlock(Blocks.BEETROOTS));
        // Not a CropBlock, but it has an age property, so it can use its own model and loot table like the other vanilla crops
        register(context, IRCrops.NETHER_WART, CropBuilder.of(NetherWartBlock.MAX_AGE + 1,
                        new Crop.Models.BlockModel(Blocks.NETHER_WART, NetherWartBlock.AGE.getName()),
                        Blocks.NETHER_WART.getLootTable())
                .requirements(SoilRequirement.of(Blocks.SOUL_SAND))
                .seedItem(Items.NETHER_WART));
    }

    /**
     * @return an ingredient matching drinkable potions of any of the given potion types
     */
    @SafeVarargs
    private static Ingredient potionIngredient(Holder<Potion>... potions) {
        return CompoundIngredient.of(Arrays.stream(potions)
                .map(potion -> DataComponentIngredient.of(false, DataComponents.POTION_CONTENTS, new PotionContents(potion), Items.POTION))
                .toArray(Ingredient[]::new));
    }

    private static void register(BootstrapContext<Crop> context, ResourceKey<Crop> key, CropBuilder builder) {
        context.register(key, builder.build());
    }

    /**
     * Unaffected coprium crop without mutations
     */
    private static CropBuilder copriumCrop(ItemLike seed) {
        return CropBuilder.of(7, modelsForAge(models -> {
                    for (int age = 0; age < 7; age++) {
                        models.put(age, cropBlockModel("coprium_stage_" + age));
                    }
                }), IRLootTables.COPRIUM)
                .requirements(BlockBelowSoilRequirement.of(Tags.Blocks.ORES_COPPER))
                .seedItem(seed);
    }

    private static CropBuilder oxidizedCopriumCrop(String name, ItemLike seedDrop, ResourceKey<Crop> nextStage) {
        CropBuilder cropBuilder = CropBuilder.of(7, modelsForAge(models -> {
                    for (int age = 0; age < 7; age++) {
                        models.put(age, cropBlockModel(name + "_stage_" + age));
                    }
                }))
                .requirements(BlockBelowSoilRequirement.of(Tags.Blocks.ORES_COPPER))
                .canHarvest(false)
                .seedDrop(seedDrop);
        if (nextStage != null)
            cropBuilder.mutations(new CropMutation(nextStage, 0.1f, true));

        return cropBuilder;
    }

    private static CropBuilder stemCrop(String modelPrefix, ItemLike seed, ResourceKey<LootTable> lootTable) {
        return CropBuilder.of(4, modelsForAge(models -> {
                    models.put(0, cropBlockModel(modelPrefix + "_stem_0"));
                    models.put(1, cropBlockModel(modelPrefix + "_stem_1"));
                    models.put(2, cropBlockModel(modelPrefix + "_stem_2"));
                    models.put(3, cropBlockModel(modelPrefix + "_grown"));
                }), lootTable)
                .seedItem(seed);
    }

    private static ResourceLocation cropBlockModel(String fileName) {
        return IndustrialRecrafted.rl("block/crop/" + fileName);
    }

    private static Crop.Models modelsForAge(Consumer<Int2ObjectMap<ResourceLocation>> modelsForAgesFunction) {
        Int2ObjectMap<ResourceLocation> modelsForAges = new Int2ObjectOpenHashMap<>();
        modelsForAgesFunction.accept(modelsForAges);
        Map<String, ResourceLocation> models = new HashMap<>();
        modelsForAges.forEach((age, model) -> models.put("age=" + age, model));
        return new Crop.Models.ForAge(models);
    }

    private static CropBuilder cropFromBlock(Block modelBlock) {
        if (!(modelBlock instanceof CropBlock cropBlock) || !(modelBlock instanceof CropBlockGetter cropBlockGetter)) {
            throw new IllegalArgumentException("Block " + BuiltInRegistries.BLOCK.getKey(modelBlock) + " is not a crop block");
        }

        return CropBuilder.of(cropBlock.getMaxAge() + 1,
                        new Crop.Models.BlockModel(modelBlock, cropBlockGetter.indrec$getAgeProperty().getName()),
                        modelBlock.getLootTable()
                ).seedItem(cropBlockGetter.indrec$getSeedItem());
    }

}
