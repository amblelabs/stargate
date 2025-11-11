package dev.amble.stargate.client.api.behavior.stargate;

import dev.amble.stargate.api.address.Glyph;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.text.OrderedText;
import org.joml.Matrix4f;

public class GlyphCache {

    private static final int MAX_GLYPHS = Glyph.ALL.length;
    private static GlyphCache INSTANCE;

    private final TextRenderer renderer;

    public final OrderedText[] texts = new OrderedText[MAX_GLYPHS];
    public final float[] widths = new float[MAX_GLYPHS];

    private GlyphCache() {
        this.renderer = MinecraftClient.getInstance().textRenderer;

        for (int i = 0; i < MAX_GLYPHS; i++) {
            OrderedText text = Glyph.asText(Glyph.ALL[i]).asOrderedText();

            texts[i] = text;
            widths[i] = renderer.getWidth(text) / -2f;
        }
    }

    public void draw(int index, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumers, TextRenderer.TextLayerType layer, int backgroundColor, int light) {
        renderer.draw(texts[index], widths[index], y, color, shadow, matrix, vertexConsumers, layer, backgroundColor, light);
    }

    public static GlyphCache get() {
        return INSTANCE == null ? INSTANCE = new GlyphCache() : INSTANCE;
    }
}
