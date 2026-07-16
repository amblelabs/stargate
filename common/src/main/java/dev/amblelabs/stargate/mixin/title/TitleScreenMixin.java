package dev.amblelabs.stargate.mixin.title;

import dev.amblelabs.stargate.client.screens.CustomLogoRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    @Shadow
    @Final
    private LogoRenderer logoRenderer;

    @Unique
    private CustomLogoRenderer stargate$customLogoRenderer;

    protected TitleScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "added", at = @At("TAIL"))
    public void added(CallbackInfo ci) {
        if (Minecraft.getInstance().getResourcePackRepository().getSelectedIds().contains("stargate:menu"))
            this.stargate$customLogoRenderer = new CustomLogoRenderer(((LogoRendererAccessor) this.logoRenderer).getKeepLogoThroughFade());
    }

    @Inject(method = "mouseClicked", at = @At("RETURN"), cancellable = true)
    public void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ() || this.stargate$customLogoRenderer == null) return;

        cir.setReturnValue(this.stargate$customLogoRenderer.mouseClicked(this.width, mouseX, mouseY, button));
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/LogoRenderer;renderLogo(Lnet/minecraft/client/gui/GuiGraphics;IF)V"))
    public void render(LogoRenderer instance, GuiGraphics guiGraphics, int screenWidth, float transparency) {
        if (this.stargate$customLogoRenderer != null)
            instance = stargate$customLogoRenderer;

        instance.renderLogo(guiGraphics, screenWidth, transparency);
    }
}
