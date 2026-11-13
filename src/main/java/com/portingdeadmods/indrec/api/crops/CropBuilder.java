package com.portingdeadmods.indrec.api.crops;

import com.mojang.datafixers.util.Either;
import com.portingdeadmods.indrec.api.crops.requirements.CropRequirement;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Builder for {@link Crop}s. The stages, models and loot are required, everything else is optional:
 * <ul>
 *     <li>seed: none, the crop can't be planted</li>
 *     <li>seed drop: none, the seed is dropped when removing the crop</li>
 *     <li>can harvest: true</li>
 *     <li>requirements: none</li>
 *     <li>mutations: none</li>
 *     <li>interactions: none</li>
 * </ul>
 */
public final class CropBuilder {
    private final int stages;
    private final Crop.Models models;
    private final Optional<Either<LootTable, ResourceKey<LootTable>>> loot;
    private @Nullable Item seed;
    private @Nullable SeedDrop seedDrop;
    private boolean canHarvest = true;
    private final List<CropRequirement> requirements = new ArrayList<>();
    private final List<CropMutation> mutations = new ArrayList<>();
    private final List<CropInteraction> interactions = new ArrayList<>();

    private CropBuilder(int stages, Crop.Models models, Optional<Either<LootTable, ResourceKey<LootTable>>> loot) {
        this.stages = stages;
        this.models = models;
        this.loot = loot;
    }

    public static CropBuilder of(int stages, Crop.Models models, ResourceKey<LootTable> lootTable) {
        return new CropBuilder(stages, models, Optional.of(Either.right(lootTable)));
    }

    public static CropBuilder of(int stages, Crop.Models models, LootTable lootTable) {
        return new CropBuilder(stages, models, Optional.of(Either.left(lootTable)));
    }

    /**
     * Creates a crop without loot
     */
    public static CropBuilder of(int stages, Crop.Models models) {
        return new CropBuilder(stages, models, Optional.empty());
    }

    public CropBuilder seedItem(ItemLike seedItem) {
        this.seed = seedItem.asItem();
        return this;
    }

    /**
     * @param seedDrop dropped instead of the seed when the crop is removed
     * @param chance   chance (0-1) for the seed drop to drop
     */
    public CropBuilder seedDrop(ItemStack seedDrop, float chance) {
        this.seedDrop = new SeedDrop(seedDrop.copy(), chance);
        return this;
    }

    public CropBuilder seedDrop(ItemLike seedDrop, float chance) {
        return this.seedDrop(new ItemStack(seedDrop), chance);
    }

    public CropBuilder seedDrop(ItemStack seedDrop) {
        return this.seedDrop(seedDrop, 1);
    }

    public CropBuilder seedDrop(ItemLike seedDrop) {
        return this.seedDrop(new ItemStack(seedDrop), 1);
    }

    public CropBuilder canHarvest(boolean canHarvest) {
        this.canHarvest = canHarvest;
        return this;
    }

    public CropBuilder requirements(CropRequirement... requirements) {
        this.requirements.addAll(List.of(requirements));
        return this;
    }

    public CropBuilder mutations(CropMutation... mutations) {
        this.mutations.addAll(List.of(mutations));
        return this;
    }

    public CropBuilder interactions(CropInteraction... interactions) {
        this.interactions.addAll(List.of(interactions));
        return this;
    }

    public Crop build() {
        return new Crop(this.stages, Optional.ofNullable(this.seed), Optional.ofNullable(this.seedDrop), this.canHarvest,
                List.copyOf(this.requirements), List.copyOf(this.mutations), List.copyOf(this.interactions), this.models, this.loot);
    }
}
