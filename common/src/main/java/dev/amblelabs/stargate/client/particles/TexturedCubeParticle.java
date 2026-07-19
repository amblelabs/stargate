package dev.amblelabs.stargate.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import org.joml.Vector2f;

public abstract class TexturedCubeParticle extends CubeParticle {

    protected TextureAtlasSprite sprite;
    protected Vector2f loc;

    private float mappedU0;
    private float mappedU1;
    private float mappedV0;
    private float mappedV1;

    protected TexturedCubeParticle(SpriteSet sprite, Vector2f loc, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        this.sprite = sprite.get(0, this.lifetime);
        this.loc = loc;

        this.updateMappedUv();
    }

    public void setSpriteFromAge(SpriteSet sprite) {
        if (!this.removed) {
            this.sprite = sprite.get(this.age, this.lifetime);
            this.updateMappedUv();
        }
    }

    private void updateMappedUv() {
        float u = Mth.clamp(this.loc.x, 0, 1);
        float v = Mth.clamp(this.loc.y, 0, 1);

        float uMin = this.sprite.getU0();
        float uMax = this.sprite.getU1();
        float vMin = this.sprite.getV0();
        float vMax = this.sprite.getV1();

        float uSpan = uMax - uMin;
        float vSpan = vMax - vMin;
        float texelU = uSpan / 16.0f;
        float texelV = vSpan / 16.0f;

        float uCenter = uMin + (uSpan * u);
        float vCenter = vMin + (vSpan * v);

        this.mappedU0 = Mth.clamp(uCenter - texelU, uMin, uMax);
        this.mappedU1 = Mth.clamp(uCenter + texelU, uMin, uMax);
        this.mappedV0 = Mth.clamp(vCenter - texelV, vMin, vMax);
        this.mappedV1 = Mth.clamp(vCenter + texelV, vMin, vMax);
    }

    @Override
    protected float getU0() {
        return this.mappedU0;
    }

    @Override
    protected float getU1() {
        return this.mappedU1;
    }

    @Override
    protected float getV0() {
        return this.mappedV0;
    }

    @Override
    protected float getV1() {
        return this.mappedV1;
    }
}

