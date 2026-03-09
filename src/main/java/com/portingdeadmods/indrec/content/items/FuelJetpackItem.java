package com.portingdeadmods.indrec.content.items;

import com.portingdeadmods.indrec.registries.IRArmorMaterials;
import com.portingdeadmods.indrec.utils.TooltipUtils;
import com.portingdeadmods.portingdeadlibs.api.data.PDLDataComponents;
import com.portingdeadmods.portingdeadlibs.api.items.IFluidItem;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.List;
import java.util.function.IntSupplier;

public class FuelJetpackItem extends AbstractJetpackItem implements IFluidItem {
    private final IntSupplier fuelCapacity;
    private final IntSupplier fuelUsage;

    public FuelJetpackItem(Properties properties, Holder<ArmorMaterial> material, Type type, IntSupplier fuelUsage, IntSupplier fuelCapacity) {
        super(properties.component(PDLDataComponents.FLUID, SimpleFluidContent.EMPTY), type, material);
        this.fuelUsage = fuelUsage;
        this.fuelCapacity = fuelCapacity;
    }

    public static FuelJetpackItem defaultItem(Properties properties) {
        return new FuelJetpackItem(properties, IRArmorMaterials.FUEL_JETPACK, Type.CHESTPLATE, () -> 2, () -> 1_000);
    }

    @Override
    public int getFuelUsage(ItemStack stack, Entity entity) {
        return this.fuelUsage.getAsInt();
    }

    @Override
    public int getFuelStored(ItemStack stack) {
        return getFluidCap(stack).getFluidInTank(0).getAmount();
    }

    @Override
    public void extractFuel(ItemStack stack, int amount) {
        getFluidCap(stack).drain(amount, IFluidHandler.FluidAction.EXECUTE);
    }

    @Override
    public int getFluidCapacity() {
        return this.fuelCapacity.getAsInt();
    }

    public static IFluidHandler getFluidCap(ItemStack stack) {
        return stack.getCapability(Capabilities.FluidHandler.ITEM);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext p_339594_, List<Component> tooltip, TooltipFlag p_41424_) {
        super.appendHoverText(stack, p_339594_, tooltip, p_41424_);
        TooltipUtils.addFluidToolTipAlways(tooltip, stack, this.getFluidCapacity());
    }

}
