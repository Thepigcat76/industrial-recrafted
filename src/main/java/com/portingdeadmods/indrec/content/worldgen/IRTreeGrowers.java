package com.portingdeadmods.indrec.content.worldgen;

import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class IRTreeGrowers {
    public static final TreeGrower RUBBER = new TreeGrower("rubber",
            Optional.empty(),
            Optional.of(IRWorldgenKeys.RUBBER_TREE_KEY.configuredFeature()),
            Optional.empty());
}
