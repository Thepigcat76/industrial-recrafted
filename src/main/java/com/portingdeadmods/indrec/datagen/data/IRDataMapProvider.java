package com.portingdeadmods.indrec.datagen.data;

import net.neoforged.neoforge.registries.datamaps.builtin.Waxable;
import net.neoforged.neoforge.registries.datamaps.builtin.Oxidizable;
import com.portingdeadmods.indrec.data.maps.IRDataMaps;
import com.portingdeadmods.indrec.data.maps.MatterFabricatorAmplifier;
import com.portingdeadmods.indrec.registries.IRBlocks;
import com.portingdeadmods.indrec.registries.IRItems;
import net.minecraft.core.HolderLookup;
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

        builder(NeoForgeDataMaps.OXIDIZABLES)
                .add(IRBlocks.COPRIUM, new Oxidizable(IRBlocks.EXPOSED_COPRIUM.get()), false)
                .add(IRBlocks.EXPOSED_COPRIUM, new Oxidizable(IRBlocks.WEATHERED_COPRIUM.get()), false)
                .add(IRBlocks.WEATHERED_COPRIUM, new Oxidizable(IRBlocks.OXIDIZED_COPRIUM.get()), false);

        builder(NeoForgeDataMaps.WAXABLES)
                .add(IRBlocks.COPRIUM, new Waxable(IRBlocks.WAXED_COPRIUM.get()), false)
                .add(IRBlocks.EXPOSED_COPRIUM, new Waxable(IRBlocks.WAXED_EXPOSED_COPRIUM.get()), false)
                .add(IRBlocks.WEATHERED_COPRIUM, new Waxable(IRBlocks.WAXED_WEATHERED_COPRIUM.get()), false)
                .add(IRBlocks.OXIDIZED_COPRIUM, new Waxable(IRBlocks.WAXED_OXIDIZED_COPRIUM.get()), false);

        builder(IRDataMaps.MATTER_FABRICATOR_AMPLIFIERS)
                .add(IRItems.SCRAP, new MatterFabricatorAmplifier(0.015f), false)
                .add(IRItems.SCRAP_BOX, new MatterFabricatorAmplifier(0.15f), false);
    }

}
