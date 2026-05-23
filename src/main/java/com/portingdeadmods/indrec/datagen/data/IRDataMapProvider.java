package com.portingdeadmods.indrec.datagen.data;

import com.portingdeadmods.indrec.data.maps.IRDataMaps;
import com.portingdeadmods.indrec.registries.IRBlocks;
import com.portingdeadmods.indrec.registries.IRItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.concurrent.CompletableFuture;

public final class IRDataMapProvider extends DataMapProvider {
    public IRDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        builder(NeoForgeDataMaps.COMPOSTABLES)
                .add(IRItems.PLANT_BALL, new Compostable(0.85f), false)
                .add(IRBlocks.RUBBER_TREE_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.3f), false)
                .add(IRBlocks.RUBBER_TREE_SAPLING.asItem().builtInRegistryHolder(), new Compostable(0.3f), false);

        builder(IRDataMaps.MATTER_FABRICATOR_AMPLIFIERS)
                .add(IRItems.SCRAP, 0.1f, false)
                .add(IRItems.SCRAP_BOX, 1f, false);
    }

}
