package dev.amble.stargate.api.address;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.amble.lib.api.Identifiable;
import dev.amble.stargate.StargateMod;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicReference;

public record Glyph(RegistryKey<World> world, char glyph) implements Identifiable {

    public static final Codec<Glyph> CODEC = Codecs.exceptionCatching(RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("dimension").forGetter(g -> g.world.getValue()),
            Codec.STRING.fieldOf("glyph").forGetter(symbol -> String.valueOf(symbol.glyph()))
    ).apply(instance, Glyph::new)));

    public static final int ALPHABET_LENGTH = 36;

    private static final char ALPHABET_START_OFFSET = '#';
    public static final String ALPHABET = "ABCDEFASDHAKJSDHAJDHAJSDH";
    public static final char[] ALL = ALPHABET.toCharArray();

//    static {
//        char[] chars = new char[ALPHABET_LENGTH];
//        for (char i = 0; i < ALPHABET_LENGTH; i++) {
//            chars[i] = (char) (i + ALPHABET_START_OFFSET);
//        }
//
//        ALL = chars;
//        ALPHABET = new String(chars);
//    }

    private static final Identifier FONT_ID = StargateMod.id("stargate");
    private static final Style STYLE = Style.EMPTY.withFont(FONT_ID);

    public static char idxToChar(int idx) {
        return (char) (ALPHABET_START_OFFSET + idx);
    }

    public static int charToIdx(char c) {
        return c - ALPHABET_START_OFFSET;
    }

    public static Text asText(String s) {
        return Text.literal(s).setStyle(STYLE);
    }

    public static Text asText(char c) {
        return Text.literal(String.valueOf(c)).setStyle(STYLE);
    }

    private Glyph(Identifier dimension, String glyph) {
        this(dimension, glyph.charAt(0));
    }

    public Glyph(Identifier dimension, char glyph) {
        this(RegistryKey.of(RegistryKeys.WORLD, dimension), glyph);
    }

    public Glyph(RegistryKey<World> world, char glyph) {
        this.world = world;
        this.glyph = validate(glyph);
    }

    // Validate that the input is present in ALL, otherwise return ALL[0].
    public static char validate(char input) {
        for (char c : ALL) {
            if (c == input) return c;
        }
        return ALL[0];
    }

    public static Glyph fromInputStream(InputStream stream) {
        return fromJson(JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject());
    }

    public static Glyph fromJson(JsonObject json) {
        AtomicReference<Glyph> created = new AtomicReference<>();

        CODEC.decode(JsonOps.INSTANCE, json).get().ifLeft(planet -> created.set(planet.getFirst())).ifRight(err -> {
            created.set(null);
            StargateMod.LOGGER.error("Error decoding datapack symbol: {}", err);
        });

        return created.get();
    }

    @Override
    public Identifier id() {
        return world.getValue();
    }
}
