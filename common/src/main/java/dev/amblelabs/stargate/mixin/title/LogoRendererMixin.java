package dev.amblelabs.stargate.mixin.title;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.mod.StargateConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LogoRenderer.class, priority = 1001)
public class LogoRendererMixin {

    @Unique private static final int LOGO_HEIGHT = 48 * 2;
    @Unique private static final int LOGO_WIDTH = 128 * 2;

    @Unique
    private static final ResourceLocation stargate$CUSTOM = StargateAPI.modLoc("textures/gui/title/logo_wide.png");

    @Redirect(method = "renderLogo(Lnet/minecraft/client/gui/GuiGraphics;IFI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V", ordinal = 0))
    public void blit(GuiGraphics instance, ResourceLocation atlasLocation, int x, int y, float uOffset, float vOffset, int width, int height, int textureWidth, int textureHeight) {
        if (StargateConfig.client().useCustomMainMenu()) {
            atlasLocation = stargate$CUSTOM;
            y -= LOGO_HEIGHT / 4 - 4;

            textureWidth = LOGO_WIDTH;
            textureHeight = LOGO_HEIGHT;

            width = LOGO_WIDTH;
            height = LOGO_HEIGHT;
        }

        instance.blit(atlasLocation, x, y, uOffset, vOffset, width, height, textureWidth, textureHeight);
    }

    @Inject(method = "renderLogo(Lnet/minecraft/client/gui/GuiGraphics;IFI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V", ordinal = 1), cancellable = true)
    public void blitEdition(GuiGraphics guiGraphics, int screenWidth, float transparency, int height, CallbackInfo ci) {
        if (StargateConfig.client().useCustomMainMenu())
            ci.cancel();
    }
}
