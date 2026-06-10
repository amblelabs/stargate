package dev.amblelabs.stargate.common.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@Environment(value=EnvType.CLIENT)
public abstract class CubeParticle
        extends Particle {
    protected float quadSize;

    protected CubeParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.quadSize = 0.1f * (this.random.nextFloat() * 0.5f + 0.5f) * 2.0f;
    }

    protected CubeParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.quadSize = 0.1f * (this.random.nextFloat() * 0.5f + 0.5f) * 2.0f;
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        Quaternionf quaternionf = new Quaternionf();
        if (this.roll != 0.0f) {
            quaternionf.rotateZ(Mth.lerp(partialTicks, this.oRoll, this.roll));
        }
        this.renderRotatedQuad(buffer, camera, quaternionf, partialTicks);
    }

    protected void renderRotatedQuad(VertexConsumer buffer, Camera camera, Quaternionf quaternion, float partialTicks) {
        Vec3 vec3 = camera.getPosition();
        float f = (float)(Mth.lerp(partialTicks, this.xo, this.x) - vec3.x());
        float g = (float)(Mth.lerp(partialTicks, this.yo, this.y) - vec3.y());
        float h = (float)(Mth.lerp(partialTicks, this.zo, this.z) - vec3.z());
        this.renderCube(buffer, quaternion, f, g, h, partialTicks);
    }

    protected void renderCube(VertexConsumer buffer, Quaternionf quaternion,
                              float x, float y, float z, float partialTicks) {

        float s = this.getQuadSize(partialTicks);

        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();

        int light = this.getLightColor(partialTicks);

        // Cube vertices
        Vector3f[] verts = {
                new Vector3f(-s, -s, -s), // 0
                new Vector3f( s, -s, -s), // 1
                new Vector3f( s,  s, -s), // 2
                new Vector3f(-s,  s, -s), // 3
                new Vector3f(-s, -s,  s), // 4
                new Vector3f( s, -s,  s), // 5
                new Vector3f( s,  s,  s), // 6
                new Vector3f(-s,  s,  s)  // 7
        };

        // Faces (quad indices)
        int[][] faces = {
                {5, 6, 7, 4}, // Front
                {0, 3, 2, 1}, // Back
                {4, 7, 3, 0}, // Left
                {1, 2, 6, 5}, // Right
                {6, 2, 3, 7}, // Top
                {1, 5, 4, 0}  // Bottom
        };

        // UVs per vertex
        float[][] uvs = {
                {u0, v1},
                {u0, v0},
                {u1, v0},
                {u1, v1}
        };

        for (int[] face : faces) {
            for (int i = 0; i < 4; i++) {
                Vector3f v = verts[face[i]];

                this.renderVertex(
                        buffer,
                        quaternion,
                        x, y, z,
                        v.x(), v.y(), v.z(),
                        uvs[i][0], uvs[i][1],
                        light
                );
            }
        }
    }

    private void renderVertex(VertexConsumer buffer,
                              Quaternionf quaternion,
                              float x, float y, float z,
                              float vx, float vy, float vz,
                              float u, float v,
                              int packedLight) {

        Vector3f pos = new Vector3f(vx, vy, vz)
                .rotate(quaternion)
                .add(x, y, z);

        buffer.addVertex(pos.x(), pos.y(), pos.z())
                .setUv(u, v)
                .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                .setLight(0xf000f0); // set to max light
    }

    public float getQuadSize(float scaleFactor) {
        return this.quadSize;
    }

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

