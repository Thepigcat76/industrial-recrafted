package com.portingdeadmods.indrec.content.items.electric;

import com.mojang.datafixers.util.Pair;
import com.portingdeadmods.indrec.IRConfig;
import com.portingdeadmods.indrec.IRDataComponents;
import com.portingdeadmods.indrec.api.energy.items.ElectricConsumerItem;
import com.portingdeadmods.indrec.api.energy.items.EnergyItem;
import com.portingdeadmods.indrec.impl.energy.ComponentEuStorage;
import com.portingdeadmods.indrec.impl.energy.EnergyTierImpl;
import com.portingdeadmods.indrec.registries.IREnergyTiers;
import com.portingdeadmods.indrec.utils.ItemBarUtils;
import com.portingdeadmods.indrec.utils.TooltipUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ElectricHoeItem extends HoeItem implements EnergyItem, ElectricConsumerItem {
    private final Supplier<EnergyTierImpl> energyTier;
    private final IntSupplier energyUsage;
    private final IntSupplier defaultEnergyCapacity;

    public ElectricHoeItem(Properties properties, Tier tier, Supplier<EnergyTierImpl> energyTier, IntSupplier energyUsage, IntSupplier defaultEnergyCapacity) {
        super(tier, properties.stacksTo(1)
                .component(IRDataComponents.ENERGY, new ComponentEuStorage(defaultEnergyCapacity.getAsInt())));
        this.energyTier = energyTier;
        this.energyUsage = energyUsage;
        this.defaultEnergyCapacity = defaultEnergyCapacity;
    }

    public static ElectricHoeItem defaultItem() {
        return new ElectricHoeItem(new Item.Properties(), Tiers.IRON, IREnergyTiers.LOW, () -> IRConfig.electricHoeEnergyUsage, () -> IRConfig.electricHoeCapacity);
    }

    public int getEnergyUsage() {
        return energyUsage.getAsInt();
    }

    @Override
    public int getDefaultCapacity() {
        return defaultEnergyCapacity.getAsInt();
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        return ItemBarUtils.energyBarWidth(itemStack);
    }

    @Override
    public int getBarColor(ItemStack itemStack) {
        return ItemBarUtils.energyBarColor(itemStack);
    }

    @Override
    public boolean isBarVisible(ItemStack p_150899_) {
        return true;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        ItemStack itemInHand = context.getItemInHand();
        BlockState toolModifiedState = level.getBlockState(blockpos).getToolModifiedState(context, ItemAbilities.HOE_TILL, false);
        Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> pair = toolModifiedState == null ? null : Pair.of(ctx -> true, changeIntoState(toolModifiedState));

        if (pair == null) {
            return InteractionResult.PASS;
        } else {
            Player player = context.getPlayer();
            Predicate<UseOnContext> predicate = pair.getFirst();
            Consumer<UseOnContext> consumer = pair.getSecond();
            if (predicate.test(context) && canWork(itemInHand, player)) {
                level.playSound(player, blockpos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (!level.isClientSide()) {
                    consumer.accept(context);
                    consumeEnergy(itemInHand, player);
                }

                return InteractionResult.sidedSuccess(level.isClientSide());
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    @Override
    public EnergyTierImpl getEnergyTier() {
        return energyTier.get();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag p41424) {
        super.appendHoverText(stack, ctx, tooltip, p41424);

        TooltipUtils.addEnergyTooltip(tooltip, stack);
    }

    @Override
    public boolean requireEnergyToWork(ItemStack itemStack, @Nullable Entity entity) {
        return true;
    }

    @Override
    public int getEnergyUsage(ItemStack itemStack, @Nullable Entity entity) {
        return getEnergyUsage();
    }
}