package com.portingdeadmods.indrec.compat.jei;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.api.crops.Crop;
import com.portingdeadmods.indrec.api.crops.CropMutation;
import com.portingdeadmods.indrec.registries.IRItems;
import com.portingdeadmods.indrec.registries.IRTranslations;
import com.portingdeadmods.indrec.utils.TooltipUtils;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceKey;

/**
 * Shows crop mutations as {@code [crop] -> [result]}, each crop represented by its seed
 */
public class CropMutationRecipeCategory extends AbstractRecipeCategory<CropMutationRecipeCategory.Recipe> {
    public static final RecipeType<Recipe> RECIPE_TYPE = RecipeType.create(IndustrialRecrafted.MODID, "crop_mutation", Recipe.class);

    private static final int SLOT_SIZE = 18;
    private static final int ARROW_WIDTH = 22;
    private static final int OUTPUT_SLOT_SIZE = 26;
    private static final int GAP = 4;
    private static final int WIDTH = SLOT_SIZE + GAP + ARROW_WIDTH + GAP + OUTPUT_SLOT_SIZE;
    private static final int HEIGHT = OUTPUT_SLOT_SIZE;

    public CropMutationRecipeCategory(IGuiHelper guiHelper) {
        super(RECIPE_TYPE, IRTranslations.CROP_MUTATION.component(), guiHelper.createDrawableItemLike(IRItems.CROP_STICKS), WIDTH, HEIGHT);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, Recipe recipe, IFocusGroup focuses) {
        JeiCropSlots.cropSlot(builder, RecipeIngredientRole.INPUT, recipe.crop(), 1, (HEIGHT - SLOT_SIZE) / 2 + 1)
                .setStandardSlotBackground();

        CropMutation mutation = recipe.mutation();
        IRecipeSlotBuilder output = JeiCropSlots.cropSlot(builder, RecipeIngredientRole.OUTPUT, mutation.result(), WIDTH - OUTPUT_SLOT_SIZE + 5, (HEIGHT - 16) / 2)
                .setOutputSlotBackground();
        String chance = TooltipUtils.formatChance(mutation.chance());
        output.addRichTooltipCallback((view, tooltip) -> {
            tooltip.add(IRTranslations.CROP_MUTATION_CHANCE.component(chance).withStyle(ChatFormatting.GRAY));
            tooltip.add((mutation.keepAge() ? IRTranslations.CROP_KEEPS_AGE : IRTranslations.CROP_RESETS_AGE).component().withStyle(ChatFormatting.GRAY));
        });
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, Recipe recipe, IFocusGroup focuses) {
        builder.addRecipeArrow().setPosition(SLOT_SIZE + GAP, (HEIGHT - 16) / 2);
    }

    public record Recipe(ResourceKey<Crop> crop, CropMutation mutation) {
    }
}
