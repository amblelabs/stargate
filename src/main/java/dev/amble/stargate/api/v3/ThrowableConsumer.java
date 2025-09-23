package dev.amble.stargate.api.v3;

public interface ThrowableConsumer<T> {
    void apply(T obj) throws Throwable;
}
