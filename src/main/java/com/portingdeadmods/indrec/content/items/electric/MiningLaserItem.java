package com.portingdeadmods.indrec.content.items.electric;

import com.portingdeadmods.indrec.api.energy.items.ElectricConsumerItem;
import com.portingdeadmods.indrec.api.energy.EnergyTier;
import com.portingdeadmods.indrec.api.energy.items.SimpleEnergyItem;
import com.portingdeadmods.indrec.registries.IREnergyTiers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class MiningLaserItem extends SimpleEnergyItem implements ElectricConsumerItem {
    public MiningLaserItem(Properties properties, Supplier<? extends EnergyTier> energyTier, IntSupplier defaultEnergyCapacity) {
        super(properties, energyTier, defaultEnergyCapacity);
    }

    public static MiningLaserItem defaultItem(Properties properties) {
        return new MiningLaserItem(properties, IREnergyTiers.EXTREME, () -> 64000);
    }

    @Override
    public boolean requireEnergyToWork(ItemStack itemStack, @Nullable Entity entity) {
        return true;
    }

    @Override
    public int getEnergyUsage(ItemStack itemStack, @Nullable Entity entity) {
        return 0;
    }

    public static ItemStack getLaserInHand(Player player) {
        ItemStack heldItem = player.getMainHandItem();
        if (!(heldItem.getItem() instanceof MiningLaserItem)) {
            heldItem = player.getOffhandItem();
            if (!(heldItem.getItem() instanceof MiningLaserItem)) {
                return ItemStack.EMPTY;
            }
        }
        return heldItem;
    }

}
