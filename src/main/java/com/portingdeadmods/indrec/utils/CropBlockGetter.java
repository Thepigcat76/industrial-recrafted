package com.portingdeadmods.indrec.utils;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public interface CropBlockGetter {
    ItemLike indrec$getSeedItem();

    IntegerProperty indrec$getAgeProperty();

}
