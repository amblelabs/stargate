package dev.amble.stargate;

import dev.amble.lib.container.RegistryContainer;
import dev.amble.lib.register.AmbleRegistries;
import dev.amble.stargate.api.StargateServerData;
import dev.amble.stargate.api.address.GlyphOriginRegistryOld;
import dev.amble.stargate.command.StargateDataCommand;
import dev.amble.stargate.command.StargateDialCommand;
import dev.amble.stargate.command.StargateSyncCommand;
import dev.amble.stargate.fluid.StargateFluids;
import dev.amble.stargate.init.*;
import dev.amble.stargate.world.gen.StargateWorldGeneration;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class StargateMod implements ModInitializer {

	public static final String MOD_ID = "stargate";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// FIXME: get rid of this
	public static final Random RANDOM = new Random();

	@Override
	public void onInitialize() {
		AmbleRegistries.getInstance().registerAll(
				GlyphOriginRegistryOld.getInstance()
		);

		StargateArgumentTypes.register();

		CommandRegistrationCallback.EVENT.register((dispatcher, access, env) -> {
			StargateDataCommand.register(dispatcher);
			StargateSyncCommand.register(dispatcher);
			StargateDialCommand.register(dispatcher);
		});

		StargateYAARs.init();
		StargateCriterions.init();

		RegistryContainer.register(StargateItemGroups.class, MOD_ID);
		RegistryContainer.register(StargateItems.class, MOD_ID);
		RegistryContainer.register(StargateBlocks.class, MOD_ID);
		RegistryContainer.register(StargateBlockEntities.class, MOD_ID);
		RegistryContainer.register(StargateEntities.class, MOD_ID);
		RegistryContainer.register(StargateSounds.class, MOD_ID);
		RegistryContainer.register(StargateAttributes.class, MOD_ID);
		RegistryContainer.register(StargateStatusEffects.class, MOD_ID);
		RegistryContainer.register(StargateFluids.class, MOD_ID);

		StargateWorldGeneration.generateStargateWorldGen();
		StargateServerData.init();
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}