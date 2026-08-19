package dev.amblelabs.stargate.client.particles;

import dev.amblelabs.stargate.common.particles.PuddleParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;

public class KawooshParticle extends TexturedCubeParticle {

    private static final float MAX_ALPHA = 0.85f;

    private final SpriteSet sprites;
    private final float baseQuadSize;

    protected KawooshParticle(SpriteSet sprites, ClientLevel level, double x, double y, double z,
                              double xSpeed, double ySpeed, double zSpeed, PuddleParticleOptions options) {
        super(sprites, options.loc(), level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.sprites = sprites;

        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;

        float f = this.random.nextFloat() * 0.4f + 0.7f;
        this.quadSize = this.baseQuadSize = 0.16f * f;

        this.lifetime = 7 + this.random.nextInt(6);
        this.alpha = 0.0f;

        int color = options.color();
        this.rCol = FastColor.ARGB32.red(color) / 255.0f;
        this.gCol = FastColor.ARGB32.green(color) / 255.0f;
        this.bCol = FastColor.ARGB32.blue(color) / 255.0f;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        float t = (this.age + scaleFactor) / this.lifetime;
        float grow = Mth.clamp(t * 6.0f, 0.0f, 1.0f);
        float shrink = 1.0f - Mth.clamp((t - 0.6f) / 0.4f, 0.0f, 1.0f);
        return this.baseQuadSize * grow * shrink;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }

        this.setPos(this.x + this.xd, this.y + this.yd, this.z + this.zd);

        this.xd *= 0.88;
        this.yd *= 0.88;
        this.zd *= 0.88;

        float t = this.age / (float) this.lifetime;
        float fade;
        if (t < 0.25f) fade = t / 0.25f;
        else if (t > 0.55f) fade = 1.0f - (t - 0.55f) / 0.45f;
        else fade = 1.0f;
        this.alpha = Mth.clamp(fade, 0.0f, 1.0f) * MAX_ALPHA;

        this.setSpriteFromAge(this.sprites);
    }

    public static class Provider implements ParticleProvider<PuddleParticleOptions> {

        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(PuddleParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new KawooshParticle(this.sprites, level, x, y, z, xSpeed, ySpeed, zSpeed, type);
        }
    }
}
