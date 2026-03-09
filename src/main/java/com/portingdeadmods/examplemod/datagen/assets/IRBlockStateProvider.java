package com.portingdeadmods.examplemod.datagen.assets;

import com.portingdeadmods.examplemod.IndustrialReclassified;
import com.portingdeadmods.examplemod.api.blocks.PipeBlock;
import com.portingdeadmods.examplemod.content.blocks.RubberTreeResinHoleBlock;
import com.portingdeadmods.examplemod.registries.IRBlocks;
import com.portingdeadmods.examplemod.registries.IRMachines;
import com.portingdeadmods.examplemod.utils.machines.IRMachine;
import com.portingdeadmods.portingdeadlibs.api.datagen.ModelBuilder;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class IRBlockStateProvider extends BlockStateProvider {
    public IRBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, IndustrialReclassified.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        treeStatesAndModels();
        simpleBlock(IRBlocks.TIN_ORE.get());
        simpleBlock(IRBlocks.URANIUM_ORE.get());
        simpleBlock(IRBlocks.IRIDIUM_ORE.get());
        simpleBlock(IRBlocks.DEEPSLATE_TIN_ORE.get());
        simpleBlock(IRBlocks.DEEPSLATE_URANIUM_ORE.get());
        simpleBlock(IRBlocks.DEEPSLATE_IRIDIUM_ORE.get());
        simpleBlock(IRBlocks.MACHINE_FRAME.get());
        simpleBlock(IRBlocks.ADVANCED_MACHINE_FRAME.get());
        simpleBlock(IRBlocks.REINFORCED_STONE.get());
        simpleBlock(IRBlocks.REINFORCED_GLASS.get());
        doorBlock(IRBlocks.REINFORCED_DOOR.get(),
                blockTextureSuffix(IRBlocks.REINFORCED_DOOR.get(), "_bottom"), blockTextureSuffix(IRBlocks.REINFORCED_DOOR.get(), "_top"));
        modelBuilder(IRBlocks.INDUSTRIAL_TNT.get())
                .sides(this::blockTextureSuffix, "_side")
                .top(block -> this.blockTextureSuffix(Blocks.TNT, "_top"))
                .bottom(block -> this.blockTextureSuffix(Blocks.TNT, "_bottom"))
                .create();
        modelBuilder(IRBlocks.NUKE.get())
                .sides(this::blockTextureSuffix, "_side")
                .top(block -> this.blockTextureSuffix(Blocks.TNT, "_top"))
                .bottom(block -> this.blockTextureSuffix(Blocks.TNT, "_bottom"))
                .create();
        sheetBlock(IRBlocks.STICKY_RESIN_SHEET.get());
        sheetBlock(IRBlocks.RUBBER_SHEET.get());

        simpleBlock(IRBlocks.TIN_BLOCK.get());
        simpleBlock(IRBlocks.URANIUM_BLOCK.get());
        simpleBlock(IRBlocks.BRONZE_BLOCK.get());

        cableBlock(IRBlocks.TIN_CABLE.get(), 6);
        cableBlock(IRBlocks.COPPER_CABLE.get(), 6);
        cableBlock(IRBlocks.GOLD_CABLE.get(), 6);
        cableBlock(IRBlocks.HV_CABLE.get(), 8);
        cableBlock(IRBlocks.GLASS_FIBRE_CABLE.get(), 4);
        cableBlock(IRBlocks.BURNT_CABLE.get(), 4);

        modelBuilder(IRMachines.BASIC_GENERATOR.getBlock())
                .defaultTexture(blockTexture(IRBlocks.MACHINE_FRAME.get()))
                .front(this::blockTextureSuffix, "_front")
                .horizontalFacing()
                .active()
                .create();

        modelBuilder(IRMachines.GEOTHERMAL_GENERATOR.getBlock())
                .defaultTexture(blockTexture(IRBlocks.MACHINE_FRAME.get()))
                .front(this::blockTextureSuffix, "_front")
                .horizontalFacing()
                //.active()
                .create();

        modelBuilder(IRMachines.WATER_MILL.getBlock())
                .defaultTexture(blockTexture(IRBlocks.MACHINE_FRAME.get()))
                .front(this::blockTextureSuffix, "_front")
                .horizontalFacing()
                .create();

        modelBuilder(IRMachines.WIND_MILL.getBlock())
                .defaultTexture(blockTexture(IRBlocks.MACHINE_FRAME.get()))
                .front(this::blockTextureSuffix, "_front")
                .horizontalFacing()
                .create();

        modelBuilder(IRBlocks.NUCLEAR_REACTOR_CHAMBER.get())
                .defaultTexture(blockTexture(IRBlocks.ADVANCED_MACHINE_FRAME.get()))
                .top(this::blockTexture)
                .horizontalFacing()
                .create();

        modelBuilder(IRBlocks.NUCLEAR_REACTOR.get())
                .defaultTexture(blockTexture(IRBlocks.ADVANCED_MACHINE_FRAME.get()))
                .front(this::blockTextureSuffix, "_front")
                .top(this.blockTexture(IRBlocks.NUCLEAR_REACTOR_CHAMBER.get()))
                .create();

        modelBuilder(IRMachines.BASIC_SOLAR_PANEL.getBlock())
                .defaultTexture(blockTexture(IRBlocks.MACHINE_FRAME.get()))
                .top(this::blockTextureSuffix, "_top")
                .horizontalFacing()
                .create();

        modelBuilder(IRMachines.ELECTRIC_FURNACE.getBlock())
                .defaultTexture(blockTexture(IRBlocks.MACHINE_FRAME.get()))
                .front(this::blockTextureSuffix, "_front")
                .horizontalFacing()
                .active()
                .create();

        modelBuilder(IRMachines.MACERATOR.getBlock())
                .defaultTexture(blockTexture(IRBlocks.MACHINE_FRAME.get()))
                .front(this::blockTextureSuffix, "_front")
                .top(this::blockTextureSuffix, "_top")
                .horizontalFacing()
                .active()
                .create();

        modelBuilder(IRMachines.COMPRESSOR.getBlock())
                .defaultTexture(blockTexture(IRBlocks.MACHINE_FRAME.get()))
                .front(this::blockTextureSuffix, "_front")
                .top(this::blockTextureSuffix, "_top")
                .horizontalFacing()
                .active()
                .create();

        modelBuilder(IRMachines.RECYCLER.getBlock())
                .defaultTexture(blockTexture(IRBlocks.MACHINE_FRAME.get()))
                .front(this::blockTextureSuffix, "_front")
                .top(this::blockTextureSuffix, "_top")
                .horizontalFacing()
                .create();

        modelBuilder(IRMachines.EXTRACTOR.getBlock())
                .defaultTexture(blockTexture(IRBlocks.MACHINE_FRAME.get()))
                .front(this::blockTextureSuffix, "_front")
                .top(this::blockTextureSuffix, "_top")
                .horizontalFacing()
                .active()
                .create();

        modelBuilder(IRMachines.CANNING_MACHINE.getBlock())
                .defaultTexture(blockTexture(IRBlocks.MACHINE_FRAME.get()))
                .front(this::blockTextureSuffix, "_front")
                .horizontalFacing()
                .active()
                .create();

        modelBuilder(IRMachines.CHARGE_PAD.getBlock())
                .defaultTexture(blockTexture(IRBlocks.MACHINE_FRAME.get()))
                .top(this::blockTextureSuffix, "_top")
                .horizontalFacing()
                .create();
    }



    private void cableBlock(Block block, int width) {
        MultiPartBlockStateBuilder builder = getMultipartBuilder(block);
        cableConnection(builder, block, width, Direction.DOWN, 0, 0);
        cableConnection(builder, block, width, Direction.UP, 180, 0);
        cableConnection(builder, block, width, Direction.NORTH, 90, 180);
        cableConnection(builder, block, width, Direction.EAST, 90, 270);
        cableConnection(builder, block, width, Direction.SOUTH, 90, 0);
        cableConnection(builder, block, width, Direction.WEST, 90, 90);
        builder.part().modelFile(cableBaseModel(block, width)).addModel().end();
    }

    private ModelFile cableBaseModel(Block block, int width) {
        ResourceLocation loc = BuiltInRegistries.BLOCK.getKey(block);
        float from = (16 - width) / 2F;
        float to = (16 + width) / 2F;
        return models().withExistingParent(loc.getPath() + "_base", mcLoc("block/block"))
                .texture("texture", loc.withPrefix("block/"))
                .texture("particle", loc.withPrefix("block/"))
                .element()
                .from(from, from, from)
                .to(to, to, to)
                .allFaces((direction, builder) -> builder.uvs(from, from, to, to).texture("#texture").end().end())
                .end();
    }

    private ModelFile cableConnectionModel(Block block, int width) {
        ResourceLocation loc = BuiltInRegistries.BLOCK.getKey(block);
        float from = (16 - width) / 2F;
        float to = (16 + width) / 2F;

        return models().withExistingParent(loc.getPath() + "_connection", mcLoc("block/block"))
                .texture("texture", loc.withPrefix("block/"))
                .texture("particle", loc.withPrefix("block/"))
                .element()
                .from(from, 0, from)
                .to(to, from, to)
                .allFacesExcept((direction, builder) -> {
                    if (direction == Direction.DOWN) builder.texture("#texture").cullface(direction);
                    else builder.texture("#texture");
                }, Set.of(Direction.UP))
                .end();
    }

    private void cableConnection(MultiPartBlockStateBuilder builder, Block block, int width, Direction direction, int x, int y) {
        builder.part().modelFile(cableConnectionModel(block, width)).rotationX(x).rotationY(y).addModel()
                .condition(PipeBlock.CONNECTION[direction.get3DDataValue()], true).end();
    }

    private void sheetBlock(Block block) {
        getVariantBuilder(block).forAllStates(state -> new ConfiguredModel[]{
            new ConfiguredModel(state.getValue(BlockStateProperties.LAYERS) == 8
                    ? models().cubeAll(name(block) + "_full", blockTexture(block))
                    : sheetBlockModel(state.getBlock(), state.getValue(BlockStateProperties.LAYERS) * 2))
        });
    }

    private BlockModelBuilder sheetBlockModel(Block block, int height) {
        return models().withExistingParent(name(block) + height, "snow_height" + height)
                .texture("texture", blockTexture(block))
                .texture("particle", blockTexture(block));
    }

    private @NotNull ModelBuilder modelBuilder(Block block) {
        return new ModelBuilder(block, this);
    }

    private void treeStatesAndModels() {
        logBlock(IRBlocks.RUBBER_TREE_LOG.get(), "tree");
        logBlock(IRBlocks.STRIPPED_RUBBER_TREE_LOG.get(), "tree");
        woodBlock(IRBlocks.RUBBER_TREE_WOOD.get(), IRBlocks.RUBBER_TREE_LOG.get(), "tree");
        woodBlock(IRBlocks.STRIPPED_RUBBER_TREE_WOOD.get(), IRBlocks.STRIPPED_RUBBER_TREE_LOG.get(), "tree");
        simpleBlockParentItem(IRBlocks.RUBBER_TREE_PLANKS.get(), "cube_all", "all", "tree");
        simpleBlockParentItem(IRBlocks.RUBBER_TREE_LEAVES.get(), "leaves", "all", "tree");
        simpleBlock(IRBlocks.RUBBER_TREE_SAPLING.get(), models().singleTexture(
                name(IRBlocks.RUBBER_TREE_SAPLING.get()),
                mcLoc(ModelProvider.BLOCK_FOLDER + "/cross"),
                "cross",
                blockTexture(IRBlocks.RUBBER_TREE_SAPLING.get(), "tree")
        ).renderType("cutout"));
        rubberTreeResinHole(IRBlocks.RUBBER_TREE_RESIN_HOLE);
        buttonBlock(IRBlocks.RUBBER_TREE_BUTTON.get(), blockTexture(IRBlocks.RUBBER_TREE_PLANKS.get(), "tree"));
        inventoryModel(IRBlocks.RUBBER_TREE_BUTTON.get(), "button_inventory", blockTexture(IRBlocks.RUBBER_TREE_PLANKS.get(), "tree"));
        doorBlock(IRBlocks.RUBBER_TREE_DOOR.get(),
                blockTexture(IRBlocks.RUBBER_TREE_DOOR.get(), "tree", "_bottom"), blockTexture(IRBlocks.RUBBER_TREE_DOOR.get(), "tree", "_top"));
        fenceBlock(IRBlocks.RUBBER_TREE_FENCE.get(), blockTexture(IRBlocks.RUBBER_TREE_PLANKS.get(), "tree"));
        inventoryModel(IRBlocks.RUBBER_TREE_FENCE.get(), "fence_inventory", blockTexture(IRBlocks.RUBBER_TREE_PLANKS.get(), "tree"));
        fenceGateBlock(IRBlocks.RUBBER_TREE_FENCE_GATE.get(), blockTexture(IRBlocks.RUBBER_TREE_PLANKS.get(), "tree"));
        pressurePlateBlock(IRBlocks.RUBBER_TREE_PRESSURE_PLATE.get(), blockTexture(IRBlocks.RUBBER_TREE_PLANKS.get(), "tree"));
        trapdoorBlock(IRBlocks.RUBBER_TREE_TRAPDOOR.get(), blockTexture(IRBlocks.RUBBER_TREE_TRAPDOOR.get(), "tree"), false);
        slabBlock(IRBlocks.RUBBER_TREE_SLAB.get(), ResourceLocation.fromNamespaceAndPath(IndustrialReclassified.MODID, "block/rubber_tree_planks"),
                blockTexture(IRBlocks.RUBBER_TREE_PLANKS.get(), "tree"));
        stairsBlock(IRBlocks.RUBBER_TREE_STAIRS.get(),
                blockTexture(IRBlocks.RUBBER_TREE_PLANKS.get(), "tree"));
    }

    private void rubberTreeResinHole(DeferredBlock<RubberTreeResinHoleBlock> block) {
        VariantBlockStateBuilder builder = getVariantBuilder(block.get());
        BlockModelBuilder modelBuilder = models().cube(name(block.get()) + "_full",
                        blockTexture(IRBlocks.RUBBER_TREE_LOG.get(), "tree").withSuffix("_top"),
                        blockTexture(IRBlocks.RUBBER_TREE_LOG.get(), "tree").withSuffix("_top"),
                        blockTexture(block.get(), "tree").withSuffix("_full"),
                        blockTexture(IRBlocks.RUBBER_TREE_LOG.get(), "tree"),
                        blockTexture(IRBlocks.RUBBER_TREE_LOG.get(), "tree"),
                        blockTexture(IRBlocks.RUBBER_TREE_LOG.get(), "tree"))
                .texture("particle", blockTexture(IRBlocks.RUBBER_TREE_LOG.get(), "tree"));
        BlockModelBuilder emptyModelBuilder = models().cube(name(block.get()),
                        blockTexture(IRBlocks.RUBBER_TREE_LOG.get(), "tree").withSuffix("_top"),
                        blockTexture(IRBlocks.RUBBER_TREE_LOG.get(), "tree").withSuffix("_top"),
                        blockTexture(block.get(), "tree"),
                        blockTexture(IRBlocks.RUBBER_TREE_LOG.get(), "tree"),
                        blockTexture(IRBlocks.RUBBER_TREE_LOG.get(), "tree"),
                        blockTexture(IRBlocks.RUBBER_TREE_LOG.get(), "tree"))
                .texture("particle", blockTexture(IRBlocks.RUBBER_TREE_LOG.get(), "tree"));

        for (Direction dir : BlockStateProperties.HORIZONTAL_FACING.getPossibleValues()) {
            builder.partialState().with(RubberTreeResinHoleBlock.RESIN, true).with(BlockStateProperties.HORIZONTAL_FACING, dir)
                    .modelForState().modelFile(modelBuilder).rotationY(((int) dir.toYRot() + 180) % 360).addModel()
                    .partialState().with(RubberTreeResinHoleBlock.RESIN, false).with(BlockStateProperties.HORIZONTAL_FACING, dir)
                    .modelForState().modelFile(emptyModelBuilder).rotationY(((int) dir.toYRot() + 180) % 360).addModel();
        }
    }

    private void logBlock(RotatedPillarBlock block, String textureFolder) {
        ResourceLocation name = BuiltInRegistries.BLOCK.getKey(block);
        axisBlock(block, blockTexture(block, textureFolder), blockTexture(block, textureFolder, "_top"));
        simpleBlockItem(block, models().withExistingParent(name.getPath(), mcLoc("minecraft:block/cube_column")));
    }

    private void inventoryModel(Block block, String parentModel, ResourceLocation texture) {
        ResourceLocation name = BuiltInRegistries.BLOCK.getKey(block);
        models().getBuilder(name.getPath() + "_inventory").parent(models().getExistingFile(ResourceLocation.parse(parentModel))).texture("texture", texture);
    }

    private void woodBlock(RotatedPillarBlock block, Block blockTexture, String textureFolder) {
        ResourceLocation name = BuiltInRegistries.BLOCK.getKey(block);
        ResourceLocation texture = blockTexture(blockTexture, textureFolder);
        axisBlock(block, models().cubeColumn(name.getPath(), texture, texture),
                models().cubeColumnHorizontal(name.getPath(), texture, texture));
        simpleBlockItem(block, models().withExistingParent(name.getPath(), mcLoc("minecraft:block/cube_column")));
    }

    private ResourceLocation blockTextureSuffix(Block block, String suffix) {
        return blockTexture(block, null, suffix);
    }

    private ResourceLocation blockTexture(Block block, String textureFolder) {
        return blockTexture(block, textureFolder, "");
    }

    private ResourceLocation blockTexture(Block block, String textureFolder, String suffix) {
        ResourceLocation name = key(block);
        if (textureFolder == null || textureFolder.trim().isEmpty())
            return ResourceLocation.fromNamespaceAndPath(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + name.getPath() + suffix);
        return ResourceLocation.fromNamespaceAndPath(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + textureFolder + "/" + name.getPath() + suffix);
    }

    private void simpleBlockParentItem(Block block, String modelName, String textureKey, String textureFolder) {
        simpleBlockWithItem(block, models()
                .withExistingParent(key(block).getPath(), ModelProvider.BLOCK_FOLDER + "/" + modelName)
                .texture(textureKey, blockTexture(block, textureFolder)));
    }

    public ResourceLocation key(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    public String name(Block block) {
        return key(block).getPath();
    }

}
