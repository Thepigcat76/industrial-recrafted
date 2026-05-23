package com.portingdeadmods.indrec.content.items;

import com.portingdeadmods.indrec.registries.IRLootTables;
import com.portingdeadmods.indrec.registries.IRTranslations;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import java.util.List;

public class ScrapBoxItem extends Item {
    public ScrapBoxItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemInHand = player.getItemInHand(usedHand);
        if (level instanceof ServerLevel serverLevel) {
            LootTable lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(IRLootTables.SCRAP_BOX);
            LootParams params = new LootParams.Builder(serverLevel).create(LootContextParamSets.EMPTY);
            ObjectArrayList<ItemStack> randomItems = lootTable.getRandomItems(params);
            for (ItemStack randomItem : randomItems) {
                ItemHandlerHelper.giveItemToPlayer(player, randomItem, player.getInventory().selected);
            }
        }
        ItemStack newStack = !player.isCreative() ? itemInHand.copyWithCount(itemInHand.getCount() - 1) : itemInHand.copy();
        if (newStack.isEmpty()) {
            return InteractionResultHolder.success(ItemStack.EMPTY);
        }
        return InteractionResultHolder.success(newStack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(IRTranslations.SCRAP_BOX_TOOLTIP.component().withStyle(ChatFormatting.GRAY));
    }

}
