package dev.amblelabs.stargate.client.screens;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.common.lib.StargateMusic;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.ChatFormatting;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.GsonHelper;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CreditsScreen extends Screen {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final ResourceLocation VIGNETTE_LOCATION = ResourceLocation.withDefaultNamespace("textures/misc/credits_vignette.png");
    private static final ResourceLocation CREDITS_LOCATION = StargateAPI.modLoc("texts/credits.json");

    private static final Component SECTION_HEADING = Component.literal("============").withStyle(ChatFormatting.WHITE);
    private static final String NAME_PREFIX = "           ";

    private static final float SPEEDUP_FACTOR = 5.0F;
    private static final float SPEEDUP_FACTOR_FAST = 15.0F;

    private final Runnable onFinished;
    private float scroll;

    private final List<FormattedCharSequence> lines = new ArrayList<>();
    private final IntSet centeredLines = new IntOpenHashSet();

    private int totalScrollLength;
    private boolean speedupActive;
    private final IntSet speedupModifiers = new IntOpenHashSet();

    private final float unmodifiedScrollSpeed = 0.75F;
    private float scrollSpeed = this.unmodifiedScrollSpeed;

    private int direction = 1;
    private final LogoRenderer logoRenderer = new CustomLogoRenderer(false);

    public static CreditsScreen createAndClose() {
        Minecraft minecraft = Minecraft.getInstance();
        Screen prevScreen = minecraft.screen;

        return new CreditsScreen(() -> minecraft.setScreen(prevScreen));
    }

    public CreditsScreen(Runnable onFinished) {
        super(GameNarrator.NO_TITLE);

        this.onFinished = onFinished;
    }

    private float calculateScrollSpeed() {
        return this.speedupActive ? this.unmodifiedScrollSpeed * (SPEEDUP_FACTOR + (float) this.speedupModifiers.size() * SPEEDUP_FACTOR_FAST) * (float) this.direction
                : this.unmodifiedScrollSpeed * (float) this.direction;
    }

    @Override
    public void tick() {
        if (this.minecraft == null) return;

        this.minecraft.getMusicManager().tick();
        this.minecraft.getSoundManager().tick(false);

        float f = (float) (this.totalScrollLength + 2 * this.height);

        if (this.scroll > f) {
            this.respawn();
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_UP) {
            this.direction = -1;
        } else if (keyCode != GLFW.GLFW_KEY_LEFT_CONTROL && keyCode != GLFW.GLFW_KEY_RIGHT_CONTROL) {
            if (keyCode == GLFW.GLFW_KEY_SPACE) this.speedupActive = true;
        } else {
            this.speedupModifiers.add(keyCode);
        }

        this.scrollSpeed = this.calculateScrollSpeed();
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_UP) this.direction = 1;

        if (keyCode == GLFW.GLFW_KEY_SPACE) {
            this.speedupActive = false;
        } else if (keyCode == GLFW.GLFW_KEY_LEFT_CONTROL || keyCode == GLFW.GLFW_KEY_RIGHT_CONTROL) {
            this.speedupModifiers.remove(keyCode);
        }

        this.scrollSpeed = this.calculateScrollSpeed();
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public void onClose() {
        this.respawn();
    }

    private void respawn() {
        this.onFinished.run();
    }

    @Override
    protected void init() {
        if (this.minecraft == null) return;

        this.lines.clear();
        this.centeredLines.clear();

        try (Reader reader = this.minecraft.getResourceManager().openAsReader(CREDITS_LOCATION)) {
            this.addCreditsFile(reader);
        } catch (Exception exception) {
            LOGGER.error("Couldn't load credits from file {}", CREDITS_LOCATION, exception);
        }

        this.totalScrollLength = this.lines.size() * 12;
    }

    private void addCreditsFile(Reader reader) {
        for (JsonElement sectionEl : GsonHelper.parseArray(reader)) {
            JsonObject sectionEntry = sectionEl.getAsJsonObject();
            String section = sectionEntry.get("section").getAsString();

            this.addCreditsLine(SECTION_HEADING, true);
            this.addCreditsLine(Component.literal(section).withStyle(ChatFormatting.YELLOW), true);
            this.addCreditsLine(SECTION_HEADING, true);

            this.addEmptyLine();
            this.addEmptyLine();

            for (JsonElement disciplineEl : sectionEntry.getAsJsonArray("disciplines")) {
                JsonObject discipleEntry = disciplineEl.getAsJsonObject();
                String discipline = discipleEntry.get("discipline").getAsString();

                if (StringUtils.isNotEmpty(discipline)) {
                    this.addCreditsLine(Component.literal(discipline).withStyle(ChatFormatting.YELLOW), true);
                    this.addEmptyLine();
                    this.addEmptyLine();
                }

                for (JsonElement titleEl : discipleEntry.getAsJsonArray("titles")) {
                    JsonObject titleEntry = titleEl.getAsJsonObject();
                    String title = titleEntry.get("title").getAsString();
                    JsonArray names = titleEntry.getAsJsonArray("names");
                    this.addCreditsLine(Component.literal(title).withStyle(ChatFormatting.GRAY), false);

                    for (JsonElement name : names) {
                        this.addCreditsLine(Component.literal(NAME_PREFIX)
                                .append(name.getAsString()).withStyle(ChatFormatting.WHITE), false);
                    }

                    this.addEmptyLine();
                    this.addEmptyLine();
                }
            }
        }
    }

    private void addEmptyLine() {
        this.lines.add(FormattedCharSequence.EMPTY);
    }

    private void addCreditsLine(Component creditsLine, boolean centered) {
        if (centered) this.centeredLines.add(this.lines.size());
        this.lines.add(creditsLine.getVisualOrderText());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderVignette(guiGraphics);

        this.scroll = Math.max(0.0F, this.scroll + partialTick * this.scrollSpeed);

        int i = this.width / 2 - 128;
        int j = this.height + 50;
        float f = -this.scroll;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, f, 0.0F);

        this.logoRenderer.renderLogo(guiGraphics, this.width, 1.0F, j);
        int k = j + 100;

        for (int l = 0; l < this.lines.size(); ++l) {
            if (l == this.lines.size() - 1) {
                float g = (float) k + f - (this.height / 2f - 6);
                if (g < 0.0F) {
                    guiGraphics.pose().translate(0.0F, -g, 0.0F);
                }
            }

            if ((float) k + f + 12.0F + 8.0F > 0.0F && (float) k + f < (float) this.height) {
                FormattedCharSequence formattedCharSequence = this.lines.get(l);
                if (this.centeredLines.contains(l)) {
                    guiGraphics.drawCenteredString(this.font, formattedCharSequence, i + 128, k, -1);
                } else {
                    guiGraphics.drawString(this.font, formattedCharSequence, i, k, -1);
                }
            }

            k += 12;
        }

        guiGraphics.pose().popPose();
    }

    private void renderVignette(GuiGraphics guiGraphics) {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR);
        guiGraphics.blit(VIGNETTE_LOCATION, 0, 0, 0, 0.0F, 0.0F, this.width, this.height, this.width, this.height);
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    }

    @Override
    protected void renderMenuBackground(GuiGraphics guiGraphics, int x, int y, int width, int height) {
        float f = this.scroll * 0.5F;
        Screen.renderMenuBackgroundTexture(guiGraphics, Screen.MENU_BACKGROUND, 0, 0, 0.0F, f, width, height);
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    @Override
    public void removed() {
        if (this.minecraft != null)
            this.minecraft.getMusicManager().stopPlaying(StargateMusic.CREDITS);
    }

    @Override
    public Music getBackgroundMusic() {
        return StargateMusic.CREDITS;
    }
}
