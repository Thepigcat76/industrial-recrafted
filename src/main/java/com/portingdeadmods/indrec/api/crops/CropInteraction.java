package com.portingdeadmods.indrec.api.crops;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.portingdeadmods.indrec.IRRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.Optional;

public record CropInteraction(Ingredient ingredient, ResourceKey<Crop> result, boolean keepAge, boolean consume,
                              Optional<ItemStack> remainder, Optional<Holder<SoundEvent>> sound) {
    public static final Codec<CropInteraction> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(CropInteraction::ingredient),
            ResourceKey.codec(IRRegistries.CROP).fieldOf("result").forGetter(CropInteraction::result),
            Codec.BOOL.optionalFieldOf("keep_age", true).forGetter(CropInteraction::keepAge),
            Codec.BOOL.optionalFieldOf("consume", true).forGetter(CropInteraction::consume),
            ItemStack.CODEC.optionalFieldOf("remainder").forGetter(CropInteraction::remainder),
            SoundEvent.CODEC.optionalFieldOf("sound").forGetter(CropInteraction::sound)
    ).apply(inst, CropInteraction::new));

    /**
     * Creates an interaction that keeps the age, consumes the item, has no remainder and plays no sound
     */
    public static CropInteraction of(Ingredient ingredient, ResourceKey<Crop> result) {
        return new CropInteraction(ingredient, result, true, true, Optional.empty(), Optional.empty());
    }

    public CropInteraction withSound(SoundEvent sound) {
        return new CropInteraction(this.ingredient, this.result, this.keepAge, this.consume, this.remainder, Optional.of(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(sound)));
    }

    public CropInteraction withKeepAge(boolean keepAge) {
        return new CropInteraction(this.ingredient, this.result, keepAge, this.consume, this.remainder, this.sound);
    }

    public CropInteraction withConsume(boolean consume) {
        return new CropInteraction(this.ingredient, this.result, this.keepAge, consume, this.remainder, this.sound);
    }

    /**
     * @param remainder given to the player when the item is consumed (e.g. an empty bottle for potions)
     */
    public CropInteraction withRemainder(ItemStack remainder) {
        return new CropInteraction(this.ingredient, this.result, this.keepAge, this.consume, Optional.of(remainder.copy()), this.sound);
    }

    public CropInteraction withRemainder(ItemLike remainder) {
        return this.withRemainder(new ItemStack(remainder));
    }
}
