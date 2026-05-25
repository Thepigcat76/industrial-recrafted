package com.portingdeadmods.indrec.client.screens.widgets;

import com.portingdeadmods.indrec.IRCapabilities;
import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.impl.energy.IRGenericEnergyWrapper;
import com.portingdeadmods.indrec.utils.TooltipUtils;
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
            IndustrialRecrafted.rl("energy_bar/vertical_empty"),
            IndustrialRecrafted.rl("energy_bar/vertical_full"),
            IndustrialRecrafted.rl("energy_bar/vertical_empty_border"),
            IndustrialRecrafted.rl("energy_bar/vertical_full_border")
    );
    private static final Sprites HORIZONTAL_SPRITES = new Sprites(
            IndustrialRecrafted.rl("energy_bar/horizontal_empty"),
            IndustrialRecrafted.rl("energy_bar/horizontal_full"),
            IndustrialRecrafted.rl("energy_bar/horizontal_empty_border"),
            IndustrialRecrafted.rl("energy_bar/horizontal_full_border")
    );
    public static final int HORIZONTAL_WIDTH = 48;
    public static final int HORIZONTAL_HEIGHT = 12;

    public static final int VERTICAL_WIDTH = 12;
    public static final int VERTICAL_HEIGHT = 48;

    private final EnergyStorageWrapper wrapper;
    private final String energyUnit;
    private Orientation orientation;
    private boolean hasBorder;

    public IREnergyBarWidget(int x, int y, int width, int height, EnergyStorageWrapper wrapper, String energyUnit) {
        super(x, y, width, height, CommonComponents.EMPTY);
        this.wrapper = wrapper;
        this.energyUnit = energyUnit;
        this.orientation = Orientation.VERTICAL;
    }

    public static IREnergyBarWidget widgetForFE(int x, int y, int width, int height, ContainerBlockEntity blockEntity) {
        return new IREnergyBarWidget(x, y, width, height, new IRGenericEnergyWrapper(blockEntity.getHandler(IRCapabilities.ENERGY_BLOCK)), "EU");
    }

    public static IREnergyBarWidget widgetForEU(int x, int y, int width, int height, ContainerBlockEntity blockEntity) {
        return new IREnergyBarWidget(x, y, width, height, new NeoEnergyStorageWrapper(blockEntity.getHandler(Capabilities.EnergyStorage.BLOCK)), "FE");
    }

    public IREnergyBarWidget setHasBorder(boolean hasBorder) {
        this.hasBorder = hasBorder;
        return this;
    }

    public IREnergyBarWidget setOrientation(Orientation orientation) {
        this.orientation = orientation;
        return this;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        boolean horizontal = this.orientation == Orientation.HORIZONTAL;
        Sprites sprites = horizontal ? HORIZONTAL_SPRITES : VERTICAL_SPRITES;

        ResourceLocation locEmpty = sprites.getEmptySprite(this.hasBorder);
        guiGraphics.blitSprite(locEmpty, this.getX(), this.getY(), this.width, this.height);

        int energyStored = this.wrapper.getEnergyStored();
        int maxStored = this.wrapper.getEnergyCapacity();

        int maxProgress = horizontal ? this.width : this.height;
        int progress = (int)(((float)energyStored / (float)maxStored) * maxProgress);

        if (horizontal) {
            guiGraphics.enableScissor(this.getX(), this.getY(), this.getX() + progress, this.getY() + this.height);
        } else {
            guiGraphics.enableScissor(this.getX(), this.getY() + this.height - progress, this.getX() + this.width, this.getY() + this.height);
        }

        ResourceLocation locFull = sprites.getFullSprite(this.hasBorder);
        guiGraphics.blitSprite(locFull, this.getX(), this.getY(), this.width, this.height);

        guiGraphics.disableScissor();

        if (this.isHovered()) {
            Font font = Minecraft.getInstance().font;
            guiGraphics.renderTooltip(font, Component.literal(TooltipUtils.formatEnergy(energyStored) + "/" + TooltipUtils.formatEnergy(maxStored) + this.energyUnit), mouseX, mouseY);
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
