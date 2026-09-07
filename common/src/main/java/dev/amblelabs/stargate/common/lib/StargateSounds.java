package dev.amblelabs.stargate.common.lib;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.xplat.XplatAbstractions;
import dev.amblelabs.stargate.xplat.XplatRegister;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

public class StargateSounds {

    private static final XplatRegister<SoundEvent> REGISTER = XplatAbstractions.INSTANCE.createRegister(BuiltInRegistries.SOUND_EVENT);

    public static void register() {
        REGISTER.registerAll();
    }

    public static final Holder<SoundEvent> STARGATE_THEME = sound("music.theme");

    public static final Holder<SoundEvent> WORMHOLE_LOOP = sound("block.stargate.loop");

    public static final Holder<SoundEvent> TOASTER_DING = sound("block.toaster.ding");
    public static final Holder<SoundEvent> TOASTER_LOAD = sound("block.toaster.load");
    public static final Holder<SoundEvent> TOASTER_ACTIVE = sound("block.toaster.active");

    public static final Holder<SoundEvent> GATE_TELEPORT = sound("block.stargate.tp");
    public static final Holder<SoundEvent> GATE_OPEN = sound("block.stargate.open");
    public static final Holder<SoundEvent> GATE_CLOSE = sound("block.stargate.close");
    public static final Holder<SoundEvent> GATE_FAIL = sound("block.stargate.fail");

    public static final Holder<SoundEvent> IRIS_HIT = sound("block.stargate.iris.hit");
    public static final Holder<SoundEvent> IRIS_OPEN = sound("block.stargate.iris.open");
    public static final Holder<SoundEvent> IRIS_CLOSE = sound("block.stargate.iris.close");

    public static final Holder<SoundEvent> CHEVRON_LOCK = sound("block.stargate.chevron");

    private static Holder<SoundEvent> sound(String name) {
        return REGISTER.registerHolder(name, () -> SoundEvent.createVariableRangeEvent(StargateAPI.modLoc(name)));
    }
}
