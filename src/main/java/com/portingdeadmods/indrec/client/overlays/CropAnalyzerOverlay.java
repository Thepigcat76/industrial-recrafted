package com.portingdeadmods.indrec.client.overlays;

import com.portingdeadmods.indrec.api.crops.Crop;
import com.portingdeadmods.indrec.client.CycledItemRenderer;
import com.portingdeadmods.indrec.content.blockentities.CropBlockEntity;
import com.portingdeadmods.indrec.content.crops.requirements.LightLevelRequirement;
import com.portingdeadmods.indrec.content.crops.requirements.BlockBelowSoilRequirement;
import com.portingdeadmods.indrec.content.crops.requirements.SoilRequirement;
import com.portingdeadmods.indrec.content.items.electric.CropAnalyzerItem;
import com.portingdeadmods.indrec.networking.serverbound.RequestCropDropsPayload;
import com.portingdeadmods.indrec.registries.IRItems;
import com.portingdeadmods.indrec.registries.IRTranslations;
import com.portingdeadmods.indrec.utils.CropDrop;
import com.portingdeadmods.indrec.utils.CropDropsKey;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CropAnalyzerOverlay implements LayeredDraw.Layer {
    private static final int DROPS_RETRY_INTERVAL = 40;
    private static final int ITEM_SIZE = 18;
    private static final int DROP_TEXT_OFFSET = ITEM_SIZE + 2;
    /**
     * Height of the visible glyphs of the font, without the shadow and the line spacing
     */
    private static final int GLYPH_HEIGHT = 7;
    private static final int SECTION_SPACING = 4;
    private static final int HEADER_SPACING = 3;
    private static final int TAB_GAP = 6;
    private static final int TAB_BACKDROP_COLOR = 0x50FFFFFF;
    private static final String TAB_ARROW_LEFT = "◀";
    private static final String TAB_ARROW_RIGHT = "▶";
    private static final float COUNT_LABEL_SCALE = 0.75f;
    private static final int LINE_HEIGHT = 10;
    private static final int SECTION_HEIGHT = LINE_HEIGHT + ITEM_SIZE;
    private static final int PROGRESS_SQUARES = 10;
    private static final int PROGRESS_SQUARE_SIZE = 5;
    private static final int PROGRESS_SQUARE_GAP = 2;
    private static final int PROGRESS_BAR_WIDTH = PROGRESS_SQUARES * (PROGRESS_SQUARE_SIZE + PROGRESS_SQUARE_GAP) - PROGRESS_SQUARE_GAP;
    private static final int CROSSHAIR_OFFSET = 12;
    // Kept across frames, so the cycle continues while looking at the same crop
    private static final CycledItemRenderer BLOCK_BELOW_SOIL_RENDERER = new CycledItemRenderer();
    private static final CycledItemRenderer SOIL_RENDERER = new CycledItemRenderer();
    private static final LightLevelRequirement DEFAULT_LIGHT_REQUIREMENT =
            LightLevelRequirement.min(com.portingdeadmods.indrec.content.blocks.CropBlock.DEFAULT_MIN_LIGHT_LEVEL);
    private static long lastCycleTick = -1;
    private static Tab currentTab = Tab.GENERAL;
    private static @Nullable BlockPos lastLookedAtPos = null;
    private static boolean tabsVisible = false;

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        tabsVisible = false;
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        // The progress the server reported belongs to the last analysis, without the reset a new analysis would show it until the server reports again
        if (player != null && !isAnalyzing(player)) {
            CropAnalyzerClientData.setServerAnalyzeTicks(0);
        }
        if (player == null || mc.level == null || mc.options.hideGui || !player.isHolding(IRItems.CROP_ANALYZER.get())) return;
        if (!(mc.hitResult instanceof BlockHitResult hitResult) || hitResult.getType() != HitResult.Type.BLOCK) return;

        BlockPos pos = hitResult.getBlockPos();
        if (!pos.equals(lastLookedAtPos)) {
            lastLookedAtPos = pos.immutable();
            currentTab = Tab.GENERAL;
        }

        CropInfo info = getCropInfo(mc, pos);
        if (info == null) return;

        // Advance the cycled blocks once per game tick, independent of the frame rate
        long gameTime = mc.level.getGameTime();
        if (gameTime != lastCycleTick) {
            lastCycleTick = gameTime;
            for (BlockSection section : info.sections()) {
                section.renderer().tick(0);
            }
        }
        renderInfo(guiGraphics, mc.font, info);
        tabsVisible = info.tabbed();
    }

    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (!tabsVisible || mc.screen != null || mc.options.hideGui || player == null || !player.isShiftKeyDown()
                || !player.isHolding(IRItems.CROP_ANALYZER.get()) || event.getScrollDeltaY() == 0) return;
        int direction = event.getScrollDeltaY() < 0 ? 1 : -1;
        currentTab = Tab.values()[Mth.clamp(currentTab.ordinal() + direction, 0, Tab.values().length - 1)];
        event.setCanceled(true);
    }

    private static @Nullable CropInfo getCropInfo(Minecraft mc, BlockPos pos) {
        BlockState state = mc.level.getBlockState(pos);
        List<Component> lines = new ArrayList<>();
        List<Component> requirementLines = new ArrayList<>();
        List<BlockSection> sections;
        int light = mc.level.getRawBrightness(pos, 0);

        if (mc.level.getBlockEntity(pos) instanceof CropBlockEntity cropBE && cropBE.getCrop() != null) {
            LocalPlayer player = mc.player;
            float progress = -1;
            if (isAnalyzing(player)) {
                // The server decides when analyzing completes, so never display less progress than it reports
                int ticks = Math.max(player.getTicksUsingItem(), CropAnalyzerClientData.getServerAnalyzeTicks());
                progress = Mth.clamp(ticks / (float) CropAnalyzerItem.ANALYZE_DURATION, 0, 1);
            }

            // Only the analyzer the player holds counts, each analyzer keeps track of its own crops
            boolean analyzed = CropAnalyzerItem.hasAnalyzed(CropAnalyzerItem.getHeldAnalyzer(player), cropBE.getCrop());
            if (!analyzed && progress <= 97) {
                lines.add(IRTranslations.CROP_ANALYZER_CROP.component().withStyle(ChatFormatting.GRAY)
                        .append(IRTranslations.CROP_ANALYZER_UNKNOWN_CROP.component().withStyle(ChatFormatting.GRAY)));
                if (progress < 0) {
                    lines.add(IRTranslations.CROP_ANALYZER_HINT.component().withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
                } else {
                    lines.add(IRTranslations.CROP_ANALYZER_ANALYZING.component((int) (progress * 100)).withStyle(ChatFormatting.WHITE));
                }
                return new CropInfo(lines, List.of(), List.of(), List.of(), false, progress);
            }

            Crop crop = cropBE.getCrop().value();
            Component name = cropBE.getCrop().unwrapKey()
                    .<Component>map(key -> Component.translatable(Crop.getTranslationKey(key)))
                    .orElse(IRTranslations.CROP_ANALYZER_UNKNOWN_CROP.component());
            addCropLines(lines, name, cropBE.getAge(), cropBE.getMaxAge());

            List<LightLevelRequirement> lightRequirements = crop.requirements().stream()
                    .filter(LightLevelRequirement.class::isInstance)
                    .map(LightLevelRequirement.class::cast)
                    .toList();
            LightLevelRequirement lightRequirement = lightRequirements.isEmpty() ? DEFAULT_LIGHT_REQUIREMENT : new LightLevelRequirement(
                    lightRequirements.stream().mapToInt(LightLevelRequirement::min).max().orElse(0),
                    lightRequirements.stream().mapToInt(LightLevelRequirement::max).min().orElse(15));
            addLightLine(requirementLines, lightRequirement, light);

            sections = new ArrayList<>();
            List<ItemStack> blocksBelowSoil = crop.requirements().stream()
                    .filter(BlockBelowSoilRequirement.class::isInstance)
                    .flatMap(requirement -> ((BlockBelowSoilRequirement) requirement).blocks().stream())
                    .map(block -> new ItemStack(block.value()))
                    .filter(stack -> !stack.isEmpty())
                    .toList();
            if (!blocksBelowSoil.isEmpty()) {
                sections.add(BlockSection.of(IRTranslations.CROP_ANALYZER_BLOCK_BELOW_SOIL.component(), BLOCK_BELOW_SOIL_RENDERER, blocksBelowSoil));
            }
            List<ItemStack> soil = crop.requirements().stream()
                    .filter(SoilRequirement.class::isInstance)
                    .flatMap(requirement -> ((SoilRequirement) requirement).blocks().stream())
                    .map(block -> new ItemStack(block.value()))
                    .filter(stack -> !stack.isEmpty())
                    .toList();
            // Crops without a soil requirement grow on farmland
            if (soil.isEmpty() && crop.requirements().stream().noneMatch(SoilRequirement.class::isInstance)) {
                soil = List.of(new ItemStack(Blocks.FARMLAND));
            }
            if (!soil.isEmpty()) {
                sections.add(BlockSection.of(IRTranslations.CROP_ANALYZER_SOIL.component(), SOIL_RENDERER, soil));
            }
        } else if (state.getBlock() instanceof CropBlock cropBlock) {
            addCropLines(lines, cropBlock.getName(), cropBlock.getAge(state), cropBlock.getMaxAge());
            addLightLine(requirementLines, DEFAULT_LIGHT_REQUIREMENT, light);
            sections = List.of(BlockSection.of(IRTranslations.CROP_ANALYZER_SOIL.component(), SOIL_RENDERER, List.of(new ItemStack(Blocks.FARMLAND))));
        } else {
            return null;
        }

        CropDropsKey key = CropDropsKey.of(mc.level, pos);
        if (key == null) return new CropInfo(lines, List.of(), requirementLines, sections, true, -1);

        List<CropDrop> drops = CropAnalyzerClientData.getDrops(key);
        long gameTime = mc.level.getGameTime();
        if (drops == null && CropAnalyzerClientData.shouldRequest(key, gameTime, DROPS_RETRY_INTERVAL)) {
            CropAnalyzerClientData.markRequested(key, gameTime);
            PacketDistributor.sendToServer(new RequestCropDropsPayload(pos.immutable()));
        }
        return new CropInfo(lines, drops != null ? drops : List.of(), requirementLines, sections, true, -1);
    }

    private static boolean isAnalyzing(LocalPlayer player) {
        return player.isUsingItem() && player.getUseItem().is(IRItems.CROP_ANALYZER.get());
    }

    /**
     * e.g. "Light: 9+ (current 12)", the requirement is green if the current light level meets it, red otherwise
     */
    private static void addLightLine(List<Component> lines, LightLevelRequirement requirement, int light) {
        int min = requirement.min();
        int max = requirement.max();
        MutableComponent value;
        if (min == max) {
            value = IRTranslations.CROP_ANALYZER_LIGHT_EXACT.component(min);
        } else if (max >= 15) {
            value = IRTranslations.CROP_ANALYZER_LIGHT_MIN.component(min);
        } else {
            value = IRTranslations.CROP_ANALYZER_LIGHT_RANGE.component(min, max);
        }
        boolean met = light >= min && light <= max;
        lines.add(IRTranslations.CROP_ANALYZER_LIGHT.component().withStyle(ChatFormatting.GRAY)
                .append(value.withStyle(met ? ChatFormatting.GREEN : ChatFormatting.RED))
                .append(Component.literal(" ").append(IRTranslations.CROP_ANALYZER_LIGHT_CURRENT.component(light)).withStyle(ChatFormatting.DARK_GRAY)));
    }

    private static void addCropLines(List<Component> lines, Component name, int age, int maxAge) {
        lines.add(IRTranslations.CROP_ANALYZER_CROP.component().withStyle(ChatFormatting.GRAY)
                .append(name.copy().withStyle(ChatFormatting.WHITE)));
        lines.add(IRTranslations.CROP_ANALYZER_STAGE.component().withStyle(ChatFormatting.GRAY)
                .append(IRTranslations.CROP_ANALYZER_STAGE_VALUE.component(age, maxAge).withStyle(ChatFormatting.WHITE)));
    }

    private static void renderInfo(GuiGraphics guiGraphics, Font font, CropInfo info) {
        if (!info.tabbed()) {
            TabContent content = new TabContent(info.lines(), List.of(), List.of());
            int width = info.progress() < 0 ? content.width(font) : Math.max(content.width(font), PROGRESS_BAR_WIDTH);
            renderTooltip(guiGraphics, font, false, content, width, info.progress());
            return;
        }

        List<Component> generalLines = new ArrayList<>(info.lines());
        if (!info.drops().isEmpty()) {
            generalLines.add(IRTranslations.CROP_ANALYZER_DROPS.component().withStyle(ChatFormatting.GRAY));
        }
        TabContent general = new TabContent(generalLines, info.drops(), List.of());
        TabContent requirements = new TabContent(info.requirementLines(), List.of(), info.sections());
        TabContent content = currentTab == Tab.GENERAL ? general : requirements;

        // Widest of all tabs, so the tooltip keeps its width when switching tabs
        int headerWidth = font.width(TAB_ARROW_LEFT) + font.width(TAB_ARROW_RIGHT) + 2 * TAB_GAP + tabLabelsWidth(font);
        int width = Math.max(headerWidth, Math.max(general.width(font), requirements.width(font)));
        renderTooltip(guiGraphics, font, true, content, width, -1);
    }

    private static int tabLabelsWidth(Font font) {
        int width = (Tab.values().length - 1) * TAB_GAP;
        for (Tab tab : Tab.values()) {
            width += font.width(tab.label);
        }
        return width;
    }

    /**
     * e.g. "◀ General  Requirements ▶", an arrow is white if there is another tab in its direction
     */
    private static void renderTabHeader(GuiGraphics guiGraphics, Font font, int x, int y, int width) {
        Tab[] tabs = Tab.values();
        boolean hasLeft = currentTab.ordinal() > 0;
        boolean hasRight = currentTab.ordinal() < tabs.length - 1;
        guiGraphics.drawString(font, TAB_ARROW_LEFT, x, y, hasLeft ? 0xFFFFFFFF : 0xFF555555);
        guiGraphics.drawString(font, TAB_ARROW_RIGHT, x + width - font.width(TAB_ARROW_RIGHT), y, hasRight ? 0xFFFFFFFF : 0xFF555555);

        int labelX = x + (width - tabLabelsWidth(font)) / 2;
        for (Tab tab : tabs) {
            int labelWidth = font.width(tab.label);
            boolean selected = tab == currentTab;
            if (selected) {
                // The label's width already contains one pixel of spacing to the right
                guiGraphics.fill(labelX - 2, y - 2, labelX + labelWidth + 1, y + GLYPH_HEIGHT + 2, TAB_BACKDROP_COLOR);
            }
            guiGraphics.drawString(font, tab.label, labelX, y, selected ? 0xFFFFFFFF : 0xFFAAAAAA);
            labelX += labelWidth + TAB_GAP;
        }
    }

    /**
     * @param progress the analyzing progress shown below the content, negative to show none
     */
    private static void renderTooltip(GuiGraphics guiGraphics, Font font, boolean tabbed, TabContent content, int width, float progress) {
        int headerHeight = tabbed ? LINE_HEIGHT + HEADER_SPACING : 0;
        // The content's height leaves out the 2px of spacing below its last line, the bar starts after that spacing
        int progressHeight = progress < 0 ? 0 : 2 + PROGRESS_SQUARE_SIZE;
        int height = headerHeight + content.height() + progressHeight;

        int x = guiGraphics.guiWidth() / 2 + CROSSHAIR_OFFSET;
        // Top of the tooltip aligned with the screen center, moved up only if it would go off-screen
        int y = Math.max(4, Math.min(guiGraphics.guiHeight() / 2, guiGraphics.guiHeight() - height - 4));

        TooltipRenderUtil.renderTooltipBackground(guiGraphics, x, y, width, height, 0);

        int lineY = y;
        if (tabbed) {
            renderTabHeader(guiGraphics, font, x, lineY, width);
            lineY += headerHeight;
        }

        for (Component line : content.lines()) {
            guiGraphics.drawString(font, line, x, lineY, 0xFFFFFFFF);
            lineY += LINE_HEIGHT;
        }

        if (progress >= 0) {
            int filled = (int) (progress * PROGRESS_SQUARES);
            for (int i = 0; i < PROGRESS_SQUARES; i++) {
                int squareX = x + i * (PROGRESS_SQUARE_SIZE + PROGRESS_SQUARE_GAP);
                guiGraphics.fill(squareX, lineY, squareX + PROGRESS_SQUARE_SIZE, lineY + PROGRESS_SQUARE_SIZE, i < filled ? 0xFFFFFFFF : TAB_BACKDROP_COLOR);
            }
        }

        for (int i = 0; i < content.drops().size(); i++) {
            int rowY = lineY - 1 + i * ITEM_SIZE;
            CropDrop drop = content.drops().get(i);
            guiGraphics.renderItem(drop.stack(), x, rowY);
            renderCountLabel(guiGraphics, font, drop, x, rowY);
            renderCenteredText(guiGraphics, font, dropText(drop), x + DROP_TEXT_OFFSET, rowY);
        }

        for (int i = 0; i < content.sections().size(); i++) {
            int titleY = lineY + content.drops().size() * ITEM_SIZE + content.sectionSpacing() + i * SECTION_HEIGHT;
            int rowY = titleY + LINE_HEIGHT - 1;
            BlockSection section = content.sections().get(i);
            guiGraphics.drawString(font, section.title().copy().withStyle(ChatFormatting.GRAY), x, titleY, 0xFFFFFFFF);
            section.renderer().render(guiGraphics, x, rowY);
            ItemStack block = section.renderer().getItem();
            if (!block.isEmpty()) {
                renderCenteredText(guiGraphics, font, block.getHoverName().copy().withStyle(ChatFormatting.WHITE), x + DROP_TEXT_OFFSET, rowY);
            }
        }
    }

    /**
     * Renders the text vertically centered next to the 16px tall item at {@code itemY}
     */
    private static void renderCenteredText(GuiGraphics guiGraphics, Font font, Component text, int x, int itemY) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        // Fractional offset, since odd glyph heights can't be centered on whole gui pixels
        poseStack.translate(0, itemY + (16 - GLYPH_HEIGHT) / 2f, 0);
        guiGraphics.drawString(font, text, x, 0, 0xFFFFFFFF);
        poseStack.popPose();
    }

    /**
     * e.g. "57% (avg 2.71)", the count range is rendered on the item
     */
    private static Component dropText(CropDrop drop) {
        float percent = drop.chance() * 100;
        String chance = percent >= 99.995f ? "100%" : formatNumber(percent) + "%";
        MutableComponent text = Component.literal(chance).withStyle(ChatFormatting.WHITE);
        if (drop.minCount() != drop.maxCount()) {
            text.append(Component.literal(" ").append(IRTranslations.CROP_ANALYZER_DROP_AVERAGE.component(formatNumber(drop.averageCount())))
                    .withStyle(ChatFormatting.DARK_GRAY));
        }
        return text;
    }

    /**
     * Renders the count or count range in the bottom right corner of the item, like the regular stack count
     * but scaled down, since ranges are wider than counts
     */
    private static void renderCountLabel(GuiGraphics guiGraphics, Font font, CropDrop drop, int itemX, int itemY) {
        String label;
        if (drop.minCount() != drop.maxCount()) {
            label = drop.minCount() + "-" + drop.maxCount();
        } else if (drop.minCount() != 1) {
            label = String.valueOf(drop.minCount());
        } else {
            return;
        }
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(itemX + 17, itemY + 17, 200);
        poseStack.scale(COUNT_LABEL_SCALE, COUNT_LABEL_SCALE, 1);
        guiGraphics.drawString(font, label, -font.width(label), -font.lineHeight + 1, 0xFFFFFFFF, true);
        poseStack.popPose();
    }

    /**
     * Two significant decimals at most, without trailing zeros
     */
    private static String formatNumber(float value) {
        String formatted = String.format(Locale.ROOT, value >= 10 ? "%.1f" : "%.2f", value);
        return formatted.contains(".") ? formatted.replaceAll("0+$", "").replaceAll("\\.$", "") : formatted;
    }

    private record CropInfo(List<Component> lines, List<CropDrop> drops, List<Component> requirementLines,
                            List<BlockSection> sections, boolean tabbed, float progress) {
    }

    private enum Tab {
        GENERAL(IRTranslations.CROP_ANALYZER_TAB_GENERAL.component()),
        REQUIREMENTS(IRTranslations.CROP_ANALYZER_TAB_REQUIREMENTS.component());

        private final Component label;

        Tab(Component label) {
            this.label = label;
        }
    }

    private record TabContent(List<Component> lines, List<CropDrop> drops, List<BlockSection> sections) {
        int width(Font font) {
            int width = 0;
            for (Component line : lines) {
                width = Math.max(width, font.width(line));
            }
            for (CropDrop drop : drops) {
                width = Math.max(width, DROP_TEXT_OFFSET + font.width(dropText(drop)));
            }
            for (BlockSection section : sections) {
                // Widest name of all cycled blocks, so the tooltip doesn't change size while cycling
                int nameWidth = section.renderer().getItems().stream().mapToInt(stack -> font.width(stack.getHoverName())).max().orElse(0);
                width = Math.max(width, Math.max(font.width(section.title()), DROP_TEXT_OFFSET + nameWidth));
            }
            return width;
        }

        int sectionSpacing() {
            return !drops.isEmpty() && !sections.isEmpty() ? SECTION_SPACING : 0;
        }

        int height() {
            return lines.size() * LINE_HEIGHT - 2 + drops.size() * ITEM_SIZE + sections.size() * SECTION_HEIGHT + sectionSpacing();
        }
    }

    private record BlockSection(Component title, CycledItemRenderer renderer) {
        static BlockSection of(Component title, CycledItemRenderer renderer, List<ItemStack> blocks) {
            renderer.setItems(blocks);
            return new BlockSection(title, renderer);
        }
    }
}
