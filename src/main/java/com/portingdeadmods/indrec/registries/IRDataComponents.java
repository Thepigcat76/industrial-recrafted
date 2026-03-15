package com.portingdeadmods.indrec.registries;

import com.mojang.serialization.Codec;
import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.impl.energy.ComponentEuStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

// Registry for minecraft's items data component system
// entities and blockentities are handled in IRAttachmentTypes
public final class IRDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, IndustrialRecrafted.MODID);

    public static final Supplier<DataComponentType<Boolean>> ACTIVE = registerDataComponentType("active",
            () -> builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));
    public static final Supplier<DataComponentType<Optional<BlockPos>>> TARGET_POS = registerDataComponentType("target_pos",
            () -> builder -> builder.persistent(BlockPos.CODEC.optionalFieldOf("target_pos").codec())
                    .networkSynchronized(ByteBufCodecs.optional(BlockPos.STREAM_CODEC)));

    // Data for capabilities
    public static final Supplier<DataComponentType<ComponentEuStorage>> ENERGY = registerDataComponentType("energy",
            () -> builder -> builder.persistent(ComponentEuStorage.CODEC).networkSynchronized(ComponentEuStorage.STREAM_CODEC));
    public static final Supplier<DataComponentType<SimpleFluidContent>> FLUID = registerDataComponentType("fluid",
            () -> builder -> builder.persistent(SimpleFluidContent.CODEC).networkSynchronized(SimpleFluidContent.STREAM_CODEC));
    public static final Supplier<DataComponentType<List<ItemStack>>> ITEM = registerDataComponentType("items",
            () -> builder -> builder.persistent(ItemStack.CODEC.listOf()).networkSynchronized(ItemStack.LIST_STREAM_CODEC));

    public static <T> Supplier<DataComponentType<T>> registerDataComponentType(
            String name, Supplier<UnaryOperator<DataComponentType.Builder<T>>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.get().apply(DataComponentType.builder()).build());
    }
}