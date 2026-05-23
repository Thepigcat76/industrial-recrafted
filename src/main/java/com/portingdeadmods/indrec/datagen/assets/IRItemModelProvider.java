package com.portingdeadmods.indrec.datagen.assets;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.client.items.IRItemProperties;
import com.portingdeadmods.indrec.content.items.electric.BatteryItem;
import com.portingdeadmods.indrec.registries.IRBlocks;
import com.portingdeadmods.indrec.registries.IRFluids;
import com.portingdeadmods.indrec.registries.IRItems;
import com.portingdeadmods.indrec.registries.IRMachines;
import com.portingdeadmods.indrec.utils.machines.IRMachine;
import com.portingdeadmods.portingdeadlibs.api.fluids.PDLFluid;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class IRItemModelProvider extends ItemModelProvider {
    public static final Set<? extends Block> CUSTOM_ITEM_MODELS = IRBlocks.CUSTOM_ITEM_MODELS.stream().map(Supplier::get).collect(Collectors.toSet());

    public IRItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, IndustrialRecrafted.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        IRBlocks.BLOCKS.getBlockItems().stream().filter(b -> !CUSTOM_ITEM_MODELS.contains(b.get().getBlock())).map(Supplier::get).map(BlockItem::getBlock).forEach(this::simpleBlockItem);

        basicItem(IRItems.RAW_TIN.get());
        basicItem(IRItems.RAW_URANIUM.get());
        basicItem(IRItems.RAW_IRIDIUM.get());

        basicItem(IRItems.TIN_INGOT.get());
        basicItem(IRItems.REFINED_IRON_INGOT.get());
        basicItem(IRItems.BRONZE_INGOT.get());
        basicItem(IRItems.IRIDIUM_ALLOY_INGOT.get());
        basicItem(IRItems.MIXED_METAL_INGOT.get());
        basicItem(IRItems.URANIUM_INGOT.get());

        basicItem(IRItems.IRIDIUM_INGOT.get());
        basicItem(IRItems.TIN_PLATE.get());
        basicItem(IRItems.COPPER_PLATE.get());
        basicItem(IRItems.DENSE_COPPER_PLATE.get());
        basicItem(IRItems.ADVANCED_ALLOY_PLATE.get());

        basicItem(IRItems.TIN_DUST.get());
        basicItem(IRItems.BRONZE_DUST.get());
        basicItem(IRItems.COPPER_DUST.get());
        basicItem(IRItems.GOLD_DUST.get());
        basicItem(IRItems.IRON_DUST.get());
        basicItem(IRItems.COAL_DUST.get());

        basicItem(IRItems.BASIC_CIRCUIT.get());
        basicItem(IRItems.ADVANCED_CIRCUIT.get());

        basicItem(IRItems.TREETAP.get());
        handheldItem(IRItems.WRENCH.get());
        handheldItem(IRItems.CUTTER.get());
        basicItem(IRItems.REMOTE_DETONATOR.get());
        basicItem(IRItems.DYNAMITE.get());

        handheldItem(IRItems.ELECTRIC_HOE.get());
        //handheldItem(IRItems.ELECTRIC_WRENCH.get());
        handheldItem(IRItems.ELECTRIC_TREETAP.get());
        handheldItem(IRItems.MINING_LASER.get());
        activeModel(IRItems.NANO_SABER);
        activeModel(IRItems.BASIC_CHAINSAW);
        activeModel(IRItems.ADVANCED_CHAINSAW);
        activeModel(IRItems.BASIC_DRILL);
        activeModel(IRItems.ADVANCED_DRILL);

        basicItem(IRItems.NANO_HELMET.get());
        basicItem(IRItems.NANO_CHESTPLATE.get());
        basicItem(IRItems.NANO_LEGGINGS.get());
        basicItem(IRItems.NANO_BOOTS.get());

        basicItem(IRItems.QUANTUM_HELMET.get());
        basicItem(IRItems.QUANTUM_CHESTPLATE.get());
        basicItem(IRItems.QUANTUM_LEGGINGS.get());
        basicItem(IRItems.QUANTUM_BOOTS.get());

        basicItem(IRItems.JETPACK.get());
        basicItem(IRItems.ELECTRIC_JETPACK.get());

        batteryModel(IRItems.REDSTONE_BATTERY.get());
        batteryModel(IRItems.ENERGY_CRYSTAL.get());
        batteryModel(IRItems.LAPOTRON_CRYSTAL.get());

        fluidContainerModel(IRItems.FLUID_CELL.get());
        fluidContainerModel(IRItems.JETPACK.get());
        basicItem(IRItems.FUSE.get());
        basicItem(IRItems.TIN_CAN.get());
        basicItem(IRItems.TIN_CAN_FOOD.get());

        for (PDLFluid fluid : IRFluids.HELPER.getFluids()) {
            bucket(fluid.getStillFluid());
        }

        basicItem(IRItems.STICKY_RESIN.get());
        basicItem(IRItems.RUBBER.get());

        basicItem(IRItems.SINGLE_URANIUM_FUEL_ROD.get());
        basicItem(IRItems.DOUBLE_URANIUM_FUEL_ROD.get());
        basicItem(IRItems.QUAD_URANIUM_FUEL_ROD.get());

        basicItem(IRItems.COAL_BALL.get());
        basicItem(IRItems.COMPRESSED_COAL_BALL.get());
        basicItem(IRItems.GRAPHENE.get());
        basicItem(IRItems.CARBON_FIBER.get());
        basicItem(IRItems.CARBON_MESH.get());
        basicItem(IRItems.CARBON_PLATE.get());

        basicItem(IRItems.SCRAP.get());
        basicItem(IRItems.SCRAP_BOX.get());
        basicItem(IRItems.UU_MATTER.get());

        energyStorageUnit(IRMachines.BATTERY_BOX);
        energyStorageUnit(IRMachines.BASIC_ENERGY_STORAGE_UNIT);
        energyStorageUnit(IRMachines.ADVANCED_ENERGY_STORAGE_UNIT);

        cableItem(IRBlocks.TIN_CABLE.get(), 6);
        cableItem(IRBlocks.COPPER_CABLE.get(), 6);
        cableItem(IRBlocks.GOLD_CABLE.get(), 6);
        cableItem(IRBlocks.HV_CABLE.get(), 8);
        cableItem(IRBlocks.GLASS_FIBRE_CABLE.get(), 4);
        cableItem(IRBlocks.BURNT_CABLE.get(), 4);

        basicItemBlock(IRBlocks.RUBBER_TREE_DOOR.asItem(), "tree");
        basicItemBlock(IRBlocks.RUBBER_TREE_SAPLING.asItem(), "tree");
        parentItemBlock(IRBlocks.RUBBER_TREE_BUTTON.get().asItem(), "_inventory");
        parentItemBlock(IRBlocks.RUBBER_TREE_FENCE.get().asItem(), "_inventory");
        parentItemBlock(IRBlocks.RUBBER_TREE_TRAPDOOR.get().asItem(), "_bottom");

        basicItemBlock(IRBlocks.REINFORCED_DOOR.asItem(), null);
        parentItemBlock(IRBlocks.STICKY_RESIN_SHEET.asItem(), "2");
        parentItemBlock(IRBlocks.RUBBER_SHEET.asItem(), "2");

        basicItem(IRItems.PLANT_BALL.get());

        overrideItemModel(6, basicItem(IRItems.ELECTRIC_JETPACK, itemTexture(IRItems.ELECTRIC_JETPACK).withSuffix("_0")), IRItemProperties.JETPACK_STAGE_KEY,
                i -> basicItem(IRItems.ELECTRIC_JETPACK, "_" + i));

    }

    private void energyStorageUnit(ItemLike energyStorageUnit) {
        ModelBuilder<ItemModelBuilder>.TransformsBuilder builder = parentItemBlock(energyStorageUnit.asItem())
                .guiLight(BlockModel.GuiLight.SIDE)
                .transforms();
        builder.transform(ItemDisplayContext.GUI)
                .rotation(30, 315, 270)
                .scale(0.625f, 0.625f, 0.625f)
                .end();
        builder.transform(ItemDisplayContext.GROUND)
                .rotation(0, 0, 270)
                .translation(0, 3, 0)
                .scale(0.25f, 0.25f, 0.25f)
                .end();
        builder.transform(ItemDisplayContext.FIXED)
                .rotation(0, 90, 270)
                .scale(0.5f, 0.5f, 0.5f)
                .end();
        builder.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
                .rotation(75, 135, 270)
                .translation(0f, 0.25f, 0f)
                .scale(0.375f, 0.375f, 0.375f)
                .end();
        builder.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                .rotation(0, 45, 270)
                .scale(0.4f, 0.4f, 0.4f)
                .end();
        builder.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
                .rotation(0, 225, 270)
                .scale(0.4f, 0.4f, 0.4f)
                .end();
        builder.end();
    }

    private void fluidContainerModel(ItemLike item) {
        withExistingParent(name(item), ResourceLocation.fromNamespaceAndPath("neoforge", "item/default"))
                .texture("base", itemTexture(item))
                .texture("fluid", itemTexture(item).withSuffix("_overlay"))
                .texture("particle", itemTexture(item).withSuffix("_overlay"))
                .customLoader(DynamicFluidContainerModelBuilder::begin)
                .applyTint(true)
                .flipGas(true)
                .fluid(Fluids.EMPTY);
    }

    protected void cableItem(ItemLike cable, int width) {
        ResourceLocation loc = BuiltInRegistries.ITEM.getKey(cable.asItem());
        float from = (16 - width) / 2F;
        float to = (16 + width) / 2F;

        ItemModelBuilder modelBuilder = withExistingParent(loc.getPath(), mcLoc("block/block"))
                .texture("texture", loc.withPrefix("item/"))
                .texture("particle", loc.withPrefix("item/"))
                .element()
                .from(16 - width, from, from)
                .to(16, to, to)
                .allFaces((direction, builder) -> builder.uvs(0, 16 - width, width, 16).texture("#texture"))
                .end()
                .element()
                .from(0, from, from)
                .to(width, to, to)
                .allFaces((direction, builder) -> builder.uvs(0, 0, width, width).texture("#texture"))
                .end();
        // Middle part
        if (width < 8) {
            modelBuilder.element()
                    .from(width, from, from)
                    .to(16 - width, to, to)
                    .allFaces((direction, builder) -> builder.uvs(0, width, width, 16 - width).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#texture"))
                    .end();
        }

    }

    private void bucket(Fluid f) {
        withExistingParent(key(f.getBucket()).getPath(), ResourceLocation.fromNamespaceAndPath("neoforge", "item/bucket_drip"))
                .customLoader(DynamicFluidContainerModelBuilder::begin)
                .fluid(f);
    }

    private void batteryModel(BatteryItem item) {
        overrideItemModel(item.getStages(), basicItem(item, itemTexture(item).withSuffix("_0")), IRItemProperties.BATTERY_STAGE_KEY,
                i -> basicItem(item, "_" + i));
    }

    private void activeModel(ItemLike item) {
        overrideItemModel(2, handheldItem(item.asItem()), IRItemProperties.ACTIVE_KEY,
                i -> i == 1 ? handheldItem(key(item).withSuffix("_active")) : handheldItem(item.asItem()));
    }

    private void overrideItemModel(int variants, ItemModelBuilder defaultModel, ResourceLocation key, Function<Integer, ItemModelBuilder> overrideFunction) {
        for (int i = 0; i < variants; i++) {
            ItemModelBuilder model = overrideFunction.apply(i);
            defaultModel.override()
                    .model(model)
                    .predicate(key, i)
                    .end();
        }
    }

    public ItemModelBuilder basicItem(ItemLike item, ResourceLocation texture) {
        return getBuilder(name(item))
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(texture.getNamespace(), texture.getPath()));
    }

    public ItemModelBuilder basicItem(ItemLike item, String suffix) {
        ResourceLocation location = key(item);
        return getBuilder(location + suffix)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(location.getNamespace(), "item/" + location.getPath() + suffix));
    }

    public ResourceLocation itemTexture(ItemLike item) {
        ResourceLocation name = key(item);
        return ResourceLocation.fromNamespaceAndPath(name.getNamespace(), ModelProvider.ITEM_FOLDER + "/" + name.getPath());
    }

    private ItemModelBuilder basicItemBlock(Item item, String textureFolder) {
        String folder = textureFolder + "/";
        if (textureFolder == null || textureFolder.trim().isEmpty())
            folder = "";
        ResourceLocation name = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item));
        return getBuilder(name.toString()).parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(name.getNamespace(), "block/" + folder + name.getPath()));
    }

    private ItemModelBuilder parentItemBlock(Item item) {
        return parentItemBlock(item, "");
    }

    private ItemModelBuilder parentItemBlock(Item item, String suffix) {
        ResourceLocation name = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item));
        return getBuilder(name.toString())
                .parent(new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(name.getNamespace(), "block/" + name.getPath() + suffix)));
    }

    private static @NotNull ResourceLocation key(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem());
    }

    public String name(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
    }

}
