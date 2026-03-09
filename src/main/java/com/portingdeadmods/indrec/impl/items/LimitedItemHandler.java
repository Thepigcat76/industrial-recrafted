package com.portingdeadmods.indrec.impl.items;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

public class LimitedItemHandler implements IItemHandler {
    private final IItemHandler inner;
    private final IntSet insertOnly;
    private final IntSet extractOnly;
    private final IntSet unmodifiable;

    public LimitedItemHandler(IItemHandler inner) {
        this(inner, new IntOpenHashSet(), new IntOpenHashSet(), new IntOpenHashSet());
    }

    public LimitedItemHandler(IItemHandler inner, IntSet insertOnly, IntSet extractOnly, IntSet unmodifiable) {
        this.inner = inner;
        this.insertOnly = insertOnly;
        this.extractOnly = extractOnly;
        this.unmodifiable = unmodifiable;
    }

    public void addInsertOnly(int slot) {
        this.insertOnly.add(slot);
    }

    public void addExtractOnly(int slot) {
        this.extractOnly.add(slot);
    }

    public void addUnmodifiable(int slot) {
        this.unmodifiable.add(slot);
    }

    @Override
    public int getSlots() {
        return this.inner.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return this.inner.getStackInSlot(i);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack itemStack, boolean simulate) {
        if (!this.extractOnly.contains(slot) && !this.unmodifiable.contains(slot)) {
            return this.inner.insertItem(slot, itemStack, simulate);
        }
        return itemStack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (!this.insertOnly.contains(slot) && !this.unmodifiable.contains(slot)) {
            return this.inner.extractItem(slot, amount, simulate);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int i) {
        return this.inner.getSlotLimit(i);
    }

    @Override
    public boolean isItemValid(int i, ItemStack itemStack) {
        return this.inner.isItemValid(i, itemStack);
    }
}
