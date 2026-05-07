package dev.amblelabs.stargate.mixin.title;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.mod.StargateConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = LogoRenderer.class, priority = 1001)
public class LogoRendererMixin {

    @Unique private static final int LOGO_HEIGHT = 48*2;
    @Unique private static final int LOGO_WIDTH = 128*2;

    @Unique
    private static final ResourceLocation stargate$CUSTOM = StargateAPI.modLoc("textures/gui/title/logo_wide.png");

    @Redirect(method = "renderLogo(Lnet/minecraft/client/gui/GuiGraphics;IFI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V", ordinal = 0))
    public void blit(GuiGraphics instance, ResourceLocation resourceLocation, int i, int j, float f, float g, int k, int l, int m, int n) {
        boolean custom = StargateConfig.client().useCustomMainMenu();

        int x = i;
        int y = j;

        int texWidth = m;
        int texHeight = n;

        int w = k;
        int h = l;

        if (custom) {
            resourceLocation = stargate$CUSTOM;
            y -= LOGO_HEIGHT / 4;

            texWidth = LOGO_WIDTH;
            texHeight = LOGO_HEIGHT;

            w = LOGO_WIDTH;
            h = LOGO_HEIGHT;
        }

        instance.blit(resourceLocation, x, y, f, g, w, h, texWidth, texHeight);
    }

    @Redirect(method = "renderLogo(Lnet/minecraft/client/gui/GuiGraphics;IFI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V", ordinal = 1))
    public void blitEdition(GuiGraphics instance, ResourceLocation resourceLocation, int i, int j, float f, float g, int k, int l, int m, int n) {
        if (!StargateConfig.client().useCustomMainMenu())
            instance.blit(resourceLocation, i, j, f, g, k, l, m, n);
    }
}
