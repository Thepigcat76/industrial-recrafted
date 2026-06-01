package com.portingdeadmods.indrec.api.crops;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public record Crop(int stages, Optional<Item> seedItem, Models models,
                   Either<LootTable, ResourceKey<LootTable>> loot) {
    public static final Codec<Crop> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.INT.fieldOf("stages").forGetter(Crop::stages),
            BuiltInRegistries.ITEM.byNameCodec().optionalFieldOf("seed_item").forGetter(Crop::seedItem),
            Models.CODEC.optionalFieldOf("models", Models.EMPTY).forGetter(Crop::models),
            Codec.either(LootTable.DIRECT_CODEC, ResourceKey.codec(Registries.LOOT_TABLE)).fieldOf("loot").forGetter(Crop::loot)
    ).apply(inst, Crop::new));
    public static final StreamCodec<? super RegistryFriendlyByteBuf, Crop> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

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
