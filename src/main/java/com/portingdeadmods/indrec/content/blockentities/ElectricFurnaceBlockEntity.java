package com.portingdeadmods.indrec.content.blockentities;

import com.portingdeadmods.indrec.IRCapabilities;
import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;
import com.portingdeadmods.indrec.content.menus.ElectricFurnaceMenu;
import com.portingdeadmods.indrec.impl.energy.EnergyHandlerImpl;
import com.portingdeadmods.indrec.registries.IREnergyTiers;
import com.portingdeadmods.indrec.registries.IRMachines;
import com.portingdeadmods.indrec.registries.IRTranslations;
import com.portingdeadmods.portingdeadlibs.utils.capabilities.HandlerUtils;
import net.minecraft.core.BlockPos;
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
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElectricFurnaceBlockEntity extends MachineBlockEntity implements MenuProvider {
    private RecipeHolder<SmeltingRecipe> cachedRecipe;

    public ElectricFurnaceBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRMachines.ELECTRIC_FURNACE, blockPos, blockState);
        this.addMachineEuStorage(EnergyHandlerImpl.NoDrain::new, this::onEuChanged);
        this.addItemHandler(HandlerUtils::newItemStackHandler, builder -> builder
                .slots(3)
                .validator((slot, item) -> switch (slot) {
                    case 0 -> true;
                    case 1 -> false;
                    case 2 -> item.getCapability(IRCapabilities.ENERGY_ITEM) != null;
                    default -> throw new IllegalArgumentException("Non existent slot " + slot + "on Electric Furnace");
                })
                .onChange(this::onItemsChanged));
    }

    public int getMaxProgress() {
        return this.cachedRecipe != null ? this.cachedRecipe.value().getCookingTime() : 0;
    }

    public RecipeHolder<SmeltingRecipe> getCachedSmeltingRecipe() {
        return this.cachedRecipe;
    }

    @Override
    protected void onEuChanged(int oldAmount) {
    }

    @Override
    protected void onItemsChanged(int slot) {
        SingleRecipeInput recipeInput = new SingleRecipeInput(this.getItemHandler().getStackInSlot(0));
        RecipeHolder<SmeltingRecipe> recipe = this.level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, recipeInput, this.level)
                .orElse(null);
        if (recipe != null && forceInsertItem((IItemHandlerModifiable) this.getItemHandler(), 1, recipe.value().getResultItem(this.level.registryAccess()).copy(), true, this::onItemsChanged).isEmpty()) {
            this.cachedRecipe = recipe;
        } else {
            this.cachedRecipe = null;
        }
    }

    @Override
    public void tickRecipe() {
        if (this.cachedRecipe != null && this.getEuStorage().getEnergyStored() > 0) {
            if (this.progress < this.getMaxProgress()) {
                this.progress += this.progressIncrement;
                this.getEuStorage().forceDrainEnergy(3, false);

                setActive(true);
                this.playMachineSound();
            } else {
                this.progress = 0;
                SmeltingRecipe recipe = this.cachedRecipe.value();
                ItemStack resultItem = recipe.getResultItem(this.level.registryAccess());
                forceInsertItem((IItemHandlerModifiable) this.getItemHandler(), 1, resultItem.copy(), false, this::onItemsChanged);
                this.getItemHandler().extractItem(0, 1, false);
                if (this.cachedRecipe == null) {
                    setActive(false);
                }
            }
        } else if (this.progress != 0) {
            this.progress = 0;

            setChanged();
            setActive(false);
        }
    }

    @Override
    protected void playMachineSound() {
    }

    @Override
    public @NotNull Component getDisplayName() {
        return IRTranslations.ELECTRIC_FURNACE.component();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new ElectricFurnaceMenu(i, inventory, this);
    }
}
