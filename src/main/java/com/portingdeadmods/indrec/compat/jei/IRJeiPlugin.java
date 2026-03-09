package com.portingdeadmods.indrec.compat.jei;

import com.portingdeadmods.indrec.IndustrialReclassified;
import com.portingdeadmods.indrec.content.recipes.MachineRecipe;
import com.portingdeadmods.indrec.content.recipes.MachineRecipeLayout;
import com.portingdeadmods.indrec.registries.IRMachines;
import com.portingdeadmods.indrec.registries.IRRecipeLayouts;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JeiPlugin
public class IRJeiPlugin implements IModPlugin {
    public static final ResourceLocation UID = IndustrialReclassified.rl("ir_jei_plugin");
    private final Map<MachineRecipeLayout<?>, IRecipeCategory<?>> recipes = new HashMap<>();

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registerCategory(registration, createCategory(registration, IRRecipeLayouts.COMPRESSOR, Component.literal("Compressor"), IRMachines.COMPRESSOR.getBlock()), IRRecipeLayouts.COMPRESSOR);
        registerCategory(registration, createCategory(registration, IRRecipeLayouts.MACERATOR, Component.literal("Macerator"), IRMachines.MACERATOR.getBlock()), IRRecipeLayouts.MACERATOR);
        registerCategory(registration, createCategory(registration, IRRecipeLayouts.EXTRACTOR, Component.literal("Extractor"), IRMachines.EXTRACTOR.getBlock()), IRRecipeLayouts.EXTRACTOR);
        registerCategory(registration, createCategory(registration, IRRecipeLayouts.CANNING_MACHINE, Component.literal("Canning Machine"), IRMachines.CANNING_MACHINE.getBlock()), IRRecipeLayouts.CANNING_MACHINE);
    }

    private static @NotNull MachineRecipeCategory createCategory(IRecipeCategoryRegistration registration, MachineRecipeLayout<MachineRecipe> layout, Component component, ItemLike icon) {
        ClientLevel level = Minecraft.getInstance().level;
        List<MachineRecipe> recipes = level.getRecipeManager().getAllRecipesFor(layout.getRecipeType()).stream().map(RecipeHolder::value).toList();
        return new MachineRecipeCategory(registration.getJeiHelpers().getGuiHelper(), layout, recipes, component, icon);
    }

    private void registerCategory(IRecipeCategoryRegistration registration, MachineRecipeCategory category, MachineRecipeLayout<?> layout) {
        recipes.put(layout, category);
        registration.addRecipeCategories(category);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(IRMachines.COMPRESSOR.getBlock(), this.recipes.get(IRRecipeLayouts.COMPRESSOR).getRecipeType());
        registration.addRecipeCatalyst(IRMachines.MACERATOR.getBlock(), this.recipes.get(IRRecipeLayouts.MACERATOR).getRecipeType());
        registration.addRecipeCatalyst(IRMachines.EXTRACTOR.getBlock(), this.recipes.get(IRRecipeLayouts.EXTRACTOR).getRecipeType());
        registration.addRecipeCatalyst(IRMachines.CANNING_MACHINE.getBlock(), this.recipes.get(IRRecipeLayouts.CANNING_MACHINE).getRecipeType());
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registerRecipe(registration, IRRecipeLayouts.COMPRESSOR);
        registerRecipe(registration, IRRecipeLayouts.MACERATOR);
        registerRecipe(registration, IRRecipeLayouts.EXTRACTOR);
        registerRecipe(registration, IRRecipeLayouts.CANNING_MACHINE);
    }

    private <R extends MachineRecipe> void registerRecipe(IRecipeRegistration registration, MachineRecipeLayout<R> layout) {
        List<R> recipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(layout.getRecipeType()).stream()
                .map(RecipeHolder::value)
                .toList();
        RecipeType<R> recipeType = (RecipeType<R>) this.recipes.get(layout).getRecipeType();
        registration.addRecipes(recipeType, recipes);
    }
}
