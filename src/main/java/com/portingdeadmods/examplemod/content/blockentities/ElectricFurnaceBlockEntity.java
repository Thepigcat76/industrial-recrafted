package com.portingdeadmods.examplemod.content.blockentities;

import com.portingdeadmods.examplemod.IRCapabilities;
import com.portingdeadmods.examplemod.api.blockentities.MachineBlockEntity;
import com.portingdeadmods.examplemod.content.menus.ElectricFurnaceMenu;
import com.portingdeadmods.examplemod.impl.energy.EnergyHandlerImpl;
import com.portingdeadmods.examplemod.registries.IREnergyTiers;
import com.portingdeadmods.examplemod.registries.IRMachines;
import com.portingdeadmods.examplemod.registries.IRTranslations;
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
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ElectricFurnaceBlockEntity extends MachineBlockEntity implements MenuProvider {
    private RecipeHolder<SmeltingRecipe> cachedRecipe;
    private int progress;

    public ElectricFurnaceBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRMachines.ELECTRIC_FURNACE, blockPos, blockState);
        this.addEuStorage(EnergyHandlerImpl.NoDrain::new, IREnergyTiers.LOW, 4000, this::onEuChanged);
        this.addItemHandler(HandlerUtils::newItemStackHandler, builder -> builder
                .slots(3)
                .validator((slot, item) -> switch (slot) {
                    case 0, 1 -> true;
                    case 2 -> item.getCapability(IRCapabilities.ENERGY_ITEM) != null;
                    default -> throw new IllegalArgumentException("Non existent slot " + slot + "on Basic Generator");
                })
                .onChange(this::onItemsChanged));
    }

    public int getProgress() {
        return progress;
    }

    public int getMaxProgress() {
        return this.cachedRecipe != null ? this.cachedRecipe.value().getCookingTime() : 0;
    }

    public RecipeHolder<SmeltingRecipe> getCachedSmeltingRecipe() {
        return this.cachedRecipe;
    }

    @Override
    protected void onItemsChanged(int slot) {
        this.updateData();

        SingleRecipeInput recipeInput = new SingleRecipeInput(this.getItemHandler().getStackInSlot(0));
        RecipeHolder<SmeltingRecipe> recipe = this.level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, recipeInput, this.level)
                .orElse(null);
        if (recipe != null && this.getItemHandler().insertItem(1, recipe.value().getResultItem(this.level.registryAccess()).copy(), true).isEmpty()) {
            this.cachedRecipe = recipe;
        } else {
            this.cachedRecipe = null;
        }
    }

    @Override
    public void tickRecipe() {
        if (!this.level.isClientSide()) {
            if (this.cachedRecipe != null && this.getEuStorage().getEnergyStored() > 0) {
                if (this.progress < this.getMaxProgress()) {
                    this.progress++;
                    this.getEuStorage().forceDrainEnergy(3, false);
                } else {
                    this.progress = 0;
                    SmeltingRecipe recipe = this.cachedRecipe.value();
                    ItemStack resultItem = recipe.getResultItem(this.level.registryAccess());
                    this.getItemHandler().insertItem(1, resultItem.copy(), false);
                    this.getItemHandler().extractItem(0, 1, false);
                }
            } else if (this.progress != 0) {
                this.progress = 0;
                this.updateData();
            }
        }
    }

    @Override
    protected void loadData(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadData(tag, provider);

        this.progress = tag.getInt("progress");
    }

    @Override
    protected void saveData(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveData(tag, provider);

        tag.putInt("progress", this.progress);
    }

    @Override
    public Component getDisplayName() {
        return IRTranslations.ELECTRIC_FURNACE.component();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new ElectricFurnaceMenu(i, inventory, this);
    }
}
