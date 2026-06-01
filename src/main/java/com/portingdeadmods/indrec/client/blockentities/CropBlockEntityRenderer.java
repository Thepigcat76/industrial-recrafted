package com.portingdeadmods.indrec.client.blockentities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.portingdeadmods.indrec.api.crops.Crop;
import com.portingdeadmods.indrec.content.blockentities.CropBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.client.renderers.blockentities.PDLBERenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.neoforge.client.model.data.ModelData;

import java.util.HashMap;
import java.util.Map;

public class CropBlockEntityRenderer extends PDLBERenderer<CropBlockEntity> {
    private final Map<ResourceKey<Crop>, IntegerProperty> ageProperties;

    public CropBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        this.ageProperties = new HashMap<>();
    }

    @Override
    public void render(CropBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ModelManager modelManager = Minecraft.getInstance().getModelManager();
        Holder<Crop> crop = blockEntity.getCrop();
        if (crop != null) {
            Crop.Models models = crop.value().models();
            if (models instanceof Crop.Models.ForAge(Map<String, ResourceLocation> modelsForAge)) {
                ResourceLocation resourceLocation = modelsForAge.get("age=0");
                ModelResourceLocation modeLocation = ModelResourceLocation.standalone(resourceLocation);
                BakedModel model = modelManager.getModel(modeLocation);
                this.context.getBlockRenderDispatcher().getModelRenderer().renderModel(poseStack.last(), bufferSource.getBuffer(RenderType.CUTOUT), blockEntity.getBlockState(), model, 255, 255, 255, packedLight, packedOverlay, ModelData.EMPTY, RenderType.CUTOUT);
            } else if (models instanceof Crop.Models.BlockModel(Block baseBlock, String ageProperty)) {
                BlockState state = baseBlock.defaultBlockState();
                IntegerProperty ageProp = this.ageProperties.computeIfAbsent(crop.getKey(), k -> state.getProperties().stream()
                        .filter(prop -> prop.getName().equals(ageProperty))
                        .findFirst()
                        .orElse(null) instanceof IntegerProperty p ? p : null);
                BlockState newState = state;
                if (ageProp != null) {
                    newState = state.setValue(ageProp, crop.value().stages());
                }
                this.context.getBlockRenderDispatcher().renderSingleBlock(newState, poseStack, bufferSource, packedLight, packedOverlay, ModelData.EMPTY, RenderType.CUTOUT);
            }
        }
    }

}
