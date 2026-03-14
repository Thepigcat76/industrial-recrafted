package com.portingdeadmods.indrec.registries;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public final class IRLootTables {
    public static final ResourceKey<LootTable> SCRAP_BOX = ResourceKey.create(Registries.LOOT_TABLE, IndustrialRecrafted.rl("scrap_box"));
}
