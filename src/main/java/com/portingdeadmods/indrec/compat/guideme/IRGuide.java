package com.portingdeadmods.indrec.compat.guideme;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import guideme.Guide;
import guideme.GuideItemSettings;
import guideme.Guides;
import guideme.compiler.TagCompiler;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

/**
 * References GuideME classes, only use it after checking {@link GuideMECompat#isLoaded()}
 */
public final class IRGuide {
    public static final ResourceLocation ID = GuideMECompat.GUIDE_ID;
    public static final String NAME_KEY = GuideMECompat.GUIDE_NAME_KEY;

    private IRGuide() {
    }

    /**
     * Registers the guide, must only be called on the client.
     * The guide is registered in code instead of a data-driven guide json so it can provide custom tag compilers.
     */
    public static void register() {
        Guide.builder(ID)
                .itemSettings(new GuideItemSettings(
                        Optional.of(Component.translatable(NAME_KEY)),
                        List.of(),
                        Optional.of(IndustrialRecrafted.rl("item/guide"))
                ))
                .extension(TagCompiler.EXTENSION_POINT, new CropListTagCompiler())
                .build();
    }

    public static ItemStack createGuideItem() {
        return Guides.createGuideItem(ID);
    }
}
