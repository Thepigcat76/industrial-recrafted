package com.portingdeadmods.indrec.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.portingdeadmods.indrec.data.maps.IRDataMaps;
import com.portingdeadmods.indrec.data.maps.MatterFabricatorAmplifier;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract Holder<Item> getItemHolder();

    @Inject(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/TooltipFlag;isAdvanced()Z", ordinal = 1))
    private void indrec$addTooltip(Item.TooltipContext tooltipContext, Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir, @Local List<Component> tooltip) {
        MatterFabricatorAmplifier amplifier = this.getItemHolder().getData(IRDataMaps.MATTER_FABRICATOR_AMPLIFIERS);
        if (amplifier != null) {
            tooltip.add(Component.literal("Matter Fabricator Amplifier").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.literal("Reduces energy required by %.1f".formatted(amplifier.energyReduction() * 100f) + "%").withStyle(ChatFormatting.GRAY));
        }

    }
}
