package dev.amblelabs.stargate.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class CubeParticle extends Particle {

    private static final float[][] VERTICES = {
            {-1, -1, -1}, // 0
            { 1, -1, -1}, // 1
            { 1,  1, -1}, // 2
            {-1,  1, -1}, // 3
            {-1, -1,  1}, // 4
            { 1, -1,  1}, // 5
            { 1,  1,  1}, // 6
            {-1,  1,  1}  // 7
    };

    private static final int[][] FACES = {
            {5, 6, 7, 4},
            {0, 3, 2, 1},
            {4, 7, 3, 0},
            {1, 2, 6, 5},
            {6, 2, 3, 7},
            {1, 5, 4, 0}
    };

    private static final int LIGHT = 0xF000F0;

    protected float quadSize;

    private final Vector3f tempPos = new Vector3f();
    private final Quaternionf rotation = new Quaternionf();

    protected CubeParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.quadSize = (this.random.nextFloat() * 0.5f + 0.5f) * 0.2f;
    }

    public float getQuadSize(float scaleFactor) {
        return this.quadSize;
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        this.rotation.identity();
        if (this.roll != 0.0f) {
            this.rotation.rotateZ(Mth.lerp(partialTicks, this.oRoll, this.roll));
        }

        Vec3 cameraPos = camera.getPosition();
        float x = (float) (Mth.lerp(partialTicks, this.xo, this.x) - cameraPos.x());
        float y = (float) (Mth.lerp(partialTicks, this.yo, this.y) - cameraPos.y());
        float z = (float) (Mth.lerp(partialTicks, this.zo, this.z) - cameraPos.z());

        float size = this.getQuadSize(partialTicks);

        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();

        float r = this.rCol;
        float g = this.gCol;
        float b = this.bCol;
        float a = this.alpha;

        for (int[] face : FACES) {
            {
                float[] v = VERTICES[face[0]];
                tempPos.set(v[0] * size, v[1] * size, v[2] * size);
                tempPos.rotate(this.rotation);
                tempPos.add(x, y, z);
                buffer.addVertex(tempPos.x(), tempPos.y(), tempPos.z())
                        .setUv(u0, v1)
                        .setColor(r, g, b, a)
                        .setLight(LIGHT);
            }
            {
                float[] v = VERTICES[face[1]];
                tempPos.set(v[0] * size, v[1] * size, v[2] * size);
                tempPos.rotate(this.rotation);
                tempPos.add(x, y, z);
                buffer.addVertex(tempPos.x(), tempPos.y(), tempPos.z())
                        .setUv(u0, v0)
                        .setColor(r, g, b, a)
                        .setLight(LIGHT);
            }
            {
                float[] v = VERTICES[face[2]];
                tempPos.set(v[0] * size, v[1] * size, v[2] * size);
                tempPos.rotate(this.rotation);
                tempPos.add(x, y, z);
                buffer.addVertex(tempPos.x(), tempPos.y(), tempPos.z())
                        .setUv(u1, v0)
                        .setColor(r, g, b, a)
                        .setLight(LIGHT);
            }
            {
                float[] v = VERTICES[face[3]];
                tempPos.set(v[0] * size, v[1] * size, v[2] * size);
                tempPos.rotate(this.rotation);
                tempPos.add(x, y, z);
                buffer.addVertex(tempPos.x(), tempPos.y(), tempPos.z())
                        .setUv(u1, v1)
                        .setColor(r, g, b, a)
                        .setLight(LIGHT);
            }
        }
    }

    // technically, not used, but override just-in-case. for performance.
    @Override
    protected int getLightColor(float partialTick) {
        return LIGHT;
    }

    // technically, not used, but override just-in-case. for performance.
    @Override
    public Particle scale(float scale) {
        this.quadSize *= scale;
        return super.scale(scale);
    }

    protected abstract float getU0();
    protected abstract float getU1();
    protected abstract float getV0();
    protected abstract float getV1();
}

