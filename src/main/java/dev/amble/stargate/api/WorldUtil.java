package dev.amble.stargate.api;

import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

// TODO: move into Amble
public class WorldUtil {

	public static Text worldText(RegistryKey<World> key) {
		return Text.translatableWithFallback(key.getValue().toTranslationKey("dimension"), fakeTranslate(key));
	}

	private static String fakeTranslate(RegistryKey<World> id) {
		return fakeTranslate(id.getValue());
	}

	private static String fakeTranslate(Identifier id) {
		return fakeTranslate(id.getPath());
	}

	private static String fakeTranslate(String path) {
		// Split the string into words
		String[] words = path.split("_");

		// Capitalize the first letter of each word
		for (int i = 0; i < words.length; i++) {
			words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1).toLowerCase();
		}

		// Join the words back together with spaces
		return String.join(" ", words);
	}
}
