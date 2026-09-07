package dev.amblelabs.stargate.api.util;

import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.impl.ecs.state.LevelState;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.util.Collection;
import java.util.List;

public class StargateUtil {

    public static void playSound(Stargate stargate, Holder<SoundEvent> sound) {
        LevelState state = stargate.stateOrNull(LevelState.state);
        if (state == null) return;

        SoundUtil.playSound(state.level(), state.pos(), sound, SoundSource.BLOCKS);
    }

    public static Collection<Player> getPlayersNearby(Stargate stargate, int radius) {
        LevelState globalPos = stargate.stateOrNull(LevelState.state);
        if (globalPos == null) return List.of();

        globalPos.level().playSound(null, globalPos.pos(), SoundEvents.CHAIN_BREAK, SoundSource.BLOCKS);

        // TODO: iris break event
        AABB aabb = AABB.ofSize(globalPos.pos().getCenter(), radius, radius, radius);
        return globalPos.level().getNearbyPlayers(TargetingConditions.forNonCombat(), null, aabb);
    }
}
