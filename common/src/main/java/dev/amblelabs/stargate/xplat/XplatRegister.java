package dev.amblelabs.stargate.xplat;

import dev.amblelabs.stargate.api.StargateAPI;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public interface XplatRegister<B> {

    default <T extends B> Supplier<T> register(String id, Supplier<T> provider) {
        return register(StargateAPI.modLoc(id), provider);
    }

    <T extends B> Supplier<T> register(ResourceLocation id, Supplier<T> provider);

    default <T extends B> Holder<B> registerHolder(String id, Supplier<T> provider) {
        return registerHolder(StargateAPI.modLoc(id), provider);
    }

    <T extends B> Holder<B> registerHolder(ResourceLocation id, Supplier<T> provider);

    void registerAll();
}
