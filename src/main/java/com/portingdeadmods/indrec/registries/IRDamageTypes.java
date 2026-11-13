package com.portingdeadmods.indrec.registries;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public final class IRDamageTypes {
    public static final ResourceKey<DamageType> IRON_VINES = ResourceKey.create(Registries.DAMAGE_TYPE, IndustrialRecrafted.rl("iron_vines"));

    private IRDamageTypes() {
    }

    public static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(IRON_VINES, new DamageType(IndustrialRecrafted.MODID + ".iron_vines", 0.1F));
    }
}
