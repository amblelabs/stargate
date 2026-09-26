package dev.amblelabs.stargate.neoforge.xplat;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.neoforge.NeoForgeStargateInit;
import dev.amblelabs.stargate.xplat.XplatRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class NeoForgeRegistrar<B> implements XplatRegistrar<B> {
    private final String modId;
    private final Function<String, DeferredRegister<B>> creator;
    private final Map<String, DeferredRegister<B>> registers = new HashMap<>();

    protected NeoForgeRegistrar(String modId, Function<String, DeferredRegister<B>> creator) {
        this.modId = modId;
        this.creator = creator;
        this.registers.put(modId, creator.apply(modId));
    }

    public NeoForgeRegistrar(ResourceKey<Registry<B>> registryKey) {
        this(StargateAPI.MOD_ID, modId -> DeferredRegister.create(registryKey, modId));
    }

    public NeoForgeRegistrar(Registry<B> registry) {
        this(StargateAPI.MOD_ID, modId -> DeferredRegister.create(registry, modId));
    }

    protected DeferredRegister<B> get(String modId) {
        return this.registers.computeIfAbsent(modId, this.creator);
    }

    protected DeferredRegister<B> get(ResourceLocation loc) {
        return this.get(loc.getNamespace());
    }

    @Override
    public <T extends B> Supplier<T> register(String id, Supplier<T> provider) {
        return get(this.modId).register(id, provider);
    }

    @Override
    public <T extends B> Supplier<T> register(ResourceLocation loc, Supplier<T> supplier) {
        return get(loc).register(loc.getPath(), supplier);
    }

    @Override
    public <T extends B> Holder<B> registerHolder(String id, Supplier<T> provider) {
        return get(this.modId).register(id, provider);
    }

    @Override
    public <T extends B> Holder<B> registerHolder(ResourceLocation loc, Supplier<T> supplier) {
        return get(loc).register(loc.getPath(), supplier);
    }

    @Override
    public void registerAll() {
        registers.forEach((modId, register) ->
                register.register(NeoForgeStargateInit.EVENT_BUS));
    }
}