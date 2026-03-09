package com.portingdeadmods.indrec.content.blockentities;

import com.portingdeadmods.indrec.IRConfig;
import com.portingdeadmods.indrec.api.blockentities.GeneratorBlockEntity;
import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;
import com.portingdeadmods.indrec.impl.energy.EnergyHandlerImpl;
import com.portingdeadmods.indrec.registries.IREnergyTiers;
import com.portingdeadmods.indrec.registries.IRMachines;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

public class WaterMillBlockEntity extends MachineBlockEntity implements GeneratorBlockEntity {
    private float independentAngle;
    private float chasingVelocity;
    public int spinningDirection;

    private float fluidLevel;

    public WaterMillBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRMachines.WATER_MILL, blockPos, blockState);
        addEuStorage(EnergyHandlerImpl.NoFill::new, IREnergyTiers.MEDIUM, IRConfig.waterMillEnergyCapacity, this::onEuChanged);
    }

    @Override
    protected void onEuChanged(int oldAmount) {
        this.updateData();
    }

    @Override
    public void tick() {
        super.tick();

        float actualSpeed = (float) (getRawSpeed(this.fluidLevel) * 500f);
        chasingVelocity += ((actualSpeed * 10 / 3f) - chasingVelocity) * .25f;
        independentAngle += chasingVelocity;

        GeneratorBlockEntity.transportEnergy(this.level, this.worldPosition, this.getEuStorage());
    }

    @Override
    protected void tickRecipe() {
        BlockState state = this.getBlockState();
        Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        BlockPos fluidPos = this.worldPosition.relative(facing);
        FluidState fluidState = this.level.getBlockState(fluidPos).getFluidState();

        if (!fluidState.isEmpty() && fluidState.getType() instanceof FlowingFluid) {
            this.fluidLevel = fluidState.getHeight(this.level, fluidPos);

            Vec3 flow = fluidState.getFlow(level, fluidPos);
            Vec3 horizontalFlow = new Vec3(flow.x, 0, flow.z).normalize();
            Vec3 facingVec = Vec3.atLowerCornerOf(facing.getNormal());

            double crossY = horizontalFlow.x * facingVec.z - horizontalFlow.z * facingVec.x;
            if (crossY > 0.01) {
                this.spinningDirection = 1;
            } else if (crossY < -0.01) {
                this.spinningDirection = -1;
            } else {
                this.spinningDirection = 0;
            }
        } else {
            this.fluidLevel = 0;
            this.spinningDirection = 0;
        }

        int amount = (int) (this.getGenerationAmount() * getRawSpeed(this.fluidLevel));
        this.getEuStorage().forceFillEnergy(amount, false);

    }

    @Override
    public boolean shouldSpreadEnergy() {
        return true;
    }

    public static double getRawSpeed(float fluidLevel) {
        if (fluidLevel == 0 || fluidLevel == 1) {
            return 0;
        }
        double k = 10; // smaller = wider, larger = sharper
        return Math.exp(-k * Math.pow(fluidLevel - 0.5, 2));
    }

    public float getIndependentAngle(float partialTicks) {
        return (independentAngle + partialTicks * chasingVelocity) / 360;
    }

    @Override
    public int getGenerationAmount() {
        return IRConfig.waterMillEnergyProduction;
    }

}
