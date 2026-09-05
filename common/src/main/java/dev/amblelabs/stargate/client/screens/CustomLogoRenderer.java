package dev.amblelabs.stargate.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LogoRenderer;
import org.lwjgl.glfw.GLFW;

public class CustomLogoRenderer extends LogoRenderer {

    private static final int LOGO_HEIGHT = 96;

    private final boolean keepLogoThroughFade;

    public CustomLogoRenderer(boolean keepLogoThroughFade) {
        super(keepLogoThroughFade);

        this.keepLogoThroughFade = keepLogoThroughFade;
    }

    @Override
    public void renderLogo(GuiGraphics guiGraphics, int screenWidth, float transparency) {
        this.renderLogo(guiGraphics, screenWidth, transparency, 30 - LOGO_HEIGHT / 4 + 4);
    }

    @Override
    public void renderLogo(GuiGraphics guiGraphics, int screenWidth, float transparency, int height) {
        guiGraphics.setColor(1, 1, 1, this.keepLogoThroughFade ? 1 : transparency);
        RenderSystem.enableBlend();

        int x = (screenWidth - LOGO_WIDTH) / 2;
        guiGraphics.blit(MINECRAFT_LOGO, x, height, 0, 0, LOGO_WIDTH, LOGO_HEIGHT, LOGO_WIDTH, LOGO_HEIGHT);

        guiGraphics.setColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
    }

    public boolean mouseClicked(int screenWidth, double mouseX, double mouseY, int button) {
        if (button != GLFW.GLFW_MOUSE_BUTTON_1) return false;

        int x = (screenWidth - LOGO_WIDTH) / 2;
        int y = 30 - LOGO_HEIGHT / 4 + 4;

        if (mouseX >= x && mouseX <= x + LOGO_WIDTH && mouseY >= y && mouseY <= y + LOGO_HEIGHT) {
            Minecraft.getInstance().setScreen(CreditsScreen.createAndClose());
            return true;
        }

        return false;
    }
}
