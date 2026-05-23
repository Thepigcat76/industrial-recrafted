//package com.portingdeadmods.indrec.content.items.electric;
//
//import com.portingdeadmods.indrec.IRConfig;
//import com.portingdeadmods.indrec.api.CustomWrenchableBlock;
//import com.portingdeadmods.indrec.api.energy.*;
//import com.portingdeadmods.indrec.api.energy.items.ElectricConsumerItem;
//import com.portingdeadmods.indrec.api.energy.items.SimpleEnergyItem;
//import com.portingdeadmods.indrec.registries.IREnergyTiers;
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.InteractionResult;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.context.UseOnContext;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.state.BlockState;
//import net.neoforged.neoforge.items.ItemHandlerHelper;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.function.IntSupplier;
//import java.util.function.Supplier;
//
//public class ElectricWrenchItem extends SimpleEnergyItem implements ElectricConsumerItem {
//    private final IntSupplier energyUsage;
//
//    public ElectricWrenchItem(Item.Properties properties, Supplier<? extends EnergyTier> energyTier, IntSupplier energyUsage, IntSupplier defaultEnergyCapacity) {
//        super(properties, energyTier, defaultEnergyCapacity);
//        this.energyUsage = energyUsage;
//    }
//
//    public static ElectricWrenchItem defaultItem() {
//        return new ElectricWrenchItem(new Item.Properties(), IREnergyTiers.LOW, () -> IRConfig.electricWrenchEnergyUsage, () -> IRConfig.electricWrenchCapacity);
//    }
//
//    @Override
//    public boolean isDamageable(ItemStack stack) {
//        return false;
//    }
//
//    @Override
//    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
//        Player player = useOnContext.getPlayer();
//        Level level = useOnContext.getLevel();
//        BlockPos clickPos = useOnContext.getClickedPos();
//        BlockState wrenchableBlock = level.getBlockState(clickPos);
//        ItemStack itemInHand = useOnContext.getItemInHand();
//        EnergyHandler energyStorage = getEnergyCap(itemInHand);
//
//        // only on the server side
//        if (!level.isClientSide) {
//            // check if block can be wrenched
//            if (wrenchableBlock instanceof CustomWrenchableBlock iWrenchableBlockBlock
//                    && iWrenchableBlockBlock.canWrench(level, clickPos, wrenchableBlock)
//                    && player.isCrouching()
//                    && this.canWork(itemInHand, player)) {
//                // Drop the block itself instead of custom drop
//                if (iWrenchableBlockBlock.getCustomDropItem().isEmpty()) {
//                    ItemStack dropItem = wrenchableBlock.getBlock().asItem().getDefaultInstance();
//                    ItemHandlerHelper.giveItemToPlayer(player, dropItem);
//                }
//                // Drop the custom drop
//                else {
//                    ItemStack dropItem = iWrenchableBlockBlock.getCustomDropItem();
//                    ItemHandlerHelper.giveItemToPlayer(player, dropItem);
//                }
//                this.consumeEnergy(itemInHand, player);
//                level.removeBlock(clickPos, false);
//                return InteractionResult.SUCCESS;
//            }
//        }
//        return InteractionResult.FAIL;
//    }
//
//    @Override
//    public int getDefaultCapacity() {
//        return 10000;
//    }
//
//    @Override
//    public boolean requireEnergyToWork(ItemStack itemStack, @Nullable Entity entity) {
//        return true;
//    }
//
//    @Override
//    public int getEnergyUsage(ItemStack itemStack, @Nullable Entity entity) {
//        return this.energyUsage.getAsInt();
//    }
//}