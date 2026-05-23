package com.portingdeadmods.indrec.impl.recipes;

import com.portingdeadmods.indrec.api.recipes.MachineRecipeLayout;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;

import java.util.HashMap;
import java.util.Map;

public class RegisterRecipeLayoutEvent extends Event implements IModBusEvent {
    public static final Map<ResourceLocation, MachineRecipeLayout<?>> LAYOUTS = new HashMap<>();

    public void register(ResourceLocation id, MachineRecipeLayout<?> layout) {
        LAYOUTS.put(id, layout);
    }
}
