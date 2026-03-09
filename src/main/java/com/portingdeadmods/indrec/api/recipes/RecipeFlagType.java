package com.portingdeadmods.indrec.api.recipes;

import net.minecraft.resources.ResourceLocation;

public record RecipeFlagType<F extends RecipeComponentFlag>(ResourceLocation id) {
}
