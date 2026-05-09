package dev.amblelabs.stargate.mixin.title;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.mod.StargateConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.client.renderer.PanoramaRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = PanoramaRenderer.class, priority = 1001)
public class PanoramaRendererMixin {

    @Unique
    private static final CubeMap stargate$CUSTOM = new CubeMap(StargateAPI.modLoc("textures/gui/title/background/panorama"));

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/CubeMap;render(Lnet/minecraft/client/Minecraft;FFF)V"))
    public void blit(CubeMap instance, Minecraft minecraft, float f, float g, float h) {
        CubeMap map = StargateConfig.client().useCustomMainMenu() ? stargate$CUSTOM : instance;
        map.render(minecraft, f, g, h);
    }
}
