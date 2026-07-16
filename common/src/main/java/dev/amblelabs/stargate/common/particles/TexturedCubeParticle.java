package dev.amblelabs.stargate.common.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import org.joml.Vector2f;

@Environment(EnvType.CLIENT)
public abstract class TexturedCubeParticle
        extends CubeParticle {
    protected TextureAtlasSprite sprite;
    protected Vector2f loc;
    private boolean hasMappedUv;
    private float mappedU0;
    private float mappedU1;
    private float mappedV0;
    private float mappedV1;

    protected TexturedCubeParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    protected TexturedCubeParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    protected void setSprite(TextureAtlasSprite sprite) {
        this.sprite = sprite;
        this.updateMappedUv();
    }

    protected void setLoc(Vector2f loc) {
        this.loc = loc;
        this.updateMappedUv();
    }

    private void updateMappedUv() {
        if (this.sprite == null || this.loc == null) {
            this.hasMappedUv = false;
            return;
        }

        float u = Mth.clamp(this.loc.x, 0.0f, 1.0f);
        float v = Mth.clamp(this.loc.y, 0.0f, 1.0f);

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

        this.mappedU0 = Mth.clamp(uCenter - (texelU), uMin, uMax);
        this.mappedU1 = Mth.clamp(uCenter + (texelU), uMin, uMax);
        this.mappedV0 = Mth.clamp(vCenter - (texelV), vMin, vMax);
        this.mappedV1 = Mth.clamp(vCenter + (texelV), vMin, vMax);
        this.hasMappedUv = true;
    }

    @Override
    protected float getU0() {
        return this.hasMappedUv ? this.mappedU0 : this.sprite.getU0();
    }

    @Override
    protected float getU1() {
        return this.hasMappedUv ? this.mappedU1 : this.sprite.getU1();
    }

    @Override
    protected float getV0() {
        return this.hasMappedUv ? this.mappedV0 : this.sprite.getV0();
    }

    @Override
    protected float getV1() {
        return this.hasMappedUv ? this.mappedV1 : this.sprite.getV1();
    }

    public void setSpriteFromAge(SpriteSet sprite) {
        if (!this.removed) {
            this.setSprite(sprite.get(this.age, this.lifetime));
        }
    }
}

