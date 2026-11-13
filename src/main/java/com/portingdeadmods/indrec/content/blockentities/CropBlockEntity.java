package com.portingdeadmods.indrec.content.blockentities;

import com.portingdeadmods.indrec.IRRegistries;
import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.api.crops.Crop;
import com.portingdeadmods.indrec.networking.clientbound.SetCropAgePayload;
import com.portingdeadmods.indrec.networking.clientbound.SetCropPayload;
import com.portingdeadmods.indrec.registries.IRBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CropBlockEntity extends BlockEntity {
    private Holder<Crop> crop;
    private int age;
    private int maxAge;

    public CropBlockEntity(BlockPos pos, BlockState blockState) {
        super(IRBlockEntityTypes.CROP.get(), pos, blockState);
    }

    public void plant(Holder<Crop> crop) {
        this.plant(crop, 0);
    }

    public void plant(Holder<Crop> crop, int age) {
        if (crop == null) {
            this.removeCrop();
            return;
        }
        this.crop = crop;
        this.maxAge = crop.value().maxStage();
        this.age = Mth.clamp(age, 0, this.maxAge);
        this.setChanged();
        this.syncToClient();
    }

    public void removeCrop() {
        this.crop = null;
        this.age = 0;
        this.maxAge = 0;
        this.setChanged();
        this.syncToClient();
    }

    public Optional<ItemStack> createSeedDrop(RandomSource random) {
        if (this.crop == null) return Optional.empty();

        return this.crop.value().createSeedDrop(random);
    }

    private void syncToClient() {
        if (this.level != null && !this.level.isClientSide()) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
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
                this.age = tag.getInt("age");
                this.maxAge = crop.value().maxStage();
                return;
            }
            IndustrialRecrafted.LOGGER.error("Cannot load crop with id: {}", cropString);
        }

        // No (valid) crop, also handles the client receiving an update after the crop was removed
        this.crop = null;
        this.age = 0;
        this.maxAge = 0;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        Holder<Crop> crop = this.getCrop();
        if (crop != null) {
            tag.putString("crop", crop.getKey().location().toString());
        }

        tag.putInt("age", this.age);

    }

    public int getAge() {
        return this.age;
    }

    public int getMaxAge() {
        return this.maxAge;
    }

    public void setAge(int age) {
        this.age = age;
        this.setChanged();
    }

    public void setCrop(Holder<Crop> crop) {
        this.crop = crop;
        this.maxAge = crop.value().maxStage();
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
