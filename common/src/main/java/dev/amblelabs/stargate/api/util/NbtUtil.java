package dev.amblelabs.stargate.api.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class NbtUtil {

    public static String getString(CompoundTag tag, String key, String def) {
        if (tag.contains(key, CompoundTag.TAG_STRING)) return tag.getString(key);
        return def;
    }

    public static String getString(CompoundTag tag, String key, Supplier<String> def) {
        if (tag.contains(key, CompoundTag.TAG_STRING)) return tag.getString(key);
        return def.get();
    }

    public static @Nullable ResourceLocation getLoc(CompoundTag tag, String key) {
        if (tag.contains(key, CompoundTag.TAG_STRING)) return ResourceLocation.parse(tag.getString(key));
        return null;
    }

    public static ResourceLocation getLoc(CompoundTag tag, String key, ResourceLocation def) {
        ResourceLocation loc = getLoc(tag, key);
        return loc != null ? loc : def;
    }

    public static ResourceLocation getLoc(CompoundTag tag, String key, Supplier<ResourceLocation> def) {
        ResourceLocation loc = getLoc(tag, key);
        return loc != null ? loc : def.get();
    }

    public static @Nullable Integer getInt(CompoundTag tag, String key) {
        if (tag.contains(key, CompoundTag.TAG_INT)) return tag.getInt(key);
        return null;
    }

    public static int getInt(CompoundTag tag, String key, int def) {
        Integer res = getInt(tag, key);
        return res != null ? res : def;
    }

    public static int getInt(CompoundTag tag, String key, IntSupplier def) {
        Integer res = getInt(tag, key);
        return res != null ? res : def.getAsInt();
    }

    public static @Nullable Float getFloat(CompoundTag tag, String key) {
        if (tag.contains(key, CompoundTag.TAG_INT)) return tag.getFloat(key);
        return null;
    }

    public static float getFloat(CompoundTag tag, String key, float def) {
        Float res = getFloat(tag, key);
        return res != null ? res : def;
    }

    public static float getFloat(CompoundTag tag, String key, FloatSupplier def) {
        Float res = getFloat(tag, key);
        return res != null ? res : def.getAsFloat();
    }

    @FunctionalInterface
    public interface FloatSupplier {
        float getAsFloat();
    }
}
