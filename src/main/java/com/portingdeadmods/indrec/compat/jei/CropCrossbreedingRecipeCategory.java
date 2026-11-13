package com.portingdeadmods.indrec.compat.jei;


import com.portingdeadmods.indrec.utils.TooltipUtils;
import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.content.recipes.CropCrossbreedingRecipe;
import com.portingdeadmods.indrec.registries.IRItems;
import com.portingdeadmods.indrec.registries.IRTranslations;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.ChatFormatting;


/**
 * Shows crop crossbreeding recipes as {@code [parent] + [parent] -> [result]}, each crop represented by its seed
 */
public class CropCrossbreedingRecipeCategory extends AbstractRecipeCategory<CropCrossbreedingRecipe> {
    public static final RecipeType<CropCrossbreedingRecipe> RECIPE_TYPE = RecipeType.create(IndustrialRecrafted.MODID, "crop_crossbreeding", CropCrossbreedingRecipe.class);

    private static final int SLOT_SIZE = 18;
    private static final int PLUS_WIDTH = 13;
    private static final int ARROW_WIDTH = 22;
    private static final int OUTPUT_SLOT_SIZE = 26;
    private static final int GAP = 4;
    private static final int WIDTH = SLOT_SIZE + GAP + PLUS_WIDTH + GAP + SLOT_SIZE + GAP + ARROW_WIDTH + GAP + OUTPUT_SLOT_SIZE;
    private static final int HEIGHT = OUTPUT_SLOT_SIZE;

    public CropCrossbreedingRecipeCategory(IGuiHelper guiHelper) {
        super(RECIPE_TYPE, IRTranslations.CROP_CROSSBREEDING.component(), guiHelper.createDrawableItemLike(IRItems.CROP_STICKS), WIDTH, HEIGHT);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CropCrossbreedingRecipe recipe, IFocusGroup focuses) {
        int slotY = (HEIGHT - SLOT_SIZE) / 2 + 1;
        parentSlot(JeiCropSlots.cropSlot(builder, RecipeIngredientRole.INPUT, recipe.firstParent(), 1, slotY), recipe.firstParentSpreads());
        parentSlot(JeiCropSlots.cropSlot(builder, RecipeIngredientRole.INPUT, recipe.secondParent(), SLOT_SIZE + GAP + PLUS_WIDTH + GAP + 1, slotY), recipe.secondParentSpreads());

        IRecipeSlotBuilder output = JeiCropSlots.cropSlot(builder, RecipeIngredientRole.OUTPUT, recipe.result(), WIDTH - OUTPUT_SLOT_SIZE + 5, (HEIGHT - 16) / 2)
                .setOutputSlotBackground();
        if (recipe.chance() < 1) {
            String chance = TooltipUtils.formatChance(recipe.chance());
            output.addRichTooltipCallback((view, tooltip) ->
                    tooltip.add(IRTranslations.CROP_CROSSBREEDING_CHANCE.component(chance).withStyle(ChatFormatting.GRAY)));
        }
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, CropCrossbreedingRecipe recipe, IFocusGroup focuses) {
        builder.addRecipePlusSign().setPosition(SLOT_SIZE + GAP, (HEIGHT - PLUS_WIDTH) / 2);
        builder.addRecipeArrow().setPosition(SLOT_SIZE + GAP + PLUS_WIDTH + GAP + SLOT_SIZE + GAP, (HEIGHT - 16) / 2);
    }

    private static void parentSlot(IRecipeSlotBuilder slot, boolean spreads) {
        slot.setStandardSlotBackground();
        if (spreads) {
            slot.addRichTooltipCallback((view, tooltip) ->
                    tooltip.add(IRTranslations.CROP_CROSSBREEDING_CAN_SPREAD.component().withStyle(ChatFormatting.GRAY)));
        }
    }
}
