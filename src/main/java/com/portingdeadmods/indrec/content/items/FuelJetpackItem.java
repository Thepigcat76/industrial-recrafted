package com.portingdeadmods.indrec.content.items;

import com.portingdeadmods.indrec.registries.IRArmorMaterials;
import com.portingdeadmods.indrec.utils.TooltipUtils;
import com.portingdeadmods.portingdeadlibs.api.data.PDLDataComponents;
import com.portingdeadmods.portingdeadlibs.api.items.IFluidItem;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import javax.swing.plaf.ToolTipUI;
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
        return new FuelJetpackItem(properties, IRArmorMaterials.FUEL_JETPACK, Type.CHESTPLATE, () -> 1, () -> 1_000);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        IFluidHandlerItem otherHandler = other.getCapability(Capabilities.FluidHandler.ITEM);
        IFluidHandler thisHandler = getFluidCap(stack);
        if (otherHandler != null) {
            FluidStack drained = otherHandler.drain(otherHandler.getFluidInTank(0).getAmount(), IFluidHandler.FluidAction.EXECUTE);
            int filled = thisHandler.fill(drained, IFluidHandler.FluidAction.EXECUTE);
            FluidStack remainder = drained.copyWithAmount(drained.getAmount() - filled);
            otherHandler.fill(remainder, IFluidHandler.FluidAction.EXECUTE);
            return true;
        }
        return super.overrideOtherStackedOnMe(stack, other, slot, action, player, access);
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
    public void drainFuel(ItemStack stack, int amount) {
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
        TooltipUtils.addFluidFillableTooltip(tooltip);
    }

}
