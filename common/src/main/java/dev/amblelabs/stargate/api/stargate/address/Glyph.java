package dev.amblelabs.stargate.api.stargate.address;

import dev.amblelabs.stargate.api.StargateAPI;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public record Glyph(ResourceKey<Level> world, char glyph) {

    public static final int ALPHABET_LENGTH = 36;
    private static final char ALPHABET_START = '7';

    public static final char[] ALL;

    static {
        ALL = new char[ALPHABET_LENGTH];

        for (char i = 0; i < ALPHABET_LENGTH; i++) {
            ALL[i] = (char) (ALPHABET_START + i);
        }
    }

    private static final char ALPHABET_END = ALL[ALPHABET_LENGTH - 1];

//    static {
//        char[] chars = new char[ALPHABET_LENGTH];
//        for (char i = 0; i < ALPHABET_LENGTH; i++) {
//            chars[i] = (char) (i + ALPHABET_START_OFFSET);
//        }
//
//        ALL = chars;
//        ALPHABET = new String(chars);
//    }

    private static final ResourceLocation FONT_ID = StargateAPI.modLoc("stargate");
    private static final Style STYLE = Style.EMPTY.withFont(FONT_ID);

    public static char idxToChar(int idx) {
        return (char) (idx + ALPHABET_START);
    }

    public static int charToIdx(char c) {
        return c - ALPHABET_START;
    }

    public static Component asText(String s) {
        return Component.literal(s).setStyle(STYLE);
    }

    public static Component asText(char c) {
        return Component.literal(String.valueOf(c)).setStyle(STYLE);
    }

    private Glyph(ResourceLocation dimension, String glyph) {
        this(dimension, glyph.charAt(0));
    }

    public Glyph(ResourceLocation dimension, char glyph) {
        this(ResourceKey.create(Registries.DIMENSION, dimension), glyph);
    }

    public Glyph(ResourceKey<Level> world, char glyph) {
        this.world = world;
        this.glyph = validate(glyph);
    }

    // Validate that the input is present in ALL, otherwise return ALL[0].
    public static char validate(char input) {
        return ALPHABET_START >= input && input <= ALPHABET_END ? input : ALPHABET_START;
    }
}