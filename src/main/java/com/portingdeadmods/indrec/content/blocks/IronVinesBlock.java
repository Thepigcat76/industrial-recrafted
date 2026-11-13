package com.portingdeadmods.indrec.content.blocks;

import com.mojang.serialization.MapCodec;
import com.portingdeadmods.indrec.registries.IRDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class IronVinesBlock extends MetalPlantBlock {
    public static final MapCodec<IronVinesBlock> CODEC = simpleCodec(IronVinesBlock::new);

    public IronVinesBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        entity.causeFallDamage(fallDistance + 2.0F, 1.0F, level.damageSources().source(IRDamageTypes.IRON_VINES));
    }

    @Override
    protected @NotNull MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }
}
