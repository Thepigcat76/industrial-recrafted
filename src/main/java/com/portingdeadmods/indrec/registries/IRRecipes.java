package com.portingdeadmods.indrec.registries;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.content.recipes.CropCrossbreedingRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

// Machine recipes are in IRRecipeLayouts
public final class IRRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, IndustrialRecrafted.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, IndustrialRecrafted.MODID);

    public static final Supplier<RecipeType<CropCrossbreedingRecipe>> CROP_CROSSBREEDING_TYPE = RECIPE_TYPES.register("crop_crossbreeding",
            () -> RecipeType.simple(IndustrialRecrafted.rl("crop_crossbreeding")));
    public static final Supplier<RecipeSerializer<CropCrossbreedingRecipe>> CROP_CROSSBREEDING_SERIALIZER = RECIPE_SERIALIZERS.register("crop_crossbreeding",
            CropCrossbreedingRecipe.Serializer::new);

    private IRRecipes() {
    }
}
