package dev.amble.stargate.api.util;

import java.util.function.Supplier;

public class NonNull {

    public static <R> R get(R r, Supplier<R> s) {
        return r != null ? r : s.get();
    }

    public static <R> R get(R r, R s) {
        return r != null ? r : s;
    }
}
