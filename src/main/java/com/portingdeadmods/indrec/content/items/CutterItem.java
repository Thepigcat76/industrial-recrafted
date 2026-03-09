package com.portingdeadmods.indrec.content.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CutterItem extends Item {
    public CutterItem(Properties properties) {
        super(properties);
    }

    public static CutterItem defaultItem(Properties properties) {
        return new CutterItem(properties.durability(80));
    }

    @Override
    public @NotNull ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        if (itemStack.getDamageValue() > 0) {
            itemStack.setDamageValue(itemStack.getDamageValue() - 1);
        } else {
            itemStack.shrink(1);
            if (itemStack.getCount() == 0) {
                return ItemStack.EMPTY;
            }
        }
        return itemStack;
    }

}
