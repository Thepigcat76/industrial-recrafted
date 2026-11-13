package com.portingdeadmods.indrec.compat.jei;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.api.crops.Crop;
import com.portingdeadmods.indrec.api.crops.CropInteraction;
import com.portingdeadmods.indrec.registries.IRItems;
import com.portingdeadmods.indrec.registries.IRTranslations;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceKey;

/**
 * Shows crop interactions as {@code [crop] + [item] -> [result]}, each crop represented by its seed
 */
public class CropInteractionRecipeCategory extends AbstractRecipeCategory<CropInteractionRecipeCategory.Recipe> {
    public static final RecipeType<Recipe> RECIPE_TYPE = RecipeType.create(IndustrialRecrafted.MODID, "crop_interaction", Recipe.class);

    private static final int SLOT_SIZE = 18;
    private static final int PLUS_WIDTH = 13;
    private static final int ARROW_WIDTH = 22;
    private static final int OUTPUT_SLOT_SIZE = 26;
    private static final int GAP = 4;
    private static final int WIDTH = SLOT_SIZE + GAP + PLUS_WIDTH + GAP + SLOT_SIZE + GAP + ARROW_WIDTH + GAP + OUTPUT_SLOT_SIZE;
    private static final int HEIGHT = OUTPUT_SLOT_SIZE;

    public CropInteractionRecipeCategory(IGuiHelper guiHelper) {
        super(RECIPE_TYPE, IRTranslations.CROP_INTERACTION.component(), guiHelper.createDrawableItemLike(IRItems.CROP_STICKS), WIDTH, HEIGHT);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, Recipe recipe, IFocusGroup focuses) {
        CropInteraction interaction = recipe.interaction();
        int slotY = (HEIGHT - SLOT_SIZE) / 2 + 1;

        JeiCropSlots.cropSlot(builder, RecipeIngredientRole.INPUT, recipe.crop(), 1, slotY)
                .setStandardSlotBackground();
        builder.addSlot(RecipeIngredientRole.CATALYST, SLOT_SIZE + GAP + PLUS_WIDTH + GAP + 1, slotY)
                .addIngredients(interaction.ingredient())
                .setStandardSlotBackground()
                .addRichTooltipCallback((view, tooltip) -> {
                    tooltip.add((interaction.consume() ? IRTranslations.ITEM_CONSUMED : IRTranslations.CROP_INTERACTION_NOT_CONSUMED).component().withStyle(ChatFormatting.GRAY));
                    if (interaction.consume()) {
                        interaction.remainder().ifPresent(remainder ->
                                tooltip.add(IRTranslations.CROP_INTERACTION_REMAINDER.component(remainder.getHoverName()).withStyle(ChatFormatting.GRAY)));
                    }
                });

        JeiCropSlots.cropSlot(builder, RecipeIngredientRole.OUTPUT, interaction.result(), WIDTH - OUTPUT_SLOT_SIZE + 5, (HEIGHT - 16) / 2)
                .setOutputSlotBackground()
                .addRichTooltipCallback((view, tooltip) ->
                        tooltip.add((interaction.keepAge() ? IRTranslations.CROP_KEEPS_AGE : IRTranslations.CROP_RESETS_AGE).component().withStyle(ChatFormatting.GRAY)));
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, Recipe recipe, IFocusGroup focuses) {
        builder.addRecipePlusSign().setPosition(SLOT_SIZE + GAP, (HEIGHT - PLUS_WIDTH) / 2);
        builder.addRecipeArrow().setPosition(SLOT_SIZE + GAP + PLUS_WIDTH + GAP + SLOT_SIZE + GAP, (HEIGHT - 16) / 2);
    }

    public record Recipe(ResourceKey<Crop> crop, CropInteraction interaction) {
    }
}
