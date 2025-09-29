package dev.amble.stargate.api.address;

import dev.amble.lib.register.datapack.SimpleDatapackRegistry;
import dev.amble.stargate.StargateMod;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GlyphOriginRegistry extends SimpleDatapackRegistry<Glyph> {

	private final Set<Character> unusedCharacters = new HashSet<>();
	private boolean unusedInitialized = false;

	private final Glyph fallback = new Glyph(World.OVERWORLD, 'Q');

	private GlyphOriginRegistry() {
		super(Glyph::fromInputStream, Glyph.CODEC, "point_of_origin", true, StargateMod.MOD_ID);
	}

	@Override
	protected void defaults() {
		register(fallback);
	}

	@Override
	public Glyph fallback() {
		return get(World.OVERWORLD.getValue());
	}

	@Override
	public Glyph get(Identifier id) {
		// TODO: each run will produce a different fallback glyph!
		return REGISTRY.computeIfAbsent(id, key ->
				new Glyph(key, this.pickUnused()));
	}

	private char pickUnused() {
		if (!this.unusedInitialized) {
			for (char glyph : Glyph.ALL) {
				unusedCharacters.add(glyph);
			}

			for (Glyph glyph : REGISTRY.values()) {
				unusedCharacters.remove(glyph.glyph());
			}

			this.unusedInitialized = true;
		}

		Iterator<Character> iterator = unusedCharacters.iterator();
		char unused = iterator.next();
		iterator.remove();

		return unused;
	}

	private static GlyphOriginRegistry instance;

	public static GlyphOriginRegistry getInstance() {
		return instance == null ? instance = new GlyphOriginRegistry() : instance;
	}
}
