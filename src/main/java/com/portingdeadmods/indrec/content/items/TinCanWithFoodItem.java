package com.portingdeadmods.indrec.content.items;

import com.portingdeadmods.indrec.registries.IRFoodProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class TinCanWithFoodItem extends Item {
    public TinCanWithFoodItem(Properties properties) {
        super(properties);
    }

    public static TinCanWithFoodItem defaultItem(Properties properties) {
        return new TinCanWithFoodItem(properties.food(IRFoodProperties.TIN_CAN));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        //tooltipComponents.add(Component.literal("Food"));
    }
}
