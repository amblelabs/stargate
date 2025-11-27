package dev.amble.stargate.client.screen;

import dev.amble.stargate.StargateMod;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class DialingComputerScreen extends Screen {

    private static final Identifier TEXTURE = StargateMod.id("textures/gui/dialing_computer.png");

    private static final int bgHeight = 154;
    private static final int bgWidth = 256;

    private static final int bgBorder = 9;

    public DialingComputerScreen() {
        super(Text.translatable("screen.stargate.computer.title"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int top = (this.height - bgHeight) / 2;
        int left = (this.width - bgWidth) / 2;

        context.drawTexture(TEXTURE, left, top, 0, 0, bgWidth, bgHeight);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    // close when E is pressed
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }

        if (client.options.inventoryKey.matchesKey(keyCode, scanCode)) {
            this.close();
            return true;
        }

        return false;
    }
}
