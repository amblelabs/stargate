package dev.amblelabs.stargate.api.util;

import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.impl.ecs.state.LevelState;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class StargateUtil {

    @Contract(pure = true)
    public static @Nullable BlockState getBlockState(Stargate stargate) {
        LevelState state = stargate.stateOrNull(LevelState.state);
        if (state == null) return null;

        return getBlockState(state);
    }

    public static BlockState getBlockState(LevelState state) {
        return state.level().getBlockState(state.pos());
    }

    @CheckReturnValue
    public static boolean playSound(Stargate stargate, Holder<SoundEvent> sound) {
        LevelState state = stargate.stateOrNull(LevelState.state);
        if (state == null) return false;

        playSound(state, sound);
        return true;
    }

    public static void playSound(LevelState phys, Holder<SoundEvent> sound) {
        SoundUtil.playSound(phys.level(), phys.pos(), sound, SoundSource.BLOCKS);
    }

    @CheckReturnValue
    public static boolean playSound(Stargate stargate, SoundEvent sound) {
        LevelState state = stargate.stateOrNull(LevelState.state);
        if (state == null) return false;

        playSound(state, sound);
        return true;
    }

    public static void playSound(LevelState phys, SoundEvent sound) {
        SoundUtil.playSound(phys.level(), phys.pos(), sound, SoundSource.BLOCKS);
    }

    public static Collection<Player> getPlayersNearby(Stargate stargate, int radius) {
        LevelState globalPos = stargate.stateOrNull(LevelState.state);
        if (globalPos == null) return List.of();

        AABB aabb = AABB.ofSize(globalPos.pos().getCenter(), radius, radius, radius);

        //noinspection DataFlowIssue - can be null, actually
        return globalPos.level().getNearbyPlayers(TargetingConditions.forNonCombat(), null, aabb);
    }
}
