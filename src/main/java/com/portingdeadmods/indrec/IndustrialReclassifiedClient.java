package com.portingdeadmods.indrec;

import com.portingdeadmods.indrec.api.fluid.SimpleFluidItem;
import com.portingdeadmods.indrec.client.blockentities.WaterMillBlockEntityRenderer;
import com.portingdeadmods.indrec.client.blockentities.WindMillBlockEntityRenderer;
import com.portingdeadmods.indrec.client.items.IRItemProperties;
import com.portingdeadmods.indrec.client.screens.*;
import com.portingdeadmods.indrec.content.blockentities.WaterMillBlockEntity;
import com.portingdeadmods.indrec.content.blockentities.WindMillBlockEntity;
import com.portingdeadmods.indrec.content.items.electric.BatteryItem;
import com.portingdeadmods.indrec.registries.*;
import com.thepigcat.transportlib.client.debug.TransportNetworkRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.TntRenderer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.model.DynamicFluidContainerModel;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = IndustrialReclassified.MODID, dist = Dist.CLIENT)
public final class IndustrialReclassifiedClient {
    public IndustrialReclassifiedClient(IEventBus modEventBus, ModContainer container) {
        modEventBus.addListener(this::registerMenuScreens);
        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::registerItemColor);
        modEventBus.addListener(this::registerEntityRenderers);
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
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            this.registerItemProperties();
            ItemBlockRenderTypes.setRenderLayer(IRBlocks.REINFORCED_DOOR.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(IRBlocks.REINFORCED_GLASS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(IRBlocks.GLASS_FIBRE_CABLE.get(), RenderType.cutout());
        });
    }

    private void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(IREntityTypes.INDUSTRIAL_TNT.get(), TntRenderer::new);

        event.registerBlockEntityRenderer((BlockEntityType<WindMillBlockEntity>) IRMachines.WIND_MILL.getBlockEntityType(), WindMillBlockEntityRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType<WaterMillBlockEntity>) IRMachines.WATER_MILL.getBlockEntityType(), WaterMillBlockEntityRenderer::new);
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
        event.register(new SimpleFluidItem.Colors(), IRItems.FLUID_CELL.get());
        event.register(new DynamicFluidContainerModel.Colors(), IRFluids.BIO_FUEL.getDeferredBucket());
//        for (PDLFluid fluid : IRFluids.HELPER.getFluids()) {
//            if (fluid instanceof MoltenMetalFluid) {
//                event.register(new DynamicFluidContainerModel.Colors(), fluid.getDeferredBucket());
//            }
//        }
    }

}
