package dev.amble.stargate.mixins;



import dev.amble.stargate.StargateMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.LogoDrawer;
import net.minecraft.util.Identifier;



@Mixin(LogoDrawer.class)
public class DefaultLogoMixin {

    @Unique private static final Identifier STARGATE_LOGO = StargateMod.id("textures/gui/title/stargate_logo.png");
    @Unique private final MinecraftClient client = MinecraftClient.getInstance();

    @Redirect(method = "draw(Lnet/minecraft/client/gui/DrawContext;IFI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIFFIIII)V", ordinal = 0))
    private void stargate$drawCustomLogo(DrawContext context, Identifier texture, int x, int y, float u, float v, int width,
                                    int height, int textureWidth, int textureHeight) {

        Identifier currentLogo;

            currentLogo = STARGATE_LOGO;


//        if (!AITModClient.CONFIG.customMenu) {
//            context.drawTexture(texture, x, y, u, v, width, height, textureWidth, textureHeight);
//            return;
//        }

        int screenWidth = this.client.getWindow().getScaledWidth();
        int centerX = screenWidth / 2 - 128;

        context.drawTexture(currentLogo, centerX, y - 18, 0.0f, 0.0f, 266, 94, 266, 94);

    }

    @Redirect(method = "draw(Lnet/minecraft/client/gui/DrawContext;IFI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIFFIIII)V", ordinal = 1))
    private void stargate$skipEdition(DrawContext context, Identifier texture, int x, int y, float u, float v, int width,
                                 int height, int textureWidth, int textureHeight) {
            context.drawTexture(texture, x, y, u, v, width, height, textureWidth, textureHeight);
    }


}
