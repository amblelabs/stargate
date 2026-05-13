package dev.amblelabs.stargate.common.particles;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ScalableParticleOptionsBase;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.FastColor;

public class PuddleParticleOptions extends ScalableParticleOptionsBase
        implements ParticleOptions {
    private final ParticleType<PuddleParticleOptions> type;
    private final int color;

    public static MapCodec<PuddleParticleOptions> codec(ParticleType<PuddleParticleOptions> particleType) {
        return ExtraCodecs.ARGB_COLOR_CODEC.xmap(integer -> new PuddleParticleOptions(particleType, (int)integer), colorParticleOption -> colorParticleOption.color).fieldOf("color");
    }

    public static StreamCodec<? super ByteBuf, PuddleParticleOptions> streamCodec(ParticleType<PuddleParticleOptions> type) {
        return ByteBufCodecs.INT.map(integer -> new PuddleParticleOptions(type, (int)integer), colorParticleOption -> colorParticleOption.color);
    }

    private PuddleParticleOptions(ParticleType<PuddleParticleOptions> type, int color) {
        super(1);
        this.type = type;
        this.color = color;
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

    public static PuddleParticleOptions create(ParticleType<PuddleParticleOptions> type, int color) {
        return new PuddleParticleOptions(type, color);
    }

    public static PuddleParticleOptions create(ParticleType<PuddleParticleOptions> type, float red, float green, float blue) {
        return PuddleParticleOptions.create(type, FastColor.ARGB32.colorFromFloat(1.0f, red, green, blue));
    }
}

