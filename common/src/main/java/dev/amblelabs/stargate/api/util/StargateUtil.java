package dev.amblelabs.stargate.api.util;

import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.impl.ecs.state.LevelState;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class StargateUtil {

    public static void playSound(Stargate stargate, SoundEvent sound) {
        LevelState state = stargate.stateOrNull(LevelState.state);
        if (state == null) return;

        state.level.playSound(null, state.pos, sound, SoundSource.BLOCKS);
    }
}
