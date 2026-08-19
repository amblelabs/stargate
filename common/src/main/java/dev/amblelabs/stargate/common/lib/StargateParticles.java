package dev.amblelabs.stargate.common.lib;

import dev.amblelabs.stargate.client.particles.KawooshParticle;
import dev.amblelabs.stargate.client.particles.PuddleParticle;
import dev.amblelabs.stargate.common.particles.PuddleParticleOptions;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static dev.amblelabs.stargate.api.StargateAPI.modLoc;

@SuppressWarnings("unused")
public class StargateParticles {

    public static void registerParticles(BiConsumer<ParticleType<?>, ResourceLocation> r) {
        for (var e : PARTICLES.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }

    private static final Map<ResourceLocation, ParticleType<?>> PARTICLES = new LinkedHashMap<>();

    //
    public static final ParticleType<PuddleParticleOptions> PUDDLE = register(
            "puddle", new PuddleParticleOptions.Type(false));

    public static final ParticleType<PuddleParticleOptions> KAWOOSH = register(
            "kawoosh", new PuddleParticleOptions.Type(true));

    private static <T extends ParticleOptions> ParticleType<T> register(String id, ParticleType<T> type) {
        ParticleType<?> old = PARTICLES.put(modLoc(id), type);
        if (old != null) throw new IllegalArgumentException("Typo? Duplicate id " + id);

        return type;
    }

    public static class FactoryHandler {

        @FunctionalInterface
        public interface Consumer {
            <T extends ParticleOptions> void register(ParticleType<T> type,
                    Function<SpriteSet, ParticleProvider<T>> constructor);
        }

        public static void registerFactories(Consumer consumer) {
            consumer.register(PUDDLE, PuddleParticle.Provider::new);
            consumer.register(KAWOOSH, KawooshParticle.Provider::new);
        }
    }
}