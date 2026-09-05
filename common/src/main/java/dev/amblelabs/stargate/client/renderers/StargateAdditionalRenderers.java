package dev.amblelabs.stargate.client.renderers;

import dev.amblelabs.stargate.api.mod.StargateConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class StargateAdditionalRenderers {

    private static final Minecraft minecraft = Minecraft.getInstance();

    private static final ResourceLocation FONT = ResourceLocation.fromNamespaceAndPath("amblekit", "amblestone");
    private static final ResourceLocation FONT_SMALL = ResourceLocation.fromNamespaceAndPath("amblekit", "amblestonelite");

    public static void overlayGui(GuiGraphics graphics, DeltaTracker tracker) {
        if (!StargateConfig.client().renderOverlay()) return;

        final Component title = Component.literal("STARGATE").withStyle(style -> style.withFont(FONT));
        final Component edition = Component.literal(": sojourner").withStyle(style -> style.withFont(FONT_SMALL));
        final Component wip = Component.literal("(work in progress)").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);

        final int padding = 2;

        final Font font = minecraft.font;
        graphics.drawString(font, title, padding, padding, 0xFFFFFFFF);

        graphics.drawString(font, edition, padding + font.width(title) - 4, padding, 0xFFFFFF);
        graphics.drawString(font, wip, graphics.guiWidth() - font.width(wip) - padding, graphics.guiHeight() - font.lineHeight - padding, 0xFFFFFF);
    }
}
