package dev.amblelabs.stargate.api.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SoundUtil {

    public static void playSound(Level level, BlockPos pos, Holder<SoundEvent> sound, SoundSource source) {
        playSound(level, pos, sound, source, 1);
    }

    public static void playSound(Level level, BlockPos pos, Holder<SoundEvent> sound, SoundSource source, float volume) {
        playSound(level, pos, sound, source, volume, 1);
    }

    public static void playSound(Level level, BlockPos pos, Holder<SoundEvent> sound, SoundSource source, float volume, float pitch) {
        Vec3 vec = pos.getCenter();
        playSound(level, vec.x, vec.y, vec.z, sound, source, volume, pitch);
    }

    public static void playSound(Level level, double x, double y, double z, Holder<SoundEvent> sound, SoundSource source) {
        playSound(level, x, y, z, sound, source, 1);
    }

    public static void playSound(Level level, double x, double y, double z, Holder<SoundEvent> sound, SoundSource source, float volume) {
        playSound(level, x, y, z, sound, source, volume, 1);
    }

    public static void playSound(Level level, double x, double y, double z, Holder<SoundEvent> sound, SoundSource source, float volume, float pitch) {
        level.playSound(null, x, y, z, sound, source, volume, pitch);
    }
}
