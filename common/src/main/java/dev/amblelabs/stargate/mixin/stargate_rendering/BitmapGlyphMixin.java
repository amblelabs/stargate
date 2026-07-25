package dev.amblelabs.stargate.mixin.stargate_rendering;

import dev.amblelabs.stargate.client.api.mod.CustomGlyph;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net.minecraft.client.gui.font.providers.BitmapProvider$Glyph")
public abstract class BitmapGlyphMixin implements CustomGlyph {

    @Shadow
    public abstract int width();

    @Shadow
    public abstract int height();

    @Override
    public int stargate$width() {
        return this.width();
    }

    @Override
    public int stargate$height() {
        return this.height();
    }
}
