package com.portingdeadmods.indrec.data.maps;

import com.mojang.serialization.Codec;
import com.portingdeadmods.indrec.IndustrialRecrafted;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

public final class IRDataMaps {
    public static final DataMapType<Item, MatterFabricatorAmplifier> MATTER_FABRICATOR_AMPLIFIERS = DataMapType.builder(IndustrialRecrafted.rl("matter_fabricator_amplifiers"), Registries.ITEM, MatterFabricatorAmplifier.CODEC)
            .synced(MatterFabricatorAmplifier.CODEC, false)
            .build();
}
