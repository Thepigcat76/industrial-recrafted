package com.portingdeadmods.indrec.compat.jei;

import com.portingdeadmods.indrec.client.screens.widgets.JEIEnergyBarWidget;
import com.portingdeadmods.indrec.content.recipes.MachineRecipe;
import com.portingdeadmods.indrec.content.recipes.MachineRecipeLayout;
import com.portingdeadmods.indrec.content.recipes.components.energy.EnergyInputComponent;
import com.portingdeadmods.indrec.content.recipes.components.TimeComponent;
import com.portingdeadmods.indrec.content.recipes.flags.ItemInputComponentFlag;
import com.portingdeadmods.indrec.content.recipes.flags.ItemOutputComponentFlag;
import com.portingdeadmods.indrec.registries.IRRecipeComponentFlags;
import com.portingdeadmods.indrec.registries.IRTranslations;
import com.portingdeadmods.portingdeadlibs.api.capabilities.EnergyStorageWrapper;
import com.portingdeadmods.portingdeadlibs.api.recipes.IngredientWithCount;
import com.portingdeadmods.portingdeadlibs.client.screens.widgets.EnergyBarWidget;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.OptionalInt;

public class MachineRecipeCategory extends AbstractRecipeCategory<MachineRecipe> {
    private final EnergyBarWidget energyBarWidget;
    private int energyUsed;
    private int arrowX;

    public MachineRecipeCategory(IGuiHelper guiHelper, MachineRecipeLayout<?> layout, List<MachineRecipe> recipes, Component title, ItemLike machineIcon) {
        super(RecipeType.create(layout.getId().getNamespace(), layout.getId().getPath(), MachineRecipe.class), title, guiHelper.createDrawableItemLike(machineIcon), calculateWidthMax(recipes), 48);
        this.energyBarWidget = new JEIEnergyBarWidget(0, 0, new EnergyStorageWrapper() {
            @Override
            public int getEnergyStored() {
                return energyUsed;
            }

            @Override
            public int getEnergyCapacity() {
                return energyUsed;
            }
        }, IRTranslations.ENERGY_UNIT.component().getString(), true);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MachineRecipe recipe, IFocusGroup focuses) {
        ItemInputComponentFlag input = recipe.getComponentByFlag(IRRecipeComponentFlags.ITEM_INPUT);
        ItemOutputComponentFlag output = recipe.getComponentByFlag(IRRecipeComponentFlags.ITEM_OUTPUT);

        int x = 0;
        for (int i = 0; i < input.getIngredients().size(); i++) {
            IngredientWithCount ingredient = input.getIngredients().get(i);
            float chance;
            if (input.getChances().size() > i) {
                chance = input.getChances().get(i);
            } else {
                chance = 1f;
            }

            x = i * 18 + this.energyBarWidget.getWidth() + 4;
            IRecipeSlotBuilder slotBuilder = builder.addInputSlot(x, this.getHeight() / 2 - 8)
                    .addIngredients(ingredient.toIngredientSaveCount())
                    .setStandardSlotBackground();
            if (chance < 1f) {
                slotBuilder.addRichTooltipCallback((view, tooltip) -> {
                    tooltip.add(Component.literal("Chance " + chance * 100 + "%").withStyle(ChatFormatting.DARK_GRAY));
                });
            }
        }

        if (!input.getIngredients().isEmpty()) {
            x += 18;
        }

        int extraSlotOffsetX = 0;
        if (output != null) {
            for (int i = 0; i < output.getOutputs().size(); i++) {
                ItemStack outputItem = output.getOutputs().get(i);
                float chance;
                if (output.getChances().size() > i) {
                    chance = output.getChances().get(i);
                } else {
                    chance = 1f;
                }

                IRecipeSlotBuilder slotBuilder = builder.addOutputSlot(x + 22 + 4 + 9 + Math.max(i - 1, 0) * 16 + extraSlotOffsetX, this.getHeight() / 2 - 8)
                        .addItemStack(outputItem);
                if (i == 0) {
                    slotBuilder.setOutputSlotBackground();
                    extraSlotOffsetX += 26;
                } else {
                    slotBuilder.setStandardSlotBackground();
                }
                if (chance < 1f) {
                    slotBuilder.addRichTooltipCallback((view, tooltip) -> {
                        tooltip.add(Component.literal("Chance " + chance * 100 + "%").withStyle(ChatFormatting.DARK_GRAY));
                    });
                }
            }
        }


//        else if (recipe.getId().equals(IRRecipeLayouts.FOOD_CANNING_MACHINE.getId())) {
//            builder.addOutputSlot(this.getWidth() - 16, this.getHeight() / 2 - 8)
//                    .addItemStack(IRItems.TIN_CAN_FOOD.toStack())
//                    .addRichTooltipCallback((view, tooltip) -> tooltip.add(Component.literal("Any Food Item").withStyle(ChatFormatting.DARK_GRAY)));
//        }

    }

