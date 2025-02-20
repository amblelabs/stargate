package dev.amble.stargate;

import dev.amble.stargate.api.PointOfOriginRegistry;
import dev.amble.stargate.core.*;
import dev.amble.lib.container.RegistryContainer;
import dev.amble.stargate.api.StargateServerData;
import dev.amble.stargate.core.entities.DHDControlEntity;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
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

		StargateServerData.init();

		entityAttributeRegister();

		PointOfOriginRegistry.getInstance().register();
	}

	public void entityAttributeRegister() {
		FabricDefaultAttributeRegistry.register(StargateEntities.DHD_CONTROL_TYPE,
				DHDControlEntity.createDummyAttributes());
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}