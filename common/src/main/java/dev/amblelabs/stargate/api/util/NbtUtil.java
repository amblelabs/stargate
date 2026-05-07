package dev.amblelabs.stargate.api.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

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
}
