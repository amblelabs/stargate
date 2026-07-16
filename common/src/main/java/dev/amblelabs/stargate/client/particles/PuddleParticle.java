package dev.amblelabs.stargate.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.amblelabs.stargate.common.particles.PuddleParticleOptions;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;

public class PuddleParticle extends TexturedCubeParticle {

    private final SpriteSet sprites;

    protected PuddleParticle(SpriteSet sprites, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, PuddleParticleOptions options) {
        super(sprites, options.loc(), level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.sprites = sprites;

        float f = this.random.nextFloat() * 0.4f + 0.6f;
        this.quadSize = 0.18f * f;

        this.lifetime += 100;
        this.setAlpha(0.5f);

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
        return this.quadSize * Mth.clamp(((float) this.age + scaleFactor) / (float)this.lifetime * 32.0f, 0.0f, 1.0f);
    }

    @Override
    public void tick() {
        super.tick();

        this.setSpriteFromAge(this.sprites);
        this.scale(0.999f);
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        super.render(buffer, camera, partialTicks);
    }

    public static class Provider implements ParticleProvider<PuddleParticleOptions> {

        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(PuddleParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new PuddleParticle(this.sprites, level, x, y, z, xSpeed, ySpeed, zSpeed, type);
        }
    }
}

