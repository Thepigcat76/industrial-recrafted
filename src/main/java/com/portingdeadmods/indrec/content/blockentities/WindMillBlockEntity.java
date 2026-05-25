package com.portingdeadmods.indrec.content.blockentities;

import com.portingdeadmods.indrec.IRConfig;
import com.portingdeadmods.indrec.api.blockentities.GeneratorBlockEntity;
import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;
import com.portingdeadmods.indrec.impl.energy.EnergyHandlerImpl;
import com.portingdeadmods.indrec.registries.IREnergyTiers;
import com.portingdeadmods.indrec.registries.IRMachines;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;

public class WindMillBlockEntity extends MachineBlockEntity implements GeneratorBlockEntity {
    private float independentAngle;
    private float chasingVelocity;

    public WindMillBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRMachines.WIND_MILL, blockPos, blockState);
        this.addMachineEuStorage(EnergyHandlerImpl.NoFill::new, this::onEuChanged);
    }

    @Override
    protected void onEuChanged(int oldAmount) {
        this.updateData();
    }

    @Override
    public void tick() {
        super.tick();

        float actualSpeed = (float) (getRawSpeed() * 500f);
        chasingVelocity += ((actualSpeed * 10 / 3f) - chasingVelocity) * .25f;
        independentAngle += chasingVelocity;

        GeneratorBlockEntity.transportEnergy(this.level, this.worldPosition, this.getEuStorage());
    }

    @Override
    public boolean shouldSpreadEnergy() {
        return true;
    }

    @Override
    protected void tickRecipe() {
        int amount = (int) (this.getGenerationAmount() * this.getRawSpeed());
        this.getEuStorage().forceFillEnergy(amount, false);
    }

    private double getRawSpeed() {
        return getRawSpeed(this.worldPosition.getY(), this.level.getSeaLevel(), this.level.getMinBuildHeight(), this.level.getMaxBuildHeight(), this.level.getBiome(this.worldPosition));
    }

    public float getIndependentAngle(float partialTicks) {
        return (independentAngle + partialTicks * chasingVelocity) / 360;
    }

    public static double getRawSpeed(int yPos, int seaLevel, int minY, int maxY, Holder<Biome> biome) {
        float mod;
        if (biome.is(BiomeTags.IS_OCEAN)) {
            mod = 1.5f;
        } else {
            mod = 1f;
        }
        if (yPos < seaLevel) {
            return 0;
        }

        double y = yPos - minY;
        double height = maxY - minY - seaLevel;
        return (y / height) * mod;
    }

    @Override
    public int getGenerationAmount() {
        return IRConfig.windMillEnergyProduction;
    }

}
