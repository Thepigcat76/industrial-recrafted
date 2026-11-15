package com.portingdeadmods.indrec.content.items;

import com.portingdeadmods.indrec.IndustrialRecrafted;

import com.portingdeadmods.indrec.data.components.ToolboxContents;
import com.portingdeadmods.indrec.registries.IRDataComponents;
import com.portingdeadmods.indrec.registries.IRTranslations;
import com.portingdeadmods.indrec.tags.IRTags;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.Optional;

public class ToolboxItem extends Item {
    private static final int BAR_COLOR = Mth.color(0.4F, 0.4F, 1.0F);

    public ToolboxItem(Properties properties) {
        super(properties.stacksTo(1).component(IRDataComponents.TOOLBOX_CONTENTS, ToolboxContents.EMPTY));
    }

    public static ToolboxContents getContents(ItemStack toolbox) {
        return toolbox.getOrDefault(IRDataComponents.TOOLBOX_CONTENTS, ToolboxContents.EMPTY);
    }

    public static boolean canStore(ItemStack stack) {
        return !stack.isEmpty()
                && stack.is(IRTags.ItemTags.TOOLBOX_STORABLE)
                && !(stack.getItem() instanceof ToolboxItem)
                && stack.canFitInsideContainerItems();
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack toolbox, Slot slot, ClickAction action, Player player) {
        if (toolbox.getCount() != 1 || action != ClickAction.SECONDARY) return false;

        ToolboxContents contents = getContents(toolbox);
        ItemStack slotItem = slot.getItem();
        if (slotItem.isEmpty()) {
            if (!contents.isEmpty()) {
                ItemStack remainder = slot.safeInsert(contents.getSelectedUnsafe().copy());
                if (remainder.isEmpty()) {
                    toolbox.set(IRDataComponents.TOOLBOX_CONTENTS, contents.removeSelected());
                    playRemoveSound(player);
                }
            }
        } else if (canStore(slotItem) && contents.canInsert(slotItem)) {
            ItemStack taken = slot.safeTake(slotItem.getCount(), slotItem.getCount(), player);
            if (!taken.isEmpty()) {
                toolbox.set(IRDataComponents.TOOLBOX_CONTENTS, contents.insert(taken));
                playInsertSound(player);
            }
        }
        return true;
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack toolbox, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (toolbox.getCount() != 1 || action != ClickAction.SECONDARY || !slot.allowModification(player)) return false;

        ToolboxContents contents = getContents(toolbox);
        if (other.isEmpty()) {
            if (!contents.isEmpty()) {
                access.set(contents.getSelectedUnsafe().copy());
                toolbox.set(IRDataComponents.TOOLBOX_CONTENTS, contents.removeSelected());
                playRemoveSound(player);
            }
        } else if (canStore(other) && contents.canInsert(other)) {
            toolbox.set(IRDataComponents.TOOLBOX_CONTENTS, contents.insert(other));
            access.set(ItemStack.EMPTY);
            playInsertSound(player);
        }
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack toolbox = player.getItemInHand(hand);
        if (toolbox.getCount() != 1) return InteractionResultHolder.pass(toolbox);
        if (level.isClientSide()) return InteractionResultHolder.success(toolbox);

        ToolboxContents contents = getContents(toolbox);
        Inventory inventory = player.getInventory();
        boolean collected = false;
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack == toolbox || !canStore(stack) || contents.findSlotFor(stack, true) == -1) continue;
            contents = contents.insertFiltered(stack);
            inventory.setItem(i, ItemStack.EMPTY);
            collected = true;
        }

        if (collected) {
            toolbox.set(IRDataComponents.TOOLBOX_CONTENTS, contents);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BUNDLE_INSERT, SoundSource.PLAYERS, 0.8F, 0.8F + level.getRandom().nextFloat() * 0.4F);
        }
        return InteractionResultHolder.success(toolbox);
    }

    @Override
    public void onDestroyed(ItemEntity itemEntity) {
        ToolboxContents contents = getContents(itemEntity.getItem());
        if (!contents.isEmpty()) {
            itemEntity.getItem().set(IRDataComponents.TOOLBOX_CONTENTS, ToolboxContents.EMPTY);
            ItemUtils.onContainerDestroyed(itemEntity, contents.items().stream().filter(item -> !item.isEmpty()).map(ItemStack::copy).toList());
        }
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return Optional.of(new Tooltip(getContents(stack)));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        ToolboxContents contents = getContents(stack);
        tooltip.add(IRTranslations.TOOLBOX_FULLNESS.component(contents.count(), ToolboxContents.MAX_TOOLS).withStyle(ChatFormatting.GRAY));
        if (contents.isEmpty()) {
            tooltip.add(IRTranslations.TOOLBOX_INSERT_HINT.component().withStyle(ChatFormatting.DARK_GRAY));
        } else {
            tooltip.add(IRTranslations.TOOLBOX_SELECTED.component().withStyle(ChatFormatting.GRAY)
                    .append(contents.getSelectedUnsafe().getHoverName().copy().withStyle(ChatFormatting.WHITE)));
            tooltip.add(IRTranslations.TOOLBOX_SELECT_HINT.component().withStyle(ChatFormatting.DARK_GRAY));
        }
        // Keybind component shows the key the player bound, not the default one
        tooltip.add(IRTranslations.TOOLBOX_FILTER_HINT.component(Component.keybind("key." + IndustrialRecrafted.MODID + ".open_toolbox").withStyle(ChatFormatting.GRAY))
                .withStyle(ChatFormatting.DARK_GRAY));
        tooltip.add(IRTranslations.TOOLBOX_COLLECT_HINT.component().withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return !getContents(stack).isEmpty();
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.min(1 + 12 * getContents(stack).count() / ToolboxContents.MAX_TOOLS, 13);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return BAR_COLOR;
    }

    private static void playInsertSound(Player player) {
        player.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + player.level().getRandom().nextFloat() * 0.4F);
    }

    private static void playRemoveSound(Player player) {
        player.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + player.level().getRandom().nextFloat() * 0.4F);
    }

    public record Tooltip(ToolboxContents contents) implements TooltipComponent {
    }
}
