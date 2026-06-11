package dev.amblelabs.stargate.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.amblelabs.stargate.common.particles.PuddleParticleOptions;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;

public class PuddleParticle extends PuddleParticleBase {

    protected PuddleParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, PuddleParticleOptions options, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);

        this.xd = 0;
        this.yd = 0;
        this.zd = 0;

        this.setLoc(options.loc());
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
            return new PuddleParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type, this.sprites);
        }
    }
}

