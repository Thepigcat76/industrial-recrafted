package com.portingdeadmods.indrec.client.blockentities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.neoforge.client.model.data.ModelData;

import java.util.HashMap;
import java.util.Map;

public class CropBlockEntityRenderer extends PDLBERenderer<CropBlockEntity> {
    private static final Direction[] DIRECTIONS = {Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, null};

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
                ResourceLocation resourceLocation = modelsForAge.get("age=" + blockEntity.getAge());
                ModelResourceLocation modeLocation = ModelResourceLocation.standalone(resourceLocation);
                BakedModel model = modelManager.getModel(modeLocation);
                renderModelWithBakedColors(poseStack.last(), bufferSource.getBuffer(RenderType.CUTOUT), blockEntity.getBlockState(), model, packedLight, packedOverlay);
            } else if (models instanceof Crop.Models.BlockModel(Block baseBlock, String ageProperty)) {
                BlockState state = baseBlock.defaultBlockState();
                IntegerProperty ageProp = this.ageProperties.computeIfAbsent(crop.getKey(), k -> state.getProperties().stream()
                        .filter(prop -> prop.getName().equals(ageProperty))
                        .findFirst()
                        .orElse(null) instanceof IntegerProperty p ? p : null);
                BlockState newState = state;
                if (ageProp != null) {
                    newState = state.setValue(ageProp, blockEntity.getAge());
                }
                this.context.getBlockRenderDispatcher().renderSingleBlock(newState, poseStack, bufferSource, packedLight, packedOverlay, ModelData.EMPTY, RenderType.CUTOUT);
            }
        }
    }

    /**
     * Renders the model like {@link net.minecraft.client.renderer.block.ModelBlockRenderer#renderModel}, but keeps the vertex colors
     * baked into the quads, so crop models can apply a static tint through the {@code neoforge_data} {@code color} of their elements.
     */
    private static void renderModelWithBakedColors(PoseStack.Pose pose, VertexConsumer consumer, BlockState state, BakedModel model, int packedLight, int packedOverlay) {
        RandomSource random = RandomSource.create();
        float[] brightness = {1f, 1f, 1f, 1f};
        int[] lightmap = {packedLight, packedLight, packedLight, packedLight};
        for (Direction direction : DIRECTIONS) {
            random.setSeed(42L);
            for (BakedQuad quad : model.getQuads(state, direction, random, ModelData.EMPTY, RenderType.CUTOUT)) {
                consumer.putBulkData(pose, quad, brightness, 1f, 1f, 1f, 1f, lightmap, packedOverlay, true);
            }
        }
    }

}
