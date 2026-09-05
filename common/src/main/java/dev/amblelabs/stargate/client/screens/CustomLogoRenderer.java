package dev.amblelabs.stargate.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.amblelabs.stargate.common.I18n;
import dev.amblelabs.stargate.mixin.title.LogoRendererAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetTooltipHolder;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import org.lwjgl.glfw.GLFW;

public class CustomLogoRenderer extends LogoRenderer {

    private static final Tooltip TOOLTIP = Tooltip.create(I18n.LOGO_TOOLTIP);

    private static final int LOGO_HEIGHT = 96;
    private static final int LOGO_Y = DEFAULT_HEIGHT_OFFSET / 4;

    private static final int LOGO_PADDING = 8;

    private static final int START_Y = LOGO_Y + LOGO_PADDING;
    private static final int END_Y = LOGO_Y + LOGO_HEIGHT - LOGO_PADDING;

    private final boolean keepLogoThroughFade;

    private final WidgetTooltipHolder tooltip = new WidgetTooltipHolder();

    public static CustomLogoRenderer fromVanilla(LogoRenderer renderer) {
        return new CustomLogoRenderer(((LogoRendererAccessor) renderer).getKeepLogoThroughFade());
    }

    public CustomLogoRenderer(boolean keepLogoThroughFade) {
        super(keepLogoThroughFade);

        this.keepLogoThroughFade = keepLogoThroughFade;
        this.tooltip.set(TOOLTIP);
    }

    public void renderTooltip(int screenWidth, int mouseX, int mouseY) {
        this.tooltip.refreshTooltipForNextRenderPass(this.isHovered(screenWidth, mouseX, mouseY), false,
                ScreenRectangle.empty());
    }

    @Override
    public void renderLogo(GuiGraphics guiGraphics, int screenWidth, float transparency) {
        this.renderLogo(guiGraphics, screenWidth, transparency, LOGO_Y);
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

        if (this.isHovered(screenWidth, mouseX, mouseY)) {
            Minecraft.getInstance().setScreen(CreditsScreen.createAndClose());
            return true;
        }

        return false;
    }

    private boolean isHovered(int screenWidth, double mouseX, double mouseY) {
        int x = (screenWidth - LOGO_WIDTH) / 2;

        int startX = x + LOGO_PADDING;
        int endX = x + LOGO_WIDTH - LOGO_PADDING;

        return mouseX >= startX && mouseX <= endX && mouseY >= START_Y && mouseY <= END_Y;
    }
}
