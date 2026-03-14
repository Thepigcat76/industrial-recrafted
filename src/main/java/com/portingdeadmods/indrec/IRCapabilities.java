package com.portingdeadmods.indrec;

import com.portingdeadmods.indrec.api.energy.EnergyHandler;
import com.portingdeadmods.indrec.api.reactor.ReactorComponent;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import org.jetbrains.annotations.Nullable;


public final class IRCapabilities {
    public static final BlockCapability<EnergyHandler, @Nullable Direction> ENERGY_BLOCK = BlockCapability.createSided(IndustrialRecrafted.rl("energy"), EnergyHandler.class);
    public static final ItemCapability<EnergyHandler, Void> ENERGY_ITEM = ItemCapability.createVoid(IndustrialRecrafted.rl("energy"), EnergyHandler.class);
    public static final ItemCapability<ReactorComponent, Void> REACTOR_ITEM = ItemCapability.createVoid(IndustrialRecrafted.rl("reactor"), ReactorComponent.class);

}