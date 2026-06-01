package com.portingdeadmods.indrec.content.recipes.layouts;

import com.portingdeadmods.indrec.content.recipes.MachineRecipeImpl;
import com.portingdeadmods.indrec.api.recipes.MachineRecipeLayout;
import com.portingdeadmods.indrec.content.recipes.components.energy.EnergyOutputComponent;
import com.portingdeadmods.indrec.content.recipes.components.fluids.FluidInputComponent;
import net.minecraft.resources.ResourceLocation;

public class GeothermalGeneratorRecipeLayout extends MachineRecipeLayout<MachineRecipeImpl> {
    public GeothermalGeneratorRecipeLayout(ResourceLocation id) {
        super(id, MachineRecipeImpl::new);
        this.addComponent(FluidInputComponent.TYPE, "input_fluid");
        this.addComponent(EnergyOutputComponent.TYPE, "output_energy");
    }
}
