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

    public static final SoundEvent STARGATE_THEME = sound("music.theme");

    public static final SoundEvent WORMHOLE_LOOP = sound("block.stargate.loop");

    public static final SoundEvent TOASTER_DING = sound("block.toaster.ding");
    public static final SoundEvent TOASTER_LOAD = sound("block.toaster.load");
    public static final SoundEvent TOASTER_ACTIVE = sound("block.toaster.active");

    public static final SoundEvent GATE_TELEPORT = sound("block.stargate.tp");
    public static final SoundEvent GATE_OPEN = sound("block.stargate.open");
    public static final SoundEvent GATE_CLOSE = sound("block.stargate.close");
    public static final SoundEvent GATE_FAIL = sound("block.stargate.fail");

    public static final SoundEvent IRIS_HIT = sound("block.stargate.iris.hit");
    public static final SoundEvent IRIS_OPEN = sound("block.stargate.iris.open");
    public static final SoundEvent IRIS_CLOSE = sound("block.stargate.iris.close");

    public static final SoundEvent CHEVRON_LOCK = sound("block.stargate.chevron");

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
