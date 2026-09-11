package dev.amblelabs.stargate.api.stargate.address;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.amblelabs.stargate.api.StargateAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.*;

public class GlyphGen {

    public record Glyph(Part[] parts) { }

    public record Register(ResourceLocation loc, List<Part> parts, int width, int height, int size) {

        public static Register from(ResourceLocation location, int size) throws IOException {
            List<Part> parts = new ArrayList<>();

            try (InputStream stream = Minecraft.getInstance().getResourceManager().getResource(location).orElseThrow().open();
                 NativeImage image = NativeImage.read(stream)) {
                for (int x = 0; x < image.getWidth() / size; x++) {
                    for (int y = 0; y < image.getHeight() / size; y++) {
                        parts.add(new Part(x, y));
                    }
                }

                // TODO: wrap instead of copying
                return new Register(location, List.copyOf(parts),
                        image.getWidth(), image.getHeight(), size);
            }
        }
    }

    public record Definition(@Nullable Glyph[] cache, List<Register> registers, int total) {

        private static int getTotal(Collection<Register> registers) {
            int total = 0;
            for (Register register : registers) {
                total += register.parts.size();
            }

            return total;
        }

        public static Definition create(List<Register> registers) {
            int total = getTotal(registers);
            int combs = (int) CombinationUnranking.getCombinations(total, registers.size());

            StargateAPI.LOGGER.info("Created definition: {} combinations", combs);

            return new Definition(new Glyph[combs], registers, total);
        }

        public Glyph getOrCreateGlyph(int idx) {
            if (this.cache[idx] != null)
                return Objects.requireNonNull(this.cache[idx]);

            int[] indices = CombinationUnranking.unrankCombination(
                    this.total, this.registers.size(), BigInteger.valueOf(idx));

            StargateAPI.LOGGER.info("Creating glyph #{}: {}", idx, Arrays.toString(indices));

            Part[] parts = new Part[indices.length];

            for (int i = 0; i < indices.length; i++) {
                parts[i] = this.registers.get(i).parts().get(indices[i]);
            }

            return this.cache[idx] = new Glyph(parts);
        }

        public void renderGlyph(int idx, PoseStack pose) {
            Glyph glyph = this.getOrCreateGlyph(idx);

            Part[] parts = glyph.parts;
            for (int i = 0; i < parts.length; i++) {
                Register register = this.registers.get(i);
                int size = register.size;
                Part part = parts[i];

                float minU = (float) (part.x * size) / register.width;
                float minV = (float) (part.y * size) / register.height;
                float maxU = (float) ((part.x + 1) * size) / register.width;
                float maxV = (float) ((part.y + 1) * size) / register.height;

                RenderSystem.setShaderTexture(0, register.loc);
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                Matrix4f matrix4f = pose.last().pose();
                BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                builder.addVertex(matrix4f, 0, 0, 0).setUv(minU, minV);
                builder.addVertex(matrix4f, 0, size, 0).setUv(minU, maxV);
                builder.addVertex(matrix4f, size, size, 0).setUv(maxU, maxV);
                builder.addVertex(matrix4f, size, 0, 0).setUv(maxU, minV);

                BufferUploader.drawWithShader(builder.buildOrThrow());
            }
        }
    }

    public record Part(int x, int y) { }
}
