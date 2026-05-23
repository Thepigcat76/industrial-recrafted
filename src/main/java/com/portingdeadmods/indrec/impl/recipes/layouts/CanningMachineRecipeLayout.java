package com.portingdeadmods.indrec.impl.recipes.layouts;

import com.portingdeadmods.indrec.api.recipes.RecipeComponent;
import com.portingdeadmods.indrec.api.recipes.RecipeComponentFlag;
import com.portingdeadmods.indrec.api.recipes.RecipeFlagType;
import com.portingdeadmods.indrec.data.components.CannedFood;
import com.portingdeadmods.indrec.impl.recipes.MachineRecipeImpl;
import com.portingdeadmods.indrec.impl.recipes.MachineRecipeInput;
import com.portingdeadmods.indrec.api.recipes.MachineRecipeLayout;
import com.portingdeadmods.indrec.impl.recipes.components.energy.EnergyInputComponent;
import com.portingdeadmods.indrec.impl.recipes.components.EnumRecipeComponent;
import com.portingdeadmods.indrec.impl.recipes.components.TimeComponent;
import com.portingdeadmods.indrec.impl.recipes.components.items.ItemInputComponent;
import com.portingdeadmods.indrec.impl.recipes.components.items.ItemInputListComponent;
import com.portingdeadmods.indrec.impl.recipes.components.items.ItemOutputComponent;
import com.portingdeadmods.indrec.impl.recipes.flags.ItemInputComponentFlag;
import com.portingdeadmods.indrec.registries.IRDataComponents;
import com.portingdeadmods.indrec.registries.IRItems;
import com.portingdeadmods.indrec.registries.IRRecipeComponentFlags;
import com.portingdeadmods.portingdeadlibs.api.recipes.IngredientWithCount;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CanningMachineRecipeLayout extends MachineRecipeLayout<MachineRecipeImpl> {
    public static final RecipeComponent.Type<EnumRecipeComponent<Variant>> TYPE = EnumRecipeComponent.createType(Variant.class);

    public CanningMachineRecipeLayout(ResourceLocation id) {
        super(id, MachineRecipeImpl::new);
        this.addComponent(TYPE, "variant", () -> new EnumRecipeComponent<>(Variant.DEFAULT));
        this.addComponent(ItemInputListComponent.TYPE, "inputs", ItemInputListComponent::new);
        this.addComponent(ItemOutputComponent.TYPE, "output", () -> new ItemOutputComponent(ItemStack.EMPTY));
        this.addComponent(EnergyInputComponent.TYPE, "energy", () -> new EnergyInputComponent(800));
        this.addComponent(TimeComponent.TYPE, "duration", () -> new TimeComponent(200));
    }

    @Override
    public boolean matches(MachineRecipeImpl recipe, MachineRecipeInput input, Level level) {
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
    public ItemStack createResultItem(MachineRecipeImpl recipe, @Nullable MachineRecipeInput input, HolderLookup.Provider provider) {
        if (this.getVariant(recipe) == Variant.FOOD_CANNING) {
            if (input != null) {
                ResourceKey<Item> inputItem;
                FoodProperties food;
                if (input.getItem(0).has(DataComponents.FOOD)) {
                    food = input.getItem(0).get(DataComponents.FOOD);
                    inputItem = input.getItem(0).getItemHolder().getKey();
                } else if (input.getItem(1).has(DataComponents.FOOD)) {
                    food = input.getItem(1).get(DataComponents.FOOD);
                    inputItem = input.getItem(1).getItemHolder().getKey();
                } else {
                    return ItemStack.EMPTY;
                }

                ItemStack foodCanStack = IRItems.TIN_CAN_FOOD.toStack();

                food = new FoodProperties(food.nutrition(), food.saturation(), food.canAlwaysEat(), food.eatSeconds(), Optional.of(IRItems.TIN_CAN.toStack()), food.effects());

                foodCanStack.set(DataComponents.FOOD, food);
                foodCanStack.set(IRDataComponents.CANNED_FOOD, new CannedFood(inputItem));

                return foodCanStack;
            }
            return IRItems.TIN_CAN_FOOD.toStack();
        }
        return super.createResultItem(recipe, input, provider);
    }

    @Override
    public <F extends RecipeComponentFlag> F getComponentByFlag(MachineRecipeImpl recipe, RecipeFlagType<F> flagType) {
        if (this.getVariant(recipe) == Variant.FOOD_CANNING) {
            if (flagType == IRRecipeComponentFlags.ITEM_INPUT) {
                return (F) new ItemInputListComponent(new ItemInputComponent(IRItems.TIN_CAN), new ItemInputComponent(Tags.Items.FOODS));
            } else if (flagType == IRRecipeComponentFlags.ITEM_OUTPUT) {
                return (F) new ItemOutputComponent(IRItems.TIN_CAN_FOOD);
            }
        }
        return super.getComponentByFlag(recipe, flagType);
    }

    private Variant getVariant(MachineRecipeImpl recipe) {
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
