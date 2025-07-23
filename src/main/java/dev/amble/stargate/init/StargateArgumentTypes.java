package dev.amble.stargate.init;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.command.argumenttypes.StargateArgumentType;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;

import java.util.function.Supplier;

public class StargateArgumentTypes {

    public static void register() {
        register("stargate", StargateArgumentType.class, StargateArgumentType::server);
    }

    private static <T extends ArgumentType<?>> void register(String name, Class<T> t, Supplier<T> supplier) {
        ArgumentTypeRegistry.registerArgumentType(StargateMod.id(name), t,
                ConstantArgumentSerializer.of(supplier));
    }
}
