package com.portingdeadmods.indrec.content.items.electric;

import com.portingdeadmods.indrec.IRConfig;
import com.portingdeadmods.indrec.api.energy.EnergyTier;
import com.portingdeadmods.indrec.api.energy.items.ElectricArmorItem;
import com.portingdeadmods.indrec.registries.IRArmorMaterials;
import com.portingdeadmods.indrec.registries.IREnergyTiers;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class QuantumArmorItem extends ElectricArmorItem {
    public QuantumArmorItem(Properties properties, Holder<ArmorMaterial> material, Type type, Supplier<? extends EnergyTier> energyTier, IntSupplier energyUsage, IntSupplier energyCapacity) {
        super(properties, material, type, energyTier, energyUsage, energyCapacity);
    }

    public static QuantumArmorItem defaultItem(Properties properties, Type type) {
        return new QuantumArmorItem(properties, IRArmorMaterials.QUANTUM, type, IREnergyTiers.INSANE, () -> IRConfig.quantumSuitEnergyUsage, () -> IRConfig.quantumSuitEnergyCapacity);
    }
}
