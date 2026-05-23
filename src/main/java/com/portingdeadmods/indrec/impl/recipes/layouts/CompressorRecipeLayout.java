package com.portingdeadmods.indrec.impl.recipes.layouts;

import com.portingdeadmods.indrec.impl.recipes.MachineRecipeImpl;
import com.portingdeadmods.indrec.api.recipes.MachineRecipeLayout;
import com.portingdeadmods.indrec.impl.recipes.components.energy.EnergyInputComponent;
import com.portingdeadmods.indrec.impl.recipes.components.TimeComponent;
import com.portingdeadmods.indrec.impl.recipes.components.items.ItemInputComponent;
import com.portingdeadmods.indrec.impl.recipes.components.items.ItemOutputComponent;
import net.minecraft.resources.ResourceLocation;

public class CompressorRecipeLayout extends MachineRecipeLayout<MachineRecipeImpl> {
    public CompressorRecipeLayout(ResourceLocation id) {
        super(id, MachineRecipeImpl::new);
        this.addComponent(ItemInputComponent.TYPE, "input");
        this.addComponent(ItemOutputComponent.TYPE, "output");
        this.addComponent(EnergyInputComponent.TYPE, "energy", () -> new EnergyInputComponent(800));
        this.addComponent(TimeComponent.TYPE, "duration", () -> new TimeComponent(200));
    }
}
