package com.portingdeadmods.examplemod.content.items.electric;

import com.portingdeadmods.examplemod.IRDataComponents;
import com.portingdeadmods.examplemod.api.energy.EnergyHandler;
import com.portingdeadmods.examplemod.api.energy.EnergyTier;
import com.portingdeadmods.examplemod.api.energy.items.ElectricConsumerItem;
import com.portingdeadmods.examplemod.api.energy.items.EnergyItem;
import com.portingdeadmods.examplemod.content.items.AbstractJetpackItem;
import com.portingdeadmods.examplemod.impl.energy.ComponentEuStorage;
import com.portingdeadmods.examplemod.registries.IRArmorMaterials;
import com.portingdeadmods.examplemod.registries.IREnergyTiers;
import com.portingdeadmods.examplemod.utils.TooltipUtils;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class ElectricJetpackItem extends AbstractJetpackItem implements EnergyItem, ElectricConsumerItem {
    private static final int STAGES = 6;
    private final Supplier<? extends EnergyTier> energyTier;
    private final IntSupplier energyUsage;
    private final IntSupplier energyCapacity;

    public ElectricJetpackItem(Properties properties, Holder<ArmorMaterial> material, Type type, Supplier<? extends EnergyTier> energyTier, IntSupplier energyUsage, IntSupplier energyCapacity) {
        super(properties.component(IRDataComponents.ENERGY, new ComponentEuStorage(energyCapacity.getAsInt())), type, material);
        this.energyTier = energyTier;
        this.energyUsage = energyUsage;
        this.energyCapacity = energyCapacity;
    }

    public static ElectricJetpackItem defaultItem(Properties properties) {
        return new ElectricJetpackItem(properties, IRArmorMaterials.ELECTRIC_JETPACK, Type.CHESTPLATE, IREnergyTiers.MEDIUM, () -> 64, () -> 16_000);
    }

    public float getJetpackStage(ItemStack stack) {
        EnergyHandler energyStorage = getEnergyCap(stack);
        return ((float) energyStorage.getEnergyStored() / energyStorage.getEnergyCapacity()) * (STAGES - 1);
    }

    @Override
    public int getFuelUsage(ItemStack stack, Entity entity) {
        return this.getEnergyUsage(stack, entity);
    }

    @Override
    public int getFuelStored(ItemStack stack) {
        return getEnergyCap(stack).getEnergyStored();
    }

    @Override
    public void extractFuel(ItemStack stack, int amount) {
        getEnergyCap(stack).drainEnergy(amount, false);
    }

    @Override
    public boolean requireEnergyToWork(ItemStack itemStack, @Nullable Entity entity) {
        return true;
    }

    @Override
    public int getEnergyUsage(ItemStack itemStack, @Nullable Entity entity) {
        return this.energyUsage.getAsInt();
    }

    @Override
    public EnergyTier getEnergyTier() {
        return this.energyTier.get();
    }

    @Override
    public int getDefaultCapacity() {
        return this.energyCapacity.getAsInt();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        TooltipUtils.addEnergyTooltip(tooltipComponents, stack);
    }
}
