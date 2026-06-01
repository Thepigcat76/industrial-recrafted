package com.portingdeadmods.indrec.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.portingdeadmods.indrec.IndustrialRecraftedClient;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BlockStateModelLoader;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mixin(ModelBakery.class)
public class ModelBakeryMixin {
    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/client/ClientHooks;onRegisterAdditionalModels(Ljava/util/Set;)V"))
    private void indrec$registerCropModels(BlockColors blockColors,
                                           ProfilerFiller profilerFiller,
                                           Map<ResourceLocation, BlockModel> modelResources,
                                           Map<ResourceLocation, List<BlockStateModelLoader.LoadedJson>> blockStateResources, CallbackInfo ci, @Local Set<ModelResourceLocation> additionalModels) {
        IndustrialRecraftedClient.registerCropModels(modelResources, additionalModels);
    }
}
