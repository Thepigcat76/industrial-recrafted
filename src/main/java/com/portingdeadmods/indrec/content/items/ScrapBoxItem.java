package com.portingdeadmods.indrec.content.items;

import com.portingdeadmods.indrec.registries.IRLootTables;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.items.ItemHandlerHelper;

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
        ItemStack newStack = itemInHand.copyWithCount(itemInHand.getCount() - 1);
        if (newStack.isEmpty()) {
            return InteractionResultHolder.success(ItemStack.EMPTY);
        }
        return InteractionResultHolder.success(newStack);
    }
}
