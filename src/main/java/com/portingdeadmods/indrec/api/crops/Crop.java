package com.portingdeadmods.indrec.api.crops;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import com.portingdeadmods.indrec.api.crops.requirements.CropRequirement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @param seed         the item used to plant (and returned when removing) the crop, if absent the crop can't be planted.
 * @param seedDrop     dropped (with a chance) instead of the seed when the crop is removed (e.g. using shears), if absent the seed is dropped
 * @param canHarvest   whether the crop can be harvested using a hoe once fully grown
 * @param requirements requirements that all have to be met for the crop to grow
 * @param mutations    crops this crop can mutate into
 * @param interactions items that can be used on the crop to turn it into another crop
 * @param loot         dropped when the crop is harvested or broken, if absent the crop drops nothing besides its seed (drop)
 */
public record Crop(int stages, Optional<Item> seed, Optional<SeedDrop> seedDrop, boolean canHarvest,
                   List<CropRequirement> requirements, List<CropMutation> mutations, List<CropInteraction> interactions, Models models,
                   Optional<Either<LootTable, ResourceKey<LootTable>>> loot) {
    public static final Codec<Crop> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.INT.fieldOf("stages").forGetter(Crop::stages),
            BuiltInRegistries.ITEM.byNameCodec().optionalFieldOf("seed").forGetter(Crop::seed),
            SeedDrop.CODEC.optionalFieldOf("seed_drop").forGetter(Crop::seedDrop),
            Codec.BOOL.optionalFieldOf("can_harvest", true).forGetter(Crop::canHarvest),
            CropRequirement.CODEC.listOf().optionalFieldOf("requirements", List.of()).forGetter(Crop::requirements),
            CropMutation.CODEC.listOf().optionalFieldOf("mutations", List.of()).forGetter(Crop::mutations),
            CropInteraction.CODEC.listOf().optionalFieldOf("interactions", List.of()).forGetter(Crop::interactions),
            Models.CODEC.optionalFieldOf("models", Models.EMPTY).forGetter(Crop::models),
            Codec.either(LootTable.DIRECT_CODEC, ResourceKey.codec(Registries.LOOT_TABLE)).optionalFieldOf("loot").forGetter(Crop::loot)
    ).apply(inst, Crop::new));
    public static final StreamCodec<? super RegistryFriendlyByteBuf, Crop> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

    public Optional<ItemStack> createSeedDrop(RandomSource random) {
        if (this.seedDrop.isPresent()) {
            return this.seedDrop.get().drops(random);
        }
        return this.createSeedStack();
    }

    public Optional<ItemStack> createSeedStack() {
        return this.seed.map(ItemStack::new);
    }

    public boolean isSeed(ItemStack stack) {
        if (this.seed.isEmpty() || !stack.is(this.seed.get())) return false;
        return this.seed.filter(stack::is).isPresent();
    }

    /**
     * @param self the holder of this crop
     * @return whether all requirements of this crop are met at the given position
     */
    public boolean meetsRequirements(Holder<Crop> self, LevelReader level, BlockPos pos) {
        for (CropRequirement requirement : this.requirements) {
            if (!requirement.test(self, level, pos)) return false;
        }
        return true;
    }

    public int maxStage() {
        return this.stages() - 1;
    }

    public static String getTranslationKey(ResourceKey<Crop> key) {
        return Util.makeDescriptionId("crop", key.location());
    }

    public interface Models {
        Codec<Models> CODEC = Codec.STRING.dispatch(Models::type, str -> switch (str) {
            case BlockModel.TYPE -> BlockModel.CODEC;
            case ForAge.TYPE -> ForAge.CODEC;
            default -> null;
        });
        Models EMPTY = new ForAge(Map.of());

        String type();

        MapCodec<? extends Models> codec();

        record BlockModel(Block baseBlock, String ageProperty) implements Models {
            public static final String TYPE = "base_block";
            public static final MapCodec<Models.BlockModel> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                    BuiltInRegistries.BLOCK.byNameCodec().fieldOf("base_block").forGetter(BlockModel::baseBlock),
                    Codec.STRING.fieldOf("age_property").forGetter(BlockModel::ageProperty)
            ).apply(inst, BlockModel::new));

            @Override
            public String type() {
                return TYPE;
            }

            @Override
            public MapCodec<? extends Models> codec() {
                return CODEC;
            }
        }

        record ForAge(Map<String, ResourceLocation> modelsForAge) implements Models {
            public static final String TYPE = "models_for_age";
            public static final String MODEL_CROP_DIRECTORY = "block/crop/";
            public static final MapCodec<Models.ForAge> CODEC = MapCodec.assumeMapUnsafe(
                    Codec.unboundedMap(Codec.STRING, ResourceLocation.CODEC
                                    .validate(loc -> loc.getPath().equals(TYPE) || loc.getPath().startsWith(MODEL_CROP_DIRECTORY) ? DataResult.success(loc) : DataResult.error(() -> errorMessage(loc))))
                            .xmap(ForAge::new, ForAge::modelsForAge)
            );

            public ForAge(Map<String, ResourceLocation> modelsForAge) {
                Map<String, ResourceLocation> temp = new HashMap<>(modelsForAge);
                temp.remove("type");
                this.modelsForAge = Map.copyOf(temp);
            }

            @Override
            public String type() {
                return TYPE;
            }

            @Override
            public MapCodec<? extends Models> codec() {
                return CODEC;
            }

            private static String errorMessage(ResourceLocation loc) {
                return "Model " + loc + " for crop is not valid, since its not in the " + MODEL_CROP_DIRECTORY + " directory";
            }
        }

    }

}
