package com.portingdeadmods.indrec.content.recipes.layouts;

import com.portingdeadmods.indrec.api.recipes.RecipeComponent;
import com.portingdeadmods.indrec.content.recipes.MachineRecipe;
import com.portingdeadmods.indrec.content.recipes.MachineRecipeInput;
import com.portingdeadmods.indrec.content.recipes.MachineRecipeLayout;
import com.portingdeadmods.indrec.content.recipes.components.energy.EnergyInputComponent;
import com.portingdeadmods.indrec.content.recipes.components.EnumRecipeComponent;
import com.portingdeadmods.indrec.content.recipes.components.TimeComponent;
import com.portingdeadmods.indrec.content.recipes.components.items.ItemInputListComponent;
import com.portingdeadmods.indrec.content.recipes.components.items.ItemOutputComponent;
import com.portingdeadmods.indrec.content.recipes.flags.ItemInputComponentFlag;
import com.portingdeadmods.indrec.registries.IRItems;
import com.portingdeadmods.indrec.registries.IRRecipeComponentFlags;
import com.portingdeadmods.portingdeadlibs.api.recipes.IngredientWithCount;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CanningMachineRecipeLayout extends MachineRecipeLayout<MachineRecipe> {
    public static final RecipeComponent.Type<EnumRecipeComponent<Variant>> TYPE = EnumRecipeComponent.createType(Variant.class);

    public CanningMachineRecipeLayout(ResourceLocation id) {
        super(id, MachineRecipe::new);
        this.addComponent(TYPE, "variant", () -> new EnumRecipeComponent<>(Variant.DEFAULT));
        this.addComponent(ItemInputListComponent.TYPE, "inputs");
        this.addComponent(ItemOutputComponent.TYPE, "output", () -> new ItemOutputComponent(ItemStack.EMPTY));
        this.addComponent(EnergyInputComponent.TYPE, "energy", () -> new EnergyInputComponent(800));
        this.addComponent(TimeComponent.TYPE, "duration", () -> new TimeComponent(200));
    }

    @Override
    public boolean matches(MachineRecipe recipe, MachineRecipeInput input, Level level) {
        Variant variant = this.getVariant(recipe);
        if (variant == Variant.FOOD_CANNING) {
            if (input.getItem(0).has(DataComponents.FOOD) || input.getItem(1).has(DataComponents.FOOD)) {
                ItemInputComponentFlag recipeInput = recipe.getComponentByFlag(IRRecipeComponentFlags.ITEM_INPUT);
                for (IngredientWithCount ingredient : recipeInput.getIngredients()) {
                    if (ingredient.test(input.getItem(0)) || ingredient.test(input.getItem(1))) return true;
                }
            }
            return false;
        }
        return super.matches(recipe, input, level);
    }

    @Override
    public ItemStack createResultItem(MachineRecipe recipe, @Nullable MachineRecipeInput input, HolderLookup.Provider provider) {
        if (this.getVariant(recipe) == Variant.FOOD_CANNING) {
            if (input != null) {
                FoodProperties food;
                if (input.getItem(0).has(DataComponents.FOOD)) {
                    food = input.getItem(0).get(DataComponents.FOOD);
                } else if (input.getItem(1).has(DataComponents.FOOD)) {
                    food = input.getItem(1).get(DataComponents.FOOD);
                } else {
                    return ItemStack.EMPTY;
                }

                ItemStack foodCanStack = IRItems.TIN_CAN_FOOD.toStack();

                food = new FoodProperties(food.nutrition(), food.saturation(), food.canAlwaysEat(), food.eatSeconds(), Optional.of(IRItems.TIN_CAN.toStack()), food.effects());

                foodCanStack.set(DataComponents.FOOD, food);

                return foodCanStack;
            }
            return IRItems.TIN_CAN_FOOD.toStack();
        }
        return super.createResultItem(recipe, input, provider);
    }

    private Variant getVariant(MachineRecipe recipe) {
        EnumRecipeComponent<Variant> variantComp = recipe.getComponent(TYPE);
        return variantComp != null ? variantComp.value() : Variant.DEFAULT;
    }

    public enum Variant implements StringRepresentable {
        DEFAULT("default"),
        FOOD_CANNING("food_canning");

        private final String name;

        Variant(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }

    }

}
