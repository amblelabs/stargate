package dev.amblelabs.stargate.common.lib;

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

    private static <O extends ParticleOptions, T extends ParticleType<O>> T register(String id, T particle) {
        var old = PARTICLES.put(modLoc(id), particle);
        if (old != null) {
            throw new IllegalArgumentException("Typo? Duplicate id " + id);
        }
        return particle;
    }

    public static class FactoryHandler {
        @FunctionalInterface
        public interface Consumer<T extends ParticleOptions> {
            void register(ParticleType<T> type,
                Function<SpriteSet, ParticleProvider<T>> constructor);
        }

        @SuppressWarnings({"EmptyMethod", "unused"})
        public static <T extends ParticleOptions> void registerFactories(Consumer<T> consumer) {

        }
    }
}