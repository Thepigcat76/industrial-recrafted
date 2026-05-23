package com.portingdeadmods.indrec.client.screens.widgets;

import com.portingdeadmods.indrec.IRCapabilities;
import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.impl.energy.IRGenericEnergyWrapper;
import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.capabilities.EnergyStorageWrapper;
import com.portingdeadmods.portingdeadlibs.api.capabilities.NeoEnergyStorageWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.Capabilities;

public class IREnergyBarWidget extends AbstractWidget {
    private static final Sprites VERTICAL_SPRITES = new Sprites(
            PortingDeadLibs.rl("energy_bar_empty"),
            PortingDeadLibs.rl("energy_bar"),
            PortingDeadLibs.rl("energy_bar_empty_border"),
            PortingDeadLibs.rl("energy_bar_border")
    );
    private static final Sprites HORIZONTAL_SPRITES = new Sprites(
            IndustrialRecrafted.rl("horizontal_energy_bar_empty"),
            IndustrialRecrafted.rl("horizontal_energy_bar"),
            IndustrialRecrafted.rl("horizontal_energy_bar_empty_border"),
            IndustrialRecrafted.rl("horizontal_energy_bar_border")
    );

    private final EnergyStorageWrapper wrapper;
    private final String energyUnit;
    private Orientation orientation;
    private boolean hasBorder;

    public IREnergyBarWidget(int x, int y, EnergyStorageWrapper wrapper, String energyUnit) {
        super(x, y, 12, 48, CommonComponents.EMPTY);
        this.wrapper = wrapper;
        this.energyUnit = energyUnit;
        this.orientation = Orientation.VERTICAL;
    }

    public static IREnergyBarWidget widgetForFE(int x, int y, ContainerBlockEntity blockEntity) {
        return new IREnergyBarWidget(x, y, new IRGenericEnergyWrapper(blockEntity.getHandler(IRCapabilities.ENERGY_BLOCK)), "EU");
    }

    public static IREnergyBarWidget widgetForEU(int x, int y, ContainerBlockEntity blockEntity) {
        return new IREnergyBarWidget(x, y, new NeoEnergyStorageWrapper(blockEntity.getHandler(Capabilities.EnergyStorage.BLOCK)), "FE");
    }

    public IREnergyBarWidget setHasBorder(boolean hasBorder) {
        this.hasBorder = hasBorder;
        return this;
    }

    public IREnergyBarWidget setOrientation(Orientation orientation) {
        this.orientation = orientation;
        if (this.orientation == Orientation.HORIZONTAL) {
            setSize(48, 12);
        } else {
            setSize(12, 48);
        }
        return this;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        ResourceLocation loc = (this.orientation == Orientation.HORIZONTAL ? HORIZONTAL_SPRITES.getEmptySprite(this.hasBorder) : VERTICAL_SPRITES.getFullSprite(this.hasBorder));
        guiGraphics.blitSprite(loc, this.width, this.height, 0, 0, this.getX(), this.getY(), this.width, this.height);
        int energyStored = this.wrapper.getEnergyStored();
        int maxStored = this.wrapper.getEnergyCapacity();
        int maxProgress = this.orientation == Orientation.HORIZONTAL ? this.width : this.height;
        int progress = (int)(((float)energyStored / (float)maxStored) * maxProgress);
        ResourceLocation locFull = (this.orientation == Orientation.HORIZONTAL ? HORIZONTAL_SPRITES.getFullSprite(this.hasBorder) : VERTICAL_SPRITES.getEmptySprite(this.hasBorder));

        int uPos = 0;
        int vPos = 0;
        int uWidth = this.width;
        int vHeight = this.height;
        if (orientation == Orientation.VERTICAL) {
            vHeight = maxProgress - progress;
        }
        if (orientation == Orientation.HORIZONTAL) {
            uWidth = progress;
        }

        guiGraphics.blitSprite(locFull, this.width, this.height, uPos, vPos, this.getX(), this.getY(), uWidth, vHeight);

        if (this.isHovered()) {
            Font font = Minecraft.getInstance().font;
            int stored = this.wrapper.getEnergyStored();
            guiGraphics.renderTooltip(font, Component.literal(stored + "/" + this.wrapper.getEnergyCapacity() + this.energyUnit), mouseX, mouseY);
        }

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

    public enum Orientation {
        HORIZONTAL,
        VERTICAL,
    }

    private record Sprites(ResourceLocation emptySprite, ResourceLocation fullSprite, ResourceLocation emptyBorderSprite, ResourceLocation fullBorderSprite) {
        public ResourceLocation getEmptySprite(boolean hasBorder) {
            return hasBorder ? emptyBorderSprite : emptySprite;
        }

        public ResourceLocation getFullSprite(boolean hasBorder) {
            return hasBorder ? fullBorderSprite : fullSprite;
        }

    }

}
