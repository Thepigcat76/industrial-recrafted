package com.portingdeadmods.indrec.impl.recipes.layouts;

import com.portingdeadmods.indrec.impl.recipes.MachineRecipeImpl;
import com.portingdeadmods.indrec.api.recipes.MachineRecipeLayout;
import com.portingdeadmods.indrec.impl.recipes.components.energy.EnergyOutputComponent;
import com.portingdeadmods.indrec.impl.recipes.components.fluids.FluidInputComponent;
import net.minecraft.resources.ResourceLocation;

public class GeothermalGeneratorRecipeLayout extends MachineRecipeLayout<MachineRecipeImpl> {
    public GeothermalGeneratorRecipeLayout(ResourceLocation id) {
        super(id, MachineRecipeImpl::new);
        this.addComponent(FluidInputComponent.TYPE, "input_fluid");
        this.addComponent(EnergyOutputComponent.TYPE, "output_energy");
    }
}
