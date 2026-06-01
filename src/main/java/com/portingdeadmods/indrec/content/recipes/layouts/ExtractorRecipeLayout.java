package com.portingdeadmods.indrec.content.recipes.layouts;

import com.portingdeadmods.indrec.content.recipes.MachineRecipeImpl;
import com.portingdeadmods.indrec.api.recipes.MachineRecipeLayout;
import com.portingdeadmods.indrec.content.recipes.components.energy.EnergyInputComponent;
import com.portingdeadmods.indrec.content.recipes.components.TimeComponent;
import com.portingdeadmods.indrec.content.recipes.components.items.ItemInputComponent;
import com.portingdeadmods.indrec.content.recipes.components.items.ItemOutputListComponent;
import net.minecraft.resources.ResourceLocation;

public class ExtractorRecipeLayout extends MachineRecipeLayout<MachineRecipeImpl> {
    public ExtractorRecipeLayout(ResourceLocation id) {
        super(id, MachineRecipeImpl::new);
        this.addComponent(ItemInputComponent.TYPE, "input");
        this.addComponent(ItemOutputListComponent.TYPE, "outputs");
        this.addComponent(EnergyInputComponent.TYPE, "energy", () -> new EnergyInputComponent(800));
        this.addComponent(TimeComponent.TYPE, "duration", () -> new TimeComponent(200));
    }
}
