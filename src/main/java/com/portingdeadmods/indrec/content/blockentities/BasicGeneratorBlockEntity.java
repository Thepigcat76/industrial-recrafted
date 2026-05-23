package com.portingdeadmods.indrec.content.blockentities;

import com.portingdeadmods.indrec.IRCapabilities;
import com.portingdeadmods.indrec.IRConfig;
import com.portingdeadmods.indrec.api.blockentities.GeneratorBlockEntity;
import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;
import com.portingdeadmods.indrec.api.blocks.MachineBlock;
import com.portingdeadmods.indrec.api.energy.EnergyHandler;
import com.portingdeadmods.indrec.content.menus.BasicGeneratorMenu;
import com.portingdeadmods.indrec.impl.energy.EnergyHandlerImpl;
import com.portingdeadmods.indrec.registries.IREnergyTiers;
import com.portingdeadmods.indrec.registries.IRMachines;
import com.portingdeadmods.indrec.registries.IRTranslations;
import com.portingdeadmods.portingdeadlibs.api.utils.PDLBlockStateProperties;
import com.portingdeadmods.portingdeadlibs.utils.capabilities.HandlerUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BasicGeneratorBlockEntity extends MachineBlockEntity implements MenuProvider, GeneratorBlockEntity {
    private int burnTime;
    private int maxBurnTime;

    public BasicGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(IRMachines.BASIC_GENERATOR, pos, state);
        addEuStorage(EnergyHandlerImpl.NoFill::new, IREnergyTiers.LOW, IRConfig.basicGeneratorEnergyCapacity, this::onEuChanged);
        addItemHandler(HandlerUtils::newItemStackHandler, builder -> builder
                .slots(2)
                .validator((slot, item) -> switch (slot) {
                    case 0 -> item.getBurnTime(RecipeType.SMELTING) > 0;
                    case 1 -> item.getCapability(IRCapabilities.ENERGY_ITEM) != null;
                    default -> throw new IllegalArgumentException("Non existent slot " + slot + "on Basic Generator");
                })
                .onChange(this::onItemsChanged));
    }

    @Override
    public boolean shouldSpreadEnergy() {
        return true;
    }

    public boolean isActive() {
        return this.burnTime > 0;
    }

    public int getBurnTime() {
        return burnTime;
    }

    public int getMaxBurnTime() {
        return maxBurnTime;
    }

    @Override
    public int emitRedstoneLevel() {
        return ItemHandlerHelper.calcRedstoneFromInventory(this.getItemHandler());
    }

    @Override
    public int getGenerationAmount() {
        return IRConfig.basicGeneratorEnergyProduction;
    }

//    @Override
//    public boolean supportsUpgrades() {
//        return false;
//    }

    public void onItemsChanged(int slot) {
        //this.updateData();

        IItemHandler itemHandler = this.getItemHandler();
        if (itemHandler != null) {
            ItemStack stack = itemHandler.getStackInSlot(slot);
            int burnTime = stack.getBurnTime(RecipeType.SMELTING);
            if (burnTime > 0 && this.burnTime <= 0) {
                this.burnTime = burnTime;
                this.maxBurnTime = burnTime;
                itemHandler.extractItem(0, 1, false);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getRedstoneSignalType().isActive(this.getRedstoneSignalStrength())) {
            EnergyHandler energyStorage = this.getEuStorage();
            if (this.burnTime > 0) {
                if (!level.isClientSide()) {
                    int filled = energyStorage.forceFillEnergy(getGenerationAmount(), false);
                    if (filled > 0) {
                        this.burnTime--;
                    }
                }
                setActive(true);
            } else {
                setActive(false);
                this.maxBurnTime = 0;
                IItemHandler itemHandler = getItemHandler();
                ItemStack stack = itemHandler.getStackInSlot(0);
                int burnTime = stack.getBurnTime(RecipeType.SMELTING);
                if (burnTime > 0) {
                    this.burnTime = burnTime;
                    this.maxBurnTime = burnTime;
                    itemHandler.extractItem(0, 1, false);
                }

            }
        }

        GeneratorBlockEntity.transportEnergy(level, worldPosition, this.getEuStorage());
    }

    @Override
    protected void saveData(CompoundTag pTag, HolderLookup.Provider provider) {
        super.saveData(pTag, provider);
        pTag.putInt("burnTime", burnTime);
        pTag.putInt("maxBurnTime", maxBurnTime);
    }

    @Override
    protected void loadData(CompoundTag pTag, HolderLookup.Provider provider) {
        super.loadData(pTag, provider);
        burnTime = pTag.getInt("burnTime");
        maxBurnTime = pTag.getInt("maxBurnTime");
    }

    @Override
    public @NotNull Component getDisplayName() {
        return IRTranslations.BASIC_GENERATOR.component();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new BasicGeneratorMenu(i, inventory, this);
    }
}