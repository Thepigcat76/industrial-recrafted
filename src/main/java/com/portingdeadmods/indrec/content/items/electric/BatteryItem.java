package com.portingdeadmods.indrec.content.items.electric;

import com.portingdeadmods.indrec.IRConfig;
import com.portingdeadmods.indrec.registries.IRDataComponents;
import com.portingdeadmods.indrec.api.energy.EnergyHandler;
import com.portingdeadmods.indrec.api.energy.EnergyTier;
import com.portingdeadmods.indrec.api.energy.items.SimpleEnergyItem;
import com.portingdeadmods.indrec.registries.IREnergyTiers;
import com.portingdeadmods.indrec.registries.IRTranslations;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class BatteryItem extends SimpleEnergyItem {
    private final int stages;
    private final boolean barVisible;

    public BatteryItem(Properties properties, Supplier<? extends EnergyTier> energyTier, IntSupplier defaultEnergyCapacity, int stages, boolean barVisible) {
        super(properties, energyTier, defaultEnergyCapacity);
        this.stages = stages;
        this.barVisible = barVisible;
    }

    public static BatteryItem batteryItem(Properties properties) {
        return new BatteryItem(properties, IREnergyTiers.LOW, () -> IRConfig.basicBatteryCapacity, 6, false);
    }

    public static BatteryItem energyCrystalItem(Properties properties) {
        return new BatteryItem(properties, IREnergyTiers.HIGH, () -> IRConfig.energyCrystalCapacity, 2, true);
    }

    public static BatteryItem lapotronCrystalItem(Properties properties) {
        return new BatteryItem(properties, IREnergyTiers.INSANE, () -> IRConfig.lapotronCrystalCapacity, 2, true);
    }

    public int getStages() {
        return stages;
    }

    public float getBatteryStage(ItemStack itemStack) {
        EnergyHandler energyStorage = getEnergyCap(itemStack);
        return ((float) energyStorage.getEnergyStored() / energyStorage.getEnergyCapacity()) * (this.stages - 1);
    }

    @Override
    public boolean isBarVisible(ItemStack p_150899_) {
        return this.barVisible;
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pEntity instanceof Player player && pStack.getOrDefault(IRDataComponents.ACTIVE, false)) {
            if (pLevel.getGameTime() % 3 != 0) return;
            EnergyHandler batteryHandler = getEnergyCap(pStack);
            if (batteryHandler.getEnergyStored() <= 0) return;
            for (ItemStack itemStack : player.getInventory().items) {
                if (pStack.equals(itemStack)) continue;
                EnergyHandler energyStorage = getEnergyCap(itemStack);
                if (energyStorage != null) {
                    int canAccept = energyStorage.fillEnergy(getEnergyTier().maxOutput(), true);
                    if (canAccept > 0) {
                        int drained = batteryHandler.drainEnergy(canAccept, false);
                        energyStorage.fillEnergy(drained, false);
                    }
                } else {
                    IEnergyStorage feEnergyStorage = itemStack.getCapability(Capabilities.EnergyStorage.ITEM);
                    if (feEnergyStorage == null) continue;
                    int canAccept = feEnergyStorage.receiveEnergy(getEnergyTier().maxOutput(), true);
                    if (canAccept > 0) {
                        int drained = batteryHandler.drainEnergy(canAccept, false);
                        feEnergyStorage.receiveEnergy(drained, false);
                    }
                }
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        if (pPlayer.isShiftKeyDown()) {
            itemStack.set(IRDataComponents.ACTIVE, !itemStack.getOrDefault(IRDataComponents.ACTIVE, false));
            return InteractionResultHolder.success(itemStack);
        }
        return InteractionResultHolder.fail(itemStack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag p41424) {
        MutableComponent activeTooltip = Component.literal("Charging: ").withStyle(ChatFormatting.GRAY);

        if (stack.getOrDefault(IRDataComponents.ACTIVE, false)) {
            activeTooltip.append(IRTranslations.ACTIVE.component().withStyle(ChatFormatting.GREEN));
        } else {
            activeTooltip.append(IRTranslations.INACTIVE.component().withStyle(ChatFormatting.RED));
        }

        tooltip.add(activeTooltip);

        super.appendHoverText(stack, ctx, tooltip, p41424);
    }
}