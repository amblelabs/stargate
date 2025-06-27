package dev.amble.stargate.api;

import dev.amble.lib.register.datapack.SimpleDatapackRegistry;
import dev.amble.stargate.StargateMod;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class GlyphOriginRegistry extends SimpleDatapackRegistry<Glyph> {

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
		return REGISTRY.computeIfAbsent(id, key -> new Glyph(key, Glyph.pickRandom()));
	}

	@Override
	public void reload(ResourceManager manager) {

	}

	private static GlyphOriginRegistry instance;

	public static GlyphOriginRegistry getInstance() {
		return instance == null ? instance = new GlyphOriginRegistry() : instance;
	}
}
