package dev.amble.stargate.api;

import dev.amble.lib.data.DirectedGlobalPos;
import dev.amble.lib.data.DistanceInformation;
import dev.amble.stargate.StargateMod;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * Represents a Stargate address.
 * 6 Characters Long
 * @param text address in string form
 * @param pos the position of the Stargate
 */
public record Address(String text, DirectedGlobalPos pos) {
	private static final Identifier FONT_ID = StargateMod.id("stargate");
	private static final Style STYLE = Style.EMPTY.withFont(FONT_ID);

	public static final int LENGTH = 7;

	/**
	 * Creates a new address with a random string of characters.
	 * @param pos the position of the Stargate
	 */
	public Address(DirectedGlobalPos pos) {
		this(randomAddress(pos.getDimension().getValue()), pos);
	}

	/**
	 * @return the address in enchanting glyphs
	 */
	public Text asText() {
		return asText(text);
	}

	public static Text asText(String text) {
		return Text.literal(text).fillStyle(STYLE);
	}

	public DistanceInformation distanceTo(Address other) {
		return this.pos.distanceTo(other.pos);
	}

	public NbtCompound toNbt() {
		NbtCompound nbt = new NbtCompound();

		nbt.putString("Text", text);
		nbt.put("Position", pos.toNbt());

		return nbt;
	}

	public static Address fromNbt(NbtCompound nbt) {
		String text = nbt.getString("Text");

		DirectedGlobalPos pos = DirectedGlobalPos.fromNbt(nbt.getCompound("Position"));

		return new Address(text, pos);
	}

	private static String randomAddress(Identifier world) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < LENGTH - 1; i++) {
			builder.append(Glyph.pickRandom());
		}

		builder.append(GlyphOriginRegistry.getInstance().get(world).glyph());
		return builder.toString();
	}
}
