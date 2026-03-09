package com.portingdeadmods.indrec.content.items.electric;

import com.portingdeadmods.indrec.IRConfig;
import com.portingdeadmods.indrec.api.energy.items.ElectricDiggerItem;
import com.portingdeadmods.indrec.api.energy.EnergyTier;
import com.portingdeadmods.indrec.registries.IREnergyTiers;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class ElectricChainsawItem extends ElectricDiggerItem {

    public ElectricChainsawItem(Properties properties, float attackSpeed, float baseAttackDamage, Tier tier, Supplier<? extends EnergyTier> energyTier, IntSupplier energyUsage, IntSupplier defaultEnergyCapacity) {
        super(properties, attackSpeed, baseAttackDamage, tier, BlockTags.MINEABLE_WITH_AXE, energyTier, energyUsage, defaultEnergyCapacity);
    }

    public static ElectricChainsawItem basicItem(Properties properties) {
        return new ElectricChainsawItem(properties, -2.8F, 5, Tiers.IRON, IREnergyTiers.LOW, () -> IRConfig.basicChainsawEnergyUsage, () -> IRConfig.basicChainsawCapacity);
    }

    public static ElectricChainsawItem advancedItem(Properties properties) {
        return new ElectricChainsawItem(properties, -2.8F, 7, Tiers.DIAMOND, IREnergyTiers.HIGH, () -> IRConfig.advancedChainsawEnergyUsage, () -> IRConfig.advancedChainsawCapacity);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_AXE_ACTIONS.contains(itemAbility);
    }

}