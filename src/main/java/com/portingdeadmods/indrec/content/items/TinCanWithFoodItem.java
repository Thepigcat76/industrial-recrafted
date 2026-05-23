package com.portingdeadmods.indrec.content.items;

import com.portingdeadmods.indrec.data.components.CannedFood;
import com.portingdeadmods.indrec.registries.IRDataComponents;
import com.portingdeadmods.indrec.registries.IRFoodProperties;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class TinCanWithFoodItem extends Item {
    public TinCanWithFoodItem(Properties properties) {
        super(properties);
    }

    public static TinCanWithFoodItem defaultItem(Properties properties) {
        return new TinCanWithFoodItem(properties.food(IRFoodProperties.TIN_CAN).component(IRDataComponents.CANNED_FOOD, new CannedFood(Items.AIR.builtInRegistryHolder().getKey())));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        CannedFood cannedFood = stack.get(IRDataComponents.CANNED_FOOD);
        Level level = context.level();
        if (level != null) {
            String descId = level.registryAccess().lookupOrThrow(Registries.ITEM).get(cannedFood.cannedFood()).get().value().getDescriptionId();

            tooltipComponents.add(Component.literal("Stored Food: " + Component.translatable(descId).getString()));
        }
    }
}
