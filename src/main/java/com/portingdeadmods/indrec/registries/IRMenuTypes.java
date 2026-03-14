package com.portingdeadmods.indrec.registries;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class IRMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, IndustrialRecrafted.MODID);
}
