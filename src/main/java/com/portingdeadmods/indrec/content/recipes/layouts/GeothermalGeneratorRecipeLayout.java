package com.portingdeadmods.indrec.content.recipes.layouts;

import com.portingdeadmods.indrec.content.recipes.MachineRecipe;
import com.portingdeadmods.indrec.content.recipes.MachineRecipeLayout;
import com.portingdeadmods.indrec.content.recipes.components.energy.EnergyOutputComponent;
import com.portingdeadmods.indrec.content.recipes.components.fluids.FluidInputComponent;
import net.minecraft.resources.ResourceLocation;

public class GeothermalGeneratorRecipeLayout extends MachineRecipeLayout<MachineRecipe> {
    public GeothermalGeneratorRecipeLayout(ResourceLocation id) {
        super(id, MachineRecipe::new);
        this.addComponent(FluidInputComponent.TYPE, "input_fluid");
        this.addComponent(EnergyOutputComponent.TYPE, "output_energy");
    }
}
