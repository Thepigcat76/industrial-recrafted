package com.portingdeadmods.indrec.content.menus;

import com.portingdeadmods.indrec.content.items.ToolboxItem;
import com.portingdeadmods.indrec.data.components.ToolboxContents;
import com.portingdeadmods.indrec.registries.IRDataComponents;
import com.portingdeadmods.indrec.registries.IRMenuTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ToolboxFilterMenu extends AbstractContainerMenu {
    public static final int FILTER_SLOTS_X = 17;
    public static final int FILTER_SLOTS_Y = 18;
    public static final int INVENTORY_Y = 49;
    public static final int IMAGE_HEIGHT = 132;

    private final InteractionHand hand;
    private final Player player;
    private final SimpleContainer filters;
    private final int lockedHotbarSlot;

    public ToolboxFilterMenu(int containerId, Inventory inventory, RegistryFriendlyByteBuf buf) {
        this(containerId, inventory, buf.readEnum(InteractionHand.class));
    }

    public ToolboxFilterMenu(int containerId, Inventory inventory, InteractionHand hand) {
        super(IRMenuTypes.TOOLBOX.get(), containerId);
        this.hand = hand;
        this.player = inventory.player;
        this.lockedHotbarSlot = hand == InteractionHand.MAIN_HAND ? inventory.selected : -1;
        this.filters = new SimpleContainer(ToolboxContents.MAX_TOOLS);

        ToolboxContents contents = ToolboxItem.getContents(this.getToolbox());
        for (int i = 0; i < ToolboxContents.MAX_TOOLS; i++) {
            this.filters.setItem(i, contents.getFilterUnsafe(i).copy());
            this.addSlot(new FilterSlot(this.filters, i, FILTER_SLOTS_X + i * 18, FILTER_SLOTS_Y));
        }

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                this.addSlot(new Slot(inventory, column + row * 9 + 9, 8 + column * 18, INVENTORY_Y + row * 18));
            }
        }
        for (int column = 0; column < 9; column++) {
            if (column == this.lockedHotbarSlot) {
                this.addSlot(new LockedSlot(inventory, column, 8 + column * 18, INVENTORY_Y + 58));
            } else {
                this.addSlot(new Slot(inventory, column, 8 + column * 18, INVENTORY_Y + 58));
            }
        }
    }

    public InteractionHand getHand() {
        return this.hand;
    }

    public ItemStack getToolbox() {
        return this.player.getItemInHand(this.hand);
    }

    public static boolean isFilterSlot(int slotId) {
        return slotId >= 0 && slotId < ToolboxContents.MAX_TOOLS;
    }

    public void setFilter(int slot, ItemStack filter) {
        if (!isFilterSlot(slot) || !(filter.isEmpty() || ToolboxItem.canStore(filter))) return;
        ItemStack toolbox = this.getToolbox();
        if (!(toolbox.getItem() instanceof ToolboxItem)) return;

        ItemStack singleFilter = filter.copyWithCount(1);
        this.filters.setItem(slot, singleFilter);
        toolbox.set(IRDataComponents.TOOLBOX_CONTENTS, ToolboxItem.getContents(toolbox).withFilter(slot, singleFilter));
    }

    @Override
    public void clicked(int slotId, int button, ClickType clickType, Player player) {
        if (isFilterSlot(slotId)) {
            if (clickType == ClickType.PICKUP || clickType == ClickType.QUICK_MOVE) {
                this.setFilter(slotId, this.getCarried());
            }
            return;
        }
        if (clickType == ClickType.SWAP && (button == this.lockedHotbarSlot || button == Inventory.SLOT_OFFHAND && this.hand == InteractionHand.OFF_HAND)) {
            return;
        }
        super.clicked(slotId, button, clickType, player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        if (isFilterSlot(index)) return ItemStack.EMPTY;
        ItemStack stack = this.slots.get(index).getItem();
        if (ToolboxItem.canStore(stack)) {
            for (int i = 0; i < ToolboxContents.MAX_TOOLS; i++) {
                if (this.filters.getItem(i).isEmpty()) {
                    this.setFilter(i, stack);
                    break;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.getToolbox().getItem() instanceof ToolboxItem;
    }

    public static class FilterSlot extends Slot {
        public FilterSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public boolean mayPickup(Player player) {
            return false;
        }
    }

    private static class LockedSlot extends Slot {
        public LockedSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPickup(Player player) {
            return false;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }
}
