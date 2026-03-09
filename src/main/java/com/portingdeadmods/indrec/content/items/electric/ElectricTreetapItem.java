package com.portingdeadmods.indrec.content.items.electric;

import com.portingdeadmods.indrec.IRConfig;
import com.portingdeadmods.indrec.api.energy.items.ElectricConsumerItem;
import com.portingdeadmods.indrec.api.energy.EnergyHandler;
import com.portingdeadmods.indrec.api.energy.EnergyTier;
import com.portingdeadmods.indrec.api.energy.items.SimpleEnergyItem;
import com.portingdeadmods.indrec.content.blocks.RubberTreeResinHoleBlock;
import com.portingdeadmods.indrec.registries.IRBlocks;
import com.portingdeadmods.indrec.registries.IREnergyTiers;
import com.portingdeadmods.indrec.registries.IRItems;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class ElectricTreetapItem extends SimpleEnergyItem implements ElectricConsumerItem {
    private final IntSupplier energyUsage;
    private final IntSupplier energyCapacity;

    public ElectricTreetapItem(Properties properties, Supplier<? extends EnergyTier> energyTier, IntSupplier energyUsage, IntSupplier defaultEnergyCapacity) {
        super(properties, energyTier, defaultEnergyCapacity);
        this.energyUsage = energyUsage;
        this.energyCapacity = defaultEnergyCapacity;
    }

    public static ElectricTreetapItem defaultItem() {
        return new ElectricTreetapItem(new Item.Properties(), IREnergyTiers.LOW, () -> IRConfig.electricTreeTapEnergyUsage, () -> IRConfig.electricTreeTapCapacity);
    }

    public int getEnergyUsage() {
        return energyUsage.getAsInt();
    }

    @Override
    public int getDefaultCapacity() {
        return this.energyCapacity.getAsInt();
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();
        BlockPos blockPos = useOnContext.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);
        ItemStack itemInHand = useOnContext.getItemInHand();
        EnergyHandler energyStorage = getEnergyCap(itemInHand);

        if (blockState.is(IRBlocks.RUBBER_TREE_RESIN_HOLE.get()) && blockState.getValue(RubberTreeResinHoleBlock.RESIN)) {
            Player player = useOnContext.getPlayer();
            if (canWork(itemInHand, player)) {
                level.setBlockAndUpdate(blockPos, blockState.setValue(RubberTreeResinHoleBlock.RESIN, false));
                ItemStack resinDrop = new ItemStack(IRItems.STICKY_RESIN.get());
                RandomSource random = useOnContext.getLevel().random;
                int randomInt = random.nextInt(1, 4);
                resinDrop.setCount(randomInt);
                ItemHandlerHelper.giveItemToPlayer(player, resinDrop);
                this.consumeEnergy(itemInHand, player);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public boolean requireEnergyToWork(ItemStack itemStack, Entity player) {
        return true;
    }

    @Override
    public int getEnergyUsage(ItemStack itemStack, @Nullable Entity entity) {
        return this.energyUsage.getAsInt();
    }
}