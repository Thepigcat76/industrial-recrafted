package com.portingdeadmods.indrec.content.recipes.layouts;

import com.portingdeadmods.indrec.content.recipes.MachineRecipe;
import com.portingdeadmods.indrec.content.recipes.MachineRecipeLayout;
import com.portingdeadmods.indrec.content.recipes.components.energy.EnergyInputComponent;
import com.portingdeadmods.indrec.content.recipes.components.TimeComponent;
import com.portingdeadmods.indrec.content.recipes.components.items.DynamicItemInputComponent;
import com.portingdeadmods.indrec.content.recipes.components.items.ItemOutputComponent;
import com.portingdeadmods.indrec.registries.IRItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class RecyclerRecipeLayout extends MachineRecipeLayout<MachineRecipe> {
    public RecyclerRecipeLayout(ResourceLocation id) {
        super(id, MachineRecipe::new);
        this.addComponent(DynamicItemInputComponent.TYPE, "input", () -> new DynamicItemInputComponent(RecyclerRecipeLayout::testInput));
        this.addComponent(ItemOutputComponent.TYPE, "output", () -> new ItemOutputComponent(IRItems.SCRAP.toStack(), 0.33f));
        this.addComponent(EnergyInputComponent.TYPE, "energy", () -> new EnergyInputComponent(800));
        this.addComponent(TimeComponent.TYPE, "duration", () -> new TimeComponent(200));
    }

    private static boolean testInput(List<ItemStack> items, Boolean strict) {
        return !items.isEmpty() && items.stream().anyMatch(item -> !item.isEmpty());
    }
}
