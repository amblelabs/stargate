package dev.amblelabs.stargate.mixin.title;

import net.minecraft.client.gui.components.LogoRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LogoRenderer.class)
public interface LogoRendererAccessor {
    @Accessor
    boolean getKeepLogoThroughFade();
}
