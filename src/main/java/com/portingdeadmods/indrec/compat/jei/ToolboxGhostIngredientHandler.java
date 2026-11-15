package com.portingdeadmods.indrec.compat.jei;

import com.portingdeadmods.indrec.client.screens.ToolboxFilterScreen;
import com.portingdeadmods.indrec.content.items.ToolboxItem;
import com.portingdeadmods.indrec.content.menus.ToolboxFilterMenu;
import com.portingdeadmods.indrec.networking.serverbound.SetToolboxFilterPayload;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Allows dragging tools from JEI onto the filter slots of the toolbox screen
 */
public class ToolboxGhostIngredientHandler implements IGhostIngredientHandler<ToolboxFilterScreen> {
    @Override
    public <I> List<Target<I>> getTargetsTyped(ToolboxFilterScreen screen, ITypedIngredient<I> ingredient, boolean doStart) {
        Optional<ItemStack> stack = ingredient.getItemStack();
        if (stack.isEmpty() || !ToolboxItem.canStore(stack.get())) return List.of();

        List<Target<I>> targets = new ArrayList<>();
        ToolboxFilterMenu menu = screen.getMenu();
        for (Slot slot : menu.slots) {
            if (!(slot instanceof ToolboxFilterMenu.FilterSlot)) continue;
            int index = slot.index;
            Rect2i area = new Rect2i(screen.getGuiLeft() + slot.x, screen.getGuiTop() + slot.y, 16, 16);
            targets.add(new Target<>() {
                @Override
                public Rect2i getArea() {
                    return area;
                }

                @Override
                public void accept(I ignored) {
                    ItemStack filter = stack.get().copyWithCount(1);
                    menu.setFilter(index, filter);
                    PacketDistributor.sendToServer(new SetToolboxFilterPayload(index, filter));
                }
            });
        }
        return targets;
    }

    @Override
    public void onComplete() {
    }
}
