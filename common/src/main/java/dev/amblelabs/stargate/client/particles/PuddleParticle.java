package dev.amblelabs.stargate.client.particles;

import dev.amblelabs.stargate.common.particles.PuddleParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;

public class PuddleParticle extends TexturedCubeParticle {

    private final SpriteSet sprites;

    private final float baseQuadSize;
    private final float lifetimeInv;

    protected PuddleParticle(SpriteSet sprites, ClientLevel level, double x, double y, double z, PuddleParticleOptions options) {
        super(sprites, options.loc(), level, x, y, z, 0, 0, 0);
        this.sprites = sprites;

        float f = this.random.nextFloat() * 0.4f + 0.6f;
        this.quadSize = this.baseQuadSize = 0.18f * f;

        this.lifetime += 100;
        this.lifetimeInv = 1.0f / this.lifetime;
        this.alpha = 0.5f;

        // yes, this is mandatory.
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        float progress = ((float) this.age + scaleFactor) * this.lifetimeInv;
        float growth = Mth.clamp(progress * 32.0f, 0.0f, 1.0f);
        float decay = 1.0f - progress * 0.3f;

        return this.baseQuadSize * growth * Math.max(decay, 0.01f);
    }

    @Override
    public void tick() {
        // we replace super.tick() with this, because we don't actually move the particle
        if (this.age++ >= this.lifetime) this.remove();

        this.setSpriteFromAge(this.sprites);
    }

    public static class Provider implements ParticleProvider<PuddleParticleOptions> {

        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(PuddleParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new PuddleParticle(this.sprites, level, x, y, z, type);
        }
    }
}

