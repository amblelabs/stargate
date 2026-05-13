package dev.amblelabs.stargate.common.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.DustParticleBase;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.ParticleOptions;
import org.joml.Vector3f;

@Environment(value=EnvType.CLIENT)
public class PuddleParticle
        extends PuddleParticleBase<PuddleParticleOptions> {

    protected PuddleParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, PuddleParticleOptions options, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, options, sprites);
        float f = this.random.nextFloat() * 0.4f + 0.6f;
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.setColor(options.getRed() * f, options.getGreen() * f, options.getBlue() * f);
        this.setLoc(options.loc());
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        super.render(buffer, camera, partialTicks);
    }

    @Environment(value=EnvType.CLIENT)
    public static class Provider
            implements ParticleProvider<PuddleParticleOptions> {
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

