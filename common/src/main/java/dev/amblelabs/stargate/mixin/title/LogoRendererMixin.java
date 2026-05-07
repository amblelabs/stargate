package dev.amblelabs.stargate.mixin.title;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.mod.StargateConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.resources.ResourceLocation;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = LogoRenderer.class, priority = 1001)
public class LogoRendererMixin {

    @Shadow
    @Final
    public static ResourceLocation MINECRAFT_LOGO;
    @Unique
    private static final ResourceLocation stargate$CUSTOM = StargateAPI.modLoc("textures/gui/title/minecraft.png");

    @Redirect(method = "renderLogo(Lnet/minecraft/client/gui/GuiGraphics;IFI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V", ordinal = 0))
    public void blit(GuiGraphics instance, ResourceLocation resourceLocation, int i, int j, float f, float g, int k, int l, int m, int n) {
        instance.blit(resourceLocation, i, j, f, g, k, l, m, n + (StargateConfig.client().useCustomMainMenu() ? 14 : 0));
    }

    @Redirect(method = "renderLogo(Lnet/minecraft/client/gui/GuiGraphics;IFI)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/components/LogoRenderer;MINECRAFT_LOGO:Lnet/minecraft/resources/ResourceLocation;", opcode = Opcodes.GETSTATIC))
    public ResourceLocation minecraftLogo() {
        return StargateConfig.client().useCustomMainMenu() ? stargate$CUSTOM : MINECRAFT_LOGO;
    }

    @Redirect(method = "renderLogo(Lnet/minecraft/client/gui/GuiGraphics;IFI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V", ordinal = 1))
    public void blitEdition(GuiGraphics instance, ResourceLocation resourceLocation, int i, int j, float f, float g, int k, int l, int m, int n) {
        if (!StargateConfig.client().useCustomMainMenu())
            instance.blit(resourceLocation, i, j, f, g, k, l, m, n);
    }
}
