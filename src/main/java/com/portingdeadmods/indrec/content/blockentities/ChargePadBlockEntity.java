package com.portingdeadmods.indrec.content.blockentities;

import com.portingdeadmods.indrec.IRCapabilities;
import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;
import com.portingdeadmods.indrec.api.energy.EnergyHandler;
import com.portingdeadmods.indrec.impl.energy.EnergyHandlerImpl;
import com.portingdeadmods.indrec.registries.IREnergyTiers;
import com.portingdeadmods.indrec.registries.IRMachines;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class ChargePadBlockEntity extends MachineBlockEntity {
    public ChargePadBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRMachines.CHARGE_PAD, blockPos, blockState);
        this.addEuStorage(EnergyHandlerImpl.NoDrain::new, IREnergyTiers.LOW, 64000, this::onEuChanged);
    }

    @Override
    public void tick() {
        super.tick();
        List<Player> players = level.getEntitiesOfClass(Player.class, new AABB(this.worldPosition.above()));
        for (Player player : players) {
            for (ItemStack armorItem : player.getArmorSlots()) {
                EnergyHandler euStorage = armorItem.getCapability(IRCapabilities.ENERGY_ITEM);
                if (euStorage != null) {
                    euStorage.fillEnergy(1, false);
                }
            }
        }
    }

}
