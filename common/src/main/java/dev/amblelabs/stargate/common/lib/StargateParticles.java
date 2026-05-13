package dev.amblelabs.stargate.common.lib;

import com.mojang.serialization.MapCodec;
import dev.amblelabs.stargate.common.particles.PuddleParticle;
import dev.amblelabs.stargate.common.particles.PuddleParticleOptions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
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
    public static final ParticleType<PuddleParticleOptions> PUDDLE = register("puddle", false,
            PuddleParticleOptions::codec, PuddleParticleOptions::streamCodec);

    private static <T extends ParticleOptions> ParticleType<T> register(String id, boolean overrideLimitter, final Function<ParticleType<T>, MapCodec<T>> codecGetter, final Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> streamCodecGetter) {
        ParticleType<T> particle = new ParticleType<>(overrideLimitter) {
            public MapCodec<T> codec() {
                return codecGetter.apply(this);
            }

            public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
                return streamCodecGetter.apply(this);
            }
        };
        var old = PARTICLES.put(modLoc(id), particle);
        if (old != null) {
            throw new IllegalArgumentException("Typo? Duplicate id " + id);
        }
        return particle;
    }

    public static class FactoryHandler {
        @FunctionalInterface
        public interface Consumer {
            void register(
                    ParticleType<?> type,
                    Function<SpriteSet, ParticleProvider<?>> constructor
            );
        }

        public static void registerFactories(Consumer consumer) {
            consumer.register(PUDDLE, PuddleParticle.Provider::new);
        }
    }
}