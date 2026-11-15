package com.portingdeadmods.indrec.registries;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import net.minecraft.core.registries.BuiltInRegistries;
import com.portingdeadmods.indrec.content.menus.ToolboxFilterMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class IRMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, IndustrialRecrafted.MODID);

    public static final Supplier<MenuType<ToolboxFilterMenu>> TOOLBOX = MENU_TYPES.register("toolbox", () -> IMenuTypeExtension.create(ToolboxFilterMenu::new));
}
