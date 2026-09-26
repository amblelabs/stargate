package dev.amblelabs.stargate.mixin.title;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.client.screens.CustomLogoRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    @Mutable
    @Shadow
    @Final
    private LogoRenderer logoRenderer;

    @Unique
    private @Nullable LogoRenderer stargate$vanillaLogoRenderer;

    protected TitleScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        this.stargate$vanillaLogoRenderer = this.logoRenderer;
    }

    @Inject(method = "added", at = @At("TAIL"))
    public void added(CallbackInfo ci) {
        if (Objects.requireNonNull(this.minecraft).getResourcePackRepository().getSelectedIds().contains(StargateAPI.PACK_MENU.toString()))
            this.logoRenderer = CustomLogoRenderer.fromVanilla(this.logoRenderer);
        else this.logoRenderer = this.stargate$vanillaLogoRenderer;
    }

    @Inject(method = "mouseClicked", at = @At("RETURN"), cancellable = true)
    public void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ() || !(this.logoRenderer instanceof CustomLogoRenderer custom)) return;

        cir.setReturnValue(custom.mouseClicked(this.width, mouseX, mouseY, button));
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V"))
    public void renderLogoTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (this.logoRenderer instanceof CustomLogoRenderer custom)
            custom.renderTooltip(this.width, mouseX, mouseY);
    }
}
