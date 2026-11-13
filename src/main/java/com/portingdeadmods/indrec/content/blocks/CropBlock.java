package com.portingdeadmods.indrec.content.blocks;

import com.portingdeadmods.indrec.api.crops.CropInteraction;
import com.portingdeadmods.indrec.api.crops.CropMutation;
import com.portingdeadmods.indrec.api.crops.requirements.CropRequirement;
import com.portingdeadmods.indrec.content.crops.requirements.LightLevelRequirement;
import com.portingdeadmods.indrec.content.crops.requirements.SoilRequirement;
import com.portingdeadmods.indrec.tags.IRTags;
import com.portingdeadmods.indrec.utils.CropHarvestUtils;
import com.mojang.serialization.MapCodec;
import com.portingdeadmods.indrec.IRConfig;
import com.portingdeadmods.indrec.IRRegistries;
import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.api.crops.Crop;
import com.portingdeadmods.indrec.content.recipes.CropCrossbreedingRecipe;
import com.portingdeadmods.indrec.content.blockentities.CropBlockEntity;
import com.portingdeadmods.indrec.networking.clientbound.SetCropAgePayload;
import com.portingdeadmods.indrec.registries.IRBlockEntityTypes;
import com.portingdeadmods.indrec.registries.IRCrops;
import com.portingdeadmods.indrec.registries.IRItems;
import com.portingdeadmods.indrec.registries.IRRecipes;
import com.portingdeadmods.portingdeadlibs.utils.BlockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class CropBlock extends BaseEntityBlock implements BonemealableBlock {
    public static final BooleanProperty CROSSBREED = BooleanProperty.create("crossbreed");
    public static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 12, 14);

    public CropBlock(Properties properties) {
        super(properties.randomTicks());
        registerDefaultState(this.defaultBlockState().setValue(CROSSBREED, false));
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        if (level.getBlockEntity(pos) instanceof CropBlockEntity cropBE) {
            Holder<Crop> crop = cropBE.getCrop();
            if (crop != null) {
                return crop.value().createSeedStack()
                        .orElse(IRItems.CROP_STICKS.toStack());
            }
        }
        return super.getCloneItemStack(state, target, level, pos, player);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(CROSSBREED));
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(CropBlock::new);
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(IRTags.BlockTags.VALID_INDUSTRIAL_SOIL);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState belowBlockState = level.getBlockState(blockpos);
        TriState soilDecision = belowBlockState.canSustainPlant(level, blockpos, Direction.UP, state);
        if (!soilDecision.isDefault()) {
            return soilDecision.isTrue();
        } else {
            return this.mayPlaceOn(belowBlockState, level, blockpos);
        }
    }

    @Override
    protected @NotNull BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return !state.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return state.getFluidState().isEmpty();
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return pathComputationType == PathComputationType.AIR && !this.hasCollision || super.isPathfindable(state, pathComputationType);
    }

    private static List<ItemStack> getCropLoot(ServerLevel serverLevel, Holder<Crop> crop, ItemStack tool, BlockPos cropPos, Entity harverstingEntity, CropBlockEntity cropBe) {
        return getCropLoot(serverLevel, crop, cropBe.getAge(), cropBe.getBlockState(), tool, cropPos, harverstingEntity);
    }

    public static List<ItemStack> getCropLoot(ServerLevel serverLevel, Holder<Crop> crop, int age, BlockState state, ItemStack tool, BlockPos cropPos, @Nullable Entity harverstingEntity) {
        LootTable lootTable = crop.value().loot()
                .map(loot -> loot.map(table -> table, key -> serverLevel.getServer().reloadableRegistries().getLootTable(key)))
                .orElse(null);

        if (lootTable != null) {
            Crop.Models models = crop.value().models();
            if (models instanceof Crop.Models.BlockModel(Block baseBlock, String ageProperty)) {
                IntegerProperty ageProp = baseBlock.defaultBlockState().getProperties().stream()
                        .filter(prop -> prop.getName().equals(ageProperty))
                        .findFirst()
                        .orElse(null) instanceof IntegerProperty _ageProp ? _ageProp : null;

                if (ageProp != null) {
                    state = baseBlock.defaultBlockState().setValue(ageProp, Math.min(crop.value().maxStage(), age));
                }
            }
            LootParams params = new LootParams.Builder(serverLevel)
                    .withParameter(LootContextParams.BLOCK_STATE, state)
                    .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(cropPos))
                    .withParameter(LootContextParams.TOOL, tool)
                    .withOptionalParameter(LootContextParams.THIS_ENTITY, harverstingEntity)
                    .create(LootContextParamSets.BLOCK);
            return lootTable.getRandomItems(params);
        }

        return List.of();
    }

    @Override
    protected @NotNull List<ItemStack> getDrops(@NotNull BlockState state, LootParams.@NotNull Builder params) {
        List<ItemStack> drops = new ArrayList<>(super.getDrops(state, params));
        if (params.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof CropBlockEntity cropBE) {
            cropBE.createSeedDrop(params.getLevel().getRandom()).ifPresent(drops::add);
        }
        return drops;
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        CropBlockEntity cropBE = BlockUtils.getBE(CropBlockEntity.class, level, pos);
        if (cropBE == null) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        Holder<Crop> beCrop = cropBE.getCrop();
        if (beCrop != null && tryInteract(stack, level, pos, player, hand, cropBE, beCrop)) {
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        if (stack.is(ItemTags.HOES) && beCrop != null && beCrop.value().canHarvest() && cropBE.getAge() == cropBE.getMaxAge()
                && CropHarvestUtils.canUseHoe(stack, player)) {
            if (!level.isClientSide) {
                List<ItemStack> items = getCropLoot((ServerLevel) level, beCrop, stack, pos, player, cropBE);
                for (ItemStack item : items) {
                    Block.popResource(level, pos, item);
                }
                CropHarvestUtils.useHoe(stack, player, hand);
            }
            level.playSound(null, pos, SoundEvents.CROP_BREAK, SoundSource.BLOCKS);
            cropBE.setAge(0);
            return ItemInteractionResult.SUCCESS;
        }
        if (stack.is(Items.SHEARS)) {
            if (beCrop != null) {
                if (!level.isClientSide) {
                    cropBE.createSeedDrop(level.getRandom()).ifPresent(seedStack -> Block.popResource(level, pos, seedStack));
                }
                level.playSound(null, pos, SoundEvents.CROP_BREAK, SoundSource.BLOCKS);
                cropBE.removeCrop();
                return ItemInteractionResult.SUCCESS;
            } else if (state.getValue(CROSSBREED)) {
                level.setBlockAndUpdate(pos, state.setValue(CROSSBREED, false));
                Block.popResource(level, pos, new ItemStack(IRItems.CROP_STICKS.get()));
                level.playSound(null, pos, SoundEvents.BAMBOO_WOOD_BREAK, SoundSource.BLOCKS);
                return ItemInteractionResult.SUCCESS;
            }
        }
        if (stack.is(IRItems.CROP_STICKS) && !state.getValue(CROSSBREED)) {
            level.setBlockAndUpdate(pos, state.setValue(CROSSBREED, true));
            stack.consume(1, player);
            level.playSound(null, pos, SoundEvents.BAMBOO_WOOD_PLACE, SoundSource.BLOCKS);
            return ItemInteractionResult.SUCCESS;
        }

        Holder<Crop> itemCrop = null;
        if (!stack.isEmpty()) {
            Optional<HolderLookup.RegistryLookup<Crop>> registryAccess = level.registryAccess().lookup(IRRegistries.CROP);
            if (registryAccess.isPresent()) {
                Stream<Holder.Reference<Crop>> elements = registryAccess.get().listElements();
                itemCrop = elements.filter(holder -> holder.value().isSeed(stack))
                        .findFirst()
                        .orElse(null);
            }
        }
        if (itemCrop != null && beCrop == null && !state.getValue(CROSSBREED)) {
            cropBE.plant(itemCrop);
            stack.consume(1, player);
            level.playSound(null, pos, SoundEvents.CROP_PLANTED, SoundSource.BLOCKS);
            return ItemInteractionResult.SUCCESS;
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    // Chance that one of the parents spreads instead of the recipe's result being planted, once the recipe's chance was hit
    private static final float PARENT_SPREAD_CHANCE = 2f / 3f;
    /**
     * Light level crops without a custom {@link LightLevelRequirement} need to grow, same as vanilla crops
     */
    public static final int DEFAULT_MIN_LIGHT_LEVEL = 9;

    private static boolean isFullyGrown(CropBlockEntity cropBE) {
        return cropBE.getAge() >= cropBE.getMaxAge();
    }


    /**
     * Tries to crossbreed the crops next to the (empty) crossbreed sticks at the position.
     * Only pairs where at least one of the crops is not fully grown yet can crossbreed.
     * One of the recipes matching such a pair is picked at random and its chance is rolled.
     * On success there is a {@link #PARENT_SPREAD_CHANCE} chance that one of the two parents spreads onto the sticks,
     * otherwise the recipe's result is planted.
     */
    private static void tryCrossbreed(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        List<CropBlockEntity> neighbours = new ArrayList<>();
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            CropBlockEntity neighbour = BlockUtils.getBE(CropBlockEntity.class, level, pos.relative(direction));
            if (neighbour != null && neighbour.getCrop() != null) {
                neighbours.add(neighbour);
            }
        }
        if (neighbours.size() < 2) return;

        List<CropCrossbreedingRecipe> candidates = new ArrayList<>();
        for (int i = 0; i < neighbours.size(); i++) {
            for (int j = i + 1; j < neighbours.size(); j++) {
                CropBlockEntity first = neighbours.get(i);
                CropBlockEntity second = neighbours.get(j);
                if (isFullyGrown(first) && isFullyGrown(second)) continue;

                CropCrossbreedingRecipe.Input input = new CropCrossbreedingRecipe.Input(first.getCrop(), second.getCrop());
                for (RecipeHolder<CropCrossbreedingRecipe> recipe : level.getRecipeManager().getRecipesFor(IRRecipes.CROP_CROSSBREEDING_TYPE.get(), input, level)) {
                    candidates.add(recipe.value());
                }
            }
        }
        if (candidates.isEmpty()) return;

        CropCrossbreedingRecipe recipe = candidates.get(random.nextInt(candidates.size()));
        if (random.nextFloat() >= recipe.chance()) return;

        // Only parents the recipe allows to spread can spread, if there are none the result is always planted
        List<ResourceKey<Crop>> spreadingParents = recipe.spreadingParents();
        ResourceKey<Crop> resultKey = !spreadingParents.isEmpty() && random.nextFloat() < PARENT_SPREAD_CHANCE
                ? spreadingParents.get(random.nextInt(spreadingParents.size()))
                : recipe.result();
        Optional<Holder.Reference<Crop>> result = level.registryAccess().holder(resultKey);
        if (result.isEmpty()) {
            IndustrialRecrafted.LOGGER.warn("Crossbreeding crop {} does not exist", resultKey.location());
            return;
        }

        CropBlockEntity cropBE = BlockUtils.getBE(CropBlockEntity.class, level, pos);
        if (cropBE != null) {
            cropBE.plant(result.get());
        }
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.isAreaLoaded(pos, 1)) {
            CropBlockEntity cropBE = BlockUtils.getBE(CropBlockEntity.class, level, pos);
            if (cropBE == null) return;

            if (cropBE.getCrop() == null) {
                if (state.getValue(CROSSBREED)) {
                    if (level.getRawBrightness(pos, 0) >= DEFAULT_MIN_LIGHT_LEVEL) {
                        tryCrossbreed(state, level, pos, random);
                    }
                } else if (random.nextFloat() < IRConfig.weedGrowthChance) {
                    level.registryAccess().holder(IRCrops.WEED).ifPresent(cropBE::plant);
                }
                return;
            }

            if (tryMutate(level, cropBE, random)) return;

            Holder<Crop> crop = cropBE.getCrop();
            if (canGrow(crop, level, pos)) {
                int i = cropBE.getAge();
                float f = net.minecraft.world.level.block.CropBlock.getGrowthSpeed(state, level, pos);
                if (CommonHooks.canCropGrow(level, pos, state, random.nextInt((int) (25.0F / f) + 1) == 0)) {
                    if (i < cropBE.getMaxAge()) {
                        //level.setBlock(pos, this.getStateForAge(i + 1), 2);
                        setAgeAndSync(level, pos, cropBE, i + 1);
                        CommonHooks.fireCropGrowPost(level, pos, state);
                    }
                }
            }

        }
    }

    /**
     * Applies the first of the crop's interactions that matches the used item, replacing the crop with the interaction's result
     *
     * @return whether an interaction was applied
     */
    private static boolean tryInteract(ItemStack stack, Level level, BlockPos pos, Player player, InteractionHand hand, CropBlockEntity cropBE, Holder<Crop> crop) {
        for (CropInteraction interaction : crop.value().interactions()) {
            if (!interaction.ingredient().test(stack)) continue;

            Optional<Holder.Reference<Crop>> result = level.registryAccess().holder(interaction.result());
            if (result.isEmpty()) {
                IndustrialRecrafted.LOGGER.warn("Interaction crop {} does not exist", interaction.result().location());
                continue;
            }
            if (!level.isClientSide) {
                cropBE.plant(result.get(), interaction.keepAge() ? cropBE.getAge() : 0);
                interaction.sound().ifPresent(sound -> level.playSound(null, pos, sound.value(), SoundSource.BLOCKS));
            }
            if (interaction.consume()) {
                if (interaction.remainder().isPresent()) {
                    // Consumes the item and gives the remainder, in creative only gives the remainder if the player doesn't have it yet
                    player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, interaction.remainder().get().copy()));
                } else {
                    stack.consume(1, player);
                }
            }
            return true;
        }
        return false;
    }

    private static boolean tryMutate(ServerLevel level, CropBlockEntity cropBE, RandomSource random) {
        for (CropMutation mutation : cropBE.getCrop().value().mutations()) {
            if (!mutation.mutates(random)) continue;

            Optional<Holder.Reference<Crop>> result = level.registryAccess().holder(mutation.result());
            if (result.isEmpty()) {
                IndustrialRecrafted.LOGGER.warn("Mutation crop {} does not exist", mutation.result().location());
                continue;
            }
            cropBE.plant(result.get(), mutation.keepAge() ? Math.min(result.get().value().maxStage(), cropBE.getAge()) : 0);
            return true;
        }
        return false;
    }

    // Fallback light requirement: >= DEFAULT_MIN_LIGHT_LEVEL
    // Fallback Soil: FARMLAND
    public static boolean canGrow(Holder<Crop> crop, LevelReader level, BlockPos pos) {
        if (!crop.value().meetsRequirements(crop, level, pos)) return false;
        boolean hasLightRequirement = hasRequirement(crop, LightLevelRequirement.class);
        if (!hasLightRequirement && level.getRawBrightness(pos, 0) < DEFAULT_MIN_LIGHT_LEVEL) return false;
        boolean hasSoilRequirement = hasRequirement(crop, SoilRequirement.class);
        return hasSoilRequirement || level.getBlockState(pos.below()).is(Blocks.FARMLAND);
    }

    private static boolean hasRequirement(Holder<Crop> crop, Class<? extends CropRequirement> requirementClass) {
        return crop.value().requirements().stream().anyMatch(requirementClass::isInstance);
    }

    public static void setAgeAndSync(ServerLevel level, BlockPos pos, CropBlockEntity cropBE, int age) {
        cropBE.setAge(age);
        PacketDistributor.sendToPlayersTrackingChunk(level, new ChunkPos(pos), new SetCropAgePayload(pos, age));
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return level.getBlockEntity(pos) instanceof CropBlockEntity cropBE && cropBE.getCrop() != null && !isFullyGrown(cropBE)
                && canGrow(cropBE.getCrop(), level, pos);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        if (level.getBlockEntity(pos) instanceof CropBlockEntity cropBE && cropBE.getCrop() != null) {
            int maxAge = cropBE.getMaxAge();
            int minIncrease = Math.max(1, Math.round(maxAge * 2 / 7f));
            int maxIncrease = Math.max(minIncrease, Math.round(maxAge * 5 / 7f));
            int age = Math.min(maxAge, cropBE.getAge() + Mth.nextInt(random, minIncrease, maxIncrease));
            setAgeAndSync(level, pos, cropBE, age);
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return IRBlockEntityTypes.CROP.get().create(blockPos, blockState);
    }
}