    @Override
    public void draw(MachineRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        ItemInputComponentFlag input = recipe.getComponentByFlag(IRRecipeComponentFlags.ITEM_INPUT);

        int energy = recipe.getComponent(EnergyInputComponent.TYPE).energy();
        int time = recipe.getComponent(TimeComponent.TYPE).time();

        this.energyUsed = recipe.getComponent(EnergyInputComponent.TYPE).energy();
        this.energyBarWidget.render(guiGraphics, (int) mouseX, (int) mouseY, 0f);

        boolean hovered = guiGraphics.containsPointInScissor((int) mouseX, (int) mouseY) && mouseX >= this.energyBarWidget.getX() && mouseY >= this.energyBarWidget.getY() && mouseX < this.energyBarWidget.getX() + this.energyBarWidget.getWidth() && mouseY < this.energyBarWidget.getY() + this.energyBarWidget.getHeight();
        if (hovered) {
            guiGraphics.renderComponentTooltip(Minecraft.getInstance().font, List.of(Component.literal(energy / time + " EU/t"), Component.literal(energy / (time / 20) + " EU/s").withStyle(ChatFormatting.GRAY)), (int) mouseX, (int) mouseY);
        }

        int arrowX = this.energyBarWidget.getWidth() + 4 + input.getIngredients().size() * 18 + 4;
        double arrowY = (double) this.getHeight() / 2 - 8;
        if (mouseX > arrowX && mouseX < arrowX + 22 && mouseY > arrowY && mouseY < arrowY + 16) {
            guiGraphics.renderComponentTooltip(Minecraft.getInstance().font, List.of(Component.literal(time + "t"), Component.literal((time / 20) + "s").withStyle(ChatFormatting.GRAY)), (int) mouseX, (int) mouseY);
        }
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, MachineRecipe recipe, IFocusGroup focuses) {
        ItemInputComponentFlag input = recipe.getComponentByFlag(IRRecipeComponentFlags.ITEM_INPUT);
        int energy = recipe.getComponent(EnergyInputComponent.TYPE).energy();
        int time = recipe.getComponent(TimeComponent.TYPE).time();
        int arrowX = this.energyBarWidget.getWidth() + 4 + input.getIngredients().size() * 18 + 4;
        builder.addAnimatedRecipeArrow(time).setPosition(arrowX, this.getHeight() / 2 - 8);
//        MutableComponent literal = Component.literal((time / 20) + "s");
//        builder.addText(literal.withStyle(ChatFormatting.DARK_GRAY), this.getWidth(), Minecraft.getInstance().font.lineHeight)
//                .setPosition((this.getWidth() - Minecraft.getInstance().font.width(literal)) / 2, this.getHeight() - Minecraft.getInstance().font.lineHeight * 2);
    }

    private static int calculateWidthMax(List<MachineRecipe> recipes) {
        OptionalInt max = recipes.stream().mapToInt(MachineRecipeCategory::calculateRecipeWidth).max();
        return max.orElse(96);
    }

    private static int calculateRecipeWidth(MachineRecipe recipe) {
        ItemInputComponentFlag input = recipe.getComponentByFlag(IRRecipeComponentFlags.ITEM_INPUT);
        ItemOutputComponentFlag output = recipe.getComponentByFlag(IRRecipeComponentFlags.ITEM_OUTPUT);

        int defaultWidth = 96;
        int width = 12 + 4 + 22 + 4; // energyBarWidget.getWidth() + animatedArrow.width() + PADDINGS

        if (input != null && !input.getIngredients().isEmpty()) {
            width += input.getIngredients().size() * 18;
            width += 4;
        }

        if (output != null && !output.getOutputs().isEmpty()) {
            width += (output.getOutputs().size() - 1) * 18 + 26;
            width += 4;
        }

        return Math.max(width, defaultWidth);
    }

}
