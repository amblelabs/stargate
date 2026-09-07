package dev.amblelabs.stargate.xplat;

import net.minecraft.core.Holder;

import java.util.function.Supplier;

public interface XplatRegister<B> {

    <T extends B> Supplier<T> register(String id, Supplier<T> provider);

    <T extends B> Holder<B> registerHolder(String id, Supplier<T> provider);

    void registerAll();
}
