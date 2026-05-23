package com.portingdeadmods.indrec.utils;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public class ItemStackBuilder {
    private final ItemStack stack;

    private ItemStackBuilder(ItemLike item) {
        this.stack = item.asItem().getDefaultInstance();
    }

    public ItemStackBuilder count(int count) {
        this.stack.setCount(count);
        return this;
    }

    public <T> ItemStackBuilder component(Supplier<DataComponentType<T>> type, T value) {
        this.stack.set(type, value);
        return this;
    }

    public <T> ItemStackBuilder component(DataComponentType<T> type, T value) {
        this.stack.set(type, value);
        return this;
    }

    public ItemStack build() {
        return this.stack;
    }

    public static ItemStackBuilder of(ItemLike item) {
        return new ItemStackBuilder(item);
    }

}
