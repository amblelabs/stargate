package dev.pavatus.stargate;

import dev.pavatus.lib.container.RegistryContainer;
import dev.pavatus.stargate.core.*;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class StargateMod implements ModInitializer {
	public static final String MOD_ID = "stargate";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Random RANDOM = new Random();

	@Override
	public void onInitialize() {
		RegistryContainer.register(StargateItems.class, MOD_ID);
		RegistryContainer.register(StargateItems.Groups.class, MOD_ID);
		RegistryContainer.register(StargateBlocks.class, MOD_ID);
		RegistryContainer.register(StargateBlockEntities.class, MOD_ID);
		RegistryContainer.register(StargateEntities.class, MOD_ID);
		RegistryContainer.register(StargateSounds.class, MOD_ID);
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}