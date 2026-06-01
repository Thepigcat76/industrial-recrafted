package com.portingdeadmods.indrec.mixins;

import com.portingdeadmods.indrec.utils.CropBlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CropBlock.class)
public abstract class CropBlockMixin implements CropBlockGetter {
    @Shadow
    protected abstract ItemLike getBaseSeedId();

    @Shadow
    protected abstract IntegerProperty getAgeProperty();

    @Override
    public ItemLike indrec$getSeedItem() {
        return this.getBaseSeedId();
    }

    @Override
    public IntegerProperty indrec$getAgeProperty() {
        return this.getAgeProperty();
    }
}
