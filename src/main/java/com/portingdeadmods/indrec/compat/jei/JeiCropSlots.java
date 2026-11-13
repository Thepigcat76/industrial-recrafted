package com.portingdeadmods.indrec.compat.jei;

import com.portingdeadmods.indrec.IRRegistries;
import com.portingdeadmods.indrec.api.crops.Crop;
import com.portingdeadmods.indrec.api.crops.SeedDrop;
import com.portingdeadmods.indrec.registries.IRItems;
import com.portingdeadmods.indrec.registries.IRTranslations;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

/**
 * Slots showing crops in JEI, each crop is represented by its seed
 */
final class JeiCropSlots {
    private JeiCropSlots() {
    }

    /**
     * Adds a slot showing the crop's representative stack, with the crop's name in the tooltip
     */
    static IRecipeSlotBuilder cropSlot(IRecipeLayoutBuilder builder, RecipeIngredientRole role, ResourceKey<Crop> cropKey, int x, int y) {
        Optional<Holder.Reference<Crop>> crop = Optional.ofNullable(Minecraft.getInstance().level)
                .flatMap(level -> level.registryAccess().registry(IRRegistries.CROP))
                .flatMap(registry -> registry.getHolder(cropKey));

        ItemStack representativeStack = crop.map(JeiCropSlots::representativeStack).orElse(IRItems.CROP_STICKS.toStack());

        return builder.addSlot(role, x, y).addItemStack(representativeStack)
                .addRichTooltipCallback((view, tooltip) ->
                        tooltip.add(IRTranslations.CROP_ANALYZER_CROP.component().withStyle(ChatFormatting.GRAY)
                                .append(Component.translatable(Crop.getTranslationKey(cropKey)).withStyle(ChatFormatting.WHITE))));
    }

    /**
     * @return the seed of the crop, its seed drop if it has no seed, or crop sticks if it has neither
     */
    static ItemStack representativeStack(Holder<Crop> crop) {
        return crop.value().createSeedStack()
                .or(() -> crop.value().seedDrop().map(SeedDrop::item).map(ItemStack::copy))
                .orElseGet(IRItems.CROP_STICKS::toStack);
    }
}
