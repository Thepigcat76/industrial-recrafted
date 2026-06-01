package com.portingdeadmods.indrec;

import com.portingdeadmods.indrec.api.crops.Crop;
import com.portingdeadmods.indrec.api.fluid.SimpleFluidItem;
import com.portingdeadmods.indrec.client.blockentities.CropBlockEntityRenderer;
import com.portingdeadmods.indrec.client.blockentities.WaterMillBlockEntityRenderer;
import com.portingdeadmods.indrec.client.blockentities.WindMillBlockEntityRenderer;
import com.portingdeadmods.indrec.client.items.IRItemProperties;
import com.portingdeadmods.indrec.client.screens.*;
import com.portingdeadmods.indrec.content.blockentities.WaterMillBlockEntity;
import com.portingdeadmods.indrec.content.blockentities.WindMillBlockEntity;
import com.portingdeadmods.indrec.content.items.electric.BatteryItem;
import com.portingdeadmods.indrec.registries.*;
import com.thepigcat.transportlib.client.debug.TransportNetworkRenderer;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.TntRenderer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.model.DynamicFluidContainerModel;
import net.neoforged.neoforge.common.NeoForge;

import java.util.*;

@Mod(value = IndustrialRecrafted.MODID, dist = Dist.CLIENT)
public final class IndustrialRecraftedClient {
    public static final ModelResourceLocation WINDMILL_BLADE_MODEL = ModelResourceLocation.standalone(IndustrialRecrafted.rl("block/windmill_blade"));
    public static final ModelResourceLocation WATERMILL_BLADE_MODEL = ModelResourceLocation.standalone(IndustrialRecrafted.rl("block/watermill_blade"));
    public static final Set<ModelResourceLocation> CROP_MODELS = new HashSet<>();

    public IndustrialRecraftedClient(IEventBus modEventBus, ModContainer container) {
        modEventBus.addListener(this::registerMenuScreens);
        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::registerItemColor);
        modEventBus.addListener(this::registerEntityRenderers);
        modEventBus.addListener(this::registerAdditionalModels);
        NeoForge.EVENT_BUS.addListener(TransportNetworkRenderer::renderNetworkNodes);

        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    private void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(IRMachines.BASIC_GENERATOR.getMenuType(), BasicGeneratorScreen::new);
        event.register(IRMachines.GEOTHERMAL_GENERATOR.getMenuType(), GeothermalGeneratorScreen::new);
        event.register(IRMachines.ELECTRIC_FURNACE.getMenuType(), ElectricFurnaceScreen::new);
        event.register(IRMachines.COMPRESSOR.getMenuType(), CompressorScreen::new);
        event.register(IRMachines.MACERATOR.getMenuType(), MaceratorScreen::new);
        event.register(IRMachines.EXTRACTOR.getMenuType(), ExtractorScreen::new);
        event.register(IRMachines.RECYCLER.getMenuType(), RecyclerScreen::new);
        event.register(IRMachines.CANNING_MACHINE.getMenuType(), CanningMachineScreen::new);
        event.register(IRMachines.BASIC_SOLAR_PANEL.getMenuType(), SolarPanelScreen::new);
        event.register(IRMachines.BATTERY_BOX.getMenuType(), BatteryBoxScreen::new);
        event.register(IRMachines.MATTER_FABRICATOR.getMenuType(), MatterFabricatorScreen::new);
    }

    private void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
        event.register(WINDMILL_BLADE_MODEL);
        event.register(WATERMILL_BLADE_MODEL);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            this.registerItemProperties();
            ItemBlockRenderTypes.setRenderLayer(IRBlocks.REINFORCED_DOOR.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(IRBlocks.REINFORCED_GLASS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(IRBlocks.GLASS_FIBRE_CABLE.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(IRBlocks.CROP.get(), RenderType.cutout());
        });
    }

    private void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(IREntityTypes.INDUSTRIAL_TNT.get(), TntRenderer::new);
        event.registerEntityRenderer(IREntityTypes.DYNAMITE.get(), ctx -> new ThrownItemRenderer<>(ctx, 2, false));

        event.registerBlockEntityRenderer((BlockEntityType<WindMillBlockEntity>) IRMachines.WIND_MILL.getBlockEntityType(), WindMillBlockEntityRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType<WaterMillBlockEntity>) IRMachines.WATER_MILL.getBlockEntityType(), WaterMillBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(IRBlockEntityTypes.CROP.get(), CropBlockEntityRenderer::new);
    }

    private void registerItemProperties() {
        ItemProperties.register(IRItems.NANO_SABER.get(), IRItemProperties.ACTIVE_KEY, (ClampedItemPropertyFunction) IRItemProperties::isActive);
        ItemProperties.register(IRItems.BASIC_CHAINSAW.get(), IRItemProperties.ACTIVE_KEY, (ClampedItemPropertyFunction) IRItemProperties::isItemHeld);
        ItemProperties.register(IRItems.ADVANCED_CHAINSAW.get(), IRItemProperties.ACTIVE_KEY, (ClampedItemPropertyFunction) IRItemProperties::isItemHeld);
        ItemProperties.register(IRItems.BASIC_DRILL.get(), IRItemProperties.ACTIVE_KEY, (ClampedItemPropertyFunction) IRItemProperties::isItemHeld);
        ItemProperties.register(IRItems.ADVANCED_DRILL.get(), IRItemProperties.ACTIVE_KEY, (ClampedItemPropertyFunction) IRItemProperties::isItemHeld);

        // IMPORTANT: WE DON'T USE CLAMPED ITEM PROPERTY FUNCTION HERE CUZ IT MEANS PROPERTIES CANT GO ABOVE 1
        ItemProperties.register(IRItems.ELECTRIC_JETPACK.get(), IRItemProperties.JETPACK_STAGE_KEY, IRItemProperties::getJetpackStage);
        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof BatteryItem) {
                ItemProperties.register(item, IRItemProperties.BATTERY_STAGE_KEY, IRItemProperties::getBatteryStage);
            }
        }
    }

    private void registerItemColor(RegisterColorHandlersEvent.Item event) {
        event.register(new SimpleFluidItem.Colors(), IRItems.FLUID_CELL.get());
        event.register(new SimpleFluidItem.Colors(), IRItems.JETPACK.get());
        event.register(new DynamicFluidContainerModel.Colors(), IRFluids.BIO_FUEL.getDeferredBucket());
//        for (PDLFluid fluid : IRFluids.HELPER.getFluids()) {
//            if (fluid instanceof MoltenMetalFluid) {
//                event.register(new DynamicFluidContainerModel.Colors(), fluid.getDeferredBucket());
//            }
//        }
    }

    public static void registerCropModels(Map<ResourceLocation, BlockModel> modelResources, Set<ModelResourceLocation> additionalModels) {
        for (ResourceLocation resourceLocation : modelResources.keySet()) {
            if (resourceLocation.getPath().startsWith("models/block/crop/")) {
                String path = resourceLocation.getPath();
                path = path.substring("models/".length());
                path = path.substring(0, path.length() - ".json".length());
                ModelResourceLocation location = ModelResourceLocation.standalone(resourceLocation.withPath(path));
                additionalModels.add(location);
                CROP_MODELS.add(location);
            }
        }
    }

}
