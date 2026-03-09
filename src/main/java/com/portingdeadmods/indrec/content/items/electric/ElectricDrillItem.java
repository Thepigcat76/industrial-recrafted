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

public class ElectricDrillItem extends ElectricDiggerItem {
    public ElectricDrillItem(Properties properties, float attackSpeed, float baseAttackDamage, Tier tier, Supplier<? extends EnergyTier> energyTier, IntSupplier energyUsage, IntSupplier energyCapacity) {
        super(properties, attackSpeed, baseAttackDamage, tier, BlockTags.MINEABLE_WITH_PICKAXE, energyTier, energyUsage, energyCapacity);
    }

    public static ElectricDrillItem basicItem(Properties properties) {
        return new ElectricDrillItem(properties, -2.8F, 1, Tiers.IRON, IREnergyTiers.LOW, () -> IRConfig.basicDrillEnergyUsage, () -> IRConfig.basicDrillCapacity);
    }

    public static ElectricDrillItem advancedItem(Properties properties) {
        return new ElectricDrillItem(properties, -2.8F, 1, Tiers.DIAMOND, IREnergyTiers.HIGH, () -> IRConfig.advancedDrillEnergyUsage, () -> IRConfig.advancedDrillCapacity);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_PICKAXE_ACTIONS.contains(itemAbility) || ItemAbilities.DEFAULT_SHOVEL_ACTIONS.contains(itemAbility);
    }
}