package dev.amblelabs.stargate.common.lib;

import dev.amblelabs.stargate.client.particles.KawooshParticle;
import dev.amblelabs.stargate.client.particles.PuddleParticle;
import dev.amblelabs.stargate.common.particles.PuddleParticleOptions;
import dev.amblelabs.stargate.xplat.XplatAbstractions;
import dev.amblelabs.stargate.xplat.XplatRegister;
import it.unimi.dsi.fastutil.booleans.Boolean2ObjectFunction;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class StargateParticles {

    private static final XplatRegister<ParticleType<?>> REGISTER = XplatAbstractions.INSTANCE.createRegister(BuiltInRegistries.PARTICLE_TYPE);

    public static void register() {
        REGISTER.registerAll();
    }

    public static final Supplier<ParticleType<PuddleParticleOptions>> PUDDLE = type(
            "puddle", false, PuddleParticleOptions.Type::new);

    public static final Supplier<ParticleType<PuddleParticleOptions>> KAWOOSH = type(
            "kawoosh", true, PuddleParticleOptions.Type::new);

    private static <T extends ParticleOptions> Supplier<ParticleType<T>> type(String id, boolean overrideLimiter, Boolean2ObjectFunction<ParticleType<T>> consumer) {
        return type(id, () -> consumer.apply(overrideLimiter));
    }

    private static <T extends ParticleOptions> Supplier<ParticleType<T>> type(String id, Supplier<ParticleType<T>> type) {
        return REGISTER.register(id, type);
    }

    public static class FactoryHandler {

        @FunctionalInterface
        public interface Consumer {
            <T extends ParticleOptions> void register(ParticleType<T> type,
                                                      Function<SpriteSet, ParticleProvider<T>> constructor);

            default <T extends ParticleOptions> void register(Supplier<ParticleType<T>> type,
                                                              Function<SpriteSet, ParticleProvider<T>> constructor) {
                register(type.get(), constructor);
            }
        }

        public static void registerFactories(Consumer consumer) {
            consumer.register(PUDDLE, PuddleParticle.Provider::new);
            consumer.register(KAWOOSH, KawooshParticle.Provider::new);
        }
    }
}