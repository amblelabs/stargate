package dev.amble.stargate.util;

import net.minecraft.nbt.*;

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
}
