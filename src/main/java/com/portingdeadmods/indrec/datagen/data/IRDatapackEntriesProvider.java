package com.portingdeadmods.indrec.datagen.data;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class IRDatapackEntriesProvider extends DatapackBuiltinEntriesProvider {

    public IRDatapackEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(IndustrialRecrafted.MODID));
    }

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, IRWorldgenProvider::bootstrapConfiguredFeatures)
            .add(Registries.PLACED_FEATURE, IRWorldgenProvider::bootstrapPlacedFeatures)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, IRBiomeModifiers::bootstrapBiomeModifiers);
}