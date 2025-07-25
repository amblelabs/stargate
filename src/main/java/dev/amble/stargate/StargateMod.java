package dev.amble.stargate;

import dev.amble.lib.register.AmbleRegistries;
import dev.amble.stargate.api.PointOfOriginRegistry;
import dev.amble.stargate.core.*;
import dev.amble.lib.container.RegistryContainer;
import dev.amble.stargate.api.StargateServerData;
import dev.amble.stargate.core.entities.DHDControlEntity;
import dev.amble.stargate.core.fluid.StargateFluids;
import dev.amble.stargate.world.StargateConfiguredFeature;
import dev.amble.stargate.world.StargatePlacedFeatures;
import dev.amble.stargate.world.gen.StargateWorldGeneration;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.Registry;
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

		AmbleRegistries.getInstance().registerAll(
				PointOfOriginRegistry.getInstance()
		);

		RegistryContainer.register(StargateItemGroups.class, MOD_ID);
		RegistryContainer.register(StargateItems.class, MOD_ID);
		RegistryContainer.register(StargateBlocks.class, MOD_ID);
		RegistryContainer.register(StargateBlockEntities.class, MOD_ID);
		RegistryContainer.register(StargateEntities.class, MOD_ID);
		RegistryContainer.register(StargateSounds.class, MOD_ID);

		StargateWorldGeneration.generateStargateWorldGen();
		StargateServerData.init();

		entityAttributeRegister();

		StargateFluids.register();
	}

	public void entityAttributeRegister() {
		FabricDefaultAttributeRegistry.register(StargateEntities.DHD_CONTROL_TYPE,
				DHDControlEntity.createDummyAttributes());
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}