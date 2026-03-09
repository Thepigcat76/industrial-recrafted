package com.portingdeadmods.indrec.content.blockentities;

import com.portingdeadmods.indrec.IRCapabilities;
import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;
import com.portingdeadmods.indrec.content.menus.CanningMachineMenu;
import com.portingdeadmods.indrec.content.recipes.MachineRecipe;
import com.portingdeadmods.indrec.content.recipes.MachineRecipeInput;
import com.portingdeadmods.indrec.content.recipes.components.TimeComponent;
import com.portingdeadmods.indrec.content.recipes.flags.ItemInputComponentFlag;
import com.portingdeadmods.indrec.impl.energy.EnergyHandlerImpl;
import com.portingdeadmods.indrec.impl.items.LimitedItemHandler;
import com.portingdeadmods.indrec.registries.*;
import com.portingdeadmods.portingdeadlibs.api.recipes.IngredientWithCount;
import com.portingdeadmods.portingdeadlibs.utils.capabilities.HandlerUtils;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CanningMachineBlockEntity extends MachineBlockEntity implements MenuProvider {
    private final IItemHandler exposedItemHandler;
    private MachineRecipe cachedRecipe;
    private int progress;

    public CanningMachineBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRMachines.CANNING_MACHINE, blockPos, blockState);
        this.addEuStorage(EnergyHandlerImpl.NoDrain::new, IREnergyTiers.LOW, 4000, this::onEuChanged);
        this.addItemHandler(HandlerUtils::newItemStackHandler, builder -> builder
                .slots(4)
                .validator((slot, item) -> switch (slot) {
                    case 0, 1 -> true;
                    case 2 -> false;
                    case 3 -> item.getCapability(IRCapabilities.ENERGY_ITEM) != null;
                    default -> throw new IllegalArgumentException("Non existent slot " + slot + "on Extractor");
                })
                .onChange(this::onItemsChanged));
        this.exposedItemHandler = new LimitedItemHandler(this.getItemHandler(), IntSet.of(0, 1), IntSet.of(2), IntSet.of(3));
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
                    ItemStack resultItem = this.cachedRecipe.assemble(this.createRecipeInput(), this.level.registryAccess());
                    ItemInputComponentFlag inputs = this.cachedRecipe.getComponentByFlag(IRRecipeComponentFlags.ITEM_INPUT);
                    forceInsertItem((IItemHandlerModifiable) this.getItemHandler(), 2, resultItem.copy(), false, this::onItemsChanged);
                    for (int i = 0; i < 2; i++) {
                        List<IngredientWithCount> ingredients = inputs.getIngredients();
                        int count;
                        if (ingredients.size() == 2) {
                            count = ingredients.get(i).count();
                        } else {
                            count = 1;
                        }
                        this.getItemHandler().extractItem(i, count, false);
                    }
                }
            } else if (this.progress != 0) {
                this.progress = 0;
                this.updateData();
            }
        }
    }

    @Override
    protected @NotNull MachineRecipeInput createRecipeInput() {
        return new MachineRecipeInput(List.of(this.getItemHandler().getStackInSlot(0), this.getItemHandler().getStackInSlot(1)));
    }

    public MachineRecipe getCachedRecipe() {
        return cachedRecipe;
    }

    public int getProgress() {
        return progress;
    }

    public int getMaxProgress() {
        return this.cachedRecipe != null ? this.cachedRecipe.getComponent(TimeComponent.TYPE).time() : 0;
    }

    @Override
    protected void onItemsChanged(int slot) {
        this.updateData();

        MachineRecipe recipe = this.level.getRecipeManager().getRecipeFor(IRRecipeLayouts.CANNING_MACHINE.getRecipeType(), this.createRecipeInput(), this.level)
                .map(RecipeHolder::value)
                .orElse(null);
        if (recipe != null && forceInsertItem((IItemHandlerModifiable) this.getItemHandler(), 2, recipe.getResultItem(this.level.registryAccess()).copy(), true, i -> {
        }).isEmpty()) {
            this.cachedRecipe = recipe;
        } else {
            this.cachedRecipe = null;
        }
    }

    @Override
    public IItemHandler getItemHandlerOnSide(Direction direction) {
        return this.exposedItemHandler;
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
    public @NotNull Component getDisplayName() {
        return IRTranslations.EXTRACTOR.component();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new CanningMachineMenu(i, inventory, this);
    }

}
