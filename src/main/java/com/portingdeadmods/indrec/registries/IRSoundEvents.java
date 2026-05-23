package com.portingdeadmods.indrec.registries;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class IRSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, IndustrialRecrafted.MODID);

    public static final Supplier<SoundEvent> JETPACK = registerSoundEvent("jetpack");
    public static final Supplier<SoundEvent> EXTRACTOR = registerSoundEvent("extractor");
    public static final Supplier<SoundEvent> MACHINE = registerSoundEvent("machine");

    private static Supplier<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(IndustrialRecrafted.MODID, name)));
    }
}
