package com.portingdeadmods.indrec.api.energy.items;

import com.portingdeadmods.indrec.IRCapabilities;
import com.portingdeadmods.indrec.registries.IRDataComponents;
import com.portingdeadmods.indrec.api.energy.EnergyHandler;
import com.portingdeadmods.indrec.api.energy.EnergyTier;
import com.portingdeadmods.indrec.impl.energy.ComponentEuStorage;
import com.portingdeadmods.indrec.utils.ItemBarUtils;
import com.portingdeadmods.indrec.utils.TooltipUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public abstract class ElectricDiggerItem extends DiggerItem implements EnergyItem, ElectricConsumerItem {
    protected final float baseAttackDamage;
    protected final float attackSpeed;
    private final Tier tier;
    @org.jetbrains.annotations.NotNull
    private final TagKey<Block> blocks;
    protected final Supplier<? extends EnergyTier> energyTier;
    private final IntSupplier energyUsage;
    private final IntSupplier defaultEnergyCapacity;

    public ElectricDiggerItem(Properties properties, float attackSpeed, float baseAttackDamage, Tier tier, TagKey<Block> blocks, Supplier<? extends EnergyTier> energyTier, IntSupplier energyUsage, IntSupplier defaultEnergyCapacity) {
        super(tier, blocks, properties
                .durability(0)
                .component(IRDataComponents.ENERGY, new ComponentEuStorage(defaultEnergyCapacity.getAsInt()))
                .component(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY));
        this.baseAttackDamage = baseAttackDamage;
        this.attackSpeed = attackSpeed;
        this.energyUsage = energyUsage;
        this.blocks = blocks;
        this.energyTier = energyTier;
        this.tier = tier;
        this.defaultEnergyCapacity = defaultEnergyCapacity;
    }

    @Override
    public void initEnergyStorage(EnergyHandler energyStorage, ItemStack itemStack) {
        if (energyStorage.getEnergyStored() == 0) {
            itemStack.remove(DataComponents.TOOL);
            itemStack.remove(DataComponents.ATTRIBUTE_MODIFIERS);
        } else {
            itemStack.set(DataComponents.TOOL, tier.createToolProperties(this.blocks));
            itemStack.set(DataComponents.ATTRIBUTE_MODIFIERS, DiggerItem.createAttributes(
                    tier,
                    !requireEnergyToWork(itemStack, null)
                            || energyStorage.getEnergyStored() >= getEnergyUsage(itemStack, null)
                            ? baseAttackDamage
                            : 0,
                    attackSpeed
            ));
        }
    }

    @Override
    public void onEnergyChanged(ItemStack itemStack, int oldAmount) {
        EnergyHandler energyStorage = itemStack.getCapability(IRCapabilities.ENERGY_ITEM);

        if (energyStorage.getEnergyStored() == 0) {
            itemStack.remove(DataComponents.TOOL);
            itemStack.remove(DataComponents.ATTRIBUTE_MODIFIERS);
        } else if (oldAmount == 0) {
            itemStack.set(DataComponents.TOOL, tier.createToolProperties(this.blocks));
            itemStack.set(DataComponents.ATTRIBUTE_MODIFIERS, DiggerItem.createAttributes(
                    tier,
                    !requireEnergyToWork(itemStack, null)
                            || energyStorage.getEnergyStored() >= getEnergyUsage(itemStack, null)
                            ? baseAttackDamage
                            : 0,
                    attackSpeed
            ));
        }
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        Player player = miningEntity instanceof Player player0 ? player0 : null;
        if (requireEnergyToWork(stack, player)) {
            int energyUsage = getEnergyUsage(stack, player);
            EnergyHandler batterySource = findActiveBatteryEnergy(player);
            if (batterySource != null && batterySource.getEnergyStored() >= energyUsage) {
                batterySource.drainEnergy(energyUsage, false);
            } else if (canWork(stack, player)) {
                getEnergyCap(stack).drainEnergy(energyUsage, false);
            } else {
                return false;
            }
        }
        return true;

    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity p_40995_, LivingEntity entity) {
        Player player = entity instanceof Player player0 ? player0 : null;
        if (requireEnergyToWork(stack, player)) {
            int energyUsage = (int) (getEnergyUsage(stack, player) * 1.5f);
            EnergyHandler batterySource = findActiveBatteryEnergy(player);
            if (batterySource != null && batterySource.getEnergyStored() >= energyUsage) {
                batterySource.drainEnergy(energyUsage, false);
            } else if (canWork(stack, player)) {
                getEnergyCap(stack).drainEnergy(energyUsage, false);
            }
        }
        return true;
    }

    protected static EnergyHandler findActiveBatteryEnergy(@Nullable Player player) {
        if (player == null) return null;
        for (ItemStack itemStack : player.getInventory().items) {
            if (itemStack.isEmpty() || !(itemStack.getItem() instanceof com.portingdeadmods.indrec.content.items.electric.BatteryItem)) continue;
            if (!itemStack.getOrDefault(IRDataComponents.ACTIVE, false)) continue;
            EnergyHandler handler = itemStack.getCapability(IRCapabilities.ENERGY_ITEM);
            if (handler != null && handler.getEnergyStored() > 0) {
                return handler;
            }
        }
        return null;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_PICKAXE_ACTIONS.contains(itemAbility);
    }

    @Override
    public boolean isBarVisible(ItemStack p_150899_) {
        return true;
    }

    @Override
    public int getBarColor(ItemStack itemStack) {
        return ItemBarUtils.energyBarColor(itemStack);
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        return ItemBarUtils.energyBarWidth(itemStack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, ctx, tooltip, flag);

        TooltipUtils.addEnergyTooltip(tooltip, stack);
    }

    @Override
    public boolean requireEnergyToWork(ItemStack itemStack, Entity player) {
        return true;
    }

    @Override
    public int getEnergyUsage(ItemStack itemStack, @Nullable Entity entity) {
        return this.energyUsage.getAsInt();
    }

    @Override
    public int getDefaultCapacity() {
        return this.defaultEnergyCapacity.getAsInt();
    }

    @Override
    public EnergyTier getEnergyTier() {
        return energyTier.get();
    }
}