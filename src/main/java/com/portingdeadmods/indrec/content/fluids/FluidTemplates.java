package com.portingdeadmods.indrec.content.fluids;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.portingdeadlibs.api.fluids.FluidTemplate;
import net.minecraft.resources.ResourceLocation;

public enum FluidTemplates implements FluidTemplate {
    WATER(ResourceLocation.parse("block/water_still"),
            ResourceLocation.parse("block/water_flow"),
            ResourceLocation.fromNamespaceAndPath(IndustrialRecrafted.MODID, "misc/in_soap_water"));

    private final ResourceLocation still;
    private final ResourceLocation flowing;
    private final ResourceLocation overlay;

    FluidTemplates(ResourceLocation still, ResourceLocation flowing, ResourceLocation overlay) {
        this.still = still;
        this.flowing = flowing;
        this.overlay = overlay;
    }

    @Override
    public ResourceLocation getStillTexture() {
        return still;
    }

    @Override
    public ResourceLocation getFlowingTexture() {
        return flowing;
    }

    @Override
    public ResourceLocation getOverlayTexture() {
        return overlay;
    }
}