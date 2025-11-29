package dev.amble.stargate.util;

import java.util.function.Consumer;
import java.util.function.Function;

public class LambdaUtil {

    public static <A, B extends A> Consumer<A> cast(Consumer<B> c) {
        return (a -> c.accept((B) a));
    }
}
