package com.portingdeadmods.indrec.api.blockentities;

import net.minecraft.world.entity.player.Player;

public interface WrenchListenerBlockEntity {
    void beforeRemoveByWrench(Player player);
}
