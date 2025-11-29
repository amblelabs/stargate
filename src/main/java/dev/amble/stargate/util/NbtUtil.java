package dev.amble.stargate.util;

import net.minecraft.nbt.*;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NbtUtil {

    private static boolean isOf(NbtElement element, int type) {
        int i = element == null ? NbtElement.END_TYPE : element.getType();
        return i == type || (type == NbtElement.NUMBER_TYPE && NbtElement.BYTE_TYPE <= i && i <= NbtElement.DOUBLE_TYPE);
    }

    public static long getLong(NbtCompound nbt, String key, long def) {
        NbtElement l = nbt.get(key);

        if (l != null && isOf(l, NbtElement.NUMBER_TYPE)) {
            try {
                return ((AbstractNbtNumber) l).longValue();
            } catch (ClassCastException ignored) { }
        }

        return def;
    }

    @SuppressWarnings("DataFlowIssue")
    public static @Nullable Identifier getId(NbtCompound nbt, String key) {
        return getId(nbt, key, null);
    }

    public static @NotNull Identifier getId(NbtCompound nbt, String key, Identifier def) {
        NbtElement l = nbt.get(key);

        if (l != null && isOf(l, NbtElement.STRING_TYPE)) {
            try {
                return new Identifier(l.asString());
            } catch (ClassCastException ignored) { }
        }

        return def;
    }

    public static void putId(NbtCompound nbt, String key, Identifier id) {
        nbt.putString(key, id.toString());
    }

    public static <T> RegistryKey<T> getRegistryKey(NbtCompound nbt, String key, Registry<T> registry, RegistryKey<T> def) {
        Identifier id = getId(nbt, key);

        if (id == null)
            return def;

        return RegistryKey.of(registry.getKey(), id);
    }

    public static <T> @NotNull RegistryKey<T> getRegistryKey(NbtCompound nbt, String key, Registry<T> registry, Identifier def) {
        Identifier id = getId(nbt, key, def);
        return RegistryKey.of(registry.getKey(), id);
    }

    public static <T> @NotNull RegistryKey<T> getRegistryKey(NbtCompound nbt, String key, DefaultedRegistry<T> registry) {
        return getRegistryKey(nbt, key, registry, registry.getDefaultId());
    }

    public static <T> @Nullable RegistryKey<T> getRegistryKey(NbtCompound nbt, String key, Registry<T> registry) {
        return getRegistryKey(nbt, key, registry, (RegistryKey<T>) null);
    }

    public static <T> void putRegistryKey(NbtCompound nbt, String key, RegistryKey<T> registryKey) {
        putId(nbt, key, registryKey.getValue());
    }

    public static <T> @Nullable T getRegistered(NbtCompound nbt, String key, Registry<T> registry, Identifier def) {
        RegistryKey<T> regKey = getRegistryKey(nbt, key, registry, def);
        return registry.get(regKey);
    }

    public static <T> @Nullable T getRegistered(NbtCompound nbt, String key, DefaultedRegistry<T> registry) {
        return getRegistered(nbt, key, registry, registry.getDefaultId());
    }

    public static <T> @Nullable T getRegistered(NbtCompound nbt, String key, Registry<T> registry, RegistryKey<T> def) {
        RegistryKey<T> regKey = getRegistryKey(nbt, key, registry, def);
        return registry.get(regKey);
    }

    public static <T> @NotNull T getRegistered(NbtCompound nbt, String key, Registry<T> registry, RegistryEntry<T> def) {
        return getRegistered(nbt, key, registry, def.value());
    }

    public static <T> @NotNull T getRegistered(NbtCompound nbt, String key, Registry<T> registry, T def) {
        RegistryKey<T> regKey = getRegistryKey(nbt, key, registry);
        if (regKey == null) return def;

        T t = registry.get(regKey);
        return t != null ? t : def;
    }

    @SuppressWarnings("DataFlowIssue")
    public static <T> @Nullable T getRegistered(NbtCompound nbt, String key, Registry<T> registry) {
        return getRegistered(nbt, key, registry, (T) null);
    }

    public static <T> @Nullable RegistryEntry<T> getRegistryEntry(NbtCompound nbt, String key, Registry<T> registry, Identifier def) {
        RegistryKey<T> regKey = getRegistryKey(nbt, key, registry, def);
        return registry.getEntry(regKey).orElse(null);
    }

    public static <T> @Nullable RegistryEntry<T> getRegistryEntry(NbtCompound nbt, String key, DefaultedRegistry<T> registry) {
        return getRegistryEntry(nbt, key, registry, registry.getDefaultId());
    }

    public static <T> @Nullable RegistryEntry<T> getRegistryEntry(NbtCompound nbt, String key, Registry<T> registry, RegistryKey<T> def) {
        RegistryKey<T> regKey = getRegistryKey(nbt, key, registry, def);
        return registry.getEntry(regKey).orElse(null);
    }

    public static <T> @NotNull RegistryEntry<T> getRegistryEntry(NbtCompound nbt, String key, Registry<T> registry, RegistryEntry<T> def) {
        RegistryKey<T> regKey = getRegistryKey(nbt, key, registry);
        return regKey == null ? def : registry.getEntry(regKey).map(o -> (RegistryEntry<T>) o).orElse(def);
    }

    @SuppressWarnings("DataFlowIssue")
    public static <T> @Nullable RegistryEntry<T> getRegistryEntry(NbtCompound nbt, String key, Registry<T> registry) {
        return getRegistryEntry(nbt, key, registry, (RegistryEntry<T>) null);
    }

    public static <T> void putRegistryEntry(NbtCompound nbt, String key, RegistryEntry<T> entry) {
        putRegistryKey(nbt, key, entry.getKey().get());
    }
}
