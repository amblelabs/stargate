package dev.amblelabs.stargate.api.util;

import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.impl.ecs.state.LevelState;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class StargateUtil {

    public static void playSound(Stargate stargate, Holder<SoundEvent> sound) {
        LevelState state = stargate.stateOrNull(LevelState.state);
        if (state == null) return;

        SoundUtil.playSound(state.level(), state.pos(), sound, SoundSource.BLOCKS);
    }
}
