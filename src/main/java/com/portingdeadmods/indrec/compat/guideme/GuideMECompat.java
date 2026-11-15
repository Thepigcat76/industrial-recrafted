package com.portingdeadmods.indrec.compat.guideme;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;

/**
 * GuideME is an optional dependency. This class must not reference any GuideME classes, so it can be loaded without GuideME installed.
 * Only use {@link IRGuide} after checking {@link #isLoaded()}.
 */
public final class GuideMECompat {
    public static final String MODID = "guideme";
    public static final ResourceLocation GUIDE_ID = IndustrialRecrafted.rl("guide");
    public static final String GUIDE_NAME_KEY = "item." + IndustrialRecrafted.MODID + ".guide";

    private GuideMECompat() {
    }

    public static boolean isLoaded() {
        return ModList.get().isLoaded(MODID);
    }
}
