package com.portingdeadmods.indrec.content.blockentities;

import com.portingdeadmods.indrec.IRRegistries;
import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.api.crops.Crop;
import com.portingdeadmods.indrec.registries.IRBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;

import java.util.Optional;

public class CropBlockEntity extends BlockEntity {
    private Holder<Crop> crop;
    private int age;
    private int maxAge;

    public CropBlockEntity(BlockPos pos, BlockState blockState) {
        super(IRBlockEntityTypes.CROP.get(), pos, blockState);
    }

    public void plant(Holder<Crop> crop) {
        this.crop = crop;
        if (this.crop != null) {
            this.age = 0;
            this.maxAge = crop.value().stages();
        }
    }

    public Holder<Crop> getCrop() {
        return crop;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        if (tag.contains("crop")) {
            String cropString = tag.getString("crop");
            ResourceKey<Crop> cropKey = ResourceKey.create(IRRegistries.CROP, ResourceLocation.parse(cropString));
            Optional<Holder.Reference<Crop>> cropHolder = registries.holder(cropKey);
            if (cropHolder.isPresent()) {
                this.crop = cropHolder.get();
            } else {
                IndustrialRecrafted.LOGGER.error("Cannot load crop with id: {}", cropString);
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        Holder<Crop> crop = this.getCrop();
        if (crop != null) {
            tag.putString("crop", crop.getKey().location().toString());
        }

    }

    public int getAge() {
        return this.age;
    }

    public int getMaxAge() {
        return this.maxAge;
    }
}
