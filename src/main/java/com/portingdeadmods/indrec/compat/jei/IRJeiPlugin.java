package com.portingdeadmods.indrec.compat.jei;

import com.portingdeadmods.indrec.client.screens.ToolboxFilterScreen;

import com.portingdeadmods.indrec.IRCapabilities;
import com.portingdeadmods.indrec.IRRegistries;
import com.portingdeadmods.indrec.api.crops.CropInteraction;
import com.portingdeadmods.indrec.api.crops.CropMutation;
import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.content.items.FluidCellItem;
import com.portingdeadmods.indrec.content.items.FuelJetpackItem;
import com.portingdeadmods.indrec.registries.IRItems;
import com.portingdeadmods.indrec.registries.IRRecipes;
import com.portingdeadmods.indrec.api.recipes.MachineRecipeLayout;
import com.portingdeadmods.indrec.content.recipes.MachineRecipeImpl;
import com.portingdeadmods.indrec.registries.IRMachines;
import com.portingdeadmods.indrec.registries.IRRecipeLayouts;
import com.portingdeadmods.portingdeadlibs.api.client.screens.PDLAbstractContainerScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JeiPlugin
public class IRJeiPlugin implements IModPlugin {
    public static final ResourceLocation UID = IndustrialRecrafted.rl("ir_jei_plugin");
    private final Map<MachineRecipeLayout<?>, IRecipeCategory<?>> recipes = new HashMap<>();

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        for (Item item : BuiltInRegistries.ITEM) {
            if (!BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(IndustrialRecrafted.MODID)) continue;

            if (item instanceof FluidCellItem || item instanceof FuelJetpackItem) {
                registration.registerSubtypeInterpreter(item, IRSubtypeInterpreters.FLUID);
            } else if (new ItemStack(item).getCapability(IRCapabilities.ENERGY_ITEM) != null) {
                registration.registerSubtypeInterpreter(item, IRSubtypeInterpreters.ENERGY);
            }
        }
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registerCategory(registration, createCategory(registration, IRRecipeLayouts.COMPRESSOR, Component.literal("Compressor"), IRMachines.COMPRESSOR.getBlock()), IRRecipeLayouts.COMPRESSOR);
        registerCategory(registration, createCategory(registration, IRRecipeLayouts.MACERATOR, Component.literal("Macerator"), IRMachines.MACERATOR.getBlock()), IRRecipeLayouts.MACERATOR);
        registerCategory(registration, createCategory(registration, IRRecipeLayouts.EXTRACTOR, Component.literal("Extractor"), IRMachines.EXTRACTOR.getBlock()), IRRecipeLayouts.EXTRACTOR);
        registerCategory(registration, createCategory(registration, IRRecipeLayouts.CANNING_MACHINE, Component.literal("Canning Machine"), IRMachines.CANNING_MACHINE.getBlock()), IRRecipeLayouts.CANNING_MACHINE);
        registration.addRecipeCategories(new CropCrossbreedingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CropMutationRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CropInteractionRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    private static @NotNull MachineRecipeCategory createCategory(IRecipeCategoryRegistration registration, MachineRecipeLayout<MachineRecipeImpl> layout, Component component, ItemLike icon) {
        ClientLevel level = Minecraft.getInstance().level;
        List<MachineRecipeImpl> recipes = level.getRecipeManager().getAllRecipesFor(layout.getRecipeType()).stream().map(RecipeHolder::value).toList();
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
        registration.addRecipeCatalyst(IRItems.CROP_STICKS.get(), CropCrossbreedingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(IRItems.CROP_STICKS.get(), CropMutationRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(IRItems.CROP_STICKS.get(), CropInteractionRecipeCategory.RECIPE_TYPE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGenericGuiContainerHandler(PDLAbstractContainerScreen.class, new WidgetBounds());
        registration.addGhostIngredientHandler(ToolboxFilterScreen.class, new ToolboxGhostIngredientHandler());
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registerRecipe(registration, IRRecipeLayouts.COMPRESSOR);
        registerRecipe(registration, IRRecipeLayouts.MACERATOR);
        registerRecipe(registration, IRRecipeLayouts.EXTRACTOR);
        registerRecipe(registration, IRRecipeLayouts.CANNING_MACHINE);
        registration.addRecipes(CropCrossbreedingRecipeCategory.RECIPE_TYPE, Minecraft.getInstance().level.getRecipeManager()
                .getAllRecipesFor(IRRecipes.CROP_CROSSBREEDING_TYPE.get()).stream()
                .map(RecipeHolder::value)
                .toList());

        List<CropMutationRecipeCategory.Recipe> mutations = new ArrayList<>();
        List<CropInteractionRecipeCategory.Recipe> interactions = new ArrayList<>();
        Minecraft.getInstance().level.registryAccess().registry(IRRegistries.CROP).ifPresent(crops -> crops.holders().forEach(crop -> {
            for (CropMutation mutation : crop.value().mutations()) {
                mutations.add(new CropMutationRecipeCategory.Recipe(crop.key(), mutation));
            }
            for (CropInteraction interaction : crop.value().interactions()) {
                interactions.add(new CropInteractionRecipeCategory.Recipe(crop.key(), interaction));
            }
        }));
        registration.addRecipes(CropMutationRecipeCategory.RECIPE_TYPE, mutations);
        registration.addRecipes(CropInteractionRecipeCategory.RECIPE_TYPE, interactions);
    }

    private <R extends MachineRecipeImpl> void registerRecipe(IRecipeRegistration registration, MachineRecipeLayout<R> layout) {
        List<R> recipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(layout.getRecipeType()).stream()
                .map(RecipeHolder::value)
                .toList();
        RecipeType<R> recipeType = (RecipeType<R>) this.recipes.get(layout).getRecipeType();
        registration.addRecipes(recipeType, recipes);
    }
}
