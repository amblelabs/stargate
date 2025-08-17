package dev.amble.stargate.client.renderers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.function.BiFunction;

@Environment(EnvType.CLIENT)
public class StargateRenderLayers extends RenderLayer {
    private StargateRenderLayers(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode,
                            int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction,
                            Runnable endAction) {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }

    private static final BiFunction<Identifier, Boolean, RenderLayer> EMISSIVE_CULL_Z_OFFSET = Util
            .memoize((texture, affectsOutline) -> {
                RenderPhase.Texture texture2 = new RenderPhase.Texture(texture, false, false);
                MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder()
                        .program(RenderPhase.EYES_PROGRAM)
                        .texture(texture2)
                        .cull(DISABLE_CULLING)
                        .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
                        .layering(RenderPhase.VIEW_OFFSET_Z_LAYERING)
                        .lightmap(ENABLE_LIGHTMAP)
                        .writeMaskState(COLOR_MASK)
                        .depthTest(RenderPhase.LEQUAL_DEPTH_TEST)
                        .build(false);
                return RenderLayer.of("emissive_cull_z_offset",
                        VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256,
                        false, true, multiPhaseParameters);
            });

    public static RenderLayer emissiveCullZOffset(Identifier texture, boolean affectsOutline) {
        return EMISSIVE_CULL_Z_OFFSET.apply(texture, affectsOutline);
    }
}
