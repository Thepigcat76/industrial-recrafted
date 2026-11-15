package com.portingdeadmods.indrec.data.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

import java.util.List;

public final class ToolboxContents {
    public static final int MAX_TOOLS = 8;
    public static final ToolboxContents EMPTY = new ToolboxContents(List.of(), List.of(), 0);

    private static final Codec<List<ItemStack>> SLOTS_CODEC = ItemContainerContents.CODEC.xmap(
            ToolboxContents::toList, ItemContainerContents::fromItems);
    private static final StreamCodec<RegistryFriendlyByteBuf, List<ItemStack>> SLOTS_STREAM_CODEC = ItemContainerContents.STREAM_CODEC.map(
            ToolboxContents::toList, ItemContainerContents::fromItems);

    public static final Codec<ToolboxContents> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            SLOTS_CODEC.optionalFieldOf("items", List.of()).forGetter(ToolboxContents::items),
            SLOTS_CODEC.optionalFieldOf("filters", List.of()).forGetter(ToolboxContents::filters),
            Codec.INT.optionalFieldOf("selected", 0).forGetter(ToolboxContents::selected)
    ).apply(inst, ToolboxContents::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ToolboxContents> STREAM_CODEC = StreamCodec.composite(
            SLOTS_STREAM_CODEC,
            ToolboxContents::items,
            SLOTS_STREAM_CODEC,
            ToolboxContents::filters,
            ByteBufCodecs.VAR_INT,
            ToolboxContents::selected,
            ToolboxContents::new
    );

    private final NonNullList<ItemStack> items;
    private final NonNullList<ItemStack> filters;
    private final int selected;

    public ToolboxContents(List<ItemStack> items, List<ItemStack> filters, int selected) {
        this.items = copySlots(items, false);
        this.filters = copySlots(filters, true);
        this.selected = this.normalizeSelected(selected);
    }

    private static NonNullList<ItemStack> copySlots(List<ItemStack> stacks, boolean singleCount) {
        NonNullList<ItemStack> list = NonNullList.withSize(MAX_TOOLS, ItemStack.EMPTY);
        for (int i = 0; i < Math.min(stacks.size(), MAX_TOOLS); i++) {
            ItemStack stack = stacks.get(i);
            list.set(i, singleCount ? stack.copyWithCount(1) : stack.copy());
        }
        return list;
    }

    private static List<ItemStack> toList(ItemContainerContents contents) {
        NonNullList<ItemStack> list = NonNullList.withSize(MAX_TOOLS, ItemStack.EMPTY);
        contents.copyInto(list);
        return list;
    }

    // The selection always points to a filled slot, unless the toolbox is empty
    private int normalizeSelected(int selected) {
        if (selected >= 0 && selected < MAX_TOOLS && !this.items.get(selected).isEmpty()) return selected;
        for (int i = 0; i < MAX_TOOLS; i++) {
            if (!this.items.get(i).isEmpty()) return i;
        }
        return 0;
    }

    public List<ItemStack> items() {
        return this.items;
    }

    public List<ItemStack> filters() {
        return this.filters;
    }

    public int selected() {
        return this.selected;
    }

    public ItemStack getItemUnsafe(int slot) {
        return this.items.get(slot);
    }

    public ItemStack getFilterUnsafe(int slot) {
        return this.filters.get(slot);
    }

    public int count() {
        int count = 0;
        for (ItemStack item : this.items) {
            if (!item.isEmpty()) count++;
        }
        return count;
    }

    public boolean isEmpty() {
        return this.count() == 0;
    }

    public ItemStack getSelectedUnsafe() {
        return this.items.get(this.selected);
    }

    public static boolean matchesFilter(ItemStack filter, ItemStack stack) {
        return filter.isEmpty() || ItemStack.isSameItem(filter, stack);
    }

    public int findSlotFor(ItemStack stack) {
        return this.findSlotFor(stack, false);
    }

    public int findSlotFor(ItemStack stack, boolean filteredOnly) {
        int unfiltered = -1;
        for (int i = 0; i < MAX_TOOLS; i++) {
            if (!this.items.get(i).isEmpty()) continue;
            ItemStack filter = this.filters.get(i);
            if (filter.isEmpty()) {
                if (unfiltered == -1) unfiltered = i;
            } else if (matchesFilter(filter, stack)) {
                return i;
            }
        }
        return filteredOnly ? -1 : unfiltered;
    }

    public boolean canInsert(ItemStack stack) {
        return this.findSlotFor(stack) != -1;
    }

    public ToolboxContents withSelected(int selected) {
        return new ToolboxContents(this.items, this.filters, selected);
    }

    public ToolboxContents cycleSelected(int offset) {
        if (this.isEmpty() || offset == 0) return this;
        int step = Integer.signum(offset);
        int newSelected = this.selected;
        for (int moved = 0; moved < Math.abs(offset); moved++) {
            do {
                newSelected = Math.floorMod(newSelected + step, MAX_TOOLS);
            } while (this.items.get(newSelected).isEmpty());
        }
        return this.withSelected(newSelected);
    }

    public ToolboxContents insert(ItemStack stack) {
        return this.insertIntoSlot(stack, this.findSlotFor(stack));
    }

    public ToolboxContents insertFiltered(ItemStack stack) {
        return this.insertIntoSlot(stack, this.findSlotFor(stack, true));
    }

    private ToolboxContents insertIntoSlot(ItemStack stack, int slot) {
        if (slot == -1) return this;
        NonNullList<ItemStack> newItems = copySlots(this.items, false);
        newItems.set(slot, stack.copy());
        return new ToolboxContents(newItems, this.filters, slot);
    }

    public ToolboxContents removeSelected() {
        if (this.isEmpty()) return this;
        NonNullList<ItemStack> newItems = copySlots(this.items, false);
        newItems.set(this.selected, ItemStack.EMPTY);
        return new ToolboxContents(newItems, this.filters, this.selected).withSelectedAfter(this.selected);
    }

    private ToolboxContents withSelectedAfter(int slot) {
        for (int i = 1; i <= MAX_TOOLS; i++) {
            int candidate = (slot + i) % MAX_TOOLS;
            if (!this.items.get(candidate).isEmpty()) return this.withSelected(candidate);
        }
        return this;
    }

    public ToolboxContents withFilter(int slot, ItemStack filter) {
        NonNullList<ItemStack> newFilters = copySlots(this.filters, true);
        newFilters.set(slot, filter.copyWithCount(1));
        return new ToolboxContents(this.items, newFilters, this.selected);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof ToolboxContents other
                && this.selected == other.selected
                && ItemStack.listMatches(this.items, other.items)
                && ItemStack.listMatches(this.filters, other.filters);
    }

    @Override
    public int hashCode() {
        return 31 * (31 * ItemStack.hashStackList(this.items) + ItemStack.hashStackList(this.filters)) + this.selected;
    }

    @Override
    public String toString() {
        return "ToolboxContents{items=" + this.items + ", filters=" + this.filters + ", selected=" + this.selected + "}";
    }
}
