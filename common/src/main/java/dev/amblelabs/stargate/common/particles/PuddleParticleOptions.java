package dev.amblelabs.stargate.common.particles;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ScalableParticleOptionsBase;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.FastColor;
import org.joml.Vector2f;

public class PuddleParticleOptions extends ScalableParticleOptionsBase implements ParticleOptions {

    private final ParticleType<PuddleParticleOptions> type;

    private final int color;
    private final Vector2f loc;

    public static MapCodec<PuddleParticleOptions> codec(ParticleType<PuddleParticleOptions> particleType) {
        return RecordCodecBuilder.mapCodec((instance) -> instance.group(
                ExtraCodecs.ARGB_COLOR_CODEC.fieldOf("color").forGetter(options -> options.color),
                Codec.FLOAT.fieldOf("u").forGetter(options -> options.loc().x),
                Codec.FLOAT.fieldOf("v").forGetter(options -> options.loc().y)
        ).apply(instance, (color, u, v) ->
                new PuddleParticleOptions(particleType, color, u, v)));
    }

    public static StreamCodec<? super ByteBuf, PuddleParticleOptions> streamCodec(ParticleType<PuddleParticleOptions> type) {
        return StreamCodec.composite(
                ByteBufCodecs.INT, options -> options.color,
                ByteBufCodecs.FLOAT, options -> options.loc().x,
                ByteBufCodecs.FLOAT, options -> options.loc().y, (color, u, v) ->
                        new PuddleParticleOptions(type, color, u, v)
        );
    }

    public PuddleParticleOptions(ParticleType<PuddleParticleOptions> type, int color, Vector2f loc) {
        super(1);

        this.type = type;
        this.color = color;
        this.loc = loc;
    }

    public PuddleParticleOptions(ParticleType<PuddleParticleOptions> type, int color, float x, float y) {
        this(type, color, new Vector2f(x, y));
    }

    public ParticleType<PuddleParticleOptions> getType() {
        return this.type;
    }

    public float getRed() {
        return (float)FastColor.ARGB32.red(this.color) / 255.0f;
    }

    public float getGreen() {
        return (float)FastColor.ARGB32.green(this.color) / 255.0f;
    }

    public float getBlue() {
        return (float)FastColor.ARGB32.blue(this.color) / 255.0f;
    }

    public float getAlpha() {
        return (float)FastColor.ARGB32.alpha(this.color) / 255.0f;
    }

    public Vector2f loc() {
        return this.loc;
    }

    public static PuddleParticleOptions create(ParticleType<PuddleParticleOptions> type, int color, Vector2f loc) {
        return new PuddleParticleOptions(type, color, loc);
    }

    public static PuddleParticleOptions create(ParticleType<PuddleParticleOptions> type, float red, float green, float blue, Vector2f loc) {
        return PuddleParticleOptions.create(type, FastColor.ARGB32.colorFromFloat(1.0f, red, green, blue), loc);
    }

    public static class Type extends ParticleType<PuddleParticleOptions> {

        public Type(boolean overrideLimitter) {
            super(overrideLimitter);
        }

        @Override
        public MapCodec<PuddleParticleOptions> codec() {
            return PuddleParticleOptions.codec(this);
        }

        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, PuddleParticleOptions> streamCodec() {
            return PuddleParticleOptions.streamCodec(this);
        }
    }
}

