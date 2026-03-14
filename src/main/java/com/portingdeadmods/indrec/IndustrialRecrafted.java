package com.portingdeadmods.indrec;

import com.portingdeadmods.indrec.api.blockentities.MachineBlockEntity;
import com.portingdeadmods.indrec.api.energy.items.EnergyItem;
import com.portingdeadmods.indrec.content.recipes.MachineRecipeLayout;
import com.portingdeadmods.indrec.content.recipes.RegisterRecipeLayoutEvent;
import com.portingdeadmods.indrec.content.worldgen.IRPlacerTypes;
import com.portingdeadmods.indrec.impl.energy.ItemEnergyHandlerWrapper;
import com.portingdeadmods.indrec.registries.*;
import com.portingdeadmods.portingdeadlibs.api.config.PDLConfigHelper;
import com.portingdeadmods.portingdeadlibs.api.data.PDLDataComponents;
import com.portingdeadmods.portingdeadlibs.api.items.IFluidItem;
import com.portingdeadmods.portingdeadlibs.utils.capabilities.CapabilityRegistrationHelper;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.*;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;

import java.util.Map;

@Mod(IndustrialRecrafted.MODID)
public final class IndustrialRecrafted {
    public static final String MODID = "indrec";
    public static final String MODNAME = "Industrial Reclassified";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final ModelResourceLocation WINDMILL_BLADE_MODEL = ModelResourceLocation.standalone(rl("block/windmill_blade"));
    public static final ModelResourceLocation WATERMILL_BLADE_MODEL = ModelResourceLocation.standalone(rl("block/watermill_blade"));

    public IndustrialRecrafted(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::registerPayloads);
        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(this::registerRegistries);
        modEventBus.addListener(this::registerRecipeLayout);
        modEventBus.addListener(this::addFeaturePacks);
        modEventBus.addListener(this::registerAdditionalModels);
        modEventBus.addListener(RegisterEvent.class, event -> this.onRegister(event, modEventBus));

        IRItems.ITEMS.register(modEventBus);
        IREnergyTiers.ENERGY_TIERS.register(modEventBus);
        IRDataComponents.DATA_COMPONENT_TYPES.register(modEventBus);
        IRBlocks.BLOCKS.register(modEventBus);
        IRTranslations.TRANSLATIONS.register(modEventBus);
        IRCreativeTabs.TABS.register(modEventBus);
        IRBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
        IREntityTypes.ENTITY_TYPES.register(modEventBus);
        IRMenuTypes.MENU_TYPES.register(modEventBus);
        IRPlacerTypes.FOLIAGE_PLACERS.register(modEventBus);
        IRPlacerTypes.TRUNK_PLACERS.register(modEventBus);
        IRMachines.HELPER.register(modEventBus);
        IRArmorMaterials.ARMOR_MATERIALS.register(modEventBus);
        IRNetworks.NETWORKS.register(modEventBus);
        IRSoundEvents.SOUND_EVENTS.register(modEventBus);
        IRFluids.HELPER.register(modEventBus);

        PDLConfigHelper.registerConfig(IRConfig.class, ModConfig.Type.COMMON, modContainer);
    }

    private void addFeaturePacks(AddPackFindersEvent event) {
    }

    private void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
        event.register(WINDMILL_BLADE_MODEL);
        event.register(WATERMILL_BLADE_MODEL);
    }

    private void registerRegistries(NewRegistryEvent event) {
        event.register(IRRegistries.ENERGY_TIER);
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MODID);
    }

    private void registerRecipeLayout(RegisterRecipeLayoutEvent event) {
        IRRecipeLayouts.LAYOUTS.forEach(event::register);
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof EnergyItem energyItem) {
                event.registerItem(IRCapabilities.ENERGY_ITEM,
                        (stack, ctx) -> new ItemEnergyHandlerWrapper(stack, energyItem::getEnergyTier, energyItem.getDefaultCapacity()), item);
            }

            if (item instanceof IFluidItem fluidItem) {
                event.registerItem(Capabilities.FluidHandler.ITEM,
                        (stack, ctx) -> new FluidHandlerItemStack(PDLDataComponents.FLUID, stack, fluidItem.getFluidCapacity()), item);
            }

        }

        for (DeferredHolder<BlockEntityType<?>, ? extends BlockEntityType<?>> be : IRBlockEntityTypes.BLOCK_ENTITY_TYPES.getEntries()) {
            Block validBlock = be.get().getValidBlocks().stream().iterator().next();
            BlockEntity testBE = be.get().create(BlockPos.ZERO, validBlock.defaultBlockState());
            if (testBE instanceof MachineBlockEntity containerBlockEntity) {
                if (containerBlockEntity.getEuStorage() != null) {
                    event.registerBlockEntity(IRCapabilities.ENERGY_BLOCK, (BlockEntityType<MachineBlockEntity>) be.get(), MachineBlockEntity::getEuHandlerOnSide);
                }
            }
            CapabilityRegistrationHelper.registerBECaps(event, IRBlockEntityTypes.BLOCK_ENTITY_TYPES);
        }


    }

    private void onRegister(RegisterEvent event, IEventBus modEventBus) {
        if (event.getRegistryKey().equals(Registries.RECIPE_SERIALIZER)) {
            modEventBus.post(new RegisterRecipeLayoutEvent());

            for (Map.Entry<ResourceLocation, MachineRecipeLayout<?>> entry : RegisterRecipeLayoutEvent.LAYOUTS.entrySet()) {
                event.register(Registries.RECIPE_SERIALIZER, entry.getKey(), () -> entry.getValue().getRecipeSerializer());
            }
        }
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
