package com.portingdeadmods.indrec.content.blockentities;

import com.portingdeadmods.indrec.IRCapabilities;
import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;
import com.portingdeadmods.indrec.api.blocks.MachineBlock;
import com.portingdeadmods.indrec.content.menus.ExtractorMenu;
import com.portingdeadmods.indrec.impl.recipes.MachineRecipeImpl;
import com.portingdeadmods.indrec.impl.recipes.MachineRecipeInput;
import com.portingdeadmods.indrec.impl.recipes.components.TimeComponent;
import com.portingdeadmods.indrec.impl.energy.EnergyHandlerImpl;
import com.portingdeadmods.indrec.impl.items.LimitedItemHandler;
import com.portingdeadmods.indrec.registries.*;
import com.portingdeadmods.portingdeadlibs.api.utils.PDLBlockStateProperties;
import com.portingdeadmods.portingdeadlibs.utils.capabilities.HandlerUtils;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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

public class ExtractorBlockEntity extends MachineBlockEntity implements MenuProvider {
    private final IItemHandler exposedItemHandler;

    public ExtractorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRMachines.EXTRACTOR, blockPos, blockState);
        this.addEuStorage(EnergyHandlerImpl.NoDrain::new, IREnergyTiers.LOW, 4000, this::onEuChanged);
        this.addItemHandler(HandlerUtils::newItemStackHandler, builder -> builder
                .slots(4)
                .validator((slot, item) -> switch (slot) {
                    case 0 -> true;
                    case 1, 2 -> false;
                    case 3 -> item.getCapability(IRCapabilities.ENERGY_ITEM) != null;
                    default -> throw new IllegalArgumentException("Non existent slot " + slot + "on Extractor");
                })
                .onChange(this::onItemsChanged));
        this.exposedItemHandler = new LimitedItemHandler(this.getItemHandler(), IntSet.of(0), IntSet.of(1, 2), IntSet.of(3));
    }

    @Override
    protected void playMachineSound() {
        if (this.progress % 12 == 0) {
            this.level.playSound(null, worldPosition, IRSoundEvents.EXTRACTOR.get(), SoundSource.BLOCKS, 0.08f, 1f);
        }
    }

//    @Override
//    public void tickRecipe() {
//        if (!this.level.isClientSide()) {
//            if (this.cachedRecipe != null && this.getEuStorage().getEnergyStored() > 0) {
//                if (this.progress < this.getMaxProgress()) {
//                    this.progress++;
//                    this.getEuStorage().forceDrainEnergy(3, false);
//                    setActive(true);
//                } else {
//                    this.progress = 0;
//                    ItemStack resultItem = this.cachedRecipe.getResultItem(this.level.registryAccess());
//                    int inputCount = this.cachedRecipe.getComponentByFlag(IRRecipeComponentFlags.ITEM_INPUT).getIngredients().getFirst().count();
//                    forceInsertItem((IItemHandlerModifiable) this.getItemHandler(), 1, resultItem.copy(), false, this::onItemsChanged);
//                    this.getItemHandler().extractItem(0, inputCount, false);
//                    setActive(false);
//                }
//            } else if (this.progress != 0) {
//                this.progress = 0;
//                this.updateData();
//                setActive(false);
//            }
//        }
//    }

    @Override
    public IItemHandler getItemHandlerOnSide(Direction direction) {
        return this.exposedItemHandler;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return IRTranslations.EXTRACTOR.component();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new ExtractorMenu(i, inventory, this);
    }

}
