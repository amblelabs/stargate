package dev.amble.stargate.init;

import dev.amble.lib.container.impl.SoundContainer;
import dev.amble.stargate.StargateMod;
import net.minecraft.sound.SoundEvent;

public class StargateSounds implements SoundContainer {
	public static final SoundEvent GATE_OPEN = create("gate_open");
	public static final SoundEvent GATE_CLOSE = create("gate_close");
	public static final SoundEvent GATE_FAIL = create("gate_fail");
	public static final SoundEvent GATE_TELEPORT = create("gate_teleport");
	public static final SoundEvent WORMHOLE_LOOP = create("wormhole_loop");
	public static final SoundEvent CHEVRON_LOCK = create("chevron_lock");
	public static final SoundEvent RING_LOOP = create("ring_loop");
	public static final SoundEvent RING_START = create("ring_start");
	public static final SoundEvent DHD_PRESS = create("dhd_press");

	// Iris
	public static final SoundEvent IRIS_HIT = create("iris_hit");
	public static final SoundEvent IRIS_CLOSE = create("iris_close");
	public static final SoundEvent IRIS_OPEN = create("iris_open");

	public static SoundEvent create(String name) {
		return SoundEvent.of(StargateMod.id(name));
	}
}
