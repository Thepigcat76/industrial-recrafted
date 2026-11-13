package com.portingdeadmods.indrec.content.blocks;

import com.portingdeadmods.indrec.api.blocks.MachineBlock;
import com.portingdeadmods.indrec.api.energy.EnergyTier;
import com.portingdeadmods.indrec.utils.EnergyHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.function.Supplier;

public class TransformerBlock extends MachineBlock {
    private final Supplier<? extends EnergyTier> highTier;

    public TransformerBlock(String name, Builder builder, Supplier<? extends EnergyTier> lowTier, Supplier<? extends EnergyTier> highTier) {
        super(name, builder, lowTier);
        this.highTier = highTier;
    }

    public EnergyTier getHighTier() {
        return this.highTier.get();
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        Component lowerTier = Component.empty().append(this.getEnergyTier().getDisplayName()).withColor(this.getEnergyTier().color());
        Component higherTier = Component.empty().append(this.getHighTier().getDisplayName()).withColor(this.getHighTier().color());
        tooltipComponents.add(Component.literal("Transforms Energy between ").withStyle(ChatFormatting.GRAY).append(lowerTier).append(Component.literal(" and ").withStyle(ChatFormatting.GRAY)).append(higherTier));

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    protected void addEnergyTierTooltip(List<Component> tooltipComponents) {
    }

    @Override
    protected void appendEnergyInfoTooltip(List<Component> tooltipComponents) {
        EnergyTier lowTier = this.getEnergyTier();
        EnergyTier highTier = this.getHighTier();
        tooltipComponents.add(energyInfo("Low Side Max In/Out", lowTier));
        tooltipComponents.add(energyInfo("High Side Max In/Out", highTier));
    }

    private static Component energyInfo(String label, EnergyTier tier) {
        return Component.literal("%s%s:%s %s EU/t".formatted(ChatFormatting.GRAY, label, ChatFormatting.RESET, EnergyHelper.formatEnergy(tier.maxOutput()))).withStyle(ChatFormatting.GREEN);
    }

}
