package dev.amblelabs.stargate.common.lib;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static dev.amblelabs.stargate.api.StargateAPI.modLoc;

public class StargateSounds {
    public static void registerSounds(BiConsumer<SoundEvent, ResourceLocation> r) {
        for (var e : SOUNDS.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }

    private static final Map<ResourceLocation, SoundEvent> SOUNDS = new LinkedHashMap<>();

    // Sounds!!

    private static SoundEvent sound(String name) {
        var id = modLoc(name);
        var sound = SoundEvent.createVariableRangeEvent(id);
        var old = SOUNDS.put(id, sound);
        if (old != null) {
            throw new IllegalArgumentException("Typo? Duplicate id " + name);
        }
        return sound;
    }
}
